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
package com.timeInc.ark.media;

import java.io.File;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;

import com.timeInc.mageng.util.file.FileValidator;
import com.timeInc.mageng.util.misc.Status;

/**
 * Validates that the file extension matches one of the MediaType set
 */
public class CaseInsensitiveExtensionValidator implements FileValidator {
	private final Set<MediaType> mediaType;

	/**
	 * Instantiates a new case insensitive extension validator.
	 *
	 * @param mediaType the media type
	 */
	public CaseInsensitiveExtensionValidator(Set<MediaType> mediaType) {
		this.mediaType = mediaType;
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.util.file.FileValidator#validate(java.io.File)
	 */
	@Override
	public Status validate(File file) {
		if(!mediaType.contains(getMediaTypeFor(file))) {
			return Status.getFailure("Invalid file type for: " + file.getName() + " Types accepted: " + mediaType);
		} else
			return Status.getSuccess();
	}
	
	private static MediaType getMediaTypeFor(File file) {
		String extension = FilenameUtils.getExtension(file.getAbsolutePath());
		return new MediaType(extension.toLowerCase());	
	}
}
