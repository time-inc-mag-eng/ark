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

import com.timeInc.dps.producer.enums.ProtectedAccess;
import com.timeInc.folio.parser.article.sidecar.ArticleAccess;

/**
 * The Class FolioProducerUtil.
 */
public class FolioProducerUtil {
	private FolioProducerUtil() {}

	/**
	 * Convert a side car article access level
	 * to DPS access level
	 *
	 * @param access the side car access level
	 * @return the DPS access level
	 */
	public static ProtectedAccess convertAccessFrom(ArticleAccess access) {
		switch(access) {
			case FREE: 
				return ProtectedAccess.Free;
			case METERED: 
				return ProtectedAccess.Open;
			default:
				return ProtectedAccess.Closed;
		}
	}

}

