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
package com.timeInc.ark.naming;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.generic.DateTool;

import com.timeInc.ark.issue.IssueMeta;

/**
 * Names an {@link IssueMeta} using a velocity template. The following
 * velocity variables are exposed:
 * pub - the publication name
 * issue - the issue name
 * saledate - the sale date, can be formated using the same pattern as {@link SimpleDateFormat}
 * shortpub - the publication name as it appears to vendors
 * app - the application name
 * shortdate - the short date, can be formatted using the same pattern as {@link SimpleDateFormat}
 * 
 * 
 */
public class VelocityNaming implements Naming {
	private enum NamingToken {
		PUB("pub"), ISSUE("issue"), SALE_DATE("saledate"), 
		SHORT_PUB("shortpub"),
		SHORT_DATE("shortdate"), APP("app");

		private final String token;

		private NamingToken(String token) {
			this.token = token;
		}

		@Override
		public String toString() {
			return token;
		}
	}

	private String template;


	/**
	 * Instantiates a new velocity naming.
	 *
	 * @param template the template
	 */
	public VelocityNaming(String template) {
		this.template = template;
	}

	protected VelocityNaming() {};

	/* (non-Javadoc)
	 * @see com.timeInc.ark.naming.Naming#getName(com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public String getName(final IssueMeta meta) {
		Properties velLog4j = new Properties();
		velLog4j.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogSystem" );
		
		VelocityEngine ve = new VelocityEngine(velLog4j); 
		
		VelocityContext context = new VelocityContext();

		context.put("date", new DateTool());
		
		context.put(NamingToken.PUB.toString(), meta.getPublication().getName());
		context.put(NamingToken.ISSUE.toString(), meta.getIssueName());
		context.put(NamingToken.SALE_DATE.toString(), meta.getOnSaleDate());
		context.put(NamingToken.SHORT_PUB.toString(), meta.getApplication().getVendorPubName());
		context.put(NamingToken.SHORT_DATE.toString(), meta.getCoverDate());
		context.put(NamingToken.APP.toString(), meta.getApplicationName());

		StringWriter writer = new StringWriter();
		
		ve.evaluate(context, writer, "TemplateName", template);

		return writer.getBuffer().toString();
	}
	
	/**
	 * Gets the template.
	 *
	 * @return the template
	 */
	public String getTemplate() { return template; }

	/**
	 * Sets the template.
	 *
	 * @param template the new template
	 */
	public void setTemplate(String template) {
		this.template = template;
	} 
}

