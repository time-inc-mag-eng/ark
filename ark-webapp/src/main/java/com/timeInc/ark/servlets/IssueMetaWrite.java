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
import com.timeInc.ark.dao.ApplicationDAO;
import com.timeInc.ark.dao.GenericDAO.ConstraintViolation;
import com.timeInc.ark.dao.HibernateIssueMetaDAO;
import com.timeInc.ark.dao.IssueDAO;
import com.timeInc.ark.dao.IssueMetaDAO;
import com.timeInc.ark.dao.PaymentDAO;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.issue.action.CUDAction;
import com.timeInc.ark.request.HibernateIssueMetaDeserializer;
import com.timeInc.ark.request.IssueMetaRequest;
import com.timeInc.ark.response.ErrorMessage;
import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.ark.util.ExtJsJsonDeserializer;
import com.timeInc.ark.util.ExtJsJsonDeserializer.IdWrapper;
import com.timeInc.ark.util.ExtJsJsonSerializer;

/**
 * Create / Update / Delete IssueMeta
 */
public class IssueMetaWrite extends PermissionServlet {
	private static final long serialVersionUID = 1L;

	private static final String PARAM_ACTION = "write";  // the request parameter name

	private static final String ACTION_VALUE_DELETE = "delete"; // the corresponding request parameter values for ACTION
	private static final String ACTION_VALUE_UPDATE = "update";
	private static final String ACTION_VALUE_CREATE = "create";

	private static final String PARAM_JSON_DATA = "data"; // the request parameter that is in the form of a JSON

	@Override
	protected void handlePost(HttpServletRequest request, HttpServletResponse response,  User user) throws ServletException, IOException {	

		String crudType = request.getParameter(PARAM_ACTION);
		String json = request.getParameter(PARAM_JSON_DATA);

		final HibernateIssueMetaDAO metaDao = IssueMetaDAO.Factory.getDao(user.getRole());
		
		CUDAction crdAction = getAction(crudType, user, metaDao);
		List<IssueMeta> metas = deserialize(json, crudType, metaDao, user);
		
		Collection<ConstraintViolation> failures = new ArrayList<ConstraintViolation>();
		
		for(IssueMeta meta : metas) {
			ConstraintViolation cf = crdAction.performActionOn(meta);
			
			if(cf != null)
				failures.add(crdAction.performActionOn(meta));
		}
		
		if(!failures.isEmpty()) {
			response.setContentType ("application/json; charset=UTF-8");
			String msg = ExtJsJsonSerializer.fail("There was an error modifying issue(s)", ErrorMessage.Code.CONSTRAINT, failures);
			response.getWriter().print(msg);
		}
	}

	private static List<IssueMeta> deserialize(String json, String crudType, HibernateIssueMetaDAO metaDao, User user) {
		if(crudType.equals(ACTION_VALUE_DELETE)) {
			List<IdWrapper> deserializedIds =  ExtJsJsonDeserializer.asType(new TypeToken<List<IdWrapper>>(){}.getType(),json); 
			List<Integer> metaIds = new ArrayList<Integer>(deserializedIds.size());
			
			for(IdWrapper wrappedId : deserializedIds) 
				metaIds.add(wrappedId.id);
			
			return metaDao.getById(metaIds); 
		} else {
			HibernateIssueMetaDeserializer requestToIssueMeta = new HibernateIssueMetaDeserializer(metaDao, IssueDAO.Factory.getDao(user.getRole()), ApplicationDAO.Factory.getDao(user.getRole()), new PaymentDAO()); // update an issueid without bringing into session

			if(crudType.equals(ACTION_VALUE_UPDATE)) {
				List<IssueMetaRequest.Persisted> requestMetas = ExtJsJsonDeserializer.asType(new TypeToken<List<IssueMetaRequest.Persisted>>(){}.getType(), json);
				return requestToIssueMeta.getInstance(requestMetas);
			} else if(crudType.equals(ACTION_VALUE_CREATE)) {
				List<IssueMetaRequest> requestMetas = ExtJsJsonDeserializer.asType(new TypeToken<List<IssueMetaRequest>>(){}.getType(), json);
				return requestToIssueMeta.getNewInstance(requestMetas);
			} else {
				throw new IllegalArgumentException("Unknown crud:" + crudType);
			}
		}
	}

	private CUDAction getAction(String crudType, User user, IssueMetaDAO dao) {
		if(crudType.equals(ACTION_VALUE_DELETE)) {
			return new CUDAction.Delete(getGlobalSetting().getWorkFlowEvent(), user, dao);
		} else if(crudType.equals(ACTION_VALUE_UPDATE)) {
			return new CUDAction.Update(getGlobalSetting().getWorkFlowEvent(), user, dao, getGlobalSetting().getDataChangeMsger());
		} else if(crudType.equals(ACTION_VALUE_CREATE)) {
			return new CUDAction.Create(getGlobalSetting().getWorkFlowEvent(), user, dao);
		} else {
			throw new IllegalArgumentException("Unknown crud:" + crudType);
		}
	}

	@Override
	protected List<Role> getEntitledRoles() {
		List<Role> roles = Role.getRolesWithSameOrHigher(Role.Admin);
		roles.add(Role.IssueManager);
		return roles;
	}
}
