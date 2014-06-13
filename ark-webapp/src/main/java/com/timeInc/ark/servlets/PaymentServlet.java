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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.timeInc.ark.dao.PaymentDAO;
import com.timeInc.ark.issue.PaymentType;
import com.timeInc.ark.role.Role;
import com.timeInc.ark.role.User;
import com.timeInc.ark.util.ExtJsJsonSerializer;

/**
 * Servlet that gets the list of all PaymentTypes.
 *
 */
public class PaymentServlet extends PermissionServlet {
	private static final long serialVersionUID = 1L;
       	
	/**
	 * Writes a json to the response containing the list of all PaymentType
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void handleGet(HttpServletRequest req, HttpServletResponse resp,  User user) throws ServletException, IOException {
		PaymentDAO paymentDAO = new PaymentDAO();
		List<PaymentType> payments = paymentDAO.getAll();
		
		String jsonResponse = ExtJsJsonSerializer.success(payments);
		resp.setContentType("application/json");
		resp.getWriter().write(jsonResponse);
	}
	
	@Override
	protected List<Role> getEntitledRoles() {
		List<Role> roles = super.getEntitledRoles();
		roles.add(Role.IssueManager);
		return roles;
	}
}
