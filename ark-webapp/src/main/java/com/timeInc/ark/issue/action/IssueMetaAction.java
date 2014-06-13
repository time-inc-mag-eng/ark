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

import com.timeInc.ark.event.AbstractEvent;
import com.timeInc.ark.event.IssueMetaEvent;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.role.User;
import com.timeInc.mageng.util.event.EventManager;


/**
 * An IssueMetaAction represents a certain action that is to be performed
 * for against an {@link IssueMeta}. It provides the normal the workflow event where
 * the action is performed and the action even is sent to the
 * {@link EventManager}
 *
 * @param <T> the status of the action
 */
public abstract class IssueMetaAction<T> {
	private final EventManager<Class<? extends AbstractEvent>, AbstractEvent> em;
	protected final User user;
	
	
	/**
	 * Instantiates a new issue meta action.
	 *
	 * @param em the em
	 * @param user the user
	 */
	public IssueMetaAction(EventManager<Class<? extends AbstractEvent>, AbstractEvent> em, User user) {
		this.em = em;
		this.user = user;
	}

	/**
	 * Perform action on the specified {@link IssueMeta}
	 *
	 * @param meta the meta
	 * @return the status after executing the action
	 */
	public T performActionOn(IssueMeta meta) {
		checkPrecondition(meta);
		
		Date occurence = new Date();
		PublicationGroup group = meta.getPublication();
		String issueName = meta.getIssueName();
		Date coverDate = meta.getCoverDate();
		String refId = meta.getReferenceId();
		String appName = meta.getApplicationName();
		
		T result = executeFn(meta);
		
		IssueMetaEvent logEntry = getLogType(result, occurence, group, issueName, coverDate, refId, appName);
		em.notify(logEntry.getClass(), logEntry);
		
		return result;
	}
	
	protected void checkPrecondition(IssueMeta meta) {}
	
	abstract protected T executeFn(IssueMeta meta);
	
	abstract protected IssueMetaEvent getLogType(T result, Date occurrence, PublicationGroup group, String issue, Date coverDate, String refid, String appName); 
}
