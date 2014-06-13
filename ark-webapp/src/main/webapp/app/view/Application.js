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
Ext.define('Ark.view.Application', {
	extend: 'Ext.form.ComboBox',

	requires: ['Ark.store.Applications'],

	alias: 'widget.application',
	itemId: 'application',
	name:'application',
	listConfig: { // fix loading bug 4.0.7 see http://www.sencha.com/forum/showthread.php?156908-combobox-queryMode-local-still-shows-loading&p=678899&viewfull=1#post678899
		loadMask: false  
	},
	queryMode: 'local',
	queryAction: 'all',
	lastQuery: '',
	fieldLabel: 'Application', // text to display next to combo-box
	valueField: 'id', // store fields
	displayField: 'name',
	emptyText: 'Select an application...',		
    selectOnFocus: true,
    allowBlank: false,
    forceSelection: true,
    editable: false,
    disabled: true,
    store: {
    	type: 'applications'
    }
});
