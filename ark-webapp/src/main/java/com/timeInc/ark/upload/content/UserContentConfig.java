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

import java.io.File;

import com.timeInc.ark.role.User;
import com.timeInc.ark.upload.UserUploadConfig;

/**
 * Value class that contains options specified by a user for content upload
 * @see UserUploadConfig
 */
public class UserContentConfig extends UserUploadConfig {
	private final boolean reuploadEntirely;
	
	/**
	 * Instantiates a new user content config.
	 *
	 * @param user the user
	 * @param workingDirectory the working directory
	 * @param uploadedFile the uploaded file
	 * @param reuploadEntirely the reupload entirely
	 */
	public UserContentConfig(User user, File workingDirectory, File uploadedFile, boolean reuploadEntirely) {
		super(user, workingDirectory, uploadedFile, reuploadEntirely);
		
		this.reuploadEntirely = reuploadEntirely;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.upload.UserUploadConfig#toString()
	 */
	@Override
	public String toString() {
		return "UserContentConfig [reuploadEntirely=" + reuploadEntirely
				+ ", toString()=" + super.toString() + "]";
	}
}
