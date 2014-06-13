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
package com.timeInc.ark.packager;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.mageng.util.misc.Precondition;

/**
 * Does not do any sort of packaging but can be useful for nested
 * packaging where {@link SingleNamingPackager#getEnclosedName(IssueMeta)}
 * is used.
 */
public class Noop extends SingleNamingPackager {
	
	/**
	 * Instantiates a new noop.
	 */
	public Noop() {	}

	@Override
	protected String getExtension(File parsedData) {
		Precondition.checkNull(parsedData, "parsedData");
		return FilenameUtils.getExtension(parsedData.getName());
	}
	
	@Override
	protected File getOutputFile(File outputDirectory, File parsedData, IssueMeta meta) {
		return parsedData;
	}
	

	@Override
	protected void packUsingSpecifiedNaming(File input, File outputFile) throws IOException {
	}
}
