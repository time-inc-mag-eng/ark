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
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.timeInc.ark.application.Application;
import com.timeInc.ark.dao.ApplicationDAO;
import com.timeInc.ark.dao.IssueDAO;
import com.timeInc.ark.dao.IssueDAO.Factory;
import com.timeInc.ark.dao.PublicationGroupDAO;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.response.ApplicationResponse;
import com.timeInc.ark.response.ErrorMessage;
import com.timeInc.ark.response.ResponseFactory;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.ark.util.ExtJsJsonSerializer;
import com.timeInc.mageng.util.string.StringUtil;


/**
 * Gets a list of application depending on the action parameter.
 * If the action parameter value is ACTION_VALUE_GET_FOR_BASEITEMS then it gets a list of
 * application that belongs to an issue, otherwise if the parameter value is ACTION_VALUE_GET_FOR_SINGLE
 * then it gets a list of applications belonging to a PublicationGroup
 */
public class GetApplicationsServlet extends PermissionServlet {
	private static final long serialVersionUID = 1L;

	private static final String PARAM_ACTION = "action";
	private static final String ACTION_VALUE_GET_FOR_BASEITEMS = "baseitems";
	private static final String ACTION_VALUE_GET_FOR_SINGLE = "single";

	private static final String PARAM_ISSUE_ID = "issueId";
	private static final String PARAM_PUBLICATION_ID = "pubId";

	@Override
	protected void handleGet(HttpServletRequest req, HttpServletResponse resp,  User user) throws ServletException, IOException {

		resp.setContentType("application/json; charset=UTF-8");

		String action = req.getParameter(PARAM_ACTION);
		
		ApplicationDAO dao = ApplicationDAO.Factory.getDao(user.getRole());
		
		if(!StringUtil.isEmpty(action)) {
			if(action.equals(ACTION_VALUE_GET_FOR_BASEITEMS)) {
				Integer issueId = Integer.valueOf(req.getParameter(PARAM_ISSUE_ID));	

				IssueDAO issueDAO = Factory.getDao(user.getRole());
				
				Issue issue = issueDAO.read(issueId);
				
				List<Application<?,?>> apps =  dao.getAssociatedMetaFor(issue);

				writeResult(resp, ResponseFactory.Util.getInstancesAsList(new ApplicationResponse.ApplicationResponseFactory(), apps));

			} else if(action.equals(ACTION_VALUE_GET_FOR_SINGLE)) {
				PublicationGroupDAO pubDAO = PublicationGroupDAO.Factory.getDao(user.getRole());

				PublicationGroup pub = pubDAO.read(Integer.valueOf(req.getParameter(PARAM_PUBLICATION_ID)));

				List<Application<?,?>> apps =  dao.getByPublication(pub);
				
				writeResult(resp, ResponseFactory.Util.getInstancesAsList(new ApplicationResponse.ApplicationResponseFactory(), apps));
			} else {
				throw new IllegalArgumentException("invalid get action parameter: " + action);
			}
		} else 
			throw new IllegalArgumentException("no action parameter");	
	}

	private static void writeResult(HttpServletResponse resp, Collection<?> result) throws IOException {
		if(result == null || result.isEmpty()) {
			resp.getWriter().write(ExtJsJsonSerializer.fail("No applications found!", ErrorMessage.Code.GENERIC));
		} else {
			resp.getWriter().write(ExtJsJsonSerializer.success(result));
		}	
	}
	
	@Override
	protected List<Role> getEntitledRoles() {
		List<Role> roles = super.getEntitledRoles();
		roles.add(Role.IssueManager);
		return roles;
	}
	
	
}
