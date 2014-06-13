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

import com.timeInc.ark.dao.IssueDAO.Factory;
import com.timeInc.ark.dao.MediaLocationDAO;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.media.MediaLocation;
import com.timeInc.ark.response.MediaLocationResponse.OriginMediaResponseFactory;
import com.timeInc.ark.response.ResponseFactory;
import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.ark.util.ExtJsJsonSerializer;

/**
 * Gets a list of {@code MediaLocation} that belongs to an Issue.
 */
public class GetMediaLocation extends PermissionServlet {
	private static final long serialVersionUID = 1L;

	private static final String PARAM_ISSUE_ID = "issueId"; 
	
	@Override
	protected void handleGet(HttpServletRequest req, HttpServletResponse resp,  User user) throws ServletException, IOException {
		resp.setContentType ("application/json; charset=UTF-8");
		
		int issueId = Integer.valueOf(req.getParameter(PARAM_ISSUE_ID));

		MediaLocationDAO mediaDAO = new MediaLocationDAO();
		
		Issue issue = Factory.getDao(user.getRole()).read(issueId);

		List<MediaLocation> entries = mediaDAO.getMediaLocationForIssue(issue);
		
		resp.getWriter().write(ExtJsJsonSerializer.success(ResponseFactory.Util.getInstancesAsList(new OriginMediaResponseFactory(), entries)));		
	}

	@Override
	protected List<Role> getEntitledRoles() {
		return Role.getRolesWithSameOrHigher(Role.Media);
	}
}
