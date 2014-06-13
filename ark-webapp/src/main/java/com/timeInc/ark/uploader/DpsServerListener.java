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
 * The listener interface for receiving {@link com.timeInc.ark.uploader.DpsServer} events.
 */
public interface DpsServerListener {
	
	/**
	 * A folio was created on DPS.
	 * @param folio 
	 * @param folioId the folio id returne by dps
	 */
	void folioCreated(Folio folio, String folioId);
	
	
	/**
	 * An article is removed
	 * @param article
	 */
	void articleRemoved(Article article);
	
	
	/**
	 * An article is added to a folio
	 * @param folio
	 * @param article
	 */
	void articleAdded(Folio folio, Article article);
	
	
	/**
	 * An article that is already associated with a folio 
	 * was uploaded to dps
	 * @param article
	 * @param articleId
	 */
	void articleUpdated(Article article, String articleId);
	
	/**
	 * Html resource is added to a folio
	 * @param folio
	 */
	void htmlResourceAdded(Folio folio);
	
	/**
	 * Html resouce is removed from a folio
	 * @param folio
	 */
	void htmlResourceRemoved(Folio folio);
	
	/**
	 * All articles associate with this folio have been deleted from dps
	 * @param folio
	 */
	void articlesRemoved(Folio folio);
}
