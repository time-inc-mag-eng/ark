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
Ext.define('Ark.view.OriginInfo', {
	extend: ('Ext.container.Container'),
	
	//requires: ['Ark.store.OriginInfo'],
	alias: 'widget.originInfo',
	itemId: 'originInfo',
	defaultType: 'textfield',
	items: [{
			fieldLabel:'Server',
			name:'server',
			allowBlank:false, 
			vtype:'url'
		},{
			fieldLabel:'Port',
			name:'port',
			regex: /(^[1][0-9]{0,4}$|^[1-5][0-9]{1,4}$|^[6][0-4][0-9]{1,3}$|^[6][5][0-4][0-9][0-9]$|^[6][5][5][0-8][0-9]$|^[6][5][5][9][0-5]$)/,
			///regex:  /^\d+$/,
			allowBlank:false
		},{
			fieldLabel:'UserName',
			name:'userName',
			allowBlank:false
		},{
			fieldLabel:'Password',
			name:'password',
			allowBlank:false
		}
	]
	
});

