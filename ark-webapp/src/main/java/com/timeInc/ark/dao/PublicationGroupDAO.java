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

import java.util.Collection;
import java.util.List;

import com.timeInc.ark.dao.HibernatePublicationDAO.AdminPublicationDAO;
import com.timeInc.ark.dao.HibernatePublicationDAO.UserPublicationDAO;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.role.Role;

/**
 * DAO for {@link PublicationGroup}
 */
public interface PublicationGroupDAO extends GenericDAO<PublicationGroup, Integer> {
	
	/**
	 * Get all publication groups by adGroups
	 * @param adGroups
	 * @return
	 */
	List<PublicationGroup> findByAd(Collection<String> adGroups);

	/**
	 * Factory for {@link PublicationGroupDAO}
	 */
	public static class Factory {
		/**
		 * Gets the {@link PublicationGroupDAO}
		 *
		 * @param userRole the user role
		 * @return the dao
		 */
		public static PublicationGroupDAO getDao(Role userRole) {
			switch(userRole) {
			case Admin:
			case SuperAdmin:
				return new AdminPublicationDAO();
			default:
				return new UserPublicationDAO();
			}
		}
	}
}
