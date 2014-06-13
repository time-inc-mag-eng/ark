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
import java.io.IOException;

import org.apache.log4j.Logger;

import com.timeInc.ark.issue.Resolution;
import com.timeInc.mageng.util.file.FileValidator;
import com.timeInc.mageng.util.misc.Status;

/**
 * Validator that verifies an image's resolution is within bounds of the height and width range.
 */
public class MinMaxResolution implements FileValidator {
	private static final Logger log = Logger.getLogger(MinMaxResolution.class);
	
	private int minWidth = 0; 
	private int maxWidth = 0;

	private int minHeight = 0;
	private int maxHeight = 0;
	
	/* (non-Javadoc)
	 * @see com.timeInc.util.file.FileValidator#validate(java.io.File)
	 */
	@Override
	public Status validate(File file) {
		try {
			Resolution resolution = new Resolution(file);
			
			int height = resolution.getHeight();
			int width = resolution.getWidth();
			
			if(width < minWidth || width > maxWidth || height < minHeight || height > maxHeight)
				return Status.getFailure(file.getName() + " height needs to be between:" + minHeight + " to " + maxHeight + "  and width between:" + minWidth + " to " + maxWidth);
			else
				return Status.getSuccess();
			
		} catch (IOException e) {
			log.error("IOException", e);
			return Status.getFailure("Unable to fetch resolution info");
		}		
	}
	
	
	/**
	 * Sets the height.
	 *
	 * @param minHeight the min height
	 * @param maxHeight the max height
	 */
	public void setHeight(int minHeight, int maxHeight) {
		gteZero(minHeight);
		gteZero(maxHeight);
		
		this.maxHeight = maxHeight;
		this.minHeight = minHeight;
	}
	
	/**
	 * Sets the width.
	 *
	 * @param minWidth the min width
	 * @param maxWidth the max width
	 */
	public void setWidth(int minWidth, int maxWidth) {
		gteZero(minWidth);
		gteZero(maxWidth);
		
		this.minWidth = minWidth;
		this.maxWidth = maxWidth;
	}
	
	
	private static void gteZero(int val) {
		if(val < 0)
			throw new IllegalArgumentException("Dimension must be greater than zero");
	}

}
