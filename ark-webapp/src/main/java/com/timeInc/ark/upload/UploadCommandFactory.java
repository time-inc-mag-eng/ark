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
package com.timeInc.ark.upload;

import java.util.ArrayList;
import java.util.List;

import com.timeInc.ark.backup.Backup;
import com.timeInc.ark.event.AbstractEvent;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.mageng.util.event.EventManager;
import com.timeInc.mageng.util.progress.ProgressableCommand;

/**
 * A factory that produces a list of ProgressableCommand to be submitted
 * to a ProgressExecutor.
 * 
 * See {@link #getUploadCommandFor(List, UserUploadConfig)} for more information
 *
 * @param <T> the generic type
 */
public class UploadCommandFactory<T> {
	private final UploadFacadeBuilderFactory<T> factory;

	/**
	 * Instantiates a new upload command factory.
	 *
	 * @param factory the factory
	 */
	public UploadCommandFactory(UploadFacadeBuilderFactory<T> factory) {
		this.factory = factory;
	}

	/**
	 * Produces a list of OpenSessionCommands that are a one to one mapping in order between the specified 
	 * domainObjs using the UserUploadConfig
	 *
	 * @param domainObjs the domain objs
	 * @param config the config
	 * @return the upload command for
	 */
	public List<ProgressableCommand> getUploadCommandFor(List<T> domainObjs, UserUploadConfig config) {
		List<ProgressableCommand> commands = new ArrayList<ProgressableCommand>(domainObjs.size());

		for(T domainObj : domainObjs) {
			ProgressableCommand command = new OpenSessionCommand<T>(domainObj, factory.construct(config));
			commands.add(command);
		}

		return commands;
	}
	
	/**
	 * Abstract implementation where an EventManager and Backup is required to construct a
	 * UploadFacadeBuilder for an IssueMeta upload.
	 */
	public static abstract class UploadMetaFacadeFactoryProducer implements UploadFacadeBuilderFactory<IssueMeta> {
		protected final EventManager<Class<? extends AbstractEvent>, AbstractEvent> userEvent;
		protected final Backup backup;
		
		/**
		 * Instantiates a new upload meta facade factory producer.
		 *
		 * @param userEvent the user event
		 * @param backup the backup
		 */
		public UploadMetaFacadeFactoryProducer(EventManager<Class<? extends AbstractEvent>, AbstractEvent> userEvent, Backup backup) {
			this.userEvent = userEvent;
			this.backup = backup;
		}
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.upload.UploadFacadeBuilderFactory#construct(com.timeInc.ark.upload.UserUploadConfig)
		 */
		public abstract UploadFacadeBuilder<IssueMeta> construct(UserUploadConfig config); 
	}
}
