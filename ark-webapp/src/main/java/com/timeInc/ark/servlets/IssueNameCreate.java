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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;
import com.timeInc.ark.dao.GenericDAO.ConstraintViolation;
import com.timeInc.ark.dao.IssueDAO;
import com.timeInc.ark.dao.PublicationGroupDAO.Factory;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.response.ErrorMessage;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.ark.util.ExtJsJsonDeserializer;
import com.timeInc.ark.util.ExtJsJsonSerializer;


/**
 * Create / Update / Delete Issue
 */
public class IssueNameCreate extends PermissionServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(IssueNameCreate.class);
	
	private static final String PARAM_PUBLICATION_ID = "pubId"; // the request parameter name for the publication id
	
    private static final String PARAM_ACTION = "write";  // the request parameter name
	private static final String ACTION_VALULE_DELETE = "delete"; // the corresponding request parameter values for ACTION
	private static final String ACTION_VALUE_UPDATE = "update";
	private static final String ACTION_VALUE_CREATE = "create";
	
	private static final String PARAM_JSON_DATA = "data"; // the request parameter that is in the form of a JSON
	
	@Override
	protected void handlePost(HttpServletRequest request, HttpServletResponse response,  User user) throws ServletException, IOException {
		String crud = request.getParameter(PARAM_ACTION);
		String json = request.getParameter(PARAM_JSON_DATA);
		
		log.debug("Request: " + crud + " json:" + json);
		
		IssueDAO dao = IssueDAO.Factory.getDao(user.getRole());
		
		List<Issue> deserializedIssue = ExtJsJsonDeserializer.asType(new TypeToken<List<Issue>>(){}.getType(), json);
		
		Collection<ConstraintViolation> failures = new ArrayList<ConstraintViolation>();
		
		if(crud.equals(ACTION_VALULE_DELETE)) {
			List<Integer> ids = new ArrayList<Integer>(deserializedIssue.size());		
			
			for(Issue issue : deserializedIssue)
				ids.add(issue.getId());
			
			failures = dao.deletebyId(ids);
		} else {
			PublicationGroup group = Factory.getDao(user.getRole()).read(Integer.valueOf(request.getParameter(PARAM_PUBLICATION_ID)));
			

			
			for(Issue issue : deserializedIssue) 
				issue.setPublication(group);
			
			if(crud.equals(ACTION_VALUE_UPDATE)) {
				failures = dao.update(deserializedIssue);
			} else if(crud.equals(ACTION_VALUE_CREATE)) {
				failures = dao.create(deserializedIssue);
			}
		} 
		
		if(!failures.isEmpty()) {
			response.setContentType ("application/json; charset=UTF-8");
			response.getWriter().print(ExtJsJsonSerializer.fail("There was an error modifying data", ErrorMessage.Code.CONSTRAINT, failures));
		}
		
	}
	
	@Override
	protected List<Role> getEntitledRoles() {
		List<Role> roles = Role.getRolesWithSameOrHigher(Role.Admin);
		roles.add(Role.IssueManager);
		return roles;
	}
}
