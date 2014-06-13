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
package com.timeInc.ark.parser.content.dps;

/**
 * A way of assigning sort order for {@link com.timeInc.ark.issue.Article} when
 * it is being uploaded to Adobe DPS
 */
public interface SortOrderStrategy {
	static int START_RANGE = 0; // inclusive
	static int END_RANGE = 2147483647;
	
	
	/**
	 * Gets the step size for a range of articles
	 * @param start the start sort order inclusive
	 * @param end the end of sort order inclusive
	 * @param numElements the number of articles
	 * @return the step size 
	 */
	int getStepSize(int start, int end, int numElements);
}
