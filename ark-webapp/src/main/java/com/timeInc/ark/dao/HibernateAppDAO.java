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

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.timeInc.ark.application.Application;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.role.PublicationGroup;

@SuppressWarnings("rawtypes")
abstract class HibernateAppDao extends HibernateGenericDAO<Application, Integer> implements ApplicationDAO {
	HibernateAppDao() {
		super(Application.class);
	}
	
	protected Order getOrder() {
		return Order.asc("name"); 
	}
	
	protected Criterion getBase() {
		return Restrictions.eq("active", true);
	}
	
	@Override
	public List<Application<?,?>> getById(Integer... id) {
		return emptyListIfNull(getCriteria(Restrictions.in("id", id), getBase()).addOrder(getOrder()).list());
	}
	
	@Override
	public List<Application<?,?>> getByPublication(PublicationGroup... pgs) {
		return emptyListIfNull(getCriteria(Restrictions.in("publicationGroup", pgs), getBase()).addOrder(getOrder()).list());
	}
	
	/**
	 * The Class SuperAdminAppDao.
	 */
	static class SuperAdminAppDao extends HibernateAppDao {
		
		@Override
		protected Criterion getBase() {
			return Restrictions.or(Restrictions.eq("active", true), Restrictions.eq("active", false)) ;
		}
		

		/* (non-Javadoc)
		 * @see com.timeInc.ark.dao.ApplicationDAO#getAssociatedMetaFor(com.timeInc.ark.issue.Issue)
		 */
		@Override
		public List<Application<?, ?>> getAssociatedMetaFor(Issue issue) {
			Criteria crit = getCriteria().createAlias("metas","_meta")
			.add(Restrictions.eq("_meta.issue", issue))
			.addOrder(getOrder());

			return emptyListIfNull(crit.list());
		}
	}

	/**
	 * The Class AdminAppDao.
	 */
	static class AdminAppDao extends HibernateAppDao {
		
		@Override
		protected Criterion getBase() {
			return Restrictions.or(Restrictions.eq("active", true), Restrictions.eq("active", false)) ;
		}
		

		/* (non-Javadoc)
		 * @see com.timeInc.ark.dao.ApplicationDAO#getAssociatedMetaFor(com.timeInc.ark.issue.Issue)
		 */
		@Override
		public List<Application<?, ?>> getAssociatedMetaFor(Issue issue) {
			Criteria crit = getCriteria().add(getBase()).createAlias("metas","_meta")
			.add(Restrictions.eq("_meta.issue", issue))
			.addOrder(getOrder());

			return emptyListIfNull(crit.list());
		}
	}

	/**
	 * The Class UserAppDao.
	 */
	static class UserAppDao extends HibernateAppDao {

		/* (non-Javadoc)
		 * @see com.timeInc.ark.dao.ApplicationDAO#getAssociatedMetaFor(com.timeInc.ark.issue.Issue)
		 */
		@Override
		public List<Application<?, ?>> getAssociatedMetaFor(Issue issue) {
			Criteria crit = getCriteria().add(getBase()).createAlias("metas","_meta")
			.add(Restrictions.eq("_meta.issue", issue))
			.add(Restrictions.eq("_meta.active", true))
			.addOrder(getOrder());

			return emptyListIfNull(crit.list());
		}
	}
}


