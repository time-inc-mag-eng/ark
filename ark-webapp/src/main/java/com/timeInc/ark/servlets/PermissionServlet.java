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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.mageng.util.exceptions.PrettyException;

/**
 * Servlet that checks a user's role to see if they belong in one of the roles in 
 * {@code #getEntitledRoles()}
 * 
 * @see #exceptionIfRoleHasNoAccess(Role)
 */
public abstract class PermissionServlet extends GlobalContextServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute("user");
		exceptionIfRoleHasNoAccess(user.getRole());
		handlePost(req, resp, user);
	}
	
	@Override
	protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute("user");
		
		exceptionIfRoleHasNoAccess(user.getRole());
		
		handleGet(req, resp, user);
	}
	
	
	protected void handlePost(HttpServletRequest req, HttpServletResponse resp,  User user) throws ServletException, IOException {
		super.doPost(req, resp);
	}
	
	protected void handleGet(HttpServletRequest req, HttpServletResponse resp,  User user) throws ServletException, IOException {
		super.doGet(req, resp);
	}
	
	protected List<Role> getEntitledRoles() {
		return Role.getRolesWithSameOrHigher(Role.User);
	}
	
	protected void exceptionIfRoleHasNoAccess(Role role) {
		boolean match = false;
		
		for(Role entitled : getEntitledRoles()) {
			if(role == entitled) {
				match = true;
				break;
			}
		}
		
		if(!match)
			throw new PrettyException("Permission denied");
	}
}
