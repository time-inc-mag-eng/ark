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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.timeInc.mageng.util.misc.Precondition;

/**
 * Read Ark's application settings from a Java properties file.
 */
public class GlobalSettingFileReader {
	private static final Logger log = Logger.getLogger(GlobalSettingFileReader.class);

	private static final String LDAP_HOST = "ldap-host";
	private static final String LDAP_DOMAIN = "ldap-domain";
	private static final String LDAP_USER_BASE = "ldap-user-base";
	
	private static final String LDAP_ADMIN_ROLE = "ldap-admin-role";
	private static final String LDAP_SUPERADMIN_ROLE = "ldap-superadmin-role";
	private static final String LDAP_ISSUE_ROLE = "ldap-issue-role";
	private static final String LDAP_MEDIA_ROLE = "ldap-meda-role";

	private static final String UPLOAD_DIRECTORY = "upload-directory";

	private static final String KIOSK_URL = "kiosk-url";
	
	private static final String DPS_URL = "dps-apiurl";
	private static final String DPS_CONSUMERSECRET ="dps-consumersecret";
	private static final String DPS_CONSUMERKEY= "dps-consumerkey";
	private static final String DPS_RETRYCOUNT="dps-retrycount";

	private static final String SMTP_HOST = "smtp-host";
	private static final String SMTP_SENDER = "email-sender";

	private static final String CDP_ENDPOINT = "cdp-endpoint";
	private static final String CDP_USERNAME = "cdp-username";
	private static final String CDP_PASSWORD = "cdp-password";
	private static final String CDP_DEVICE_ID = "cdp-deviceid";

	private static final String ORIGIN_NOTIFY = "origin-notify-emails";

	private static final String PUSH_SERVER = "push-server";

	private static final String BACKUP_LOCATION = "upload-backup-path";

	private static final String JMS_PUBLISH_BROKER_FOLIO_URL = "jms-folio-publish-broker-url";
	private static final String JMS_PUBLISH_FOLIO_QUEUE = "jms-folio-publish-queue";
	
	private static final String JMS_UNPUBLISH_BROKER_FOLIO_URL = "jms-folio-unpublish-broker-url";
	private static final String JMS_UNPUBLISH_FOLIO_QUEUE = "jms-folio-unpublish-queue";
	

	private static final String JMS_BROKER_ARK_URL = "jms-ark-broker-url";
	private static final String JMS_ISSUE_META_CHANGE_TOPIC = "jms-ark-meta-change-topic";
	private static final String JMS_CONTENT_UPLOAD_TOPIC = "jms-ark-content-upload-topic";
	
	private static final String JMS_PREVIEW_UPLOAD_TOPIC = "jms-ark-preview-upload-topic";
	
	
	private static final String LOG4J_PROPERTIES_FILE = "log-file";

	
	/**
	 * Load the setting from a JAVA properties file.
	 *
	 * @param propertyFile the property file
	 * @return ark settings
	 * @throws IllegalArgumentException if a file property does not exist
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static GlobalSetting getSettingFrom(File propertyFile) throws IOException {
		log.debug("Getting config file from " + propertyFile);

		InputStream is = null;

		try {
			is = new FileInputStream(propertyFile);
			Properties properties = new Properties();
			properties.load(is);
			
			GlobalSetting setting = new GlobalSetting(
					
					new LDAPServerInfo(properties.getProperty(LDAP_HOST), properties.getProperty(LDAP_DOMAIN), properties.getProperty(LDAP_USER_BASE),
							properties.getProperty(LDAP_ADMIN_ROLE), properties.getProperty(LDAP_SUPERADMIN_ROLE), properties.getProperty(LDAP_ISSUE_ROLE), properties.getProperty(LDAP_MEDIA_ROLE)),
					Precondition.checkFileExists(new File(properties.getProperty(UPLOAD_DIRECTORY))), 
					properties.getProperty(KIOSK_URL),
					properties.getProperty(SMTP_HOST), properties.getProperty(SMTP_SENDER),
					properties.getProperty(CDP_ENDPOINT), properties.getProperty(CDP_DEVICE_ID), properties.getProperty(CDP_USERNAME),
					properties.getProperty(CDP_PASSWORD), properties.getProperty(ORIGIN_NOTIFY), 
					properties.getProperty(PUSH_SERVER), 
					Precondition.checkFileExists(new File(properties.getProperty(BACKUP_LOCATION))),
					properties.getProperty(JMS_UNPUBLISH_BROKER_FOLIO_URL), properties.getProperty(JMS_UNPUBLISH_FOLIO_QUEUE),
					properties.getProperty(JMS_PUBLISH_BROKER_FOLIO_URL),
					properties.getProperty(JMS_PUBLISH_FOLIO_QUEUE), properties.getProperty(JMS_BROKER_ARK_URL),
					properties.getProperty(JMS_ISSUE_META_CHANGE_TOPIC), properties.getProperty(JMS_CONTENT_UPLOAD_TOPIC),
					Precondition.checkFileExists(new File(properties.getProperty(LOG4J_PROPERTIES_FILE))),
					properties.getProperty(JMS_PREVIEW_UPLOAD_TOPIC),
					properties.getProperty(DPS_CONSUMERSECRET), properties.getProperty(DPS_CONSUMERKEY), properties.getProperty(DPS_URL),
					properties.getProperty(DPS_RETRYCOUNT));
			
			log.info("Loaded ark setting ==> " + properties);
			
			return setting;
			
		} finally {
			if(is != null) is.close();
		}


	}
}
