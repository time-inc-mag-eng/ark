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
package com.timeInc.ark.util.hibernate;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.timeInc.mageng.util.filter.Filter;
import com.timeInc.mageng.util.filter.FilterAttribute;

/**
 * Hibernate implementation of a Filter that uses Criterion for filtering the attribute.
 */
public class CriterionFilter implements Filter<Criterion> {

	private final FilterAttribute<? extends Object> holder;
	private final String alias;
	
	
	/**
	 * Instantiates a new criterion filter.
	 *
	 * @param holder the holder
	 * @param alias the alias
	 */
	public CriterionFilter(FilterAttribute<? extends Object> holder, String alias) {
		this.holder = holder;
		this.alias = !alias.isEmpty() ? alias + "." : "";
	}
	
	/**
	 * Instantiates a new criterion filter.
	 *
	 * @param holder the holder
	 */
	public CriterionFilter(FilterAttribute<? extends Object> holder) {
		this(holder, "");
	}
	
	
	/* (non-Javadoc)
	 * @see com.timeInc.mageng.util.filter.Filter#retrieveFilter()
	 */
	@Override
	public Criterion retrieveFilter() {
		
		Object value = holder.getValue();
		String field = holder.getField();
		
		
		if(holder.getOperation() == Operation.EQ)
			return Restrictions.eq(alias + field,value);
		else if(holder.getOperation() == Operation.GREATER)
			return Restrictions.gt(alias + field,value);
		else if(holder.getOperation() == Operation.LESS)
			return Restrictions.lt(alias + field,value);
		
		return null;
	}
}
