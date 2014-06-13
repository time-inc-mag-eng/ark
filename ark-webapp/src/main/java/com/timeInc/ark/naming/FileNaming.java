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
package com.timeInc.ark.naming;

import com.timeInc.ark.issue.IssueMeta;

/**
 * Produces a file naming convention for a particular {@link IssueMeta}
 */
public class FileNaming {
	private Integer id;
	private Naming naming;
	
	FileNaming() {}

	/**
	 * Instantiates a new file naming.
	 *
	 * @param naming the underlying naming
	 */
	public FileNaming(Naming naming) {
		this.naming = naming;
	}
	
	
	/**
	 * Gets the file name.
	 *
	 * @param meta the meta
	 * @return the file name
	 */
	public String getFileName(IssueMeta meta) {
		return naming.getName(meta);
	}
	
	/**
	 * Gets the file naming.
	 *
	 * @return the file naming
	 */
	public Naming getUnderlyingNaming() {
		return naming;
	}

	/**
	 * Sets the naming.
	 *
	 * @param naming the new naming
	 */
	public void setNaming(Naming naming) {
		this.naming = naming;
	}
}
