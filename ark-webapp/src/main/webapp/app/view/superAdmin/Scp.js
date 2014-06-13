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
Ext.define('Ark.view.superAdmin.Scp', {
	requires: ['Ark.view.superAdmin.ImagePreviewGrid'],
	extend:'Ext.panel.Panel',
	alias: 'widget.Scp',
	id:'ScpType',
	itemId:'Scppanel',
	name:'ScpType',
	//cls:'accordion-panel',
	layout:'accordion',
	defaults:{
		bodyStyle:'background-color:#F6FAFF',
		autoScroll:true
	},
	height:'80%',	
	width:'100%',
	deferredRender:true,

	items: [{   
		xtype:'panel',
		title: 'Server Info',
		itemId: 'ScpServer',
		name:'ScpServer',
		border:false,
		layout: 'anchor',
		defaults: {
			labelWidth: 200,
			anchor:'100%',
			xtype:  'textfield',
			margin : '5 20 0 20'
		},

		items: [
		        {
		        	fieldLabel:'UserName',
		        	itemId:'userName',
		        	name:'userName',
		        	allowBlank:false,
		        	margin : '20 20 0 20'
		        },{
		        	fieldLabel:'Password',
		        	itemId:'password',
		        	name:'password',
		        	allowBlank:false
		        },{
		        	fieldLabel:'Server',
		        	itemId:'server',
		        	name:'server',
		        	allowBlank:false

		        },{
		        	fieldLabel:'Port',
		        	itemId:'port',
		        	name:'port',
		        	value:22,
		        	regex: /(^[1][0-9]{0,4}$|^[1-5][0-9]{1,4}$|^[6][0-4][0-9]{1,3}$|^[6][5][0-4][0-9][0-9]$|^[6][5][5][0-8][0-9]$|^[6][5][5][9][0-5]$)/,
		        	allowBlank:false
		        }]

	},//ScpServer
	{
		xtype:'panel',
		title: 'General Settings',
		items:[{xtype:'appSettings'}]
	},
	{	xtype:'panel',
		itemId: 'ScpSettings',
		title:'Content Settings',
		name:'ScpSettings',	
		layout:'anchor',
		defaults: {
			labelWidth: 200,
			anchor:'100%',
			xtype:  'textfield',
			margin : '5 20 0 20'
		},
		items: [{
			fieldLabel:'Directory Path',
			itemId:'dirPath',
			name:'dirPath',
			allowBlank:false,
			margin : '20 20 0 20',
			qtip:  Globals.help,
			listeners: {
				render: function(c) {
					Ext.QuickTips.register({
						target: c.getEl(),
						text: c.qtip
					});
				}
			}
		},{
			fieldLabel:'Content Type',
			xtype:'combobox',
			itemId:'contentType',
			name: 'contentType',			
			store: new Ext.data.ArrayStore({
				id: 0,
				fields: [
				         'tId',
				         'contType'
				         ],
				         data: [[1, 'any_ofip'], [2, 'any_folio'],[3,'any'],[4,'ofip_html5'],[5,'ofip_cds_nonprogressive'],[6,'ofip_cds_progressive']]
			}),
			displayField: 'contType',
			forceSelection: true,
			typeAhead: true,
			valueField:'contType',
			queryMode: 'local',
			triggerAction:'all',
			allowBlank:false		
		},{	        		  
			fieldLabel:'File Naming Convention',
			itemId:'pathNaming', 
			name:'pathNaming',
			allowBlank:false,
			qtip:  Globals.help,
			listeners: {
				render: function(c) {
					Ext.QuickTips.register({
						target: c.getEl(),
						text: c.qtip
					});
				}
			}
		}]

	},//ScpSettings	       
	{
		xtype:'panel',
		title: 'Image Previews',
		border:false,
		layout:'anchor',		
		items:[{
			xtype:'textfield',
			fieldLabel:'Image Preview Naming Convention',
			qtip:  Globals.help,
			listeners: {
				render: function(c) {
					Ext.QuickTips.register({
						target: c.getEl(),
						text: c.qtip
					});
				}
			},
			itemId:'previewNaming', 
			id:'previewNaming',
			name:'previewNaming',
			labelWidth: 200,
			allowBlank:false,
			anchor:'100%',
			margin: '20 20 0 20 '
		},{
			xtype:'imagePreviewGrid',			
			margin: '5 20 0 20'
		}]
	},
	{
		xtype:'panel',
		title: 'Preview Email Settings',
		border:false,
		autoScroll: true,
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
	},
	{
		xtype:'panel',	
		title: 'Content Email Settings',
		border:false,
		autoScroll: true,
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
	}


	]
});

