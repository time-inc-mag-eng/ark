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
package com.timeInc.ark.parser.content;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.parser.SingleContent;
import com.timeInc.mageng.util.file.FileExistValidator;
import com.timeInc.mageng.util.file.FileUtil;
import com.timeInc.mageng.util.file.FileValidator;
import com.timeInc.mageng.util.misc.Status;
import com.timeInc.mageng.util.xml.XMLUtil;
import com.timeInc.mageng.util.xml.XMLUtil.StringReplacer;

/**
 * Parser for Woodwing OFIP that converts it to a non-progressive. Requires the input to be an expanded directory.
 * @exception ContentException if there was a problem with converting
 */
public class CdsOfip extends IdentifyingContent<SingleContent>  {	
	private static final String XML_FILE = "magazine.xml";
	private static final String THUMBNAIL_PRETEXT = "thumb";

	private static final String XPATH_EXPRESSION_ISSUE_DESCRIPTION = "issue/issuedescription";
	private static final String NULL = "null";

	private static final Logger log = Logger.getLogger(CdsOfip.class);

	private final FileValidator validator = new FileExistValidator();


	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.AbstractSanitizer#parseContentAfterSanitized(java.io.File, com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public SingleContent parseContentAfterSanitized(File rootContentFolderPath, IssueMeta meta) throws ContentException {
		log.debug("OFIP content parsing @ " + rootContentFolderPath);

		updateXml(rootContentFolderPath,meta);

		return new SingleContent(rootContentFolderPath);
	}

	void afterXmlUpdate(File rootContentFolderPath) {}

	private void updateXml(File rootContentFolderPath, IssueMeta meta) throws ContentException {
		/* update null in issue description to issue date */
		try {
			XMLUtil.searchReplaceTextSubstring(new File(rootContentFolderPath,XML_FILE).getAbsolutePath(), 
					new StringReplacer() {
				public String replace(String original, String searchString,
						String replaceString) {
					return original.replaceFirst(searchString, replaceString);
				}
			}, 
			XPATH_EXPRESSION_ISSUE_DESCRIPTION, 
			NULL, 
			meta.getIssueName());
		} catch (Exception e) {
			throw new ContentException("There was an error transforming magazine.xml",e);
		}
	}


	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.Parser.ContentParser#validate(java.io.File)
	 */
	@Override
	public Status validate(File contentDirectory) {
		File rootXML = new File(contentDirectory,XML_FILE);
		return validator.validate(rootXML);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.Parser.ContentParser#getType()
	 */
	@Override
	public String getType() {
		return "ofip_cds_nonprogressive";
	}
	
	/**
	 * Parser for Woodwing OFIP that converts it to a progressive format. Requires the input to be an expanded directory.
	 * @exception ContentException if there was a problem with converting
	 */
	public static class Progressive extends CdsOfip {
		
		@Override
		protected void afterXmlUpdate(File rootContentFolderPath) {
			try {
				log.debug("This application wants progressive download, preparing helper files");
				createProgressiveHelperFile(rootContentFolderPath);
			} catch(IOException io) {
				throw new ContentException("IOException occurred while creating final content file",io); 
			} catch(DocumentException de) {
				throw new ContentException("Problem creating progressive helper file",de);
			}
		}
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.parser.content.CdsOfip#getType()
		 */
		@Override
		public String getType() {
			return "ofip_cds_progressive";
		}
		
		private static void createProgressiveHelperFile(File rcf) throws DocumentException, IOException {
			File xml = new File(rcf,XML_FILE);
			if (xml.exists() && xml.isFile() && xml.canRead()) {
				SAXReader reader = new SAXReader();
				Document document = reader.read(xml);

				/* let's get all the thumbnail nodes */
				Node node = document.selectSingleNode("//issue/items/item[1]/pages/page/verticalpage/pthumb");

				if(node == null) { // horizontal orientation only
					node = document.selectSingleNode("//issue/items/item[1]/pages/page/horizontalpage/pthumb");
				}
				File coverStoryDirectory = new File(rcf,node.getText()).getParentFile();

				log.debug("Cover Story directory: " + coverStoryDirectory.getPath());

				if (coverStoryDirectory.exists()) {
					log.trace("Cover directory found, now processing it to be zipped.");
					/* there is a cover story let's move it to get it zipped as initialload.zip */
					File initialLoadFolder = new File(rcf, "initialload" + File.separator + "images");
					File imagesDirectory = new File(rcf,"images");
					FileUtil.moveDirectoryToDirectory(coverStoryDirectory, initialLoadFolder, true);
					/* now let's get all the thumbnail files */
					FilenameFilter filter = new FilenameFilter() { 
						public boolean accept(File dir, String name) { 
							return name.startsWith(THUMBNAIL_PRETEXT); 
						} 
					};
					File thumbnails[] = FileUtil.getFiles(imagesDirectory, filter, true);
					log.trace("Moving thumbnails to be zipped");
					/* move all thumbnails to initialload */
					for (File f : thumbnails) {
						log.trace("Moving Thumbnail: " + f.getAbsolutePath());

						log.trace(initialLoadFolder.getAbsoluteFile() + " " +  f.getParent());
						FileUtil.moveFileToDirectory(f, 
								new File(initialLoadFolder.getAbsoluteFile() + f.getParent().substring(f.getParent().lastIndexOf(File.separator))), 
								true);
					}

					/* now let's zip this thing */
					FileUtil.zip(rcf.getAbsoluteFile() + File.separator + "initialload", 
							new File(rcf, "initialload.zip"));
					log.trace("Zipping initialload.zip file done, deleting initial Directory");
					FileUtil.deleteDirectoryAndContent(new File(rcf,"initialload"));

					/* now create the plist file which contains reference to all files
					 * like such
					 * <?xml version="1.0" encoding="UTF-8"?>
						<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
						<plist version="1.0">
						  <array>
						    <dict>
						      <key>directory</key>
						      <string>images/story_157485</string>
						      <key>filename</key>
						      <string>fullscreen_158017.jpg</string>
						      <key>mtime</key>
						      <integer>1299716133</integer>
						      <key>filesize</key>
						      <integer>214752</integer>
						      <key>story</key>
						      <integer>157485</integer>
						    </dict>
						    .
						    .
						    .
						   </array>
						  </plist>
					 * 
					 * */
					DocumentType dt = DocumentFactory.getInstance().createDocType("plist", 
							"-//Apple//DTD PLIST 1.0//EN", 
							"http://www.apple.com/DTDs/PropertyList-1.0.dtd");
					Document doc = DocumentFactory.getInstance().createDocument("UTF-8");
					doc.setDocType(dt);

					Element e = doc.addElement("plist").addAttribute("version", "1.0");
					Element array = e.addElement("array");

					Collection<File> c = FileUtil.listAllFiles(rcf, true);
					log.trace("Processing " + c.size() + " file for files.plist");
					for (File f : c) {
						String parentFolder = f.getParentFile().getName();
						Element dict = array.addElement("dict");
						dict.addElement("key").addText("directory");
						/* if the parent file is the root folder just insert nothing here */
						String directory = f.getParentFile().getAbsolutePath().length() >  rcf.getAbsolutePath().length() + 1 ? f.getParentFile().getAbsolutePath().substring(rcf.getAbsolutePath().length() + 1) : "";

						directory = directory.replaceAll("\\\\","/"); // on a windows machine the path is \ convert it to /

						dict.addElement("string").addText(directory);
						dict.addElement("key").addText("filename");
						dict.addElement("string").addText(f.getName());
						dict.addElement("key").addText("mtime");
						dict.addElement("integer").addText(String.valueOf(f.lastModified()/1000));
						dict.addElement("key").addText("filesize");
						dict.addElement("integer").addText(String.valueOf(f.length()));
						dict.addElement("key").addText("story");
						dict.addElement("integer").addText(parentFolder.startsWith("story_") ? parentFolder.substring(6) : "0");
					}
					/* now let's write the xml file */
					String outputfile = rcf.getAbsoluteFile() + File.separator + "files.plist";
					FileOutputStream fos = new FileOutputStream(outputfile);
					OutputFormat format = OutputFormat.createPrettyPrint();
					XMLWriter writer = new XMLWriter(fos, format);
					try {
						writer.write(doc);
						writer.flush();
					} finally {
						writer.close();
						fos.close();
					}
					log.trace("Done writing: " + outputfile);
				}
			}
		}	
		
	}
	
}
