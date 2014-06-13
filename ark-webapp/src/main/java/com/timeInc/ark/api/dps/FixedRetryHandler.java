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
package com.timeInc.ark.api.dps;

import org.apache.log4j.Logger;

import com.timeInc.dps.producer.RetryHandler;

/**
 * Retries "unknown" error code from Adobe DPS a fixed number of
 * times.
 */
public class FixedRetryHandler implements RetryHandler {
	private static final Logger log = Logger.getLogger(FixedRetryHandler.class);
	
	private final int timesToRetry;

	/**
	 * Instantiates a new fixed retry handler.
	 *
	 * @param timesToRetry the times to retry
	 */
	public FixedRetryHandler(int timesToRetry) {
		this.timesToRetry = timesToRetry;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.dps.producer.RetryHandler#canRetry(int, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean canRetry(int retryCount, String errorCode, String errorDetail) {
		if(retryCount <= timesToRetry && errorCode.equalsIgnoreCase("unknown")) {			
			log.debug("Retrying request " + retryCount + " out of " + timesToRetry);
			log.info("Retrying dps known error:" + errorCode + ":" + errorDetail);
			return true;
		}
		else {
			log.error(errorCode + ":" + errorDetail);
			return false;
		}
	}
}
