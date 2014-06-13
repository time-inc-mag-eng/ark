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
package com.timeInc.ark.event;

import org.apache.log4j.Logger;

import com.timeInc.ark.dao.EventDAO;
import com.timeInc.mageng.util.event.EventListener;


/**
 * The listener for receiving event's of class type {@link AbstractEvent}.
 * Persists the events.
 */
public class PersistentLogEventListener implements EventListener<Class<? extends AbstractEvent>, AbstractEvent> {
	private static final Logger log = Logger.getLogger(PersistentLogEventListener.class);
	
	private final EventDAO eventDAO;
	
	public PersistentLogEventListener(EventDAO eventDAO) {
		this.eventDAO = eventDAO;
	}
	
	/**
	 * Instantiates a new logging event listener.
	 */
	public PersistentLogEventListener() {
		this(new EventDAO());
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.util.event.EventListener#handleEvent(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void handleEvent(Class<? extends AbstractEvent> eventType, AbstractEvent eventConfig) {
		log.debug("Handling event of type:" + eventType);
		eventDAO.create(eventConfig);			
	}
}
