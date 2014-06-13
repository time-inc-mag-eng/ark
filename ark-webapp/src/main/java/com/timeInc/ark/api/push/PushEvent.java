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
package com.timeInc.ark.api.push;

import java.io.Serializable;
import java.util.Date;

/**
 * An event that indicates a remote newsstand push has been successfully sent.
 */
public class PushEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String productId;
	private Date pushScheduledDate;
	private String pushId;
	private String referenceId;
	
	PushEvent() {}

	/**
	 * Instantiates a new push event.
	 *
	 * @param productId the product id
	 * @param pushScheduledDate the push scheduled date
	 * @param pushId the push id
	 * @param referenceId the reference id
	 */
	public PushEvent(String productId, Date pushScheduledDate,
			String pushId, String referenceId) {
		this.productId = productId;
		this.pushScheduledDate = pushScheduledDate;
		this.pushId = pushId;
		this.referenceId = referenceId;
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
	 * Gets the push scheduled date.
	 *
	 * @return the push scheduled date
	 */
	public Date getPushScheduledDate() {
		return pushScheduledDate;
	}

	/**
	 * Gets the push id.
	 *
	 * @return the push id
	 */
	public String getPushId() {
		return pushId;
	}

	/**
	 * Gets the reference id.
	 *
	 * @return the reference id
	 */
	public String getReferenceId() {
		return referenceId;
	}
}
