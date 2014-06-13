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
package com.timeInc.ark.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.naming.Naming;
import com.timeInc.mageng.util.email.EmailProcessor;
import com.timeInc.mageng.util.string.StringUtil;

/**
 * Sends an email to recipients using {@link Naming} for the body and subject.
 */
public class TemplateEmail {
	private static final Logger log = Logger.getLogger(TemplateEmail.class);

	private int id;

	private String receipients;

	private final EmailProcessor emailSender;
	private String emailFrom;
	private String smtpHost;

	private Naming body;
	private Naming subject;

	
	/**
	 * Instantiates a new template email.
	 *
	 * @param emailSender the email sender
	 * @param emailFrom from
	 * @param smtpHost smtp host
	 */
	public TemplateEmail(EmailProcessor emailSender, String emailFrom,
			String smtpHost) {
		this.emailSender = emailSender;
		this.emailFrom = emailFrom;
		this.smtpHost = smtpHost;
	}

	/**
	 * Instantiates a new template email.
	 *
	 * @param emailFrom the email from
	 * @param smtpHost the smtp host
	 */
	public TemplateEmail(String emailFrom, String smtpHost) {
		this(EmailProcessor.getInstance(), emailFrom, smtpHost);
	}

	/**
	 * Instantiates a new template email.
	 */
	public TemplateEmail() {
		this(null, null);
	}

	/**
	 * Send email using for an IssueMeta using the provided
	 * naming.
	 *
	 * @param meta the meta
	 */
	public void sendEmail(IssueMeta meta) {
		if(StringUtil.isEmpty(receipients)) {
			log.warn("sendEmail(IssueMeta meta) method invocation but no receipients!!");
		} else {
			List<String> receipientsTokenized = getRecipients(receipients);

			log.debug("Sending email to: " + receipientsTokenized);

			String bodyStr = body.getName(meta);
			String subStr = subject.getName(meta);

			log.debug("Subject:" + subStr);
			log.debug("Body:" + bodyStr);

			emailSender.sendEmail(receipientsTokenized, subStr, bodyStr, emailFrom, smtpHost);
		}
	}

	private static List<String> getRecipients(String emailList) {
		// split the email string with "," as separator
		String[] recipientsArray = emailList.split(",");

		//create the list from array
		List<String> recipients = new ArrayList<String>();
		for(int i=0; i<recipientsArray.length; i++){
			// remove the leading and trailing white space
			String email = recipientsArray[i].trim();
			recipients.add(email);
		}
		/* if size or recipients is zero then return null*/
		if(recipients.size() < 1) return null;
		return recipients;
	}	
	
	/**
	 * Sets the receipients.
	 *
	 * @param receipients the new receipients
	 */
	public void setReceipients(String receipients) {
		this.receipients = receipients;
	}

	/**
	 * Sets the body.
	 *
	 * @param body the new body
	 */
	public void setBody(Naming body) {
		this.body = body;
	}

	/**
	 * Sets the subject.
	 *
	 * @param subject the new subject
	 */
	public void setSubject(Naming subject) {
		this.subject = subject;
	}

	/**
	 * Gets the receipients.
	 *
	 * @return the receipients
	 */
	public String getReceipients() {
		return receipients;
	}

	/**
	 * Gets the smtp host.
	 *
	 * @return the smtp host
	 */
	public String getSmtpHost() {
		return smtpHost;
	}

	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public Naming getBody() {
		return body;
	}

	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public Naming getSubject() {
		return subject;
	}

	/**
	 * Sets the smtp host.
	 *
	 * @param host the new smtp host
	 */
	public void setsmtpHost(String host) {
		this.smtpHost = host;
	}

	/**
	 * Sets the email from.
	 *
	 * @param emailFrom the new email from
	 */
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}	
}
