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
package com.timeInc.ark.parser.content;

import java.io.File;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.parser.SingleContent;
import com.timeInc.mageng.util.file.FileExistValidator;
import com.timeInc.mageng.util.file.FileValidator;
import com.timeInc.mageng.util.misc.Status;

/**
 * Content parser for Woodwing Ofip Html5. The input must be a directory. It does no conversion, but simply provides a validator
 * if the input is indeed an Ofip Html5 content.
 */
public class OfipHtml5 extends IdentifyingContent<SingleContent> {
	private static final String HTML_FILE = "index.html";
	
	
	private final FileValidator validator = new FileExistValidator();
	

	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.Parser.ContentParser#validate(java.io.File)
	 */
	@Override
	public Status validate(File contentDirectory) {
		File indexHtml = new File(contentDirectory,HTML_FILE);
		return validator.validate(indexHtml);
	}

	@Override
	protected SingleContent parseContentAfterSanitized(File rootContentFolderPath, IssueMeta meta) {
		return new SingleContent(rootContentFolderPath);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.Parser.ContentParser#getType()
	 */
	@Override
	public String getType() {
		return "ofip_html5";
	}
}
