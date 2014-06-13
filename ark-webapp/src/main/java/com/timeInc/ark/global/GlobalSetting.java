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
package com.timeInc.ark.global;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.timeInc.ark.api.dps.ArkFolioClientFactory;
import com.timeInc.ark.api.push.DelayedPushFactory;
import com.timeInc.ark.api.ww.CdsConfig;
import com.timeInc.ark.backup.LocalStorage;
import com.timeInc.ark.backup.Storage;
import com.timeInc.ark.event.AbstractEvent;
import com.timeInc.ark.event.IssueEvent;
import com.timeInc.ark.event.PersistentLogEventListener;
import com.timeInc.ark.event.IssueMetaEvent.Creation;
import com.timeInc.ark.event.IssueMetaEvent.DeleteFolio;
import com.timeInc.ark.event.IssueMetaEvent.Deletion;
import com.timeInc.ark.event.IssueMetaEvent.NewContent;
import com.timeInc.ark.event.IssueMetaEvent.NewPreview;
import com.timeInc.ark.event.IssueMetaEvent.PublishContent;
import com.timeInc.ark.event.IssueMetaEvent.PushSchedule;
import com.timeInc.ark.event.IssueMetaEvent.UnpublishFolio;
import com.timeInc.ark.event.IssueMetaEvent.Update;
import com.timeInc.ark.event.IssueMetaEvent.UpdateContent;
import com.timeInc.ark.event.IssueMetaEvent.UpdatePreview;
import com.timeInc.ark.media.MediaPathNaming;
import com.timeInc.ark.media.PseudoUniqueMediaNaming;
import com.timeInc.ark.media.uploader.MediaUploadListener;
import com.timeInc.ark.media.uploader.PersistentMediaLocationListener;
import com.timeInc.ark.newsstand.NewsstandFeedFactory;
import com.timeInc.ark.publish.FolioPublisher;
import com.timeInc.ark.publish.JMSFolioPublisher;
import com.timeInc.ark.role.LDAPAuthentication;
import com.timeInc.ark.upload.PostUploadListener;
import com.timeInc.ark.upload.PostUploadListener.WorkingDirectoryCleaner;
import com.timeInc.ark.util.JMSMsgQueue;
import com.timeInc.ark.util.JMSTopicSender;
import com.timeInc.mageng.util.event.EventManager;
import com.timeInc.mageng.util.event.ThreadSafeEventManager;
import com.timeInc.mageng.util.progress.concurrent.ProgressCompletionExecutor;
import com.timeInc.mageng.util.progress.concurrent.cache.CacheExecutor;


/**
 * Ark's application setting.
 */
public class GlobalSetting {
	private static volatile HttpClient client = null;
	
	private final File uploadDirectory;
	
	private final String pushServer;
	
	private final EmailServerInfo emailInfo;
	
	private final CdsConfig.Connection wwConnectionInfo;
	
	private final String originNotifyEmails;
	
	private final DelayedPushFactory echoFactory;
	
	private final LDAPAuthentication userFactory;
	
	private final PostUploadListener postUploadListener;
	
	private final Storage backupStorage;
	private final File rootBackupDir;
	
	private final JMSTopicSender metaJms;
	
	private final JMSTopicSender contentJms;
	
	private final JMSTopicSender previewJms;
	
	private final NewsstandFeedFactory nsFactory;
	
	private final EventManager<Class<? extends AbstractEvent>, AbstractEvent> wfEvent;
	
	private final MediaPathNaming mediaNaming;
	
	private final MediaUploadListener mediaListener;
	
	private final FolioPublisher folioPublisher;
	
	private final HttpClient httpClient;
	
	private final ProgressCompletionExecutor completionExecutor;
	private final CacheExecutor asyncExecutor;
	
	private final ArkFolioClientFactory folioClientFactory;
	
	private final DefaultSetting defaultSetting;
	
	/**
	 * Instantiates a new global setting.
	 *
	 * @param ldapInfo the ldap info
	 * @param uploadDirectory the upload directory
	 * @param emailInfo the email info
	 * @param originNotifyEmails the origin notify emails
	 * @param wwConnectionInfo the ww connection info
	 * @param pushServer the push server
	 * @param echoFactory the echo factory
	 * @param postUploadListener the post upload listener
	 * @param rootBackupDir the root backup dir
	 * @param backupStorage the backup storage
	 * @param metaJms the meta jms
	 * @param contentJms the content jms
	 * @param previewJms the preview jms
	 * @param wfEvent the wf event
	 * @param nsFactory the ns factory
	 * @param mediaNaming the media naming
	 * @param mediaListener the media listener
	 * @param folioPublisher the folio publisher
	 * @param logProperties the log properties
	 * @param httpClient the http client
	 * @param completionExecutor the completion executor
	 * @param asyncExecutor the async executor
	 * @param defaultSetting the default setting
	 */
	@SuppressWarnings("unchecked")
	public GlobalSetting(
			LDAPServerInfo ldapInfo,
			File uploadDirectory, 
			EmailServerInfo emailInfo, 
			String originNotifyEmails,
			CdsConfig.Connection wwConnectionInfo,
			String pushServer,
			DelayedPushFactory echoFactory,
			PostUploadListener postUploadListener,
			File rootBackupDir, Storage backupStorage, 
			JMSTopicSender metaJms,	JMSTopicSender contentJms, JMSTopicSender previewJms,
			EventManager<Class<? extends AbstractEvent>, AbstractEvent> wfEvent, 
			NewsstandFeedFactory nsFactory,
			MediaPathNaming mediaNaming, MediaUploadListener mediaListener,
			FolioPublisher folioPublisher,
			File logProperties,
			HttpClient httpClient,
			ProgressCompletionExecutor completionExecutor,
			CacheExecutor asyncExecutor,
			DefaultSetting defaultSetting) {
		
		this.uploadDirectory = uploadDirectory;
		this.emailInfo = emailInfo;
		this.originNotifyEmails = originNotifyEmails;
		this.wwConnectionInfo = wwConnectionInfo;
		this.echoFactory = echoFactory;
		this.userFactory = new LDAPAuthentication(ldapInfo);
		this.postUploadListener = postUploadListener;
		this.backupStorage = backupStorage;
		this.metaJms = metaJms;
		this.contentJms = contentJms;
		this.wfEvent = wfEvent;
		this.rootBackupDir = rootBackupDir;
		this.nsFactory = nsFactory;
		this.mediaListener = mediaListener;
		this.mediaNaming = mediaNaming;
		this.folioPublisher = folioPublisher;
		this.previewJms = previewJms;
		this.httpClient = httpClient;
		this.completionExecutor = completionExecutor;
		this.asyncExecutor = asyncExecutor;
		this.folioClientFactory = new ArkFolioClientFactory(httpClient);
		this.defaultSetting = defaultSetting;
		this.pushServer = pushServer;
		
		wfEvent.register(new PersistentLogEventListener(), Update.class, Creation.class, Deletion.class,
				NewContent.class, UpdateContent.class, 
				NewPreview.class, UpdatePreview.class,
				PublishContent.class, PushSchedule.class,
				IssueEvent.MediaUpload.class,
				DeleteFolio.class, UnpublishFolio.class);
		
		
		PropertyConfigurator.configureAndWatch(logProperties.getAbsolutePath());
		
	}

	/**
	 * Instantiates a new global setting.
	 * @param ldap the ldap server information
	 * @param uploadDirectory the upload directory
	 * @param kioskEndpoint the kiosk endpoint
	 * @param smtpHost the smtp host
	 * @param smtpSender the smtp sender
	 * @param cdsEndpoint the cds endpoint
	 * @param cdsDeviceId the cds device id
	 * @param cdsUserName the cds user name
	 * @param cdsPassword the cds password
	 * @param originNotifyEmail the origin notify email
	 * @param pushServer the push server
	 * @param backupDirectory the backup directory
	 * @param jmsUnPublishHost the jms un publish host
	 * @param jmsUnPublishQueue the jms un publish queue
	 * @param jmsPublishHost the jms publish host
	 * @param jmsPublishQueue the jms publish queue
	 * @param jmsArkUrl the jms ark url
	 * @param jmsArkMetaChangeTopic the jms ark meta change topic
	 * @param jmsArkContentUploadTopic the jms ark content upload topic
	 * @param logProperties the log properties
	 * @param jmsArkPreviewUploadTopic the jms ark preview upload topic
	 * @param consumersecret the consumersecret
	 * @param consumerkey the consumerkey
	 * @param apiurl the apiurl
	 * @param retrycount the retrycount
	 */
	public GlobalSetting(LDAPServerInfo ldap,
			File uploadDirectory, String kioskEndpoint, String smtpHost, String smtpSender,
			String cdsEndpoint, String cdsDeviceId,
			String cdsUserName, String cdsPassword, String originNotifyEmail,
			String pushServer, File backupDirectory, 
			String jmsUnPublishHost, String jmsUnPublishQueue, 
			String jmsPublishHost, String jmsPublishQueue, 
			String jmsArkUrl, String jmsArkMetaChangeTopic, String jmsArkContentUploadTopic,
			File logProperties,
			String jmsArkPreviewUploadTopic, 
			String consumersecret, String consumerkey,String apiurl,
			String retrycount) {
		
		this(ldap,
				uploadDirectory,
				new EmailServerInfo(smtpHost, smtpSender),
				originNotifyEmail,
				new CdsConfig.Connection(kioskEndpoint, cdsDeviceId, cdsEndpoint, cdsUserName, cdsPassword),
				pushServer, new DelayedPushFactory(),
				new WorkingDirectoryCleaner(),
				backupDirectory, new LocalStorage(), 
				new JMSTopicSender(jmsArkUrl, jmsArkMetaChangeTopic),
				new JMSTopicSender(jmsArkUrl, jmsArkContentUploadTopic),
				new JMSTopicSender(jmsArkUrl, jmsArkPreviewUploadTopic),
				new ThreadSafeEventManager<Class<? extends AbstractEvent>, AbstractEvent>(),
				new NewsstandFeedFactory(),
				new PseudoUniqueMediaNaming(),
				new PersistentMediaLocationListener(),
				new JMSFolioPublisher(new JMSMsgQueue(jmsPublishHost, jmsPublishQueue), new JMSMsgQueue(jmsUnPublishHost, jmsUnPublishQueue)),
				logProperties,
				getPooledHttpClient(),
				new ProgressCompletionExecutor(Executors.newSingleThreadScheduledExecutor()),
				new CacheExecutor(),
				new DefaultSetting(consumersecret, consumerkey, apiurl,retrycount));
	}
	
	
	private static HttpClient getPooledHttpClient() {
		if(client == null) {
			synchronized(GlobalSetting.class) {
				if(client == null) {
					PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
					connectionManager.setMaxTotal(500);
					connectionManager.setDefaultMaxPerRoute(500);
					
					DefaultHttpClient defaultclient = new DefaultHttpClient(connectionManager);
					
					defaultclient.setHttpRequestRetryHandler(new HttpRequestRetryHandler() {
					    @Override
					    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
					    	return false; // IMPORTANT: the DPS client throws an exception when no response is received; 
					    	// returning true will concurrently retry the request but ark has no idea this is going on....
					      }
					   }); 
					
					client = defaultclient;
				}
			}
		}		
		return client;
	}	
	
	/**
	 * Clean up.
	 */
	@SuppressWarnings("deprecation")
	public void cleanUp() {
		Logger.getRootLogger().info("Terminating globally managed objects");
		
		httpClient.getConnectionManager().shutdown();
		
		completionExecutor.terminate();
		asyncExecutor.terminate();
		
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getName().equals("FileWatchdog")) { // FileWatchDog swallows interrupt this is a hack
				t.stop();
			}
		}
	}
	
	/**
	 * Gets the folio client factory.
	 *
	 * @return the folio client factory
	 */
	public ArkFolioClientFactory getFolioClientFactory() {
		return folioClientFactory;
	}

	/**
	 * Gets the completion executor.
	 *
	 * @return the completion executor
	 */
	public ProgressCompletionExecutor getCompletionExecutor() {
		return completionExecutor;
	}

	/**
	 * Gets the async executor.
	 *
	 * @return the async executor
	 */
	public CacheExecutor getAsyncExecutor() {
		return asyncExecutor;
	}

	/**
	 * Gets the work flow event.
	 *
	 * @return the work flow event
	 */
	public EventManager<Class<? extends AbstractEvent>, AbstractEvent> getWorkFlowEvent() {
		return wfEvent;
	}

	/**
	 * Gets the upload directory.
	 *
	 * @return the upload directory
	 */
	public File getUploadDirectory() {
		return uploadDirectory;
	}


	/**
	 * Gets the email info.
	 *
	 * @return the email info
	 */
	public EmailServerInfo getEmailInfo() {
		return emailInfo;
	}

	/**
	 * Gets the origin notify emails.
	 *
	 * @return the origin notify emails
	 */
	public String getOriginNotifyEmails() {
		return originNotifyEmails;
	}

	/**
	 * Gets the folio publisher.
	 *
	 * @return the folio publisher
	 */
	public FolioPublisher getFolioPublisher() {
		return folioPublisher;
	}

	/**
	 * Gets the echo factory.
	 *
	 * @return the echo factory
	 */
	public DelayedPushFactory getEchoFactory() {
		return echoFactory;
	}

	/**
	 * Gets the user factory.
	 *
	 * @return the user factory
	 */
	public LDAPAuthentication getUserFactory() {
		return userFactory; 
	}

	/**
	 * Gets the post upload listener.
	 *
	 * @return the post upload listener
	 */
	public PostUploadListener getPostUploadListener() {
		return postUploadListener;
	}
	

	/**
	 * Gets the backup storage.
	 *
	 * @return the backup storage
	 */
	public Storage getBackupStorage() {
		return backupStorage;
	}

	/**
	 * Gets the preview upload msger.
	 *
	 * @return the preview upload msger
	 */
	public JMSTopicSender getPreviewUploadMsger() {
		return previewJms;
	}
	
	/**
	 * Gets the data change msger.
	 *
	 * @return the data change msger
	 */
	public JMSTopicSender getDataChangeMsger() {
		return metaJms;
	}

	/**
	 * Gets the content upload msger.
	 *
	 * @return the content upload msger
	 */
	public JMSTopicSender getContentUploadMsger() {
		return contentJms;
	}

	/**
	 * Gets the root backup dir.
	 *
	 * @return the root backup dir
	 */
	public File getRootBackupDir() {
		return rootBackupDir;
	}

	/**
	 * Gets the ns factory.
	 *
	 * @return the ns factory
	 */
	public NewsstandFeedFactory getNsFactory() {
		return nsFactory;
	}

	/**
	 * Gets the media naming.
	 *
	 * @return the media naming
	 */
	public MediaPathNaming getMediaNaming() {
		return mediaNaming;
	}

	/**
	 * Gets the media listener.
	 *
	 * @return the media listener
	 */
	public MediaUploadListener getMediaListener() {
		return mediaListener;
	}

	/**
	 * Gets the default setting.
	 *
	 * @return the default setting
	 */
	public DefaultSetting getDefaultSetting() {
		return defaultSetting;
	}

	/**
	 * Gets the ww connection info.
	 *
	 * @return the ww connection info
	 */
	public CdsConfig.Connection getWwConnectionInfo() {
		return wwConnectionInfo;
	}

	/**
	 * Gets the http client.
	 *
	 * @return the http client
	 */
	public HttpClient getHttpClient() {
		return httpClient;
	}
	
	/**
	 * Gets the push server.
	 *
	 * @return the push server
	 */
	public String getPushServer() {
		return pushServer;
	}
}
