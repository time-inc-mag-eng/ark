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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.timeInc.ark.issue.Article;
import com.timeInc.ark.issue.Resolution;
import com.timeInc.ark.parser.content.dps.FolioInfo.FolioComparisonInfo.Action;
import com.timeInc.dps.producer.enums.FolioIntent;

/**
 * Value class that holds the parsed information for a Folio 
 */
public class FolioInfo {
	private final String targetViewer; 
	private final Resolution resolution;
	private final FolioIntent orientation;
	private final File htmlResource;
	private final Action uploadAction;
	private final List<ArticleMapping> mapping;
	

	/**
	 * Instantiates a new folio info.
	 *
	 * @param mapping the mapping
	 * @param uploadAction the upload action
	 * @param htmlResource the html resource
	 * @param targetViewer the target viewer
	 * @param resolution the resolution
	 * @param orientation the orientation
	 */
	public FolioInfo(List<ArticleMapping> mapping, Action uploadAction,
			File htmlResource, String targetViewer,
			Resolution resolution, FolioIntent orientation) {
		
		this.mapping = mapping;
		
		this.uploadAction = uploadAction;

		this.htmlResource = htmlResource;
		this.targetViewer = targetViewer;
		this.resolution = resolution;
		this.orientation = orientation;
	}

	/**
	 * Gets the upload action.
	 *
	 * @return the upload action
	 */
	public Action getUploadAction() {
		return uploadAction;
	}
	
	/**
	 * Gets the article mapping.
	 *
	 * @return the article mapping
	 */
	public List<ArticleMapping> getArticleMapping() {
		return mapping;
	}

	/**
	 * Checks if is contains html resources.
	 *
	 * @return true, if is contains html resources
	 */
	public boolean isContainsHtmlResources() {
		return htmlResource != null;
	}
	
	/**
	 * Gets the html resource.
	 *
	 * @return the html resource
	 */
	public File getHtmlResource() {
		return htmlResource;
	}

	/**
	 * Gets the target viewer.
	 *
	 * @return the target viewer
	 */
	public String getTargetViewer() {
		return targetViewer;
	}

	/**
	 * Gets the resolution.
	 *
	 * @return the resolution
	 */
	public Resolution getResolution() {
		return resolution;
	}

	/**
	 * Gets the orientation.
	 *
	 * @return the orientation
	 */
	public FolioIntent getOrientation() {
		return orientation;
	}
	

	/**
	 * The Class FolioComparisonInfo.
	 */
	public static class FolioComparisonInfo {
		
		/**
		 * The Enum Action.
		 */
		public enum Action { UPDATE, ADD, UNFINISHED, NEW }
		
		private final List<Article> articles = new ArrayList<Article>();
		
		private final Action action;
		
		/**
		 * Instantiates a new folio comparison info.
		 *
		 * @param uploadAction the upload action
		 */
		public FolioComparisonInfo(Action uploadAction) {
			this.action = uploadAction;
		}
		
		/**
		 * Adds the article.
		 *
		 * @param article the article
		 */
		public void addArticle(Article article) {
			articles.add(article);
		}

		/**
		 * Gets the action.
		 *
		 * @return the action
		 */
		public Action getAction() {
			return action;
		}

		/**
		 * Gets the articles.
		 *
		 * @return the articles
		 */
		public List<Article> getArticles() {
			return Collections.unmodifiableList(articles);
		}
	}
}
