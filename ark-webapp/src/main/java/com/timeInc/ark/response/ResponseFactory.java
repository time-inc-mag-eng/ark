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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A factory for creating a list of Response objects.
 *
 * @param <T> the generic type
 * @param <V> the value type
 */
public interface ResponseFactory<T, V> {
	T getInstance(V param);
	
	/**
	 * The Class Util.
	 */
	public static class Util {
		private Util() {}
		
		/**
		 * Gets the instances as list.
		 *
		 * @param <T> the generic type
		 * @param <V> the value type
		 * @param factory the factory
		 * @param params the params
		 * @return the instances as list
		 */
		public static <T,V> List<T> getInstancesAsList(ResponseFactory<T,V> factory, Collection<V> params) {
			List<T> instances = new ArrayList<T>(params.size());
			
			writeToCollection(factory, params, instances);
			return instances;
		}
		
		private static <T,V> void writeToCollection(ResponseFactory<T,V> factory, Collection<V> params, Collection<T> out) {
			for(V param : params) 
				out.add(factory.getInstance(param));
		}
	}
}
