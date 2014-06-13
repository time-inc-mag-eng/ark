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

import java.util.Collection;
import java.util.Collections;

import org.hibernate.criterion.Order;

import com.timeInc.mageng.util.misc.Precondition;

/**
 * Represents the data associated with a page result.
 *
 * @param <T> the data type
 */
public class PageData<T> {
	private final long total;
	private final Collection<T> data;
	
	
	/**
	 * Instantiates a new page data.
	 *
	 * @param data the data
	 * @param total the total
	 */
	public PageData(Collection<T> data, long total) {
		if(data.isEmpty())
			this.data = Collections.emptyList();
		else
			this.data = data;
		
		this.total = total;
	}
		
	/**
	 * Gets the total.
	 *
	 * @return the total
	 */
	public long getTotal() {
		return total;
	}
	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public Collection<T> getData() {
		return data;
	}

	/**
	 * A paging request to get a PageData.
	 */
	public static class PageRequest {
		
		/**
		 * The sort order of a field.
		 */
		public enum OrderType {
			ASC {
				public Order getOrder(String columnName) {
					return Order.asc(columnName);
				}
			}, DESC {
				public Order getOrder(String columnName) {
					return Order.desc(columnName);
				}
			};
			
			/**
			 * Gets the sort order.
			 *
			 * @param sortOrder the sort order
			 * @return the sort order
			 */
			public static OrderType getSortOrder(String sortOrder) {
				if(sortOrder.equalsIgnoreCase("DESC"))
					return DESC;
				else if(sortOrder.equalsIgnoreCase("ASC"))
					return ASC;
				else 
					throw new IllegalArgumentException("sortOrder must either be DESC or ASC");
			}
			
			/**
			 * Gets the order.
			 *
			 * @param columnName the column name
			 * @return the order
			 */
			public abstract Order getOrder(String columnName);
			

		};

		private final OrderType dir;
		private final int pageSize;		
		private final int startIndex;
		private final String sortColumn;


		/**
		 * Instantiates a new page request.
		 *
		 * @param orderType the order type
		 * @param pageNumber the page number
		 * @param sortColumn the sort column
		 * @param pageSize the page size
		 */
		public PageRequest(OrderType orderType, int pageNumber, String sortColumn, int pageSize ) {
			this(orderType, sortColumn, pageSize, calculateIndex(pageNumber,pageSize));
		}
		
		private static int calculateIndex(int pageNumber, int pageSize) {
			int index = pageNumber == 1 ? 0 : (pageNumber-1) * pageSize;
			return index;
		}
		
		/**
		 * Instantiates a new page request.
		 *
		 * @param orderType the order type
		 * @param sortColumn the sort column
		 * @param pageSize the page size
		 * @param startIndex the start index
		 */
		public PageRequest(OrderType orderType, String sortColumn, int pageSize, int startIndex) {
			Precondition.checkStringEmpty(sortColumn,"sortColumn");

			if(pageSize <= 0)
				throw new IllegalArgumentException("Page size must be > 0");

			if(startIndex < 0)
				throw new IllegalArgumentException("Start index must be >= 0");

			this.pageSize = pageSize;
			this.startIndex = startIndex;
			this.sortColumn = sortColumn;
			this.dir = orderType;
		}

		/**
		 * Gets the dir.
		 *
		 * @return the dir
		 */
		public OrderType getDir() {
			return dir;
		}

		/**
		 * Gets the page size.
		 *
		 * @return the page size
		 */
		public int getPageSize() {
			return pageSize;
		}

		/**
		 * Gets the start index.
		 *
		 * @return the start index
		 */
		public int getStartIndex() {
			return startIndex;
		}	
		
		/**
		 * Gets the sort column.
		 *
		 * @return the sort column
		 */
		public String getSortColumn() {
			return sortColumn;
		}
	}
	
}
