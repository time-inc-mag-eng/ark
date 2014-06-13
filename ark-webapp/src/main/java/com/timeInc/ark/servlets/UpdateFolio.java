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

import com.timeInc.ark.dao.IssueMetaDAO;
import com.timeInc.ark.event.AbstractEvent;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.issue.action.UpdateFolioAction;
import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.ark.util.ExtJsJsonSerializer;
import com.timeInc.mageng.util.event.EventManager;
import com.timeInc.mageng.util.misc.Status;

/**
 * The Class UpdateFolio.
 */
public class UpdateFolio extends PermissionServlet {
	private static final long serialVersionUID = 1L;

	private static final String ACTION_PARAM = "action";
	
	private static final String ACTION_DELETE = "delete";
	private static final String ACTION_UNPUBLISH = "unpublish";
	private static final String ACTION_PUBLISH = "publish";
	
	private static final String META_ID_PARAM = "metaId";

	@Override
	protected void handlePost(HttpServletRequest req, HttpServletResponse resp,  User user) throws ServletException, IOException {
		resp.setContentType("application/json");
		
		int metaId = Integer.valueOf(req.getParameter(META_ID_PARAM));
		
		IssueMeta meta = IssueMetaDAO.Factory.getDao(user.getRole()).read(metaId);
		String action = req.getParameter(ACTION_PARAM);
		
		UpdateFolioAction updateAction = getFolioAction(meta, action, user);
		Status result = updateAction.performActionOn(meta);
		
		resp.getWriter().write(ExtJsJsonSerializer.success(result.getDescription()));
	}
	
	
	@Override
	protected List<Role> getEntitledRoles() {
		return Role.getRolesWithSameOrHigher(Role.Admin);
	}
	
	private UpdateFolioAction getFolioAction(IssueMeta meta, String actionValue, User user) {
		EventManager<Class<? extends AbstractEvent>, AbstractEvent> em = getGlobalSetting().getWorkFlowEvent();
		
		if(actionValue.equals(ACTION_DELETE)) {
			return new UpdateFolioAction.DeleteFolio(em, user, new UpdateFolioAction.UnpublishFolio(em, user));
		} else if(actionValue.equals(ACTION_PUBLISH)) {
			return new UpdateFolioAction.PublishFolioAdapter(em, user);
		} else if(actionValue.equals(ACTION_UNPUBLISH)) {
			return new UpdateFolioAction.UnpublishFolio(em, user);
		} else 
			throw new IllegalArgumentException("Unknown action value: " + actionValue);
	}
}
