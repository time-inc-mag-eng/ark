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
package com.timeInc.ark.packager;
import java.io.File;
import java.io.IOException;

import com.timeInc.ark.issue.IssueMeta;

/**
 * Packages parsed data so that it can be uploaded to a destination.
 *
 * @param <T> the package
 * @param <V> the parsed data
 */
public interface Packager<T extends PackageInfo, V> {
	
	/**
	 * Packages a parsed data for an IssueMeta so that it can
	 * be uploaded to a destination.
	 * @param parsedData
	 * @param outputDirectory
	 * @param meta
	 * @return
	 * @throws IOException
	 */
	T pack(V parsedData, File outputDirectory, IssueMeta meta) throws IOException;
}
