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
package com.timeInc.ark.media;

import java.util.Date;

import com.timeInc.ark.issue.Issue;

/**
 * Value class that represents a media location for an {@link Issue} that is uploaded to CDN.
 */
public class MediaLocation {
	private Integer id;
	
	private Issue issue;
	
	private String fileName;
	
	private String relativePath;
	
	private String previewUrl;
	
	private String cdnUrl;
	
	private Date modified;
	
	protected MediaLocation() {}
	
	/**
	 * Instantiates a new media location.
	 *
	 * @param issue the issue
	 * @param fileName the file name
	 * @param previewUrl the preview url
	 * @param cdnUrl the cdn url
	 * @param relativePath the relative path
	 */
	public MediaLocation(Issue issue, String fileName, String previewUrl, String cdnUrl, String relativePath) {
		this.issue = issue;
		this.fileName = fileName;
		this.previewUrl = previewUrl;
		this.cdnUrl = cdnUrl;
		this.modified = new Date();
		this.relativePath = relativePath;
	}
	
	/**
	 * File updated.
	 *
	 * @param previewUrl the preview url
	 * @param cdnUrl the cdn url
	 * @param relativePath the relative path
	 */
	public void fileUpdated(String previewUrl, String cdnUrl, String relativePath) {
		this.previewUrl = previewUrl;
		this.cdnUrl = cdnUrl;
		this.relativePath = relativePath;
		this.modified = new Date();
	}

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Gets the preview url.
	 *
	 * @return the preview url
	 */
	public String getPreviewUrl() {
		return previewUrl;
	}

	/**
	 * Gets the cdn url.
	 *
	 * @return the cdn url
	 */
	public String getCdnUrl() {
		return cdnUrl;
	}

	/**
	 * Gets the modified.
	 *
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * Gets the relative path.
	 *
	 * @return the relative path
	 */
	public String getRelativePath() {
		return relativePath;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}
}
