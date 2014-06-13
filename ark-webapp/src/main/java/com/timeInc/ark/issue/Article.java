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

/**
 * A folio article
 */
public class Article implements Comparable<Article>, Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String articleId;
	private String dossierId;
	private int sortOrder;
	private Folio folio;
	
	@SuppressWarnings("unused")
	private Article() {}
	
	Article(String dossierId) {
		this(dossierId, null, -1);
	}
	
	/**
	 * Instantiates a new article.
	 *
	 * @param dossierId the dossier id
	 * @param sortOrder the sort order relative to other articles
	 */
	public Article(String dossierId, int sortOrder) {
		this(dossierId, null, sortOrder);
	}
	
	/**
	 * Instantiates a new article.
	 *
	 * @param dossierId the dossier id
	 * @param articleId the article id
	 * @param sortOrder the sort order
	 */
	public Article(String dossierId, String articleId, int sortOrder) {
		this.dossierId = dossierId;
		this.sortOrder = sortOrder;
		this.articleId = articleId;
	}
	
	Integer getId() {
		return id;
	}

	void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the dossier id.
	 *
	 * @return the dossier id
	 */
	public String getDossierId() {
		return dossierId;
	}
	
	/**
	 * Sets the dossier id.
	 *
	 * @param dossierId the new dossier id
	 */
	public void setDossierId(String dossierId) {
		this.dossierId = dossierId;
	}
	
	/**
	 * Gets the article id.
	 *
	 * @return the article id
	 */
	public String getArticleId() {
		return articleId;
	}
	
	/**
	 * Sets the article id.
	 *
	 * @param articleId the new article id
	 */
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	
	/**
	 * Gets the sort order.
	 *
	 * @return the sort order
	 */
	public int getSortOrder() {
		return sortOrder;
	}
	
	
	/**
	 * Sets the sort order.
	 *
	 * @param sortOrder the new sort order
	 */
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	/**
	 * Gets the folio.
	 *
	 * @return the folio
	 */
	public Folio getFolio() {
		return folio;
	}

	void setFolio(Folio folio) {
		this.folio = folio;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getDossierId() == null) ? 0 : getDossierId().hashCode());
		return result;
	}

	/**
	 * @return true if the object has the same dossier id; false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Article other = (Article) obj;
		if (getDossierId() == null) {
			if (other.getDossierId() != null)
				return false;
		} else if (!getDossierId().equals(other.getDossierId()))
			return false;
		return true;
	}

	/**
	 * @return 0 if the sortOrder is the same, -1 if the sortOrder is less
	 * than the parameter, 1 otherwise
	 */
	@Override
	public int compareTo(Article other) {
		if(getSortOrder() == other.getSortOrder())
			return 0;
		else if(getSortOrder() < other.getSortOrder())
			return -1;
		else
			return 1;
	}
}
