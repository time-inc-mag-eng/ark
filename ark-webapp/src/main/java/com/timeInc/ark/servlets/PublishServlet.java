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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.timeInc.ark.dao.IssueMetaDAO;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.issue.action.PublishAction;
import com.timeInc.ark.response.PublishResponse;
import com.timeInc.ark.role.User;
import com.timeInc.ark.util.ExtJsJsonSerializer;
import com.timeInc.mageng.util.misc.Status;
import com.timeInc.mageng.util.string.StringUtil;


/**
 * The Class PublishServlet.
 */
public class PublishServlet extends PermissionServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String PARAM_META_ISSUE_IDS = "metaIds";
	
	protected void handlePost(HttpServletRequest request, HttpServletResponse response, final User user) throws ServletException, IOException {
		response.setContentType("application/json");
		
		List<Integer> ids = StringUtil.splitCommaSepIntoPrimitive(Integer.class, request.getParameter(PARAM_META_ISSUE_IDS));
		List<IssueMeta> metas = IssueMetaDAO.Factory.getDao(user.getRole()).getById(ids);
		
		List<PublishResponse> responseList = new ArrayList<PublishResponse>();
		PublishAction publishAction = new PublishAction(getGlobalSetting().getWorkFlowEvent(), user);
		
		
		for(final IssueMeta meta : metas) {
			Status result = publishAction.performActionOn(meta);
			
			if(result != null && !result.isError())
				responseList.add(new PublishResponse(meta, result));
		}
		
		
		// CCD - ccd cdc dcc
		// c cd cdc
		
		response.getWriter().write(ExtJsJsonSerializer.success(responseList));
	}
}
