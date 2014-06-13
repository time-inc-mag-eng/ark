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
package com.timeInc.ark.parser;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.mageng.util.file.FileUtil;


/**
 * 
 * Parser for a directory that removes OS generated files.
 * 
 * @param <T> the generic type
 */
public abstract class AbstractSanitizer<T> implements Parser<T> {

	/** The Constant MAC_FOLDER. */
	public static final String MAC_FOLDER = "__MACOSX";

	/** The Constant DS_STORE. */
	public static final String DS_STORE = ".DS_Store";

	private static final Logger log = Logger.getLogger(AbstractSanitizer.class);

	private final Set<String> fileNames;

	/**
	 * Instantiates a new abstract sanitizer.
	 *
	 * @param filesToRemove the files to remove
	 */
	public AbstractSanitizer(Set<String> filesToRemove) {
		this.fileNames = filesToRemove;
	}

	/**
	 * Instantiates a new abstract sanitizer.
	 */
	public AbstractSanitizer() {
		fileNames = new HashSet<String>();
		fileNames.add(MAC_FOLDER);
		fileNames.add(DS_STORE);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.Parser#parse(java.io.File, com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public final T parse(File toParseDir, IssueMeta meta) {
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return fileNames.contains(pathname.getName());
			}
		};

		Collection<File> c = FileUtil.listFiles(toParseDir, filter , true); 

		for (File f : c) {
			log.trace("Deleting: " + f.getAbsolutePath());
			FileUtil.deleteQuietly(f);
		}		

		return parseContentAfterSanitized(toParseDir,meta);
	}


	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.Parser#requiresUnpacked()
	 */
	@Override
	public boolean requiresUnpacked() {
		return true;
	}

	protected abstract T parseContentAfterSanitized(File rootContent, IssueMeta meta);
}
