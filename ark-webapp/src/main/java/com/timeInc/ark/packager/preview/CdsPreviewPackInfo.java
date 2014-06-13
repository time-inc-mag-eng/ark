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
 * Cds preview package information for {@link com.timeInc.ark.application.CdsApplication}
 */
public class CdsPreviewPackInfo extends ManagedPackage {
	private final CdsPreviewName requiredNames;
	
	/**
	 * Instantiates a new cds preview pack info.
	 *
	 * @param reqPreviewName the required cds preview filenames
	 * @param previewDir the preview dir
	 * @param nsFileName the newsstand cover image filename
	 */
	public CdsPreviewPackInfo(CdsPreviewName reqPreviewName, File previewDir, String nsFileName) {
		super(previewDir, nsFileName);
		this.requiredNames = reqPreviewName;
	}
	
	/**
	 * Gets the required preview image filenames.
	 *
	 * @return the preview name
	 */
	public CdsPreviewName getPreviewName() {
		return requiredNames;
	}

	/**
	 * Required Cds preview image filenames
	 */
	public static class CdsPreviewName {
		
		/** The vertical cover. */
		public final String verticalCover;
		
		/** The horizontal cover. */
		public final String horizontalCover;
		
		/** The small cover. */
		public final String smallCover;
		
		/** The large cover. */
		public final String largeCover;
		
		
		/**
		 * Instantiates a new cds preview name.
		 *
		 * @param verticalCover the vertical cover
		 * @param horizontalCover the horizontal cover
		 * @param smallCover the small cover
		 * @param largeCover the large cover
		 */
		public CdsPreviewName(String verticalCover, String horizontalCover,
				String smallCover, String largeCover) {
			this.verticalCover = verticalCover;
			this.horizontalCover = horizontalCover;
			this.smallCover = smallCover;
			this.largeCover = largeCover;
		}
	}
}
