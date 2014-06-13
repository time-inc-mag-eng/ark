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
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.timeInc.ark.application.Application;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.util.hibernate.CriterionFilter;
import com.timeInc.ark.util.hibernate.PageData;
import com.timeInc.ark.util.hibernate.PageData.PageRequest;
import com.timeInc.mageng.util.filter.Filter;
import com.timeInc.mageng.util.filter.FilterAttribute;

public abstract class HibernateIssueMetaDAO extends HibernateGenericDAO<IssueMeta,Integer> implements IssueMetaDAO {

	/**
	 * Instantiates a new hibernate issue meta dao.
	 */
	public HibernateIssueMetaDAO() {
		super(IssueMeta.class);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.dao.IssueMetaDAO#getById(java.util.List)
	 */
	@Override
	public final List<IssueMeta> getById(List<Integer> ids) {
		return findByCriteria(Order.asc("onSaleDate"), Restrictions.in("id",ids));
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.dao.IssueMetaDAO#getBy(com.timeInc.ark.issue.Issue, java.util.List)
	 */
	@Override
	public List<IssueMeta> getBy(Issue issue, List<Application<?,?>> apps) {
		return findByCriteria(Restrictions.eq("issue", issue), Restrictions.in("application", apps));
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.dao.IssueMetaDAO#getBy(com.timeInc.ark.application.Application)
	 */
	@Override 
	public List<IssueMeta> getBy(Application<?,?> app) {
		return findByCriteria(Restrictions.eq("application", app));
	}


	protected Criteria critForApp(Application<?,?> app) {
		return getCriteria(Restrictions.eq("application", app));
	}

	@Override
	protected Op<IssueMeta> createOrUpdateConstraint() { 
		return new Op<IssueMeta>() {
			@Override
			public ConstraintViolation fn(IssueMeta meta, int index) {
				Criteria crit = getCriteria(Restrictions.eq("referenceId", meta.getReferenceId()),
						Restrictions.eq("application.id", meta.getApplication().getId()));

				if(meta.getId() != null) {
					crit.add(Restrictions.not(Restrictions.eq("id", meta.getId())));
				}

				List<IssueMeta> dup = crit.list();	

				if(!dup.isEmpty()) 
					return new ConstraintViolation(String.valueOf(meta.getId() == null ? index : meta.getId()), meta.getReferenceId() + " already exists in " + meta.getApplicationName(), index);
				else
					return null;
			}
		};
	}


	/**
	 * The Class UserIssueMetaDAO.
	 */
	static class UserIssueMetaDAO extends HibernateIssueMetaDAO {

		/* (non-Javadoc)
		 * @see com.timeInc.ark.dao.IssueMetaDAO#getWithFilterBy(com.timeInc.ark.application.Application, com.timeInc.ark.util.hibernate.PageData.PageRequest, java.util.List)
		 */
		@Override
		public PageData<IssueMeta> getWithFilterBy(Application<?, ?> app, PageRequest request, List<? extends FilterAttribute<Object>> filters) {
			throw new UnsupportedOperationException("Not yet implemented");
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.dao.IssueMetaDAO#getBy(com.timeInc.ark.application.Application, com.timeInc.ark.util.hibernate.PageData.PageRequest)
		 */
		@Override
		public PageData<IssueMeta> getBy(Application<?, ?> app, PageRequest request) {
			throw new UnsupportedOperationException("Not yet implemented");
		}
	}

	/**
	 * The Class IssueMetaManagerDAO.
	 */
	static class IssueMetaManagerDAO extends HibernateIssueMetaDAO {

		@Override
		protected Op<IssueMeta> deleteConstraint() {
			return new Op<IssueMeta>() {
				@Override
				public ConstraintViolation fn(IssueMeta meta, int index) {
					if(meta.getFolio() != null) 
						return new ConstraintViolation(String.valueOf(meta.getId() == null ? index : meta.getId()), meta.getReferenceId() + " has an associated folio", index);
					else
						return null;
				}
			};			
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.dao.IssueMetaDAO#getWithFilterBy(com.timeInc.ark.application.Application, com.timeInc.ark.util.hibernate.PageData.PageRequest, java.util.List)
		 */
		@Override
		public PageData<IssueMeta> getWithFilterBy(Application<?, ?> app, PageRequest request, List<? extends FilterAttribute<Object>> filters) {
			long count = 0; // unsigned int

			Criteria crit = critForApp(app).createAlias("issue", "_is");
			Criteria critCount = critForApp(app).createAlias("issue", "_is");

			if(filters != null) {
				for(FilterAttribute<Object> attrib : filters) {
					Filter<Criterion> critGenerator = attrib.getField().equals("shortDate") ? new CriterionFilter(attrib, "_is") :  new CriterionFilter(attrib);
					crit.add(critGenerator.retrieveFilter());
					critCount.add(critGenerator.retrieveFilter());
				}
			}

			count = (Long) critCount.setProjection(Projections.rowCount()).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).uniqueResult();

			if(request.getSortColumn().equals("issueNameId")) { // sort by the issue association using its name property
				Order order = request.getDir().getOrder("_is.name");
				crit.addOrder(order);
			} else if(request.getSortColumn().equals("shortDate")) { // sort by the issue association using its name property
				Order order = request.getDir().getOrder("_is.shortDate");
				crit.addOrder(order);
			} else {
				Order order = request.getDir().getOrder(request.getSortColumn());
				crit.addOrder(order);
			}

			List<IssueMeta> result = crit.setFirstResult(request.getStartIndex()).setMaxResults(request.getPageSize()).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();	

			PageData<IssueMeta> pd = new PageData<IssueMeta>(result, count);

			return pd;
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.dao.IssueMetaDAO#getBy(com.timeInc.ark.application.Application, com.timeInc.ark.util.hibernate.PageData.PageRequest)
		 */
		@Override
		public PageData<IssueMeta> getBy(Application<?, ?> app, PageRequest request) {
			return getWithFilterBy(app, request, null);
		}		
	}


	/**
	 * The Class AdminIssueMetaDAO.
	 */
	static class AdminIssueMetaDAO extends IssueMetaManagerDAO {
		@Override
		protected Op<IssueMeta> deleteConstraint() { return HibernateGenericDAO.NULL_OP; }
	}	
}
