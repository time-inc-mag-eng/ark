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
import java.util.Set;

import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.media.MediaType;

/**
 * Uploader for media files for a particular {@link Issue}
 */
public interface MediaUploader {
	/**
	 * Uploads a media file for an {@link Issue}
	 * @param issue the issue
	 * @param file the media file
	 */
	void upload(Issue issue, File file);
	
	
	/**
	 * Get supported media type that this uploader supports
	 * @return
	 */
	Set<MediaType> getSupportedMediaType();
}