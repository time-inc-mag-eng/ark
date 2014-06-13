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
package com.timeInc.ark.issue;


/**
 * The payment type that is associated with an {@link IssueMeta}
 */
public class PaymentType implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	/** The Constant PAID. */
	public static final String PAID = "Paid";
	
	/** The Constant FREE. */
	public static final String FREE = "Free";
	
	/** The Constant SUBSCRIPTION. */
	public static final String SUBSCRIPTION = "Subscription";
	
	private int id;
	
	private String name;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
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
	 * Checks if is retail.
	 *
	 * @return true, if is retail
	 */
	public boolean isRetail() {
		if(name.equals(FREE))
				return false;
		else if(name.equals(SUBSCRIPTION) || name.equals(PAID)) 
			return true;
		else 
			throw new IllegalArgumentException("Can not determine whether content is free for payment type:" + name);
	}
}
