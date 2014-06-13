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
package com.timeInc.ark.issue;

import com.timeInc.ark.issue.remote.RemoteMetaData;

/**
 * Value class to represent remote data that for a particular
 * Folio.
 */
public class FolioRemoteData extends RemoteMetaData {

	private final String targetViewer;
	
	/**
	 * Instantiates a new dps remote issue.
	 *
	 * @param coverStory the cover story
	 * @param targetViewer the target viewer
	 */
	public FolioRemoteData(String coverStory, String targetViewer) {
		super(coverStory, true);
		
		this.targetViewer = targetViewer;
	}
	
	/**
	 * Gets the target viewer.
	 *
	 * @return the target viewer
	 */
	public String getTargetViewer() {
		return targetViewer;
	}

}
