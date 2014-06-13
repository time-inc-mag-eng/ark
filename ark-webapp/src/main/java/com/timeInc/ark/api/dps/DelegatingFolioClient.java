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
package com.timeInc.ark.api.dps;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.timeInc.ark.issue.Article;
import com.timeInc.ark.issue.FolioRemoteData;
import com.timeInc.ark.issue.Folio;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.issue.Resolution;
import com.timeInc.ark.parser.content.dps.ArticleMapping;
import com.timeInc.ark.parser.content.dps.FolioProducerUtil;
import com.timeInc.dps.producer.ManagedProducer;
import com.timeInc.dps.producer.ManagedProducers;
import com.timeInc.dps.producer.enums.FolioIntent;
import com.timeInc.dps.producer.enums.Viewer;
import com.timeInc.dps.producer.http.request.binding.CreateFolioRequest;
import com.timeInc.dps.producer.http.request.binding.DeleteArticleRequest;
import com.timeInc.dps.producer.http.request.binding.DeleteFolioRequest;
import com.timeInc.dps.producer.http.request.binding.DeleteHtmlResourceRequest;
import com.timeInc.dps.producer.http.request.binding.FolioInfoRequest;
import com.timeInc.dps.producer.http.request.binding.GetArticleListRequest;
import com.timeInc.dps.producer.http.request.binding.UpdateArticleRequest;
import com.timeInc.dps.producer.http.request.binding.UpdateFolioRequest;
import com.timeInc.dps.producer.http.request.binding.UploadArticleRequest;
import com.timeInc.dps.producer.http.request.binding.UploadHtmlResourceRequest;
import com.timeInc.dps.producer.http.request.binding.UploadPreviewRequest;
import com.timeInc.dps.producer.request.config.CloseSessionConfig;
import com.timeInc.dps.producer.request.config.CreateFolioConfig;
import com.timeInc.dps.producer.request.config.DeleteArticleConfig;
import com.timeInc.dps.producer.request.config.DeleteFolioConfig;
import com.timeInc.dps.producer.request.config.DeleteHtmlResourceConfig;
import com.timeInc.dps.producer.request.config.FolioInfoConfig;
import com.timeInc.dps.producer.request.config.GetArticleListConfig;
import com.timeInc.dps.producer.request.config.OpenSessionConfig;
import com.timeInc.dps.producer.request.config.UpdateArticleConfig;
import com.timeInc.dps.producer.request.config.UpdateFolioConfig;
import com.timeInc.dps.producer.request.config.UploadArticleConfig;
import com.timeInc.dps.producer.request.config.UploadAssetConfig;
import com.timeInc.dps.producer.response.ArticleInfo;
import com.timeInc.dps.producer.response.CreateFolio;
import com.timeInc.dps.producer.response.FolioInfo;
import com.timeInc.dps.producer.response.UploadArticle;
import com.timeInc.folio.parser.article.sidecar.ArticleAccess;

/**
 * The Class DelegatingFolioClient.
 */
public class DelegatingFolioClient implements ArkFolioClient {
	private static final Logger log = Logger.getLogger(DelegatingFolioClient.class);
	
	private final ManagedProducer client;
	
	/**
	 * Instantiates a new delegating folio client.
	 *
	 * @param producer the producer
	 */
	public DelegatingFolioClient(ManagedProducer producer) {
		this.client = producer;
	}
	
	/**
	 * Instantiates a new delegating folio client.
	 *
	 * @param address the address
	 * @param consumerKey the consumer key
	 * @param consumerSecret the consumer secret
	 * @param retryCount the retry count
	 */
	public DelegatingFolioClient (String address, String consumerKey, String consumerSecret, int retryCount) {
		this(ManagedProducers.getHttpRetryingClient(address,consumerKey,consumerSecret,retryCount, new FixedRetryHandler(retryCount)));
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.api.dps.ArkFolioClient#openSession(java.lang.String, java.lang.String)
	 */
	@Override
	public void openSession(String userName, String password) {
		OpenSessionConfig config = new OpenSessionConfig(userName,password,false);
		client.open(config);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.api.dps.ArkFolioClient#closeSession()
	 */
	@Override
	public void closeSession() {
		client.close(new CloseSessionConfig());
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.api.dps.ArkFolioClient#deleteArticle(com.timeInc.ark.issue.Article)
	 */
	@Override
	public void deleteArticle(Article article) {
		DeleteArticleConfig config = new DeleteArticleConfig(article.getFolio().getFolioId(),article.getArticleId());
		DeleteArticleRequest request = new DeleteArticleRequest(config);

		client.sendRequest(request);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.api.dps.ArkFolioClient#uploadArticle(com.timeInc.ark.issue.Folio, com.timeInc.ark.parser.content.dps.ArticleMapping)
	 */
	@Override
	public String uploadArticle(Folio folio, ArticleMapping articleMapping) {
		Article article = articleMapping.getArticle();
		UploadArticleConfig config = new UploadArticleConfig(folio.getFolioId(),articleMapping.getFile(),article.getSortOrder(),FilenameUtils.getBaseName(articleMapping.getFile().getAbsolutePath()));
		UploadArticleRequest request = new UploadArticleRequest(config);

		UploadArticle info = client.sendRequest(request);
		
		return info.getArticleInfo().getId();
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.api.dps.ArkFolioClient#setArticleAccess(com.timeInc.ark.issue.Folio, java.lang.String, com.timeInc.folio.parser.article.sidecar.ArticleAccess)
	 */
	@Override
	public void setArticleAccess(Folio folio, String articleId, ArticleAccess access) {
		switch(access) {
			case NONE: 
				log.debug("Not setting access since access type of:" + ArticleAccess.NONE + " detected");
				break;
			default:
				client.sendRequest(new UpdateArticleRequest(
						new UpdateArticleConfig.Builder(folio.getFolioId(), articleId)
							.withAccess(FolioProducerUtil.convertAccessFrom(access))
							.build()));	
		}
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.api.dps.ArkFolioClient#createFolio(com.timeInc.ark.issue.IssueMeta, com.timeInc.ark.issue.Resolution, com.timeInc.dps.producer.enums.FolioIntent, java.lang.String, com.timeInc.dps.producer.enums.Viewer, java.lang.String)
	 */
	@Override
	public String createFolio(IssueMeta meta, Resolution resolution, FolioIntent intent, String targetViewer, Viewer rendition, String pubName ) {
		CreateFolioConfig config = new CreateFolioConfig.Builder(meta.getReferenceId(), pubName, meta.getIssueName(), resolution.getWidth(), resolution.getHeight())
		.withFolioIntent(intent)
		.withCoverDate(meta.getCoverDate())
		.withPublicationDate(meta.getOnSaleDate())
		.withTargetViewer(targetViewer)
		.withViewer(rendition)
		.build();

		CreateFolioRequest request = new CreateFolioRequest(config);

		CreateFolio response = client.sendRequest(request);

		return response.getFolioID();
	}
	
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.api.dps.ArkFolioClient#updateFolio(com.timeInc.ark.issue.IssueMeta, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateFolio(IssueMeta meta, String targetViewer, String pubName) {
		UpdateFolioConfig config = new UpdateFolioConfig.Builder(meta.getFolio().getFolioId())
									.withCoverDate(meta.getCoverDate())
									.withMagazineTitle(pubName)
									.withPublicationDate(meta.getOnSaleDate())
									.withTargetViewer(targetViewer)
									.build();
		
		UpdateFolioRequest request = new UpdateFolioRequest(config);

		client.sendRequest(request);
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.api.dps.ArkFolioClient#uploadPreview(java.io.File, java.io.File, com.timeInc.ark.issue.Folio)
	 */
	@Override
	public void uploadPreview(File portrait, File landscape, Folio folio) {
		UploadAssetConfig porCfg = new UploadAssetConfig(folio.getFolioId(), portrait);
		client.sendRequest(UploadPreviewRequest.forPortrait(porCfg));

		UploadAssetConfig lanCfg = new UploadAssetConfig(folio.getFolioId(), landscape);
		client.sendRequest(UploadPreviewRequest.forLandscape(lanCfg));
		
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.api.dps.ArkFolioClient#updateFolioDescription(java.lang.String, com.timeInc.ark.issue.Folio)
	 */
	@Override
	public void updateFolioDescription(String description, Folio folio) {
		UpdateFolioConfig config = new UpdateFolioConfig.Builder(folio.getFolioId())
									.withFolioDescription(description)
									.build();

		UpdateFolioRequest request = new UpdateFolioRequest(config);
		client.sendRequest(request);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.api.dps.ArkFolioClient#getFolioInfo(com.timeInc.ark.issue.Folio)
	 */
	@Override
	public FolioRemoteData getFolioInfo(Folio folio) {
		FolioInfoConfig config = new FolioInfoConfig(folio.getFolioId());
		
		FolioInfoRequest request = new FolioInfoRequest(config);
		FolioInfo info = client.sendRequest(request).getFolio();
		
		return new FolioRemoteData(info.getFolioDescription(), info.getTargetViewer());
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.api.dps.ArkFolioClient#deleteHtmlResourceIfAny(com.timeInc.ark.issue.Folio)
	 */
	@Override
	public void deleteHtmlResourceIfAny(Folio folio) {
		DeleteHtmlResourceConfig config = new DeleteHtmlResourceConfig(folio.getFolioId());
		DeleteHtmlResourceRequest request = new DeleteHtmlResourceRequest(config);
		client.sendRequest(request);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.api.dps.ArkFolioClient#uploadHtmlResources(com.timeInc.ark.issue.Folio, java.io.File)
	 */
	@Override
	public void uploadHtmlResources(Folio folio, File locationOfHtmlResource) {
		UploadHtmlResourceRequest request = new UploadHtmlResourceRequest(new UploadAssetConfig(folio.getFolioId(),locationOfHtmlResource));
		client.sendRequest(request);
	}

	/* (non-Javadoc)
	 * @see com.timeInc.ark.api.dps.ArkFolioClient#getArticleInfo(com.timeInc.ark.issue.Folio)
	 */
	@Override
	public List<ArticleInfo> getArticleInfo(Folio folio) {
		return client.sendRequest(new GetArticleListRequest(new GetArticleListConfig(folio.getFolioId()))).getArticlesInOrder();
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.api.dps.ArkFolioClient#deleteFolio(com.timeInc.ark.issue.Folio)
	 */
	@Override 
	public void deleteFolio(Folio folio) {
		client.sendRequest(new DeleteFolioRequest(new DeleteFolioConfig(folio.getFolioId())));
	}
}
