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

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.timeInc.ark.role.User;
import com.timeInc.ark.upload.UserUploadConfig;
import com.timeInc.ark.upload.content.ContentUploadSetting;
import com.timeInc.mageng.util.string.StringUtil;

/**
 * Value class that gets passed to 
 * {@link com.timeInc.ark.application.Application#uploadPreview(com.timeInc.ark.packager.PackageInfo, com.timeInc.ark.issue.IssueMeta, PreviewUploadSetting)}
 * 
 */
public class PreviewUploadSetting {
	private final User user;
	private final String coverStory;
	private final File tempDir;
	private final String newsStandCover;
	
	/**
	 * Instantiates a new preview upload setting.
	 *
	 * @param user the user
	 * @param coverStory the cover story
	 * @param newsStandCover the news stand cover
	 * @param tempDir the temp dir
	 */
	public PreviewUploadSetting(User user, String coverStory, String newsStandCover, File tempDir) {
		this.user = user;
		this.coverStory = coverStory;
		this.newsStandCover = newsStandCover;
		this.tempDir = tempDir;
	}
	
	/**
	 * Instantiates a new preview upload setting.
	 *
	 * @param user the user
	 * @param coverStory the cover story
	 * @param tempDir the temp dir
	 */
	public PreviewUploadSetting(User user, String coverStory, File tempDir) {
		this(user, coverStory, coverStory, tempDir);
	}
	
	/**
	 * Gets the user email.
	 *
	 * @return the user email
	 */
	public String getUserEmail() {
		return user.getEmail();
	}
	
	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	
	
	/**
	 * Checks for newsstand description.
	 *
	 * @return true, if successful
	 */
	public boolean hasNewsstandDescription() {
		return !StringUtil.isEmpty(newsStandCover); 
	}
	
	/**
	 * Gets the NS description.
	 *
	 * @return the NS description
	 */
	public String getNSDescription() {
		if(hasNewsstandDescription())
			return newsStandCover;
		else
			throw new IllegalStateException("No newsstand cover story!");
	}
	
	/**
	 * Checks for cover story.
	 *
	 * @return true, if successful
	 */
	public boolean hasCoverStory() {
		return !StringUtil.isEmpty(coverStory); 
	}
	
	/**
	 * Gets the cover story.
	 *
	 * @return the cover story
	 */
	public String getCoverStory() {
		if(hasCoverStory())
			return coverStory;
		else
			throw new IllegalStateException("No cover story!");
	}

	/**
	 * Gets the temp dir.
	 *
	 * @return the temp dir
	 */
	public File getTempDir() {
		return tempDir;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
