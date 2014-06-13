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

/**
 * A SingleContent represents a parsed file / directory that is ready to be packaged by a
 * {@link com.timeInc.ark.packager.Packager} so that it can be uploaded to the destination.
 */
public class SingleContent {
	private final File rootContent;

	/**
	 * Instantiates a new single content.
	 *
	 * @param rootContent the root content
	 */
	public SingleContent(File rootContent) {
		this.rootContent = rootContent;
	}

	/**
	 * Gets the root content of the parsed data.
	 *
	 * @return the root content
	 */
	public File getRootContent() {
		return rootContent;
	}
}
