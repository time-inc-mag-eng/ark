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
package com.timeInc.ark.upload.producer;

import java.io.File;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.packager.PackageInfo;
import com.timeInc.ark.packager.Packager;
import com.timeInc.ark.packager.SingleFileInfo;
import com.timeInc.ark.packager.SingleNamingPackager;
import com.timeInc.ark.parser.Parser.ContentParser;
import com.timeInc.ark.parser.SingleContent;
import com.timeInc.mageng.util.exceptions.PrettyException;
import com.timeInc.mageng.util.misc.Status;

/**
 * Links together a parser with a packager for content uploads.
 *
 */
public class ContentProducer<T extends PackageInfo,V> extends PackageProducer<T,V> {
	private ContentParser<V> parser;
	private Packager<T,V> packer;
	
	protected ContentProducer() {}
	
	/**
	 * Instantiates a new content producer.
	 *
	 * @param parser the parser
	 */
	public ContentProducer(Packager<T, V> packer, ContentParser<V> parser) {
		this.parser = parser;
		this.packer = packer;
	}
	
	@Override
	protected final V parse(File content, IssueMeta meta) {
		Status validStatus = getParser().validate(content);
		
		if(validStatus.isError())
			throw new PrettyException("Validation failed for content:" + validStatus.getDescription());
		
		return getParser().parse(content, meta);
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.upload.producer.PackageProducer#getParser()
	 */
	@Override
	public ContentParser<V> getParser() {
		return parser;
	}
	
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.upload.producer.PackageProducer#getPackager()
	 */
	@Override
	public Packager<T, V> getPackager() {
		return packer;
	}
	
	/**
	 * Sets the parser.
	 *
	 * @param contentParser the new parser
	 */
	public void setParser(ContentParser<V> contentParser) {
		this.parser = contentParser;
	}
	
	/**
	 * Content producer for CDSApplication
	 */
	public static class CdsContentProducer extends ContentProducer<SingleFileInfo, SingleContent> {
		private SingleNamingPackager packer;
		
		protected CdsContentProducer() {}
		
		/**
		 * Instantiates a new cds content producer.
		 *
		 * @param packer the packer
		 * @param parser the parser
		 */
		public CdsContentProducer(SingleNamingPackager packer, ContentParser<SingleContent> parser) {
			super(packer, parser);
			this.packer = packer;
		}
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.upload.producer.PackageProducer#getPackager()
		 */
		@Override
		public SingleNamingPackager getPackager() {
			return packer;
		}
	}
}

