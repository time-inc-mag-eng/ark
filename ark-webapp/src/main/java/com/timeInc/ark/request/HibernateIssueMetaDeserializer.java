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
package com.timeInc.ark.request;

import java.util.ArrayList;
import java.util.List;

import com.timeInc.ark.application.Application;
import com.timeInc.ark.dao.ApplicationDAO;
import com.timeInc.ark.dao.HibernateIssueMetaDAO;
import com.timeInc.ark.dao.IssueDAO;
import com.timeInc.ark.dao.PaymentDAO;
import com.timeInc.ark.issue.Issue;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.issue.PaymentType;

/**
 * Converts an IssueMetaRequest to an IssueMeta by populating the associations
 * an IssueMetaRequest which is by id to actual objects.
 */
public class HibernateIssueMetaDeserializer {
	private final HibernateIssueMetaDAO issueMetaDao;
	private final IssueDAO issueDAO;
    private final ApplicationDAO appDAO;
    private final PaymentDAO paymentDAO;
    
	/**
	 * Instantiates a new issue meta deserializer.
	 *
	 * @param issueMetaDao the issue meta dao
	 * @param issueDAO the issue dao
	 * @param appDAO the app dao
	 * @param paymentDAO the payment dao
	 */
	public HibernateIssueMetaDeserializer(HibernateIssueMetaDAO issueMetaDao, IssueDAO issueDAO,
			ApplicationDAO appDAO, PaymentDAO paymentDAO) {
		this.issueMetaDao = issueMetaDao;
		this.issueDAO = issueDAO;
		this.appDAO = appDAO;
		this.paymentDAO = paymentDAO;
	}
	
	
	/**
	 * Gets the single instance of IssueMetaDeserializer.
	 *
	 * @param requests the requests
	 * @return single instance of IssueMetaDeserializer
	 */
	public List<IssueMeta> getInstance(List<IssueMetaRequest.Persisted> requests) {
		List<IssueMeta> metas = new ArrayList<IssueMeta>();
		
		for(IssueMetaRequest.Persisted request : requests) {
			IssueMeta meta = issueMetaDao.read(request.id);
			updateIssueMeta(meta,request);
			issueMetaDao.detach(meta);
			
			metas.add(meta);
		}
		
		return metas;
	}
	
	/**
	 * Gets the new instance.
	 *
	 * @param requests the requests
	 * @return the new instance
	 */
	public List<IssueMeta> getNewInstance(List<IssueMetaRequest> requests) {
		List<IssueMeta> metas = new ArrayList<IssueMeta>();
		
		for(IssueMetaRequest request : requests) {
			IssueMeta meta = new IssueMeta();
			updateIssueMeta(meta,request);
			
			metas.add(meta);
		}
		
		return metas;		
	}
	
	private void updateIssueMeta(IssueMeta meta, IssueMetaRequest request) {
		if(request.applicationId != null) {
			Application<?,?> app = appDAO.read(request.applicationId);
			meta.setApplication(app);
		}
		
		if(request.paymentId != null) {
			PaymentType payment = paymentDAO.read(request.paymentId);
			meta.setPayment(payment);
		}
		
		if(request.issueNameId != null) {
			Issue issue = issueDAO.read(request.issueNameId);
			meta.setIssue(issue);
		}
		
		if(request.active != null) {
			meta.setActive(request.active);
		}
		
		if(request.onSaleDate != null) {
			meta.setOnSaleDate(request.onSaleDate);
		}
		
		if(request.volume != null) {
			meta.setVolume(request.volume);
		}
		
		if(request.price != null) {
			meta.setPrice(request.price);
		}
		
		if(request.referenceId != null) {
			meta.setReferenceId(request.referenceId);
		}
	}
}
