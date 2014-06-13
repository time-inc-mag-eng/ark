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
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.io.IOUtils;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.naming.PathNaming;
import com.timeInc.mageng.util.progress.ProgressListener;
import com.timeInc.mageng.util.progress.wrappers.CountingInputFileStream;
import com.timeInc.mageng.util.sftp.ScpCredentials;
import com.timeInc.mageng.util.sftp.SftpServer;

/**
 * Content and preview uploader via Scp using {@link PathNaming} to resolve the path on the scp server.
 */
public class ScpServer implements ContentUploader<File>, PreviewUploader<File> {
	private Integer id;
	private ScpCredentials cred;
	private PathNaming location;
	private final SftpServer sftpService;

	protected ScpServer() {
		this.sftpService = new SftpServer();
	}
	
	/**
	 * Instantiates a new scp server.
	 *
	 * @param cred the cred
	 * @param location the location
	 * @param sftpService the sftp service
	 */
	public ScpServer(ScpCredentials cred, PathNaming location, SftpServer sftpService) {
		this.cred = cred;
		this.location = location;
		this.sftpService = sftpService;
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.uploader.ContentUploader#uploadContent(java.lang.Object, com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public void uploadContent(File file, IssueMeta meta) {
		uploadContent(file, meta, null);
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.uploader.ContentUploader#uploadContent(java.lang.Object, com.timeInc.ark.issue.IssueMeta, com.timeInc.mageng.util.progress.ProgressListener)
	 */
	@Override
	public void uploadContent(File file, IssueMeta meta, ProgressListener listener) {
		String locationPath = getLocation(meta);
		
		if(listener == null) {
			sftpService.upload(cred, locationPath, file);		
		} else {
			FileInputStream fileStream = null;
			
			try {
				fileStream = new FileInputStream(file);
				sftpService.upload(cred, locationPath, new CountingInputFileStream(listener, fileStream, file.length()), file.getName());
			} catch (FileNotFoundException e) {
				throw new TransferException("File not found", e);
			} finally {
				IOUtils.closeQuietly(fileStream);
			}
		}	
	}
	
	/**
	 * Gets the location.
	 *
	 * @param meta the meta
	 * @return the location
	 */
	public String getLocation(IssueMeta meta) {
		return location.getPathName(meta);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.uploader.PreviewUploader#uploadPreview(java.lang.Object, com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public void uploadPreview(File file, IssueMeta meta) {
		this.uploadContent(file, meta);
	}
	
	/**
	 * Gets the cred.
	 *
	 * @return the cred
	 */
	public ScpCredentials getCred() {
		return cred;
	}

	/**
	 * Sets the cred.
	 *
	 * @param cred the new cred
	 */
	public void setCred(ScpCredentials cred) {
		this.cred = cred;
	}

	/**
	 * Sets the path naming.
	 *
	 * @param location the new path naming
	 */
	public void setPathNaming(PathNaming location){
		this.location=location;
	}
	
	/**
	 * Gets the path naming.
	 *
	 * @return the path naming
	 */
	public PathNaming getPathNaming(){
		return this.location;
	}
}
