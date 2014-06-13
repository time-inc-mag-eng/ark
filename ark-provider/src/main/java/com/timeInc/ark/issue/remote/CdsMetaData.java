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
package com.timeInc.ark.issue.remote;

/**
 * Represents MetaData for an Issue From Woodwing's CDS Server
 */
public class CdsMetaData extends RemoteMetaData implements Comparable<CdsMetaData> {
	private final int cdsId;
	private transient final String referenceId; 

	private static final int INVALID_ID = -1;
	
	/** Placeholder to represent that there is no metadata */
	public static CdsMetaData NONE = new CdsMetaData();

	/**
	 * Returns a mock CdsMetaData so that it 
	 * can be Compared with others.
	 *
	 * @param referenceId the reference id to use
	 * @return the mock
	 */
	public static CdsMetaData getMock(String referenceId) {
		return new CdsMetaData("", INVALID_ID, referenceId);
	}

	
	private CdsMetaData() {	
		super("", true);
		this.referenceId = "";
		this.cdsId = -1;
	}

	/**
	 * Instantiates a new cds meta data.
	 *
	 * @param coverStory the cover story
	 * @param cdsId the cds id
	 * @param referenceId the reference id
	 */
	public CdsMetaData(String coverStory, int cdsId, String referenceId) {
		super(coverStory, false);
		this.cdsId = cdsId;
		this.referenceId = referenceId;
	}

	/**
	 * Gets the cds id.
	 *
	 * @return the cds id
	 */
	public int getCdsId() {
		return cdsId;
	}

	/**
	 * Compares by the referenceid. Semantics is String compareTo method. 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(CdsMetaData o) {
		return referenceId.compareTo(o.referenceId);
	}
}
