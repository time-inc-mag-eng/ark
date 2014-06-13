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
package com.timeInc.ark.parser.content;

import com.timeInc.mageng.util.exceptions.PrettyException;

/**
 * The Class ContentException.
 */
public class ContentException extends PrettyException {
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new content exception.
	 *
	 * @param msg the msg
	 * @param t the t
	 */
	public ContentException(String msg, Throwable t) {
		super(msg,t);
	}
	
	/**
	 * Instantiates a new content exception.
	 *
	 * @param msg the msg
	 */
	public ContentException(String msg) {
		super(msg);
	}
}
