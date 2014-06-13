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
package com.timeInc.ark.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.timeInc.ark.api.push.PushEvent;


/**
 * DAO for {@link PushEvent}
 */
public class PushEventDAO extends HibernateGenericDAO<PushEvent, Integer> {
	
	/**
	 * Gets the successful remote push events
	 *
	 * @param echoProductId the echo product id
	 * @param metaReferenceId the meta reference id
	 * @param pushid the pushid
	 * @return the successful push
	 */
	public List<PushEvent> getSuccessfulPush(String echoProductId, String metaReferenceId, String pushid) {
		return findByCriteria(Restrictions.eq("pushId", pushid),
				Restrictions.eq("productId", echoProductId),
				Restrictions.eq("referenceId", metaReferenceId));
	}
}
