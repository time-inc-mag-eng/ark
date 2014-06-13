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
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;

import com.timeInc.mageng.util.file.FileValidator;
import com.timeInc.mageng.util.misc.Status;

/**
 * Validates whether the file is of image type.
 * @see ImageIO#getImageReaders(Object)
 * 
 */
public class ImageTypeValidator implements FileValidator {
	private static final Logger log = Logger.getLogger(ImageTypeValidator.class);
	
	private String imageType;

	/**
	 * Instantiates a new image type validator.
	 *
	 * @param imageType the image type
	 */
	public ImageTypeValidator(String imageType) {
		this.imageType = imageType;
	}
	
	/**
	 * Sets the image type.
	 *
	 * @param imageType the new image type
	 */
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.util.file.FileValidator#validate(java.io.File)
	 */
	@Override
	public Status validate(File imageFile) {
		ImageInputStream ios = null;

		try {
			ios = ImageIO.createImageInputStream(imageFile);

			Iterator<ImageReader> iter = ImageIO.getImageReaders(ios);

			if (!iter.hasNext()) 
				return Status.getFailure(imageFile.getName() + " is not an image");

			ImageReader reader = iter.next();
			reader.setInput(ios);

			if(!reader.getFormatName().toLowerCase().equals(imageType))
					return Status.getFailure(imageFile.getName() + " - Expected image type " + imageType + " - actual " + reader.getFormatName());

			return Status.getSuccess();
		} catch(Exception ex) {
			log.error("Error validating image", ex);
			return Status.getFailure("Error validating image type");
		} finally {
			try {
				if(ios != null) ios.close();
			} catch (IOException e) {
				log.fatal(e);
			}
		}
	}
}
