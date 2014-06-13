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
package com.timeInc.ark.packager;

import java.io.File;

/**
 * Value class that represents only one packaged file
 */
public class SingleFileInfo implements PackageInfo {
	private final File singleFile;
	
	/**
	 * Instantiates a new single file info.
	 *
	 * @param singleFile the single file
	 */
	public SingleFileInfo(File singleFile) {
		this.singleFile = singleFile;
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.packager.PackageInfo#getSize()
	 */
	@Override
	public long getSize() {
		return singleFile.length();
	}

	/**
	 * Gets the single file.
	 *
	 * @return the single file
	 */
	public File getSingleFile() {
		return singleFile;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.packager.PackageInfo#getPrettyName()
	 */
	@Override
	public String getPrettyName() {
		return singleFile.getName();
	}
}
