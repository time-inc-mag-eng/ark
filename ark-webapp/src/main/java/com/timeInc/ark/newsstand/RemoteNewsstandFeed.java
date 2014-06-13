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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.uploader.OriginPreviewUploader;
import com.timeInc.mageng.util.date.DateScheduler;
import com.timeInc.mageng.util.exceptions.PrettyException;

/**
 * Uploads a newsstand feed to an origin server.
 */
public class RemoteNewsstandFeed {
	private static final Logger log = Logger.getLogger(RemoteNewsstandFeed.class);
	
	private Integer id;

	private String relativePath;
	private String feedName;
	
	private OriginPreviewUploader uploader;

	private NewsstandFeedFactory nsFeedFactory;
	private NewsstandAccess access;
	private DateScheduler scheduler;

	protected RemoteNewsstandFeed() {
		this.access = NewsstandFeedFactory.getDefaultAccess();
		this.scheduler = NewsstandFeedFactory.getDefaultScheduler();
	}

	/**
	 * Instantiates a new remote newsstand feed.
	 *
	 * @param nsFeedFactory the ns feed factory
	 * @param access the access
	 * @param scheduler the scheduler
	 */
	public RemoteNewsstandFeed(NewsstandFeedFactory nsFeedFactory, NewsstandAccess access, DateScheduler scheduler) {
		this.nsFeedFactory = nsFeedFactory;
		this.scheduler = scheduler;
	}

	/**
	 * Updates the newsstand feed that is hosted on the origin server.
	 * The published date is calculated by invoking the @{link DateScheduler} on the meta's sale date.
	 * Depending on {@link NewsstandAccess} if it returns true then the feed is update.
	 * @param meta the meta
	 * @param description the description
	 * @param coverImageUrl the cover image url
	 * @param workingDir the working dir
	 */
	public void updateFeed(IssueMeta meta, String description, String coverImageUrl, File workingDir) {	
		try {
			NewsstandFeed feed = getNewsstandFeed();

			Date actualDate = scheduler.reschedule(meta.getOnSaleDate());

			if(access.hasAccess(meta, actualDate, feed.getPublishedDates())) {
				log.debug("Updating feed using for referenceId:" + meta.getReferenceId());

				feed = feed.updateFeed(meta.getReferenceId(), description, actualDate, new URL(coverImageUrl));

				File feedFile = new File(workingDir, feedName);
				OutputStream stream = FileUtils.openOutputStream(feedFile);

				try {
					feed.writeTo(stream);
					uploader.uploadPreview(feedFile, relativePath + "/" + feedFile.getName());
				} finally {
					IOUtils.closeQuietly(stream);
				}
			}
		} catch (Exception e) {
			throw new PrettyException("Failed to update newsstand feed", e);
		} 
	} 
	
	/**
	 * Gets the relative path to the server where this 
	 * feed is located.
	 *
	 * @return the relative path
	 */
	public String getRelativePath() {
		return relativePath;
	}

	/**
	 * Sets the ns feed factory.
	 *
	 * @param nsFeedFactory the new ns feed factory
	 */
	public void setNsFeedFactory(NewsstandFeedFactory nsFeedFactory) {
		this.nsFeedFactory = nsFeedFactory;
	}

	private NewsstandFeed getNewsstandFeed() throws IOException {
		String urlStr = uploader.getCdnUrl(relativePath + '/' + feedName);
		URL url = new URL(urlStr); 
		return nsFeedFactory.getNewsstandFeedUsing(url);
	}
}
