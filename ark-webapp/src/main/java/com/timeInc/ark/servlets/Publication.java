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

import com.timeInc.ark.response.ErrorMessage;
import com.timeInc.ark.response.PublicationResponse.PublicationResponseFactory;
import com.timeInc.ark.response.ResponseFactory;
import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.ark.util.ExtJsJsonSerializer;

/**
 * Servlet that gets a list of eligible publications for a user
 * and returns it in a json format.
 *
 */
public class Publication extends PermissionServlet {

	private static final long serialVersionUID = 1L;

	/*
	 * PublicationGroup are stored in the session; so there is a potential to have stale publicationgroups. 
	 * This requires user to invalidate the session by logging off.
	 */
	@Override
	protected void handleGet(HttpServletRequest req, HttpServletResponse resp,  User user) throws ServletException, IOException {
		resp.setContentType ("application/json; charset=UTF-8");
		
		if (user.getGroups() != null && user.getGroups().size() > 0) { 
			resp.getWriter().print(ExtJsJsonSerializer.success(ResponseFactory.Util.getInstancesAsList(new PublicationResponseFactory(), user.getGroups())));
		} else {
			resp.getWriter().print(ExtJsJsonSerializer.fail("You do not have access to any publications",ErrorMessage.Code.NO_ACCESS));
		}
	}
	
	@Override
	protected List<Role> getEntitledRoles() {
		return Role.getRolesWithSameOrHigher(Role.IssueManager);
	}

}
