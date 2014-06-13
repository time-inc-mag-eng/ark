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
package com.timeInc.ark.packager.content;
import java.io.File;
import java.util.Collections;
import java.util.List;

import com.timeInc.ark.issue.Resolution;
import com.timeInc.ark.packager.PackageInfo;
import com.timeInc.ark.parser.content.dps.ArticleMapping;
import com.timeInc.ark.parser.content.dps.FolioInfo;
import com.timeInc.ark.parser.content.dps.FolioInfo.FolioComparisonInfo.Action;
import com.timeInc.dps.producer.enums.FolioIntent;


/**
 * Value class that represents a folio package that gets uploaded to Adobe DPS.
 * Contains the packed article file, html resources, and metadata related to a folio.
 */
public class DpsContentInfo implements PackageInfo {
	private final FolioInfo info;
	private final List<ArticleMapping> packedArticles;
	private final File htmlResource;
	
	/**
	 * Instantiates a new dps content info.
	 *
	 * @param packedArticles the packed articles
	 * @param htmlResource the html resource
	 * @param info the info
	 */
	public DpsContentInfo(List<ArticleMapping> packedArticles, File htmlResource, FolioInfo info) {
		this.info = info;
		this.packedArticles = packedArticles;
		this.htmlResource = htmlResource;
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.packager.PackageInfo#getSize()
	 */
	@Override
	public long getSize() {
		long totalBytes = 0;
		
		for(ArticleMapping mapping : packedArticles) 
			totalBytes += mapping.getFile().length();
		
		totalBytes += htmlResource != null ? htmlResource.length() : 0;
		
		return totalBytes;
	}
	
	
	/**
	 * Gets the parsed info.
	 *
	 * @return the parsed info
	 */
	public FolioInfo getParsedInfo() { // todo remove this method since it is not concerned about package material -- this is used for content backup
		return info;
	}

	/**
	 * Checks if is update.
	 *
	 * @return true, if is update
	 */
	public boolean isUpdate() {
		return info.getUploadAction() == Action.UPDATE;
	}
	
	/**
	 * Gets the html resource.
	 *
	 * @return the html resource
	 */
	public File getHtmlResource() {
		return htmlResource;
	}

	/**
	 * Gets the target viewer.
	 *
	 * @return the target viewer
	 */
	public String getTargetViewer() {
		return info.getTargetViewer();
	}

	/**
	 * Gets the resolution.
	 *
	 * @return the resolution
	 */
	public Resolution getResolution() {
		return info.getResolution();
	}

	/**
	 * Gets the orientation.
	 *
	 * @return the orientation
	 */
	public FolioIntent getOrientation() {
		return info.getOrientation();
	}
	
	/**
	 * Gets the total asset count.
	 *
	 * @return the total asset count
	 */
	public int getTotalAssetCount() {
		int count = htmlResource != null ? 1 : 0;
		return count + packedArticles.size();
	}
	
	/**
	 * Gets the packed articles.
	 *
	 * @return the packed articles
	 */
	public List<ArticleMapping> getPackedArticles() {
		return Collections.unmodifiableList(packedArticles);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.packager.PackageInfo#getPrettyName()
	 */
	@Override
	public String getPrettyName() {
		return "Content.folio";
	}
}
