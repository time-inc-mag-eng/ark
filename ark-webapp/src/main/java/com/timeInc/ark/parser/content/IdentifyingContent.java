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

import com.timeInc.ark.parser.AbstractSanitizer;
import com.timeInc.ark.parser.Parser.ContentParser;


/**
 * Parent class for all hibernate ContentParser
 *
 * @param <T> the generic type
 */
public abstract class IdentifyingContent<T> extends AbstractSanitizer<T> implements ContentParser<T> {
	// todo not all contentparser need abstractsanitizer
	
	private Integer id;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
}
