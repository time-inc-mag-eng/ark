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

import java.math.BigDecimal;
import java.util.Date;

import com.timeInc.ark.application.Application;
import com.timeInc.ark.issue.remote.RemoteMetaData;
import com.timeInc.ark.role.PublicationGroup;

/**
 * An IssueMeta represents the metadata associated with an Issue
 * for a particular Application.
 */
public class IssueMeta implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private Issue issue;
	private Date onSaleDate;
	private String volume;
	private String referenceId;
	private BigDecimal price;
	private PaymentType payment;
	
	private Folio folio;
	
	private Date publishedDate;
	private Double size = 0.0;
	private Boolean active = true;
	private Boolean contentUploaded = false;
	private Boolean previewUploaded = false;
	
	private Application<?,?> application;

	/**
	 * Instantiates a new issue meta.
	 */
	public IssueMeta() {}
	
	/**
	 * Instantiates a new issue meta.
	 *
	 * @param issue the issue
	 * @param onSaleDate the on sale date
	 * @param volume the volume
	 * @param referenceId the reference id
	 * @param price the price
	 * @param payment the payment
	 * @param application the application
	 */
	public IssueMeta(Issue issue, Date onSaleDate, String volume,
			String referenceId, BigDecimal price, PaymentType payment,
			Application<?,?> application) {
		this.issue = issue;
		this.onSaleDate = onSaleDate;
		this.volume = volume;
		this.referenceId = referenceId;
		this.price = price;
		this.payment = payment;
		this.application = application;
	}

	/**
	 * Content uploaded.
	 *
	 * @param contentSize the content size
	 */
	public void contentUploaded(long contentSize) {
		this.size = ((double)contentSize) / ((double)(1024 * 1024));
		this.contentUploaded = true;
	}
	
	/**
	 * Content published.
	 */
	public void contentPublished() {
		this.publishedDate = new Date();
	}
	
	/**
	 * Content unpublished.
	 */
	public void contentUnpublished() {
		this.publishedDate = null;
	}
	
	/**
	 * Preview uploaded.
	 */
	public void previewUploaded() {
		this.previewUploaded = true;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * Gets the remote meta.
	 *
	 * @return the remote meta
	 */
	public RemoteMetaData getRemoteMeta() {
		return application.getRemoteMetaFor(this);
	}
	
	/**
	 * Reset state.
	 */
	public void resetState() {
		this.folio = null;
		this.publishedDate = null;
		this.size = 0.0;
		this.contentUploaded = false;
		this.previewUploaded = false;
	}
	
	
	/**
	 * Gets the application.
	 *
	 * @return the application
	 */
	public Application<?,?> getApplication() {
		return application;
	}
	
	/**
	 * Gets the application.
	 *
	 * @param <T> the generic type
	 * @param clz the clz
	 * @return the application
	 */
	public <T extends Application<?,?>> T getApplication(final Class<T> clz) {
		if(clz.isInstance(this.getApplication()))
			return clz.cast(this.getApplication());
		else 
			throw new IllegalArgumentException("IssueMeta does not have associated application of class:" + clz);
	}
	
	/**
	 * Gets the folio.
	 *
	 * @return the folio
	 */
	public Folio getFolio() {
		return folio;
	}
	
	/**
	 * Checks if is published.
	 *
	 * @return true, if is published
	 */
	public boolean isPublished() {
		return publishedDate != null;
	}
	
	/**
	 * Sets the folio.
	 *
	 * @param folio the new folio
	 */
	public void setFolio(Folio folio) {
		this.folio = folio;
		folio.setMeta(this);
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
	 * Gets the publication.
	 *
	 * @return the publication
	 */
	public PublicationGroup getPublication() {
		return getApplication().getPublicationGroup();
	}
	
	/**
	 * Gets the application name.
	 *
	 * @return the application name
	 */
	public String getApplicationName() {
		return getApplication().getName();
	}
	
	/**
	 * Gets the issue name.
	 *
	 * @return the issue name
	 */
	public String getIssueName() {
		return issue.getName();
	}
	
	/**
	 * Checks if is test issue.
	 *
	 * @return true, if is test issue
	 */
	public boolean isTestIssue() {
		return issue.getTestIssue();
	}
	
	/**
	 * Gets the cover date.
	 *
	 * @return the cover date
	 */
	public Date getCoverDate() {
		return issue.getShortDate();
	}

	/**
	 * Gets the on sale date.
	 *
	 * @return the on sale date
	 */
	public Date getOnSaleDate() {
		return onSaleDate;
	}

	/**
	 * Checks if is content uploaded.
	 *
	 * @return true, if is content uploaded
	 */
	public boolean isContentUploaded() {
		return contentUploaded;
	}

	/**
	 * Checks if is preview uploaded.
	 *
	 * @return true, if is preview uploaded
	 */
	public boolean isPreviewUploaded() {
		return previewUploaded;
	}
	
	/**
	 * Gets the volume.
	 *
	 * @return the volume
	 */
	public String getVolume() {
		return volume;
	}
	
	/**
	 * Gets the issue id.
	 *
	 * @return the issue id
	 */
	public int getIssueId() {
		return issue.getId();
	}
	
	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	public boolean isActive() {
		return this.active;
	}
	
	/**
	 * Gets the payment type.
	 *
	 * @return the payment type
	 */
	public PaymentType getPaymentType() {
		return payment;
	}

	/**
	 * Gets the price.
	 *
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * Gets the published date.
	 *
	 * @return the published date
	 */
	public Date getPublishedDate() {
		return this.publishedDate;
	}

	/**
	 * Sets the issue.
	 *
	 * @param issue the new issue
	 */
	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	/**
	 * Sets the on sale date.
	 *
	 * @param onSaleDate the new on sale date
	 */
	public void setOnSaleDate(Date onSaleDate) {
		this.onSaleDate = onSaleDate;
	}

	/**
	 * Sets the volume.
	 *
	 * @param volume the new volume
	 */
	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public Double getSize() {
		return size;
	}

	/**
	 * Sets the reference id.
	 *
	 * @param referenceId the new reference id
	 */
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	/**
	 * Sets the price.
	 *
	 * @param price the new price
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * Sets the payment.
	 *
	 * @param payment the new payment
	 */
	public void setPayment(PaymentType payment) {
		this.payment = payment;
	}

	/**
	 * Sets the active.
	 *
	 * @param active the new active
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	/**
	 * Sets the application.
	 *
	 * @param application the application
	 */
	public void setApplication(Application<?, ?> application) {
		this.application = application;
	}
}
