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

import java.util.List;

import org.apache.log4j.Logger;

import com.timeInc.ark.dao.PushEventDAO;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.mageng.util.date.DateUtil;

/**
 * An IssueMeta has remote push access if it is not a test issue, the sale date is 
 * not before the current day and no corresponding push event was found.
 */
public class DuplicatePushAccess implements PushAccess {
	private final Logger log = Logger.getLogger(DuplicatePushAccess.class);
	
	private final PushEventDAO pushDao;
	
	/**
	 * Instantiates a new duplicate push access.
	 *
	 * @param pushDao the push dao
	 */
	public DuplicatePushAccess(PushEventDAO pushDao) {
		this.pushDao = pushDao;
	}
	
	/**
	 * Instantiates a new duplicate push access.
	 */
	public DuplicatePushAccess() {
		this(new PushEventDAO());
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.api.push.PushAccess#hasAccess(com.timeInc.ark.issue.IssueMeta, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean hasAccess(IssueMeta meta, String productId, String pushId) {
		if(meta.getOnSaleDate().compareTo(DateUtil.presentDay()) < 0 || meta.isTestIssue()) {
			log.debug("Issue Meta Id:" + meta.getId() + " has no push access since it is either a test issue or the sale date is before today");
			return false;
		} else {
			List<PushEvent> events = pushDao.getSuccessfulPush(productId, meta.getReferenceId(), pushId);
			
			if(events.isEmpty()) {
				return true;
			} else {
				log.debug("Push event found not scheduling push msg");
				return false;
			}
		}
	}
}
