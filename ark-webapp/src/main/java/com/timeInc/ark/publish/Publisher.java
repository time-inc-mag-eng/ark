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

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.mageng.util.misc.Status;

/**
 * Publish an IssueMeta
 */
public interface Publisher {
	enum PublishType { 
		PREVIEW("preview"), CONTENT("content");
	
		private final String shortValue;
		
		private PublishType(String shortValue) { this.shortValue = shortValue; }
		
		@Override public String toString() { return shortValue; }
	};
	
	/**
	 * Publish an IssueMeta
	 * @param meta
	 * @param email the user's email that requested the publish
	 * @param type the publish type.
	 * @return a success Status if the publish was a success
	 */
	Status publish(IssueMeta meta, String email, PublishType type);
}
