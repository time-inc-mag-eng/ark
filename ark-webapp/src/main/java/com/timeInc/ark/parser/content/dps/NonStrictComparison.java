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

import java.util.List;

import com.timeInc.ark.issue.Folio;
import com.timeInc.ark.parser.content.dps.DossierComparisonInfo.DossierIdComparison;

/**
 * Doesn't strictly match the ordering of a folio's dossierid but 
 * matches as long as the original folio's dossierids ordering are preserved.
 * 
 * done w.r.t. dossierIds to Folio
 * 

 * 
 */
public class NonStrictComparison implements DossierIdComparison {

	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.content.dps.DossierComparisonInfo.DossierIdComparison#compare(com.timeInc.ark.issue.Folio, java.util.List)
	 */
	@Override
	public DossierComparisonInfo compare(Folio folio, List<String> dossierIds) {
		DossierComparisonInfo comparison = new DossierComparisonInfo();

		for (int i = 0, previousLocation = -1, shift = 0; i < dossierIds.size(); i++) {
			String dossId = dossierIds.get(i);

			int articleLocation = folio.getPositionFor(dossId);

			if(articleLocation < 0 || previousLocation >= articleLocation) {
				shift++;
				comparison.addNonMatchingId(dossId, previousLocation + shift);
			} else {
				comparison.addMatchedDossierId(dossId);
				previousLocation = articleLocation;
			}
		}
		
		return comparison;
	}

}
