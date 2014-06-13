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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.timeInc.ark.application.ApplicationEntity.ApplicationVisitor;
import com.timeInc.ark.application.CdsApplication;
import com.timeInc.ark.application.DpsApplication;
import com.timeInc.ark.application.ScpApplication;
import com.timeInc.ark.issue.Article;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.dps.producer.response.ArticleInfo;
import com.timeInc.mageng.util.string.StringUtil;

/**
 * Sync a {@link com.timeInc.ark.issue.Folio} articles articleId using Adobe DPS if the articleId is empty but Adobe DPS has an articleId.
 * Thread-safe since there is no mutable state. 
 */
public class PreExistingFolioSync implements IssueMetaSync {
	private static final Logger log = Logger.getLogger(PreExistingFolioSync.class);
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.content.dps.IssueMetaSync#synch(com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public void synch(IssueMeta meta) {
		if(meta.getFolio() != null && !StringUtil.isEmpty(meta.getFolio().getFolioId())) {
			Map<Integer, ArticleInfo> remoteIssue = getRemoteIssue(meta);

			for(Article article : meta.getFolio().getOrderedArticles()) {
				if(StringUtil.isEmpty(article.getArticleId())) {
					ArticleInfo info = remoteIssue.get(article.getSortOrder());
					if(info != null) {
						log.debug("Synching article dossierid:" + article.getDossierId() + " with articleid:" + info.getId());
						article.setArticleId(info.getId());
					}
				}
			}
		}
	}
	
	
	private static final Map<Integer, ArticleInfo> getRemoteIssue(final IssueMeta meta) {
		List<ArticleInfo> remoteList = meta.getApplication().accept(new ApplicationVisitor<List<ArticleInfo>>() {

			@Override
			public List<ArticleInfo> visit(CdsApplication app) {
				return null;
			}

			@Override
			public List<ArticleInfo> visit(DpsApplication app) {
				return app.getRemoteArticlesFor(meta);
			}

			@Override
			public List<ArticleInfo> visit(ScpApplication app) {
				return null;
			}}
				);

		Map<Integer,ArticleInfo> sortOrderMap = new HashMap<Integer,ArticleInfo>(remoteList.size());

		for(ArticleInfo info : remoteList) {
			sortOrderMap.put(info.getArticleMetadata().getSortNumber(), info);
		}

		return Collections.unmodifiableMap(sortOrderMap);
	}

}
