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
package com.timeInc.ark.role;

import com.timeInc.ark.media.uploader.MediaUploader;
import com.timeInc.ark.media.uploader.OriginMediaUploader;

/**
 * Represents a publication group which is the root of all objects.
 * There can be many applications that belong to a publication group.
 * 
 */
public class PublicationGroup implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String adGroup;

	private int issueRetention;
	
	private int mediaIssueRetention;
	
	private OriginMediaUploader mediaUploader;
	
	private boolean active;
	
	private int numDaysReport;

	/**
	 * Instantiates a new publication group.
	 */
	public PublicationGroup() {}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() { return name; }
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() { return id; }
	
	/**
	 * Gets the num days report.
	 *
	 * @return the num days report
	 */
	public int getNumDaysReport() { return numDaysReport; }
	
	/**
	 * Gets the media issue retention.
	 *
	 * @return the media issue retention
	 */
	public int getMediaIssueRetention() { return mediaIssueRetention; };
	
	/**
	 * Gets the issue retention.
	 *
	 * @return the issue retention
	 */
	public int getIssueRetention() { return issueRetention; }

	/**
	 * Gets the media uploader.
	 *
	 * @return the media uploader
	 */
	public MediaUploader getMediaUploader() { return mediaUploader; }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return id == null ? System.identityHashCode(this) : id.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (id == null) return false;
		if ( !(other instanceof PublicationGroup) ) return false;
		final PublicationGroup that = (PublicationGroup) other;
		return this.id.equals(that.getId());
	}	
	
}
