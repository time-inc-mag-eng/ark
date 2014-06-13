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
package com.timeInc.ark.parser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.parser.preview.PreviewParsedData;
import com.timeInc.mageng.util.misc.Status;

/**
 * A parser that parses an input file or directory for an IssueMeta so that it can be 
 * prepared for packaging. 
 * @see com.timeInc.ark.packager.Packager
 *
 * @param <T> the object that represents the parsed file
 */
public abstract interface Parser<T> {
	
	/**
	 * Parse the file / directory that is located at toParse.
	 * @param toParse
	 * @param meta
	 * @return the parsed information
	 */
	T parse(File toParse, IssueMeta meta);
	
	/**
	 * Determines whether or not this parser
	 * requires the toParse to be a directory or not
	 * @return true if unpacking is required so that toParse is a directory
	 * otherwise false
	 */
	boolean requiresUnpacked();
	
	/**
	 * A content parser.
	 *
	 * @param <T> the generic type
	 */
	public interface ContentParser<T> extends Parser<T> {
		/**
		 * Validates the file / directory to be parsed
		 * @param toParse
		 * @return a Status of success if validation was successful; otherwise a failed Status
		 */
		Status validate(File toParse);
		
		/**
		 * Gets a string representation of the parse type.
		 * @return
		 */
		String getType();
	}
	
	/**
	 * A preview parser
	 *
	 * @param <T> the generic type
	 */
	public interface PreviewParser<T> extends Parser<T> {
		static final String COVER_SMALL = "COVER_SMALL";
		static final String COVER_LARGE = "COVER_LARGE";
		static final String COVER_LARGE_LANDSCAPE = "COVER_LARGE_LANDSCAPE";
		static final String VERTICAL_IMAGE = "IMAGE_VERTICAL";
		static final String HORIZONTAL_IMAGE = "IMAGE_HORIZONTAL";
		static final String NEWSSTAND_COVER = "NEWSSTAND_COVER";
		
		enum ImageKey {
			COVERSMALL(COVER_SMALL), COVERLARGE_PORTRAIT(COVER_LARGE),
			COVERLARGE_LANDSCAPE(COVER_LARGE_LANDSCAPE), VERTICAL(VERTICAL_IMAGE), 
			HORIZONTAL(HORIZONTAL_IMAGE), NEWSSTAND(NEWSSTAND_COVER),
			OTHER(null);
			
			private static Map<String,ImageKey> strToEnumMap = new HashMap<String,ImageKey>();
			
			static {
				for(ImageKey key : values()) {
					strToEnumMap.put(key.key, key);
				}
			}
			
			private final String key;
			
			private ImageKey(String key) {
				this.key = key;
			}
			
			public String getStrRepr() { return key; }
			
			/**
			 * Gets an ImageKey if it maps to a string 
			 * representation otherwise it returns OTHER
			 * @param keyName
			 * @return
			 */
			public static ImageKey getImageKey(String keyName) {
				ImageKey key = strToEnumMap.get(keyName);
				return key != null ? key : OTHER;
			}
		}
		
		/**
		 * Validates the parsed preview data ensuring all
		 * the required values are available for this IssueMeta
		 * @param data the preview parsed data
		 * @param meta
		 * @return a Status of success if validation was successful; otherwise a failed Status
		 */
		Status validate(PreviewParsedData data, IssueMeta meta);
	}
}
