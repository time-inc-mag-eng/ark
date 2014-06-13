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
Ext.define('Ark.view.superAdmin.ContentEmailSettings', {
	extend:'Ext.panel.Panel',
	alias:'widget.contentEmailSettings',		
	itemId: 'contentEmailSettings',
	padding:15,
	bodyStyle:'background-color:#F6FAFF',
	disabled:true,
	border : false,
	layout: 'anchor', 
	defaults: {
		anchor: '100%',
		xtype:'textfield',
		labelWidth: 200
	},
	items: [{
		fieldLabel:'Comma Separated Recepients',
		itemId: 'contentRecepients',
		name:'contentRecepients'
	},{
		fieldLabel:'Email Subject Template',
		itemId:'contentEmailSubject',
		name:'contentEmailSubject',
		qtip: Globals.emailHelp,
		listeners: {
			render: function(c) {
				Ext.QuickTips.register({
					target: c.getEl(),
					text: c.qtip
				});
			}
		}

	},{
		fieldLabel:'Email Body Template',
		itemId:'contentEmailBody',
		name:'contentEmailBody',			
		xtype: 'htmleditor',
		enableColors: false,
		enableAlignments: false,
		qtip: Globals.emailHelp,
		listeners: {
			render: function(c) {
				Ext.QuickTips.register({
					target: c.getEl(),
					text: c.qtip
				});
			}
		}
	}]//items
}
);
