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
import java.io.IOException;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.packager.Packager;
import com.timeInc.ark.parser.Parser.PreviewParser.ImageKey;
import com.timeInc.ark.parser.preview.PreviewParsedData;


/**
 * Preview packager for {@link com.timeInc.ark.application.DpsApplication}
 */
public class DpsPreviewPackager implements Packager<DpsPreviewPackInfo, PreviewParsedData> {
	
	/**
	 * Instantiates a new dps preview packager.
	 */
	public DpsPreviewPackager() {}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.packager.Packager#pack(java.lang.Object, java.io.File, com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public DpsPreviewPackInfo pack(PreviewParsedData parsedData, File outputDirectory, IssueMeta meta) throws IOException {
		String portraitImage = parsedData.getImageName(ImageKey.COVERLARGE_PORTRAIT);
		String landscapeImage = parsedData.getImageName(ImageKey.COVERLARGE_LANDSCAPE);
		String newsstand = parsedData.getImageName(ImageKey.NEWSSTAND);;
		return new DpsPreviewPackInfo(parsedData.getRootContent(), newsstand, landscapeImage, portraitImage);
	}
}
