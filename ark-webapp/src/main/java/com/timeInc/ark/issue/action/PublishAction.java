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

import com.timeInc.ark.application.ManagedApplication;
import com.timeInc.ark.event.AbstractEvent;
import com.timeInc.ark.event.IssueMetaEvent;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.publish.Publisher.PublishType;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.role.User;
import com.timeInc.mageng.util.event.EventManager;
import com.timeInc.mageng.util.misc.Status;

/**
 * Publishes content for an IssueMeta
 */
public class PublishAction extends IssueMetaAction<Status> {

	/**
	 * Instantiates a new publish action.
	 *
	 * @param em the em
	 * @param user the user
	 */
	public PublishAction(
			EventManager<Class<? extends AbstractEvent>, AbstractEvent> em, User user) {
		super(em, user);
	}

	@Override
	protected Status executeFn(IssueMeta meta) {
		ManagedApplication<?,?> managedApp = meta.getApplication(ManagedApplication.class);
		Status status = managedApp.publish(meta, user.getEmail(), PublishType.CONTENT);		
		return status;
	}

	@Override
	protected IssueMetaEvent getLogType(Status result, Date occurrence,
			PublicationGroup group, String issue, Date coverDate, String refid,
			String appName) {
		
		return new IssueMetaEvent.PublishContent(user, occurrence, result.getDescription(), group, issue, coverDate, refid, appName);
	}
}
