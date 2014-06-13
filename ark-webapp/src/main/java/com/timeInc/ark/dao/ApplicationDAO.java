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
import com.timeInc.ark.dao.HibernateAppDao.AdminAppDao;
import com.timeInc.ark.dao.HibernateAppDao.SuperAdminAppDao;
import com.timeInc.ark.dao.HibernateAppDao.UserAppDao;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.role.Role;

/**
 * DAO for {@link Application}
 */
@SuppressWarnings("rawtypes")
public interface ApplicationDAO extends GenericDAO<Application, Integer> {
	
	/**
	 * Get applications by its identifiers
	 * @param id
	 * @return
	 */
	List<Application<?,?>> getById(Integer... id);
	
	/**
	 * Get applications that belong to the PublicationGroups
	 * @param pgs
	 * @return
	 */
	List<Application<?,?>> getByPublication(PublicationGroup... pgs);
	
	
	/**
	 * Get a list of applications that belong to a particular issue
	 * @param issue
	 * @return
	 */
	List<Application<?,?>> getAssociatedMetaFor(Issue issue);
	
	/**
	 * Factory for {@link ApplicationDAO}
	 */
	static class Factory {
		/**
		 * Gets the {@link ApplicationDAO}
		 *
		 * @param userRole the user role
		 * @return the dao
		 */
		public static ApplicationDAO getDao(Role userRole) {
			switch(userRole) {
			case Admin:
				return new AdminAppDao();
			case SuperAdmin:
				return new SuperAdminAppDao();
			default:
				return new UserAppDao();
			}
		}
	}		
}
