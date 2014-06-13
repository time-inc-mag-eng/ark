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

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.timeInc.ark.global.GlobalSetting;


/**
 * Utility class for getting a Hibernate session.
 */
public class HibernateUtil {
	private static final Logger log = Logger.getLogger(HibernateUtil.class);
	
	private static volatile SessionFactory sessionFactory = null;
	
	/**
	 * Builds the session factory.
	 *
	 * @param arkSetting the ark setting
	 * @return the session factory
	 */
	public static SessionFactory buildSessionFactory(GlobalSetting arkSetting) {
		synchronized(HibernateUtil.class) {
			if(sessionFactory == null) {
				log.debug("Building session factory...");
				
				try {
					Configuration config = new Configuration();
					config.configure("hibernate.cfg.xml");
					
					config.registerTypeOverride(new UtcTimestampType());
					config.setInterceptor(new HibernateDependencyInjector(arkSetting));
					sessionFactory =  config.buildSessionFactory();
				} catch (Throwable ex) {
					log.error("Initial SessionFactory creation failed.",ex);
					throw new ExceptionInInitializerError(ex);
				}
			}
		}

		return sessionFactory;
	}
	
	/**
	 * Gets the new session.
	 *
	 * @return the new session
	 */
	public static Session getNewSession() {
		return getSessionFactory().openSession();
	}
	
	/**
	 * Gets the session.
	 *
	 * @return the session
	 */
	public static Session getSession() {
		return getSessionFactory().getCurrentSession();
	}
	
	/**
	 * Gets the session factory.
	 *
	 * @return the session factory
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
 
	/**
	 * Shutdown.
	 */
	public static void shutdown() {
		getSessionFactory().close();
	}
}

