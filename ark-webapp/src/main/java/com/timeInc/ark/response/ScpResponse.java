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

/**
 * 
 * The Class ScpResponse
 *
 */

public class ScpResponse extends CommonAppResponse{

	private String server;
	private int port;
	private String dirPath;
	private String contentType;
	private int contentParserId;
	private String pathNaming;
	private String previewNaming;
	
	

	/**
	 * Instantiates a new scp response.
	 */
	public ScpResponse() {}
		
	/**
	 * Gets the preview naming.
	 *
	 * @return the preview naming
	 */
	public String getPreviewNaming() {
		return previewNaming;
	}

	/**
	 * Sets the preview naming.
	 *
	 * @param previewNaming the new preview naming
	 */
	public void setPreviewNaming(String previewNaming) {
		this.previewNaming = previewNaming;
	}
	
	/**
	 * Gets the content parser id.
	 *
	 * @return the content parser id
	 */
	public int getContentParserId() {
		return contentParserId;
	}
	
	/**
	 * Sets the content parser id.
	 *
	 * @param contentParserId the new content parser id
	 */
	public void setContentParserId(int contentParserId) {
		this.contentParserId = contentParserId;
	}

	/**
	 * Sets the server.
	 *
	 * @param s the new server
	 */
	public void setServer(String s){
		this.server=s;
	}
	
	/**
	 * Sets the port.
	 *
	 * @param i the new port
	 */
	public void setPort(int i){
		this.port=i;
	}
	
	/**
	 * Sets the dirpath.
	 *
	 * @param d the new dirpath
	 */
	public void setDirpath(String d){
		this.dirPath=d;
	}
	
	/**
	 * Sets the content type.
	 *
	 * @param t the new content type
	 */
	public void setContentType(String t){
		this.contentType=t;
	}
	
	/**
	 * Sets the path naming.
	 *
	 * @param path the new path naming
	 */
	public void setPathNaming (String path){
		this.pathNaming=path;
	}
	
}
