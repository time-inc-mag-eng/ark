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
package com.timeInc.ark.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.media.MediaLocation;

/**
 * DAO for {@link MediaLocation}
 */
public class MediaLocationDAO extends HibernateGenericDAO<MediaLocation, Integer> {

	/**
	 * Gets uploaded media location for issue.
	 *
	 * @param issue the issue
	 * @return the media locations for issue
	 */
	public List<MediaLocation> getMediaLocationForIssue(Issue issue) {
		return findByCriteria(Restrictions.eq("issue", issue));

	}
	
	/**
	 * Find the location of the uploaded filename for a particular issue
	 *
	 * @param issue the issue
	 * @param fileName the file name
	 * @return the media location
	 */
	public MediaLocation find(Issue issue, String fileName) {
		Criteria crit = getCriteria(Restrictions.eq("issue",issue),Restrictions.eq("fileName", fileName));
		return (MediaLocation) crit.uniqueResult();
	}
}
