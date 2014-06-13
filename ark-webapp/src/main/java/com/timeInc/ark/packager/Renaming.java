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

import com.timeInc.ark.naming.FileNaming;
import com.timeInc.mageng.util.file.FileUtil;
import com.timeInc.mageng.util.misc.Precondition;

/**
 * Packages a file by renaming it to the specified outputFile
 */
public class Renaming extends SingleNamingPackager {
	protected Renaming() {} 
	
	/**
	 * Instantiates a new renaming.
	 *
	 * @param naming the naming
	 */
	public Renaming(FileNaming naming) {
		super(naming);
	}
	
	@Override
	protected void packUsingSpecifiedNaming(File input, File outputFile) throws IOException {
		if(input.isDirectory())
			throw new IllegalArgumentException("Renaming packager expects a file");
		FileUtil.moveFile(input,outputFile);
	}

	@Override
	protected String getExtension(File input) {
		Precondition.checkNull(input, "input");
		return FilenameUtils.getExtension(input.getName());
	}
}
