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
package com.timeInc.ark.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.timeInc.ark.application.Application;
import com.timeInc.ark.application.ApplicationEntity.ApplicationVisitor;
import com.timeInc.ark.application.CdsApplication;
import com.timeInc.ark.application.DpsApplication;
import com.timeInc.ark.application.ScpApplication;
import com.timeInc.ark.issue.IssueMeta;


/**
 * Response returned that represents an Application
 */
@SuppressWarnings("unused")
public class ApplicationResponse {
	private final Integer id;
	private final String name;
	private final String referenceId_Header;
	private final BigDecimal defaultPrice;
	private final int maxCoverStory;
	private final String type;
	
	ApplicationResponse(Application<?,?> app) {
		this.name = app.getName();
		this.id = app.getId();

		this.referenceId_Header = app.getProductId();

		this.defaultPrice = app.getDefaultPrice();
		this.maxCoverStory = app.getMaxCoverStory();
		
		this.type = app.accept(new ApplicationVisitor<String>() {
			@Override
			public String visit(CdsApplication app) {
				return "cds";
			}

			@Override
			public String visit(DpsApplication app) {
				return "dps";
			}

			@Override
			public String visit(ScpApplication app) {
				return "scp";
			}
		});
	}


	/**
	 * A factory for creating ApplicationResponse objects.
	 */
	public static class ApplicationResponseFactory implements ResponseFactory<ApplicationResponse, Application<?,?>> {
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.response.ResponseFactory#getInstance(java.lang.Object)
		 */
		@Override
		public ApplicationResponse getInstance(Application<?,?> param) {
			return new ApplicationResponse(param);
		}
	}	
}
