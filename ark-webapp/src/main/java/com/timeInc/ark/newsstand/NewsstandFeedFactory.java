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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import com.timeInc.mageng.util.date.DateScheduler;

/**
 * A factory for dependencies on {@link RemoteNewsstandFeed} 
 */
public class NewsstandFeedFactory {
	private static final DateScheduler DATE_SCHEDULER = new FiveMinuteAfterMidnight();
	private static final NewsstandAccess DEFAULT_ACCESS = new GreaterThanFeed();

	/**
	 * Gets the newsstand feed using.
	 *
	 * @param url the url
	 * @return the newsstand feed using
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public NewsstandFeed getNewsstandFeedUsing(URL url) throws IOException {
		if(urlExist(url)) {
			InputStream urlStream = url.openStream();

			try {
				return new NewsstandAtomFeed(urlStream);
			} finally {
				IOUtils.closeQuietly(urlStream);
			}
		} else 
			return new NewsstandAtomFeed();		
	}

	/**
	 * Url exist.
	 *
	 * @param url the url
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean urlExist(URL url) throws IOException {
		HttpURLConnection huc =  (HttpURLConnection)  url.openConnection();
		
		try {
			huc.setRequestMethod("HEAD");
			return (huc.getResponseCode() == HttpURLConnection.HTTP_OK);
		} finally {
			if(huc != null)
				IOUtils.close(huc);
		}
	}


	static NewsstandAccess getDefaultAccess() {
		return DEFAULT_ACCESS;
	}

	static DateScheduler getDefaultScheduler() {
		return DATE_SCHEDULER;
	}

	private static class FiveMinuteAfterMidnight implements DateScheduler {
		@Override
		public Date reschedule(Date originalDate) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(originalDate);

			if(cal.get(Calendar.HOUR_OF_DAY) == 0 && cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) == 0) { 
				cal.add(Calendar.MINUTE,5);
				return cal.getTime();
			}
			else
				return originalDate;
		}
	}
}
