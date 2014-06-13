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
package com.timeInc.ark.api.dps;

import java.io.File;
import java.util.List;

import com.timeInc.ark.issue.Article;
import com.timeInc.ark.issue.FolioRemoteData;
import com.timeInc.ark.issue.Folio;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.issue.Resolution;
import com.timeInc.ark.parser.content.dps.ArticleMapping;
import com.timeInc.dps.producer.enums.FolioIntent;
import com.timeInc.dps.producer.enums.Viewer;
import com.timeInc.dps.producer.response.ArticleInfo;
import com.timeInc.folio.parser.article.sidecar.ArticleAccess;

/**
 * Communicates with Adobe DPS to create issues under an account.
 */
public interface ArkFolioClient {
	
	/**
	 * Opens a session using the provided username and password
	 * @param userName
	 * @param password
	 */
	void openSession(String userName, String password);
	
	/**
	 * Close the open session 
	 */
	void closeSession();
	
	/**
	 * Delete an existing article that belongs to a folio
	 * @param article
	 */
	void deleteArticle(Article article);
	
	
	/**
	 * Upload landscape and portrait previews to the specified folio
	 * @param portrait
	 * @param landscape
	 * @param folio
	 */
	void uploadPreview(File portrait, File landscape, Folio folio);
	
	
	/** 
	 * Upload an article to the specified folio. The folio must already be created
	 * @param folio the folio to add an article to
	 * @param articleMapping the information required to upload an article
	 * @return the article id
	 */
	String uploadArticle(Folio folio, ArticleMapping articleMapping);
	
	/**
	 * Sets the access level for an existing article
	 * @param folio the folio that contains this article
	 * @param articleId
	 * @param access the access level to set the article to
	 */
	void setArticleAccess(Folio folio, String articleId, ArticleAccess access); 
	
	/**
	 * Creates a folio based the specified parameters
	 * @param meta the issue's metadata
	 * @param resolution the resolution of the folio
	 * @param intent the orientation
	 * @param targetViewer the dps viewer version
	 * @param viewer the targeted viewer
	 * @param pubName the publication name to use
	 * @return the folio id
	 */
	String createFolio(IssueMeta meta, Resolution resolution, FolioIntent intent, String targetViewer, Viewer viewer, String pubName);
	
	/**
	 * Update an existing folio associated with the IssueMeta 
	 * using the provided parameters
	 * @param meta the metadata which contains the folio; uses the sale date and short date of this meta
	 * to update the folio
	 * @param targetViewer the target viewer
	 * @param pubName the publication name to use
	 */
	void updateFolio(IssueMeta meta, String targetViewer, String pubName);

	/**
	 * Updates a folio cover story 
	 * @param description the new description
	 * @param folio the existing folio
	 */
	void updateFolioDescription(String description, Folio folio);
	
	/**
	 * Gets remote information related to an existing folio
	 * @param folio the existing folio
	 * @return the remote information
	 */
	FolioRemoteData getFolioInfo(Folio folio);
	
	/**
	 * Get remote information for articles contained in the folio
	 * @param folio the existing folio
	 * @return list of article information
	 */
	List<ArticleInfo> getArticleInfo(Folio folio);
	
	/**
	 * Deletes html resources associated with a folio if there is any
	 * @param folio
	 */
	void deleteHtmlResourceIfAny(Folio folio);
	
	/**
	 * Upload html resource to the specified folio
	 * @param folio an existing folio
	 * @param locationOfHtmlResources
	 */
	void uploadHtmlResources(Folio folio, File locationOfHtmlResources);
	
	
	/**
	 * Delete a an existing folio
	 * @param folio
	 */
	void deleteFolio(Folio folio);
}
