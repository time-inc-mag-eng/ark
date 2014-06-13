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

/**
 * The Class ImagePreviewRequest.
 */
public class ImagePreviewRequest {
	String imgConvention;
	String regex;
	String msgError;
	
	/**
	 * Sets the img convention.
	 *
	 * @param imgConvention the new img convention
	 */
	public void setImgConvention(String imgConvention) {
		this.imgConvention = imgConvention;
	}
	
	/**
	 * Sets the regex.
	 *
	 * @param regex the new regex
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}
	
	/**
	 * Sets the msg error.
	 *
	 * @param msgError the new msg error
	 */
	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}
	
	/**
	 * Gets the img convention.
	 *
	 * @return the img convention
	 */
	public String getImgConvention() {
		return imgConvention;
	}
	
	/**
	 * Gets the regex.
	 *
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}
	
	/**
	 * Gets the msg error.
	 *
	 * @return the msg error
	 */
	public String getMsgError() {
		return msgError;
	}
}
