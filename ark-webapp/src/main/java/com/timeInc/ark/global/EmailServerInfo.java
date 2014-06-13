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
 * Value class to hold email server information.
 */
public class EmailServerInfo {
	private final String host;
	private final String sender;
	
	
	/**
	 * Instantiates a new email server info.
	 *
	 * @param host the host
	 * @param sender the sender
	 */
	public EmailServerInfo(String host, String sender) {
		super();
		this.host = host;
		this.sender = sender;
	}

	/**
	 * Gets the host.
	 *
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Gets the sender.
	 *
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}
}
