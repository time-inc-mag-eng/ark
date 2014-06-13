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

import com.timeInc.ark.role.User;

/**
 * Value class that gets passed to 
 * {@link com.timeInc.ark.application.Application#uploadContent(com.timeInc.ark.packager.PackageInfo, com.timeInc.ark.issue.IssueMeta, ContentUploadSetting, com.timeInc.mageng.util.progress.ProgressListener)}
 * 
 */
public class ContentUploadSetting {
	private final User user;
	
	/**
	 * Instantiates a new content upload setting.
	 *
	 * @param user the user
	 */
	public ContentUploadSetting(User user) {
		super();
		this.user = user;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return user.getEmail();
	}
}
