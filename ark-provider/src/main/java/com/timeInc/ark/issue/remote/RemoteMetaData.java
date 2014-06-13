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
package com.timeInc.ark.issue.remote;


/**
 * Reperesents metadata for an issue that is retrieved remotely from some service.
 */
public class RemoteMetaData {
	private final String coverStory;
	private final boolean modifiable;
	
	private static final RemoteMetaData EMPTY = new RemoteMetaData("", true);
	
	/**
	 * Retrieve an instance that represents no metadata for 
	 * instead of using nulls.
	 *
	 * @return an empty metadata
	 */
	public static RemoteMetaData empty() {
		return EMPTY;
	}
	
	/**
	 * Instantiates a new remote meta data.
	 *
	 * @param coverStory the cover story
	 * @param modifiable the modifiable
	 */
	public RemoteMetaData(String coverStory, boolean modifiable) {
		this.coverStory = coverStory;
		this.modifiable = modifiable;
		
	}
	
	/**
	 * Gets the cover story.
	 *
	 * @return the cover story
	 */
	public String getCoverStory() {
		return coverStory;
	}

	/**
	 * Checks if is modifiable.
	 *
	 * @return true, if is modifiable
	 */
	public boolean isModifiable() {
		return modifiable;
	}
}
