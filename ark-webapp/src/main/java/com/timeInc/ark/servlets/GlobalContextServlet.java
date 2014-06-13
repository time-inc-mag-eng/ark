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
package com.timeInc.ark.servlets;

import javax.servlet.http.HttpServlet;

import com.timeInc.ark.global.GlobalSetting;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.upload.ConcurrentUploader;
import com.timeInc.ark.upload.ConcurrentUploader.CachedConcurrentUploader;
import com.timeInc.ark.upload.ConcurrentUploader.ConcurrentStatusUploader;
import com.timeInc.ark.upload.preview.IssueMetaStatusMapping;
import com.timeInc.mageng.util.misc.Status;

/**
 * Utility abstract class that allows subclasses to retrieve shared objects by invoking methods.
 */
public abstract class GlobalContextServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected final GlobalSetting getGlobalSetting() {
		return (GlobalSetting) getServletContext().getAttribute(ArkServletContext.ARK_SETTING_ATTRIBUTE);
	}
	
	protected final CachedConcurrentUploader<IssueMeta> getContentUploader() {
		return (CachedConcurrentUploader<IssueMeta>) getServletContext().getAttribute(ArkServletContext.CONTENT_EXECUTOR);
	}
	
	
	protected final CachedConcurrentUploader<Issue> getMediaUploader() {
		return (CachedConcurrentUploader<Issue>) getServletContext().getAttribute(ArkServletContext.MEDIA_EXECUTOR);
	}
	
	protected final ConcurrentUploader<Status, IssueMetaStatusMapping, IssueMeta> getPreviewUploader() {
		return (ConcurrentStatusUploader) getServletContext().getAttribute(ArkServletContext.PREVIEW_EXECUTOR);
	}
}
