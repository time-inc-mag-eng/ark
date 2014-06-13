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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.timeInc.ark.application.ApplicationEntity.ApplicationVisitor;
import com.timeInc.ark.application.CdsApplication;
import com.timeInc.ark.application.DpsApplication;
import com.timeInc.ark.application.ScpApplication;
import com.timeInc.ark.backup.Backup;
import com.timeInc.ark.event.AbstractEvent;
import com.timeInc.ark.event.IssueMetaEvent;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.packager.PackageInfo;
import com.timeInc.ark.upload.AbstractUploadListener;
import com.timeInc.ark.uploader.DpsServer;
import com.timeInc.ark.util.JMSTopicSender;
import com.timeInc.mageng.util.event.EventManager;
import com.timeInc.mageng.util.sftp.ScpCredentials;
import com.timeInc.mageng.util.string.StringUtil;


/**
 * Handles events for MetaFacade pertaining to the uploading of previews. 
 */
class PreviewUploadListener extends AbstractUploadListener {
	
	private final JMSTopicSender sender;
	private final UserPreviewConfig config;
	
	/**
	 * Instantiates a new preview upload listener.
	 *
	 * @param sender the sender
	 * @param userEvent the user event
	 * @param config the config
	 * @param backup the backup
	 */
	public PreviewUploadListener(JMSTopicSender sender, EventManager<Class<? extends AbstractEvent>, AbstractEvent> userEvent, UserPreviewConfig config, Backup backup) {
		super(userEvent, config, backup);
		
		this.config = config;
		this.sender = sender;
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.upload.AbstractUploadListener#fail(com.timeInc.ark.issue.IssueMeta, java.lang.String)
	 */
	@Override
	public void fail(IssueMeta meta, String reason) {
		super.fail(meta, reason);
		sendMsgIfNeeded(meta, false);
	}

	private void sendMsgIfNeeded(final IssueMeta meta, boolean success) {
		if(meta.getApplication().isSendMsg()) {

			final Map<String,String> mapMsg = new HashMap<String,String>();

			mapMsg.put("success", Boolean.toString(success));
			mapMsg.put("pubname", meta.getApplication().getPublicationGroup().getName());
			mapMsg.put("shortpub", meta.getApplication().getVendorPubName());
			mapMsg.put("appname", meta.getApplication().getName());
			mapMsg.put("saledate", Long.toString(meta.getOnSaleDate().getTime()));
			mapMsg.put("shortdate", Long.toString(meta.getCoverDate().getTime()));
			mapMsg.put("issuename", meta.getIssueName());
			mapMsg.put("referenceid", meta.getReferenceId());
			mapMsg.put("price", meta.getPrice().toString());
			
			if(!StringUtil.isEmpty(config.getCoverStory()))
				mapMsg.put("coverstory", config.getCoverStory());
			
			if(!StringUtil.isEmpty(config.getNewsstandCover()))
				mapMsg.put("newsstand", config.getNewsstandCover());

			if(backupPath != null)
				mapMsg.put("previewpath", backupPath.getAbsolutePath());


			meta.getApplication().accept(new ApplicationVisitor<Void>() {
				@Override
				public Void visit(CdsApplication app) {	
					mapMsg.put("apptype", "cds");
					return null; }

				@Override
				public Void visit(ScpApplication app) {
					ScpCredentials cred = app.getServer().getCred();
					mapMsg.put("apptype", "scp");
					mapMsg.put("login", cred.getUsername());
					mapMsg.put("password", cred.getPassword());
					mapMsg.put("port", Integer.toString(cred.getPort()));
					mapMsg.put("host", cred.getHost());
					mapMsg.put("folder", app.getServer().getLocation(meta));
					return null; }

				@Override
				public Void visit(DpsApplication app) {
					DpsServer server = app.getServer();
					mapMsg.put("apptype", "dps");
					mapMsg.put("host", server.getAddress());
					mapMsg.put("login", server.getDpsUsername());
					mapMsg.put("password", server.getDpsPassword());
					mapMsg.put("consumerkey", server.getConsumerKey());
					mapMsg.put("consumersecret", server.getConsumerSecret());
					
					if(meta.getFolio() != null) 
						mapMsg.put("folioid", meta.getFolio().getFolioId());
					
					return null; 
				}
			});
			
			sender.sendMessage(mapMsg);
		}
	}
	

	@Override
	protected void finalizeSuccess(IssueMeta meta, PackageInfo data) {
			sendMsgIfNeeded(meta, true);
			meta.previewUploaded();
	}

	protected IssueMetaEvent getWorkFlowEvent(IssueMeta meta, String reason) {
		if(meta.isPreviewUploaded())
			return new IssueMetaEvent.NewPreview(config.getUser(), new Date(), reason, meta);
		else
			return new IssueMetaEvent.UpdatePreview(config.getUser(), new Date(), reason, meta);
	}
}
