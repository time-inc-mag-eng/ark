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
 * A parser that expects a file as its input. It does no conversion and simply validates that the
 * input exists.
 */
public class Any extends IdentifyingContent<SingleContent> {
	private final FileValidator validator = new FileExistValidator();
	
	/**
	 * Instantiates a new any.
	 */
	public Any () {}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.Parser.ContentParser#validate(java.io.File)
	 */
	@Override
	public Status validate(File singleContentFile) {
		return validator.validate(singleContentFile);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.AbstractSanitizer#requiresUnpacked()
	 */
	@Override
	public boolean requiresUnpacked() {
		return false;
	}

	@Override
	protected SingleContent parseContentAfterSanitized(File aFile,
			IssueMeta meta) {
		return new SingleContent(aFile);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.Parser.ContentParser#getType()
	 */
	@Override
	public String getType() {
		return "any";
	}
}
