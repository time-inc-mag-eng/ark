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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.timeInc.ark.api.push.PushService.IssueKeyPair;
import com.timeInc.ark.api.ww.CdsConfig.Auth;
import com.timeInc.ark.api.ww.CdsConfig.ContentLocation;
import com.timeInc.ark.api.ww.CdsConfig.MetaData;
import com.timeInc.ark.api.ww.CdsConfig.PreviewLocation;
import com.timeInc.ark.api.ww.WoodwingService;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.issue.remote.CdsMetaData;
import com.timeInc.ark.packager.SingleFileInfo;
import com.timeInc.ark.packager.preview.CdsPreviewPackInfo;
import com.timeInc.ark.packager.preview.CdsPreviewPackInfo.CdsPreviewName;
import com.timeInc.ark.upload.content.ContentUploadSetting;
import com.timeInc.ark.upload.preview.PreviewUploadSetting;
import com.timeInc.ark.upload.producer.ContentProducer.CdsContentProducer;
import com.timeInc.ark.upload.producer.PreviewProducer;
import com.timeInc.ark.uploader.OriginContentUploader;
import com.timeInc.mageng.util.exceptions.PrettyException;
import com.timeInc.mageng.util.misc.Precondition;
import com.timeInc.mageng.util.misc.Status;
import com.timeInc.mageng.util.progress.ProgressListener;

/**
 * A Woodwing CDS application that knows how to upload content/previews to CDS.
 */
public class CdsApplication extends ManagedApplication<SingleFileInfo, CdsPreviewPackInfo>  {
	private static final Logger log = Logger.getLogger(CdsApplication.class);
	
	private static final long serialVersionUID = 1L;

	private static final int MAX_COVER_STORY = 250;

	private PreviewProducer<CdsPreviewPackInfo> previewProducer;
	private CdsContentProducer contentProducer;

	private WoodwingService service;

	private Auth auth;

	private OriginContentUploader contentUploader;

	/* (non-Javadoc)
	 * @see com.timeInc.ark.application.ApplicationEntity#accept(com.timeInc.ark.application.ApplicationEntity.ApplicationVisitor)
	 */
	@Override
	public <T> T accept(ApplicationVisitor<T> entity) {
		return entity.visit(this);
	}

	@Override
	protected void uploadContentHelper(SingleFileInfo content, IssueMeta meta, ContentUploadSetting config, ProgressListener listener) {
		contentUploader.addRefreshContentEmail(config.getEmail());
		contentUploader.uploadContent(content.getSingleFile(), meta, listener);
		meta.contentUnpublished();
	}

	@Override
	protected final IssueKeyPair getPushDownloadKeyPair(IssueMeta meta) {
		CdsMetaData remoteMeta = getRemoteMetaFor(meta);

		if(remoteMeta == CdsMetaData.NONE)
			throw new IllegalStateException("Remote meta data does not exist could not get id!"); 
		else {
			return new IssueKeyPair("issueIds", String.format("[\"%s\"]", remoteMeta.getCdsId()));
		}
	}
	
	@Override
	protected void uploadPreviewResource(CdsPreviewPackInfo preview, IssueMeta meta, PreviewUploadSetting config) {
		if(getRemoteMetaFor(meta) == CdsMetaData.NONE) {
			createIssueOnKiosk(preview.getPreviewName(), meta, config.getCoverStory());
		}
	}

	private void createIssueOnKiosk(CdsPreviewName previewImageName, IssueMeta meta, String coverStory) {
		PreviewLocation previews =  new PreviewLocation(previewUploader.getCdnUrl(meta, previewImageName.verticalCover), 
				previewUploader.getCdnUrl(meta, previewImageName.horizontalCover),
				previewUploader.getCdnUrl(meta, previewImageName.smallCover),
				previewUploader.getCdnUrl(meta, previewImageName.largeCover));
		
		ContentLocation content = new ContentLocation(contentUploader.getContentUrl(meta, contentProducer.getPackager().getEnclosedName(meta)),
				contentUploader.getProtectedDir());
		
		MetaData data = new MetaData(meta.getOnSaleDate(), meta.getPaymentType().getName(), meta.getIssueName(),
				meta.getVolume(), meta.getReferenceId(), coverStory);
		
		service.createIssue(auth, getProductId(), previews, content, data);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.application.Application#getMaxCoverStory()
	 */
	@Override
	public int getMaxCoverStory() {
		return MAX_COVER_STORY;
	}

	/**
	 * Gets the remote meta for.
	 *
	 * @param metas the metas
	 * @return the remote meta for
	 */
	public final List<CdsMetaData> getRemoteMetaFor(List<IssueMeta> metas) {
		return filterByRefId(metas);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.application.Application#getRemoteMetaFor(com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public final CdsMetaData getRemoteMetaFor(IssueMeta meta) {
		return filterByRefId(Arrays.asList(meta)).get(0);
	}
	
	private List<CdsMetaData> getRemoteIssueSorted() {
		List<CdsMetaData> remoteIssue = service.getIssues(auth, getProductId());
		Collections.sort(remoteIssue);
		return remoteIssue;
	}
	
	private List<CdsMetaData> filterByRefId(List<IssueMeta> metas) {
		List<CdsMetaData> resultSet = new ArrayList<CdsMetaData>(metas.size());
		
		List<CdsMetaData> rIssue = getRemoteIssueSorted();
		
		for(IssueMeta meta : metas) {
			int index = Collections.binarySearch(rIssue, CdsMetaData.getMock(meta.getReferenceId()));
			
			if(index >= 0)
				resultSet.add(rIssue.get(index));
			else
				resultSet.add(CdsMetaData.NONE);
		}
		
		return resultSet;
	}
	
	
	/**
	 * Sets the WoodwingService
	 *
	 * @param service the new WoodwingService
	 */
	public void setWoodWingService(WoodwingService service) {
		Precondition.checkNull(service, "service");
		this.service = service;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.application.Application#getContentProducer()
	 */
	@Override
	public CdsContentProducer getContentProducer() {
		return contentProducer;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.application.Application#getPreviewProducer()
	 */
	@Override
	public PreviewProducer<CdsPreviewPackInfo> getPreviewProducer() {
		return previewProducer;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.application.ManagedApplication#publishHelper(com.timeInc.ark.issue.IssueMeta, java.lang.String, com.timeInc.ark.publish.Publisher.PublishType)
	 */
	@Override
	public Status publishHelper(IssueMeta meta, String email, PublishType type) {
		try {
			contentUploader.publish(meta);
			return new Status("Publish to origin successfully", false);
		} catch(PrettyException e) {
			log.error(e.getDetailedMsg());
			return new Status(e.getFriendlyMsg(), true);
		} catch(Exception e) {
			return new Status("Error publishing to origin server", true);
		}
	}
}
