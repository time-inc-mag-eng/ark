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
package com.timeInc.ark.application;

/**
 * Generic visitor pattern to access an application
 * in a type-safe manner.
 */
public interface ApplicationEntity {
	<T> T accept(ApplicationVisitor<T> entity);
	
	interface ApplicationVisitor<T> {
		T visit(CdsApplication app);
		T visit(DpsApplication app);
		T visit(ScpApplication app);
	}

}

