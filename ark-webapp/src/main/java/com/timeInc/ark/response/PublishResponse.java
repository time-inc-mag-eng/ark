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

import java.util.Date;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.mageng.util.misc.Status;

/**
 * Response returned that represents the result of a Publish request
 */
public class PublishResponse {
	private final int issueId;
	private final String publishResult;
	private final Date publishDate;
	
	/**
	 * Instantiates a new publish response.
	 *
	 * @param meta the meta
	 * @param result the result
	 */
	public PublishResponse(IssueMeta meta, Status result) {
		this.issueId = meta.getId();
		this.publishResult = result.getDescription();
		this.publishDate = meta.getPublishedDate();
	}
	
}
