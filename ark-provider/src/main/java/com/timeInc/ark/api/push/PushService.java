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
package com.timeInc.ark.api.push;

import java.util.Date;

/**
 * 
 * Sends a remote newsstand push using the provided values.
 *
 * @param <T> optional metadata associated with push
 */
public interface PushService<T> {
	
	/**
	 * The key pair value used to represent the association
	 * with an issue. 
	 */
	public static class IssueKeyPair {
		
		/** The issue key. */
		public final String key;
		
		/** The issue value */
		public final String val;
		
		/**
		 * Instantiates a new issue key pair.
		 *
		 * @param key the key
		 * @param val the val
		 */
		public IssueKeyPair(String key, String val) {
			this.key = key;
			this.val = val;
		}
	}
	
	
	/**
	 * Sends a remote newsstand push.
	 * @param config the optional metadata that is associated with push
	 * @param productId 
	 * @param scheduleDate
	 * @param pair
	 * @return true if request was a success; false otherwise
	 */
	boolean sendNewsstandPush(T config, String productId, Date scheduleDate, IssueKeyPair pair);
}
