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
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.timeInc.ark.api.push.PushService.IssueKeyPair;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.role.User;
import com.timeInc.mageng.util.date.DateScheduler;
import com.timeInc.mageng.util.exceptions.PrettyException;

/**
 * Schedules a remote push download request with an offset in minutes relative 
 * to the Issuemeta's sale date
 *
 * @param <T> the generic type
 */
public abstract class DelayedPush<T> {
	private static final Logger log = Logger.getLogger(DelayedPush.class);
	
	private Integer id;

	private String productId;

	private String surrogateKey;
	
	private int delayMinute; 
	

	private DateScheduler scheduler;
	private PushAccess pushAccess;

	protected PushService<T> pushService;
	
	private PushEventListener listener;	

	protected DelayedPush() {}

	/**
	 * Instantiates a new delayed push.
	 *
	 * @param productId the product id
	 * @param surrogateKey the surrogate key
	 * @param delayMinute the delay minute
	 * @param scheduler the scheduler
	 * @param pushAccess the push access
	 * @param pushService the push service
	 * @param listener the listener
	 */
	public DelayedPush(String productId, String surrogateKey, int delayMinute,
			DateScheduler scheduler, PushAccess pushAccess,
			PushService<T> pushService, PushEventListener listener) {
		super();
		this.productId = productId;
		this.surrogateKey = surrogateKey;
		this.delayMinute = delayMinute;
		this.scheduler = scheduler;
		this.pushAccess = pushAccess;
		this.pushService = pushService;
		this.listener = listener;
	}
	

	/**
	 * Send a newsstand push if the IssueMeta has
	 * access. The push scheduled date is calculated by 
	 * first invoking the {@link DateScheduler#reschedule(Date)} and provides 
	 * the meta's sale date as the parameter followed by an offset in minutes to that 
	 * date. If the push was sent successfully invoke {@link PushEventListener#pushSent(Date, Date, String, String, IssueMeta, User)}
	 * @param user the user
	 * @param meta the meta
	 * @param keyPair the key pair
	 * @throws PrettyException if {@link #delegatePush(String, Date, PushService.IssueKeyPair)} returns false
	 */
	public void sendNewsstandPush(User user, IssueMeta meta, IssueKeyPair keyPair) {
		if(pushAccess.hasAccess(meta, productId, surrogateKey)) {
			
			log.debug("Sending remote push request for refid:" + meta.getReferenceId());
			
			Date schedulerDate = scheduler.reschedule(meta.getOnSaleDate());
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(schedulerDate);
			cal.add(Calendar.MINUTE, delayMinute);
			Date pushDate = cal.getTime();
			
			Date sendDate = new Date();
			
			boolean res = delegatePush(productId, pushDate, keyPair);
			
			if(!res) 
				throw new PrettyException("Failed to schedule a newsstand push");
			else 
				listener.pushSent(pushDate, sendDate, surrogateKey, productId, meta, user);
		} 
	}
	
	/**
	 * Send a remote download push request
	 * @param productId
	 * @param scheduleDate
	 * @param pair
	 * @return true if the request was a success
	 */
	protected abstract boolean delegatePush(String productId, Date scheduleDate, IssueKeyPair pair);
	
	/**
	 * Sets the scheduler.
	 *
	 * @param scheduler the new scheduler
	 */
	public void setScheduler(DateScheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * Sets the push access.
	 *
	 * @param pushAccess the new push access
	 */
	public void setPushAccess(PushAccess pushAccess) {
		this.pushAccess = pushAccess;
	}

	/**
	 * Sets the listener.
	 *
	 * @param listener the new listener
	 */
	public void setListener(PushEventListener listener) {
		this.listener = listener;
	}

	/**
	 * Sets the push service.
	 *
	 * @param pushService the new push service
	 */
	public void setPushService(PushService<T> pushService) {
		this.pushService = pushService;
	}
}
