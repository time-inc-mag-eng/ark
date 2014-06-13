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

import java.io.File;

import com.timeInc.ark.api.push.DelayedPush;
import com.timeInc.ark.api.push.PushService.IssueKeyPair;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.newsstand.RemoteNewsstandFeed;
import com.timeInc.ark.packager.PackageInfo;
import com.timeInc.ark.packager.preview.ManagedPackage;
import com.timeInc.ark.publish.Publisher;
import com.timeInc.ark.role.User;
import com.timeInc.ark.upload.preview.PreviewUploadSetting;
import com.timeInc.ark.uploader.OriginPreviewUploader;
import com.timeInc.mageng.util.file.FileUtil;
import com.timeInc.mageng.util.misc.Status;

/**
 * An application that contains workflow that is managed internally.
 * Specifically:
 * 1) remote push download scheduling
 * 2) generating an atom feed
 * 3) publishing uploaded content
 * 4) upload previews to the origin server
 *
 * @param <T> the packaging type for content
 * @param <U> the packaging type for previews 
 */
public abstract class ManagedApplication<T extends PackageInfo, U extends ManagedPackage> extends Application<T, U> implements Publisher {
	private static final long serialVersionUID = 1L;

	protected OriginPreviewUploader previewUploader;

	private RemoteNewsstandFeed feed;

	private DelayedPush<?> pushServer;

	/**
	 * Upload previews by doing the following in order:
	 * 1) Upload the previews to the origin server
	 * 2) invoke uploadPreviewResource
	 * 3) schedule a push 
	 * 4) update the atom feed
	 */
	protected final void uploadPreviewHelper(U preview, IssueMeta meta, PreviewUploadSetting config) {
		if(feed != null && previewUploader == null)
			throw new IllegalArgumentException("Preview uploader needs to be associated with this application");
		
		if(previewUploader != null) {
			for(File file : preview.previewImageDir().listFiles()) {
				previewUploader.uploadPreview(file,  meta);
			}
		}
		
		uploadPreviewResource(preview, meta, config);		

		sendPushIfNeeded(meta, config.getUser());
		addToFeedIfNeeded(preview, meta, config);
	}

	private void sendPushIfNeeded(IssueMeta meta, User user) {
		if(pushServer != null) 
			pushServer.sendNewsstandPush(user, meta, getPushDownloadKeyPair(meta));
	}

	private void addToFeedIfNeeded(U preview, IssueMeta meta, PreviewUploadSetting config) {
		if(feed != null) {
			String nsCoverUrl = previewUploader.getCdnUrl(meta, preview.getNsFileName());
			String nsDescription = config.hasNewsstandDescription() ? config.getNSDescription() : getRemoteMetaFor(meta).getCoverStory();
			feed.updateFeed(meta, nsDescription, nsCoverUrl, FileUtil.createTempDirectory(config.getTempDir()));
		}
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.publish.Publisher#publish(com.timeInc.ark.issue.IssueMeta, java.lang.String, com.timeInc.ark.publish.Publisher.PublishType)
	 */
	@Override
	public final Status publish(IssueMeta meta, String email, PublishType type) {
		Status status = publishHelper(meta, email, type);
		
		if(status != null && !status.isError())
			meta.contentPublished();
		
		return status;
	}
	
	/**
	 * Publish uploaded content/preview for a particular {@link IssueMeta}
	 * @param meta 
	 * @param email an email in case publishing requires a callback to inform the status
	 * @param type the publish type
	 * @return the status indicating of the publish request
	 */
	protected abstract Status publishHelper(IssueMeta meta, String email, PublishType type);

	
	/**
	 * Uploads previews to the destination
	 * @param preview
	 * @param meta
	 * @param config
	 */
	protected abstract void uploadPreviewResource(U preview, IssueMeta meta, PreviewUploadSetting config);
	
	
	/**
	 * Gets the key pair required to schedule a remote push download
	 * @param meta
	 * @return
	 */
	protected abstract IssueKeyPair getPushDownloadKeyPair(IssueMeta meta);
	
	/**
	 * Gets the feed.
	 *
	 * @return the feed
	 */
	public RemoteNewsstandFeed getFeed() {
		return feed;
	}
}
