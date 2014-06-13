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
import com.timeInc.ark.packager.preview.CdsPreviewPackInfo;
import com.timeInc.ark.packager.preview.CdsPreviewPackager;
import com.timeInc.ark.packager.preview.DpsPreviewPackInfo;
import com.timeInc.ark.packager.preview.DpsPreviewPackager;
import com.timeInc.ark.parser.Parser.PreviewParser;
import com.timeInc.ark.parser.SingleContent;
import com.timeInc.ark.parser.preview.PreviewParsedData;
import com.timeInc.mageng.util.exceptions.PrettyException;
import com.timeInc.mageng.util.misc.Status;

/**
 * Links together a parser with a packager for preview uploads.
 *
 */
public class PreviewProducer<T extends PackageInfo> extends PackageProducer<T, PreviewParsedData>  {
	private PreviewParser<PreviewParsedData> parser;
	protected Packager<T, PreviewParsedData> packer;
	
	protected PreviewProducer() {}
	
	/**
	 * Instantiates a new preview producer.
	 *
	 * @param packer the packer
	 * @param parser the parser
	 */
	public PreviewProducer(Packager<T, PreviewParsedData> packer, PreviewParser<PreviewParsedData> parser) {
		this.packer = packer;
		this.parser = parser;
	}

	@Override
	protected final PreviewParsedData parse(File content, IssueMeta meta) {
		PreviewParsedData parsedData = getParser().parse(content, meta);
		Status validation = getParser().validate(parsedData, meta);
		
		if(validation.isError())
			throw new PrettyException("Validation failed for previews:" + validation.getDescription());
		
		return parsedData;
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.upload.producer.PackageProducer#getParser()
	 */
	@Override
	public PreviewParser<PreviewParsedData> getParser() {
		return parser;
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.upload.producer.PackageProducer#getPackager()
	 */
	@Override
	public Packager<T, PreviewParsedData> getPackager() {
		return packer;
	}
	
	/**
	 * Preview producer for CdsApplication
	 */
	public static class CdsPreviewProducer extends PreviewProducer<CdsPreviewPackInfo> {
		protected CdsPreviewProducer() {
			this.packer = new CdsPreviewPackager();
		}
		
		/**
		 * Instantiates a new cds preview producer.
		 *
		 * @param packer the packer
		 * @param parser the parser
		 */
		public CdsPreviewProducer(Packager<CdsPreviewPackInfo, PreviewParsedData> packer, PreviewParser<PreviewParsedData> parser) {
			super(packer,parser);
		}
	}
	
	/**
	 * Preview producer for DpsApplication
	 */
	public static class DpsPreviewProducer extends PreviewProducer<DpsPreviewPackInfo> {
		protected DpsPreviewProducer() {
			this.packer = new DpsPreviewPackager();
		}
		
		/**
		 * Instantiates a new dps preview producer.
		 *
		 * @param packer the packer
		 * @param parser the parser
		 */
		public DpsPreviewProducer(Packager<DpsPreviewPackInfo, PreviewParsedData> packer, PreviewParser<PreviewParsedData> parser) {
			super(packer,parser);
		}
	}
	
	/**
	 * Preview producer for an application that returns a parsed type of {@link SingleFileInfo} and produces a package of type
	 * {@link SingleContent}
	 */
	public static class SinglePreviewProducer extends PackageProducer<SingleFileInfo, SingleContent> {
		private PreviewParser<PreviewParsedData> parser;
		protected Packager<SingleFileInfo, SingleContent> packer;
		
		protected SinglePreviewProducer() {}
		
		/**
		 * Instantiates a new single preview producer.
		 *
		 * @param packer the packer
		 * @param parser the parser
		 */
		public SinglePreviewProducer(Packager<SingleFileInfo, SingleContent> packer, PreviewParser<PreviewParsedData> parser) {
			this.packer = packer;
			this.parser = parser;
		}
		
		@Override
		protected PreviewParsedData parse(File content, IssueMeta meta) {
			PreviewParsedData parsedData = getParser().parse(content, meta);
			getParser().validate(parsedData, meta);
			return parsedData;
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.upload.producer.PackageProducer#getParser()
		 */
		@Override
		public PreviewParser<PreviewParsedData> getParser() {
			return parser;
		}

		/* (non-Javadoc)
		 * @see com.timeInc.ark.upload.producer.PackageProducer#getPackager()
		 */
		@Override
		public Packager<SingleFileInfo, SingleContent> getPackager() {
			return packer;
		}
		
		/**
		 * Sets the packager.
		 *
		 * @param packager the packager
		 */
		public void setPackager(Packager<SingleFileInfo, SingleContent> packager){
			this.packer=packager;
		}
	}
}
