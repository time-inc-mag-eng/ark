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
import com.timeInc.ark.upload.IdToCacheMapping;
import com.timeInc.ark.upload.content.UserContentConfig;
import com.timeInc.ark.util.ExtJsJsonSerializer;
import com.timeInc.ark.util.ServletUtil;
import com.timeInc.mageng.util.file.FileUtil;
import com.timeInc.mageng.util.string.StringUtil;


/**
 * Uploads a content file to multiple IssueMeta
 */
public class ContentUploadServlet extends PermissionServlet { 
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(ContentUploadServlet.class);

	private static final String PARAM_FORM_FILE = "form-file"; 
	private static final String PARAM_META_IDS = "metaIds"; 
	private static final String PARAM_UPLOAD_AS_NEW = "newUpload";

	@Override
	protected void handlePost(HttpServletRequest req, HttpServletResponse resp,  User user) throws ServletException, IOException {	
		resp.setContentType ("text/html; charset=UTF-8");
		
		try {
			File workingDirectory = FileUtil.createTempDirectory(getGlobalSetting().getUploadDirectory());

			Map<String, String> nvmap = ServletUtil.getRequestParameters(req, resp, workingDirectory);

			boolean isUploadAsNew = nvmap.get(PARAM_UPLOAD_AS_NEW) == null ? false : true;

			List<Integer> ids = StringUtil.splitCommaSepIntoPrimitive(Integer.class, nvmap.get(PARAM_META_IDS));

			List<IssueMeta> metas = IssueMetaDAO.Factory.getDao(user.getRole()).getById(ids);

			File uploadedFile = new File(workingDirectory, nvmap.get(PARAM_FORM_FILE));

			UserContentConfig config = new UserContentConfig(user, workingDirectory, uploadedFile, isUploadAsNew);

			List<IdToCacheMapping> mapping = getContentUploader().execute(metas, config);
			
			resp.getWriter().print(ExtJsJsonSerializer.success(mapping));

		} catch (Exception e) { 
			log.error("Error uploading content", e);
			resp.getWriter().print(ExtJsJsonSerializer.fail("Error uploading content", ErrorMessage.Code.INFO));
		} 
	}	
}
