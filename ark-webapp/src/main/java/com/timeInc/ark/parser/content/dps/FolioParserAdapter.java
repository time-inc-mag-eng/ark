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
package com.timeInc.ark.parser.content.dps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.timeInc.ark.issue.Article;
import com.timeInc.ark.issue.Folio;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.issue.Resolution;
import com.timeInc.ark.parser.AbstractSanitizer;
import com.timeInc.ark.parser.Parser.ContentParser;
import com.timeInc.ark.parser.content.ContentException;
import com.timeInc.ark.parser.content.dps.FolioInfo.FolioComparisonInfo;
import com.timeInc.dps.producer.enums.FolioIntent;
import com.timeInc.folio.parser.FolioFile;
import com.timeInc.folio.parser.FolioMetaData;
import com.timeInc.folio.parser.FolioParser;
import com.timeInc.folio.parser.MetaDataFileReader;
import com.timeInc.folio.parser.MetaDataFinder;
import com.timeInc.folio.parser.RootFolioMetaDataFinder;
import com.timeInc.folio.parser.SingleFolioParser;
import com.timeInc.folio.parser.XmlFolioMetaDataReader;
import com.timeInc.folio.parser.article.ArticleFile;
import com.timeInc.folio.parser.article.ArticleParser;
import com.timeInc.folio.parser.article.ArticleXmlMetaDataReader;
import com.timeInc.folio.parser.article.MinAccessVersion;
import com.timeInc.folio.parser.article.SingleArticleParser;
import com.timeInc.folio.parser.article.sidecar.SideCarFinder;
import com.timeInc.folio.parser.article.sidecar.XmlSideCarReader;
import com.timeInc.folio.parser.exception.ParserException;
import com.timeInc.folio.parser.validation.FolioValidator;
import com.timeInc.folio.parser.validation.Validation;
import com.timeInc.mageng.util.file.FileExistValidator;
import com.timeInc.mageng.util.file.FileValidator;
import com.timeInc.mageng.util.misc.Status;

/**
 * A parser where the input is an expanded folio file.
 * Determines what to do based on the existing state of {@link Folio}
 * 
 * @throws ContentException if there is a mismatch somehow between an existing folio
 * @see FolioComparer#compare(Folio, List) 
 */
public class FolioParserAdapter extends AbstractSanitizer<FolioInfo> implements ContentParser<FolioInfo>{
	
	/** The Constant META_FINDER. */
	public static final MetaDataFinder META_FINDER = new RootFolioMetaDataFinder();
	
	private static final MetaDataFinder SIDE_CAR_FINDER = new SideCarFinder();
	private static final String XML_FILE_NAME = "Folio.xml";

	
	private final FolioComparer folioComparer;
	private final FileValidator fileValidator = new FileExistValidator();
	private final MetaDataFileReader<FolioMetaData> folioReader;
	
	
	/**
	 * Instantiates a new dps folio adapter.
	 *
	 * @param folioComparer the folio comparer
	 * @param folioReader the folio reader
	 */
	public FolioParserAdapter(FolioComparer folioComparer, MetaDataFileReader<FolioMetaData> folioReader) {
		this.folioComparer = folioComparer;
		this.folioReader = folioReader;
	}
	
	/**
	 * Instantiates a new dps folio adapter.
	 */
	public FolioParserAdapter() {
		this(new FolioComparer(), new XmlFolioMetaDataReader());
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.Parser.ContentParser#validate(java.io.File)
	 */
	@Override
	public Status validate(File contentDirectory) {
		File rootXML = new File(contentDirectory, XML_FILE_NAME);
		return fileValidator.validate(rootXML);
	}
	
	@Override
	protected FolioInfo parseContentAfterSanitized(File rootContentFolderPath, IssueMeta meta) {
		File folioMetaFile = META_FINDER.getMetaDataFile(rootContentFolderPath);
		
		FolioMetaData metaData = folioReader.read(folioMetaFile);
		
		Resolution resolution = new Resolution(metaData.getHeight(),metaData.getWidth());
		FolioIntent orientation = FolioIntent.getIntent(metaData.getOrientation());
		
		boolean newFolio = meta.getFolio() == null;
		Folio folio = !newFolio ? meta.getFolio() : new Folio(orientation, resolution, false);
		
		if(folio.getOrientation() != orientation) 
			throw new ContentException("Orientation mismatch - Current:" + folio.getOrientation() + " New:" + orientation);
			
		if(!folio.getResolution().equals(resolution)) 
			throw new ContentException("Resolution mistmatch - Current:" + folio.getResolution() + " New:" + resolution);
		

		if(newFolio) meta.setFolio(folio);
		
		try {
			FolioComparisonInfo comparison = folioComparer.compare(folio, metaData.getContentStackIds());
			
			FolioFile folioFile = getFolioParser(comparison.getArticles()).parse(metaData, rootContentFolderPath);
		
			List<ArticleMapping> mapping = new ArrayList<ArticleMapping>(comparison.getArticles().size());
			
			for(Article article : comparison.getArticles()) {
				ArticleFile articleFile = folioFile.getArticleFile(article.getDossierId());
				mapping.add(new ArticleMapping(article, articleFile.getArticle(), articleFile.getMetaData().getAccess()));
			}
			
			return new FolioInfo(mapping, comparison.getAction(), folioFile.getHtmlResource(), folioFile.getMaxTargetVersion(), resolution, orientation);
			
		} catch(ParserException pe) {
			throw new ContentException(pe.getMessage(), pe);
		}
	}	
	
	private static final FolioParser getFolioParser(final List<Article> articles) { // ensure that the articles to perform an action on exists
		return new SingleFolioParser(new FolioValidator() {
			@Override
			public Validation validate(FolioMetaData metaData, File contentDirectory) {
				for(Article article : articles) {
					File dossierIdDirectory = new File(contentDirectory, article.getDossierId());
					if(!dossierIdDirectory.exists())
						return Validation.getInvalid("Folder for dossier id " + article.getDossierId() + " does not exist");
				}
				
				return Validation.getValid();
			}
		}, getArticleParser());
	}
	
	private static final ArticleParser getArticleParser() {
		return new SingleArticleParser(META_FINDER, new ArticleXmlMetaDataReader(), SIDE_CAR_FINDER, new XmlSideCarReader(), new MinAccessVersion());
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.Parser.ContentParser#getType()
	 */
	@Override
	public String getType() {
		return "dps";
	}
	
}
