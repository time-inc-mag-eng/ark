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
 * The controller logic for preview tab
 */
Ext.define("Ark.controller.Preview", {
	extend: 'Ark.controller.AbstractBaseItem',

	requires: [ 'Ark.lib.ErrorHandler'],

	containerSelector: '#preview',
	appendContainerRef: true,

	canUpdateCoverStory: false,
	optionalCoverStory: false,

	refs: [ {
		selector : '#coverStoryText',
		ref : 'coverStory'
	},{
		selector: '#uploadBtn',
		ref : 'uploadBtn'
	}, {
		selector: '#newsStandText',
		ref: 'newsstand'
	}],

	init : function() { // add event listeners that pertain to preview tab	
		this.control({
			'#preview #resetBtn' : {
				click : this.reset
			},
			'#preview #uploadBtn' : {
				click : this.upload
			},
			'#preview #newsStandText' : { 
				beforerender: this.addValidatorToNewsstandText
			}, 
			'#coverStoryText' : {
				beforerender: this.addValidatorToCoverStory
			}
		});

		this.callParent(arguments);
	},
	
	addValidatorToCoverStory: function(coverComp) {
		var me = this;

		coverComp.validator = function(val) {
			
			if(me.optionalCoverStory == true && Ext.isEmpty(val))
				return true;
			
			if(Ext.isEmpty(val))
				return "Required";
			else {
				var maxChar = Number.MAX_VALUE;
				
				me.getGrid().getStore().each(function(rec) {	
					var appRec = this.getApplication().getStore().getById(rec.get('applicationId'));
					maxChar = Math.min(maxChar, appRec.get('maxCoverStory'));
				},me);
				
				if(val.length < 10 || val.length > maxChar)  
					return "Min 10 / Max " + maxChar + " characters";
				else 
					return true;				
			}	
		};
	},
	

	addValidatorToNewsstandText: function(newsTextComp) { // custom validation for newstand text area; min 10 characters - max 800 characters if user enter atleast some text
		newsTextComp.validator = function(val) {
			if(val && val.length > 0) {
				if(val.length < 10 || val.length > 800) 
					return "Min 10 / Max 800 characters";
				else 
					return true;
			}
			
			return true;
		};
		
	},		
	
	upload : function() {
		var form = this.getFormPanel().getForm();
		var me = this;
		if(form.isValid()) {
			form.submit({
				url: 'restricted/previewUpload.noah',
				timeout: 600,
				method: 'POST',
				scope: this,
				params: {
					metaIds: me.metaIds().toString()
				},
				submitEmptyText : false,
				waitTitle:'Processing preview image(s)', 
				waitMsg:'Please wait...',

				success: function(form,action) { 

					var me = this;

					Ext.Msg.show({
						title: 'Upload Preview Status',
						autoScroll: true,
						msg: action.result.result,
						width: 600,
						buttons: Ext.Msg.OK,
						fn: function() {
							Ext.apply(me.getGrid().getStore().getProxy().extraParams,{ // add pubid parameter to request
								issueId: me.getIssue().getValue(),
								rtype: 'deep',
								action: 'baseitems',
								metaIds: me.metaIds() });    
							me.getGrid().getStore().load();
						}
					});				
				},
				failure: function(form, action) {
					Ark.lib.ErrorHandler.handleFormError(action, me.application);
				}	          	
			});
		}
	},
	
	getFetchType: function() {
		return "deep";
	},
	
	reset: function() {
		this.callParent(arguments);
		this.getCoverStory().disable();
		this.getNewsstand().disable();
	},

	handleRemoteException: function() {
		this.reset();
	},

	onApplicationSelectHandler : function() {
		Ext.apply(this.getGrid().getStore().getProxy().extraParams,{
			rtype: 'deep',
			action: 'baseitems'
			});    

		this.getUploadBtn().disable();
	},

	applicationRemoveHandler : function(store, record) {
		this.callParent(arguments);
		this.checkCoverStory();
	},

	issueGridLoaded : function(records) {
		this.checkCoverStory();

		this.getUploadBtn().enable(); 
		this.getNewsstand().enable();
	},
	
	checkCoverStory : function() { // private
		this.canUpdateCoverStory = true;
		this.optionalCoverStory = true;

		this.getGrid().getStore().each(function(rec) {	
			if (rec.get('modifiable') != null && rec.get('modifiable') == false) 
				this.canUpdateCoverStory = false;
			
			if(Ext.isEmpty(rec.get('coverStory'))) 
				this.optionalCoverStory = false;
			
		}, this);
		
		// only enable the cover story text area if all the associated issues has no cds entries
		// note that devices such as nook will never have an associated entry
		this.getCoverStory().setDisabled(!this.canUpdateCoverStory && this.optionalCoverStory);
	}
});
