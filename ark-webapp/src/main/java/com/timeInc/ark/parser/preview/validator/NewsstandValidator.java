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
 * Newsstand cover image validator to ensure that the image meets the required 
 * resolution.
 */
public class NewsstandValidator extends AbstractPreviewValidator {
	private final MinMaxResolution resValidator;
	private final ImageTypeValidator imageTypeValidator;

	private static final int COVER_LONG_SIDE_PX = 1024;

	/**
	 * Instantiates a new newsstand validator.
	 *
	 * @param prevValidator the prev validator
	 */
	public NewsstandValidator(PreviewValidator prevValidator) {
		super(prevValidator);
		this.resValidator = new MinMaxResolution();
		this.imageTypeValidator = new ImageTypeValidator("png");
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.preview.validator.AbstractPreviewValidator#validate(com.timeInc.ark.parser.Parser.PreviewParser.ImageKey, java.io.File, com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public Status validate(ImageKey imageKey, File file, IssueMeta meta) {
		Status parentV = super.validate(imageKey, file, meta);
		
		if(!parentV.isError()) {
			switch(imageKey) {
				case NEWSSTAND:
					Status iV = imageTypeValidator.validate(file);
	
					if(!iV.isError()) {
						resValidator.setHeight(COVER_LONG_SIDE_PX, COVER_LONG_SIDE_PX);
						resValidator.setWidth(0, COVER_LONG_SIDE_PX);
						
						Status lSide = resValidator.validate(file);
						
						if(lSide.isError()) {
							resValidator.setWidth(COVER_LONG_SIDE_PX, COVER_LONG_SIDE_PX);
							resValidator.setHeight(0, COVER_LONG_SIDE_PX);
							Status rSide =  resValidator.validate(file);
							
							if(rSide.isError()) {
								return Status.getFailure(imageKey + " Max resolution is " + COVER_LONG_SIDE_PX + "x" + COVER_LONG_SIDE_PX + " and atleast one side needs to be " + COVER_LONG_SIDE_PX);
							} else 
								return Status.getSuccess();
							
						} else
							return Status.getSuccess();
					} else
						return iV;
	
				default:
					return Status.getSuccess();
			}
		} else 
			return parentV;
	}
}
