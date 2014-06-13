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
Ext.define('Ark.view.Issue', {
	extend: 'Ext.form.ComboBox',


	requires: ['Ark.store.Issues'],
	
	
	name: 'issueId', // the name of the request parameter sent to server
	queryMode: 'local', 
	alias: 'widget.issue',
	listConfig: { // fix loading bug 4.0.7 see http://www.sencha.com/forum/showthread.php?156908-combobox-queryMode-local-still-shows-loading&p=678899&viewfull=1#post678899
		loadMask: false  
	},
	itemId: 'issue',
	fieldLabel: 'Issues', // text to display next to combo-box
	valueField: 'id', // store fields
	displayField: 'name',
	emptyText: 'Select an issue or type to search...',		
    selectOnFocus: true,
    allowBlank: false,
    forceSelection: true,
    editable: true,
    disabled: true,
    store: {
    	type: 'issues'
    }
});




