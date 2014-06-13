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
package com.timeInc.ark.media;

import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.upload.ConcurrentUploader.CachedConcurrentUploader;
import com.timeInc.ark.upload.IdToCacheMapping;
import com.timeInc.ark.upload.PostUploadListener;
import com.timeInc.ark.upload.UploadCommandFactory;
import com.timeInc.mageng.util.progress.concurrent.cache.CacheExecutor;

/**
 * Concurrently executes a command for an Issue. 
 * In this case it is used for concurrently uploading media files to multiple
 * {@link Issue}
 * 
 */
public class ConcurrentMediaUploader extends CachedConcurrentUploader<Issue> {

	/**
	 * Instantiates a new concurrent media uploader.
	 *
	 * @param executor the executor
	 * @param listener the listener
	 * @param factory the factory
	 */
	public ConcurrentMediaUploader(CacheExecutor executor, PostUploadListener listener, UploadCommandFactory<Issue> factory) {
		super(executor, listener, factory);
	}

	@Override
	protected IdToCacheMapping getMapping(String cacheId, Issue issue) {
		return new IdToCacheMapping(issue.getId(), cacheId);
	}
}
