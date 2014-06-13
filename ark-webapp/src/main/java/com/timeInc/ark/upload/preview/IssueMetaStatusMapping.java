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
package com.timeInc.ark.upload.preview;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.mageng.util.misc.Status;

/**
 * Mapping between a status and an IssueMeta
 * @see com.timeInc.ark.upload.ConcurrentUploader.ConcurrentStatusUploader
 */
public class IssueMetaStatusMapping {
	
	/** The meta. */
	public final IssueMeta meta;
	
	/** The status. */
	public final Status status;
	
	/**
	 * Instantiates a new issue meta status mapping.
	 *
	 * @param meta the meta
	 * @param status the status
	 */
	public IssueMetaStatusMapping(IssueMeta meta, Status status) {
		super();
		this.meta = meta;
		this.status = status;
	}
}
