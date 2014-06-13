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

import com.timeInc.ark.response.ResponseFactory;

/**
 * The Class LogReportBean.
 */
public class ReportBean { 
	
	/** The Constant HEADER_NAME. */
	public static final String HEADER_NAME = "Name";
	
	/** The Constant HEADER_APP_NAME. */
	public static final String HEADER_APP_NAME = "App Name";
	
	/** The Constant HEADER_SHORT_DATE. */
	public static final String HEADER_SHORT_DATE = "Short Date";
	
	/** The Constant HEADER_ISSUE_NAME. */
	public static final String HEADER_ISSUE_NAME = "Issue Name";
	
	/** The Constant HEADER_REF_ID. */
	public static final String HEADER_REF_ID = "Reference Id";
	
	/** The Constant HEADER_ACTION_OCCURRED. */
	public static final String HEADER_ACTION_OCCURRED = "Date";
	
	/** The Constant HEADER_ACTION. */
	public static final String HEADER_ACTION = "Action";
	
	/** The Constant HEADER_ACTION_DESCRIPTION. */
	public static final String HEADER_ACTION_DESCRIPTION = "Description";
	
	private final String appName;
	private final Date shortDate;
	private final String name;
	private final String referenceId;
	private final String description;
	private final String eventDescription;
	private final Date date;
	private final String issueName;

	private static final String EMPTY = "N/A";
	
	
	/**
	 * Instantiates a new log report bean.
	 *
	 * @param appName the app name
	 * @param shortDate the short date
	 * @param name the name
	 * @param referenceId the reference id
	 * @param description the description
	 * @param eventDescription the event description
	 * @param date the date
	 * @param issueName the issue name
	 */
	public ReportBean(String appName, Date shortDate, String name,
			String referenceId, String description, String eventDescription,
			Date date, String issueName) {
		
		this.appName = appName;
		this.shortDate = shortDate;
		this.name = name;
		this.referenceId = referenceId;
		this.description = description;
		this.eventDescription = eventDescription;
		this.date = date;
		this.issueName = issueName;
	}


	/**
	 * Instantiates a new log report bean.
	 *
	 * @param entry the entry
	 */
	public ReportBean(IssueEvent entry) {
		this(EMPTY, entry.getShortDate(), entry.getName(), EMPTY, 
				entry.getDescription(), entry.getEventName(), entry.getOccurrence(), entry.getIssueName());
	}


	/**
	 * Instantiates a new log report bean.
	 *
	 * @param entry the entry
	 */
	public ReportBean(IssueMetaEvent entry) {
		this(entry.getAppName(), entry.getShortDate(), entry.getName(), entry.getReferenceId(), 
				entry.getDescription(), entry.getEventName(), entry.getOccurrence(), entry.getIssueName());
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
	 * Gets the short date.
	 *
	 * @return the short date
	 */
	public Date getShortDate() {
		return shortDate;
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
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the event description.
	 *
	 * @return the event description
	 */
	public String getEventDescription() {
		return eventDescription;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Date getDate() {
		return date;
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
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * A factory for creating LogReportBean objects.
	 */
	public static class LogReportBeanFactory implements ResponseFactory<ReportBean, IssueEvent> {
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.response.ResponseFactory#getInstance(java.lang.Object)
		 */
		@Override
		public ReportBean getInstance(IssueEvent param) {
			if(IssueMetaEvent.class.isAssignableFrom(param.getClass())) 
				return new ReportBean(IssueMetaEvent.class.cast(param));
			else
				return new ReportBean(param);
		}
	}
}
