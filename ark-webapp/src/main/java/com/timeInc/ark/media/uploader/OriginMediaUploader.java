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
import java.io.Serializable;
import java.util.Set;

import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.media.MediaPathNaming;
import com.timeInc.ark.media.MediaType;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.uploader.OriginPreviewUploader;

/**
 * Uploads media files to an origin server.
 */
public class OriginMediaUploader implements MediaUploader, Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private Set<MediaType> mediaType;

	private MediaUploadListener uploadListener;

	private MediaPathNaming naming;

	private OriginPreviewUploader uploader;
	
	private PublicationGroup group;
	
	protected OriginMediaUploader() {}

	/**
	 * Instantiates a new origin media uploader.
	 *
	 * @param naming the naming to use for the file to be uploaded
	 * @param uploadListener the upload listener
	 */
	public OriginMediaUploader(MediaPathNaming naming, MediaUploadListener uploadListener) {
		this.naming = naming;
		this.uploadListener = uploadListener;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.media.uploader.MediaUploader#upload(com.timeInc.ark.issue.Issue, java.io.File)
	 */
	@Override
	public void upload(Issue issue, File file) {
		String relativePathToFile = naming.getPath(issue, file);

		uploader.uploadPreview(file, relativePathToFile);
		uploadListener.uploadComplete(issue, uploader.getCdnUrl(relativePathToFile), uploader.getOriginUrl(relativePathToFile), relativePathToFile, file);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.media.uploader.MediaUploader#getSupportedMediaType()
	 */
	public Set<MediaType> getSupportedMediaType() {
		return mediaType;
	}

	/**
	 * Sets the upload listener.
	 *
	 * @param uploadListener the new upload listener
	 */
	public void setUploadListener(MediaUploadListener uploadListener) {
		this.uploadListener = uploadListener;
	}

	/**
	 * Sets the naming.
	 *
	 * @param naming the new naming
	 */
	public void setNaming(MediaPathNaming naming) {
		this.naming = naming;
	}
}
