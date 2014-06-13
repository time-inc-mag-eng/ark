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
package com.timeInc.ark.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;

/**
 * Utility class for multi-part requests
 *
 * @author apradhan1271
 */
public class ServletUtil {
	
	/** The Constant log. */
	public static final Logger log = Logger.getLogger(ServletUtil.class);

	private ServletUtil() {}


	/**
	 * Request parameters with file upload is put in a Map interface nicely.
	 *
	 * @param req the req
	 * @param res the res
	 * @param uploadDirectory the upload directory
	 * @return Map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Map<String, String> getRequestParameters(HttpServletRequest req, 
			HttpServletResponse res, File uploadDirectory) throws IOException {
		Map<String, String> nvmap = new HashMap<String, String>();
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iter;
		try {
			iter = upload.getItemIterator(req);

			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				String name = item.getFieldName();
				InputStream stream = item.openStream();
				if (item.isFormField()) {
					nvmap.put(name, Streams.asString(stream,"UTF-8"));
				} else if(!item.getName().isEmpty()) {
					log.debug("File field " + name + " with file name " + item.getName() + " detected.");
					// Process the input stream
					String fileName = item.getName();

					/* different browser send different info for file upload file name,
					 * IE sends the full path, mozilla send just the file name, so we have to strip the path
					 * to get the file name
					 */
					int slashPos = fileName.lastIndexOf("/");
					if (slashPos != -1) {
						fileName = fileName.substring(slashPos + 1);
					}
					// now let's try the back slash
					slashPos = fileName.lastIndexOf("\\");
					if (slashPos != -1) {
						fileName = fileName.substring(slashPos + 1);
					}

					nvmap.put(name, fileName);
					File file = new File(uploadDirectory,fileName);

					FileOutputStream fos = new FileOutputStream(file);
					byte buf[] = new byte[1024 * 1024];
					int len = 0;
					InputStream in = item.openStream();
					while ((len = in.read(buf)) != -1) {
						fos.write(buf, 0, len);
					}
					item.openStream().close();
					fos.close();
					in.close();
				}
			}
			return nvmap;			
		} catch (FileUploadException e) {
			throw new RuntimeException(e);
		}
	}
}
