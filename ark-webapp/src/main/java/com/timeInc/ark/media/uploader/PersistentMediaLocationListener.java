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

import java.io.File;

import org.apache.log4j.Logger;

import com.timeInc.ark.dao.MediaLocationDAO;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.media.MediaLocation;

/**
 * Persists a {@link MediaLocation} using the provided parameters for
 * {@link MediaUploadListener#uploadComplete(Issue, String, String, String, File)}
 *  by using {@link MediaLocationDAO}
 */
public class PersistentMediaLocationListener implements MediaUploadListener {
	private static final Logger log = Logger.getLogger(PersistentMediaLocationListener.class);

	private final MediaLocationDAO mediaDAO;

	/**
	 * Instantiates a new persistent media location listener.
	 *
	 * @param mediaDAO the media dao
	 */
	public PersistentMediaLocationListener(MediaLocationDAO mediaDAO) {
		this.mediaDAO = mediaDAO;
	}
	
	
	/**
	 * Instantiates a new persistent media location listener.
	 */
	public PersistentMediaLocationListener() {
		this(new MediaLocationDAO());
	}


	/* (non-Javadoc)
	 * @see com.timeInc.ark.media.uploader.MediaUploadListener#uploadComplete(com.timeInc.ark.issue.Issue, java.lang.String, java.lang.String, java.lang.String, java.io.File)
	 */
	@Override
	public void uploadComplete(Issue issue, String cdnUrl, String previewUrl, String relativePath, File file) {
		MediaLocation location = mediaDAO.find(issue, file.getName());

		if(location != null) {
			log.debug("Previously uploaded location found @ " + issue.getName() + " " + location.getFileName());
			location.fileUpdated(previewUrl, cdnUrl, relativePath);
			mediaDAO.update(location);
		} else {
			log.debug("New media location @ " + issue.getName() + " " +  cdnUrl);
			location = new MediaLocation(issue, file.getName(), previewUrl, cdnUrl, relativePath);
			mediaDAO.create(location);
		}
	}
}
