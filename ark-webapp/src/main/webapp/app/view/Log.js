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
Ext.define('Ark.view.Log', {
	extend: 'Ext.form.Panel',

	requires: ['Ark.view.Application', 'Ark.view.Publication', 'Ark.store.Logs', 'Ext.ux.FileDownload'],

	title: 'Report',
	alias: 'widget.log',
	itemId: 'log',


	layout: {
		type: 'vbox',
		align : 'stretch',
		pack  : 'start'
	},

	defaults: {
		margin: '0 10 5 0',
		msgTarget: 'side',
		labelWidth: 120
	},
	items: [{
		xtype: 'publication' // the drop downs for publication and application
	}, {
		flex: 1,
		xtype: 'grid',
		itemId: 'logGrid',
		store: {
			type: 'logs'
		},
		disabled: true,
		forceFit: true,
		height: '100',
		tbar: [
		       // begin using the right-justified button container
		       '->', 

		       'Export', // same as {xtype: 'tbtext', text: 'text1'} to create Ext.toolbar.TextItem
		       '-', {
		    	   text: 'Excel',
		    	   itemId: 'excelBtn'
		       }],
		dockedItems: [{
		      xtype: 'pagingtoolbar',
		      dock: 'bottom',
		      displayInfo: true
		 }],       
		    
		columns: [{
		    	   text: Ark.lib.Constant.applicationHeaderName,
		    	   dataIndex: 'appName',
		    	   flex:2
		       }, {
		    	   text: 'Name',
		    	   flex: 3,
		    	   dataIndex: 'name'
		       }, {
		    	   text: Ark.lib.Constant.shortDateHeaderName,
		    	   flex: 2,
		    	   dataIndex: 'shortDate',
		    	   renderer: Ark.lib.Util.utcToLocaleTimeRenderer('m/d/Y')
		    	   
		       }, {
		    	   text: Ark.lib.Constant.issueNameHeaderName,
		    	   flex: 2,
		    	   dataIndex: 'issueName'
		    
		       }, {
		    	   text: Ark.lib.Constant.referenceIdHeaderName,
		    	   flex: 3.5,
		    	   dataIndex: 'referenceId'
		  
		       }, {
		    	   text: 'Action Date',
		    	   flex: 2.5,
		    	   dataIndex: 'date',
		    	   renderer: Ark.lib.Util.utcToLocaleTimeRenderer('m/d/Y g:i a T')
		       }, {
		    	   sortable: false,
		    	   text: 'Action',
		    	   flex: 3,
		    	   dataIndex: 'eventDescription'
		       }, {
		    	   text: 'Description',
		    	   flex: 5,
		    	   dataIndex: 'description',
		    	   renderer: function(value) {
		    		   if(Ext.isEmpty(value)) 
		    			   return 'N/A';
		    		   else 
		    			   return '<div style="white-space:normal !important;">'+ value +'</div>';
		    	   }
		       }]
	}, {
		xtype: 'FileDownloader',
		itemId: 'exportIframe',
		hidden: true
	}]
});
