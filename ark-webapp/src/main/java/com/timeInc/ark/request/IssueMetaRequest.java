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
package com.timeInc.ark.request;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Value class that represents Create request for an IssueMeta
 */
public class IssueMetaRequest {
	/** The reference id. */
	public String referenceId;
	
	/** The on sale date. */
	public Date onSaleDate;
	
	/** The volume. */
	public String volume;
	
	/** The active. */
	public Boolean active;
	
	/** The price. */
	public BigDecimal price;
	
	/** The application id. */
	public Integer applicationId;
	
	/** The payment id. */
	public Integer paymentId;
	
	/** The issue name id. */
	public Integer issueNameId;
	
	/**
	 * Valute class that represents Delete or Update request for an IssueMeta
	 */
	public static class Persisted extends IssueMetaRequest {
		
		/** The id. */
		public Integer id;
	}
}
