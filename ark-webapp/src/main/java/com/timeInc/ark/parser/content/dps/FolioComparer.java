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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.timeInc.ark.issue.Article;
import com.timeInc.ark.issue.Folio;
import com.timeInc.ark.parser.content.ContentException;
import com.timeInc.ark.parser.content.dps.DossierComparisonInfo.ArticlePosition;
import com.timeInc.ark.parser.content.dps.DossierComparisonInfo.DossierIdComparison;
import com.timeInc.ark.parser.content.dps.FolioInfo.FolioComparisonInfo;
import com.timeInc.ark.parser.content.dps.FolioInfo.FolioComparisonInfo.Action;

/**
 * @see FolioComparer#compare(Folio, List)
 *
 */
class FolioComparer {
	private static final Logger log = Logger.getLogger(FolioComparer.class);
	
	private final DossierIdComparison dossierComparator;
	private final SortOrderStrategy sortOrderStrategy;
	
	
	/**
	 * Instantiates a new folio comparer.
	 *
	 * @param dossierComparator the dossier comparator
	 * @param sortOrderStrategy the sort order strategy
	 */
	public FolioComparer(DossierIdComparison dossierComparator, SortOrderStrategy sortOrderStrategy) {
		this.dossierComparator = dossierComparator;
		this.sortOrderStrategy = sortOrderStrategy;
	}
	
	/**
	 * Instantiates a new folio comparer.
	 */
	public FolioComparer() {
		this(new NonStrictComparison(), new EvenlyDistributed());
	}

	/**
	 * Get the FolioComparisonInfo by comparing an existing Folio against
	 * a list of dossierIds. 
	 * {@link FolioComparisonInfo#getAction()} is the following depending on what is returned by {@link DossierIdComparison}
	 * UPDATE - if matched count is 1 and non-match count is 0
	 * NEW - if the folio is null or the folio has no articles
	 * ADD - if match count is the same as the number of the folio's article and non-match is > 0
	 * 
	 * @param folio the existing folio; can be null
	 * @param parsedDossierIds the parsed dossier ids
	 * @return the folio comparison info
	 */
	public FolioComparisonInfo compare(Folio folio, List<String> parsedDossierIds) {
		if(folio != null && folio.hasArticles()) {
			DossierComparisonInfo comparisonInfo = dossierComparator.compare(folio,parsedDossierIds);

			if(comparisonInfo.getMatchedDossierCount() == 1 && comparisonInfo.getNonmatchedDossierCount() == 0) { // update single article
				String idOfArticleToUpdate = comparisonInfo.getMatchingIds().get(0);

				log.debug("Update article:" + idOfArticleToUpdate);
				
				FolioComparisonInfo info = new FolioComparisonInfo(Action.UPDATE);
				info.addArticle(folio.getArticle(idOfArticleToUpdate));
				
				return info;
				
			} else if(comparisonInfo.getMatchedDossierCount() == folio.getNumberOfArticles() && comparisonInfo.getNonmatchedDossierCount() > 0) { // add article(s)
				
				FolioComparisonInfo info = new FolioComparisonInfo(Action.ADD);
				
				List<Article> articleCopy = new ArrayList<Article>(folio.getOrderedArticles());
				
				for(ArticlePosition dossier : comparisonInfo.getNonMatchingIds()) {
					int position = dossier.getPosition();
					
					Article previous = position == 0 ? null : articleCopy.get(position-1);
					Article next = position >= articleCopy.size() ? null : articleCopy.get(position);

					int stepSize = 0;
					int start = 0;

					if(previous == null) { 
						stepSize = sortOrderStrategy.getStepSize(SortOrderStrategy.START_RANGE, next.getSortOrder()-1, 1);
						start = SortOrderStrategy.START_RANGE;
					} else if(next == null) { // end
						stepSize = sortOrderStrategy.getStepSize(previous.getSortOrder()+1, SortOrderStrategy.END_RANGE,1);
						start = previous.getSortOrder();
					} else { // in between
						stepSize = sortOrderStrategy.getStepSize(previous.getSortOrder()+1, next.getSortOrder()-1, 1);
						start = previous.getSortOrder();
					}
					
					int sortOrder = start + stepSize;

					log.debug("Add article:" + dossier.getDossierId() + " sort:" + sortOrder);
					
					Article newArticle = new Article(dossier.getDossierId(), sortOrder);
					info.addArticle(newArticle);
					
					articleCopy.add(position,newArticle);
				}
				
				return info;
				
			} else if(comparisonInfo.getMatchedDossierCount() == folio.getNumberOfArticles() && comparisonInfo.getNonmatchedDossierCount() == 0) { // nothing new
				List<Article> noIdList = folio.getArticlesWithNoId();
			
				if(noIdList.isEmpty())
					throw new ContentException("Folio is the same as previous."); 
				else {
					FolioComparisonInfo info = new FolioComparisonInfo(Action.UNFINISHED);
					
					
					log.debug("Resuming from previous upload");
					
					for(Article article : noIdList) 
						info.addArticle(article);
					
					return info;
				}
			} else
				throw new ContentException("Delete / Reordering of articles can not be performed");
			
		} else {	// new folio
			
			FolioComparisonInfo info = new FolioComparisonInfo(Action.NEW);
			
			int stepSize = sortOrderStrategy.getStepSize(SortOrderStrategy.START_RANGE,SortOrderStrategy.END_RANGE, parsedDossierIds.size());
			int sortOrder = SortOrderStrategy.START_RANGE + stepSize;
			
			for(String dossierId : parsedDossierIds) {
				Article newArticle = new Article(dossierId, sortOrder);
				
				log.trace("New article:" + dossierId + " sort:" + sortOrder);
				
				info.addArticle(newArticle);
				
				sortOrder += stepSize;
			}
			
			return info;
		}
	}
}
