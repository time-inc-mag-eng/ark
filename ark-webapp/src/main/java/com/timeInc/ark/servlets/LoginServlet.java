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

import org.apache.log4j.Logger;

import com.timeInc.ark.response.ErrorMessage;
import com.timeInc.ark.response.UserResponse;
import com.timeInc.ark.role.User;
import com.timeInc.ark.util.ExtJsJsonSerializer;

/**
 * Servlet that does user/pass authentication.
 *
 */
public class LoginServlet extends GlobalContextServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(LoginServlet.class);

	private static final String PARAM_USERNAME = "username";
	private static final String PARAM_PASSWORD = "password";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("application/json");

		String username = req.getParameter(PARAM_USERNAME);
		String password = req.getParameter(PARAM_PASSWORD);

		try {
			User user = getGlobalSetting().getUserFactory().authenticate(username, password);

			if(user != null) {
				
				if(user.getGroups().isEmpty()) {
					resp.getWriter().write(ExtJsJsonSerializer.fail("Please make sure you have access to publication groups.", ErrorMessage.Code.GENERIC));
				} else {
					req.getSession().setAttribute("user", user);
					resp.getWriter().write(ExtJsJsonSerializer.success(new UserResponse(user.getRole()))); 
				}
			} else {
				resp.getWriter().write(ExtJsJsonSerializer.fail("Please make sure you have access to this application and your username/password is correct.", ErrorMessage.Code.GENERIC));
			}
		} catch(Throwable t) {
			log.error(t);
			resp.getWriter().write(ExtJsJsonSerializer.fail("There was a problem with the auth server.", ErrorMessage.Code.GENERIC));
		}
	}
}
