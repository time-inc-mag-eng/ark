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
package com.timeInc.ark.parser.preview;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.parser.AbstractSanitizer;
import com.timeInc.ark.parser.Parser.PreviewParser;
import com.timeInc.ark.parser.content.ContentException;
import com.timeInc.ark.parser.preview.validator.FilePreviewValidator;
import com.timeInc.ark.parser.preview.validator.PreviewValidator;
import com.timeInc.mageng.util.exceptions.PrettyException;
import com.timeInc.mageng.util.misc.Status;

/**
 * Parses a directory or image file by trying to find an image that matches a regex in Map<String, Val> 
 * The regex must match only one image in the directory otherwise, an error will be thrown.
 */
public class RegexPreviewParser extends AbstractSanitizer<PreviewParsedData> implements PreviewParser<PreviewParsedData> {
	private Integer id;
	private Map<String, Val> expectedImages;
	
	private PreviewValidator validator;
	
	/**
	 * Instantiates a new regex preview parser.
	 */
	public RegexPreviewParser() {
		this(new FilePreviewValidator());
	}
	
	protected RegexPreviewParser(PreviewValidator validator) {
		this.validator = validator;
	}

	/**
	 * Instantiates a new regex preview parser.
	 *
	 * @param expectedImages the expected images
	 * @param validator the validator
	 */
	public RegexPreviewParser(Map<String, Val> expectedImages, PreviewValidator validator) {
		this.expectedImages = expectedImages;
		this.validator = validator;
	}

	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.AbstractSanitizer#requiresUnpacked()
	 */
	@Override
	public boolean requiresUnpacked() {
		return expectedImages.size() > 1 ? true : false;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.Parser.PreviewParser#validate(com.timeInc.ark.parser.preview.PreviewParsedData, com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public Status validate(PreviewParsedData parsedData, IssueMeta meta) {
		for(Map.Entry<String,File> pair : parsedData.getImageMapping().entrySet()) {
			Status status = validator.validate(ImageKey.getImageKey(pair.getKey()), pair.getValue(), meta);
			
			if(status.isError()) {
				return status;
			}
		}
		
		return Status.getSuccess();
	}

	@Override
	protected PreviewParsedData parseContentAfterSanitized(File content, IssueMeta meta) throws ContentException {
		Map<String, File> mapPair = new HashMap<String, File>();
		
		for (final Map.Entry<String, Val> entry : expectedImages.entrySet()) {
			if(content.isFile()) { 
				if(content.getName().matches(entry.getValue().regex))
						mapPair.put(entry.getKey(), content);
				else
					throw new PrettyException(entry.getValue().errorMsg, new IllegalStateException("Unable to match regex:" + entry.getValue().regex + " with file" + content));
			} else {
				File files[] = content.listFiles(new FilenameFilter() {
				    @Override
				    public boolean accept(File dir, String name) {
				        return name.matches(entry.getValue().regex);
				    }
				});
				
				if(files.length != 1)
					throw new PrettyException(entry.getValue().errorMsg, new IllegalStateException("Unable to match regex:" + entry.getValue() + " @ base directory" + content));
				
				mapPair.put(entry.getKey(), files[0]);
			}
		}
		
		return new PreviewParsedData(mapPair, content);
	}
	
	/**
	 * Sets the expected images.
	 *
	 * @param expectedImages the expected images
	 */
	public void setExpectedImages(Map<String, Val> expectedImages) {
		this.expectedImages = expectedImages;
	}

	/**
	 * Gets the expected images.
	 *
	 * @return the expected images
	 */
	public Map<String, Val> getExpectedImages() {
		return expectedImages;
	}

	/**
	 * Removes the from expected images.
	 *
	 * @param key the key
	 */
	public void removeFromExpectedImages(String key){
		expectedImages.remove(key);
	}
	
	/**
	 * The Class Val.
	 */
	public static class Val {
		String regex;
		String errorMsg;

		/**
		 * Gets the error msg.
		 *
		 * @return the error msg
		 */
		public String getErrorMsg() {
			return errorMsg;
		}
		
		/**
		 * Sets the error msg.
		 *
		 * @param errorMsg the new error msg
		 */
		public void setErrorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
		}
		
		/**
		 * Gets the regex.
		 *
		 * @return the regex
		 */
		public String getRegex() {
			return regex;
		}
		
		/**
		 * Sets the regex.
		 *
		 * @param regex the new regex
		 */
		public void setRegex(String regex) {
			this.regex = regex;
		}
	}
}
