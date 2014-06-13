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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A role in ark that is priority based where the highest priority
 * is entitled to everything that has a lower priority role. 
 * Depending on the role, certain operations can be allowed / disallowed.
 */
public enum Role {
	IssueManager(20), Media(50), User(100), Admin(200), SuperAdmin(300); 

	private int priority;

	Role(int priority) {
		this.priority = priority;
	}

	/**
	 * Gets the roles with same or higher.
	 *
	 * @param role the role
	 * @return the roles with same or higher
	 */
	public static List<Role> getRolesWithSameOrHigher(Role role) {
		List<Role> roles = new ArrayList<Role>();

		for(Role value : Role.values()) {
			if(value.priority >= role.priority)
				roles.add(value);
		}

		return roles;
	}

	/**
	 * Gets the lowest role.
	 *
	 * @return the lowest role
	 */
	public static Role getLowestRole() {
		Role lowestRole = User;
		for(Role role : Role.values()) {
			if(role.priority < lowestRole.priority)
				lowestRole = role;
		}

		return lowestRole;
	}

	/**
	 * Return the max role that is in strGroups and contains a mapping in roleMap
	 * @param strGroups a list of strings to be checked against roleMap
	 * @param roleMap a mapping between strings and roles
	 * @return the highest role; if no mapping is found the a default role of {@link Role.User} is used.
	 */
	static Role getMaxRole(Collection<String> strGroups,  Map<String, Role> roleMap) { // defaults to user role if no role is found
		boolean roleFound = false;

		Role maxRole = getLowestRole();

		for(String group : strGroups) {
			if(roleMap.containsKey(group)) {
				Role role = roleMap.get(group);
				roleFound = true;

				if(maxRole == null) {
					maxRole = role;
				} else if(maxRole.priority < role.priority) {
					maxRole  = role;
				}
			}
		}

		
		return roleFound ? maxRole : Role.User;
	}
}
