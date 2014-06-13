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
package com.timeInc.ark.upload;

import java.util.ArrayList;
import java.util.List;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.upload.preview.IssueMetaStatusMapping;
import com.timeInc.mageng.util.misc.Status;
import com.timeInc.mageng.util.progress.ProgressStatus;
import com.timeInc.mageng.util.progress.ProgressableCommand;
import com.timeInc.mageng.util.progress.concurrent.ProgressExecutor;
import com.timeInc.mageng.util.progress.concurrent.cache.CacheExecutor;

/**
 * Concurrently executes the upload process for a list of domain objects using the same
 * {@link com.timeInc.ark.upload.UserUploadConfig} while providing a way to track the progress of each by way of a
 * mapping.
 * 
 *  
 * @param <T> - custom metadata returned upon submitting the task to the ProgressExecutor
 * @param <V> the mapping between between type T and X
 * @param <X> the type of domain object that the upload is for
 */
public abstract class ConcurrentUploader<T, V, X> {
	private final PostUploadListener listener;
	private final UploadCommandFactory<X> factory;
	protected final ProgressExecutor<T> executor;
	
	/**
	 * Instantiates a new concurrent uploader.
	 *
	 * @param listener the listener that gets called after all submitted upload tasks are complete
	 * @param factory the factory
	 * @param executor the executor
	 */
	public ConcurrentUploader(PostUploadListener listener, UploadCommandFactory<X> factory, ProgressExecutor<T> executor) {
		this.listener = listener;
		this.factory = factory;
		this.executor = executor;
	}

	/**
	 * Concurrently executes the upload process for a list of domain objects using the same
	 * config by submitting to the ProgressExecutor the Progressable commands returned by invoking
	 * {@link UploadCommandFactory#getUploadCommandFor(List, UserUploadConfig)} 
	 *
	 * @param domainObjs a list of domain objects that the upload is for
	 * @param config the upload configuration
	 * @return a mapping between the metadata the ProgressExecutor returns and the domainObjs in order
	 */
	public List<V> execute(final List<X> domainObjs, final UserUploadConfig config) {
		List<ProgressableCommand> commands = factory.getUploadCommandFor(domainObjs, config);
		
		List<T> result = executor.startCommand(commands, new Runnable() {
			@Override
			public void run() {
				listener.afterUpload(config);
			}
		});
		
		return resultSetMapping(result, domainObjs);
	}
	
	private List<V> resultSetMapping(List<T> executorResult, List<X> domainObj) {
		List<V> mapping = new ArrayList<V>();
		
		int metaIndex = 0;
		
		for(T result : executorResult) {
			mapping.add(getMapping(result, domainObj.get(metaIndex)));
			metaIndex++;
		}
		
		return mapping;
	}
	
	protected abstract V getMapping(T executorResult, X domainObj);
	
	/**
	 * Concurrently executes the upload process for domain object X using a CacheExecutor that uses the mapping
	 * IdToCacheMapping
	 *
	 * @param <X> the type of domain object that the upload is for
	 */
	public static abstract class CachedConcurrentUploader<X> extends ConcurrentUploader<String, IdToCacheMapping, X> {
		private final CacheExecutor executor; 
		
		/**
		 * Instantiates a new cached concurrent uploader.
		 *
		 * @param executor the executor
		 * @param listener the listener
		 * @param factory the factory
		 */
		public CachedConcurrentUploader(CacheExecutor executor, PostUploadListener listener, UploadCommandFactory<X> factory) {
			super(listener, factory, executor);
			this.executor = executor;
		}
		
		/**
		 * Gets the progress status.
		 *
		 * @param id the id
		 * @return the progress status
		 */
		public ProgressStatus getProgressStatus(String id) {
			return executor.getStatus(id);
		}

		abstract protected IdToCacheMapping getMapping(String cacheId, X domainObj);
	}
	
	/**
	 * Concurrently executes the upload process for IssueMeta using a ProgressExecutor that uses the mapping
	 * IssueMetaStatusMapping
	 *
	 */
	public static class ConcurrentStatusUploader extends ConcurrentUploader<Status, IssueMetaStatusMapping, IssueMeta> {
		
		/**
		 * Instantiates a new concurrent status uploader.
		 *
		 * @param executor the executor
		 * @param listener the listener
		 * @param factory the factory
		 */
		public ConcurrentStatusUploader(ProgressExecutor<Status> executor, PostUploadListener listener, UploadCommandFactory<IssueMeta> factory) {
			super(listener, factory, executor);
		}

		@Override
		protected IssueMetaStatusMapping getMapping(Status status, IssueMeta meta) {
			return new IssueMetaStatusMapping(meta, status);
		}
	}
}
