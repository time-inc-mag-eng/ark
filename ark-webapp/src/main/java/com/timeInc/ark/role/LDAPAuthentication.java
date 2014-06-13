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
package com.timeInc.ark.role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timeInc.ark.dao.PublicationGroupDAO;
import com.timeInc.ark.dao.PublicationGroupDAO.Factory;
import com.timeInc.ark.global.LDAPServerInfo;
import com.timeInc.mageng.ldaputils.LDAPConnection;
import com.timeInc.mageng.ldaputils.LDAPUser;

/**
 * Authenticates via user/pass against an LDAP server.
 */
public class LDAPAuthentication {
	private final LDAPServerInfo ldapInfo;
	private final Map<String, Role> strToRole;

	/**
	 * Instantiates a new LDAP authentication.
	 *
	 * @param ldapInfo the ldap info
	 */
	public LDAPAuthentication(LDAPServerInfo ldapInfo) {
		this.ldapInfo = ldapInfo;
		
		strToRole = new HashMap<String, Role>();
		
		strToRole.put(ldapInfo.getAdminRole(), Role.Admin);
		strToRole.put(ldapInfo.getSuperAdminRole(), Role.SuperAdmin);
		strToRole.put(ldapInfo.getMediaRole(), Role.Media);
		strToRole.put(ldapInfo.getIssueRole(), Role.IssueManager);
	}
	

	/**
	 * Authenticates using user/pass via ldap to determine whether 
	 * access is permitted.
	 * @param user the user the username
	 * @param password the password the password
	 * @return the authenticated user otherwise null
	 */
	public User authenticate(String user, String password) {
		LDAPUser authUser = LDAPConnection.loginUser(ldapInfo.getHost(), ldapInfo.getDomain(), ldapInfo.getUserSearchBase(), 
				null, null, 
				user, password, 
				false);

		if(authUser != null) {
			Role role = Role.getMaxRole(authUser.getGroups(), strToRole);
			PublicationGroupDAO pgdao = Factory.getDao(role);
			List<PublicationGroup> groups = pgdao.findByAd(authUser.getGroups());

			return new User(authUser.getUsername(), authUser.getCommonName(), authUser.getEmail(), role, groups);
		} else {
			return null;
		}
	}
}
