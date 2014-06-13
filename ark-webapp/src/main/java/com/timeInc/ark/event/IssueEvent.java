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
package com.timeInc.ark.event;

import java.util.Date;

import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.role.User;

/**
 * Represents events related to {@link Issue}.
 */
public abstract class IssueEvent extends AbstractEvent {
	private static final long serialVersionUID = 1L;
	
	private String issueName;
	private Date shortDate;
	
	IssueEvent() {}
	
	/**
	 * Instantiates a new issue event.
	 *
	 * @param user the user
	 * @param publicationGroup the publication group
	 * @param occurrence the occurrence
	 * @param description the description
	 * @param issueName the issue name
	 * @param shortDate the short date
	 */
	public IssueEvent(User user, PublicationGroup publicationGroup, Date occurrence,
			String description, String issueName, Date shortDate)  {
		super(user, publicationGroup, occurrence, description);
		
		this.issueName = issueName;
		this.shortDate = shortDate;
		
	}
	
	/**
	 * Instantiates a new issue event.
	 *
	 * @param user the user
	 * @param occurrence the occurrence
	 * @param description the description
	 * @param issue the issue
	 */
	public IssueEvent(User user, Date occurrence,
			String description, Issue issue) {
		this(user, issue.getPublication(), occurrence, description, issue.getName(), issue.getShortDate());
	}		
	
	/**
	 * Gets the issue name.
	 *
	 * @return the issue name
	 */
	public String getIssueName() {
		return issueName;
	}

	/**
	 * Gets the short date.
	 *
	 * @return the short date
	 */
	public Date getShortDate() {
		return shortDate;
	}

	/**
	 * Represents a log for an upload media file event.
	 */
	public static class MediaUpload extends IssueEvent {
		private static final long serialVersionUID = 1L;

		MediaUpload() {}
		
		/**
		 * Instantiates a new media upload event.
		 *
		 * @param user the user
		 * @param occurrence the occurrence
		 * @param description the description
		 * @param issue the issue
		 */
		public MediaUpload(User user, Date occurrence, String description, Issue issue) {
			super(user, occurrence, description, issue);
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.log.AbstractLog#getEventName()
		 */
		@Override
		public String getEventName() {
			return "Uploaded media file";
		}
	}
}





