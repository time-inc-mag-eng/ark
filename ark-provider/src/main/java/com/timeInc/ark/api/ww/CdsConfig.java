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
package com.timeInc.ark.api.ww;

import java.util.Date;

/**
 * Configuration class that is used when making a request to Woodwing's CDP/Kiosk server
 */
public class CdsConfig {
	
	/**
	 * Authentication config for an application in CDP.
	 */
	public static class Auth {
		private String minVersion;
		private String publishKey;
		private String issueKey;
		private String productKey;
		private boolean subscription;
		
		
		Auth() {}

		/**
		 * Instantiates a new auth.
		 *
		 * @param minVersion the min version
		 * @param publishKey the publish key
		 * @param issueKey the issue key
		 * @param productKey the product key
		 * @param subscription the subscription
		 */
		public Auth(String minVersion, String publishKey, String issueKey,
				String productKey, boolean subscription) {
			this.minVersion = minVersion;
			this.publishKey = publishKey;
			this.issueKey = issueKey;
			this.productKey = productKey;
			this.subscription = subscription;
		}
		
		/**
		 * Gets the min version.
		 *
		 * @return the min version
		 */
		public String getMinVersion() {
			return minVersion;
		}

		/**
		 * Gets the publish key.
		 *
		 * @return the publish key
		 */
		public String getPublishKey() {
			return publishKey;
		}


		/**
		 * Gets the issue key.
		 *
		 * @return the issue key
		 */
		public String getIssueKey() {
			return issueKey;
		}


		/**
		 * Gets the product key.
		 *
		 * @return the product key
		 */
		public String getProductKey() {
			return productKey;
		}

		/**
		 * Checks if is subscription.
		 *
		 * @return true, if is subscription
		 */
		public boolean isSubscription() {
			return subscription;
		}
	}
	
	/**
	 * Value class to aggregate the location of the require preview images.
	 */
	public static class PreviewLocation {
		
		/** The vertical cover. */
		public final String verticalCover;
		
		/** The horizontal cover. */
		public final String horizontalCover;
		
		/** The small cover. */
		public final String smallCover;
		
		/** The large cover. */
		public final String largeCover;

		/**
		 * Instantiates a new preview location.
		 *
		 * @param verticalCover the vertical cover
		 * @param horizontalCover the horizontal cover
		 * @param smallCover the small cover
		 * @param largeCover the large cover
		 */
		public PreviewLocation(String verticalCover, String horizontalCover,
				String smallCover, String largeCover) {
			this.verticalCover = verticalCover;
			this.horizontalCover = horizontalCover;
			this.smallCover = smallCover;
			this.largeCover = largeCover;
		}
	}
	
	/**
	 * Value class that contains the content and the protected directory for CDN
	 */
	public static class ContentLocation {
		
		/** The content url. */
		public final String contentUrl;
		
		/** The protected dir. */
		public final String protectedDir;
		
		/**
		 * Instantiates a new content location.
		 *
		 * @param contentUrl the content url
		 * @param protectedDir the protected dir
		 */
		public ContentLocation(String contentUrl, String protectedDir) {
			this.contentUrl = contentUrl;
			this.protectedDir = protectedDir;
		} 
	}
	
	/**
	 * Represents an issue's meta data that is needed to create an issue on Kiosk
	 */
	public static class MetaData {
		
		/** The sale date. */
		public final Date saleDate;
		
		/** The payment. */
		public final String payment;
		
		/** The issue name. */
		public final String issueName;
		
		/** The volume. */
		public final String volume;
		
		/** The reference id. */
		public final String referenceId;
		
		/** The cover story. */
		public final String coverStory;
		
		/**
		 * Instantiates a new meta data.
		 *
		 * @param saleDate the sale date
		 * @param payment the payment
		 * @param issueName the issue name
		 * @param volume the volume
		 * @param referenceId the reference id
		 * @param coverStory the cover story
		 */
		public MetaData(Date saleDate, String payment, String issueName,
				String volume, String referenceId, String coverStory) {
			this.saleDate = saleDate;
			this.payment = payment;
			this.issueName = issueName;
			this.volume = volume;
			this.referenceId = referenceId;
			this.coverStory = coverStory;
		}
	}
	
	/**
	 * Represents the connection configuration for kiosk and cdp
	 */
	public static class Connection {
		
		/** The kiosk server. */
		public final String kioskServer;
		
		/** The cdp device id. */
		public final String cdpDeviceId;
		
		/** The cdp server. */
		public final String cdpServer;
		
		/** The cdp user name. */
		public final String cdpUserName;
		
		/** The cdp password. */
		public final String cdpPassword;
		
		/**
		 * Instantiates a new connection.
		 *
		 * @param kioskServer the kiosk server
		 * @param cdpDeviceId the cdp device id
		 * @param cdpServer the cdp server
		 * @param cdpUserName the cdp user name
		 * @param cdpPassword the cdp password
		 */
		public Connection(String kioskServer, String cdpDeviceId,
				String cdpServer, String cdpUserName, String cdpPassword) {
			this.kioskServer = kioskServer;
			this.cdpDeviceId = cdpDeviceId;
			this.cdpServer = cdpServer;
			this.cdpUserName = cdpUserName;
			this.cdpPassword = cdpPassword;
		}
	}
}

