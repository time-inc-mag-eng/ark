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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.timeInc.ark.dao.IssueDAO.Factory;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.response.ErrorMessage;
import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.ark.upload.IdToCacheMapping;
import com.timeInc.ark.upload.content.UserContentConfig;
import com.timeInc.ark.util.ExtJsJsonSerializer;
import com.timeInc.ark.util.ServletUtil;
import com.timeInc.mageng.util.file.FileUtil;

/**
 * Server class to upload media files.
 */
public class MediaUploadServlet extends PermissionServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(MediaUploadServlet.class);

	private static final String PARAM_ISSUE_ID = "issueId"; 	

	@Override
	protected void handlePost(HttpServletRequest req, HttpServletResponse resp,  User user) throws ServletException, IOException {
		
		resp.setContentType("text/html; charset=UTF-8");

		try {
			
			File workingDirectory = FileUtil.createTempDirectory(getGlobalSetting().getUploadDirectory());
			File uploadDirectory = FileUtil.createTempDirectory(workingDirectory);
			
			Map<String, String> nvmap = ServletUtil.getRequestParameters(req, resp, uploadDirectory);
			log.debug("Putting uploaded media files in: " + uploadDirectory);

			int issueId = Integer.valueOf(nvmap.get(PARAM_ISSUE_ID));
			Issue issue = Factory.getDao(user.getRole()).read(issueId);
			
			UserContentConfig config = new UserContentConfig(user, workingDirectory, uploadDirectory, true);
			
			List<IdToCacheMapping> mapping = getMediaUploader().execute(Arrays.asList(new Issue[] { issue }), config);
			
			resp.getWriter().print(ExtJsJsonSerializer.success(mapping));

		} catch (Exception e) {
			log.error("Error uploading media", e);
			resp.getWriter().print(ExtJsJsonSerializer.fail("Error uploading media", ErrorMessage.Code.INFO));
		}
	}
	
	@Override
	protected List<Role> getEntitledRoles() {
		return Role.getRolesWithSameOrHigher(Role.Media);
	}
}
