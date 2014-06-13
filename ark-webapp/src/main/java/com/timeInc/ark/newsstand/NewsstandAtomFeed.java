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
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.ExtensibleElement;
import org.apache.abdera.model.Feed;
import org.apache.log4j.Logger;

import com.timeInc.mageng.util.misc.Precondition;

/**
 *  Generates an atom feed according to the specification at 
 *  https://itunesconnect.apple.com/docs/NewsstandAtomFeedSpecification.pdf
 */
public class NewsstandAtomFeed implements NewsstandFeed {
	private static final Logger log = Logger.getLogger(NewsstandAtomFeed.class);
	
	/** The Constant NEWS_STAND_NS. */
	public static final String NEWS_STAND_NS = "http://itunes.apple.com/2011/Newsstand";
	
	/** The Constant NEWS_STAND_PREFIX. */
	public static final String NEWS_STAND_PREFIX = "news";
	
	/** The Constant COVER_ART_SIZE_PROPERTY. */
	public static final String COVER_ART_SIZE_PROPERTY = "size";
	
	/** The Constant COVER_ART_SOURCE_VALUE. */
	public static final String COVER_ART_SOURCE_VALUE = "SOURCE";
	
	/** The Constant COVER_ART_SOURCE_LINK. */
	public static final String COVER_ART_SOURCE_LINK = "src";
	
	/** The Constant COVER_ART_ICONS. */
	public static final QName COVER_ART_ICONS = new QName(NEWS_STAND_NS,"cover_art_icons"); 
	
	/** The Constant COVER_ART_ICON. */
	public static final QName COVER_ART_ICON = new QName(NEWS_STAND_NS,"cover_art_icon");
	

	private final Feed feed;

	/**
	 * Instantiates a new newsstand atom feed.
	 *
	 * @param feedInput the feed input
	 */
	public NewsstandAtomFeed(InputStream feedInput)  {
		this((Feed)Abdera.getNewParser().parse(feedInput).getRoot());
	}
	
	/**
	 * Instantiates a new newsstand atom feed.
	 *
	 * @param feed the feed
	 */
	public NewsstandAtomFeed(Feed feed)  {
		Precondition.checkNull(feed,"feed");
		this.feed = feed;
	}
	
	/**
	 * Instantiates a new newsstand atom feed.
	 */
	public NewsstandAtomFeed() {
		this(getNewFeed());	
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.newsstand.NewsstandFeed#writeTo(java.io.OutputStream)
	 */
	public void writeTo(OutputStream out) throws IOException {
		feed.getDocument().writeTo(out);
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.newsstand.NewsstandFeed#getPublishedDates()
	 */
	@Override
	public List<Date> getPublishedDates() {
		List<Date> pubDates = new ArrayList<Date>();
		
		for(Entry ent : feed.getEntries()) { // find entry if any; 
			pubDates.add(ent.getPublished());
		}
		
		return Collections.unmodifiableList(pubDates);
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.newsstand.NewsstandFeed#updateFeed(java.lang.String, java.lang.String, java.util.Date, java.net.URL)
	 */
	@Override 
	public NewsstandFeed updateFeed(String id, String description, Date publishedDate, URL cover)  {
		log.debug("Updating feed with info id:" + id + " description:" + description + " publish date: " + publishedDate + " cover url:" + cover);
		
		Feed newFeed = getNewFeed();
		createEntry(newFeed,id,description, publishedDate, cover);
		
		return new NewsstandAtomFeed(newFeed);
	}
	
	private static Feed getNewFeed() {
		Factory factory = Abdera.getNewFactory();
		Feed feed = factory.newFeed();			
		feed.declareNS(NEWS_STAND_NS, NEWS_STAND_PREFIX);
		feed.setUpdated(new Date());
		
		return feed;
	}
	
	private static void createEntry(Feed feed, String id, String description, Date publishedDate, URL cover) {
		Entry ent = feed.addEntry();
		ent.setId(id,true);
		updateEntry(ent,description,publishedDate,cover);
	}
	
	private static void updateEntry(Entry entry, String description, Date publishedDate, URL cover) {
		entry.setUpdated(new Date());
		entry.setPublished(publishedDate);
		entry.setSummary(description);

		
		ExtensibleElement coverArts = entry.getExtension(COVER_ART_ICONS);
		
		if(coverArts == null)  // new entry insert cover art element
			coverArts = entry.addExtension(COVER_ART_ICONS);

		Element coverArt = coverArts.getExtension(COVER_ART_ICON);
		
		if(coverArt == null) 
			coverArt = coverArts.addExtension(COVER_ART_ICON);
		
		
		coverArt.setAttributeValue(COVER_ART_SIZE_PROPERTY,COVER_ART_SOURCE_VALUE);
		coverArt.setAttributeValue(COVER_ART_SOURCE_LINK, cover.toExternalForm());
	}
}
