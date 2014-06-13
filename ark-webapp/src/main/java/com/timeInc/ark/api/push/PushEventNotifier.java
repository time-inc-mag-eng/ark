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
package com.timeInc.ark.api.push;

import java.util.Date;

import com.timeInc.ark.dao.PushEventDAO;
import com.timeInc.ark.event.AbstractEvent;
import com.timeInc.ark.event.IssueMetaEvent.PushSchedule;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.role.User;
import com.timeInc.mageng.util.event.EventManager;

/**
 * Notifies the event manager that a push event occurred and creates a PushEvent entry.
 */
public class PushEventNotifier implements PushEventListener {
	private final EventManager<Class<? extends AbstractEvent>, AbstractEvent> wfEvent;
	private final PushEventDAO dao;
	
	/**
	 * Instantiates a new push event notifier.
	 *
	 * @param wfEvent the wf event
	 */
	public PushEventNotifier(EventManager<Class<? extends AbstractEvent>, AbstractEvent> wfEvent) {
		this.wfEvent = wfEvent;
		this.dao = new PushEventDAO();
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.api.push.PushEventListener#pushSent(java.util.Date, java.util.Date, java.lang.String, java.lang.String, com.timeInc.ark.issue.IssueMeta, com.timeInc.ark.role.User)
	 */
	@Override
	public void pushSent(Date scheduledDate, Date sentTime, 
			String pushId, String pushProductId,
			IssueMeta meta, User user) {
		
		dao.create(new PushEvent(pushProductId, scheduledDate, pushId, meta.getReferenceId()));
		
		wfEvent.notify(PushSchedule.class, new PushSchedule(user, sentTime, "" , meta));
	}
}
