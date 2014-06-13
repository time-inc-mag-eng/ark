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
package com.timeInc.ark.util.hibernate;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.timeInc.ark.api.origin.OriginClient;
import com.timeInc.ark.api.push.DelayedPush;
import com.timeInc.ark.api.push.PushServiceLoader;
import com.timeInc.ark.api.ww.WoodwingServiceLoader;
import com.timeInc.ark.application.CdsApplication;
import com.timeInc.ark.global.GlobalSetting;
import com.timeInc.ark.media.uploader.OriginMediaUploader;
import com.timeInc.ark.newsstand.RemoteNewsstandFeed;
import com.timeInc.ark.uploader.DpsServer;
import com.timeInc.ark.uploader.OriginContentUploader;
import com.timeInc.ark.util.TemplateEmail;

/**
 * An interceptor that supplies a loaded Hibernate entity with dependencies that are required.
 */
public class HibernateDependencyInjector extends EmptyInterceptor {
	private static final long serialVersionUID = 1L;

	private final GlobalSetting setting;
	private static final WoodwingServiceLoader WW_SERVICE = new WoodwingServiceLoader();
	private static final PushServiceLoader PUSH_SERVICE = new PushServiceLoader();

	/**
	 * Instantiates a new hibernate dependency injector.
	 *
	 * @param arkSetting the ark setting
	 */
	public HibernateDependencyInjector(GlobalSetting arkSetting) {
		this.setting = arkSetting;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.EmptyInterceptor#onLoad(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		
		if(entity.getClass().getName().equals(OriginContentUploader.class.getName())) {
			OriginContentUploader server = (OriginContentUploader) entity;
			server.addRefreshContentEmail(setting.getOriginNotifyEmails());

		} else if(entity.getClass().getName().equals(CdsApplication.class.getName())) {
			CdsApplication cdsApp = (CdsApplication) entity;
			cdsApp.setWoodWingService(WW_SERVICE.getService(setting.getWwConnectionInfo()));
			
		} else if(DelayedPush.class.isInstance(entity)){
			@SuppressWarnings("rawtypes")
			DelayedPush push = (DelayedPush) entity;
			push.setPushService(PUSH_SERVICE.getService(setting.getPushServer(), push));
			
			push.setPushAccess(setting.getEchoFactory().getPushAccess());
			push.setScheduler(setting.getEchoFactory().getScheduler());
			push.setListener(setting.getEchoFactory().getEventListener(setting.getWorkFlowEvent()));
			
		} else if(entity.getClass().getName().equals(RemoteNewsstandFeed.class.getName())){
			RemoteNewsstandFeed ns = (RemoteNewsstandFeed) entity;
			ns.setNsFeedFactory(setting.getNsFactory());
			
		} else if(entity.getClass().getName().equals(TemplateEmail.class.getName())) {
			TemplateEmail tEmail = (TemplateEmail) entity;
			tEmail.setEmailFrom(setting.getEmailInfo().getSender());
			tEmail.setsmtpHost(setting.getEmailInfo().getHost());
			
		} else if(entity.getClass().getName().equals(OriginMediaUploader.class.getName())) {
			OriginMediaUploader mediaUploader = (OriginMediaUploader) entity;
			mediaUploader.setNaming(setting.getMediaNaming());
			mediaUploader.setUploadListener(setting.getMediaListener());
			
		} else if(entity.getClass().getName().equals(DpsServer.class.getName())) {
			DpsServer server = (DpsServer) entity;
			server.setClientFactory(setting.getFolioClientFactory());
			server.setPublisher(setting.getFolioPublisher());
		
		} else if(entity.getClass().getName().equals(OriginClient.class.getName())) {
			OriginClient client = (OriginClient) entity;
			client.setClient(setting.getHttpClient());
		}
		
		return super.onLoad(entity, id, state, propertyNames, types);
	}
}
