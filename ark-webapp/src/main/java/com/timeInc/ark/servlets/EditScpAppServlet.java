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
 * EditScpAppServlet is a Servlet for updating and creating Scp Application.
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
import com.timeInc.ark.application.ScpApplication;
import com.timeInc.ark.dao.ApplicationDAO;
import com.timeInc.ark.dao.ParserDAO;
import com.timeInc.ark.dao.PublicationGroupDAO;
import com.timeInc.ark.dao.TemplateEmailDAO;
import com.timeInc.ark.naming.FileNaming;
import com.timeInc.ark.naming.PathNaming;
import com.timeInc.ark.naming.VelocityNaming;
import com.timeInc.ark.packager.Packager;
import com.timeInc.ark.packager.Renaming;
import com.timeInc.ark.packager.SingleFileInfo;
import com.timeInc.ark.packager.SingleNamingPackager;
import com.timeInc.ark.packager.Zip;
import com.timeInc.ark.parser.Parser.ContentParser;
import com.timeInc.ark.parser.content.IdentifyingContent;
import com.timeInc.ark.parser.preview.RegexPreviewParser;
import com.timeInc.ark.parser.preview.RegexPreviewParser.Val;
import com.timeInc.ark.request.ImagePreviewRequest;
import com.timeInc.ark.request.ScpAppRequest;
import com.timeInc.ark.response.ErrorMessage;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.ark.upload.producer.ContentProducer;
import com.timeInc.ark.upload.producer.PackageProducer;
import com.timeInc.ark.upload.producer.PreviewProducer.SinglePreviewProducer;
import com.timeInc.ark.uploader.ScpServer;
import com.timeInc.ark.util.ExtJsJsonDeserializer;
import com.timeInc.ark.util.ExtJsJsonSerializer;
import com.timeInc.ark.util.TemplateEmail;
import com.timeInc.mageng.util.sftp.ScpCredentials;
import com.timeInc.mageng.util.sftp.SftpServer;

public class EditScpAppServlet extends PermissionServlet{

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
		String pubId= request.getParameter(PARAM_PUBLICATION_ID);
		String crud = request.getParameter(PARAM_ACTION);
		String json = request.getParameter(PARAM_JSON_DATA);
		List<ScpAppRequest> scpList=ExtJsJsonDeserializer.asType(new TypeToken<List<ScpAppRequest>>(){}.getType(), json);
		ScpApplication app=null;
		ApplicationDAO appdao = ApplicationDAO.Factory.getDao(user.getRole());
		if (scpList!=null && scpList.size()>0 && !crud.isEmpty()){
			ScpAppRequest scp=scpList.get(0); 
			List<ImagePreviewRequest> imagePreviews=ExtJsJsonDeserializer.asType(new TypeToken<List<ImagePreviewRequest>>(){}.getType(),scp.getImagePreviews());
			if(crud.equals(ACTION_VALUE_UPDATE)){
				app=(ScpApplication) appdao.getById(Integer.valueOf(appId)).get(0);
				doSettingForApp(scp, app);
				ScpServer server = app.getServer();
				ScpCredentials cred=new ScpCredentials(scp.getUserName(),scp.getPassword(),scp.getServer(),scp.getPort());
				server.setCred(cred);
				VelocityNaming velNaming= (VelocityNaming) server.getPathNaming().getUnderlyingNaming();				
				velNaming.setTemplate(scp.getDirPath());
				ContentProducer<?, ?> producer = (ContentProducer<?, ?>) app.getContentProducer();
				ContentParser cp=getContentParser(scp.getContentType());				 
				producer.setParser(cp);
				SingleNamingPackager packager = (SingleNamingPackager) producer.getPackager();
				velNaming = new VelocityNaming(scp.getPathNaming());
				packager.getNaming().setNaming(velNaming);
				FileNaming previewfn=new FileNaming(new VelocityNaming(scp.getPreviewNaming()));
				SingleNamingPackager pack= (SingleNamingPackager) app.getPreviewProducer().getPackager();
				pack.setNaming(previewfn);
				TemplateEmailDAO emailDAO=new TemplateEmailDAO();
				TemplateEmail temp; //reusable
				TemplateEmail previewEmail=app.getPreviewEmail();
				TemplateEmail contentEmail = app.getContentEmail();
				if (scp.getContentEmail()){					
					if (contentEmail==null)
						contentEmail= new TemplateEmail();
					setContentEmailTemplate(contentEmail,scp);
					app.setContentEmail(contentEmail);					
				}else if (!scp.getContentEmail() && contentEmail!=null) {					
						temp=app.getContentEmail();
						app.setContentEmail(null);
						emailDAO.delete(temp);			
				}
				if (scp.getPreviewEmail()){					
					if (previewEmail==null)
						previewEmail=new TemplateEmail();					
					setPreviewEmailTemplate(previewEmail,scp);
					app.setPreviewEmail(previewEmail);					
				}else if (scp.getPreviewEmail()==false && previewEmail!=null){					
						temp=app.getPreviewEmail();
						app.setPreviewEmail(null);
						emailDAO.delete(temp);					
				}
			}else 	
				if(crud.equals(ACTION_VALUE_CREATE)&& (imagePreviews != null && imagePreviews.size()>0)){
					PublicationGroupDAO pubDao = PublicationGroupDAO.Factory.getDao(user.getRole());
					PublicationGroup group = pubDao.read(Integer.valueOf(pubId));				
					app= new ScpApplication();
					app.setName(scp.getAppName());
					app.setPublicationGroup(group);
					doSettingForApp(scp, app);
					if (scp.getContentEmail()){						
						TemplateEmail contentEmail = new TemplateEmail();
						setContentEmailTemplate(contentEmail,scp);
						app.setContentEmail(contentEmail);					
					}
					if (scp.getPreviewEmail()){
						TemplateEmail previewEmail=new TemplateEmail();	
						setPreviewEmailTemplate(previewEmail,scp);
						app.setPreviewEmail(previewEmail);					
					}
					ScpCredentials cred=new ScpCredentials(scp.getUserName(),scp.getPassword(),scp.getServer(), scp.getPort());
					PathNaming location=new PathNaming (new VelocityNaming(scp.getDirPath()));								
					ScpServer server = new ScpServer(cred, location, new SftpServer());				
					app.setScpServer(server);
					ContentParser<?> cp=getContentParser(scp.getContentType());				
					FileNaming fn=new FileNaming(new VelocityNaming(scp.getPathNaming()));				
					SingleNamingPackager packager;
					if (scp.getContentType().equalsIgnoreCase("any"))
						packager=new Renaming(fn);
					else packager =new Zip(fn);				
					PackageProducer<SingleFileInfo, SingleFileInfo> producer= new ContentProducer(packager,cp);
					app.setContentProducer(producer);
					Map<String, Val> expectedImages=new HashMap <String, Val>();
					for (ImagePreviewRequest ip:imagePreviews ){
						Val val=new Val();
						val.setRegex(ip.getRegex());
						val.setErrorMsg(ip.getMsgError());
						expectedImages.put(ip.getImgConvention(), val);
					}				
					Packager packer = null;
					FileNaming pfn=new FileNaming(new VelocityNaming(scp.getPreviewNaming()));
					if (expectedImages.size()>1)
						packer=new Zip(pfn);
					else if (expectedImages.size()==1)
						packer=new Renaming(pfn);
					RegexPreviewParser parser=new RegexPreviewParser();
					parser.setExpectedImages(expectedImages);
					SinglePreviewProducer previewProducer = new SinglePreviewProducer(packer, parser);
					app.setPreviewProducer(previewProducer);						
					appdao.create(app);		
				}
				else {	
					response.getWriter().print(ExtJsJsonSerializer.fail("There should be at least one Image Preview!", ErrorMessage.Code.GENERIC));			
				}
		}
	}
	private void setContentEmailTemplate(TemplateEmail template,ScpAppRequest scp ){
		template.setReceipients(scp.getContentRecepients());
		template.setBody(new VelocityNaming(scp.getContentEmailBody()));
		template.setSubject(new VelocityNaming(scp.getContentEmailSubject()));

	}
	private void setPreviewEmailTemplate(TemplateEmail template,ScpAppRequest scp ){
		template.setReceipients(scp.getPreviewRecepients());
		template.setBody(new VelocityNaming(scp.getPreviewEmailBody()));
		template.setSubject(new VelocityNaming(scp.getPreviewEmailSubject()));

	}
	private void doSettingForApp(ScpAppRequest scp, ScpApplication app){
		app.setDefaultPrice(new BigDecimal(scp.getDefaultPrice()));
		app.setSendMsg(scp.getSendMsg());
		app.setActive(scp.getActive());
		app.setProductId(scp.getProductId());
		app.setVendorPubName(scp.getPubShortName());
		app.setRequireContentFirst(scp.getRequireContentFirst());
	}
	private ContentParser getContentParser (String type){
		ParserDAO parserDao=new ParserDAO();
		List<IdentifyingContent> parsersList=parserDao.getAll();
		for (IdentifyingContent<?> ic : parsersList){
			if (type.equalsIgnoreCase(ic.getType())){
				return ic;
			} 
		}
	
		return null;
	}
	@Override
	protected List<Role> getEntitledRoles() {
		return Role.getRolesWithSameOrHigher(Role.SuperAdmin);
	}
	

}
