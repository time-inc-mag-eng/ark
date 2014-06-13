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

/**
 * Value class that contains LDAP server information.
 */
public class LDAPServerInfo {
	private final String host;
	private final String domain;
	private final String userSearchBase;
	private final String adminRole;
	private final String superAdminRole;
	private final String issueRole;
	private final String mediaRole;
	
	/**
	 * Instantiates a new LDAP server info.
	 *
	 * @param host the host
	 * @param domain the domain
	 * @param userSearchBase the user search base
	 */
	public LDAPServerInfo(String host, String domain, String userSearchBase,
			String adminRole, String superAdminRole, String issueRole,
			String mediaRole) {
		super();
		this.host = host;
		this.domain = domain;
		this.userSearchBase = userSearchBase;
		this.adminRole = adminRole;
		this.superAdminRole = superAdminRole;
		this.issueRole = issueRole;
		this.mediaRole = mediaRole;
	}

	/**
	 * Gets the host.
	 *
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Gets the domain.
	 *
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Gets the user search base.
	 *
	 * @return the user search base
	 */
	public String getUserSearchBase() {
		return userSearchBase;
	}

	public String getAdminRole() {
		return adminRole;
	}

	public String getSuperAdminRole() {
		return superAdminRole;
	}

	public String getIssueRole() {
		return issueRole;
	}

	public String getMediaRole() {
		return mediaRole;
	}
}
