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

/**
 * Upload a preview package for an IssueMeta
 *
 * @param <V> the package type
 */
public interface PreviewUploader<V> {
	
	/**
	 * Upload a preview package
	 * @param packageFile the package file
	 * @param meta 
	 */
	void uploadPreview(V packageFile, IssueMeta meta);
}