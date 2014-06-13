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
Ext.define('Ark.view.superAdmin.GeneralAppSettings', {
	extend:'Ext.container.Container',
	alias:'widget.appSettings',		
	itemId: 'appSettings',
	style:'background-color:#F6FAFF',
	layout:'anchor',
	defaults: {
		labelWidth: 200,anchor:'100%',
		xtype:  'textfield',
		margin:'5 20 0 20'
	},	
	items: [{
		fieldLabel:'Publication Short Name',
		itemId: 'pubShortName',
		name:'pubShortName',
		allowBlank:false,
		margin:'20 20 0 20',
		qtip: Globals.vendorName,
		listeners: {
			render: function(c) {
				Ext.QuickTips.register({
					target: c.getEl(),
					text: c.qtip
				});
			}
		}
	},{
		fieldLabel:'Product ID',
		itemId:'productId',
		allowBlank:false,
		name:'productId' 		
	},{
		fieldLabel:'Default Price',
		itemId:'defaultPrice',
		name:'defaultPrice',
		regex: /[0-9]/,
		value:0
	},{
		xtype: 'checkbox',
		boxLabel:'Send Message',
		itemId:'sendMsg',
		name:'sendMsg'
	},{
		xtype: 'checkbox',
		boxLabel:'Active',
		itemId:'active',
		name:'active'	
	},{
		xtype: 'checkbox',
		boxLabel:'Require Content First',
		itemId:'requireContentFirst',
		name:'requireContentFirst'

	}]//items
}
);
