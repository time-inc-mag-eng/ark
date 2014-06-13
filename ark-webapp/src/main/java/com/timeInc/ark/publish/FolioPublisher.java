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

import java.util.Date;

import com.timeInc.ark.publish.Publisher.PublishType;
import com.timeInc.dps.producer.enums.Viewer;
import com.timeInc.mageng.util.misc.Status;

/**
 * Publishes a folio to DPS.
 */
public interface FolioPublisher {
	/**
	 * 
	 * Publishes a folio associated with the dps account.
	 * @param email 
	 * @param dpsPassword 
	 * @param folioId 
	 * @param rendition 
	 * @param productId 
	 * @param publishType 
	 * @param retail
	 * @param userEmail
	 * @param issueName
	 * @param saleDate
	 * @return a Success status if the publish was success
	 */
	Status publish(String email, String dpsPassword, String folioId, Viewer rendition,
			String productId, PublishType publishType, boolean retail, String userEmail, String issueName, Date saleDate);
	
	/**
	 * Unpublish a folio associated with the dps account
	 * @param email
	 * @param password
	 * @param folioId
	 * @param appName
	 * @param productId
	 * @return a Success status if the unpublish was success
	 */
	Status unpublish(String email, String password, String folioId, String appName, String productId);
}
