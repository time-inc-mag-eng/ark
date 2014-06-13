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

import org.apache.log4j.Logger;

import com.timeInc.mageng.util.compression.Unpacker;
import com.timeInc.mageng.util.exceptions.PrettyException;
import com.timeInc.mageng.util.file.FileUtil;
import com.timeInc.mageng.util.misc.Status;
import com.timeInc.mageng.util.progress.DetailedProgressListener;

/**
 * 
 * Template method in which the handling of the uploaded file is 
 * unpackaged if necessary and provided to subclasses in the 
 * {@link #delegateUpload(DetailedProgressListener, File, File)} method. 
 * 
 * 
 *
 * @param <T> the generic type
 */
public abstract class UploadFacade<T extends UserUploadConfig> {
	private static final Logger log = Logger.getLogger(UploadFacade.class);

	protected final T config;

	/**
	 * Instantiates a new upload facade.
	 *
	 * @param config the config
	 */
	public UploadFacade(T config) {
		this.config = config;
	}

	/**
	 * 
	 * Unpackages a file if necessary and delegates it to 
	 * {@link #delegateUpload(DetailedProgressListener, File, File)} method.
	 * The method's return Status is used to inform the DetailedProgressListener.
	 * If an uncaught exception is thrown and it is of type PrettyException,
	 * then the friendly message is used as the progress message, otherwise, an 
	 * default Internal Error message is used.
	 *
	 * @param progress the progress
	 */
	public final void handleUpload(final DetailedProgressListener progress) {
		log.debug("Using config settings:" + config);

		try {
			final File outputDirectory = FileUtil.createTempDirectory(config.getWorkingDirectory());

			log.debug("Final output directory that gets uploaded: " + outputDirectory);

			File unpackedFile = config.getUnpacker().unpack(unpackMethod());

			Status status = delegateUpload(progress, unpackedFile, outputDirectory);

			progress.ended(!status.isError(), status.getDescription());
			
		} catch(Throwable e) {
			String cause = "Internal Error - See log";

			if(e instanceof PrettyException) {
				PrettyException prettyExcep = (PrettyException) e;

				cause = prettyExcep.getFriendlyMsg();

				log.error("Error: " + prettyExcep.getDetailedMsg());
			} else {
				log.error("Unexpected error has occured", e);
			}
			
			handleUncaughtFailure(cause);

			progress.ended(false, cause);
		}
	}
	
	private Unpacker.Method unpackMethod() {
		if(requiresUnpacked()) 
			return Unpacker.Method.DECOMPRESS;
		else
			return Unpacker.Method.NONE;
	}
	
	protected void handleUncaughtFailure(String reason) {}

	protected abstract boolean requiresUnpacked();

	protected abstract Status delegateUpload(final DetailedProgressListener progress, File contentToParse, File outputDirectory) throws Exception;
}
