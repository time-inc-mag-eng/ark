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

/**
 * This view defines publication view, which is a combo box for
 * the all of the tabs in the tab panel
 */
Ext.define('Ark.view.Publication', {
	extend: 'Ext.form.ComboBox',

	requires: ['Ark.store.Publications'],
		
	alias: 'widget.publication',
	itemId: 'publication',

	name: 'pubId', // the name of the request parameter sent to server
	fieldLabel: 'Publication', // text to display next to combo-box
	valueField: 'id', // store fields
	displayField: 'name',
	emptyText: 'Select a publication ...',		
    selectOnFocus: true,
    allowBlank: false,
    forceSelection: true,
    editable: false,
    store: {
    	type: 'publications'
    }
});
