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
package com.timeInc.ark.upload.preview;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.timeInc.ark.application.Application;
import com.timeInc.ark.application.ApplicationEntity.ApplicationVisitor;
import com.timeInc.ark.application.CdsApplication;
import com.timeInc.ark.application.DpsApplication;
import com.timeInc.ark.application.ScpApplication;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.packager.PackageInfo;
import com.timeInc.ark.upload.MetaUploadFacade;
import com.timeInc.ark.upload.UploadListener;
import com.timeInc.mageng.util.exceptions.PrettyException;
import com.timeInc.mageng.util.progress.DetailedProgressListener;
import com.timeInc.mageng.util.string.StringUtil;


/**
 *	Preview Facade responsible for the process of uploading a user uploaded file
 *	to an IssueMeta that belongs to a specific Application. 
 */
class PreviewUploadFacade extends MetaUploadFacade<UserPreviewConfig> {
	private static final Logger log = Logger.getLogger(PreviewUploadFacade.class);
	
	/**
	 * Instantiates a new preview upload facade.
	 *
	 * @param config the config
	 * @param meta the meta
	 * @param listener the listener
	 */
	public PreviewUploadFacade(UserPreviewConfig config, IssueMeta meta,
			UploadListener listener) {
		super(config, meta, listener);
	}

	@Override
	protected boolean requiresUnpacked() {
		return meta.getApplication().getPreviewProducer().requiresUnpacked();
	}

	@Override
	protected PackageInfo handleUploadSpecific(DetailedProgressListener progress, 
			final File contentToParse, final File outputDirectory) {
		
		throwExceptionIfRequiresContentBeforePreviews();

		return meta.getApplication().accept(new ApplicationVisitor<PackageInfo>() {
			@Override
			public PackageInfo visit(CdsApplication app) { return upload(app); }

			@Override
			public PackageInfo visit(DpsApplication app) { return upload(app); }

			@Override
			public PackageInfo visit(ScpApplication app) {	return upload(app); }

			private <T extends PackageInfo> T upload(Application<?, T> app) {
				try {
					T packedData = app.getPreviewProducer().produce(contentToParse, outputDirectory, meta);

					listener.beforeUpload(meta, packedData);
					
					PreviewUploadSetting setting = null; 
					
					if(StringUtil.isEmpty(config.getNewsstandCover()))
						setting = new PreviewUploadSetting(config.getUser(), config.getCoverStory(), outputDirectory);
					else 
						setting = new PreviewUploadSetting(config.getUser(), config.getCoverStory(), config.getNewsstandCover(), outputDirectory);
					
					
					log.debug("Using preview upload setting:"  + setting);
					
					app.uploadPreview(packedData, meta, setting);

					return packedData;
				} catch (IOException e) {
					throw new RuntimeException(e); 
				}
			}
		});
	}
	
	private void throwExceptionIfRequiresContentBeforePreviews() {
		if(meta.getApplication().isRequireContentFirst() && !meta.isContentUploaded())
			throw new PrettyException("Content must be uploaded before previews");
	}

}
