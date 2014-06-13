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

import java.util.Date;

import com.timeInc.ark.api.push.PushService.IssueKeyPair;

/**
 * A class to send push message to Echo Push server's
 */
public class EchoPush extends DelayedPush<String> {
	private String token;
	
	protected EchoPush() {}

	@Override
	protected boolean delegatePush(String productId, Date scheduleDate, IssueKeyPair pair) {
		return pushService.sendNewsstandPush(token, productId, scheduleDate, pair);
	}

}
