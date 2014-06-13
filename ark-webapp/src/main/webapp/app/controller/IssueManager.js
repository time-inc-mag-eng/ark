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
 * The controller for the admin tab view. See Ark.view.Admin for more details about the components.
 * Provides logic for creating the issue name for a specific publication group and creating
 * the issue meta data for a certain application whose issue name is restricted by the set of issue name's
 * created in the create issue name for publication tab.
 */
Ext.define("Ark.controller.IssueManager", {
	extend: 'Ext.app.Controller',
	
	containerSelector: '#issue-manager',

	constructor: function(config) {	
		var me = this;
		
		baseRefs = [{ 
			selector: me.containerSelector + ' #subtab',
			ref: 'subTab'
		}, {
			selector: me.containerSelector + ' #issueNameTab publication',   // the combo box in the create issue name for publication sub-tab
			ref: 'issueNamePub'
		}, {
			selector: me.containerSelector + ' #issueNameTab #issueNameGrid', // the grid in create issues tab
			ref: 'createIssueGrid'		
		}, {
			selector: me.containerSelector + ' #createIssueTab publication', // the application component in create issues tab
			ref: 'issueCreatePub'		
		}, {
			selector: me.containerSelector + ' #createIssueTab #application', // the application component in create issues tab
			ref: 'issueCreateApp'		
		}, {
			selector: me.containerSelector + ' #createIssueTab #issueGrid', // the grid in create issues tab
			ref: 'issueCreateGrid'		
		}, {
			selector: me.containerSelector + ' #createIssueTab #issueGrid gridcolumn[dataIndex=issueNameId]', // the issue name column in the create issues grid
			ref: 'issueCreateGridIssueName'		
		}, {
			selector: me.containerSelector + ' #createIssueTab #issueGrid gridcolumn[dataIndex=paymentId]', // the payment column in the grid
			ref: 'issueCreateGridPayment'				
		}, {
			selector: me.containerSelector + ' #createIssueTab #issueGrid gridcolumn[dataIndex=referenceId]', // the referenceId column in the grid
			ref: 'issueReferenceId'				
		}, {
			selector: me.containerSelector + ' #createIssueTab #pagingTb', // paging toolbar
			ref: 'pagingToolBar'				
		}, {
			selector: me.containerSelector + ' #createIssueTab #issueGrid gridcolumn[dataIndex=testIssue]', // the referenceId column in the grid
			ref: 'testIssue'				
		}, {
			selector: me.containerSelector + ' #createIssueTab #issueGrid gridcolumn[dataIndex=shortDate]', // the referenceId column in the grid
			ref: 'shortDate'				
		}];
	
		var refs = me.refs;
		
		if(me.appendContainerRef) {
			if(refs) {
				for(var i=0;i<refs.length;i++) {
					refs[i].selector = me.containerSelector + ' ' + refs[i].selector;
				}
			}
		}
		
		me.refs = refs ? Ext.Array.merge(refs, baseRefs) : baseRefs; // merge our base refs with subclasses
		
		me.callParent(arguments);

	},	
	
	assignedIssue: null, // the issue assigned to the row before any changes where made
	refid: "", // this is used as the prefix of the reference id in the issue meta grid - it is updated when the user selects a certain application
	currentSelectedIssue: null, // keeps track of the current selected issue
	currentApp: null,
	
	currentIssueRecord: null, // keeps track of the currently modified issue in create issue tab
	
    init: function() { 
    	this.placeHolderIssue = Ext.create('Ark.model.Issue', {
    		id: -1,
    		name: 'PLACE HOLDER'    		
    	}),
    	
    	this.control({
    		/** Selectors for components within the create issue name tab **/
    		'#issueNameTab #publication' : { // selector for the publication component
    			select: this.issueNamePubSelect
    		}, 
    		
    		'#issueNameTab #issueNameAddBtn' : { 
    			click: this.addIssueName
    		},
    		
    		'#issueNameTab #issueNameDeleteBtn' : { 
    			click: this.removeIssueName
    		},
    		
    		'#issueNameTab #issueNameRefreshBtn' : { 
    			click: this.refreshIssueName
    		},
    		
    		'#issueNameTab #issueNameSaveBtn' : { 
    			click: this.saveIssueName
    		},
    		
    		'#issueNameTab #issueNameGrid' : {
    			beforerender: this.issueNameGridBeforeRender,
    			beforeedit: this.beforeRowEditIssueName
    		},
    		
    		/** Selectors for components within the create issuemeta tab **/
    		'#createIssueTab publication' : { // selector for the publication component
    			select: this.issueCreatePubSelect
    		},   		
    		'#createIssueTab #application' : { // selector for the application component
    			select: this.issueCreateAppSelect
    		},
    		'#createIssueTab #issueGrid' : { // selector for the grid panel
    			beforerender: this.issueCreateGridBeforeRender,
    			beforeedit: this.beforeRowEdit,
    			edit: this.rowEdit,
    			canceledit: this.cancelEdit
    		},
    		'#createIssueTab #issueGrid #removeFilterBtn' : { // selector for remove issue button
    			click: this.resetFilter
    		},
    		'#createIssueTab #issueGrid #issueAddBtn' : { // selector for add issue button
    			click: this.addIssue
    		}, 
    		'#createIssueTab #issueGrid #issueDeleteBtn' : { // selector for issue remove button
    			click: this.removeIssue
    		},
    		'#createIssueTab #issueGrid #issueSaveBtn': { // selector for save issues button
    			click: this.saveIssue
    		}
    	});  
    	
        this.application.on({
        	remoteInfoException: this.handleRemoteInfoException,
        	scope: this
        });
    },
    
    
    handleRemoteInfoException: function() {
    	var activeTab = this.getSubTab().getActiveTab();
    	
    	if(Ext.ComponentQuery.is(activeTab, this.containerSelector + ' ' + '#createIssueTab')) {
    		if(!this.getIssueCreateGrid().isDisabled()) {
    			this.getIssueCreateGrid().getStore().removed = [];
        		this.getPagingToolBar().doRefresh();
        	}
    	} else if(Ext.ComponentQuery.is(activeTab, this.containerSelector + ' ' + '#issueNameTab')) {
    		if(!this.getCreateIssueGrid().isDisabled()) {
    			this.getCreateIssueGrid().getStore().removed = [];
    			this.getCreateIssueGrid().getStore().load();
    		}
    	}
    },
    
    handleRemoteException: function() {
    	var activeTab = this.getSubTab().getActiveTab();
    	
    	if(Ext.ComponentQuery.is(activeTab, this.containerSelector + ' ' + '#createIssueTab')) {
    		this.resetCreateIssue();
    	} else if(Ext.ComponentQuery.is(activeTab, this.containerSelector + ' ' + '#issueNameTab')) {
    		this.resetIssueNameForm();
    	}
    },
    
         
    /*****************************
     * Handlers for create issue name tab
     *****************************/
    
    
    /**
     * Resets the form in the create issue name for publication tab
     */
    resetIssueNameForm: function() {
    	this.getCreateIssueGrid().disable();
       	this.getIssueNamePub().lastQuery = null; 
    },
    
    /**
     * Handler when the publication drop down is selected.
     * Basically enables the text field to be edited
     * @param pubComp the publication combo box
     */
	issueNamePubSelect: function(pubComp) {
		var grid = this.getCreateIssueGrid();
		grid.enable(); // enable the grid and fetch the new data
		
		var issueNameStore = grid.getStore();
		
		Ext.apply(issueNameStore.getProxy().extraParams,{ // add req parameters to retrieve the values in the issue name drop box
			pubId: pubComp.getValue(), // this should return the issue names that dont have any associated issue meta for that application
			action: 'admin'
		});			
		
		issueNameStore.load();
	},
	
	addIssueName: function() {
    	var defaultDate = new Date();
    	defaultDate.setHours(0,0,0,0);
    	
    	var newIssue = { // default values for the new issue
    		shortDate: defaultDate,
    		name: '',
    		testIssue: 0
    	};
    	    	
    	var rowEditor = this.getCreateIssueGrid().getPlugin('rowEditing');
    	
    	if(rowEditor.editing) // if there is a current edit; don't do anything just return
    		return false;
    	
    	rowEditor.startAdd(newIssue,0); // insert the new row into the the beginning and invoke the row editor
    	
	},
	
	
    removeIssueName: function() {	
		var grid = this.getCreateIssueGrid();
    	var rowEditor = grid.getPlugin('rowEditing');   	
    	rowEditor.cancelEdit();
    	
    	var sm = grid.getSelectionModel();
    	Ext.Array.forEach(sm.getSelection(),function(model) {
    		grid.getStore().remove(model); // remove automatically refreshes the grid's view
    	});
    },
    
    refreshIssueName: function() {
    	this.getCreateIssueGrid().getStore().load();
    },
    
    saveIssueName: function() {
    	var store = this.getCreateIssueGrid().getStore();
    	
    	store.sync({
    	    callback: function(){
    	    	Ext.Msg.alert('Status', 'Changes saved successfully.', function() {
    	    		store.load();
    	    	});
    	    }
    	});
    	
    },
    
    
    issueNameGridBeforeRender: function() {
    	
    	var grid = this.getCreateIssueGrid();
    	var nameField = grid.headerCt.down('gridcolumn[dataIndex=name]').getEditor();
    	
    	var me = this;
    	
    	nameField.validator = function(val) {
        	var currentRecord = grid.getSelectionModel().getSelection()[0];
        	
        	if(currentRecord) {
	    		var issueName = nameField.getValue().toUpperCase();
	    		var currentName = me.currentIssueRecord.get('name').toUpperCase();
	    		
	    		var recs = grid.getStore().queryBy(function(record, id) {
					return issueName == record.get('name').toUpperCase() && record.get('name').toUpperCase() != currentName;
				});

	    		if(recs.length > 0)
	    			return "Duplicate Name";
	    		else
	    			return true;
        	}
    		return true;
    	};	    	
    },
    
    beforeRowEditIssueName: function(e) {    	
    	if(e)
    		this.currentIssueRecord = e.record;
    },    
	
	
	/*************************************
	 * Handlers for the create issues tab
	 *******************************/
    
	
	resetCreateIssue: function() {
		this.getIssueCreateGrid().disable();
		this.getIssueCreateApp().disable();
		this.getIssueCreatePub().reset();
		this.getIssueCreatePub().lastQuery = null; 
	},

	/**
	 * Saves any dirty instances within the grid's store
	 * by syncing it.
	 */	
    saveIssue: function() {
    	var gridStore = this.getIssueCreateGrid().getStore();
    	var isError = false;
    	
    	gridStore.each(function(rec) {
    		if(rec.get('issueNameId') == this.placeHolderIssue.get('id')) {
    			Ext.MessageBox.alert('Error', 'Any modified issue can not have its issue name set to ' + this.placeHolderIssue.get('name'));
    			isError = true;
    			return false;
    		}
    	},this);
    	
    	
    	
    	var store = this.getIssueCreateGrid().getStore();
    	var me = this;
    	
    	if(!isError) {
    	  	store.sync({
        	    callback: function(){
        	    	Ext.Msg.alert('Status', 'Changes saved successfully.', function() {
        	    		me.getPagingToolBar().doRefresh();
        	    	});
        	    }
        	});
    	}
    },
    
  
    
    /**
     * Removes the issue from the grid and its store
     */
    removeIssue: function() {	
    	var grid = this.getIssueCreateGrid();
    	var rowEditor = grid.getPlugin('rowEditing');   	
    	rowEditor.cancelEdit();
    	
    	var sm = grid.getSelectionModel();
    	var issueNameStore = this.getIssueCreateGridIssueName().getEditor().getStore();	
    	Ext.Array.forEach(sm.getSelection(),function(model) {
    		var removedIssue = Ext.create('Ark.model.Issue', {
    			id: model.get('issueNameId'),
    			name: model.get('issue'),
    			testIssue: model.get('testIssue')
    		});

    	    issueNameStore.add(removedIssue);	
    		grid.getStore().remove(model); // remove automatically refreshes the grid's view
    	});
    },
    
    /**
     * Handler before a row in the grid is edited.
     * Adds the current issue name for that row to the issue name store otherwise there would be no mapping 
     * in it's store.
     * @param e the edit event - See extjs docs
     * @returns false if the row show not be edited; true otherwise
     */
    beforeRowEdit: function(e) {    	
    	var me = this;
    	   
    	var issueNameStore = me.getIssueCreateGridIssueName().getEditor().getStore(); // put the current row's issue name in the issue name store in order to display its values 
    	
    	issueNameStore.remove(me.assignedIssue); 
    	
    	var currentSelectIssueName = Ext.create('Ark.model.Issue', {
        		id: e.record.get('issueNameId'),
        		name: e.record.get('issue'),
        		testIssue: e.record.get('testIssue'),
        		shortDate: e.record.get('shortDate')
    	});
    
    
    	if(!currentSelectIssueName.equals(me.placeHolderIssue)) {
        	issueNameStore.add(currentSelectIssueName);
        	me.assignedIssue = currentSelectIssueName; // need to keep track of this to know if the issue name has been modified
        	me.currentSelectedIssue = currentSelectIssueName; // also need to update this in the event a user does not select anything
    	} else {
    		me.assignedIssue = null;
    	}
    	
    	return true;
    },
    

    /**
     * If the user canceled the edit, remove any modifications
     * done to the issue name store. Specifically remove the row's issue name.
     */
    cancelEdit: function() {
    	var me = this; 	
    	var issueNameStore = me.getIssueCreateGridIssueName().getEditor().getStore(); // remove the rows previous selected issue from the issue name store
    	issueNameStore.remove(me.assignedIssue);
    	me.assignedIssue = null;
    },
    

    /**
     * Handler that is fired after the row is edited and validation has been performed.
     * Checks to see if the issue name for that particular row has been modified and if so
     * removes the issue name from the issue name combo box store.
     * @param e the edit event - See extjs docs
     */
    rowEdit: function(e) {
    	var me = this;
    	
    	var issueNameCombo = me.getIssueCreateGridIssueName().getEditor();
    	var issueNameStore = issueNameCombo.getStore();

    	var newIssueNameId = issueNameCombo.getValue();
    	
    	
    	if(me.assignedIssue && me.assignedIssue.get('id') == newIssueNameId) { // user didn't change the issue name or its a placeholder
    		issueNameStore.remove(me.assignedIssue);
    	} else {
    		var rec = issueNameStore.getById(newIssueNameId);
    		
        	e.record.set('issue',rec.get('name'));
        	e.record.set('issueNameId',rec.get('id'));  
        	e.record.set('testIssue',rec.get('testIssue'));
        	e.record.set('shortDate',rec.get('shortDate'));
        	if(!rec.equals(me.placeHolderIssue))
        		issueNameStore.remove(rec); 
    	}    	
    	
		me.assignedIssue = null;
    	
    },
    
    /**
     * Adds an issue to the grid and its store
     */
    addIssue: function() {
    	var me = this;
    	
    	var defaultDate = new Date();
    	defaultDate.setHours(0,0,0,0);
    	
    	var newIssue = { // default values for the new issue
    		onSaleDate: defaultDate,
    		active: true,
    		testIssue: me.getIssueCreateGridIssueName().getEditor().getStore().first().get('testIssue'),
    		paymentId: 1,
    		applicationId: me.getIssueCreateApp().getValue(),
    		issueNameId: me.getIssueCreateGridIssueName().getEditor().getStore().first().get('id'),
    		issue: me.getIssueCreateGridIssueName().getEditor().getStore().first().get('name'),
    		referenceId: me.refid + "." + Ext.Date.format(new Date(),'mdY'),
    		price: me.currentApp.get('defaultPrice')
    	};
    	    	
    	var rowEditor = this.getIssueCreateGrid().getPlugin('rowEditing');
    	
    	if(rowEditor.editing) // if there is a current edit; don't do anything just return
    		return false;
    	
    	rowEditor.startAdd(newIssue,0); // insert the new row into the the beginning and invoke the row editor

    },
    
    /**
     * Resets any applied filters (shortdate and onsaledate) 
     * to the grid.
     */
    resetFilter: function() {
    	this.getIssueCreateGrid().filters.clearFilters();
    },
    
    
    /**
     * Handler before the grid is rendered. Provides custom initialization and validator
     * to the grid.
     */
    issueCreateGridBeforeRender: function() { 
    	
    	this.getIssueCreateGridIssueName().renderer = function (value, meta, record, rowIndex, colIndex, store) { // the display field for the issue name column
    		return record.get('issue');
    	};
    	
    	var paymentStore = 	this.getIssueCreateGridPayment().getEditor().getStore();

    	this.getIssueCreateGridPayment().renderer = function (value, meta, record, rowIndex, colIndex, store) { // the display field for the payment column
    		return paymentStore.getById(value).get('name');
    	};
    	 	    	  	
    	var gridStore = this.getIssueCreateGrid().getStore(); // link the grid store to the paging store	
    	
    	var pagingBar = this.getIssueCreateGrid().child('pagingtoolbar'); 
    	pagingBar.bindStore(gridStore); 
    	
    	
    	/* validation to ensure short date is in between a one year difference of sale date */
    	var saleDate = this.getIssueCreateGrid().headerCt.down('gridcolumn[dataIndex=onSaleDate]').getEditor().down('datefield');
    	
    	var me = this;
    	saleDate.validator = function(val) {
    		if(me.currentSelectedIssue) {
	    		var isTest = me.currentSelectedIssue.get('testIssue');
	    		if(isTest) {
	    			var saleDateYear = new Date(val).getFullYear();
	    			if(saleDateYear < 2020)
	    				return 'A test issue must have year >= 2020';
	    			else 
	    				return true;
	    		}
    		}

    		return true;
    	};	 
    	

		var issueNameStore = this.getIssueCreateGridIssueName().getEditor().getStore();

		
		this.getIssueCreateGrid().getStore().on('beforeload', function() { // load the issue name store before the grid's store to get any new values 
			issueNameStore.load();
			paymentStore.load(); 
		});  
		
		issueNameStore.on('load', function() {
			issueNameStore.insert(0,this.placeHolderIssue); // add a placeholder issue in the case where all issue names are taken
		},this);
		
		var issueNameComp = this.getIssueCreateGridIssueName().getEditor();
		issueNameComp.on('select',this.issueNameSelectHandler,this);
		
		
    },
    
    issueNameSelectHandler: function(issueComp,recs) {
    	var text = recs[0].get('testIssue') == true ? 'Yes' : 'No';
    	var shortDate = recs[0].get('shortDate');
    	
    	var shortDateText = Ext.util.Format.date(shortDate,'m/d/Y g:i a T');
    	this.currentSelectedIssue = recs[0];
    	this.getTestIssue().getEditor().setValue(text);
    	this.getShortDate().getEditor().setValue(shortDateText);
    	
    	this.getIssueReferenceId().getEditor().setValue(this.refid + '.' + Ext.Date.format(new Date(shortDate),'mdY'));
    },
    
    
    /**
     * Handler for the publication select event within
     * the create issue tab.
     * @param pubComp
     */
    issueCreatePubSelect: function(pubComp) {    	
		this.getIssueCreateGrid().disable(); // disable the grid
		
    	var appComp = this.getIssueCreateApp();
		
    	Ext.apply(appComp.getStore().getProxy().extraParams, { // set parameters that fetch the list of applications for a certain pub
			pubId: pubComp.getValue(),
			action: 'single'});
		
		appComp.getStore().load();
		
    	this.getIssueCreateApp().enable();
    },
	
	/** 
	 * Handler for the create issue tab
	 * when the select event is fired by the application combo box
	 * @param appComp
	 */
	issueCreateAppSelect: function(appComp) {
		var appID = appComp.getValue();
	
		this.refid = appComp.getStore().getById(appID).get('referenceId_Header'); // update the current application reference id
		this.currentApp = appComp.getStore().getById(appID);
		
		this.resetFilter(); // remove the filter from the grid
		this.getIssueCreateGrid().enable(); // enable the grid and fetch the new data
		
		var issueNameStore = this.getIssueCreateGridIssueName().getEditor().getStore();
		
		Ext.apply(issueNameStore.getProxy().extraParams,{ // add req parameters to retrieve the values in the issue name drop box
			pubId: this.getIssueCreatePub().getValue(), // this should return the issue names that dont have any associated issue meta for that application
			action: 'admin',
			appId: this.getIssueCreateApp().getValue()});		
		
		Ext.apply(this.getIssueCreateGrid().getStore().getProxy().extraParams,{ // add req parameters to populate the grid
			action: 'admin',
			appId: this.getIssueCreateApp().getValue()});   

		this.getIssueCreateGrid().getStore().loadPage(1);	
	}
});
