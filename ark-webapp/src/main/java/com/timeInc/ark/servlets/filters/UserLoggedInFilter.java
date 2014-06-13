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
package com.timeInc.ark.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.timeInc.ark.response.ErrorMessage;
import com.timeInc.ark.role.User;
import com.timeInc.ark.util.ExtJsJsonSerializer;

/**
 * Filter to determine if user logged in.
 */
public class UserLoggedInFilter implements Filter {

	
	/**
	 * Checks to see if a session attribute 'user' exists. If the user exists
	 * the filters forwards to the invoked servlet that was mapped otherwise, a
	 * json error of the following format is returned to the response.
	 *
	 * @param request the request
	 * @param response the response
	 * @param chain the chain
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		User user = (User) req.getSession().getAttribute("user");

		if(user != null) { 
			chain.doFilter(request,response);
		} else {
			HttpServletResponse resp = (HttpServletResponse) response;
			resp.setContentType("application/json");
			String bodyResp = ExtJsJsonSerializer.fail("Session timed out", ErrorMessage.Code.INVALID_SESSION);
			resp.getWriter().write(bodyResp);				
		}
	}


	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {}
}
