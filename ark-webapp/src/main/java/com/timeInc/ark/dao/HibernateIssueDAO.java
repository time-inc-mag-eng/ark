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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.timeInc.ark.application.Application;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.mageng.util.date.DateUtil;

abstract class HibernateIssueDao extends HibernateGenericDAO<Issue,Integer> implements IssueDAO {

	public HibernateIssueDao() {
		super(Issue.class);
	}

	@Override
	protected Op<Issue> createOrUpdateConstraint() {
		return new Op<Issue>() {
			public ConstraintViolation fn(Issue issue, int index) {
				List<Issue> equalIssue = getIssuesWith(issue.getPublication(), issue.getName());

				boolean matched = false;

				if(issue.getId() != null) {
					for(Issue existing: equalIssue) {
						if(existing.getId().equals(issue.getId())) {
							matched = true;
							break;
						}
					}
				}

				if(equalIssue.size() > 0 && (issue.getId() == null || !matched)) // new entry or a non-matched one
					return new ConstraintViolation(String.valueOf(issue.getId() == null ? index : issue.getId()), issue.getName() + " already exists in " + issue.getPublication().getName(), index);

				return null;
			}
		};
	}

	@Override
	protected Op<Issue> deleteConstraint() {
		return new Op<Issue>() {
			public ConstraintViolation fn(Issue issue, int index) {
				Query metaQuery = getSession().createQuery("from IssueMeta m where m.issue = :issue");
				metaQuery.setParameter("issue", issue);

				boolean emptyMeta = emptyListIfNull(metaQuery.list()).isEmpty();

				Query mediaQuery = getSession().createQuery("from MediaLocation om where om.issue = :issue");
				mediaQuery.setParameter("issue", issue);

				boolean emptyMedia = emptyListIfNull(mediaQuery.list()).isEmpty();

				return (!emptyMedia || !emptyMeta) ? new ConstraintViolation(String.valueOf(issue.getId()), "An issuemeta or media upload is associated with " + issue.getName(), index) 
				: null;
			}
		};
	}		

	private List<Issue> getIssuesWith(PublicationGroup group, String issueName) {
		return findByCriteria(Restrictions.eq("publication", group), Restrictions.eq("name", issueName));
	}

	@Override
	public final List<Issue> getAllIssue(PublicationGroup pub) {
		return getBaseCriteria(pub).list();
	}


	@Override
	public List<Issue> getIssueWithNoMeta(Application<?,?> app) {
		Query q = getSession().createQuery("from Issue i where i.publication = :pub and i not in (select m.issue from IssueMeta m where m.application = :app)");		
		q.setParameter("pub", app.getPublicationGroup());
		q.setParameter("app",app);
		return emptyListIfNull(q.list());
	}


	protected Criteria getBaseCriteria(PublicationGroup pub) {
		return getCriteria(Restrictions.eq("publication", pub)).addOrder(Order.asc("shortDate"));
	}

	protected Criteria getBaseCriteriaMeta(PublicationGroup pub) {
		return getBaseCriteria(pub).createAlias("metas", "_meta");
	}

	// returns non-duplicate issue as defined by Issue equals() method. If they are equal later short dates have precedence.
	protected List<Issue> filterDuplicate(List<Issue> issues) { 
		if(issues == null) 
			return Collections.emptyList();

		Map<Issue, Issue> nondup = new HashMap<Issue, Issue>();

		for(Issue issue : issues) { 
			if(!nondup.containsKey(issue)) {
				nondup.put(issue, issue);
			} else {
				Issue existing = nondup.get(issue);
				if(existing.getShortDate().compareTo(issue.getShortDate()) < 0) {
					nondup.put(issue, issue);
				}
			}
		}

		return new ArrayList<Issue>(nondup.values());
	}

	/**
	 * The Class AdminIssueDao.
	 */
	static class AdminIssueDao extends HibernateIssueDao {

		/* (non-Javadoc)
		 * @see com.timeInc.ark.dao.IssueDAO#getIssueWithMeta(com.timeInc.ark.role.PublicationGroup)
		 */
		@Override
		public List<Issue> getIssueWithMeta(PublicationGroup pub) {
			return filterDuplicate(getBaseCriteriaMeta(pub).list());
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.dao.IssueDAO#getIssue(com.timeInc.ark.role.PublicationGroup)
		 */
		@Override
		public List<Issue> getIssue(PublicationGroup pub) {
			return filterDuplicate(getBaseCriteria(pub).list());
		}
	}

	/**
	 * The Class UserIssueDao.
	 */
	static class UserIssueDao extends HibernateIssueDao {

		/* (non-Javadoc)
		 * @see com.timeInc.ark.dao.IssueDAO#getIssueWithMeta(com.timeInc.ark.role.PublicationGroup)
		 */
		@Override
		public List<Issue> getIssueWithMeta(PublicationGroup pub) {
			Date start = DateUtil.getBefore(DateUtil.presentDay(), Calendar.DATE, pub.getIssueRetention());
			Criteria crit = getBaseCriteriaMeta(pub).add(Restrictions.ge("shortDate", start));
			return filterDuplicate(crit.list());
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.dao.IssueDAO#getIssue(com.timeInc.ark.role.PublicationGroup)
		 */
		@Override
		public List<Issue> getIssue(PublicationGroup pub) {
			Date start = DateUtil.getBefore(DateUtil.presentDay(), Calendar.DATE, pub.getMediaIssueRetention());
			Criteria crit = getBaseCriteria(pub).add(Restrictions.ge("shortDate", start));
			return filterDuplicate(crit.list());
		}
	} 


}
