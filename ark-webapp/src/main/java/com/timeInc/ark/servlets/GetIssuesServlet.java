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
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.timeInc.ark.application.Application;
import com.timeInc.ark.dao.ApplicationDAO;
import com.timeInc.ark.dao.IssueDAO;
import com.timeInc.ark.dao.PublicationGroupDAO;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.response.ErrorMessage;
import com.timeInc.ark.response.IssueResponse.IssueResponseFactory;
import com.timeInc.ark.response.ResponseFactory;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.ark.util.ExtJsJsonSerializer;

/**
 * Gets a list of Issue belonging to a PublicationGroup
 * depending on the action parameter.
 */
public class GetIssuesServlet extends PermissionServlet {
	private static final long serialVersionUID = 1L;

	private static final String PARAM_TAB_TYPE = "tabType";
	private static final String TAB_VALUE_MEDIA = "media";

	private static final String PARAM_ACTION = "action";
	private static final String ACTION_VALUE_GET_FOR_BASEITEMS = "baseitems";
	private static final String ACTION_VALUE_GET_FOR_ADMIN = "admin";

	private static final String PARAM_APPLICATION_ID = "appId";
	private static final String PARAM_PUBLICATION_ID = "pubId";

	@Override
	protected void handleGet(HttpServletRequest req, HttpServletResponse resp,  User user) throws ServletException, IOException {

		String jsonResult = "";
		resp.setContentType ("application/json; charset=UTF-8");

		String action = req.getParameter(PARAM_ACTION);

		List<Issue> result = Collections.emptyList();

		PublicationGroupDAO pubDao = PublicationGroupDAO.Factory.getDao(user.getRole());
		PublicationGroup group = pubDao.read(Integer.valueOf(req.getParameter(PARAM_PUBLICATION_ID)));
		IssueDAO issueDao = IssueDAO.Factory.getDao(user.getRole());


		if(action.equals(ACTION_VALUE_GET_FOR_BASEITEMS)) {
			String tab = req.getParameter(PARAM_TAB_TYPE);

			if(tab != null && tab.equals(TAB_VALUE_MEDIA)) // get all issue meta even if they have no issue meta associated
				result = issueDao.getIssue(group);
			else
				result = issueDao.getIssueWithMeta(group); // get only issue with issue meta associated
			
		} else if(action.equals(ACTION_VALUE_GET_FOR_ADMIN)) {
			String appId = req.getParameter(PARAM_APPLICATION_ID);

			if(appId != null) {
				Application<?,?> app = ApplicationDAO.Factory.getDao(user.getRole()).read(Integer.valueOf(appId));
				result = issueDao.getIssueWithNoMeta(app);  // get unassociated IssueMeta for an application.
			} else {
				result = issueDao.getAllIssue(group); // get all issue meta associated with PublicationGroup
			}
			
		} else {
			throw new IllegalArgumentException("Invalid get issues action: " + action);
		}			

		if(result.isEmpty() && action.equals(ACTION_VALUE_GET_FOR_BASEITEMS)) {
			jsonResult = ExtJsJsonSerializer.fail("The associated publication has no issues", ErrorMessage.Code.GENERIC);
		} else {
			jsonResult = ExtJsJsonSerializer.success(ResponseFactory.Util.getInstancesAsList(new IssueResponseFactory(), result));
		}	

		resp.getWriter().write(jsonResult);
	}
	
	@Override
	protected List<Role> getEntitledRoles() {
		return Role.getRolesWithSameOrHigher(Role.IssueManager);
	}
}

