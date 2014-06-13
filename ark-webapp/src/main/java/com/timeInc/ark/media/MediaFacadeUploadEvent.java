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
package com.timeInc.ark.media;

import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.role.User;

/**
 * Event when a {@link MediaUploadFacade} uploads a supposedly media file for a particular Issue.
 */
public interface MediaFacadeUploadEvent {
	
	/**
	 * A media file that was uploaded for a particular issue indicating the result.
	 * @param user the user who performed this action
	 * @param issue the issue the media file belongs to
	 * @param result the result of the upload. 
	 */
	void mediaUploaded(User user, Issue issue, String result);
}
