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

/**
 * EditDpsAppServlet is a class for updating and creating Dps Application.
 */

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.reflect.TypeToken;
import com.timeInc.ark.application.DpsApplication;
import com.timeInc.ark.dao.ApplicationDAO;
import com.timeInc.ark.dao.PublicationGroupDAO;
import com.timeInc.ark.dao.TemplateEmailDAO;
import com.timeInc.ark.naming.VelocityNaming;
import com.timeInc.ark.packager.preview.DpsPreviewPackager;
import com.timeInc.ark.parser.preview.RegexPreviewParser;
import com.timeInc.ark.parser.preview.RegexPreviewParser.Val;
import com.timeInc.ark.request.DpsAppRequest;
import com.timeInc.ark.request.ImagePreviewRequest;
import com.timeInc.ark.response.ErrorMessage;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.ark.upload.producer.PreviewProducer.DpsPreviewProducer;
import com.timeInc.ark.uploader.DpsServer;
import com.timeInc.ark.util.ExtJsJsonDeserializer;
import com.timeInc.ark.util.ExtJsJsonSerializer;
import com.timeInc.ark.util.TemplateEmail;
import com.timeInc.dps.producer.enums.Viewer;
public class EditDpsAppServlet extends PermissionServlet{

	private static final long serialVersionUID = 1L;
	private static final String PARAM_ACTION = "write";  // the request parameter name
	private static final String ACTION_VALUE_UPDATE = "update";
	private static final String ACTION_VALUE_CREATE = "create";
	private static final String PARAM_JSON_DATA = "data";
	private static final String PARAM_APPLICATION_ID = "appId"; 
	private static final String PARAM_PUBLICATION_ID = "pubId";

	
	@Override
	protected void handlePost(HttpServletRequest request, HttpServletResponse response,  User user) throws ServletException, IOException {
		String appId= request.getParameter(PARAM_APPLICATION_ID);
		String crud = request.getParameter(PARAM_ACTION);
		String json = request.getParameter(PARAM_JSON_DATA);
		ApplicationDAO appdao =ApplicationDAO.Factory.getDao(user.getRole());
		List<DpsAppRequest> dpsList=ExtJsJsonDeserializer.asType(new TypeToken<List<DpsAppRequest>>(){}.getType(), json);			
		DpsServer server = null;
		DpsApplication app=null;
		DpsAppRequest dps = null;
		TemplateEmail temp;
		if (dpsList!=null && dpsList.size()>0 && !crud.isEmpty()){
			dps=dpsList.get(0);
			List<ImagePreviewRequest> imagePreviews=ExtJsJsonDeserializer.asType(new TypeToken<List<ImagePreviewRequest>>(){}.getType(),dps.getImagePreviews());
			if(crud.equals(ACTION_VALUE_UPDATE)){
				app=(DpsApplication) appdao.getById(Integer.valueOf(appId)).get(0);
				server=app.getServer();
				doSettingForApp(server,dps, app);
				TemplateEmailDAO emailDAO=new TemplateEmailDAO();
				if (dps.getContentEmail()){
					TemplateEmail contentEmail = app.getContentEmail();
					if (contentEmail==null)
						contentEmail= new TemplateEmail();
					setContentEmailTemplate(contentEmail,dps);
					app.setContentEmail(contentEmail);					
				}else if (dps.getContentEmail()==false) {
					if (app.getContentEmail()!=null){
						temp=app.getContentEmail();
						app.setContentEmail(null);
						emailDAO.delete(temp);
					}
				}
				if (dps.getPreviewEmail()){
					TemplateEmail previewEmail=app.getPreviewEmail();
					if (previewEmail==null)
						previewEmail=new TemplateEmail();					
					setPreviewEmailTemplate(previewEmail,dps);
					app.setPreviewEmail(previewEmail);					
				}else if (dps.getPreviewEmail()==false){
					if (app.getPreviewEmail()!=null){
						temp=app.getPreviewEmail();
						app.setPreviewEmail(null);
						emailDAO.delete(temp);
					}
				}
			}else
				if(crud.equals(ACTION_VALUE_CREATE)&& isPreviewValid(imagePreviews)){  //two previewImages are required
					PublicationGroupDAO pubDao = PublicationGroupDAO.Factory.getDao(user.getRole());
					PublicationGroup group = pubDao.read(Integer.valueOf(request.getParameter(PARAM_PUBLICATION_ID)));
					app=new DpsApplication();
					app.setName(dps.getAppName());
					app.setPublicationGroup(group);
					server=new DpsServer();	
					doSettingForApp(server,dps, app);
					app.setDpsServer(server);
					if (dps.getContentEmail()){
				
						TemplateEmail contentEmail = new TemplateEmail();
						setContentEmailTemplate(contentEmail,dps);
						app.setContentEmail(contentEmail);					
					}
					if (dps.getPreviewEmail()){
						TemplateEmail previewEmail=new TemplateEmail();	
						setPreviewEmailTemplate(previewEmail,dps);
						app.setPreviewEmail(previewEmail);					
					}

					Map<String, Val> expectedImages=new HashMap <String, Val>();
					for (ImagePreviewRequest ip:imagePreviews ){
						Val val=new Val();
						val.setRegex(ip.getRegex());
						val.setErrorMsg(ip.getMsgError());
						expectedImages.put(ip.getImgConvention(), val);
					}
					DpsPreviewPackager packager=new DpsPreviewPackager(); 
					RegexPreviewParser parser=new RegexPreviewParser();
					parser.setExpectedImages(expectedImages);
					DpsPreviewProducer previewSetting = new DpsPreviewProducer(packager,parser);
					app.setPreviewProducer(previewSetting);
					appdao.create(app);
				} else
					response.getWriter().print(ExtJsJsonSerializer.fail("There should be at least two Image Previews with the next convention names: COVER_LARGE_LANDSCAPE and COVER_LARGE ", ErrorMessage.Code.GENERIC));			
				}
	}
	private static Boolean isPreviewValid (List<ImagePreviewRequest> imagePreviews){	
		int i=0;
		if (imagePreviews!=null && imagePreviews.size()>1){			
			for (ImagePreviewRequest ip:imagePreviews ){
				if (ip.getImgConvention().equalsIgnoreCase("COVER_LARGE_LANDSCAPE")
						|| ip.getImgConvention().equalsIgnoreCase("COVER_LARGE"))
					i++;
			}
		}
		if (i>1) return true;
		else return false;
	}
	private void setContentEmailTemplate(TemplateEmail template,DpsAppRequest dps ){
		template.setReceipients(dps.getContentRecepients());
		template.setBody(new VelocityNaming(dps.getContentEmailBody()));
		template.setSubject(new VelocityNaming(dps.getContentEmailSubject()));

	}
	private void setPreviewEmailTemplate(TemplateEmail template,DpsAppRequest dps ){
		template.setReceipients(dps.getPreviewRecepients());
		template.setBody(new VelocityNaming(dps.getPreviewEmailBody()));
		template.setSubject(new VelocityNaming(dps.getPreviewEmailSubject()));

	}
	private void doSettingForApp(DpsServer server,DpsAppRequest dps,DpsApplication app ){
		server.setDpsUsername(dps.getUserName());
		server.setDpsPassword(dps.getPassword());  
		server.setConsumerKey(dps.getDpsConsumerKey());
		server.setConsumerSecret(dps.getDpsConsumerSecret());
		server.setTimesToRetry(Integer.parseInt(dps.getDpsRetryCount()));
		server.setAddress(dps.getDpsUrl());
		app.setSendMsg(dps.getSendMsg());
		app.setActive(dps.getActive());
		if (dps.getDefaultPrice()==null)
			app.setDefaultPrice(new BigDecimal(0));
		else
			app.setDefaultPrice(new BigDecimal(dps.getDefaultPrice()));
		app.setRequireContentFirst(dps.getRequireContentFirst());
		app.setProductId(dps.getProductId());
		app.setVendorPubName(dps.getPubShortName());
		if (dps.getRendition().equalsIgnoreCase("web"))
			app.setRendition(Viewer.WEB);
		else app.setRendition(Viewer.ANY);
	}
	@Override
	protected List<Role> getEntitledRoles() {
		return Role.getRolesWithSameOrHigher(Role.SuperAdmin);
	}

}
