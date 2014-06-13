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
package com.timeInc.ark.backup;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.naming.Naming;
import com.timeInc.mageng.util.compression.Compression;
import com.timeInc.mageng.util.compression.Zip;
import com.timeInc.mageng.util.exceptions.PrettyException;
import com.timeInc.mageng.util.file.FileUtil;

/**
 * Saves a file to a local destination
 */
public class LocalStorage implements Storage {

	/* (non-Javadoc)
	 * @see com.timeInc.ark.backup.Storage#save(java.io.File, java.io.File)
	 */
	@Override
	public void save(File filetoBackup, File destinationFile) {
		try {
			FileUtil.copyFile(filetoBackup, destinationFile);
		} catch (IOException e) {
			throw new PrettyException("Failed to copy " + filetoBackup + " -> " + destinationFile, e);
		}
	}

	/**
	 * Gets the content location.
	 *
	 * @param root the root
	 * @return the content location
	 */
	public static Location getContentLocation(File root) {
		return new DefaultLocation(new ContentNaming(), root, new Zip());
	}

	/**
	 * Gets the preview location.
	 *
	 * @param root the root folder
	 * @return the preview location
	 */
	public static Location getPreviewLocation(File root) {
		return new DefaultLocation(new PreviewNaming(), root, new Zip());
	}

	/**
	 * Generates the location of an uploaded file with a fixed extension
	 * if the file to be backed up is of that Compression type.
	 * i.e. if it was a Compression was a ZipCompression then the extension will always be
	 * zip.
	 */
	public static class DefaultLocation implements Location {
		private final Naming fileNaming;
		private final Naming locationNaming;

		private final File root;
		
		private final Compression comp;

		private static final Naming DEFAULT_LOCATION_NAMING = new DefaultNaming();

		/**
		 * Instantiates a new default location scheme.
		 *
		 * @param fileNaming the file naming
		 * @param locationNaming the location naming
		 * @param root the root
		 * @param comp the comp
		 */
		public DefaultLocation(Naming fileNaming,
				Naming locationNaming, File root, Compression comp) {
			this.fileNaming = fileNaming;
			this.locationNaming = locationNaming;
			this.root = root;
			this.comp = comp;
		}

		/**
		 * Instantiates a new default location scheme.
		 *
		 * @param fileNaming the file naming
		 * @param root the root
		 * @param comp the comp
		 */
		public DefaultLocation(Naming fileNaming, File root, Compression comp) {
			this(fileNaming, DEFAULT_LOCATION_NAMING, root, comp);
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.backup.Storage.Location#getLocation(com.timeInc.ark.issue.IssueMeta, java.io.File)
		 */
		@Override
		public File getLocation(IssueMeta meta, File filetoBackup) {
			File backUpLocation = new File(root, locationNaming.getName(meta));

			String name = fileNaming.getName(meta);
			
			if(comp.isValid(filetoBackup)) {
				return new File(backUpLocation, name + "." + comp.getExtension());
			} else {
				String ext = FilenameUtils.getExtension(filetoBackup.getName());
				ext = ext.isEmpty() ? "" : "." + ext;
				
				return new File(backUpLocation, name + ext);
			}
		}

		/**
		 * The Class LocationBackup.
		 */
		public static class DefaultNaming implements Naming  {
			
			/* (non-Javadoc)
			 * @see com.timeInc.ark.naming.Naming#getName(com.timeInc.ark.issue.IssueMeta)
			 */
			@Override
			public String getName(IssueMeta meta) {
				return meta.getPublication().getName() + File.separator
						+ meta.getApplicationName() + File.separator 
						+ meta.getIssueName().replaceAll("[^\\p{L}\\p{N}]","_") + File.separator;
			}
		}
	}
	
	
	/**
	 * The Class ContentNaming.
	 */
	public static class ContentNaming implements Naming {
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.naming.Naming#getName(com.timeInc.ark.issue.IssueMeta)
		 */
		@Override
		public String getName(IssueMeta meta) {
			return "Content";
		}
	}

	/**
	 * The Class PreviewNaming.
	 */
	public static class PreviewNaming implements Naming {
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.naming.Naming#getName(com.timeInc.ark.issue.IssueMeta)
		 */
		@Override
		public String getName(IssueMeta meta) {
			return "Preview";
		}
	}

}
