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
package com.timeInc.ark.packager.preview;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.timeInc.ark.packager.PackageInfo;
import com.timeInc.mageng.util.misc.Precondition;
import com.timeInc.mageng.util.string.StringUtil;

/**
 * Package information for a {@link com.timeInc.ark.application.ManagedApplication}. 
 */
public class ManagedPackage implements PackageInfo {
	private final String nsFileName;
	private final File previewImageDir;
	
	/**
	 * Instantiates a new managed package.
	 *
	 * @param previewImageDir the preview image directory
	 * @param nsFileName the newsstand cover image filename in the previewImageDir
	 */
	public ManagedPackage(File previewImageDir, String nsFileName) {
		if(!StringUtil.isEmpty(nsFileName))
			Precondition.checkFileExists(new File(previewImageDir, nsFileName), "newsstandCoverFile");

		if(!previewImageDir.isDirectory()) {
			throw new IllegalArgumentException("Excepting an image directory: " + previewImageDir);
		}
		
		this.nsFileName = nsFileName;
		this.previewImageDir = previewImageDir;
	}

	/**
	 * Gets the newsstand feed cover image file name.
	 *
	 * @return 
	 */
	public String getNsFileName() {
		return nsFileName;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.packager.PackageInfo#getSize()
	 */
	@Override
	public long getSize() {
		return FileUtils.sizeOfDirectory(previewImageDir);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.packager.PackageInfo#getPrettyName()
	 */
	@Override
	public String getPrettyName() {
		return "N/A";
	}
	
	/**
	 * Preview image dir.
	 *
	 * @return the file
	 */
	public File previewImageDir() {
		return previewImageDir;
	}
}
