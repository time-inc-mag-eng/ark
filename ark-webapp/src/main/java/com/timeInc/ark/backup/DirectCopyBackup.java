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
package com.timeInc.ark.backup;

import java.io.File;

import com.timeInc.ark.backup.Storage.Location;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.packager.PackageInfo;

/**
 * Copies the user uploaded file directly.
 */
public class DirectCopyBackup implements Backup {
	private final Location location;
	private final Storage storage;
	
	/**
	 * Instantiates a new direct copy backup.
	 *
	 * @param location the location
	 * @param storage the storage
	 */
	public DirectCopyBackup(Location location, Storage storage) {
		this.location = location;
		this.storage = storage;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.backup.Backup#backup(com.timeInc.ark.issue.IssueMeta, com.timeInc.ark.packager.PackageInfo, java.io.File, java.io.File, boolean, java.io.File)
	 */
	@Override
	public File backup(IssueMeta meta, PackageInfo packMetaData, File content,
			File decompressedContent, boolean newUpload, File tempDirectory) {
		File destination = location.getLocation(meta, content);
		storage.save(content, destination);
		return destination;
	}

}
