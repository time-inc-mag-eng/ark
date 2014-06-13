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


import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.timeInc.ark.event.AbstractEvent;
import com.timeInc.ark.event.IssueEvent;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.util.hibernate.HibernateUtil;
import com.timeInc.ark.util.hibernate.PageData;
import com.timeInc.ark.util.hibernate.PageData.PageRequest;
import com.timeInc.mageng.util.date.DateUtil;

/**
 * DAO for {@link AbstractEvent}
 */
public class EventDAO extends HibernateGenericDAO<AbstractEvent, Integer> {

	/**
	 * Returns the PageData of IssueLogs given a
	 * PageRequest for a certain PublicationGroup 
	 * going back a certain number of days.
	 *
	 * @param pub the pub
	 * @param daysBefore the days before
	 * @param request the request
	 * @return the PageData
	 */
	public PageData<IssueEvent> getPagedIssueLog(PublicationGroup pub, int daysBefore, PageRequest request) {		
		Criteria criteria = getBaseCriteria(pub, daysBefore)
				.addOrder(request.getDir().getOrder(request.getSortColumn()));

		long count = (Long) criteria.setProjection(Projections.rowCount()).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).uniqueResult();

		criteria.setProjection(null);
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		criteria.addOrder(request.getDir().getOrder(request.getSortColumn()));

		criteria.setFirstResult(request.getStartIndex()).setMaxResults(request.getPageSize());

		List<IssueEvent> result = criteria.list();
		
		result = result == null ? Collections.<IssueEvent>emptyList() : result;

		PageData<IssueEvent> data = new PageData<IssueEvent>(result, count);

		return data;
	}

	/**
	 * Gets a list of IssueLog for a PublicationGroup
	 * going back a certain number of days.
	 *
	 * @param pub the pub 
	 * @param daysBefore the days before
	 * @return a list of IssueLog
	 */
	public List<IssueEvent> getIssueLogBefore(PublicationGroup pub, int daysBefore) {
		List<IssueEvent> result = getBaseCriteria(pub,daysBefore).list();
		return result == null ? Collections.<IssueEvent>emptyList() : result;
	}

	private Criteria getBaseCriteria(PublicationGroup pub, int daysBefore) {
		return getCriteria(Restrictions.or(Restrictions.ge("date", DateUtil.getBefore(Calendar.DATE, daysBefore)), Restrictions.isNull("date")))
				.add(Restrictions.eq("publicationGroup", pub));
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.dao.HibernateGenericDAO#create(java.lang.Object)
	 */
	@Override
	public ConstraintViolation create(AbstractEvent entry) {
		Session unManagedSession = HibernateUtil.getNewSession();

		try {
			unManagedSession.beginTransaction();
			unManagedSession.save(entry);
			unManagedSession.getTransaction().commit();
			return null;
		} finally {
			unManagedSession.close();
		}
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public List<ConstraintViolation> create(List<AbstractEvent> objs) {
		throw new UnsupportedOperationException("Operation not supported");
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public ConstraintViolation update(AbstractEvent obj) {
		throw new UnsupportedOperationException("Operation not supported");		
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public Collection<ConstraintViolation> update(List<AbstractEvent> objs) {
		throw new UnsupportedOperationException("Operation not supported");		
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public ConstraintViolation delete(AbstractEvent obj) {
		throw new UnsupportedOperationException("Operation not supported");		
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public Collection<ConstraintViolation> delete(List<AbstractEvent> objs) {
		throw new UnsupportedOperationException("Operation not supported");		
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public ConstraintViolation deletebyId(Integer id) {
		throw new UnsupportedOperationException("Operation not supported");		
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public Collection<ConstraintViolation> deletebyId(List<Integer> ids) {
		throw new UnsupportedOperationException("Operation not supported");		
	}

}
