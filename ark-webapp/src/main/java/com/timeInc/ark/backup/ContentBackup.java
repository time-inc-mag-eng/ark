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
package com.timeInc.ark.backup;

import java.io.File;

import org.apache.log4j.Logger;

import com.timeInc.ark.application.ApplicationEntity.ApplicationVisitor;
import com.timeInc.ark.application.CdsApplication;
import com.timeInc.ark.application.DpsApplication;
import com.timeInc.ark.application.ScpApplication;
import com.timeInc.ark.backup.Storage.Location;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.packager.PackageInfo;
import com.timeInc.ark.packager.content.DpsContentInfo;
import com.timeInc.ark.parser.content.dps.ArticleMapping;
import com.timeInc.ark.parser.content.dps.FolioParserAdapter;
import com.timeInc.ark.parser.content.dps.FolioInfo;
import com.timeInc.ark.parser.content.dps.FolioInfo.FolioComparisonInfo.Action;
import com.timeInc.mageng.util.compression.Compression;

/**
 * Backup an uploaded content file.
 * 
 * If IssueMeta belongs to a DpsApplication and there is 
 * an already existing folio file at the backup location then that file
 * will be updated if it was not a new upload.
 * 
 */
public class ContentBackup implements Backup {
	private static final Logger log = Logger.getLogger(ContentBackup.class);

	private final Location location;
	private final Storage storage;
	private final Compression comp;

	/**
	 * Instantiates a new content backup.
	 *
	 * @param location the location
	 * @param storage the storage
	 * @param comp the comp
	 */
	public ContentBackup(Location location, Storage storage, Compression comp) {
		this.location = location;
		this.storage = storage;
		this.comp = comp;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.backup.Backup#backup(com.timeInc.ark.issue.IssueMeta, com.timeInc.ark.packager.PackageInfo, java.io.File, java.io.File, boolean, java.io.File)
	 */
	@Override
	public File backup(final IssueMeta meta, final PackageInfo packMetaData, final File content,
			final File decompressedContent, final boolean newUpload, final File tempDirectory) {
		
		final File locationOfBackup = location.getLocation(meta, content);
		
		
		return meta.getApplication().accept(new ApplicationVisitor<File>() { 
			@Override
			public File visit(CdsApplication app) {
				saveFile(content, locationOfBackup);
				return locationOfBackup;
			}

			@Override
			public File visit(ScpApplication app) {
				saveFile(content, locationOfBackup);
				return locationOfBackup;
			}
			
			@Override
			public File visit(DpsApplication app) {
				if(DpsContentInfo.class.isInstance(packMetaData)) { 	
					DpsContentInfo casted = DpsContentInfo.class.cast(packMetaData);
					
					FolioInfo info = casted.getParsedInfo();
					
					if(info.getUploadAction() == Action.NEW || info.getUploadAction() == Action.UNFINISHED || newUpload || !locationOfBackup.exists()) {
						log.debug("Content does not exist or it is a new upload or no folio info persisted");
						saveFile(content, locationOfBackup);
					} else {
						log.debug("Content already exists and it is not a new upload");
						
						if(casted.getParsedInfo().getHtmlResource() != null) {
							comp.remove(locationOfBackup, casted.getParsedInfo().getHtmlResource().getName(), true);
							comp.add(locationOfBackup, casted.getParsedInfo().getHtmlResource(), "");
						}
						
						if(info.getUploadAction() == Action.ADD) { 
							File folioXml = FolioParserAdapter.META_FINDER.getMetaDataFile(decompressedContent);
							comp.add(locationOfBackup, folioXml, "");							
						} else if(info.getUploadAction() == Action.UPDATE) {
							for(ArticleMapping mapping : info.getArticleMapping()) {
								String dossierId = mapping.getArticle().getDossierId();
								comp.remove(locationOfBackup, dossierId, true);
							}
						}
						
						for(ArticleMapping mapping : info.getArticleMapping()) {
							log.debug("Adding article:" + mapping.getFile() + " to " + locationOfBackup);
							comp.add(locationOfBackup, mapping.getFile(), "");
						}
					} 
					return locationOfBackup;
				} else
					throw new IllegalArgumentException("Expected an instance of DpsContentInfo but was passed in " + packMetaData.getClass());
			}
			
			private void saveFile(File source, File destination) {
				log.debug("Backing up file: " + source + " to " + destination);
				storage.save(source, destination);
			}
		});
	}
}
