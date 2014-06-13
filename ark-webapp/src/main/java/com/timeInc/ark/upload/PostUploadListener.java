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

import java.io.IOException;
import org.apache.log4j.Logger;
import com.timeInc.mageng.util.file.FileUtil;

/**
 * 
 * A listener that is invoked when an upload process is done.
 * 
 * @see ConcurrentUploader
 */
public interface PostUploadListener {
	void afterUpload(UserUploadConfig config);

	/**
	 * Removes the working directory
	 */
	public static class WorkingDirectoryCleaner implements PostUploadListener {
		private static final Logger log = Logger.getLogger(WorkingDirectoryCleaner.class);

		/* (non-Javadoc)
		 * @see com.timeInc.ark.upload.PostUploadListener#afterUpload(com.timeInc.ark.upload.UserUploadConfig)
		 */
		@Override
		public void afterUpload(UserUploadConfig config) {
			log.debug("Cleaning up " + config.getWorkingDirectory());
			try {
				FileUtil.deleteDirectory(config.getWorkingDirectory());
			} catch (IOException e) {
				log.warn("Failed to delete " + config.getWorkingDirectory(), e);
			}
		}
	}
}



