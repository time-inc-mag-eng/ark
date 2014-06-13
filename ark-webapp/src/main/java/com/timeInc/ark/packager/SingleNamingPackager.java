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
import org.apache.log4j.Logger;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.naming.FileNaming;
import com.timeInc.ark.parser.SingleContent;

/**
 * Packages a {@link SingleContent} to a {@link SingleFileInfo} . Supports nested packaging. 
 * i.e. a zip within a tar. 
 * Uses {@link FileNaming} as the name of the package. If there is no 
 * extension in FileNaming then it uses {@link SingleNamingPackager#getExtension(File)}
 */
public abstract class SingleNamingPackager implements Packager<SingleFileInfo, SingleContent> {
	private static final Logger log = Logger.getLogger(SingleNamingPackager.class);

	private Integer id;
	private SingleNamingPackager parentPacker;
	private FileNaming naming;

	/**
	 * Sets the naming.
	 *
	 * @param naming the new naming
	 */
	public void setNaming(FileNaming naming) {
		this.naming = naming;
	}
	
	/**
	 * Instantiates a new single naming packager.
	 *
	 * @param parentPacker the parent packer
	 * @param naming the naming
	 */
	public SingleNamingPackager(SingleNamingPackager parentPacker, FileNaming naming) {
		this.parentPacker = parentPacker;
		this.naming = naming;
	}
	protected SingleNamingPackager() {} 
	
	/**
	 * Instantiates a new single naming packager.
	 *
	 * @param naming the naming
	 */
	public SingleNamingPackager(FileNaming naming) {
		this(null, naming);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.packager.Packager#pack(java.lang.Object, java.io.File, com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public final SingleFileInfo pack(SingleContent singleContent, File outputDirectory, IssueMeta meta) throws IOException {
		File fileToPack = singleContent.getRootContent();

		File outputFile = getOutputFile(outputDirectory, fileToPack, meta);

		log.debug("Input file:" + fileToPack + " Output:" + outputFile);
		
		packUsingSpecifiedNaming(fileToPack, outputFile);
		
		SingleFileInfo info = getSingleFilePackDataFor(outputFile);
		
		if(parentPacker != null) {
			SingleFileInfo parentInfo = parentPacker.pack(new SingleContent(info.getSingleFile()), outputDirectory, meta);
			return parentInfo;
		} else {
			return info;
		}
	}

	/**
	 * Gets the enclosed name of the package if
	 * it is nested.
	 *
	 * @param meta the meta
	 * @return an empty String if it is not nested otherwise the naming
	 */
	public String getEnclosedName(IssueMeta meta) {
		if(parentPacker == null) {
			return "";
		} else {
			return getNaming(null, meta);
		}
	}
	
	protected File getOutputFile(File outputDirectory, File parsedData, IssueMeta meta) {
		return new File(outputDirectory, getNaming(parsedData, meta));
	}

	private String getNaming(File parsedData, IssueMeta meta) {
		String fileName = naming.getFileName(meta);

		String fileNameExt = FilenameUtils.getExtension(fileName); // naming extension overrides delegating extension

		if(fileNameExt.isEmpty()) {
			String parsedDataExt = getExtension(parsedData);

			if(parsedDataExt.isEmpty())
				return fileName;
			else 
				return fileName + "." + parsedDataExt;
		}
		else 
			return fileName;
	}

	private static SingleFileInfo getSingleFilePackDataFor(File packedFile) {
		return new SingleFileInfo(packedFile);
	}

	protected abstract String getExtension(File parsedData);

	protected abstract void packUsingSpecifiedNaming(File input, File outputFile) throws IOException;
	
	/**
	 * Gets the naming.
	 *
	 * @return the naming
	 */
	public FileNaming getNaming() {
		return naming;
	}
	
}
