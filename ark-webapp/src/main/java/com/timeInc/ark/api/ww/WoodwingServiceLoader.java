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
package com.timeInc.ark.api.ww;

import java.util.ServiceLoader;

import com.timeInc.ark.api.ww.CdsConfig.Connection;

/**
 * A factory class for {@link com.timeInc.ark.api.ww.WoodwingService} 
 */
public class WoodwingServiceLoader {
	private static ServiceLoader<WoodwingFactory> loader = ServiceLoader.load(WoodwingFactory.class);

	/**
	 * Gets the service by using the ServiceLoader
	 *
	 * @param cdsConnection the cds connection
	 * @throws IllegalStateException if no provider was found
	 * @return the first service that is found
	 */
	public WoodwingService getService(Connection cdsConnection) {
		if(loader.iterator().hasNext()) {
			WoodwingFactory factory = loader.iterator().next();
			return factory.getWoodWingService(cdsConnection);
		} else
			throw new IllegalStateException("Failed to load provider for WoodwingFactory");
	}
}
