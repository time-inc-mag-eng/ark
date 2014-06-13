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

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Generates a newsstand feed
 */
public interface NewsstandFeed {
	/**
	 * Updates the feed by adding or replacing the feed entry with id
	 * to the specified parameters.
	 * @param id the id of the entry, if it does not exist in the feed then it is added; otherwise it is replaced
	 * @param description the feed's summary for this id
	 * @param publishedDate the date this entry should be active
	 * @param cover a link to the cover
	 * @return the updated feed
	 */
	NewsstandFeed updateFeed(String id, String description, Date publishedDate, URL cover);
	
	/**
	 * Write the underlying feed to an OutputStream
	 * @param out 
	 * @throws IOException
	 */
	void writeTo(OutputStream out) throws IOException;
	
	/**
	 * Gets a list of already existing entries' published dates
	 * @return an empty list if no entry exists
	 */
	List<Date> getPublishedDates();
}
