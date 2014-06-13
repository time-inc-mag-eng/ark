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
package com.timeInc.ark.parser.content;

import java.io.File;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.parser.SingleContent;
import com.timeInc.ark.parser.content.dps.FolioParserAdapter;
import com.timeInc.mageng.util.misc.Status;

/**
 * A parser where an expanded Folio input directory is required, but no 
 * conversion is done. This is used for validating that the input directory conforms
 * to the structure of an expanded Folio file.
 */
public class AnyFolio extends IdentifyingContent<SingleContent> {
	private final FolioParserAdapter dpsAdapter;
	
	/**
	 * Instantiates a new any folio.
	 */
	public AnyFolio() {
		dpsAdapter = new FolioParserAdapter();
	}
	
	@Override
	protected SingleContent parseContentAfterSanitized(File rootContent, IssueMeta meta)
			throws ContentException {
		return new SingleContent(rootContent);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.Parser.ContentParser#validate(java.io.File)
	 */
	@Override
	public Status validate(File rootContent) {
		return dpsAdapter.validate(rootContent);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.Parser.ContentParser#getType()
	 */
	@Override
	public String getType() {
		return "any_folio";
	}
}
