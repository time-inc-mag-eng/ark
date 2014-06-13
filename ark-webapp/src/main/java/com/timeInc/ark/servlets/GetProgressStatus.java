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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.timeInc.ark.role.User;
import com.timeInc.ark.util.ExtJsJsonSerializer;
import com.timeInc.mageng.util.progress.ProgressStatus;


/**
 * Retrieves the status of a task using a progress id. 
 *
 */
public class GetProgressStatus extends PermissionServlet {
	private static final long serialVersionUID = 1L;

	private static final String PARAM_PROGRESS_ID = "progressId";
	
	private static final String TYPE_PARAM = "type";
	private static final String TYPE_CONTENT_VALUE = "content";
	private static final String TYPE_MEDIA_VALUE = "media";

	protected void handleGet(HttpServletRequest request, HttpServletResponse response,  User user) throws ServletException, IOException {
		response.setContentType ("application/json; charset=UTF-8");

		String progressId = request.getParameter(PARAM_PROGRESS_ID);		
		String type = request.getParameter(TYPE_PARAM);
		
		if(type.equals(TYPE_CONTENT_VALUE)) {
			ProgressStatus status = getContentUploader().getProgressStatus(progressId);
			String jsonResponse = ExtJsJsonSerializer.success(status);		
			response.getWriter().write(jsonResponse);
		} else if(type.equals(TYPE_MEDIA_VALUE)) {
			ProgressStatus status = getMediaUploader().getProgressStatus(progressId);
			String jsonResponse = ExtJsJsonSerializer.success(status);		
			response.getWriter().write(jsonResponse);
		}
	}
}
