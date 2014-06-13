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
package com.timeInc.ark.parser.preview.validator;

import java.io.File;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.parser.Parser.PreviewParser.ImageKey;
import com.timeInc.mageng.util.misc.Status;

/**
 * Decorator pattern so multiple {@link PreviewValidator} can be nested
 */
public abstract class AbstractPreviewValidator implements PreviewValidator {
	private final PreviewValidator validator;
	
	AbstractPreviewValidator(PreviewValidator validator) {
		this.validator = validator;
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.preview.validator.PreviewValidator#validate(com.timeInc.ark.parser.Parser.PreviewParser.ImageKey, java.io.File, com.timeInc.ark.issue.IssueMeta)
	 */
	public Status validate(ImageKey imageKey, File file, IssueMeta meta) {
		return validator.validate(imageKey, file, meta);
	}
	
}
