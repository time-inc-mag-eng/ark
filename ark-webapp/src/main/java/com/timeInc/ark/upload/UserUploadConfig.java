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

import java.io.File;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.timeInc.ark.role.User;
import com.timeInc.mageng.util.compression.Unpacker;

/**
 * Value class that represents an upload request from a user
 */
public abstract class UserUploadConfig {
	private final User user;
	private final File workingDirectory;
	private final Unpacker unpacker;
	private final boolean newUpload;
	
	/**
	 * Instantiates a new user upload config.
	 *
	 * @param user the user
	 * @param workingDirectory the working directory
	 * @param uploadedFile the uploaded file
	 * @param newUpload the new upload
	 */
	public UserUploadConfig(User user, File workingDirectory, File uploadedFile, boolean newUpload) {
		this.user = user;
		this.workingDirectory = workingDirectory;
		this.newUpload = newUpload;
		this.unpacker = new Unpacker(uploadedFile, workingDirectory);
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
	 * Checks if is new upload.
	 *
	 * @return true, if is new upload
	 */
	public boolean isNewUpload() {
		return newUpload;
	}

	/**
	 * Gets the unpacker.
	 *
	 * @return the unpacker
	 */
	public Unpacker getUnpacker() {
		return unpacker;
	}

	/**
	 * Gets the working directory.
	 *
	 * @return the working directory
	 */
	public File getWorkingDirectory() {
		return workingDirectory;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
