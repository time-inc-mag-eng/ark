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
package com.timeInc.ark.upload.content;

import com.timeInc.ark.backup.Backup;
import com.timeInc.ark.event.AbstractEvent;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.upload.UploadCommandFactory.UploadMetaFacadeFactoryProducer;
import com.timeInc.ark.upload.UploadFacade;
import com.timeInc.ark.upload.UploadListener;
import com.timeInc.ark.upload.UserUploadConfig;
import com.timeInc.ark.util.JMSTopicSender;
import com.timeInc.mageng.util.event.EventManager;

/**
 * Producer of UploadFacadeBuilder<IssueMeta> for content uploads. 
 */
public class ContentFacadeFactoryProducer extends UploadMetaFacadeFactoryProducer {
	private final JMSTopicSender sender;
	
	/**
	 * Instantiates a new content facade factory producer.
	 *
	 * @param userEvent the user event
	 * @param sender the sender
	 * @param backup the backup
	 */
	public ContentFacadeFactoryProducer(EventManager<Class<? extends AbstractEvent>, AbstractEvent> userEvent, JMSTopicSender sender, Backup backup) {
		super(userEvent, backup);
		this.sender = sender;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.upload.UploadCommandFactory.UploadMetaFacadeFactoryProducer#construct(com.timeInc.ark.upload.UserUploadConfig)
	 */
	@Override
	public UploadFacadeBuilder<IssueMeta> construct(UserUploadConfig config) {
		UserContentConfig castedConfig = (UserContentConfig) config;
		return new Builder(castedConfig, new ContentUploadListener(sender, userEvent, backup, castedConfig));
	}
	
	private static class Builder implements UploadFacadeBuilder<IssueMeta> {
		private final UserContentConfig config;
		private final UploadListener listener;

		public Builder(UserContentConfig config, UploadListener listener) {
			this.config = config;
			this.listener = listener;
		}
		
		@Override
		public UploadFacade<?> getUploadFacadeFor(IssueMeta meta) {
			return new ContentUploadFacade(config, meta, listener);
		}
	}
}
