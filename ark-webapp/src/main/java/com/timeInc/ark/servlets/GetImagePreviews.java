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
 * GetImagePreviews is a servlet to get Image Previews
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.timeInc.ark.application.Application;
import com.timeInc.ark.dao.ApplicationDAO;
import com.timeInc.ark.parser.preview.RegexPreviewParser;
import com.timeInc.ark.parser.preview.RegexPreviewParser.Val;
import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.ark.util.ExtJsJsonSerializer;

public class GetImagePreviews extends PermissionServlet {

	private static final String APPLICATION_ID = "appId";
	private static final long serialVersionUID = 1L;
	@Override
	protected List<Role> getEntitledRoles() {
		return Role.getRolesWithSameOrHigher(Role.SuperAdmin);
	}

	protected void handleGet(HttpServletRequest req, HttpServletResponse resp, User user)
			throws ServletException, IOException {
		resp.setContentType ("application/json; charset=UTF-8");
		ApplicationDAO appdao = ApplicationDAO.Factory.getDao(user.getRole());
		Integer appId = Integer.valueOf(req.getParameter(APPLICATION_ID));
		Application<?, ?> app=appdao.getById(appId).get(0);	
		RegexPreviewParser parser=(RegexPreviewParser)app.getPreviewProducer().getParser();
		Map<String,Val> map =parser.getExpectedImages();
		final Collection<Map<String, String>> imagePreviews=new ArrayList<Map<String, String>>();
		for (Map.Entry entry : map.entrySet()){
			Map<String,String> m=new  HashMap <String,String>();
			m.put("imgConvention", (String) entry.getKey());
			m.put("regex",((Val) entry.getValue()).getRegex());
			m.put("msgError",((Val) entry.getValue()).getErrorMsg());
			imagePreviews.add(m);
		}
		String jsonResult = ExtJsJsonSerializer.success(imagePreviews);			
		resp.getWriter().write(jsonResult);
	
	}
}

