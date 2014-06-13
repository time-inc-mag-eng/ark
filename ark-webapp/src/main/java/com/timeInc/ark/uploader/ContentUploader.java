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
package com.timeInc.ark.uploader;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.mageng.util.progress.ProgressListener;

/**
 * Upload a content package for an IssueMeta
 *
 * @param <T> the package type
 */
public interface ContentUploader<T> {
	
	/**
	 * Upload a content package, providing progress updates.
	 * @param packageFile the package file
	 * @param meta 
	 * @param listener
	 */
	void uploadContent(T packageFile, IssueMeta meta, ProgressListener listener);
	
	/**
	 * Upload a content package
	 * @param packageFile the package file
	 * @param meta 
	 */
	void uploadContent(T packageFile, IssueMeta meta);
}
