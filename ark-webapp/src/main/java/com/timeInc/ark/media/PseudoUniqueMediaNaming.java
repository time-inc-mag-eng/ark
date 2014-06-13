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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.io.FilenameUtils;

import com.timeInc.ark.dao.MediaLocationDAO;
import com.timeInc.ark.issue.Issue;

/**
 * Generates a pseudo unique path that is a valid HTTP path if a {@link MediaLocation} does not exists for this particular
 * media file that is associated with an issue. Includes the original media file name stripping out
 * invalid characters. 
 */
public class PseudoUniqueMediaNaming implements MediaPathNaming {
	private static final String REG_EXP = "[^\\p{L}\\p{N}]"; // remove anything besides alphanumerics
	private static final String PATH_SEP = "/";

	private final MediaLocationDAO mediaDAO;
	
	/**
	 * Instantiates a new pseudo unique media naming.
	 */
	public PseudoUniqueMediaNaming() {
		this(new MediaLocationDAO());
	}
	
	/**
	 * Instantiates a new pseudo unique media naming.
	 *
	 * @param mediaDAO the media dao
	 */
	public PseudoUniqueMediaNaming(MediaLocationDAO mediaDAO) {
		this.mediaDAO = mediaDAO;
	}
	
	private static final String getUniqueId() {
		return String.valueOf(System.currentTimeMillis());
	}
	
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.media.MediaPathNaming#getPath(com.timeInc.ark.issue.Issue, java.io.File)
	 */
	@Override
	public String getPath(Issue issue, File file) {
		String fileName = file.getName();
		
		MediaLocation location = mediaDAO.find(issue, file.getName());
		
		if(location != null) {
			return location.getRelativePath();
		} else {
			String issueName = encodeUrl(issue.getName());
			String pubName = encodeUrl(issue.getPublication().getName());
			
			String identifier = FilenameUtils.getBaseName(fileName).replaceAll(REG_EXP,"_");
			
			String pathToFile = identifier + "_" + getUniqueId() + "." + FilenameUtils.getExtension(fileName);
			return PATH_SEP + pubName + PATH_SEP + issueName + PATH_SEP + pathToFile;
		}
	}
	
	private static String encodeUrl(String value) {
		try {
			value = value.replaceAll("[^A-Za-z0-9()\\[\\]]", "");
			return URLEncoder.encode(value,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
