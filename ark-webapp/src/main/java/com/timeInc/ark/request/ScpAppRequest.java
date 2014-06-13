/*
 * 
 */
package com.timeInc.ark.request;
/**
 * 
 * The Class ScpAppRequest.
 *
 */
public class ScpAppRequest extends CommonAppRequest {
	private String server;
	private int port;
	private String previewNaming;
	private String pathNaming;
	private String contentParserId;
	private String contentType;
	private String dirPath;
	
	/**
	 * Gets the server.
	 *
	 * @return the server
	 */
	public String getServer() {
		return server;
	}
	
	/**
	 * Sets the server.
	 *
	 * @param server the new server
	 */
	public void setServer(String server) {
		this.server = server;
	}
	
	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	public void setPort(int port) {
		this.port = port;
	}

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
	 * Gets the path naming.
	 *
	 * @return the path naming
	 */
	public String getPathNaming() {
		return pathNaming;
	}
	
	/**
	 * Sets the path naming.
	 *
	 * @param pathNaming the new path naming
	 */
	public void setPathNaming(String pathNaming) {
		this.pathNaming = pathNaming;
	}
	
	/**
	 * Gets the content parser id.
	 *
	 * @return the content parser id
	 */
	public String getContentParserId() {
		return contentParserId;
	}
	
	/**
	 * Sets the content parser id.
	 *
	 * @param contentParserId the new content parser id
	 */
	public void setContentParserId(String contentParserId) {
		this.contentParserId = contentParserId;
	}
	
	/**
	 * Gets the content type.
	 *
	 * @return the content type
	 */
	public String getContentType() {
		return contentType;
	}
	
	/**
	 * Sets the content type.
	 *
	 * @param contentType the new content type
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	/**
	 * Gets the dir path.
	 *
	 * @return the dir path
	 */
	public String getDirPath() {
		return dirPath;
	}
	
	/**
	 * Sets the dir path.
	 *
	 * @param dirPath the new dir path
	 */
	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}

	
	
}
