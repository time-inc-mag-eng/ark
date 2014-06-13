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
import java.util.Collections;
import java.util.Map;

import com.timeInc.ark.parser.Parser.PreviewParser.ImageKey;
import com.timeInc.ark.parser.SingleContent;

/**
 * Value class that represents the parsed information for Preview.
 */
public class PreviewParsedData extends SingleContent {
	final Map<String, File> imageMapping;
	
	/**
	 * Instantiates a new preview parsed data.
	 *
	 * @param imageMapping the image mapping
	 * @param rootContent the root content
	 */
	public PreviewParsedData(Map<String, File> imageMapping, File rootContent) {
		super(rootContent);
		this.imageMapping = imageMapping;
	}
	
	/**
	 * Checks whether there exists an ImageKey
	 *
	 * @param key the key
	 * @return true, if exists, false otherwise
	 */
	public boolean hasImage(ImageKey key) {
		return imageMapping.containsKey(key.getStrRepr());
	}
	
	/**
	 * Gets the image file name based on the key
	 *
	 * @param key the key
	 * @return the image file name
	 */
	public String getImageName(ImageKey key) {
		if(hasImage(key)) {
			return imageMapping.get(key.getStrRepr()).getName();
		} else 
			return null;
	}
	
	Map<String, File> getImageMapping() {
		return Collections.unmodifiableMap(imageMapping);
	}
}
