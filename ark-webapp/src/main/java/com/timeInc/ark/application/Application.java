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
package com.timeInc.ark.application;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.issue.remote.RemoteMetaData;
import com.timeInc.ark.packager.PackageInfo;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.upload.content.ContentUploadSetting;
import com.timeInc.ark.upload.preview.PreviewUploadSetting;
import com.timeInc.ark.upload.producer.PackageProducer;
import com.timeInc.ark.util.TemplateEmail;
import com.timeInc.mageng.util.progress.ProgressListener;

/**
 * An application knows the workflow required to upload content/previews for a particular {@link IssueMeta} 
 * it contains.
 *
 * @param <T> the packaging type for content
 * @param <U> the packaging type for preview
 */
public abstract class Application<T extends PackageInfo, U extends PackageInfo>
		implements Serializable, ApplicationEntity {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;

	private PublicationGroup publicationGroup;

	private String name;
	private String productId;
	
	private Set<IssueMeta> metas;

	private BigDecimal defaultPrice;

	private String vendorPubName;
	
	private boolean active;
	private boolean sendMsg;

	private TemplateEmail contentEmail;
	
	private TemplateEmail previewEmail;
	
	private boolean requireContentFirst;

	Application() {}

	/**
	 * Upload content using the provided config; sending a content
	 * uploaded email if it has one.
	 *
	 * @param content the packaged content
	 * @param meta the particular {@link IssueMeta} that this upload belongs to 
	 * @param config the config
	 * @param listener the listener
	 */
	public final void uploadContent(T content, IssueMeta meta, ContentUploadSetting config, ProgressListener listener) {
		uploadContentHelper(content, meta, config, listener);

		if(contentEmail != null) {
			contentEmail.sendEmail(meta);
		} 
	}

	/**
	 * Uploads the content using the provided config. 
	 * @param content
	 * @param meta
	 * @param config
	 * @param listener
	 */
	protected abstract void uploadContentHelper(T content, IssueMeta meta, ContentUploadSetting config, ProgressListener listener);
	
	/**
	 * The content producer that takes in a user uploaded file 
	 * and produces package of type T that gets uploaded to 
	 * the destination.
	 * 
	 * @return the content producer
	 */
	public abstract PackageProducer<T, ?> getContentProducer();
	
	/**
	 * Upload previews using the provided config; sending a preview
	 * uploaded email if it has one.
	 * @param preview the preview package
	 * @param meta the meta
	 * @param config the config
	 */
	public final void uploadPreview(U preview, IssueMeta meta, PreviewUploadSetting config) {
		uploadPreviewHelper(preview, meta, config);
		
		if(previewEmail != null)
			previewEmail.sendEmail(meta);
	}
	
	
	/**
	 * Uploads the preview using the provided config. 
	 * @param preview
	 * @param meta
	 * @param config
	 */
	protected abstract void uploadPreviewHelper(U preview, IssueMeta meta, PreviewUploadSetting config);

	/**
	 * The preview producer that takes in a user uploaded file 
	 * and produces package of type U that gets uploaded 
	 * to the destination.
	 *
	 * @return the content producer
	 */
	public abstract PackageProducer<U, ?> getPreviewProducer();
	
	
	
	/**
	 * Gets the maximum cover story that the destination accepts.
	 *
	 * @return the max cover story
	 */
	public int getMaxCoverStory() {
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Gets the remote metadata that corresponds to a particular
	 * {@link IssueMeta}.
	 *
	 * @param meta the meta
	 * @return the remote metadata
	 */
	public RemoteMetaData getRemoteMetaFor(IssueMeta meta) {
		return RemoteMetaData.empty();
	}
	
	/**
	 * Gets the publication group that this application belongs to.
	 *
	 * @return the publication group
	 */
	public PublicationGroup getPublicationGroup() {
		return publicationGroup;
	}

	/**
	 * Gets the name of the application.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the default price for the {@link IssueMeta} it
	 * contains.
	 *
	 * @return the default price
	 */
	public BigDecimal getDefaultPrice() {
		return defaultPrice;
	}
	
	/**
	 * Sets the default price for the {@link IssueMeta} it 
	 * contains.
	 *
	 * @param defaultPrice the new default price
	 */
	public void setDefaultPrice(BigDecimal defaultPrice) {
		this.defaultPrice = defaultPrice;
	}
	
	/**
	 * Gets the product id.
	 *
	 * @return the product id
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * Gets the publication name that the destination
	 * may expect.
	 *
	 * @return the vendor pub name
	 */
	public String getVendorPubName() {
		return vendorPubName;
	}

	/**
	 * Determines whether content upload is required
	 * before previews
	 *
	 * @return true, if is 
	 */
	public boolean isRequireContentFirst() {
		return requireContentFirst;
	}
	

	/**
	 * Determines whether the application is active.
	 *
	 * @return true, if it is active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Make an application active or inactive
	 *
	 * @param active true if active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Determines whether an application should 
	 * send a msg.
	 *
	 * @return true, if is send msg
	 */
	public boolean isSendMsg() {
		return sendMsg;
	}

	/**
	 * Sets the send msg.
	 *
	 * @param sendMsg the new send msg
	 */
	public void setSendMsg(boolean sendMsg) {
		this.sendMsg = sendMsg;
	}

	/**
	 * Sets the publication group.
	 *
	 * @param publicationGroup the new publication group
	 */
	public void setPublicationGroup(PublicationGroup publicationGroup) {
		this.publicationGroup = publicationGroup;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the product id.
	 *
	 * @param productId the new product id
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * Sets the vendor pub name.
	 *
	 * @param vendorPubName the new vendor pub name
	 */
	public void setVendorPubName(String vendorPubName) {
		this.vendorPubName = vendorPubName;
	}

	/**
	 * Sets the content email.
	 *
	 * @param contentEmail the new content email
	 */
	public void setContentEmail(TemplateEmail contentEmail) {
		this.contentEmail = contentEmail;
	}

	/**
	 * Sets the preview email.
	 *
	 * @param previewEmail the new preview email
	 */
	public void setPreviewEmail(TemplateEmail previewEmail) {
		this.previewEmail = previewEmail;
	}

	/**
	 * Sets the require content first.
	 *
	 * @param requireContentFirst the new require content first
	 */
	public void setRequireContentFirst(boolean requireContentFirst) {
		this.requireContentFirst = requireContentFirst;
	}
	
	/**
	 * Gets the content email.
	 *
	 * @return the content email
	 */
	public TemplateEmail getContentEmail() {
		return contentEmail;
	}

	/**
	 * Gets the preview email.
	 *
	 * @return the preview email
	 */
	public TemplateEmail getPreviewEmail() {
		return previewEmail;
	}
}
