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
package com.timeInc.ark.dao;

import java.util.List;

import com.timeInc.ark.application.Application;
import com.timeInc.ark.dao.HibernateIssueMetaDAO.AdminIssueMetaDAO;
import com.timeInc.ark.dao.HibernateIssueMetaDAO.IssueMetaManagerDAO;
import com.timeInc.ark.dao.HibernateIssueMetaDAO.UserIssueMetaDAO;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.role.Role;
import com.timeInc.ark.util.hibernate.PageData;
import com.timeInc.ark.util.hibernate.PageData.PageRequest;
import com.timeInc.mageng.util.filter.FilterAttribute;

/**
 * DAO for {@link IssueMeta}
 */
public interface IssueMetaDAO extends GenericDAO<IssueMeta, Integer>  {
	
	/**
	 * Finds IssueMetas by its identifiers;
	 * @param ids
	 * @return
	 */
	List<IssueMeta> getById(List<Integer> ids);
	
	/**
	 * Get a list of IssueMeta belonging to an issue and
	 * have an application associated
	 * @param issue the issue it belongs to
	 * @param apps the associated applications
	 * @return
	 */
	List<IssueMeta> getBy(Issue issue, List<Application<?,?>> apps);
	
	/**
	 * Get a list of IssueMeta that belongs to a particular 
	 * Application
	 * @param app 
	 * @return
	 */
	List<IssueMeta> getBy(Application<?,?> app);

	
	/**
	 * Returns the PageData of IssueMetas given a
	 * PageRequest for a certain Application 
	 * that meets the FilterAttribute
	 * @param app
	 * @param request
	 * @param filters
	 * @return
	 */
	PageData<IssueMeta> getWithFilterBy(Application<?,?> app, PageRequest request, List<? extends FilterAttribute<Object>> filters);
	
	/**
	 * Returns the PageData of IssueMetas given a
	 * PageRequest for a certain Application 
	 * @param app
	 * @param request
	 * @return
	 */
	PageData<IssueMeta> getBy(Application<?,?> app, PageRequest request);


	
	/**
	 * Factory for {@link IssueMetaDAO}
	 */
	public static class Factory {
		/**
		 * Gets the {@link IssueMetaDAO}
		 *
		 * @param userRole the user role
		 * @return the dao
		 */
		public static HibernateIssueMetaDAO getDao(Role userRole) {
			switch(userRole) {
				case IssueManager: 
					return new IssueMetaManagerDAO();
				case Admin:
				case SuperAdmin:
					return new AdminIssueMetaDAO();
				default:
					return new UserIssueMetaDAO();
			}
		}
	}
}
