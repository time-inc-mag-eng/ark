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
import com.timeInc.ark.dao.HibernateIssueDao.AdminIssueDao;
import com.timeInc.ark.dao.HibernateIssueDao.UserIssueDao;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.role.Role;

/**
 * DAO for {@link Issue}
 */
public interface IssueDAO extends GenericDAO<Issue, Integer> {
	
	/**
	 * Get all issue that belong to the PublicationGroup
	 * @param pub
	 * @return
	 */
	List<Issue> getAllIssue(PublicationGroup pub);
	
	/**
	 * Get all issues that belong to the PublicationGroup
	 * with no duplicate names. If there is an Issue with the same name then earlier short date is included.
	 * @param pub
	 * @return
	 */
	List<Issue> getIssue(PublicationGroup pub);
	
	/**
	 * Get issues for a certain PublicationGroup that have
	 * associated IssueMeta
	 * @param pub
	 * @return
	 */
	List<Issue> getIssueWithMeta(PublicationGroup pub);
	
	/**
	 * Get issues for a certain PublicationGroup and Application
	 * that have no associated IssueMeta
	 * @param pub
	 * @param app
	 * @return
	 */
	List<Issue> getIssueWithNoMeta(Application<?,?> app);
	
	
	/**
	 * Factory for {@link IssueDAO}
	 */
	public static class Factory {
		/**
		 * Gets the {@link IssueDAO}
		 *
		 * @param userRole the user role
		 * @return the dao
		 */
		public static IssueDAO getDao(Role userRole) {
			switch(userRole) {
			case Admin:
			case IssueManager:
			case SuperAdmin:
				return new AdminIssueDao();
			default:
				return new UserIssueDao();
			}
		}
	}	
}
