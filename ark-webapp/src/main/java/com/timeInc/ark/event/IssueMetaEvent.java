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

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.role.User;

/**
 * Represents events related to {@link IssueMeta}.
 */
public abstract class IssueMetaEvent extends IssueEvent {
	private static final long serialVersionUID = 1L;
	private String referenceId;
	private String appName;

	IssueMetaEvent() {}
	
	/**
	 * Instantiates a new issue meta event.
	 *
	 * @param user the user
	 * @param occurrence the occurrence
	 * @param description the description
	 * @param meta the meta
	 */
	public IssueMetaEvent(User user, Date occurrence,
			String description, IssueMeta meta) {
		
		this(user, occurrence, description, meta.getPublication(), meta.getIssueName(), meta.getCoverDate(), meta.getReferenceId(), meta.getApplicationName());
	}
	
	/**
	 * Instantiates a new issue meta event.
	 *
	 * @param user the user
	 * @param occurrence the occurrence
	 * @param description the description
	 * @param group the group
	 * @param issueName the issue name
	 * @param coverDate the cover date
	 * @param referenceId the reference id
	 * @param appName the app name
	 */
	public IssueMetaEvent(User user, Date occurrence,
			String description, PublicationGroup group, 
			String issueName, Date coverDate,
			String referenceId, String appName) {
		
		super(user, group, occurrence, description, issueName, coverDate);
		
		this.referenceId = referenceId;
		this.appName = appName;
	}
	
	
	/**
	 * Gets the reference id.
	 *
	 * @return the reference id
	 */
	public String getReferenceId() {
		return referenceId;
	}

	/**
	 * Gets the app name.
	 *
	 * @return the app name
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * Represents an event to indicate that this is the first time content 
	 * has been uploaded.
	 */
	public static class NewContent extends IssueMetaEvent {
		private static final long serialVersionUID = 1L;

		NewContent() {}
		
		/**
		 * Instantiates a new new content.
		 *
		 * @param user the user
		 * @param occurrence the occurrence
		 * @param description the description
		 * @param meta the meta
		 */
		public NewContent(User user, Date occurrence,
				String description, IssueMeta meta) {
			
			super(user, occurrence, description, meta);
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.log.AbstractLog#getEventName()
		 */
		@Override
		public String getEventName() {
			return "Uploaded new content";
		}
	}
	
	/**
	 * Represents an event to indicate that this is not the first time content 
	 * has been uploaded.
	 */
	public static class UpdateContent extends IssueMetaEvent {
		private static final long serialVersionUID = 1L;

		UpdateContent() {}
		
		/**
		 * Instantiates a new update content.
		 *
		 * @param user the user
		 * @param occurrence the occurrence
		 * @param description the description
		 * @param meta the meta
		 */
		public UpdateContent(User user, Date occurrence,
				String description, IssueMeta meta) {
			
			super(user, occurrence, description, meta);
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.log.AbstractLog#getEventName()
		 */
		@Override
		public String getEventName() {
			return "Updated content";
		}
	}
	
	/**
	 * Represents an event to indicate that this is the first time previews 
	 * has been uploaded.
	 */
	public static class NewPreview extends IssueMetaEvent {
		private static final long serialVersionUID = 1L;

		NewPreview() {}
		
		/**
		 * Instantiates a new new preview.
		 *
		 * @param user the user
		 * @param occurrence the occurrence
		 * @param description the description
		 * @param meta the meta
		 */
		public NewPreview(User user, Date occurrence,
				String description, IssueMeta meta) {
			
			super(user, occurrence, description, meta);
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.log.AbstractLog#getEventName()
		 */
		@Override
		public String getEventName() {
			return "Uploaded new previews";
		}
	}
	
	/**
	 * Represents an event to indicate that this is not the first time previews 
	 * has been uploaded.
	 */
	public static class UpdatePreview extends IssueMetaEvent {
		private static final long serialVersionUID = 1L;

		UpdatePreview() {}
		
		/**
		 * Instantiates a new update preview.
		 *
		 * @param user the user
		 * @param occurrence the occurrence
		 * @param description the description
		 * @param meta the meta
		 */
		public UpdatePreview(User user, Date occurrence,
				String description, IssueMeta meta) {
			
			super(user, occurrence, description, meta);
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.log.AbstractLog#getEventName()
		 */
		@Override
		public String getEventName() {
			return "Updated previews";
		}
	}
	
	/**
	 * Represents an event to indicate that content has been published.
	 */
	public static class PublishContent extends IssueMetaEvent {
		private static final long serialVersionUID = 1L;

		PublishContent() {}
		
		/**
		 * Instantiates a new publish content.
		 *
		 * @param user the user
		 * @param occurrence the occurrence
		 * @param description the description
		 * @param group the group
		 * @param issueName the issue name
		 * @param coverDate the cover date
		 * @param referenceId the reference id
		 * @param appName the app name
		 */
		public PublishContent(User user, Date occurrence,
				String description, PublicationGroup group, 
				String issueName, Date coverDate,
				String referenceId, String appName) {
			
			super(user, occurrence, description, group, issueName, coverDate, referenceId, appName);
			
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.log.AbstractLog#getEventName()
		 */
		@Override
		public String getEventName() {
			return "Published content";
		}
	}
	
	/**
	 * Represents an event to indicate that an IssueMeta as been created
	 */
	public static class Creation extends IssueMetaEvent {
		private static final long serialVersionUID = 1L;

		Creation() {}
		
		/**
		 * Instantiates a new creation.
		 *
		 * @param user the user
		 * @param occurrence the occurrence
		 * @param description the description
		 * @param group the group
		 * @param issueName the issue name
		 * @param coverDate the cover date
		 * @param referenceId the reference id
		 * @param appName the app name
		 */
		public Creation(User user, Date occurrence,
				String description, PublicationGroup group, 
				String issueName, Date coverDate,
				String referenceId, String appName) {
			super(user, occurrence, description, group, issueName, coverDate, referenceId, appName);
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.log.AbstractLog#getEventName()
		 */
		@Override
		public String getEventName() {
			return "Created Issue Metadata";
		}
	}
	
	/**
	 * Represents an event to indicate that an IssueMeta as been updated
	 */
	public static class Update extends IssueMetaEvent {
		private static final long serialVersionUID = 1L;

		Update() {}
		
		/**
		 * Instantiates a new update.
		 *
		 * @param user the user
		 * @param occurrence the occurrence
		 * @param description the description
		 * @param group the group
		 * @param issueName the issue name
		 * @param coverDate the cover date
		 * @param referenceId the reference id
		 * @param appName the app name
		 */
		public Update(User user, Date occurrence,
				String description, PublicationGroup group, 
				String issueName, Date coverDate,
				String referenceId, String appName) {
			super(user, occurrence, description, group, issueName, coverDate, referenceId, appName);
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.log.AbstractLog#getEventName()
		 */
		@Override
		public String getEventName() {
			return "Updated Issue Metadata";
		}
	}
	
	/**
	 * Represents an event to indicate that an IssueMeta as been deleted
	 */
	public static class Deletion extends IssueMetaEvent {
		private static final long serialVersionUID = 1L;

		Deletion() {}
		
		/**
		 * Instantiates a new deletion.
		 *
		 * @param user the user
		 * @param occurrence the occurrence
		 * @param description the description
		 * @param group the group
		 * @param issueName the issue name
		 * @param coverDate the cover date
		 * @param referenceId the reference id
		 * @param appName the app name
		 */
		public Deletion(User user, Date occurrence,
				String description, PublicationGroup group, 
				String issueName, Date coverDate,
				String referenceId, String appName) {
			super(user, occurrence, description, group, issueName, coverDate, referenceId, appName);
		}
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.log.AbstractLog#getEventName()
		 */
		@Override
		public String getEventName() {
			return "Deleted Issue Metadata";
		}
	}
	
	
	/**
	 * Represents an event to indicate that a Folio has been unpublished
	 */
	public static class UnpublishFolio extends IssueMetaEvent {
		private static final long serialVersionUID = 1L;

		UnpublishFolio() {}
		
		/**
		 * Instantiates a new unpublish folio.
		 *
		 * @param user the user
		 * @param occurrence the occurrence
		 * @param description the description
		 * @param group the group
		 * @param issueName the issue name
		 * @param coverDate the cover date
		 * @param referenceId the reference id
		 * @param appName the app name
		 */
		public UnpublishFolio(User user, Date occurrence,
				String description, PublicationGroup group, 
				String issueName, Date coverDate,
				String referenceId, String appName) {
			
			super(user, occurrence, description, group, issueName, coverDate, referenceId, appName);
		}
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.log.AbstractLog#getEventName()
		 */
		@Override
		public String getEventName() {
			return "Unpublished folio from Adobe";
		}
	}
	
	/**
	 * Represents an event to indicate that a Folio has been deleted
	 */
	public static class DeleteFolio extends IssueMetaEvent {
		private static final long serialVersionUID = 1L;

		DeleteFolio() {}
		
		/**
		 * Instantiates a new delete folio.
		 *
		 * @param user the user
		 * @param occurrence the occurrence
		 * @param description the description
		 * @param group the group
		 * @param issueName the issue name
		 * @param coverDate the cover date
		 * @param referenceId the reference id
		 * @param appName the app name
		 */
		public DeleteFolio(User user, Date occurrence,
				String description, PublicationGroup group, 
				String issueName, Date coverDate,
				String referenceId, String appName) {
			
			super(user, occurrence, description, group, issueName, coverDate, referenceId, appName);
		}
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.log.AbstractLog#getEventName()
		 */
		@Override
		public String getEventName() {
			return "Deleted folio from Adobe";
		}
	}
	
	/**
	 * Represents an event to indicate that a push has been scheduled
	 */
	public static class PushSchedule extends IssueMetaEvent {
		private static final long serialVersionUID = 1L;

		PushSchedule() {}
		
		/**
		 * Instantiates a new push schedule.
		 *
		 * @param user the user
		 * @param occurrence the occurrence
		 * @param description the description
		 * @param meta the meta
		 */
		public PushSchedule(User user, Date occurrence,
				String description, IssueMeta meta) {
			
			super(user, occurrence, description, meta);
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.log.AbstractLog#getEventName()
		 */
		@Override
		public String getEventName() {
			return "Schedule push";
		}
	}	
}


