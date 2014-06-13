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
package com.timeInc.ark.newsstand;

import java.util.Date;
import java.util.List;

import com.timeInc.ark.issue.IssueMeta;

/**
 * Determines whether a newsstand atom feed should be generated for an {@link IssueMeta}
 * 
 * See https://itunesconnect.apple.com/docs/NewsstandAtomFeedSpecification.pdf for more information.
 * 
 */
public interface NewsstandAccess {
	
	/**
	 * Determines whether a newsstand atom feed should be generated for an {@link IssueMeta}
	 * @param meta
	 * @param scheduledDate the published date
	 * @param feedPublishedDates the already existing published dates in the feed
	 * @return true if a newsstand atom feed should be generateds
	 */
	boolean hasAccess(IssueMeta meta, Date scheduledDate, List<Date> feedPublishedDates);
}
