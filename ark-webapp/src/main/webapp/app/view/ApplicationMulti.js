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
Ext.define('Ark.view.ApplicationMulti', {
	extend: 'Ext.ux.form.ItemSelector',

	requires: ['Ark.store.Applications'],

	alias: 'widget.application-multi',
	itemId: 'application',
	name: 'appIds', // the name of the request parameter sent to server
	fieldLabel: 'Applications', // text to display next to combo-box
	valueField: 'id', // store fields
	displayField: 'name',
    allowBlank: false,
    disabled: true, // problem with disabled handler -- need to fix
    store: {
    	type: 'applications'
    },
    buttons: ['add', 'remove']
});
