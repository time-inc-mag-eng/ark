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

import com.timeInc.ark.issue.Folio;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.parser.Parser.PreviewParser.ImageKey;
import com.timeInc.mageng.util.misc.Status;

/**
 * Validator to ensure that the following imageKey meet the following requirements: 
 * COVERLARGE_PORTRAIT - width must be the smaller one of the folio's dimension and height is the larger
 * COVERLARGE_LANDSCAPE - height must be the smaller one of the folio's dimension and width is the larger
 */
public class FolioPreviewValidator extends AbstractPreviewValidator {
	private final MinMaxResolution resValidator;
	
	/**
	 * Instantiates a new folio preview validator.
	 *
	 * @param validator the validator
	 */
	public FolioPreviewValidator(PreviewValidator validator) {
		super(validator);
		this.resValidator = new MinMaxResolution();
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.preview.validator.AbstractPreviewValidator#validate(com.timeInc.ark.parser.Parser.PreviewParser.ImageKey, java.io.File, com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public Status validate(ImageKey imageKey, File file, IssueMeta meta) {
		Status pV = super.validate(imageKey, file, meta);
		
		if(!pV.isError()) {
			Folio folio = meta.getFolio();
			
			int max = Math.max(folio.getResolution().getHeight(), folio.getResolution().getWidth());
			int min = Math.min(folio.getResolution().getHeight(), folio.getResolution().getWidth());
			
			switch(imageKey) {
				case COVERLARGE_PORTRAIT:
						resValidator.setHeight(0, max);
						resValidator.setWidth(0, min);
						return resValidator.validate(file);
						
				case COVERLARGE_LANDSCAPE:
						resValidator.setHeight(0, min);
						resValidator.setWidth(0, max);
						return resValidator.validate(file);
						
				default:
						return Status.getSuccess();
			}
		} else
			return pV;
	}
}



