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
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.timeInc.ark.util.hibernate.HibernateUtil;

/**
 * Open session pattern for Hibernate session.
 */
public class EndTransactionFilter implements Filter {

	static Logger log = Logger.getLogger(EndTransactionFilter.class);

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {		
		try {
			HibernateUtil.getSession().beginTransaction();			
			chain.doFilter(request, response);		
			HibernateUtil.getSession().getTransaction().commit();

			log.trace("Committed transaction");
		} catch (Throwable t) { // handle any remaining exceptions and inform the client
			log.error("Unhandled exception occurred:", t);
			HibernateUtil.getSession().getTransaction().rollback();
			HttpServletResponse resp = (HttpServletResponse) response;
			resp.sendError(500);
		} 
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {}
}
