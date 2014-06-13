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
package com.timeInc.ark.backup;

import java.io.File;

import com.timeInc.ark.issue.IssueMeta;

/**
 * Represents a place where files can be stored
 */
public interface Storage {
	/**
	 * Save the file to the specified destination
	 * @param file the file to store
	 * @param destination the destination
	 */
	void save(File file, File destination);

	
	/**
	 *  Gets the location of where a file to be saved
	 *  should go. 
	 *
	 */
	interface Location {
		File getLocation(IssueMeta meta, File file);
	}
}
