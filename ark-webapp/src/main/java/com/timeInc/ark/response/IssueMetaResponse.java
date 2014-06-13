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

import java.math.BigDecimal;
import java.util.Date;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.issue.remote.RemoteMetaData;

/**
 * Response returned that represents an IssueMeta
 */
public class IssueMetaResponse {
	private final int id;
	private final String referenceId;
	private final Date onSaleDate;
	private final Date shortDate;
	private final String volume;
	private final String issue;
	private final Boolean testIssue;

	private final boolean active;
	private final int applicationId;
	private final int paymentId;
	private final BigDecimal price;
	private final Date publishedDate;
	private final Integer issueNameId;

	private String folioId;

	private String coverStory;
	private Boolean modifiable;
	
	

	private IssueMetaResponse(IssueMeta meta, boolean includeRemote) {
		this.id = meta.getId();
		this.referenceId = meta.getReferenceId();
		this.onSaleDate = meta.getOnSaleDate();
		this.shortDate = meta.getCoverDate();
		this.volume = meta.getVolume();

		this.active = meta.isActive();
		this.applicationId = meta.getApplication().getId();

		this.paymentId = meta.getPaymentType().getId();

		this.price = meta.getPrice();
		this.publishedDate = meta.getPublishedDate();
		
		this.issueNameId = meta.getIssueId();
		
		this.testIssue = meta.isTestIssue();
		this.issue = meta.getIssueName();
		
		if(meta.getFolio() != null) {
			this.folioId  = meta.getFolio().getFolioId();
		}
		

		if (includeRemote) {
			RemoteMetaData rMeta = meta.getRemoteMeta();
			this.modifiable = rMeta.isModifiable();
			this.coverStory = rMeta.getCoverStory();
		}
	}

	/**
	 * Factory for creating IssueMetaResponse whose information can be obtained directly in Ark
	 */
	public static class LocalMetaFactory implements ResponseFactory<IssueMetaResponse, IssueMeta> {
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.response.ResponseFactory#getInstance(java.lang.Object)
		 */
		@Override
		public IssueMetaResponse getInstance(IssueMeta param) {
			return new IssueMetaResponse(param, false);
		}
	}

	/**
	 * Factory for creating IssueMetaResponse whose information can be obtained directly in Ark and 
	 * includes additional remote information
	 */
	public static class RemoteMetaFactory implements ResponseFactory<IssueMetaResponse, IssueMeta> {
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.response.ResponseFactory#getInstance(java.lang.Object)
		 */
		@Override
		public IssueMetaResponse getInstance(IssueMeta param) {
			return new IssueMetaResponse(param, true);
		}
	}
}
