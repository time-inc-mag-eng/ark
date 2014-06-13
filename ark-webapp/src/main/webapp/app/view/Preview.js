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
Ext.define('Ark.view.Preview', {
	extend: 'Ext.form.Panel',

	requires: [  'Ark.view.ApplicationMulti', 'Ark.view.Publication', 'Ark.view.Issue'],

	alias: 'widget.preview',
	itemId: 'preview',
	title: 'Upload Preview',

	layout:'anchor',
	autoScroll:true,
	defaultType: 'textfield',
	
	defaults: {
		margin: '5 10 5 0',
		msgTarget: 'side',
		labelWidth: 120, 
		anchor:'100%'
	},

	items: [{ // add items to our form container
		xtype: 'publication'
	}, {
		xtype:'issue'
	}, {
		height: 230,
		xtype: 'application-multi'	
	}, {
		xtype: 'filefield',
		name: 'form-file',
		emptyText: 'Select preview file',
		fieldLabel: 'Preview File',
		buttonText	: '',
		buttonConfig: {iconCls: 'upload-icon'},
		allowBlank	: false			
	}, {
		xtype: 'textarea',
		itemId: 'coverStoryText',
		fieldLabel: 'Cover Story',
		disabled: true,
		emptyText: 'Initial upload requires a cover story',
		//anchor: '-20',
		name: 'cover-story'
	}, {
		xtype: 'textarea',
		itemId: 'newsStandText',
		fieldLabel: 'Newsstand Summary',
		disabled: true,
		emptyText: 'Optional summary for newsstand apps only - if empty the cover story will be used',
		//anchor: '-20',
		name: 'newsstand-summary'
	},  {
		xtype: 'grid', 
		itemId: 'metagrid',
		hidden: true,	
		store: {
			type: 'issuemetas'
		},
		style: 'margin-top: 20px;',
		title: 'Information Uploaded',
		height:200,
		columns: [{
    		header: Ark.lib.Constant.applicationHeaderName,  dataIndex: 'applicationId',
			flex: 2
		}, {
			header: Ark.lib.Constant.referenceIdHeaderName,  
			dataIndex: 'referenceId',
			renderer: function(val) {
				return '<div style="white-space:normal !important;">'+ val +'</div>';
			},
			flex: 5
		}, {
			header: Ark.lib.Constant.saleDateHeaderName,  	
			dataIndex: 'onSaleDate',
			flex: 3,
			renderer: Ark.lib.Util.utcToLocaleTimeRenderer('F d, Y - g:i a T')
		}, {
			header: 'Cover Story',  
			dataIndex: 'coverStory', 
			itemId:'coverstory', 
			renderer: function(val) {
				return '<div style="white-space:normal !important;">'+ val +'</div>';
			},
			flex: 8
		}]
	}],

	buttons: [{
		text: 'Upload',
		itemId: 'uploadBtn'
	}, {
		text: 'Reset',
		itemId: 'resetBtn'	
	}] 

});

