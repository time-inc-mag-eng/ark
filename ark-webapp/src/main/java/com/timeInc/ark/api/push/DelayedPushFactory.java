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

import com.timeInc.ark.event.AbstractEvent;
import com.timeInc.mageng.util.date.DateScheduler;
import com.timeInc.mageng.util.event.EventManager;

/**
 * A factory for creating DelayedPush dependencies.
 */
public class DelayedPushFactory {
	private static DateScheduler DEFAULT_SCHEDULER =  new DateScheduler() {
			@Override
			public Date reschedule(Date originalDate) {
				Date rightNow = new Date();
				
				if(originalDate.compareTo(rightNow) >= 0) 
					return originalDate;
				else
					return rightNow;
			}
		};
		
	private static PushAccess DEFAULT_PUSH_ACCESS = new DuplicatePushAccess();
	
	/**
	 * Gets the event listener.
	 *
	 * @param eventManager the event manager
	 * @return the event listener
	 */
	public PushEventListener getEventListener(EventManager<Class<? extends AbstractEvent>, AbstractEvent> eventManager) {
		return new PushEventNotifier(eventManager);
	}
	
	/**
	 * Gets the scheduler.
	 *
	 * @return the scheduler
	 */
	public DateScheduler getScheduler() {
		return DEFAULT_SCHEDULER;
	}
	
	/**
	 * Gets the push access.
	 *
	 * @return the push access
	 */
	public PushAccess getPushAccess() {
		return DEFAULT_PUSH_ACCESS; 
	}
}
