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
Ext.define('Ark.view.superAdmin.SuperAdmin', {
	extend: 'Ext.panel.Panel',
	alias:'widget.superAdmin',
	itemId: 'superAdmin',
	
	title: 'SuperAdmin',
	layout: 'fit',	
	requires: ['Ark.view.Publication','Ark.view.superAdmin.ImagePreviewGrid','Ark.view.superAdmin.Scp','Ark.view.superAdmin.DPS','Ark.view.superAdmin.ContentEmailSettings','Ark.view.superAdmin.PreviewEmailSettings','Ark.view.superAdmin.GeneralAppSettings'],
	items: {
		tabPosition: 'bottom',
		xtype : 'tabpanel',
		plain: true,
		itemId: 'superAdminTab', 
		defaults: {
			style: 'background-color:#dfe8f5;', // blue background for child components
			bodyStyle: 'background-color:#dfe8f5'
		},

		items:[{
			xtype:'form',
			title: 'Create Application', // the create app name tab
			id: 'createAppTab',
			//border: false,
        	layout: 'anchor', 
            autoScroll:true,
			defaults: {
				margin: '10 10 0 10',
				msgTarget: 'side',
				labelWidth: 120,
				style: 'background-color:#dfe8f5;', // blue background for child components
				bodyStyle: 'background-color:#dfe8f5',
				anchor:'100%'
			},
			items: [{
				xtype: 'publication' // the drop downs for publication 
			},{
				fieldLabel:'Application Name',
				name: 'appName',
				itemId:'app',
				xtype:'textfield',
				allowBlank:false
			},{
				fieldLabel:'Application Type',
				xtype:'combobox',
				itemId:'appTypeCombo',
				name:'type',
				store: new Ext.data.ArrayStore({
					id: 0,
					fields: ['typeId','displayType'],
					data: [[1, 'SCP'], [2, 'DPS']]  // data is local
				}),
				displayField: 'displayType',
				valueField:'typeId'
			}
			],
			dockedItems: [{
				xtype: 'toolbar',
				dock: 'bottom',
				ui: 'footer',
				items: ['->',{
					xtype:'button',
					text:'Submit',
					itemId:'submit'
				}]
			}]
		},{ 
			xtype:'form',
			title: 'Edit Application', // the create app name tab
			id: 'editAppTab',
			itemId: 'editAppTab',
			layout: {
				type: 'vbox',
				align : 'stretch',
				pack  : 'start'
			},
			defaults: {
				margin: '5 10 0 5',
				msgTarget: 'side',
				labelWidth: 120
			},
			items: [{
				xtype: 'publication' // the drop downs for publication 
			},{
				xtype:'application'
			},{
				fieldLabel:'Application Type',
				xtype:'textfield',
				itemId:'apptype',
				disabled: true,
				name:'apptype' 			
			}],
			dockedItems: [{
				xtype: 'toolbar',
				dock: 'bottom',
				ui: 'footer',
				items: ['->',{
					xtype:'button',
					text:'Submit',
					itemId:'submit'
				}]
			}]
		}]        	
	}//items 


});
