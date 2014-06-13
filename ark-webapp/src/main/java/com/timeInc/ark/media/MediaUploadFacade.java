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
package com.timeInc.ark.media;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.timeInc.ark.dao.MediaUploadDAO;
import com.timeInc.ark.event.AbstractEvent;
import com.timeInc.ark.event.IssueEvent;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.media.uploader.MediaUploader;
import com.timeInc.ark.role.User;
import com.timeInc.ark.upload.UploadFacade;
import com.timeInc.ark.upload.UserUploadConfig;
import com.timeInc.mageng.util.event.EventManager;
import com.timeInc.mageng.util.file.FileValidator;
import com.timeInc.mageng.util.misc.Status;
import com.timeInc.mageng.util.progress.DetailedProgressListener;
import com.timeInc.mageng.util.string.StringUtil;

/**
 * Uploads media files located in a folder, without recursively traversing the folder structure.
 */
public class MediaUploadFacade extends UploadFacade<UserUploadConfig> {
	private static final Logger log = Logger.getLogger(MediaUploadFacade.class);

	private final Issue issue;
	private final MediaFacadeUploadEvent listener;
	private final MediaUploadDAO mediaDao;

	/**
	 * Instantiates a new media upload facade.
	 *
	 * @param config the config
	 * @param issue the issue
	 * @param listener the listener
	 * @param mediaDao the media dao
	 */
	public MediaUploadFacade(UserUploadConfig config, Issue issue, MediaFacadeUploadEvent listener, MediaUploadDAO mediaDao) {
		super(config);

		this.issue = issue;
		this.listener = listener;
		this.mediaDao = mediaDao;
	}

	/**
	 * Instantiates a new media upload facade.
	 *
	 * @param config the config
	 * @param issue the issue
	 * @param listener the listener
	 */
	public MediaUploadFacade(UserUploadConfig config, Issue issue, MediaFacadeUploadEvent listener) {
		this(config, issue, listener, new MediaUploadDAO());
	}

	@Override
	protected boolean requiresUnpacked() {
		return false;
	}

	@Override
	protected Status delegateUpload(DetailedProgressListener progress, File mediaFolder, File workingDirectory) throws Exception {

		int totalItems = mediaFolder.list().length;

		MediaUploader originMedia = mediaDao.getUploaderFor(issue);

		List<String> errorMsg = new ArrayList<String>();

		FileValidator validator = new CaseInsensitiveExtensionValidator(originMedia.getSupportedMediaType());

		for(File mediaFile : mediaFolder.listFiles()) {
			progress.progressStarted("Uploading " + mediaFile.getName(), 100 / totalItems);

			if(mediaFile.isFile()) {
				String fileName = mediaFile.getName();

				log.debug("Trying to upload file... " + mediaFile + " for issue " + issue.getName());

				Status vInfo = validator.validate(mediaFile);

				if(!vInfo.isError()) {
					originMedia.upload(issue, mediaFile);
					listener.mediaUploaded(config.getUser(), issue, fileName);
				} else {
					errorMsg.add(vInfo.getDescription());
					listener.mediaUploaded(config.getUser(), issue, vInfo.getDescription());
				}
			} 

			progress.inProgress(1, 1); 
		}

		return errorMsg.isEmpty() ? Status.getSuccess() : Status.getFailure(StringUtil.mkString(", ", errorMsg));
	}


	/**
	 * Notifies the EventManager of the media upload result.
	 */
	public static class LoggingMediaUploadEvent implements MediaFacadeUploadEvent {

		private final EventManager<Class<? extends AbstractEvent>, AbstractEvent> wfEvent;

		/**
		 * Instantiates a new logging media upload event.
		 *
		 * @param wfEvent the wf event
		 */
		public LoggingMediaUploadEvent(EventManager<Class<? extends AbstractEvent>, AbstractEvent> wfEvent) {
			this.wfEvent = wfEvent;
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.media.MediaFacadeUploadEvent#mediaUploaded(com.timeInc.ark.role.User, com.timeInc.ark.issue.Issue, java.lang.String)
		 */
		@Override
		public void mediaUploaded(User user, Issue issue, String result) {
			wfEvent.notify(IssueEvent.MediaUpload.class, new IssueEvent.MediaUpload(user, new Date(), result, issue));
		}
	}
}
