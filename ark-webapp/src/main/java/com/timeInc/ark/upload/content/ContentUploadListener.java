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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

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
/**
 * Handles events for MetaFacade pertaining to the uploading of content. 
 */
class ContentUploadListener extends AbstractUploadListener {
	private static final Logger log = Logger.getLogger(ContentUploadListener.class);
	
	private final JMSTopicSender sender;
	private final UserContentConfig config;
	
	/**
	 * Instantiates a new content upload listener.
	 *
	 * @param sender the sender
	 * @param userEvent the user event
	 * @param backup the backup
	 * @param config the config
	 */
	public ContentUploadListener(JMSTopicSender sender, EventManager<Class<? extends AbstractEvent>, AbstractEvent> userEvent,
			Backup backup, UserContentConfig config) {
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

			mapMsg.put("productid", meta.getReferenceId());
			mapMsg.put("applicationname", meta.getApplication().getName());

			if(backupPath != null)
				mapMsg.put("contentpath", backupPath.getAbsolutePath());

			mapMsg.put("success", Boolean.toString(success));
			
			mapMsg.put("appid", meta.getApplication().getProductId());

			meta.getApplication().accept(new ApplicationVisitor<Void>() {
				@Override
				public Void visit(CdsApplication app) {	return null; }

				@Override
				public Void visit(ScpApplication app) {	return null; }

				@Override
				public Void visit(DpsApplication app) {
					DpsServer server = app.getServer();
					
					mapMsg.put("dpsaddress", server.getAddress());
					mapMsg.put("dpsusername", server.getDpsUsername());
					mapMsg.put("dpspassword", server.getDpsPassword());
					mapMsg.put("dpsconsumerkey", server.getConsumerKey());
					mapMsg.put("dpsconsumersecret", server.getConsumerSecret());
					
					
					if(meta.getFolio() != null) 
						mapMsg.put("folioid", meta.getFolio().getFolioId());
					
					return null; 
				}
			});
			
			
			log.debug("Sending message:" + mapMsg);

			try {
				sender.sendMessage(mapMsg);
			} catch(Exception ex) {
				log.fatal("Failed to send jms message", ex);
			}
		}
	}
	
	@Override
	protected void finalizeSuccess(IssueMeta meta, PackageInfo data) {
		sendMsgIfNeeded(meta, true);
		meta.contentUploaded(data.getSize());
	}
	
	@Override
	protected IssueMetaEvent getWorkFlowEvent(IssueMeta meta, String reason) {
		if(meta.isContentUploaded()) 
			return new IssueMetaEvent.NewContent(config.getUser(), new Date(), reason, meta);
		else 
			return new IssueMetaEvent.UpdateContent(config.getUser(), new Date(), reason, meta);
	}
}
