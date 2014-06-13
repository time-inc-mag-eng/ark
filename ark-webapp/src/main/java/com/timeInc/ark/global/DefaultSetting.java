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
package com.timeInc.ark.global;

/**
 * The Class DefaultSetting.
 */
public class DefaultSetting {

	private String dpsConsumerSecret;
	private String dpsConsumerKey;
	private String dpsUrl;
	private String dpsRetryCount;

	/**
	 * Instantiates a new default setting.
	 *
	 * @param consumersecret the consumersecret
	 * @param consumerkey the consumerkey
	 * @param apiurl the apiurl
	 * @param dpsRetryCount the dps retry count
	 */
	public DefaultSetting(String consumersecret, String consumerkey,
			String apiurl, String dpsRetryCount) {
		
		this.dpsConsumerSecret = consumersecret;
		this.dpsConsumerKey = consumerkey;
		this.dpsUrl = apiurl;
		this.dpsRetryCount = dpsRetryCount;
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
	 * Gets the dps consumer secret.
	 *
	 * @return the dps consumer secret
	 */
	public String getDpsConsumerSecret() {
		return dpsConsumerSecret;
	}

	/**
	 * Gets the consumer key.
	 *
	 * @return the consumer key
	 */
	public String getConsumerKey() {
		return dpsConsumerKey;
	}

	/**
	 * Gets the dps url.
	 *
	 * @return the dps url
	 */
	public String getDpsUrl() {
		return dpsUrl;
	}
}
