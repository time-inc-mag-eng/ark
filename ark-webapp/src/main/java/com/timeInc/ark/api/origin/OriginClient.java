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
package com.timeInc.ark.api.origin;

import java.io.Serializable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.timeInc.mageng.util.exceptions.PrettyException;

/**
 * A client that sends a request to an http server using basic user / pass scheme.
 */
public class OriginClient implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(OriginClient.class);
	
	private Integer id;

	private String originServer;
	private int originPort;
	private String originUsername;
	private String originPassword;
	
	private HttpClient client;

	protected OriginClient() {}
	
	/**
	 * Instantiates a new origin client.
	 *
	 * @param originServer the origin server
	 * @param originPort the origin port
	 * @param originUsername the origin username
	 * @param originPassword the origin password
	 * @param client the client
	 */
	public OriginClient(String originServer, int originPort,
			String originUsername, String originPassword,
			HttpClient client) {
		this.originServer = originServer;
		this.originPort = originPort;
		this.originUsername = originUsername;
		this.originPassword = originPassword;
		this.client = client;
	}

	/**
	 * Sends an http request.
	 *
	 * @param method the method
	 * @throws PrettyException if the response code is not 200 or if there was a socket error
	 */
	public void sendRequest(HttpRequest method) {
		try {
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(originUsername, getOriginPassword());
			method.addHeader(new BasicScheme().authenticate(creds, method));
			
			log.debug("executing request: " + method.getRequestLine() + " to host " + getHost());
			
			HttpResponse response = client.execute(getHost(), method);
			
			int responseCode = response.getStatusLine().getStatusCode();
			
			HttpEntity entity = response.getEntity();
			
			String responseBody = EntityUtils.toString(entity); 
			
			log.debug("Response body " + responseBody);
			
			if(responseCode != 200) {
				String detailedError = extractOriginError(responseBody);
				log.error("Returned response " + responseCode + " Reason " + detailedError);
				throw new PrettyException(detailedError);
			}

		} catch(PrettyException e) {
			throw e;
		} catch(Exception e) {
				throw new PrettyException("There was an error sending a request to "  + getHost().getHostName(), e);
		}
	}

	/**
	 * Sets the client.
	 *
	 * @param client the new client
	 */
	public void setClient(HttpClient client) {
		this.client = client;
	}

	/**
	 * Attempts to extract the detailed error description that is returned from
	 * the Origin Service response body when an http response code
	 * other than 200 is returned. In order to extract the detailed error description it must be in the following format:
	 * {@literal <BR> Error: xxx  <BR>} where xxx can be the detailed message 
	 * @param statusCode the http status code other than 200
	 * @param responseBody the response body returned from origin request
	 * @return the detailed error message otherwise "Unknown Error"
	 */
	private static String extractOriginError(String responseBody) {
		int startErrorIndex = responseBody.lastIndexOf("Error:");
		int brIndex = responseBody.indexOf("<BR>");

		if(startErrorIndex == -1 || brIndex == -1 || responseBody.indexOf("<BR>",startErrorIndex) == -1)
			return "Unknown HTTP Server Error";
		else {
			int endErrorIndex = responseBody.indexOf("<BR>",startErrorIndex);
			return responseBody.substring(startErrorIndex,endErrorIndex);
		}
	}	
	
	private String getOriginPassword() {
		return originPassword;
	}
	
	private HttpHost getHost() {
		return new HttpHost(originServer, originPort, "https");
	}
}
