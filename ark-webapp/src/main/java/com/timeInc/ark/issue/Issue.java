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
package com.timeInc.ark.issue;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.timeInc.ark.role.PublicationGroup;

/**
 * An Issue represents an issue for a PublicationGroup.
 * 
 * A PublicationGroup can contain many Applications and each Application
 * can contain many IssueMetas. An IssueMeta is tied to an Issue since it eventually
 * belongs to a PublicationGroup.
 * 
 * In Ark, a PublicationGroup will have the same issue for different applications.
 * 
 *
 */


public class Issue implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String name; 
	
	private PublicationGroup publication;
	
	private Date shortDate;
	
	Boolean testIssue = true;
	
	Set<IssueMeta> metas;
	
	/**
	 * Instantiates a new issue.
	 */
	public Issue() {}
	
	/**
	 * Constructs an instance with an Issue name that belongs to 
	 * a certain PublicationGroup.
	 *
	 * @param name the name of the Issue
	 * @param pub the PublicationGroup it belongs to
	 * @param test the test
	 * @param shortDate the short date
	 */
	public Issue(String name, PublicationGroup pub, boolean test, Date shortDate) {
		this.name = name;
		this.publication = pub;
		this.testIssue = test;
		this.shortDate = shortDate;
	}	
		
	/**
	 * Gets the test issue.
	 *
	 * @return the test issue
	 */
	public Boolean getTestIssue() {
		return testIssue;
	}

	
	/**
	 * Sets the test issue.
	 *
	 * @param testIssue the new test issue
	 */
	public void setTestIssue(Boolean testIssue) {
		this.testIssue = testIssue;
	}		
	
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the publication.
	 *
	 * @return the publication
	 */
	public PublicationGroup getPublication() {
		return publication;
	}

	/**
	 * Sets the publication.
	 *
	 * @param publication the new publication
	 */
	public void setPublication(PublicationGroup publication) {
		this.publication = publication;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	/**
	 * Gets the short date.
	 *
	 * @return the short date
	 */
	public Date getShortDate() {
		return shortDate;
	}

	/**
	 * Sets the short date.
	 *
	 * @param shortDate the new short date
	 */
	public void setShortDate(Date shortDate) {
		this.shortDate = shortDate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((publication == null) ? 0 : publication.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Issue other = (Issue) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (publication == null) {
			if (other.publication != null)
				return false;
		} else if (!publication.equals(other.publication))
			return false;
		return true;
	}
	
}
