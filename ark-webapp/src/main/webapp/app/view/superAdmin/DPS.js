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
Ext.define('Ark.view.superAdmin.DPS', {
	extend:'Ext.panel.Panel',
	alias:'widget.dps', 
	id:'dpsType',
	name:'dpsType',
	itemId:'dpspanel',
	//cls:'accordion-panel',
	layout:'accordion',
	defaults:{
		bodyStyle:'background-color:#F6FAFF',
		autoScroll:true
	},
	height:'80%',	
	width:'100%',
	requires:['Ark.store.DpsServers', 'Ark.view.superAdmin.GeneralAppSettings'],
	deferredRender:true,	
	items:[{
		xtype:'form',
		title: 'Server Info',
		itemId: 'dpsserver',
		border:false,
		store:'DpsServers',
		layout:'anchor',
		defaults: {
			labelWidth: 200,
			anchor:'100%',
			xtype:  'textfield',
			margin : '5 20 0 20'
		},
		items: [ {
			fieldLabel:'User Name',
			name: 'userName',
			itemId:'userName',
			allowBlank:false,
			vtype:'email',
			margin : '20 20 0 20'
		},{
			fieldLabel:'Password',
			itemId:'password',
			name:'password',
			allowBlank:false
		},{
			fieldLabel:'Consumer Secret',
			itemId:'dpsConsumerSecret',
			name: 'dpsConsumerSecret',
			allowBlank:false
		},{
			fieldLabel:'Consumer Key',
			itemId:'dpsConsumerKey',
			name:'dpsConsumerKey',
			allowBlank:false

		},{
			fieldLabel:'API URL',
			itemId:'dpsUrl',
			name:'dpsUrl',
			allowBlank:false,
			vtype:'url'

		},{
			fieldLabel:'Retry Count',
			itemId:'dpsRetryCount',
			name: 'dpsRetryCount',
			regex: /[0-9]/,
			allowBlank:false

		},{
			fieldLabel:'Rendition Type',
			xtype:'combobox',
			itemId:'rendition',
			name:'rendition',
			store: new Ext.data.ArrayStore({
				id: 1,
				fields: ['name','value'],
				data: [[1, 'WEB'], [2, 'ANY']]  // data is local
			}),
			displayField: 'value',
			valueField:'value',
			queryMode: 'local',
			triggerAction:'all',
			allowBlank:false,
			forceSelection: true,
			typeAhead: true
		}]	//dpsserver
	},{

		xtype:'panel',
		title: 'General Settings',
		items:[{xtype:'appSettings'}]

	},{		
		xtype:'panel',
		title: 'Image Previews',
		items:[{xtype:'imagePreviewGrid',margin:'5'}]		
	},{
		xtype:'panel',
		title: 'Preview Email Settings',
		border:false,
		itemId: 'emailPreviewPanel',
		items: [{
			xtype: 'checkbox',		
			itemId: 'previewEmailCheckbox',
			name:'previewEmail',
			boxLabel: 'Send Email on Preview?',
			checked: false,
			labelWidth: 200,
			margin:'20 0 0 20'    
		},{
			xtype:'previewEmailSettings'
		}]
	},{
		xtype:'panel',	
		title: 'Content Email Settings',
		border:false,
		itemId: 'emailContentUploadPanel',

		items: [{
			xtype: 'checkbox',			
			itemId: 'contentEmailCheckbox',
			name:'contentEmail',
			boxLabel: 'Send Email on Content Upload?',
			checked: false,
			margin:'20 0 0 20', 
			labelWidth: 200
		},{
			xtype:'contentEmailSettings'
		}]	//email page
	}]
});


