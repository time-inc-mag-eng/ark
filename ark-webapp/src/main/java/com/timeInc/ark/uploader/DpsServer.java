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
package com.timeInc.ark.uploader;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.timeInc.ark.api.dps.ArkFolioClient;
import com.timeInc.ark.api.dps.ArkFolioClientFactory;
import com.timeInc.ark.issue.Article;
import com.timeInc.ark.issue.Folio;
import com.timeInc.ark.issue.FolioRemoteData;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.issue.PaymentType;
import com.timeInc.ark.packager.content.DpsContentInfo;
import com.timeInc.ark.packager.preview.DpsPreviewPackInfo;
import com.timeInc.ark.parser.content.dps.ArticleMapping;
import com.timeInc.ark.publish.FolioPublisher;
import com.timeInc.ark.publish.Publisher.PublishType;
import com.timeInc.dps.producer.enums.Viewer;
import com.timeInc.dps.producer.response.ArticleInfo;
import com.timeInc.dps.translator.ResponseHandlerException;
import com.timeInc.mageng.util.exceptions.PrettyException;
import com.timeInc.mageng.util.misc.Status;
import com.timeInc.mageng.util.progress.ProgressListener;
import com.timeInc.mageng.util.string.StringUtil;

/**
 * Main entry point for communicating with Adobe DPS. Uploads content and previews to Adobe DPS.
 * Publishes and unpublishes folio.
 * 
 */
public class DpsServer implements ContentUploader<DpsContentInfo>, PreviewUploader<DpsPreviewPackInfo> {
	private static final Logger log = Logger.getLogger(DpsServer.class);

	private Integer id;
	
	private String address;
	private String dpsUsername;
	private String dpsPassword;	

	private String consumerSecret;
	private String consumerKey;
	
	private int timesToRetry;
	
	private ArkFolioClientFactory clientFactory;	
	
	private final DpsServerListener contentListener;
	
	private volatile ArkFolioClient folioClient; // use getClient() since this is lazily init
	
	private FolioPublisher publisher;

	/**
	 * Instantiates a new dps server.
	 *
	 * @param clientFactory the client factory
	 * @param contentListener the content listener
	 * @param publisher the publisher
	 */
	public DpsServer(ArkFolioClientFactory clientFactory, DpsServerListener contentListener, FolioPublisher publisher) {
		this.clientFactory = clientFactory;
		this.contentListener = contentListener;
		this.publisher = publisher;
	}
	
	/**
	 * Instantiates a new dps server.
	 */
	public DpsServer() {
		this.contentListener = new IssueMetaFolioUpdater();
	}
	
	/**
	 * Upload a content package, providing progress updates.
	 * Must be done within an active session.
	 * @see #doInSession(DpsRequest)
	 * @param packData the package file
	 * @param meta 
	 * @param listener
	 */
	@Override
	public void uploadContent(DpsContentInfo packData, IssueMeta meta) {
		uploadContent(packData, meta, null);
	}	
	
	/**
	 * Upload a content package
	 * Must be done within an active session.
	 * @see #doInSession(DpsRequest) 
	 * 
	 * @param packData the package file
	 * @param meta 
	 */
	@Override
	public void uploadContent(DpsContentInfo packData, IssueMeta meta, ProgressListener listener) {
		
		Folio folio = meta.getFolio();
		
		for(ArticleMapping mapping : packData.getPackedArticles()) { 
			contentListener.articleAdded(folio, mapping.getArticle());
		}
		
		int totalArticles = packData.getTotalAssetCount();
		int uploadCount = 0;

		for(ArticleMapping mapping : packData.getPackedArticles()) {
			String articleId = getClient().uploadArticle(folio, mapping);
			
			Article article = mapping.getArticle();
			
			log.debug("Uploaded article: " + article.getDossierId() + " " + articleId);
			
			contentListener.articleUpdated(article, articleId);
			
			getClient().setArticleAccess(folio, articleId, mapping.getAccess());
			
			if(listener != null)
				listener.inProgress(totalArticles, ++uploadCount);
		}
		
		if(packData.getHtmlResource() != null) {
			getClient().uploadHtmlResources(meta.getFolio(), packData.getHtmlResource());
			contentListener.htmlResourceAdded(folio);
			
			if(listener != null)
				listener.inProgress(totalArticles, ++uploadCount);
		}		
	}

	/**
	 * Creates the folio on dps.
	 * Must be done within an active session.
	 * @see #doInSession(DpsRequest)
	 *
	 * @param meta the meta that has an associated folio
	 * @param info the info
	 * @param viewer the viewer
	 * @param pubName the pub name
	 */
	public void createFolio(IssueMeta meta, DpsContentInfo info, Viewer viewer, String pubName) {
		log.debug("Creating folio for " + meta.getReferenceId() + " with rendition " + viewer.toString());
		
		String folioId = getClient().createFolio(meta,info.getResolution(),info.getOrientation(),info.getTargetViewer(), viewer, pubName);
		contentListener.folioCreated(meta.getFolio(), folioId);
	}
	
	/**
	 * Update a folio meta information in dps
	 * Must be done within an active session.
	 * @see #doInSession(DpsRequest)
	 * 
	 * @param meta the meta that has an associated folio which also exists in dps
	 * @param targetViewer the target viewer
	 * @param pubName the pub name
	 */
	public void updateFolio(IssueMeta meta, String targetViewer, String pubName) {
		getClient().updateFolio(meta, targetViewer, pubName);
	}
	
	/**
	 * Delete html resources.
	 * Must be done within an active session.
	 * @see #doInSession(DpsRequest)
	 * 
	 * @param folio the folio
	 */
	public void deleteHtmlResources(Folio folio) {
		log.debug("Deleting any html resources for existing folio");
		
		getClient().deleteHtmlResourceIfAny(folio);
		
		contentListener.htmlResourceRemoved(folio);
	}
	
	/**
	 * Delete all articles associated with a folio from dps
	 * Must be done within an active session.
	 * @see #doInSession(DpsRequest)
	 * 
	 * @param folio the folio
	 */
	public void deleteArticles(Folio folio) {
		for(Article article : folio.getOrderedArticles()) 
			deleteArticle(article);
		
		contentListener.articlesRemoved(folio);
	}
	
	/**
	 * Delete article from dps.
	 * Only article with non-null {@link Article#getArticleId()} is deleted
	 * Must be done within an active session.
	 * @see #doInSession(DpsRequest)
	 * 
	 * @param article the article
	 */
	public void deleteArticle(Article article) {
		if(article.getArticleId() != null) {
			getClient().deleteArticle(article);
			contentListener.articleRemoved(article);
		}
	}
	
	/**
	 * Upload a preview package
	 * Must be done within an active session.
	 * @see #doInSession(DpsRequest)
	 * 
	 * @param data the package file
	 * @param meta 
	 */
	@Override
	public void uploadPreview(DpsPreviewPackInfo data, IssueMeta meta) {
		File previewDir = data.previewImageDir();
		getClient().uploadPreview(new File(previewDir, data.getPortFileName()), new File(previewDir, data.getLandFileName()), meta.getFolio());
	}
	
	/**
	 * Sets the cover story for a folio.
	 * Must be done within an active session.
	 * @see #doInSession(DpsRequest)
	 * 
	 * @param folio the folio
	 * @param coverStory the cover story
	 */
	public void setCoverStoryFor(Folio folio, String coverStory) {
		getClient().updateFolioDescription(coverStory, folio);
	}
	
	/**
	 * Gets remote information for a folio
	 * Must be done within an active session.
	 * @see #doInSession(DpsRequest)
	 * 
	 * @param folio the folio
	 * @return the remote information
	 */
	public FolioRemoteData getFolioInfo(Folio folio) {
		if(folio == null)
			return null;
		
		return getClient().getFolioInfo(folio);
	}
	
	/**
	 * Gets remote articles information for a folio
	 * Must be done within an active session.
	 * @see #doInSession(DpsRequest)
	 * 
	 * @param folio the folio
	 * @return the article
	 */
	public List<ArticleInfo> getArticle(Folio folio) {
		return getClient().getArticleInfo(folio);
	}
	
	
	/**
	 * Delete folio from dps.
	 * Must be done within an active session.
	 * @see #doInSession(DpsRequest)
	 * 
	 * @param folio the folio
	 */
	public void deleteFolio(Folio folio) {
		getClient().deleteFolio(folio);
	}

	
	/**
	 * Unpublishes a folio from dps
	 *
	 * @param meta the meta
	 * @return the status
	 */
	public Status unpublish(IssueMeta meta) {
		return publisher.unpublish(dpsUsername, dpsPassword, meta.getFolio().getFolioId(), meta.getApplication().getName(), meta.getApplication().getProductId());
	}
	
	/**
	 * Publishes a folio to dps
	 *
	 * @param publishType the publish type
	 * @param meta the meta
	 * @param userEmail the user email
	 * @param rendition the rendition to publish as
	 * @return the status
	 */
	public Status publish(PublishType publishType, IssueMeta meta, String userEmail, Viewer rendition) {
		String folioId = meta.getFolio().getFolioId();
		String productId = meta.getReferenceId();
		String issueName = meta.getIssueName();
		
		boolean retail = true;
		
		if(meta.getPaymentType().getName().equals(PaymentType.FREE))
			retail = false;
		
		return publisher.publish(dpsUsername, dpsPassword, folioId, rendition, productId, publishType, retail, userEmail, issueName, meta.getOnSaleDate());
	}
	
	
	/**
	 * Make dps request(s) that is considered a unit of work.
	 *
	 * @param <T> return type after performing the work
	 */
	public interface DpsRequest<T> { 
		T execute();
	}
	
	/**
	 * Opens a dps session to execute the
	 * {@link DpsRequest}.
	 *
	 * @param work the work that needs to be executed within a dps session.
	 * @throws PrettyException containing the error status and description if the dps requests returned an error. 
	 * @return the the result of the work
	 */
	public <T> T doInSession(DpsRequest<T> work) {
		boolean isOpen = false;
		try {
			open();
			isOpen = true;
			return work.execute();
		} catch(ResponseHandlerException ex) {
			
			String msg = StringUtil.isEmpty(ex.getDescription()) ? ex.getErrorStatus() : ex.getDescription();
			
			throw new PrettyException("Error request to dps:" + msg);
		} finally {
			if(isOpen)
				close();
		}
	}
	
	private void open() {
		log.trace("Opening session using username:" + dpsUsername);
		getClient().openSession(dpsUsername, dpsPassword);
	}
	
	private void close() {
		log.trace("Closing session using username:" + dpsUsername);
		getClient().closeSession();
		
	}
	
	private ArkFolioClient getClient() {
		synchronized(this) { 
			if(folioClient == null) {
				folioClient = clientFactory.getClientUsing(address, consumerKey, consumerSecret, timesToRetry);
			}
		}
		return folioClient;
	}
	
	/**
	 * Sets the folio publisher.
	 *
	 * @param publisher the new publisher
	 */
	public void setPublisher(FolioPublisher publisher) {
		this.publisher = publisher;
	}
	
	
	/**
	 * Sets the client factory.
	 *
	 * @param clientFactory the new client factory
	 */
	public void setClientFactory(ArkFolioClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() { return address; }
	
	/**
	 * Gets the dps username.
	 *
	 * @return the dps username
	 */
	public String getDpsUsername() { return dpsUsername; }
	
	/**
	 * Gets the dps password.
	 *
	 * @return the dps password
	 */
	public String getDpsPassword() { return dpsPassword; }
	
	/**
	 * Gets the consumer secret.
	 *
	 * @return the consumer secret
	 */
	public String getConsumerSecret() { return consumerSecret; }
	
	/**
	 * Gets the consumer key.
	 *
	 * @return the consumer key
	 */
	public String getConsumerKey() { return consumerKey; }
	
	/**
	 * Gets the times to retry.
	 *
	 * @return the times to retry
	 */
	public int getTimesToRetry() {
		return timesToRetry;
	}

	/**
	 * Sets the times to retry.
	 *
	 * @param timesToRetry the new times to retry
	 */
	public void setTimesToRetry(int timesToRetry) {
		this.timesToRetry = timesToRetry;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Sets the dps username.
	 *
	 * @param dpsUsername the new dps username
	 */
	public void setDpsUsername(String dpsUsername) {
		this.dpsUsername = dpsUsername;
	}

	/**
	 * Sets the dps password.
	 *
	 * @param dpsPassword the new dps password
	 */
	public void setDpsPassword(String dpsPassword) {
		this.dpsPassword = dpsPassword;
	}

	/**
	 * Sets the consumer secret.
	 *
	 * @param consumerSecret the new consumer secret
	 */
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	/**
	 * Sets the consumer key.
	 *
	 * @param consumerKey the new consumer key
	 */
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}
}
