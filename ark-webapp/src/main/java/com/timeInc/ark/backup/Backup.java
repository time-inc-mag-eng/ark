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

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.packager.PackageInfo;

/**
 * Backs up user uploaded files for a particular IssueMeta
 */
public interface Backup {
	
	/**
	 * Backup user uploaded files
	 * @param meta the meta associated
	 * @param packMetaData the package that was sent 
	 * @param content the original uploaded file
	 * @param decompressedContent the decompressed file if it was a zip otherwise null
	 * @param newUpload if this was the first time content was uploaded
	 * @param tempDirectory working directory
	 * @return the location of the backed up file.
	 */
	File backup(IssueMeta meta, PackageInfo packMetaData, File content, File decompressedContent, 
			boolean newUpload, File tempDirectory);
}
