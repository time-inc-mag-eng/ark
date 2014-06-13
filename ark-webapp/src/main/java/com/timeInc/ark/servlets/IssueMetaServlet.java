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

import com.google.gson.reflect.TypeToken;
import com.timeInc.ark.application.Application;
import com.timeInc.ark.dao.ApplicationDAO;
import com.timeInc.ark.dao.IssueDAO;
import com.timeInc.ark.dao.IssueMetaDAO;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.response.ErrorMessage;
import com.timeInc.ark.response.IssueMetaResponse;
import com.timeInc.ark.response.ResponseFactory;
import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.ark.util.ExtJsJsonDeserializer;
import com.timeInc.ark.util.ExtJsJsonSerializer;
import com.timeInc.ark.util.hibernate.PageData;
import com.timeInc.ark.util.hibernate.PageData.PageRequest;
import com.timeInc.mageng.util.filter.ExtJsFilterAttribute;
import com.timeInc.mageng.util.filter.FilterAttribute;

/**
 * Handles a request to get a list IssueMeta(s) depending on 
 * on the "action" request parameter.
 * 
 *
 */
public class IssueMetaServlet extends PermissionServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String PARAM_FETCH_TYPE = "rtype";
	private static final String FETCH_VALUE_DEEP_REQUEST = "deep";

	private static final String PARAM_ACTION = "action";
	private static final String ACTION_VALUE_GET_FOR_BASEITEMS = "baseitems";
	private static final String ACTION_VALUE_GET_FOR_ADMIN = "admin";
	
	private static final String PARAM_PAGE_LIMIT = "limit";
	private static final String PARAM_PAGE_SORT_COLUMN = "sort";
	private static final String PARAM_PAGE_SORT_DIR = "dir";
	private static final String PARAM_PAGE_START = "start";
	
	private static final String PARAM_FILTER = "filter";
	

	private static final String PARAM_ISSUE_ID = "issueId";
	private static final String PARAM_APPLICATION_ID = "appId";
	
	@Override
	protected void handleGet(HttpServletRequest req, HttpServletResponse resp,  User user) throws ServletException, IOException {
		resp.setContentType ("application/json; charset=UTF-8");
		
		String actionType = (String) req.getParameter(PARAM_ACTION);
		
		
		IssueMetaDAO metadao = IssueMetaDAO.Factory.getDao(user.getRole());
		ApplicationDAO appdao = ApplicationDAO.Factory.getDao(user.getRole());
		
		if(actionType.equals(ACTION_VALUE_GET_FOR_BASEITEMS)) {
			String appIdStr[] = req.getParameterValues(PARAM_APPLICATION_ID);
			List<Integer> appIds = new ArrayList<Integer>();
			
			for(int i=0;i<appIdStr.length;i++) {
				appIds.add(Integer.valueOf(appIdStr[i]));
			}
			
			int issueId = Integer.valueOf(req.getParameter(PARAM_ISSUE_ID));
			
 			Collection<IssueMetaResponse> responses = getIssueMetaWith(metadao, appdao, IssueDAO.Factory.getDao(user.getRole()), appIds, issueId, req.getParameter(PARAM_FETCH_TYPE));
			
			if(responses.isEmpty()) {
				resp.getWriter().print(ExtJsJsonSerializer.fail("No issuemeta associated", ErrorMessage.Code.GENERIC));
			} else 
				resp.getWriter().print(ExtJsJsonSerializer.success(responses));
			
		} else if(actionType.equals(ACTION_VALUE_GET_FOR_ADMIN)) {
			Integer appId = Integer.valueOf(req.getParameter(PARAM_APPLICATION_ID));

			int pageSize = Integer.valueOf(req.getParameter(PARAM_PAGE_LIMIT));
			int startIndex = Integer.valueOf(req.getParameter(PARAM_PAGE_START));
			
			String sortColumn = req.getParameter(PARAM_PAGE_SORT_COLUMN);
			String sortDir = req.getParameter(PARAM_PAGE_SORT_DIR);
			
			String jsonFilter = req.getParameter(PARAM_FILTER); 

			PageRequest pageRequest = new PageRequest(PageRequest.OrderType.getSortOrder(sortDir), sortColumn, pageSize, startIndex);
			
			List<ExtJsFilterAttribute> filters  = ExtJsJsonDeserializer.asType(new TypeToken<List<ExtJsFilterAttribute>>(){}.getType(), jsonFilter);
			
			PageData<IssueMetaResponse> result = getPagedIssueMeta(metadao, appdao, pageRequest, appId, filters);
			
			resp.getWriter().print(ExtJsJsonSerializer.successWithNoBody(result));
		}					
	}
		
	private static PageData<IssueMetaResponse> getPagedIssueMeta(IssueMetaDAO metaDAO, ApplicationDAO appDAO, PageRequest request, int appId, List<? extends FilterAttribute<Object>> filters) {		
		Application<?,?> app =  appDAO.read(appId);
		PageData<IssueMeta> pageData = metaDAO.getWithFilterBy(app, request, filters);
		return new PageData<IssueMetaResponse>(ResponseFactory.Util.getInstancesAsList(new IssueMetaResponse.LocalMetaFactory(), pageData.getData()), pageData.getTotal());
	}
	
	
	private static Collection<IssueMetaResponse> getIssueMetaWith(IssueMetaDAO metaDAO, ApplicationDAO appDAO, IssueDAO issueDAO, List<Integer> appIds, int issueId, String fetchType) {
		List<Application<?,?>> apps = appDAO.getById(appIds.toArray(new Integer[appIds.size()]));
		Issue issue = issueDAO.read(issueId);
		
		List<IssueMeta> metas = metaDAO.getBy(issue, apps);
		
		if (fetchType.equals(FETCH_VALUE_DEEP_REQUEST)) {
			return ResponseFactory.Util.getInstancesAsList(new IssueMetaResponse.RemoteMetaFactory(), metas); 
		} else 		
			return ResponseFactory.Util.getInstancesAsList(new IssueMetaResponse.LocalMetaFactory(), metas);
	}
	
	
	@Override
	protected List<Role> getEntitledRoles() {
		List<Role> roles = super.getEntitledRoles();
		roles.add(Role.IssueManager);
		return roles;
	}
}
