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
package com.timeInc.ark.upload.content;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.upload.ConcurrentUploader.CachedConcurrentUploader;
import com.timeInc.ark.upload.IdToCacheMapping;
import com.timeInc.ark.upload.PostUploadListener;
import com.timeInc.ark.upload.UploadCommandFactory;
import com.timeInc.mageng.util.progress.concurrent.cache.CacheExecutor;

/**
 * Concurrently executes the upload process for IssueMeta using a ProgressExecutor that uses the mapping
 * IdToCacheMapping
 *
 */
public class ConcurrentContentUploader extends CachedConcurrentUploader<IssueMeta> {

	/**
	 * Instantiates a new concurrent content uploader.
	 *
	 * @param executor the executor
	 * @param listener the listener
	 * @param factory the factory
	 */
	public ConcurrentContentUploader(CacheExecutor executor, PostUploadListener listener, UploadCommandFactory<IssueMeta> factory) {
		super(executor, listener, factory);
	}

	@Override
	protected IdToCacheMapping getMapping(String cacheId, IssueMeta meta) {
		return new IdToCacheMapping(meta.getId(), cacheId);
	}
}
