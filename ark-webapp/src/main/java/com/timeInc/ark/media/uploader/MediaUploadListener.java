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
package com.timeInc.ark.media.uploader;

import java.io.File;

import com.timeInc.ark.issue.Issue;

/**
 * Listener for when a media file has successfully been uploaded.
 * @see OriginMediaUploader
 */
public interface MediaUploadListener {
	/**
	 * A successful media upload
	 * @param issue
	 * @param cdnUrl
	 * @param previewUrl
	 * @param relativePath
	 * @param file
	 */
	void uploadComplete(Issue issue, String cdnUrl, String previewUrl, String relativePath, File file);
}
