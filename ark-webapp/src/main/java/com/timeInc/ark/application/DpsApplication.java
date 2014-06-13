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

import java.util.List;

import org.apache.log4j.Logger;

import com.timeInc.ark.api.push.PushService.IssueKeyPair;
import com.timeInc.ark.issue.Folio;
import com.timeInc.ark.issue.FolioRemoteData;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.issue.remote.RemoteMetaData;
import com.timeInc.ark.packager.content.DpsContentInfo;
import com.timeInc.ark.packager.content.DpsContentPackager;
import com.timeInc.ark.packager.preview.DpsPreviewPackInfo;
import com.timeInc.ark.parser.content.dps.ArticleMapping;
import com.timeInc.ark.parser.content.dps.FolioInfo;
import com.timeInc.ark.parser.content.dps.FolioParserAdapter;
import com.timeInc.ark.upload.content.ContentUploadSetting;
import com.timeInc.ark.upload.preview.PreviewUploadSetting;
import com.timeInc.ark.upload.producer.ContentProducer;
import com.timeInc.ark.upload.producer.PackageProducer;
import com.timeInc.ark.upload.producer.PreviewProducer;
import com.timeInc.ark.uploader.DpsServer;
import com.timeInc.ark.uploader.DpsServer.DpsRequest;
import com.timeInc.dps.producer.enums.Viewer;
import com.timeInc.dps.producer.response.ArticleInfo;
import com.timeInc.mageng.util.exceptions.PrettyException;
import com.timeInc.mageng.util.misc.Status;
import com.timeInc.mageng.util.progress.ProgressListener;
import com.timeInc.mageng.util.string.StringUtil;

/**
 * A dps application that knows how to upload content/preview to Adobe Folio Producer.
 */
public class DpsApplication extends ManagedApplication<DpsContentInfo, DpsPreviewPackInfo> {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(DpsApplication.class);
	
	private static final int MAX_COVER_STORY = 120;
	
	private DpsServer dpsServer;
	private Viewer rendition;
	private boolean autoPublish;
	private PreviewProducer<DpsPreviewPackInfo> previewProducer;
	private PackageProducer<DpsContentInfo,FolioInfo> contentProducer;

	/**
	 * Instantiates a new dps application.
	 */
	public DpsApplication() {
		this.contentProducer = new ContentProducer<DpsContentInfo,FolioInfo>(new DpsContentPackager(), new FolioParserAdapter());
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.application.ApplicationEntity#accept(com.timeInc.ark.application.ApplicationEntity.ApplicationVisitor)
	 */
	@Override
	public <T> T accept(ApplicationVisitor<T> entity) {
		return entity.visit(this);
	}

	/**
	 * Remove a folio for this application.
	 *
	 * @param folio the folio to remove
	 */
	public void deleteFolio(final Folio folio) {
		if(folio != null) {
			dpsServer.doInSession(new DpsRequest<Void>() {
				public Void execute() {
					log.debug("Deleting folio with id " + folio.getFolioId());

					dpsServer.deleteFolio(folio);

					return null;
				}
			});
		}
	}
	
	/**
	 * Remove the articles that belong to this folio
	 *
	 * @param folio the folio
	 */
	public void clearFolio(final Folio folio) {
		if(folio != null) {
			dpsServer.doInSession(new DpsRequest<Void>() {
				public Void execute() {
					log.debug("Remove all resources for " + folio.getFolioId());

					dpsServer.deleteArticles(folio);

					if(folio.isContainsHtml())
						dpsServer.deleteHtmlResources(folio);
					
					return null;
				}
			});
		}
	}

	@Override
	protected void uploadContentHelper(final DpsContentInfo content, final IssueMeta meta, final ContentUploadSetting config, final ProgressListener listener) {
		dpsServer.doInSession(new DpsRequest<Void>() {
			public Void execute() {
				Folio folio = meta.getFolio();			

				if(StringUtil.isEmpty(folio.getFolioId()))
					dpsServer.createFolio(meta, content, rendition, getVendorPubName());
				else {
					updateFolio(meta, content);
					prepareUpdateArticle(content);
				}
					
				dpsServer.uploadContent(content, meta, listener);
				
				return null;
			}
		});
	}
	
	private void updateFolio(IssueMeta meta, DpsContentInfo content) {
		FolioRemoteData info = dpsServer.getFolioInfo(meta.getFolio());
		
		String targetViewer = info.getTargetViewer();
		
		if(content.getTargetViewer() != null && content.getTargetViewer().compareTo(info.getTargetViewer()) >= 0)
			targetViewer = content.getTargetViewer();
		
		_updateFolio(meta, targetViewer);
	}
	
	private void _updateFolio(IssueMeta meta, String targetViewer) { 
		dpsServer.updateFolio(meta, targetViewer, getVendorPubName());
	}
	
	private void prepareUpdateArticle(DpsContentInfo content) {
		if(content.isUpdate()) {
			for(ArticleMapping mapping : content.getPackedArticles()) 
				dpsServer.deleteArticle(mapping.getArticle());
		}
	}

	
	/**
	 * Gets the remote articles for.
	 *
	 * @param meta the meta
	 * @return the remote articles for
	 */
	public final List<ArticleInfo> getRemoteArticlesFor(final IssueMeta meta) {
		return dpsServer.doInSession(new DpsRequest<List<ArticleInfo>>() {
			public List<ArticleInfo> execute() {
				return dpsServer.getArticle(meta.getFolio());
			}
		});
	}
	

	/* (non-Javadoc)
	 * @see com.timeInc.ark.application.Application#getRemoteMetaFor(com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public final RemoteMetaData getRemoteMetaFor(final IssueMeta meta) {
		final FolioRemoteData remoteInfo = 
				dpsServer.doInSession(new DpsRequest<FolioRemoteData>() {
					public FolioRemoteData execute() {
						return dpsServer.getFolioInfo(meta.getFolio());
					}
				});

		if(remoteInfo == null) {
			return super.getRemoteMetaFor(meta);
		} else {
			return remoteInfo;
		}
	}

	@Override
	protected void uploadPreviewResource(final DpsPreviewPackInfo preview, final IssueMeta meta, final PreviewUploadSetting config) {
		dpsServer.doInSession(new DpsRequest<Void>() {
			public Void execute() {
				dpsServer.uploadPreview(preview, meta);
				
				if(config.hasCoverStory())
					dpsServer.setCoverStoryFor(meta.getFolio(), config.getCoverStory());

				_updateFolio(meta, null);

				return null;
			}
		});
		
		if(autoPublish) {
			Status status = publish(meta, config.getUserEmail(), PublishType.PREVIEW);
			
			if(status.isError())
				throw new PrettyException("Error publishing: " + status.getDescription());			
		}
	}

	@Override
	protected IssueKeyPair getPushDownloadKeyPair(IssueMeta meta) {
		return new IssueKeyPair("productId", meta.getReferenceId());
	}
	
	/**
	 * Unpublish.
	 *
	 * @param meta the meta
	 * @return the status
	 */
	public Status unpublish(IssueMeta meta) {
		Status status = dpsServer.unpublish(meta);
		
		if(!status.isError()) {
			meta.contentUnpublished();
		}
		return status;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.application.ManagedApplication#publishHelper(com.timeInc.ark.issue.IssueMeta, java.lang.String, com.timeInc.ark.publish.Publisher.PublishType)
	 */
	@Override
	public Status publishHelper(IssueMeta meta, String email, PublishType type) {
		if(!meta.isPreviewUploaded() && meta.getFolio() == null) 
			return new Status("Previews must be uploaded first",true);
		else {
			return dpsServer.publish(type, meta, email, rendition);
		}
	}
	
	/**
	 * Sets the preview producer.
	 *
	 * @param previewProducer the new preview producer
	 */
	public void setPreviewProducer(PreviewProducer<DpsPreviewPackInfo> previewProducer) {
		this.previewProducer = previewProducer;
	}

	/**
	 * Sets the dps server.
	 *
	 * @param dpsServer the new dps server
	 */
	public void setDpsServer(DpsServer dpsServer) {
		this.dpsServer = dpsServer;
	}
	
	/**
	 * Gets the server.
	 *
	 * @return the server
	 */
	public DpsServer getServer() {
		return dpsServer;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.application.Application#getMaxCoverStory()
	 */
	@Override
	public int getMaxCoverStory() {
		return MAX_COVER_STORY;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.application.Application#getPreviewSetting()
	 */
	@Override
	public PreviewProducer<DpsPreviewPackInfo> getPreviewProducer() {
		return previewProducer;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.application.Application#getContentSetting()
	 */
	@Override
	public PackageProducer<DpsContentInfo,FolioInfo> getContentProducer() {
		return contentProducer;
	}
	
	/**
	 * Gets the rendition.
	 *
	 * @return the rendition
	 */
	public Viewer getRendition(){
		return this.rendition;
	}

	/**
	 * Sets the rendition.
	 *
	 * @param rendition the new rendition
	 */
	public void setRendition(Viewer rendition) {
		this.rendition = rendition;
	}
}
