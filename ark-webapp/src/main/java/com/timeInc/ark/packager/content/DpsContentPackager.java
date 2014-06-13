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
package com.timeInc.ark.packager.content;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.packager.Packager;
import com.timeInc.ark.parser.content.dps.ArticleMapping;
import com.timeInc.ark.parser.content.dps.FolioInfo;
import com.timeInc.folio.parser.HtmlResourcePackager;
import com.timeInc.folio.parser.article.ArticlePackager;
import com.timeInc.folio.parser.util.ZipUtil;

/**
 * A packager for folio content that gets uploaded to Adobe DPS
 */
public class DpsContentPackager implements Packager<DpsContentInfo, FolioInfo> {
	private final HtmlResourcePackager htmlPackager;
	private final ArticlePackager articlePackager;
	
	/**
	 * Instantiates a new dps content packager.
	 *
	 * @param htmlPackager the html packager
	 * @param articlePackager the article packager
	 */
	public DpsContentPackager(HtmlResourcePackager htmlPackager, ArticlePackager articlePackager) {
		this.htmlPackager = htmlPackager;
		this.articlePackager = articlePackager;
	}
	
	/**
	 * Instantiates a new dps content packager.
	 */
	public DpsContentPackager() {
		this(new HtmlResourcePackager(new ZipUtil()), new ArticlePackager(new ZipUtil()));
	}
	
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.packager.Packager#pack(java.lang.Object, java.io.File, com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public DpsContentInfo pack(FolioInfo parsedData, File outputDirectory, IssueMeta meta) {
		File htmlResource = null;
		
		if(parsedData.isContainsHtmlResources()) {
			htmlResource = htmlPackager.pack(parsedData.getHtmlResource(),outputDirectory);
		}
		
		List<ArticleMapping> packedArticles = getPackedArticleMapping(parsedData.getArticleMapping(), outputDirectory);
		
		return new DpsContentInfo(packedArticles, htmlResource, parsedData);
	}
	
	private List<ArticleMapping> getPackedArticleMapping(List<ArticleMapping> unpackedArticle, File outputDirectory) {
		List<ArticleMapping> packedList = new ArrayList<ArticleMapping>(unpackedArticle.size());
		
		for(ArticleMapping mapping : unpackedArticle) {
			File packedArticle = articlePackager.pack(mapping.getFile(),outputDirectory);
			packedList.add(new ArticleMapping(mapping.getArticle(), packedArticle, mapping.getAccess()));
		}
		
		return packedList;
	}
}
