/*******************************************************************************
 * Copyright 2014 Time Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.timeInc.ark.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.timeInc.ark.application.Application;
import com.timeInc.ark.application.ApplicationEntity.ApplicationVisitor;
import com.timeInc.ark.application.CdsApplication;
import com.timeInc.ark.application.DpsApplication;
import com.timeInc.ark.application.ScpApplication;
import com.timeInc.ark.dao.ApplicationDAO;
import com.timeInc.ark.naming.FileNaming;
import com.timeInc.ark.naming.VelocityNaming;
import com.timeInc.ark.packager.SingleNamingPackager;
import com.timeInc.ark.parser.content.IdentifyingContent;
import com.timeInc.ark.parser.preview.RegexPreviewParser;
import com.timeInc.ark.parser.preview.RegexPreviewParser.Val;
import com.timeInc.ark.response.CommonAppResponse;
import com.timeInc.ark.response.DpsResponse;
import com.timeInc.ark.response.ScpResponse;
import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.ark.upload.producer.ContentProducer;
import com.timeInc.ark.uploader.DpsServer;
import com.timeInc.ark.uploader.ScpServer;
import com.timeInc.ark.util.ExtJsJsonSerializer;
import com.timeInc.ark.util.TemplateEmail;

/**
 * GetAppDataServlet is a class to retrieve data for an application 
 */

public class GetAppDataServlet extends PermissionServlet {
	private static final String APPLICATION_ID = "appId";

	private static final long serialVersionUID = 1L;
	@Override
	protected void handleGet(HttpServletRequest req, HttpServletResponse resp,  User user) throws ServletException, IOException {
		resp.setContentType ("application/json; charset=UTF-8");
		ApplicationDAO appdao = ApplicationDAO.Factory.getDao(user.getRole());
		final Integer appId = Integer.valueOf(req.getParameter(APPLICATION_ID));
		Application<?, ?> app=appdao.getById(appId).get(0);

 CommonAppResponse appResponse= app.accept(new ApplicationVisitor<CommonAppResponse>() {
		
				@Override
				public CommonAppResponse visit(CdsApplication app) {
					 final CommonAppResponse response=new CommonAppResponse();
					 response.setType("cds");
					// TODO Auto-generated method stub
					return response;
				}

				@Override
				public CommonAppResponse visit(DpsApplication app) {
					// TODO Auto-generated method stub
					 final DpsResponse response=new DpsResponse();
					
					DpsServer server = app.getServer();
					
					response.setType("dps");
					response.setUserName(server.getDpsUsername());
					response.setPassword(server.getDpsPassword());
					response.setDpsConsumerKey(server.getConsumerKey());
					response.setDpsConsumerSecret(server.getConsumerSecret());
					response.setDpsUrl(server.getAddress());
					response.setDpsRetryCount(String.valueOf(server.getTimesToRetry()));
					response.setRendition(app.getRendition().name());					
					return response;

				}

				@Override
				public CommonAppResponse visit(ScpApplication app) {
					 final ScpResponse response=new ScpResponse();
					 ScpServer server = app.getServer();
					 response.setType("scp");
					 response.setUserName(server.getCred().getUsername());
					 response.setPassword((server.getCred().getPassword()));
					 response.setServer(server.getCred().getHost());
					 
					 String dirPath = ((VelocityNaming)server.getPathNaming().getUnderlyingNaming()).getTemplate();
					 response.setDirpath(dirPath);
					 response.setPort(server.getCred().getPort());
					 ContentProducer<?, ?> producer = (ContentProducer<?, ?>) app.getContentProducer();
					 IdentifyingContent<?> idParser = (IdentifyingContent<?>) producer.getParser();
					 response.setContentParserId(idParser.getId());
					 response.setContentType(idParser.getType());					 
					 SingleNamingPackager packager = (SingleNamingPackager) producer.getPackager();
					 FileNaming naming = packager.getNaming();
					 VelocityNaming velNaming = (VelocityNaming) naming.getUnderlyingNaming();
					 response.setPathNaming(velNaming.getTemplate());

					 RegexPreviewParser parser=(RegexPreviewParser) app.getPreviewProducer().getParser();
					 Map<String,Val> map =parser.getExpectedImages();
					 SingleNamingPackager pack= (SingleNamingPackager) app.getPreviewProducer().getPackager();
					 FileNaming previewNaming;
					if (pack!=null){
						 previewNaming=pack.getNaming();
						 VelocityNaming vn=(VelocityNaming)previewNaming.getUnderlyingNaming();
						 response.setPreviewNaming(vn.getTemplate());
					}

					return response;
				}
				});	
 		appResponse.setId(String.valueOf(appId));
		if (app.isRequireContentFirst())
			appResponse.setRequireContentFirst(true);
		else appResponse.setRequireContentFirst(false);
		if (app.isSendMsg())
			appResponse.setSendMsg(true);
		else appResponse.setSendMsg(false);
		if (app.isActive())
			appResponse.setActive(true);
		else appResponse.setActive(false);
		appResponse.setDefaultPrice(app.getDefaultPrice().toString());
		appResponse.setProductId(app.getProductId());
		appResponse.setPubShortName(app.getVendorPubName());
		appResponse.setId(req.getParameter(APPLICATION_ID));
		if (app.getPreviewEmail()!=null){
			TemplateEmail pe = app.getPreviewEmail();
			appResponse.setPreviewEmail(true);
			appResponse.setPreviewRecepients(pe.getReceipients());
			appResponse.setPreviewEmailSubject(((VelocityNaming) pe.getSubject()).getTemplate());
			appResponse.setPreviewEmailBody(((VelocityNaming) pe.getBody()).getTemplate());						
		} else
			appResponse.setPreviewEmail(false);
		if (app.getContentEmail()!=null){
			appResponse.setContentEmail(true);
			appResponse.setContentRecepients(app.getContentEmail().getReceipients());
			VelocityNaming velNaming = (VelocityNaming)app.getContentEmail().getSubject();
			appResponse.setContentEmailSubject(velNaming.getTemplate());
			velNaming =(VelocityNaming)app.getContentEmail().getBody();
			appResponse.setContentEmailBody(velNaming.getTemplate());																		
		} else 
			appResponse.setContentEmail(false);
 		String jsonResult =ExtJsJsonSerializer.success(appResponse);
		resp.getWriter().write(jsonResult);
		
	}
	@Override
	protected List<Role> getEntitledRoles() {
		return Role.getRolesWithSameOrHigher(Role.SuperAdmin);
	}	

}
