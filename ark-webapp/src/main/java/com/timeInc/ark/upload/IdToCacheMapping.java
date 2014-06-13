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

/**
 * A mapping between an object's PK and the cache id
 */
public class IdToCacheMapping {
	
	/** The id. */
	public final int id;
	
	/** The cache id. */
	public final String cacheId;

	/**
	 * Instantiates a new int to cache id mapping.
	 *
	 * @param id the id
	 * @param cacheId the cache id
	 */
	public IdToCacheMapping(int id, String cacheId) {
		this.id = id;
		this.cacheId = cacheId;
	}
}
