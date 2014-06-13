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
Ext.define("Ark.view.Main", {
	extend: 'Ext.ux.tab.CustomTabPanel', 

	requires: ['Ark.view.Preview', 'Ark.view.ContentUpload',
	           'Ark.view.Admin', 'Ark.view.Log',
	           'Ark.view.VideoUpload', 'Ark.view.superAdmin.SuperAdmin'],
	itemId: 'mainTab',
	title: 'Ark',  // replaced with ant
	baseCls: 'x-window', // use Ext.Window styling
	margin: 5,
	alias: 'widget.main',

	defaults: {
		border: false,
		style: 'background-color:#dfe8f5;',
		bodyStyle: 'background-color:#dfe8f5; padding: 10px;' // all children will have these settings
	},

	tbar: [{
		xtype: 'tbfill' 
	},{
		text: 'Logout',
		itemId: 'logoutBtn'
	}],

	bbar: { xtype: 'toolbar', html:'<center><b>${project.version}</b></center>'}
});
