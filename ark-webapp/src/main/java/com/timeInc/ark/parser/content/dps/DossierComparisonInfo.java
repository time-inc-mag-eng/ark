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
import java.util.Collections;
import java.util.List;

import com.timeInc.ark.issue.Folio;

/**
 * Value class that holds the comparison result for a list of dossierIds with an existing Folio that contains articles
 * (which map to dossierIds)
 */
public class DossierComparisonInfo {
	
	/**
	 * The Interface DossierIdComparison.
	 */
	public interface DossierIdComparison {
		/**
		 * Compare an existing folio's dossierids with a list of dossierIds
		 * @param folio the existing folio
		 * @param dossierIds a list of dossierIds
		 * @return the comparison information
		 */
		DossierComparisonInfo compare(Folio folio, List<String> dossierIds);
	}
	
	private List<String> matches = new ArrayList<String>();
	private List<ArticlePosition> mismatches = new ArrayList<ArticlePosition>();
	
	/**
	 * Adds a matched dossier id.
	 *
	 * @param dossierId the dossier id
	 */
	public void addMatchedDossierId(String dossierId) {
		matches.add(dossierId);
	}
	
	/**
	 * Adds the non matching id
	 *
	 * @param dossierId the new dossier id
	 * @param position the position of where it should go relative to the compared existing Folio
	 */
	public void addNonMatchingId(String dossierId, int position) {
		mismatches.add(new ArticlePosition(dossierId,position));
	}
	
	/**
	 * Gets the matched dossier count.
	 *
	 * @return the matched dossier count
	 */
	public int getMatchedDossierCount() {
		return matches.size();
	}
	
	/**
	 * Gets the nonmatched dossier count.
	 *
	 * @return the nonmatched dossier count
	 */
	public int getNonmatchedDossierCount() {
		return mismatches.size();
	}
	
	/**
	 * Gets the matching ids.
	 *
	 * @return the matching ids
	 */
	public List<String> getMatchingIds() {
		return Collections.unmodifiableList(matches);
	}
	
	/**
	 * Gets the non matching ids.
	 *
	 * @return the non matching ids
	 */
	public List<ArticlePosition> getNonMatchingIds() {
		return Collections.unmodifiableList(mismatches);
	}
	
	/**
	 * The Class ArticlePosition.
	 */
	public static class ArticlePosition {
		private final String dossierId;
		private final int position;
				
		/**
		 * Instantiates a new article position.
		 *
		 * @param dossierId the dossier id
		 * @param position the position
		 */
		public ArticlePosition(String dossierId, int position) {
			this.dossierId = dossierId;
			this.position = position;
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
		 * Gets the position.
		 *
		 * @return the position
		 */
		public int getPosition() {
			return position;
		}
	}
}
