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
package com.timeInc.ark.publish;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.timeInc.ark.publish.Publisher.PublishType;
import com.timeInc.ark.util.JMSMsgQueue;
import com.timeInc.dps.producer.enums.Viewer;
import com.timeInc.mageng.util.misc.Status;


/**
 * Sends a publish msg to JMS
 */
public class JMSFolioPublisher implements FolioPublisher {
	private static final Logger log = Logger.getLogger(JMSFolioPublisher.class);

	/** The Constant SUCCESS. */
	public static final String SUCCESS = "success";
	
	private final JMSMsgQueue publisher;
	private final JMSMsgQueue unpublisher;

	/**
	 * Instantiates a new JMS folio publisher.
	 *
	 * @param publisher the publisher
	 * @param unpublisher the unpublisher
	 */
	public JMSFolioPublisher(JMSMsgQueue publisher, JMSMsgQueue unpublisher) {
		this.publisher = publisher;
		this.unpublisher = unpublisher;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.publish.FolioPublisher#publish(java.lang.String, java.lang.String, java.lang.String, com.timeInc.dps.producer.enums.Viewer, java.lang.String, com.timeInc.ark.publish.Publisher.PublishType, boolean, java.lang.String, java.lang.String, java.util.Date)
	 */
	@Override
	public Status publish(String dpsEmail, String dpsPassword, String folioId, Viewer rendition,
			String productId, PublishType publishType, boolean retail, String userEmail, String issueName,
			Date saleDate) {
		
		
			log.debug("Sending publish request email: " + dpsEmail + 
					" folioId:" + folioId + 
					" productId:" + productId  + 
					" retail:" + retail + 
					" userEmail:" + userEmail +
					" issueName:" + issueName + 
					" publishType:" + publishType + 
					" sateDate:" + saleDate + 
					" rendition:" + rendition.getClass());

			Map<String, Object> propMap = new HashMap<String, Object>();
			propMap.put("accountName", dpsEmail);
			
			Map<String, Object> msgMap = new HashMap<String, Object>();
			msgMap.put("email", dpsEmail);
			msgMap.put("password", dpsPassword);
			msgMap.put("folioId", folioId);
			msgMap.put("productId", productId);
			msgMap.put("userEmail",userEmail);
			msgMap.put("type", publishType.toString());
			msgMap.put("retail", retail);
			msgMap.put("private", true);
			msgMap.put("issueName",issueName);
			msgMap.put("saleDate", saleDate.getTime());
			msgMap.put("rendition", rendition.toString());
			
			return mapPublishResponse(publisher.enqueue(propMap, msgMap));
	}
	
	/**
	 * Map publish response.
	 *
	 * @param response the response
	 * @return the status
	 */
	public static Status mapPublishResponse(String response) {
		if(response != null && response.equalsIgnoreCase(SUCCESS)) 
			return new Status("Success. An email containing the final status will be sent.", false);
		else if(response == null) 
			return new Status("Publish request has been queued. An email containing the final status will be sent.", false);
		else {
			return new Status(response, true);
		}
	}
	
	/**
	 * Map unpublish response.
	 *
	 * @param response the response
	 * @return the status
	 */
	public static Status mapUnpublishResponse(String response) {
		if(response != null && response.equalsIgnoreCase(SUCCESS)) 
			return new Status("Unpublishing folio success", false);
		else if(response == null) 
			return new Status("Unpublish failed. Response timeout exceeded.", true);
		else {
			return new Status(response, true);
		}
	}
	

	/* (non-Javadoc)
	 * @see com.timeInc.ark.publish.FolioPublisher#unpublish(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Status unpublish(String email, String password, String folioId, String appName, String productId) {
		log.debug("Sending unpublish request email: " + email + 
				" folioId:" + folioId + 
				" productId:" + productId  + 
				" appName:" + appName);		
		
		
		Map<String, Object> msgMap = new HashMap<String, Object>();
		msgMap.put("email", email);
		msgMap.put("password", password);
		msgMap.put("folioId", folioId);
		msgMap.put("productId", productId);
		msgMap.put("appName", appName);
		
		return mapUnpublishResponse(unpublisher.enqueue(Collections.<String,Object>emptyMap(), msgMap));
	}
}
