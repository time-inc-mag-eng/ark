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
package com.timeInc.ark.request;

/**
 * The Class CommonAppRequest.
 */
public class CommonAppRequest {
	private Boolean requireContentFirst;
	private Boolean sendMsg;
	private Boolean active;
	private String productId;
	private String pubShortName;
	private String defaultPrice;
	private String imagePreviews;
	private String userName;
	private String password;
	private String appName;
	private String id;
	private Boolean contentEmail;
	private Boolean previewEmail;
	private String contentRecepients;
	private String contentEmailSubject;
	private String contentEmailBody;
	private String previewRecepients;
	private String previewEmailSubject;
	private String previewEmailBody;
	

	/**
	 * Gets the content email.
	 * 
	 * @return the content email
	 */
	public Boolean getContentEmail() {
		return contentEmail;
	}
	
	/**
	 * Sets the content email.
	 * 
	 * @param contentEmail
	 *            the new content email
	 */
	public void setContentEmail(Boolean contentEmail) {
		this.contentEmail = contentEmail;
	}
	
	/**
	 * Gets the preview email.
	 * 
	 * @return the preview email
	 */
	public Boolean getPreviewEmail() {
		return previewEmail;
	}
	
	/**
	 * Sets the preview email.
	 * 
	 * @param previewEmail
	 *            the new preview email
	 */
	public void setPreviewEmail(Boolean previewEmail) {
		this.previewEmail = previewEmail;
	}
	
	/**
	 * Gets the content recepients.
	 * 
	 * @return the content recepients
	 */
	public String getContentRecepients() {
		return contentRecepients;
	}
	
	/**
	 * Sets the content recepients.
	 * 
	 * @param contentRecepients
	 *            the new content recepients
	 */
	public void setContentRecepients(String contentRecepients) {
		this.contentRecepients = contentRecepients;
	}
	
	/**
	 * Gets the content email subject.
	 * 
	 * @return the content email subject
	 */
	public String getContentEmailSubject() {
		return contentEmailSubject;
	}
	
	/**
	 * Sets the content email subject.
	 * 
	 * @param contentEmailSubject
	 *            the new content email subject
	 */
	public void setContentEmailSubject(String contentEmailSubject) {
		this.contentEmailSubject = contentEmailSubject;
	}
	
	/**
	 * Gets the content email body.
	 * 
	 * @return the content email body
	 */
	public String getContentEmailBody() {
		return contentEmailBody;
	}
	
	/**
	 * Sets the content email body.
	 * 
	 * @param contentEmailBodyTemplate
	 *            the new content email body
	 */
	public void setContentEmailBody(String contentEmailBodyTemplate) {
		this.contentEmailBody = contentEmailBodyTemplate;
	}
	
	/**
	 * Gets the preview recepients.
	 * 
	 * @return the preview recepients
	 */
	public String getPreviewRecepients() {
		return previewRecepients;
	}
	
	/**
	 * Sets the preview recepients.
	 * 
	 * @param previewRecepients
	 *            the new preview recepients
	 */
	public void setPreviewRecepients(String previewRecepients) {
		this.previewRecepients = previewRecepients;
	}
	
	/**
	 * Gets the preview email subject.
	 * 
	 * @return the preview email subject
	 */
	public String getPreviewEmailSubject() {
		return previewEmailSubject;
	}
	
	/**
	 * Sets the preview email subject.
	 * 
	 * @param previewEmailSubject
	 *            the new preview email subject
	 */
	public void setPreviewEmailSubject(String previewEmailSubject) {
		this.previewEmailSubject = previewEmailSubject;
	}
	
	/**
	 * Gets the preview email body.
	 * 
	 * @return the preview email body
	 */
	public String getPreviewEmailBody() {
		return previewEmailBody;
	}
	
	/**
	 * Sets the preview email body.
	 * 
	 * @param previewEmailBodyTemplate
	 *            the new preview email body
	 */
	public void setPreviewEmailBody(String previewEmailBodyTemplate) {
		this.previewEmailBody = previewEmailBodyTemplate;
	}
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
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
	 * Sets the app name.
	 *
	 * @param appName the new app name
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Gets the image previews.
	 *
	 * @return the image previews
	 */
	public String getImagePreviews() {
		return imagePreviews;
	}
	
	/**
	 * Sets the image previews.
	 *
	 * @param imagePreviews the new image previews
	 */
	public void setImagePreviews(String imagePreviews) {
		this.imagePreviews = imagePreviews;
	}
	
	/**
	 * Gets the require content first.
	 *
	 * @return the require content first
	 */
	public Boolean getRequireContentFirst() {
		return requireContentFirst;
	}
	
	/**
	 * Sets the require content first.
	 *
	 * @param requireContentFirst the new require content first
	 */
	public void setRequireContentFirst(Boolean requireContentFirst) {
		this.requireContentFirst = requireContentFirst;
	}
	
	/**
	 * Gets the send msg.
	 *
	 * @return the send msg
	 */
	public Boolean getSendMsg() {
		return sendMsg;
	}
	
	/**
	 * Sets the send msg.
	 *
	 * @param sendMsg the new send msg
	 */
	public void setSendMsg(Boolean sendMsg) {
		this.sendMsg = sendMsg;
	}
	
	/**
	 * Gets the active.
	 *
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
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
	 * Gets the product id.
	 *
	 * @return the product id
	 */
	public String getProductId() {
		return productId;
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
	 * Gets the pub short name.
	 *
	 * @return the pub short name
	 */
	public String getPubShortName() {
		return pubShortName;
	}
	
	/**
	 * Sets the pub short name.
	 *
	 * @param pubShortName the new pub short name
	 */
	public void setPubShortName(String pubShortName) {
		this.pubShortName = pubShortName;
	}
	
	/**
	 * Gets the default price.
	 *
	 * @return the default price
	 */
	public String getDefaultPrice() {
		return defaultPrice;
	}
	
	/**
	 * Sets the default price.
	 *
	 * @param defaultPrice the new default price
	 */
	public void setDefaultPrice(String defaultPrice) {
		this.defaultPrice = defaultPrice;
	}
}