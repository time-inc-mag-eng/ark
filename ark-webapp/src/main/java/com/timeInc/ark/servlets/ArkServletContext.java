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
package com.timeInc.ark.servlets;

import java.io.File;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.timeInc.ark.backup.ContentBackup;
import com.timeInc.ark.backup.DirectCopyBackup;
import com.timeInc.ark.backup.LocalStorage;
import com.timeInc.ark.global.GlobalSetting;
import com.timeInc.ark.global.GlobalSettingFileReader;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.media.ConcurrentMediaUploader;
import com.timeInc.ark.media.uploader.MediaFacadeBuilderFactory;
import com.timeInc.ark.upload.ConcurrentUploader.ConcurrentStatusUploader;
import com.timeInc.ark.upload.UploadCommandFactory;
import com.timeInc.ark.upload.content.ConcurrentContentUploader;
import com.timeInc.ark.upload.content.ContentFacadeFactoryProducer;
import com.timeInc.ark.upload.preview.PreviewFacadeFactoryProducer;
import com.timeInc.ark.util.hibernate.HibernateUtil;
import com.timeInc.mageng.util.compression.Zip;

/**
 * Servlet context that performs shared object initialization and cleanup.
 */
public class ArkServletContext implements ServletContextListener {

	/** The Constant CONTENT_EXECUTOR. */
	public static final String CONTENT_EXECUTOR = "contentExecutor";
	
	/** The Constant PREVIEW_EXECUTOR. */
	public static final String PREVIEW_EXECUTOR = "previewExecutor";
	
	/** The Constant ARK_SETTING_ATTRIBUTE. */
	public static final String ARK_SETTING_ATTRIBUTE = "arkSetting";

	/** The Constant MEDIA_EXECUTOR. */
	public static final String MEDIA_EXECUTOR = "mediaExecutor";
	
	private static final String JNDI_PROPERTY_KEY = "configFile";

	private volatile boolean init = false;

	/**
	 * Instantiates a new ark servlet context.
	 */
	public ArkServletContext() {}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		GlobalSetting setting = loadGlobalConfigSetting(); 


		HibernateUtil.buildSessionFactory(setting); // load session factory using the global settings

		setAttribute(contextEvent, ARK_SETTING_ATTRIBUTE, setting);

		setAttribute(contextEvent, PREVIEW_EXECUTOR, 
				new ConcurrentStatusUploader(	setting.getCompletionExecutor(),
												setting.getPostUploadListener(), 
												new UploadCommandFactory<IssueMeta>(new PreviewFacadeFactoryProducer(setting.getWorkFlowEvent(), new DirectCopyBackup(LocalStorage.getPreviewLocation(setting.getRootBackupDir()), setting.getBackupStorage()), setting.getPreviewUploadMsger()))));

		setAttribute(contextEvent, CONTENT_EXECUTOR,
				new ConcurrentContentUploader(	setting.getAsyncExecutor(), 
												setting.getPostUploadListener(), 
												new UploadCommandFactory<IssueMeta>(new ContentFacadeFactoryProducer(setting.getWorkFlowEvent(), setting.getContentUploadMsger(), new ContentBackup(LocalStorage.getContentLocation(setting.getRootBackupDir()), setting.getBackupStorage(), new Zip())))));
		
		setAttribute(contextEvent, MEDIA_EXECUTOR,
				new ConcurrentMediaUploader(	setting.getAsyncExecutor(), 
												setting.getPostUploadListener(), 
												new UploadCommandFactory<Issue>(new MediaFacadeBuilderFactory(setting.getWorkFlowEvent()))));

		

		init = true;
	}

	private static void setAttribute(ServletContextEvent contextEvent, String attribName, Object o) {
		contextEvent.getServletContext().setAttribute(attribName, o);
	}
	
	private GlobalSetting loadGlobalConfigSetting() {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			String propFilePath = (String) envCtx.lookup(JNDI_PROPERTY_KEY);

			return GlobalSettingFileReader.getSettingFrom(new File(propFilePath));
			
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}	
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {

		if (init) {
			HibernateUtil.shutdown();
			GlobalSetting setting = (GlobalSetting) contextEvent.getServletContext().getAttribute(ARK_SETTING_ATTRIBUTE);
			setting.cleanUp();
		}
	}
}
