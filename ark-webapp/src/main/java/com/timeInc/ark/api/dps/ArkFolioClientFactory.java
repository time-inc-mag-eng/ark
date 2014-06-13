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
package com.timeInc.ark.api.dps;

import org.apache.http.client.HttpClient;

import com.timeInc.dps.producer.ManagedProducers;

/**
 * A factory for creating ArkFolioClient objects.
 */
public class ArkFolioClientFactory {
	private final HttpClient client;
	
	/**
	 * Instantiates a new ark folio client factory
	 * using the HttpClient to make requests
	 *
	 * @param client the client
	 */
	public ArkFolioClientFactory(HttpClient client) {
		this.client = client;
	}

	/**
	 * Gets an instance of {@code ArkFolioClient}
	 * using the specified authentication parameters
	 *
	 * @param address the address
	 * @param consumerKey the consumer key
	 * @param consumerSecret the consumer secret
	 * @param timesToRetry the times to retry
	 * @return the client
	 */
	public ArkFolioClient getClientUsing(String address, String consumerKey, String consumerSecret, int timesToRetry) {
		return new DelegatingFolioClient(ManagedProducers.getHttpRetryingClient(client,address,consumerKey,consumerSecret, timesToRetry, new FixedRetryHandler(timesToRetry)));
	}
}
