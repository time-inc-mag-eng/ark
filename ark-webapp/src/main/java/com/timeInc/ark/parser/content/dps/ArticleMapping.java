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

import java.io.File;

import com.timeInc.ark.issue.Article;
import com.timeInc.folio.parser.article.sidecar.ArticleAccess;

/**
 * Value class that maps an {@link Article} that is not yet associated with a Folio
 * with the actual article file.
 */
public class ArticleMapping {
	private final Article article;
	private final File file;
	private final ArticleAccess access;
	
	
	/**
	 * Instantiates a new article mapping.
	 *
	 * @param article the article
	 * @param file the file
	 * @param access the access
	 */
	public ArticleMapping(Article article, File file, ArticleAccess access) {
		this.article = article;
		this.file = file;
		this.access = access;
	}

	/**
	 * Gets the access.
	 *
	 * @return the access
	 */
	public ArticleAccess getAccess() {
		return access;
	}

	/**
	 * Gets the article.
	 *
	 * @return the article
	 */
	public Article getArticle() {
		return article;
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
}
