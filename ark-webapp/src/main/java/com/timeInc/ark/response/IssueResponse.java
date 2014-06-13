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

import com.timeInc.ark.issue.Issue;

/**
 * Response returned that represents an Issue
 */
public class IssueResponse {
	private final int id;
	private final String name;
	private final Date shortDate;
	private final boolean testIssue;

	/**
	 * Instantiates a new issue response.
	 *
	 * @param issue the issue
	 */
	public IssueResponse(Issue issue) {
		this.id = issue.getId();
		this.name = issue.getName();
		this.shortDate = issue.getShortDate();
		this.testIssue = issue.getTestIssue();
	}
	
	
	/**
	 * A factory for creating IssueResponse objects.
	 */
	public static class IssueResponseFactory implements ResponseFactory<IssueResponse, Issue> {
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.response.ResponseFactory#getInstance(java.lang.Object)
		 */
		@Override
		public IssueResponse getInstance(Issue issue) {
			return new IssueResponse(issue);
		}
	}
	
}
