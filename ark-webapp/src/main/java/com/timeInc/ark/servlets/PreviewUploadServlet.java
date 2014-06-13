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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.timeInc.ark.dao.IssueMetaDAO;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.response.ErrorMessage;
import com.timeInc.ark.role.User;
import com.timeInc.ark.upload.preview.IssueMetaStatusMapping;
import com.timeInc.ark.upload.preview.UserPreviewConfig;
import com.timeInc.ark.util.ExtJsJsonSerializer;
import com.timeInc.ark.util.ServletUtil;
import com.timeInc.mageng.util.file.FileUtil;
import com.timeInc.mageng.util.string.StringUtil;

/**
 * Servlet class the upload previews for multiple IssueMetas. On completion it returns the result for each IssueMeta.
 */
public class PreviewUploadServlet extends PermissionServlet  {
	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(PreviewUploadServlet.class);

	private static final String PARAM_FILEPATH = "form-file";
	private static final String PARAM_META_IDS = "metaIds";
	private static final String PARAM_COVERSTORY = "cover-story";
	private static final String PARAM_NEWS_STAND_DESCRIPTION = "newsstand-summary";

	protected void handlePost(HttpServletRequest req, HttpServletResponse resp,  User user) throws ServletException, IOException {
		resp.setContentType ("text/html; charset=UTF-8"); // bug in extjs where you must set content-type text/html not json

		try {
			File workingDirectory = FileUtil.createTempDirectory(getGlobalSetting().getUploadDirectory());
			Map<String, String> nvmap = ServletUtil.getRequestParameters(req, resp, workingDirectory);
			
			String newsstandFeed = nvmap.get(PARAM_NEWS_STAND_DESCRIPTION);
			String coverStory = nvmap.get(PARAM_COVERSTORY);
			
			File uploadedFile = new File(workingDirectory, nvmap.get(PARAM_FILEPATH)); 
			UserPreviewConfig config = new UserPreviewConfig(user, workingDirectory, uploadedFile, coverStory, newsstandFeed);
			
			List<Integer> ids = StringUtil.splitCommaSepIntoPrimitive(Integer.class, nvmap.get(PARAM_META_IDS));

			List<IssueMeta> metas = IssueMetaDAO.Factory.getDao(user.getRole()).getById(ids);
			
			List<IssueMetaStatusMapping> statuses = getPreviewUploader().execute(metas, config);
			
			StringBuilder responseMsg = new StringBuilder();
			
			for(IssueMetaStatusMapping mapping : statuses) {
				responseMsg.append(mapping.meta.getApplicationName() + " : " + (mapping.status.isError() ? "Error Reason:" : "") + mapping.status.getDescription() + "<br>");
			}
			
			resp.getWriter().print(ExtJsJsonSerializer.success(responseMsg.toString()));

		} catch (Exception e) {
			log.error("File upload error", e);
			resp.getWriter().print(ExtJsJsonSerializer.fail("Error uploading previews", ErrorMessage.Code.INFO));
		} 

	}
}
