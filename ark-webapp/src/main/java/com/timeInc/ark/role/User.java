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

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents a user who is entitled to accesa Ark
 */
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String userName;
	private final String name;
	private final Role role;
	private final String email;
	private final List<PublicationGroup> groups;
	
	/**
	 * Instantiates a new user.
	 *
	 * @param userName the user name
	 * @param name the name
	 * @param email the email
	 * @param role the role
	 * @param groups the groups
	 */
	public User(String userName, String name, String email, Role role,
			List<PublicationGroup> groups) {
		this.userName = userName;
		this.name = name;
		this.role = role;
		this.email = email;
		this.groups = groups;
	}
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}
	
	/**
	 * Gets the groups this user is entitled to
	 *
	 * @return the groups
	 */
	public List<PublicationGroup> getGroups() {
		return groups;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
