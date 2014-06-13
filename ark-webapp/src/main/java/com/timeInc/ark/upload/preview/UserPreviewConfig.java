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
package com.timeInc.ark.upload.preview;

import java.io.File;

import com.timeInc.ark.role.User;
import com.timeInc.ark.upload.UserUploadConfig;

/**
 * Value class that contains options specified by a user for preview upload
 * @see UserUploadConfig
 */
public class UserPreviewConfig extends UserUploadConfig {
	private final String coverStory;
	private final String newsstandCover;

	/**
	 * Instantiates a new user preview config.
	 *
	 * @param user the user
	 * @param workingDirectory the working directory
	 * @param uploadedFile the uploaded file
	 * @param coverStory the cover story
	 * @param newsstandCover the newsstand cover
	 */
	public UserPreviewConfig(User user, File workingDirectory, File uploadedFile, String coverStory, String newsstandCover) {
		super(user, workingDirectory, uploadedFile, true);
		
		this.coverStory = coverStory;
		this.newsstandCover = newsstandCover;
	}
	
	/**
	 * Gets the cover story.
	 *
	 * @return the cover story
	 */
	public String getCoverStory() {
		return coverStory;
	}

	/**
	 * Gets the newsstand cover.
	 *
	 * @return the newsstand cover
	 */
	public String getNewsstandCover() {
		return newsstandCover;
	}
}
