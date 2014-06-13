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
 * GetDpsDefaults is a Servlet to get default data for Dps application 
 */

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.ark.util.ExtJsJsonSerializer;

public class GetDpsDefaults  extends PermissionServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected List<Role> getEntitledRoles() {
		return Role.getRolesWithSameOrHigher(Role.SuperAdmin);
	}

	protected void handleGet(HttpServletRequest req, HttpServletResponse resp, User user)
			throws ServletException, IOException {
		
		resp.setContentType ("application/json; charset=UTF-8");
		String jsonResult = ExtJsJsonSerializer.success(this.getGlobalSetting().getDefaultSetting());		
			
		resp.getWriter().write(jsonResult);
	
	}
}

