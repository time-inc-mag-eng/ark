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
package com.timeInc.ark.upload.content;

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
import com.timeInc.ark.parser.content.dps.IssueMetaSync;
import com.timeInc.ark.parser.content.dps.PreExistingFolioSync;
import com.timeInc.ark.upload.MetaUploadFacade;
import com.timeInc.ark.upload.UploadListener;
import com.timeInc.mageng.util.progress.DetailedProgressListener;

/**
 *	Content Facade responsible for the process of uploading a user uploaded file
 *	to an IssueMeta that belongs to a specific Application. 
 */
class ContentUploadFacade extends MetaUploadFacade<UserContentConfig>  {
	private static final Logger log = Logger.getLogger(ContentUploadFacade.class);

	private static final int PERCENT_PARSE = 20;
	private static final int PERCENT_UPLOAD = 80;
	
	private static final IssueMetaSync DEFAULT_FOLIO_SYNC = new PreExistingFolioSync();

	/**
	 * Instantiates a new content upload facade.
	 *
	 * @param config the config
	 * @param meta the meta
	 * @param listener the listener
	 */
	public ContentUploadFacade(UserContentConfig config, IssueMeta meta, UploadListener listener) {
		super(config, meta, listener);
	}
	
	@Override
	protected PackageInfo handleUploadSpecific(final DetailedProgressListener progress,
			final File contentToParse, final File outputDirectory) {
		
		return meta.getApplication().accept(new ApplicationVisitor<PackageInfo>() {
			@Override
			public PackageInfo visit(CdsApplication app) { return upload(app); }

			@Override
			public PackageInfo visit(DpsApplication app) {
				DEFAULT_FOLIO_SYNC.synch(meta);
				
				if(config.isNewUpload()) {
					app.clearFolio(meta.getFolio());
				}
				
				return upload(app); 
			}

			@Override
			public PackageInfo visit(ScpApplication app) {	return upload(app); }

			private <T extends PackageInfo> T upload(Application<T, ?> app) {
				try {
					log.debug("Begin of parsing content directory: " + contentToParse + " for " + meta.getReferenceId());

					progress.progressStarted("Parsing and preparing content for upload..", PERCENT_PARSE);
					
					T packedData = app.getContentProducer().produce(contentToParse, outputDirectory, meta);
					
					listener.beforeUpload(meta, packedData);

					log.debug("Begin of uploading for " + meta.getReferenceId());

					progress.progressStarted("Uploading content file..", PERCENT_UPLOAD);

					app.uploadContent(packedData, meta, new ContentUploadSetting(config.getUser()), progress);

					log.debug("Upload completed successfully for " + meta.getReferenceId());
					
					return packedData;
				} catch (IOException e) {
					throw new RuntimeException(e); 
				}
			}
		});
	}
	
	@Override
	protected boolean requiresUnpacked() {
		return meta.getApplication().getContentProducer().requiresUnpacked();
	}
}
