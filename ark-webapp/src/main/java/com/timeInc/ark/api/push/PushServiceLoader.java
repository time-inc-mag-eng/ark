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
package com.timeInc.ark.api.push;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ServiceLoader;

/**
 * Service loader for {@link com.timeInc.ark.api.push.PushFactory} that produces
 * a PushService
 */
public class PushServiceLoader {
	private static ServiceLoader<PushFactory> loader = ServiceLoader.load(PushFactory.class);

	/**
	 * Gets the push service by trying to find a
	 * service provider for a PushFactory with type 
	 * parameter T. 
	 *
	 * @param <T> the generic type
	 * @param pushServer the push server
	 * @param push used to match the type parameter
	 * @return the first matched service
	 * @throws IllegalStateException if no provider found with type parameter T
	 */
	public <T> PushService<T> getService(String pushServer, DelayedPush<T> push) {
		Class<?> paramType = getTypeParam(push);
		
		for(PushFactory<?> factory : loader){
			if(matchesTypeParam(factory, paramType)) {
				return (PushService<T>) factory.getPushService(pushServer);
			}
		} 

		throw new IllegalStateException("Failed to load provider for PushFactory with generic type parameter of " + paramType);
	}
	
	private static Class<?> getTypeParam(DelayedPush<?> clzz) {
		if(ParameterizedType.class.isInstance(clzz.getClass().getGenericSuperclass())) {
			return (Class<?>)((ParameterizedType) clzz.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		} else 
			throw new IllegalArgumentException("No type parameter for: " + clzz);
		
	}

	private static <T> boolean matchesTypeParam(PushFactory<?> factory, Class<?> clzz) {
		for(Type t : factory.getClass().getGenericInterfaces()) {
			if(ParameterizedType.class.isInstance(t)) {
				ParameterizedType paramType = (ParameterizedType) t;
				if(PushFactory.class == paramType.getRawType()) {
					return clzz == paramType.getActualTypeArguments()[0];
				}
			}
		}
		
		return false;
	}
}
