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

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.packager.PackageInfo;
import com.timeInc.mageng.util.file.FileUtil;
import com.timeInc.mageng.util.misc.Status;
import com.timeInc.mageng.util.progress.DetailedProgressListener;

/**
 * Abstract implementation for IssueMeta UploadFacade which invokes lifecycle events for {@link com.timeInc.ark.upload.UploadListener}
 *
 * @param <T> configuration
 */
public abstract class MetaUploadFacade<T extends UserUploadConfig> extends UploadFacade<T> {
	private static final Logger log = Logger.getLogger(MetaUploadFacade.class);

	protected final UploadListener listener;
	protected final IssueMeta meta;

	/**
	 * Instantiates a new meta upload facade.
	 *
	 * @param config the config
	 * @param meta the meta
	 * @param listener the listener
	 */
	public MetaUploadFacade(T config, IssueMeta meta, UploadListener listener) {
		super(config);

		this.meta = meta;
		this.listener = listener;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.upload.UploadFacade#delegateUpload(com.timeInc.mageng.util.progress.DetailedProgressListener, java.io.File, java.io.File)
	 */
	@Override
	public Status delegateUpload(final DetailedProgressListener progress, File content, File outputDirectory) throws IOException {
		log.debug("Making a copy of " + content);
		File copy = FileUtil.copyToTempDir(content, config.getWorkingDirectory());
		PackageInfo data = handleUploadSpecific(progress, copy, outputDirectory);
		listener.success(meta, data);
		
		return Status.getSuccess();
	}

	@Override
	protected void handleUncaughtFailure(String reason) {
		listener.fail(meta, reason);
	}

	protected abstract PackageInfo handleUploadSpecific(final DetailedProgressListener progress, File contentToParse, File outputDirectory);
}



