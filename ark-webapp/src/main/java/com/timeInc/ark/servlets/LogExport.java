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
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.timeInc.ark.dao.EventDAO;
import com.timeInc.ark.dao.PublicationGroupDAO.Factory;
import com.timeInc.ark.event.IssueEvent;
import com.timeInc.ark.event.Report;
import com.timeInc.ark.event.ReportBean;
import com.timeInc.ark.event.Report.ExcelReport;
import com.timeInc.ark.event.ReportBean.LogReportBeanFactory;
import com.timeInc.ark.response.ResponseFactory;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.role.User;

/**
 * Servlet that exports an excel report of ark events.
 * @see com.timeInc.ark.event.AbstractEvent
 * 
 */
public class LogExport extends PermissionServlet {
	private static final long serialVersionUID = 1L;

	private static final String PARAM_PUBLICATION_ID = "pubId";

	/**
	 * Writes to the response stream the report for Ark in pdf or excel format
	 * for a certain publication.
	 * 
	 * The publication to get reporting for is exposed by the request parameter
	 * "pubId" and the type of export is determined by the request parameter
	 * "exportType"
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void handleGet(HttpServletRequest request,
			HttpServletResponse response, User user) throws ServletException,
			IOException {

		Integer pubId = Integer.valueOf(request.getParameter(PARAM_PUBLICATION_ID));

		EventDAO eventDao = new EventDAO();

		PublicationGroup pub = Factory.getDao(user.getRole()).read(pubId);

		List<IssueEvent> result = eventDao.getIssueLogBefore(pub, pub.getNumDaysReport());
		List<ReportBean> reportBean = ResponseFactory.Util.getInstancesAsList(new LogReportBeanFactory(), result);

		Calendar today = Calendar.getInstance();

		String filename = "ARK_Report_" + pub.getName().replaceAll(" ", "") + "_" + today.get(Calendar.MONTH) + "." + today.get(Calendar.DAY_OF_MONTH) + "."+ today.get(Calendar.YEAR) + ".xls";
		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-Disposition", "attachment; filename="+ filename);
		
		ServletOutputStream servletOutputStream = response.getOutputStream();
		
		Report report = new ExcelReport();
		
		report.generateReport(servletOutputStream, reportBean);
		

		servletOutputStream.flush();
		servletOutputStream.close();
	}
}
