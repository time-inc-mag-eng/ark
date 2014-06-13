/*******************************************************************************
*Copyright 2014 Time Inc
*
*Licensed under the Apache License, Version 2.0 (the "License");
*you may not use this file except in compliance with the License.
*You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing, software
*distributed under the License is distributed on an "AS IS" BASIS,
*WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*See the License for the specific language governing permissions and
*limitations under the License.
******************************************************************************/



/**
 * The controller logic for content tab
 */
Ext.define('Ark.controller.Content', {
	extend: 'Ark.controller.AbstractBaseItem',
	
	requires: ['Ark.lib.ErrorHandler', 'Ark.lib.User'],
	
	statics: {
		PROGRESS_ID: "id",
		ISSUE_META_ID: "metaIssueId",
		POLL_PROGRESS_MS: 10000
	},
		
	refs: [{
		selector: '#submitBtn', 
		ref: 'uploadBtn'
	}, {
		selector: '#resetBtn',
		ref: 'resetBtn'
	}, {
		selector: '#publishBtn',
		ref: 'publishBtn'
	}, {
		selector: '#contentfile',
		ref: 'uploadFileField'
	}, {
		selector: '#newUpload',
		ref: 'newUpload'
	}],
	
	requires: ['Ext.ux.grid.column.Component', 'Ark.model.ProgressStatus'],
	
	containerSelector: '#content',
	
	appendContainerRef: true,
	
	validProgressMetaIssueIds: new Array(),
	
    init: function() { 
    	this.control({
    		'#content #resetBtn' : { 
    			click: this.resetState
    		},
    		'#content #submitBtn' : { 
    			click: this.submitForm
    		},
    		'#content #publishBtn' : {
    			click: this.publishCds
    		}, 
    		'#content' : {
    			progressComplete: this.progressDone,
    			polledProgress: this.updateProgress
    		}
    		
    	});
    	
    	this.callParent(arguments);   	
    },
    
    publishCds: function() {
    	Ext.MessageBox.show({
    		msg: 'Publishing issue(s)...',
    		width:200,
    		title: 'Please wait',
    		wait:true,
    		closable: false,
    		waitConfig: {interval:200}
    	});
    	
    	var me = this;
    	
		Ext.Ajax.request({
			url: 'restricted/publish.noah',
			
			params: {
				metaIds: this.metaIds().toString()
			},
			
			success: function(response) {	
				var jsonArray = Ext.decode(response.responseText).result;	
				
            	Ext.Array.forEach(jsonArray,function(jsonObj) {    
            		var record = me.getGrid().getStore().getById(jsonObj['issueId']);
            		record.set('processing', jsonObj.publishResult);
            		
            		if(jsonObj.publishDate)
            			record.set('publishedDate', jsonObj.publishDate);
            	});
            	
				Ext.MessageBox.hide();

				me.setDisabledComponents(false);		
			},
			
			failure: function(response) { // failure for ajax request means an http response other than 200
				Ark.lib.ErrorHandler.handleError(response, me.application);
			}
		});
    	
    },
    
    beforeRenderGrid: function(grid) {
    	grid.addColumn({
    		header: 'Status',
    		dataIndex: 'processing', 
    		xtype: 'componentcolumn', 
    		flex: 4,
    		renderer: function(value, m, record) {  
    			if(Ext.isDefined(value)) { 
    				if(Ext.typeOf(value) === 'string') {
    					return '<div style="white-space:normal !important;">'+ value +'</div>';
    				} else {
    					if(!value.get('isDone')) {
    						return {
    							animate: false,
    							value: value.get('percent') / 100,
    							xtype: 'progressbar',
    							text: value.get('description') + ' - ' + value.get('percent') + '%'
    						};
    					}	
    					else { // completed progress check to see if its an error or success
    						if(value.get('isError'))
    							return '<div style="white-space:normal !important;">'+ value.get('description') +'</div>';
    						else 
    							return 'Complete';
    					}   					
    				}
    			} else {
    				return 'N/A';
    			}
    		}     		
    	});
    	
    	this.callParent(arguments);   
    },
    
    submitForm: function() {   	
    	var me = this;

    	var form = me.getFormPanel().getForm();
    	
    	if(form.isValid()) {
    		me.validProgressMetaIssueIds = Ext.Array.clone(me.metaIds());
    		
    		me.getFormPanel().submit({
        		method: 'POST',
        		scope: me,
        		url: 'restricted/uploadContent.noah', // link to send to when the form is submitted
        		waitTitle:'Please wait', 
                waitMsg:'Uploading content file.....',   
                
                params: {
                	metaIds: me.validProgressMetaIssueIds.toString()
                },
                
                success: function(response) {
                	me.setDisabledComponents(true);
                },
                
                failure: function(form,action) {
                	Ark.lib.ErrorHandler.handleFormError(action, me.application);
                }	          	
        	});
    	};
    },
    
    updateProgress: function(status, id) {
    	var store = this.getGrid().getStore();
    	
    	var record = store.getAt(store.findExact('id', id));
    	
    	record.set('processing', status);
    },
    
    progressDone: function(successProgressArr) {
    	this.setDisabledComponents(false);
    	
    	if(Ext.isEmpty(successProgressArr))
    		this.getPublishBtn().setDisabled(true);
    },
        
    issueGridLoaded: function(records) {
    	this.setDisableFormButtons(false);
    },
    
    setDisabledComponents: function(isDisabled) {
    	this.getPublication().setDisabled(isDisabled);
    	this.getApplication().setDisabled(isDisabled);
    	this.getIssue().setDisabled(isDisabled);
    	this.getUploadFileField().setDisabled(isDisabled);
    	this.getResetBtn().setDisabled(isDisabled);
    	this.getPublishBtn().setDisabled(isDisabled);
    	this.getUploadBtn().setDisabled(isDisabled);    	
    },
    
    setDisableFormButtons: function(isDisabled) {
    	this.getUploadBtn().setDisabled(isDisabled);
    	this.getPublishBtn().setDisabled(isDisabled);
    },
    
    beforeProgress: function() { 
    	setDisabledComponents(true);
    },

    resetState: function() {
    	this.reset();
    	this.getResetBtn().enable();
    	this.setDisableFormButtons(true);
    },
    
    handleRemoteException: function() {
    	this.resetState();
    }, 
    
    onApplicationSelectHandler: function(store, record) {
    	this.setDisableFormButtons(true);
    },
    
    onApplicationRemoveHandler: function(store, record) {
    	if(store.data.length == 0) {
    		this.setDisableFormButtons(true);
    	}
    }
});
