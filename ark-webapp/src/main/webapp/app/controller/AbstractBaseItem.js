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
Controller for the base items 
Provides application logic for 'application' -> 'publication' -> 'issues'.
**/

Ext.define("Ark.controller.AbstractBaseItem", {
	extend: 'Ext.app.Controller',
	
	constructor: function(config) {	
		var me = this,
			baseRefs = [{
				ref: 'container', selector: me.containerSelector
			}, {
				ref: 'formPanel', selector: me.containerSelector // removed panel preserved for backward compatibility
			}, {
				ref: 'publication', selector: me.containerSelector + ' #publication'
			}, {
				ref: 'application', selector: me.containerSelector + ' #application'
			}, {
				ref: 'issue', selector: me.containerSelector + ' #issue'
			}, {
				ref: 'grid', selector: me.containerSelector + ' #metagrid'
			}];
	
		var refs = me.refs;
		
		if(me.appendContainerRef) {
			if(refs) {
				for(var i=0;i<refs.length;i++) {
					refs[i].selector = me.containerSelector + ' ' + refs[i].selector;
				}
			}
		}
		
		me.refs = refs ? Ext.Array.merge(refs,baseRefs) : baseRefs; // merge our base refs with subclasses
		
		me.callParent(arguments);
	},
	
	metaIds: function() {
		var me = this;
		var ids = new Array();
		
		me.getGrid().getStore().each(function(record) {
			ids.push(record.get('id'));
		});
		
		return ids;
	},
	
	init: function() { // add event listeners that pertain to the base item work flow
    	var me = this;
    	toControl = {
    			'#metagrid' : { 
    				beforerender: me.beforeRenderGrid
    			},
    			'#publication' : { 
    				select: me.publicationSelectHandler
    			}, 
    			'#application' : {
    				select: me.applicationSelectHandler,
    				remove: me.applicationRemoveHandler
    			},
    			'#issue' : {
    				select: me.issueSelectHandler
    			}
    	}; 	
    	
    	for(key in toControl) { // add our control selectors to a specific containerSelector
    		if(toControl.hasOwnProperty(key)) {
    			var params = {};
    			params[me.containerSelector + ' ' + key] = toControl[key];
    			this.control(params);
    		}
    	}
	},	
	
	publicationSelectHandler: function(pubComp) {
		var issueComp = this.getIssue();
		
		this.getApplication().setDisabled(true);
		
		Ext.apply(issueComp.getStore().getProxy().extraParams,{ // add pubid parameter to request
		pubId: pubComp.getValue(),
		action: 'baseitems'});
		
    	issueComp.getStore().load({
    		scope: this,
    		callback: function() {
    			issueComp.clearValue();
    			issueComp.clearInvalid();
    			issueComp.setDisabled(false);    			
    		}
    	});
    	
    	this.getGrid().hide();
		this.onPublicationSelect(pubComp);
	},
	
	issueSelectHandler: function(issueComp) {
		var applicationComp = this.getApplication();
		
		Ext.apply(applicationComp.getStore().getProxy().extraParams,{ 
			issueId: issueComp.getValue(),
			action: 'baseitems'});	
		
		applicationComp.getStore().load(function() { // the itemselector instance is created before, bind the store again
			applicationComp.bindStore(this);
			applicationComp.clearValue();
			applicationComp.clearInvalid();
			applicationComp.setDisabled(false);
		});	
		
    	this.getGrid().hide();
    	
    	this.onIssueSelect(issueComp);
	},
	

	applicationSelectHandler: function(store, records) {
		var me = this;
		
		
		Ext.apply(this.getGrid().getStore().getProxy().extraParams,{ 
			rtype: me.getFetchType(),
			action: 'baseitems',
			appId: me.getApplication().getValue(),
			issueId: me.getIssue().getValue()
		});    
		
		
		me.getGrid().show();	
		
		me.getGrid().getStore().load();
		
		me.onApplicationSelectHandler.apply(this, arguments); // subclasses can override previous settings performed by this method
	},
	
	getFetchType: function() {
		return "limited";
	},
	
    applicationRemoveHandler: function(store, record) { 
    	var appId = record.get('id');
    	
    	var rowIndex = this.getGrid().getStore().find('applicationId',appId);
    	
    	this.getGrid().getStore().removeAt(rowIndex);
      	
    	this.getGrid().getView().refresh();
      	
      	if(store.data.length == 0) {
      		this.getGrid().hide();
      	}
      	
      	this.onApplicationRemoveHandler(store, record);
    },
	
	
	beforeRenderGrid: function() {	
		var me = this;
		
		var appNameColumn = Ext.ComponentQuery.query(this.containerSelector + ' #metagrid gridcolumn[dataIndex=applicationId]')[0];
		
		appNameColumn.renderer = function(val) {
			return '<div style="word-wrap: break-word; white-space:normal !important;">'+ me.getApplication().getStore().getById(val).get('name') +'</div>';
		};
		
		var grid =  this.getGrid();
		
		grid.getStore().on('load', function(store,records) {
			me.issueGridLoaded(records);
			grid.enable();
		}, this);
	},
	
	onPublicationSelect: Ext.emptyFn,
	
	onApplicationSelectHandler: Ext.emptyFn,
	
	onApplicationRemoveHandler: Ext.emptyFn,
	
	onIssueSelect: Ext.emptyFn,
	
	gridStoreCallback: Ext.emptyFn,
    
	reset: function() {
		this.getFormPanel().getForm().reset();
		this.getApplication().disable();
		this.getIssue().disable();
		this.getPublication().lastQuery = null; 
		this.getGrid().el.unmask();
		this.getGrid().hide();
	}
});

