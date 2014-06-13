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
package com.timeInc.ark.uploader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

import com.timeInc.ark.api.origin.OriginClient;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.util.progress.HttpClient4CountingEntity;
import com.timeInc.mageng.util.progress.ProgressListener;

/**
 * Uploads content to Time Inc's internal origin server by making a request to the CGI script.
 */
public class OriginContentUploader implements ContentUploader<File> {
	/** Request parameters **/
	private static final String PARAM_ISSUEID = "issue_id";
	private static final String PARAM_ISSUE_BUNDLE = "issue_bundle";
	private static final String PARAM_UPLOAD_ACTION = "upload_action";
	private static final String PARAM_UPLOAD_TYPE = "upload_type";
	private static final String PARAM_REFRESH_CACHE = "refresh_cache";
	private static final String PARAM_REFRESH_EMAILS = "refresh_email";
	private static final String PARAM_UPLOAD_PHASE = "upload_phase";	

	private enum RefreshCache implements ParamValue {
		DISABLE { public String toString() { return "0"; } },
		ENABLE { public String toString() { return "1"; } }
	}
	
	private enum UploadAction implements ParamValue {
		OVERWRITE { public String toString() { return  "overwrite"; } },
		UPDATE { public String toString() { return "update"; } }
	}

	private enum UploadType implements ParamValue {
		PROTECTED { public String toString() { return "protected"; } },
		PREVIEW { public String toString() { return "preview"; } }
	}

	private enum UploadPhase implements ParamValue {
		ALL { public String toString() { return "all"; } },
		PREVIEW { public String toString() { return "preview"; } },
		PUBLISH { public String toString() { return "publish"; } }
	}	
	
	private int id;
	
	private OriginClient client;
	
	private String baseUrl_protected;
	private String protectedDir;
	
	private String originPath;
	
	private Set<String> refreshEmail = new HashSet<String>(); 
	
	private boolean refreshCache = false;
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.uploader.ContentUploader#uploadContent(java.lang.Object, com.timeInc.ark.issue.IssueMeta)
	 */
	public void uploadContent(File file, IssueMeta meta) {
		uploadContent(file, meta, null);
	}	
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.uploader.ContentUploader#uploadContent(java.lang.Object, com.timeInc.ark.issue.IssueMeta, com.timeInc.mageng.util.progress.ProgressListener)
	 */
	public void uploadContent(File file, IssueMeta meta, ProgressListener listener) {
		Map<String, String> params = getParams(
					new Pair(PARAM_ISSUEID, meta.getReferenceId()),
					new Pair(PARAM_UPLOAD_TYPE, UploadType.PROTECTED),
					new Pair(PARAM_UPLOAD_ACTION, UploadAction.OVERWRITE),
					new Pair(PARAM_UPLOAD_PHASE, UploadPhase.PREVIEW),
					new Pair(PARAM_REFRESH_CACHE, refreshCache ? RefreshCache.ENABLE : RefreshCache.DISABLE),
					new Pair(PARAM_REFRESH_EMAILS, this.getRefreshEmails())); 

		uploadTarFileToOrigin(params, file, listener);	
	}
	
	/**
	 * Publish content to the cdn for an IssueMeta
	 *
	 * @param meta the meta
	 */
	public void publish(IssueMeta meta) {
		HttpPost post = getPost();
		
		Map<String, String> params = getParams(
				new Pair(PARAM_UPLOAD_PHASE, UploadPhase.PUBLISH), 
				new Pair(PARAM_ISSUEID, meta.getReferenceId()),
				new Pair(PARAM_UPLOAD_TYPE,UploadType.PROTECTED));

        List<NameValuePair> nvps = new ArrayList <NameValuePair>(); 
        
        for(Map.Entry<String, String> param : params.entrySet()) {
        	 nvps.add(new BasicNameValuePair(param.getKey(), param.getValue()));
        }
        
        try {
			post.setEntity(new UrlEncodedFormEntity(nvps));
			client.sendRequest(post);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Adds the refresh content email.
	 *
	 * @param email the email
	 */
	public void addRefreshContentEmail(String email) {
		refreshEmail.add(email);
	}
	
	/**
	 * Enable refresh content.
	 *
	 * @param enable the enable
	 */
	public void enableRefreshContent(boolean enable) {
		this.refreshCache = enable;
	}
	
	
	private static String normalizeUrl(String url) { // todo normalize url
		URI uri;
		try {
			uri = new URI(url).normalize();
			return uri.toString();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets the content url.
	 *
	 * @param meta the meta
	 * @param file the file
	 * @return the content url
	 */
	public String getContentUrl(IssueMeta meta, String file) {
		return normalizeUrl(baseUrl_protected + protectedDir + "/" + meta.getReferenceId() + "/" + file);
	}
	
	/**
	 * Gets the protected dir.
	 *
	 * @return the protected dir
	 */
	public String getProtectedDir() {
		return protectedDir;
	}	
	
	private HttpPost getPost() {
		return new HttpPost(originPath);
	}
	
	private void uploadTarFileToOrigin(Map<String, String> params, File file, ProgressListener listener) {
		MultipartEntity entity = new MultipartEntity();
		
		for(Map.Entry<String,String> pair : params.entrySet()) {
			try {
				entity.addPart(pair.getKey(), new StringBody(pair.getValue()));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		
		entity.addPart(PARAM_ISSUE_BUNDLE, new FileBody(file));

		HttpPost post = getPost();
		
		if(listener == null)
			post.setEntity(entity);
		else
			post.setEntity(new HttpClient4CountingEntity(entity, listener));
		
		client.sendRequest(post);
	}
	
	private String getRefreshEmails() {
		String delim = "";
		
		StringBuffer sb = new StringBuffer();
		
		for (String email : refreshEmail) {
			sb.append(delim).append(email);
			delim = ",";
		}

		return sb.toString() ;
	}
	
	private static Map<String,String> getParams(Pair... pairs) {
		Map<String,String> paramMap = new HashMap<String,String>(pairs.length);
		
		for(Pair pair : pairs) {
			paramMap.put(pair.key, pair.value);
		}
		
		return paramMap;
	}	
	
	private interface ParamValue {}
	
	private static class Pair {
		String key;
		String value;
		
		public Pair(String key, ParamValue value) {
			this(key, value.toString());
		}

		public Pair(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}
}
