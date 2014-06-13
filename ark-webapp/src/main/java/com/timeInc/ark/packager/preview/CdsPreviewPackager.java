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
import com.timeInc.ark.packager.preview.CdsPreviewPackInfo.CdsPreviewName;
import com.timeInc.ark.parser.Parser.PreviewParser.ImageKey;
import com.timeInc.ark.parser.preview.PreviewParsedData;

/**
 * Packager for {@link com.timeInc.ark.application.CdsApplication}
 */
public class CdsPreviewPackager implements Packager<CdsPreviewPackInfo, PreviewParsedData> {
	
	/**
	 * Instantiates a new cds preview packager.
	 */
	public CdsPreviewPackager() {}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.packager.Packager#pack(java.lang.Object, java.io.File, com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public CdsPreviewPackInfo pack(PreviewParsedData parsedData, File outputDirectory, IssueMeta meta) throws IOException {
		String smallCover = parsedData.getImageName(ImageKey.COVERSMALL);
		String largeCover = parsedData.getImageName(ImageKey.COVERLARGE_PORTRAIT);
		
		String horizontal = parsedData.getImageName(ImageKey.HORIZONTAL);
		String vertical = parsedData.getImageName(ImageKey.VERTICAL);
		String newsstand =  parsedData.getImageName(ImageKey.NEWSSTAND);
		
		return new CdsPreviewPackInfo(new CdsPreviewName(vertical, horizontal, smallCover, largeCover), parsedData.getRootContent(), newsstand);
	}
}
