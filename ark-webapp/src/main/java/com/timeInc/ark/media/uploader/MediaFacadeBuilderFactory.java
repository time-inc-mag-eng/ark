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
package com.timeInc.ark.media.uploader;

import com.timeInc.ark.event.AbstractEvent;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.media.MediaUploadFacade;
import com.timeInc.ark.media.MediaUploadFacade.LoggingMediaUploadEvent;
import com.timeInc.ark.upload.UploadFacade;
import com.timeInc.ark.upload.UploadFacadeBuilderFactory;
import com.timeInc.ark.upload.UserUploadConfig;
import com.timeInc.mageng.util.event.EventManager;

/**
 * A factory for constructing builder's which in turn construct {@link MediaUploadFacade}
 */
public class MediaFacadeBuilderFactory implements UploadFacadeBuilderFactory<Issue> {
	
	private final EventManager<Class<? extends AbstractEvent>, AbstractEvent> wfEvent; 
	
	/**
	 * Instantiates a new media facade factory producer.
	 *
	 * @param wfEvent the wf event
	 */
	public MediaFacadeBuilderFactory(EventManager<Class<? extends AbstractEvent>, AbstractEvent> wfEvent) {
		this.wfEvent = wfEvent;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.upload.UploadCommandFactory.UploadFacadeFactoryProducer#construct(com.timeInc.ark.upload.UserUploadConfig)
	 */
	@Override
	public UploadFacadeBuilder<Issue> construct(UserUploadConfig config) {
		return new MediaFacadeBuilder(config);
	}
	
	private class MediaFacadeBuilder implements UploadFacadeBuilder<Issue> {
		private final UserUploadConfig config;

		public MediaFacadeBuilder(UserUploadConfig config) {
			this.config = config;
		}
		
		@Override
		public UploadFacade<?> getUploadFacadeFor(Issue issue) {
			return new MediaUploadFacade(config, issue, new LoggingMediaUploadEvent(wfEvent));
		}
	}
}
