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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.timeInc.ark.dao.EventDAO;
import com.timeInc.ark.dao.PublicationGroupDAO.Factory;
import com.timeInc.ark.event.IssueEvent;
import com.timeInc.ark.event.ReportBean;
import com.timeInc.ark.event.ReportBean.LogReportBeanFactory;
import com.timeInc.ark.response.ResponseFactory;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.role.User;
import com.timeInc.ark.util.ExtJsJsonSerializer;
import com.timeInc.ark.util.hibernate.PageData;
import com.timeInc.ark.util.hibernate.PageData.PageRequest;

/**
 * Servlet that gets a paged {@code IssueEvent} for a PublicationGroup.
 */
public class EventServlet extends PermissionServlet {

	private static final long serialVersionUID = 1L;

	private static final String PARAM_PUBLICATION_ID = "pubId";
	private static final String PARAM_PAGE_LIMIT = "limit";
	private static final String PARAM_PAGE_SORT_COLUMN = "sort";
	private static final String PARAM_PAGE_SORT_DIR = "dir";
	private static final String PARAM_PAGE_START = "start";

	@Override
	protected void handleGet(HttpServletRequest req, HttpServletResponse resp,  User user) throws ServletException, IOException {
		
		resp.setContentType ("application/json; charset=UTF-8");

		int pageSize = Integer.valueOf((String)req.getParameter(PARAM_PAGE_LIMIT)); 
		int startIndex = Integer.valueOf((String)req.getParameter(PARAM_PAGE_START));
		String sortColumn = (String)req.getParameter(PARAM_PAGE_SORT_COLUMN);
		String sortDir = (String) req.getParameter(PARAM_PAGE_SORT_DIR);
		
		int pubId = Integer.valueOf((String)req.getParameter(PARAM_PUBLICATION_ID)); 
		
		PublicationGroup pub = Factory.getDao(user.getRole()).read(pubId);
		
		PageRequest pageRequest = new PageRequest(PageRequest.OrderType.getSortOrder(sortDir),sortColumn,pageSize,startIndex);
		
		EventDAO logDao = new EventDAO();
		
		PageData<IssueEvent> entries = logDao.getPagedIssueLog(pub, pub.getNumDaysReport(), pageRequest);
		
		PageData<ReportBean> response = new PageData<ReportBean>(ResponseFactory.Util.getInstancesAsList(new LogReportBeanFactory(), entries.getData()), entries.getTotal());
		resp.getWriter().write(ExtJsJsonSerializer.successWithNoBody(response));
	}
}
