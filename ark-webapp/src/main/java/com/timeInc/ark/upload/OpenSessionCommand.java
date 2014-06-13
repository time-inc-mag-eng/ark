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
package com.timeInc.ark.upload;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.timeInc.ark.upload.UploadFacadeBuilderFactory.UploadFacadeBuilder;
import com.timeInc.ark.util.hibernate.HibernateUtil;
import com.timeInc.mageng.util.progress.DetailedProgressListener;
import com.timeInc.mageng.util.progress.ProgressableCommand;

/**
 * Open session pattern for Hibernate where the specified object is merged into the session and 
 * passed along to {@link com.timeInc.ark.upload.UploadFacade#handleUpload(DetailedProgressListener)}.
 * 
 * If {@link com.timeInc.ark.upload.UploadFacade#handleUpload(DetailedProgressListener)} throws an uncaught exception 
 * the transaction is rolled back and the {@link DetailedProgressListener} is ended with an "Internal Error" cause
 *
 * @param <T> the object to merge
 */
public class OpenSessionCommand<T> implements ProgressableCommand {	
	private static final Logger log = Logger.getLogger(OpenSessionCommand.class);

	private final T toMerge;
	private final UploadFacadeBuilder<T> builder;
	
	/**
	 * Instantiates a new open session delegator.
	 *
	 * @param toMerge the to merge
	 * @param builder the builder
	 */
	public OpenSessionCommand(T toMerge, UploadFacadeBuilder<T> builder) {
		this.toMerge = toMerge;
		this.builder = builder;
	}

	/* (non-Javadoc)
	 * @see com.timeInc.mageng.util.progress.ProgressableCommand#execute(com.timeInc.mageng.util.progress.DetailedProgressListener)
	 */
	@Override
	public void execute(DetailedProgressListener listener) {
		Session session = null;
		
		try {
			session = HibernateUtil.getSession();

			session.beginTransaction();
			
			T mergedDomain = (T) session.merge(toMerge);
			
			builder.getUploadFacadeFor(mergedDomain).handleUpload(listener);

			log.debug("Committing transaction");
			
			session.getTransaction().commit();

		} catch(Throwable e) {
			log.error("Error while processing the command - rolling back..", e);

			listener.ended(false, "Internal Error."); 

			try { 
				session.getTransaction().rollback();
			} catch(RuntimeException ex) {
				log.fatal(ex);
			}
		}
	}
}
