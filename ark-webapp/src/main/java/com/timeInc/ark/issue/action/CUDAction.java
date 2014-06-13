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
package com.timeInc.ark.issue.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.timeInc.ark.dao.GenericDAO.ConstraintViolation;
import com.timeInc.ark.dao.IssueMetaDAO;
import com.timeInc.ark.event.AbstractEvent;
import com.timeInc.ark.event.IssueMetaEvent;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.role.User;
import com.timeInc.ark.util.JMSTopicSender;
import com.timeInc.mageng.util.event.EventManager;

/**
 * (C)reate(U)pdate(D)elete IssueMeta objects
 */
public abstract class CUDAction extends IssueMetaAction<ConstraintViolation> {
	protected final IssueMetaDAO dao;

	CUDAction(EventManager<Class<? extends AbstractEvent>, AbstractEvent> em, User user, IssueMetaDAO dao) {
		super(em, user);
		this.dao = dao;
	}

	@Override
	protected final ConstraintViolation executeFn(IssueMeta meta) {
		return getModificationFn(meta);
	}

	abstract protected ConstraintViolation getModificationFn(IssueMeta meta);

	abstract protected IssueMetaEvent getLogType(String result, Date occurrence, PublicationGroup group, String issue, Date coverDate, String refid, String appName);

	@Override
	protected final IssueMetaEvent getLogType(ConstraintViolation result, Date occurrence,
			PublicationGroup group, String issue, Date coverDate,
			String refid, String appName) {

		return getLogType(result != null ? result.reason : "", occurrence, group, issue, coverDate, refid, appName);
	}


	/**
	 * Deletion {@link IssueMeta}
	 */
	public static class Delete extends CUDAction {
		
		/**
		 * Instantiates a new delete.
		 *
		 * @param em the em
		 * @param user the user
		 * @param dao the dao
		 */
		public Delete(EventManager<Class<? extends AbstractEvent>, AbstractEvent> em, User user, IssueMetaDAO dao) {
			super(em, user, dao);
		}

		@Override
		protected ConstraintViolation getModificationFn(final IssueMeta meta) {
			return dao.delete(meta);
		}

		@Override
		protected IssueMetaEvent getLogType(String result, Date occurrence,
				PublicationGroup group, String issue, Date coverDate,
				String refid, String appName) {
			return new IssueMetaEvent.Deletion(user, occurrence, result, group, issue, coverDate, refid, appName);
		}
	}

	/**
	 * Update {@link IssueMeta}
	 */
	public static class Update extends CUDAction {
		private final JMSTopicSender updateTopic;
		private static final Logger log = Logger.getLogger(Update.class);

		/**
		 * Instantiates a new update.
		 *
		 * @param em the em
		 * @param user the user
		 * @param dao the dao
		 * @param updateTopic the update topic
		 */
		public Update(EventManager<Class<? extends AbstractEvent>, AbstractEvent> em, User user, IssueMetaDAO dao, JMSTopicSender updateTopic) {
			super(em, user, dao);
			this.updateTopic = updateTopic;
		}

		@Override
		protected ConstraintViolation getModificationFn(final IssueMeta meta) {
			ConstraintViolation cf = dao.update(meta);

			if(cf == null)
				sendDataChangeMsg(meta);

			return cf;
		}

		@Override
		protected IssueMetaEvent getLogType(String result, Date occurrence,
				PublicationGroup group, String issue, Date coverDate,
				String refid, String appName) {
			return new IssueMetaEvent.Update(user, occurrence, result, group, issue, coverDate, refid, appName);
		}


		private void sendDataChangeMsg(IssueMeta meta) {
			try {
				Map<String,String> mapMsg = new HashMap<String,String>();
				mapMsg.put("tablename","issue_meta");
				mapMsg.put("recordid","" + meta.getId());

				updateTopic.sendMessage(mapMsg);
			} catch(Exception ex) {
				log.warn("Failed to send message", ex);
			}		
		}
	}

	/**
	 * Create {@link IssueMeta}
	 */
	public static class Create extends CUDAction {
		
		/**
		 * Instantiates a new creates the.
		 *
		 * @param em the em
		 * @param user the user
		 * @param dao the dao
		 */
		public Create(EventManager<Class<? extends AbstractEvent>, AbstractEvent> em, User user, IssueMetaDAO dao) {
			super(em, user, dao);
		}

		@Override
		protected ConstraintViolation getModificationFn(final IssueMeta meta) {
			return dao.create(meta);
		}

		@Override
		protected IssueMetaEvent getLogType(String result, Date occurrence,
				PublicationGroup group, String issue, Date coverDate,
				String refid, String appName) {
			return new IssueMetaEvent.Creation(user, occurrence, result, group, issue, coverDate, refid, appName);
		}
	}
}
