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

/**
 * Dps preview package information for a {@link com.timeInc.ark.application.DpsApplication}.
 */
public class DpsPreviewPackInfo extends ManagedPackage {
	private final String landFileName;
	private final String portFileName;
	
	/**
	 * Instantiates a new dps preview pack info.
	 *
	 * @param previewDir the preview dir
	 * @param nsFileName the newsstand cover image file name
	 * @param landFileName the landscape file name
	 * @param portFileName the portrait file name
	 */
	public DpsPreviewPackInfo(File previewDir, String nsFileName, String landFileName, String portFileName) {
		super(previewDir, nsFileName);
		
		this.landFileName = landFileName;
		this.portFileName = portFileName;
	}

	/**
	 * Gets the landscape file name.
	 *
	 * @return the land file name
	 */
	public String getLandFileName() {
		return landFileName;
	}

	/**
	 * Gets the portrait file name.
	 *
	 * @return the port file name
	 */
	public String getPortFileName() {
		return portFileName;
	}
}
