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
package com.timeInc.ark.response;

import java.util.Collection;

/**
 * Response returned that represents an Error.
 */
public class ErrorMessage {
	private final String reason;
	private final Code errorCode;
	private final Collection<?> customData;
	
	/**
	 * Instantiates a new error message.
	 *
	 * @param reason the reason
	 * @param errorCode the error code
	 */
	public ErrorMessage(String reason, Code errorCode) {
		this(reason, errorCode, null);
	}
	
	/**
	 * Instantiates a new error message.
	 *
	 * @param reason the reason
	 * @param errorCode the error code
	 * @param customData the custom data
	 */
	public ErrorMessage(String reason, Code errorCode, Collection<?> customData) {
		this.reason = reason;
		this.errorCode = errorCode;
		this.customData = customData;
	}

	/**
	 * Gets the reason.
	 *
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * Gets the error code.
	 *
	 * @return the error code
	 */
	public Code getErrorCode() {
		return errorCode;
	}

	/**
	 * The Enum Code.
	 */
	public enum Code {
		INVALID_SESSION("invalid_session"),
		NO_ACCESS("no_access"),
		GENERIC("generic"),
		INFO("info"),
		INTERNAL("internal"),
		CONSTRAINT("constraint");
		
		private String errorCode;
		
		Code(String errorCode) {
			this.errorCode = errorCode;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override 
		public String toString() {
			return this.errorCode;
		}	
	}
}
