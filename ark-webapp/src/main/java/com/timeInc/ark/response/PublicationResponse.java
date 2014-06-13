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

import com.timeInc.ark.role.PublicationGroup;

/**
 * Response returned that represents an PublicationGroup
 */
public class PublicationResponse {
	private final int id;
	private final String name;
	private final int numDaysReport;
	
	PublicationResponse(PublicationGroup group) {
		this.id = group.getId();
		this.name = group.getName();
		this.numDaysReport = group.getNumDaysReport();
	}
	
	
	/**
	 * A factory for creating PublicationResponse objects.
	 */
	public static class PublicationResponseFactory implements ResponseFactory<PublicationResponse, PublicationGroup> {
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.response.ResponseFactory#getInstance(java.lang.Object)
		 */
		@Override
		public PublicationResponse getInstance(PublicationGroup group) {
			return new PublicationResponse(group);
		}
	}
	
}
