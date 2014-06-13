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
package com.timeInc.ark.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * A Generic DAO
 *
 * @param <T> the object type
 * @param <PK> the primary key type
 */
public interface GenericDAO<T, PK extends Serializable> {
	/**
	 * Create the object
	 * @param obj the object
	 * @return a ConstraintViolation if it conflicts with an already existing object; null otherwise
	 */
	ConstraintViolation create(T obj);
	
	/**
	 * Create a list of objects
	 * @param objs the objects
	 * @return a list of ConstraintViolation for each violating object. The index of that object
	 * included as {@link ConstraintViolation#index}
	 */
	List<ConstraintViolation> create(List<T> objs);
	
	/**
	 * Get an object by its identifier
	 * @param key the identifier
	 * @return an object that has the specified identifier
	 */
	T read(PK key);
	
	/**
	 * Update the object
	 * @param obj the object
	 * @return a ConstraintViolation if it conflicts with an already existing object; null otherwise
	 */
	ConstraintViolation update(T obj);
	
	/**
	 * Update a list of objects
	 * @param objs the objects
	 * @return a list of ConstraintViolation for each violating object. The index of that object
	 * included as {@link ConstraintViolation#index}
	 */
	Collection<ConstraintViolation> update(List<T> objs);
	
	/**
	 * Delete the object
	 * @param obj the object
	 * @return a ConstraintViolation if it conflicts with an already existing object; null otherwise
	 */
	ConstraintViolation delete(T obj);
	
	/**
	 * Delete a list of objects
	 * @param objs the objects
	 * @return a list of ConstraintViolation for each violating object. The index of that object
	 * included as {@link ConstraintViolation#index}
	 */
	Collection<ConstraintViolation> delete(List<T> objs);
	
	
	/**
	 * Delete the object by identifier
	 * @param id the identifier
	 * @return a ConstraintViolation if it conflicts with an already existing object; null otherwise
	 */
	ConstraintViolation deletebyId(PK id);
	
	/**
	 * Create a list of objects by its identifier
	 * @param ids the objects
	 * @return a list of ConstraintViolation for each violating object. The index of that object
	 * included as {@link ConstraintViolation#index}
	 */
	Collection<ConstraintViolation> deletebyId(List<PK> ids);
	
	
	/**
	 * Retrieve all of the objects
	 * @return the list of all objects
	 */
	List<T> getAll();
	
	/**
	 * Represents a constraint violation between other existing objects.
	 */
	public static class ConstraintViolation {
		
		/** The obj id. */
		public final String objId;
		
		/** The reason. */
		public final String reason;
		
		/** The index. */
		public final int index;
		
		/**
		 * Instantiates a new constraint failure.
		 *
		 * @param objId the obj id
		 * @param reason the reason
		 * @param index the index
		 */
		public ConstraintViolation(String objId, String reason, int index) {
			super();
			this.objId = objId;
			this.reason = reason;
			this.index = index;
		}
	}
}
