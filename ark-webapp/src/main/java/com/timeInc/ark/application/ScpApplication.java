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
package com.timeInc.ark.application;


import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.packager.SingleFileInfo;
import com.timeInc.ark.upload.content.ContentUploadSetting;
import com.timeInc.ark.upload.preview.PreviewUploadSetting;
import com.timeInc.ark.upload.producer.PackageProducer;
import com.timeInc.ark.upload.producer.PreviewProducer.SinglePreviewProducer;
import com.timeInc.ark.uploader.ScpServer;
import com.timeInc.mageng.util.progress.ProgressListener;

/**
 * An scp application uploads a single packaged content/preview to an scp server.
 */
public class ScpApplication extends Application<SingleFileInfo, SingleFileInfo> {
	private static final long serialVersionUID = 1L ;
	
	private ScpServer uploader;
	
	private SinglePreviewProducer previewProducer;
	private PackageProducer<SingleFileInfo, SingleFileInfo> contentProducer;
	
	/**
	 * Instantiates a new scp application with empty
	 * settings.
	 */
	public ScpApplication() {}
	
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.application.ApplicationEntity#accept(com.timeInc.ark.application.ApplicationEntity.ApplicationVisitor)
	 */
	@Override
	public <T> T accept(ApplicationVisitor<T> entity) {
		return entity.visit(this);
	}

	@Override
	protected void uploadContentHelper(SingleFileInfo content, IssueMeta meta, ContentUploadSetting config, ProgressListener listener) {
		uploader.uploadContent(content.getSingleFile(), meta, listener);
	}

	@Override
	protected void uploadPreviewHelper(SingleFileInfo preview, IssueMeta meta, PreviewUploadSetting config) {
		uploader.uploadPreview(preview.getSingleFile(), meta);
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.application.Application#getPreviewSetting()
	 */
	@Override
	public SinglePreviewProducer getPreviewProducer() {
		return previewProducer;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.application.Application#getContentSetting()
	 */
	@Override
	public PackageProducer<SingleFileInfo, SingleFileInfo> getContentProducer() {
		return contentProducer;
	}

	/**
	 * Gets the scp server where
	 * content/previews should go to.
	 *
	 * @return the server
	 */
	public ScpServer getServer(){
		return this.uploader;
	}

	/**
	 * Sets the content producer.
	 *
	 * @param producer the producer
	 */
	public void setContentProducer(PackageProducer<SingleFileInfo, SingleFileInfo> producer) {
		this.contentProducer = producer;
	}

	/**
	 * Sets the preview producer.
	 *
	 * @param producer the new preview setting
	 */
	public void setPreviewProducer(SinglePreviewProducer producer) {
		this.previewProducer = producer;
	}
	
	/**
	 * Sets the scp server.
	 *
	 * @param uploader the new scp server
	 */
	public void setScpServer(ScpServer uploader) {
		this.uploader = uploader;
	}
}
