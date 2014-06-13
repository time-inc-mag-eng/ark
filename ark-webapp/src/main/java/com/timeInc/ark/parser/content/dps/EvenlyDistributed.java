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
 * Provides an even distribution of the step size given the number of articles.
 * i.e. if there were 5 articles and the range is [0, 10] then the step size will be 2
 */
public class EvenlyDistributed implements SortOrderStrategy {
	// start and end inclusive
	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.content.dps.SortOrderStrategy#getStepSize(int, int, int)
	 */
	public int getStepSize(int start, int end, int numElements) {
		if(start < START_RANGE || start > END_RANGE 
				|| end < START_RANGE || end > END_RANGE || start > end)
			throw new IllegalArgumentException("Not a valid start and end -- they must be within range");
		
		if(numElements < 1) 
			throw new IllegalArgumentException("Number of Elements must be greater than 0");
		
		long range = ((long)end) - start + 1;
	
		int stepSize = (int) (range / (numElements+1));

		if(stepSize == 0)
			throw new IllegalArgumentException("Can not find a valid step size");
		else
			return stepSize;
	}
	
}

