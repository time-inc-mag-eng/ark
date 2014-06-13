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
 * The Class DpsAppRequest 
 *
 */

public class DpsAppRequest extends CommonAppRequest {
	private String rendition;
	
	private String dpsConsumerSecret;
	private String dpsConsumerKey;
	private String dpsUrl;
	private String dpsRetryCount;

	/**
	 * Gets the dps consumer secret.
	 * 
	 * @return the dps consumer secret
	 */
	public String getDpsConsumerSecret() {
		return dpsConsumerSecret;
	}
	
	/**
	 * Sets the dps consumer secret.
	 * 
	 * @param dpsConsumerSecret
	 *            the new dps consumer secret
	 */
	public void setDpsConsumerSecret(String dpsConsumerSecret) {
		this.dpsConsumerSecret = dpsConsumerSecret;
	}
	
	/**
	 * Gets the dps consumer key.
	 * 
	 * @return the dps consumer key
	 */
	public String getDpsConsumerKey() {
		return dpsConsumerKey;
	}
	
	/**
	 * Sets the dps consumer key.
	 * 
	 * @param dpsConsumerKey
	 *            the new dps consumer key
	 */
	public void setDpsConsumerKey(String dpsConsumerKey) {
		this.dpsConsumerKey = dpsConsumerKey;
	}
	
	/**
	 * Gets the dps url.
	 * 
	 * @return the dps url
	 */
	public String getDpsUrl() {
		return dpsUrl;
	}
	
	/**
	 * Sets the dps url.
	 * 
	 * @param dpsUrl
	 *            the new dps url
	 */
	public void setDpsUrl(String dpsUrl) {
		this.dpsUrl = dpsUrl;
	}
	
	/**
	 * Gets the dps retry count.
	 * 
	 * @return the dps retry count
	 */
	public String getDpsRetryCount() {
		return dpsRetryCount;
	}
	
	/**
	 * Sets the dps retry count.
	 * 
	 * @param dpsRetryCount
	 *            the new dps retry count
	 */
	public void setDpsRetryCount(String dpsRetryCount) {
		this.dpsRetryCount = dpsRetryCount;
	}

	/**
	 * Gets the rendition.
	 * 
	 * @return the rendition
	 */
	public String getRendition() {
		return rendition;
	}
	
	/**
	 * Sets the rendition.
	 * 
	 * @param rendition
	 *            the new rendition
	 */
	public void setRendition(String rendition) {
		this.rendition = rendition;
	}


	

	
}
