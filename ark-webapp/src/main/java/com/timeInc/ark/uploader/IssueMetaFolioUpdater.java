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
package com.timeInc.ark.uploader;

import com.timeInc.ark.issue.Article;
import com.timeInc.ark.issue.Folio;

/**
 * Updates the state of passed in parameters.
 */
public class IssueMetaFolioUpdater implements DpsServerListener {

	/* (non-Javadoc)
	 * @see com.timeInc.ark.uploader.DpsServerListener#articleRemoved(com.timeInc.ark.issue.Article)
	 */
	@Override
	public void articleRemoved(Article article) {
		article.setArticleId(null);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.uploader.DpsServerListener#articleUpdated(com.timeInc.ark.issue.Article, java.lang.String)
	 */
	@Override
	public void articleUpdated(Article article, String articleId) {
		article.setArticleId(articleId);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.uploader.DpsServerListener#htmlResourceAdded(com.timeInc.ark.issue.Folio)
	 */
	@Override
	public void htmlResourceAdded(Folio folio) {
		folio.setContainsHtml(true);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.uploader.DpsServerListener#htmlResourceRemoved(com.timeInc.ark.issue.Folio)
	 */
	@Override
	public void htmlResourceRemoved(Folio folio) {
		folio.setContainsHtml(false);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.uploader.DpsServerListener#articlesRemoved(com.timeInc.ark.issue.Folio)
	 */
	@Override
	public void articlesRemoved(Folio folio) {
		folio.removeAllArticles();
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.uploader.DpsServerListener#folioCreated(com.timeInc.ark.issue.Folio, java.lang.String)
	 */
	@Override
	public void folioCreated(Folio folio, String folioId) {
		folio.setFolioId(folioId);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.uploader.DpsServerListener#articleAdded(com.timeInc.ark.issue.Folio, com.timeInc.ark.issue.Article)
	 */
	@Override
	public void articleAdded(Folio folio, Article article) {
		folio.addArticle(article);
	}
}
