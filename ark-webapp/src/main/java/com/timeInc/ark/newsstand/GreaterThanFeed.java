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

import org.apache.log4j.Logger;

import com.timeInc.ark.issue.IssueMeta;

/**
 * Determines if an {@link IssueMeta} should generate an atom feed entry if
 * the following conditions are all met
 * 1) the IssueMeta is not a test 
 * 2) the scheduledDate is greater than all of the already existing feedPublishedDates
 *
 */
public class GreaterThanFeed implements NewsstandAccess {
	private static Logger log = Logger.getLogger(GreaterThanFeed.class);
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.newsstand.NewsstandAccess#hasAccess(com.timeInc.ark.issue.IssueMeta, java.util.Date, java.util.List)
	 */
	@Override
	public boolean hasAccess(IssueMeta meta, Date scheduledDate, List<Date> feedPublishedDates) {
		if(meta.isTestIssue()) {
			log.debug("No access since meta id: " + meta.getId() + " is a test issue");
			return false;
		}

		for(Date pubDate : feedPublishedDates)  {
			if(scheduledDate.compareTo(pubDate) < 0) {
				log.debug("No access since meta id: " + meta.getId() + " with date:" + scheduledDate + " is before a publish date of " + pubDate);
				return false;
			}
		}
		
		return true;
	}
}
