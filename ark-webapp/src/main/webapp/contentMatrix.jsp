<!--
Copyright 2014 Time Inc

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->
<%@ page
	import="com.timeInc.ark.role.Role, com.timeInc.ark.application.*, com.timeInc.ark.application.ApplicationEntity.ApplicationVisitor, com.timeInc.ark.issue.remote.*, com.timeInc.ark.issue.*, com.timeInc.ark.dao.*, com.timeInc.ark.role.*, java.text.DecimalFormat, java.util.*"%>
<html>
<head>
<title>iPad Content Size</title>
<style type="text/css">
body {
	font-family: arial, tahoma, verdana;
	font-size: 11px;
}

table {
	border: 1px solid #CCCCCC;
}

td {
	font-size: 11px;
	border-bottom: 1px solid #CCCCCC;
}

select,option {
	font-family: arial, tahoma, verdana;
	font-size: 11px;
}

.error {
	color: red
}

.title {
	font-weight: bold;
	background-color: #CCCCCC;
}

.label {
	background-color: #EEEEEE;
	border-right: 1px solid #CCCCCC;
}

.label-id {
	background-color: #EEEEEE;
	border-left: 1px solid #CCCCCC;
}

.value {
	padding-right: 10px;
}
</style>
</head>
<body leftmargin="40px" topmargin="40px">


	<form method="POST" name="pform">
		Publication : <select name="publicationGroupId"
			onchange="this.form.submit()">
			<option value="0">Select one</option>

			<%
					PublicationGroupDAO pubDAO = PublicationGroupDAO.Factory.getDao(Role.User);
					List<PublicationGroup> pubs = pubDAO.getAll();
					for(PublicationGroup pub : pubs) {
						out.print("<option value=\"" + pub.getId() + "\">" + pub.getName() + "</option>");
					}
				%>
		</select>

		<p></p>
		<%
			if (request.getParameter("applicationId") != null) {
				try {
					Integer appId = new Integer(request.getParameter("applicationId"));
					
					Application<?,?> app =  ApplicationDAO.Factory.getDao(Role.User).read(appId);
					
					if (app != null) {
						boolean isCdsApp = app.accept(new ApplicationVisitor<Boolean>() {
							public Boolean visit(CdsApplication app) { return true; }
							public Boolean visit(DpsApplication app) { return false; }
							public Boolean visit(ScpApplication app) { return false; }
						});
						
						final List<IssueMeta> issues = IssueMetaDAO.Factory.getDao(Role.User).getBy(app);

						if(issues != null && issues.size() > 0) {
							out.print("<table cellpadding='8' cellspacing='0'>");
							out.print("<tr><td class='title'>Issue Name</td><td class='title'>Size in MB</td>");
							
							List<CdsMetaData> cdsMetas =
									app.accept(new ApplicationVisitor<List<CdsMetaData>>() {
										public List<CdsMetaData> visit(CdsApplication app) { return app.getRemoteMetaFor(issues); }
										public List<CdsMetaData> visit(DpsApplication app) { return null; }
										public List<CdsMetaData> visit(ScpApplication app) { return null; }
									});					
									
							
							if(isCdsApp) {
								out.print("<td class='title'>Issue ID</td></tr>");
							}
							
							for (int i = 0; i < issues.size(); i++) {
									IssueMeta issue = issues.get(i);
									
									if(issue.isContentUploaded()) {
										out.print("<tr><td class='label'>"
												+ issue.getIssueName()
												+ "</td><td class='value' align='right'>"
												+ new DecimalFormat("#.##")
														.format(issue.getSize())
												+ "</td>");
													
										if(isCdsApp) {
											CdsMetaData cdsMeta = cdsMetas.get(i);
											out.print("<td class='label-id'>");
											if(cdsMeta != CdsMetaData.NONE) {
												out.print(cdsMeta.getCdsId());										
											}
		    										 out.print("</td>");
										}	
									}
									out.print("</tr>");
								}
								
							out.print("</table>");
						} else {
							out.print("<span class='error'>There are no issues available for selected Application.</span>");
						}
					}
				} catch (NumberFormatException e) { /* do nothing, hacking attempt */
				}

			} else if (request.getParameter("publicationGroupId") != null) {
				try {
					Integer pubgpId = new Integer(
							request.getParameter("publicationGroupId"));
					PublicationGroup pg = PublicationGroupDAO.Factory.getDao(Role.User).read(pubgpId);
					if (pg != null) {
						List<Application<?,?>> apps = ApplicationDAO.Factory.getDao(Role.User).getByPublication(pg);
						if (apps != null && apps.size() > 0) {
							out.print("Application : <select name='applicationId' onchange='this.form.submit()'><option value='0'>Select One</option>");
							for (Application<?,?> app : apps) {
								out.print("<option value='" + app.getId()
										+ "'>" + app.getName() + "</option>");
							}
						} else {
							out.print("<span class='error'>There are no Applicaiton set up for selected Publication.</span>");
						}
					}
					out.print("<script>document.pform.publicationGroupId.value="
							+ pubgpId + ";</script>");
				} catch (NumberFormatException e) { /* do nothing, hacking attempt */
				}
			}
		%>
	</form>
</body>
</html>
