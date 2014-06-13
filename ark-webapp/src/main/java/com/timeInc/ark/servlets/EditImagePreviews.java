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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.reflect.TypeToken;
import com.timeInc.ark.application.Application;
import com.timeInc.ark.application.ApplicationEntity.ApplicationVisitor;
import com.timeInc.ark.application.CdsApplication;
import com.timeInc.ark.application.DpsApplication;
import com.timeInc.ark.application.ScpApplication;
import com.timeInc.ark.dao.ApplicationDAO;
import com.timeInc.ark.dao.SingleNamingPackagerDAO;
import com.timeInc.ark.naming.FileNaming;
import com.timeInc.ark.naming.VelocityNaming;
import com.timeInc.ark.packager.Renaming;
import com.timeInc.ark.packager.SingleNamingPackager;
import com.timeInc.ark.packager.Zip;
import com.timeInc.ark.parser.preview.RegexPreviewParser;
import com.timeInc.ark.parser.preview.RegexPreviewParser.Val;
import com.timeInc.ark.request.ImagePreviewRequest;
import com.timeInc.ark.response.ErrorMessage;
import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.ark.upload.producer.PreviewProducer.SinglePreviewProducer;
import com.timeInc.ark.util.ExtJsJsonDeserializer;
import com.timeInc.ark.util.ExtJsJsonSerializer;

/**
 * Class that handles creating, updating and deleting ImagePreview
 */
@WebServlet("/EditImagePreviews")
public class EditImagePreviews extends PermissionServlet {
	private static final long serialVersionUID = 1L;

	private static final String PARAM_ACTION = "write";  // the request parameter name
	private static final String ACTION_VALULE_DELETE = "delete"; // the corresponding request parameter values for ACTION
	private static final String ACTION_VALUE_UPDATE = "update";
	private static final String ACTION_VALUE_CREATE = "create";
	private static final String PARAM_JSON_DATA = "data";
	private static final String PARAM_APPLICATION_ID = "appId";     
	@Override
	protected void handlePost(HttpServletRequest request, HttpServletResponse response,  User user) throws ServletException, IOException {

		String crud = request.getParameter(PARAM_ACTION);
		String json = request.getParameter(PARAM_JSON_DATA);
		String appId= request.getParameter(PARAM_APPLICATION_ID);
		ApplicationDAO appdao = ApplicationDAO.Factory.getDao(user.getRole());
		Application<?, ?> app=appdao.getById(Integer.valueOf(appId)).get(0);
		String appType= app.accept(new ApplicationVisitor<String >() {

			@Override
			public String  visit(CdsApplication app) {
				return "cds";
			}

			@Override
			public String visit(DpsApplication app) {
				return "dps";
			}

			@Override
			public String visit(ScpApplication app) {
				return "scp";
			}
		});	
		String fails="";
		String comma ="";
		List<ImagePreviewRequest> imagePreviews=ExtJsJsonDeserializer.asType(new TypeToken<List<ImagePreviewRequest>>(){}.getType(), json);
		RegexPreviewParser parser=(RegexPreviewParser)app.getPreviewProducer().getParser();
		Map<String, Val> expectedImages =parser.getExpectedImages();
		SingleNamingPackagerDAO packagerDAO=new SingleNamingPackagerDAO();
		if (crud!=null && appId!=null){
			if(crud.equals(ACTION_VALULE_DELETE)) {
				for (ImagePreviewRequest ip:imagePreviews ){
					if (appType.equalsIgnoreCase("dps") && (ip.getImgConvention().equalsIgnoreCase("COVER_LARGE_LANDSCAPE")
							|| ip.getImgConvention().equalsIgnoreCase("COVER_LARGE"))){
						fails=fails+" "+comma+ip.getImgConvention();
						comma=",";
					}else if (appType.equalsIgnoreCase("scp") && expectedImages.size()-1==0){
						fails=fails+" "+comma+ip.getImgConvention();
						comma=",";
					}else if (appType.equalsIgnoreCase("scp")&& expectedImages.size()-1==1){
						//zip packager becomes renaming
						SingleNamingPackager packager=(SingleNamingPackager) app.getPreviewProducer().getPackager();
						FileNaming fn=packager.getNaming();

						VelocityNaming velNaming = (VelocityNaming) fn.getUnderlyingNaming();						
						SingleNamingPackager newPackager= new Renaming(new FileNaming(new VelocityNaming(velNaming.getTemplate())));	
						SinglePreviewProducer previewProducer = ((ScpApplication)app).getPreviewProducer();
						previewProducer.setPackager(newPackager);
						packagerDAO.delete(packager);

						parser.removeFromExpectedImages(ip.getImgConvention());	
					} else {
						parser.removeFromExpectedImages(ip.getImgConvention());							
					}
				}
				if (!fails.isEmpty()&& appType.equalsIgnoreCase("dps"))
					response.getWriter().print(ExtJsJsonSerializer.fail("Image Previews with " + fails+" naming convention can't be deleted!", ErrorMessage.Code.GENERIC));
				else if (!fails.isEmpty()&& appType.equalsIgnoreCase("scp")){
					response.getWriter().print(ExtJsJsonSerializer.fail("Image Preview with "+fails+ " naming convention can't be deleted.There should be at least one Image Preview!", ErrorMessage.Code.GENERIC));
				}
			}else if(crud.equals(ACTION_VALUE_UPDATE)){
				for (ImagePreviewRequest ip:imagePreviews ){									
					expectedImages.get(ip.getImgConvention()).setRegex(ip.getRegex());
					expectedImages.get(ip.getImgConvention()).setErrorMsg(ip.getMsgError());
				}
			}else if(crud.equals(ACTION_VALUE_CREATE)){
				int size =expectedImages.size();
				for (ImagePreviewRequest ip:imagePreviews ){
					Val val=new Val();
					val.setRegex(ip.getRegex());
					val.setErrorMsg(ip.getMsgError());
					expectedImages.put(ip.getImgConvention(), val);
				}
				if (appType.equalsIgnoreCase("scp")&&size==1){
					SingleNamingPackager packager=(SingleNamingPackager) app.getPreviewProducer().getPackager();
					FileNaming fn=packager.getNaming();

					VelocityNaming velNaming = (VelocityNaming) fn.getUnderlyingNaming();
					SingleNamingPackager newPackager= new Zip(new FileNaming(new VelocityNaming(velNaming.getTemplate())));	
					SinglePreviewProducer previewProducer = ((ScpApplication)app).getPreviewProducer();
					previewProducer.setPackager(newPackager);
					packagerDAO.delete(packager);

				}

			}
		}
		appdao.update(app);

	}
	@Override
	protected List<Role> getEntitledRoles() {
		return Role.getRolesWithSameOrHigher(Role.SuperAdmin);
	}
}
