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
package com.timeInc.ark.issue;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.timeInc.dps.producer.enums.FolioIntent;

/**
 * A folio that an IssueMeta contains.
 */
public class Folio implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private FolioIntent orientation;
	private String folioId;
	private Resolution resolution;
	private boolean containsHtml;
	
	private IssueMeta meta;
	
	private Set<Article> articles = new HashSet<Article>();
	
	private ArticleSetBackedList articleBackedList = new ArticleSetBackedList();
	
	protected Folio() {}

	/**
	 * Instantiates a new folio.
	 *
	 * @param orientation the orientation
	 * @param folioId the folio id
	 * @param resolution the resolution
	 * @param containsHtml the contains html
	 */
	public Folio(FolioIntent orientation, String folioId, Resolution resolution, boolean containsHtml)  {
		super();
		this.orientation = orientation;
		this.folioId = folioId;
		this.resolution = resolution;
	}
	
	/**
	 * Instantiates a new folio.
	 *
	 * @param orientation the orientation
	 * @param resolution the resolution
	 * @param containsHtml the contains html
	 */
	public Folio(FolioIntent orientation, Resolution resolution, boolean containsHtml)  {
		this(orientation, null, resolution, containsHtml);
	}
	
	/**
	 * Removes the all articles.
	 */
	public void removeAllArticles() {
		articleBackedList.clear();
	}
	
	/**
	 * Adds the article.
	 *
	 * @param article the article
	 */
	public void addArticle(Article article) {
		article.setFolio(this);
		articleBackedList.add(article);
	}
	
	/**
	 * Gets the ordered articles.
	 *
	 * @return the ordered articles
	 */
	public List<Article> getOrderedArticles() {
		return Collections.unmodifiableList(articleBackedList.getOrderedArticle());
	}

	/**
	 * Gets the article by dossier id.
	 *
	 * @param dossierId the dossier id
	 * @return the article
	 */
	public Article getArticle(String dossierId) {
		Article articleToGet = new Article(dossierId);
		
		for(Article article : articleBackedList) {
			if(article.equals(articleToGet))
				return article;
		}
		
		return null;
	}
	
	/**
	 * Gets the position relative to other 
	 * dossier ids.
	 *
	 * @param dossierId the dossier id
	 * @return the -1 if the dossier does not exist in the folio yet, a zero indexed position otherwise
	 */
	public int getPositionFor(String dossierId) {
		Article articleToCheck = new Article(dossierId); 
		
		int index = 0;
		for(Article article : articleBackedList.getOrderedArticle()) {
			if(article.equals(articleToCheck))
				return index;
			else
				index++;
		}
		
		return -1;
	}
	
	/**
	 * Removes the article.
	 *
	 * @param a the a
	 */
	public void removeArticle(Article a) {
		articleBackedList.remove(a);
	}
	
	
	/**
	 * Gets the articles with no id.
	 *
	 * @return the articles with no id
	 */
	public List<Article> getArticlesWithNoId() {
		List<Article> noIdList = new ArrayList<Article>();
		
		for(Article article : articleBackedList) {
			if(article.getArticleId() == null)
				noIdList.add(article);
		}
		
		return Collections.unmodifiableList(noIdList);
	}

	/**
	 * Checks if folio has any articles
	 *
	 * @return true, if it does
	 */
	public boolean hasArticles() {
		return !articleBackedList.isEmpty();
	}

	/**
	 * Gets the number of articles.
	 *
	 * @return the number of articles
	 */
	public int getNumberOfArticles() {
		return articleBackedList.size();
	}

	protected Integer getId() {	return id; }
	protected void setId(Integer id) { this.id = id; }

	protected IssueMeta getMeta() {	return meta; }
	void setMeta(IssueMeta meta) { this.meta = meta; }
	
	protected Set<Article> getArticles() { return articles;	}
	protected void setArticles(Set<Article> articles) {	this.articles = articles; }

	/**
	 * Checks if is contains html.
	 *
	 * @return true, if is contains html
	 */
	public boolean isContainsHtml() { return containsHtml; }
	
	/**
	 * Sets the contains html.
	 *
	 * @param containsHtml the new contains html
	 */
	public void setContainsHtml(boolean containsHtml) {	this.containsHtml = containsHtml; }

	public void setFolioId(String folioId) { this.folioId = folioId; }
	
	/**
	 * Gets the folio id.
	 *
	 * @return the folio id
	 */
	public String getFolioId() { return folioId; }

	/**
	 * Gets the resolution.
	 *
	 * @return the resolution
	 */
	public Resolution getResolution() { return resolution; }
	protected void setResolution(Resolution resolution) { this.resolution = resolution; }
	
	/**
	 * Gets the orientation.
	 *
	 * @return the orientation
	 */
	public FolioIntent getOrientation() { return orientation; }
	protected void setOrientation(FolioIntent orientation) { this.orientation = orientation; }
	
	private class ArticleSetBackedList extends AbstractSet<Article> {
		private List<Article> sortedArticleList; 
		private boolean init = false;
		
		public List<Article> getOrderedArticle() {
			if(!init) {
				sortedArticleList = new ArrayList<Article>(getArticles());
				Collections.sort(sortedArticleList);
			}
			
			return sortedArticleList;
		}
		
		
		@Override 
		public boolean add(Article article) {
			getOrderedArticle().add(article);
			Collections.sort(getOrderedArticle());
			return articles.add(article);
		}
		
		@Override
		public boolean remove(Object article) {
			getOrderedArticle().remove(article);
			return articles.remove(article);
		}
				
		@Override
		public void clear() {
			getOrderedArticle().clear();
			articles.clear();
		}

		@Override
		public int size() {
			return articles.size();
		}

		@Override
		public Iterator<Article> iterator() {
			return articles.iterator();
		}
	}
}
