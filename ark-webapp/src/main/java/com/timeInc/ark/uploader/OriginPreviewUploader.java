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
package com.timeInc.ark.uploader;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;

import com.timeInc.ark.api.origin.OriginClient;
import com.timeInc.ark.issue.IssueMeta;


/**
 * Preview uploader that uploads to an origin server using HttpPut.
 * 
 * @see OriginClient
 */
public class OriginPreviewUploader implements PreviewUploader<File> {
	private int id; 
	
	private OriginClient client;
	
	private String originBaseUrl;

	private String cdnBaseUrl;

	private String apiPath;
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.uploader.PreviewUploader#uploadPreview(java.lang.Object, com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public void uploadPreview(File file, IssueMeta meta) {
		uploadPreview(file, getRelativePathFor(meta) + "/" + file.getName());
	}
	
	// document exception
	/**
	 * Upload preview.
	 *
	 * @param file the file
	 * @param relativePathToFile the relative path to file
	 */
	public void uploadPreview(File file, String relativePathToFile) {
			String requestPath = normalizeURL(apiPath + "/" + relativePathToFile);
			HttpPut httpput = new HttpPut(requestPath);
			httpput.setEntity(new FileEntity(file));
			client.sendRequest(httpput);			
	}
	
	/**
	 * Gets the origin url.
	 *
	 * @param meta the meta
	 * @param fileName the file name
	 * @return the origin url
	 */
	public String getOriginUrl(IssueMeta meta, String fileName) {
		return getOriginUrl(getRelativePathFor(meta) + "/" + fileName);
	}
	
	/**
	 * Gets the origin url.
	 *
	 * @param relativePathToFile the relative path to file
	 * @return the origin url
	 */
	public String getOriginUrl(String relativePathToFile) {
		return normalizeURL(originBaseUrl + "/" + relativePathToFile);
	}
	
	/**
	 * Gets the cdn url.
	 *
	 * @param relativePathToFile the relative path to file
	 * @return the cdn url
	 */
	public String getCdnUrl(String relativePathToFile)  {
		return normalizeURL(cdnBaseUrl + "/" + relativePathToFile);
	}
	
	/**
	 * Gets the cdn url.
	 *
	 * @param meta the meta
	 * @param fileName the file name
	 * @return the cdn url
	 */
	public String getCdnUrl(IssueMeta meta, String fileName)  {
		return getCdnUrl(getRelativePathFor(meta) + "/" + fileName);
	}
	
	private static String normalizeURL(String url) {
		try {
			return new URI(url).normalize().toString();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static String getRelativePathFor(IssueMeta meta) {
		return meta.getReferenceId();
	}
}
