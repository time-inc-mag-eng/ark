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

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.timeInc.ark.role.PublicationGroup;

abstract class HibernatePublicationDAO extends HibernateGenericDAO<PublicationGroup,Integer> implements PublicationGroupDAO {

	public HibernatePublicationDAO() {
		super(PublicationGroup.class);
	}

	protected Criteria getBaseCrit(Collection<String> adGroups) {
		Criteria crit = getCriteria(Restrictions.in("adGroup", adGroups));
		crit.addOrder(Order.asc("name"));
		return crit;
	}		

	/**
	 * The Class AdminPublicationDAO.
	 */
	static class AdminPublicationDAO extends HibernatePublicationDAO  {

		/* (non-Javadoc)
		 * @see com.timeInc.ark.dao.PublicationGroupDAO#findByAd(java.util.Collection)
		 */
		@Override
		public List<PublicationGroup> findByAd(Collection<String> adGroups) {
			return getBaseCrit(adGroups).list();
		}
	}

	/**
	 * The Class UserPublicationDAO.
	 */
	static class UserPublicationDAO extends HibernatePublicationDAO  {

		/* (non-Javadoc)
		 * @see com.timeInc.ark.dao.PublicationGroupDAO#findByAd(java.util.Collection)
		 */
		@Override
		public List<PublicationGroup> findByAd(Collection<String> adGroups) {
			return getBaseCrit(adGroups).add(Restrictions.eq("active",true)).list();
		}
	}

}

