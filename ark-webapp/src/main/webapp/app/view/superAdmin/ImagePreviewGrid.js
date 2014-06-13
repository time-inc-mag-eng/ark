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
Ext.define('Ark.view.superAdmin.ImagePreviewGrid',{
	extend:'Ext.grid.Panel',
	title: 'Image Previews',
	headerPosition: 'left',
	alias: 'widget.imagePreviewGrid',
	multiSelect: true,
	itemId: 'imgPreviewGrid',
	name:'imgPreviewGrid',
	height: '280',
	store: {
		type: 'imagePreviews'	
	},
	requires: [  'Ext.ux.grid.plugin.RowEditing', 'Ark.store.ImagePreviews' ],
	columns: [{
		header: 'Regex',
		flex:1,
		dataIndex: 'regex',
		editor: {
			xtype: 'textfield',
			allowBlank: false	
		}
	}, {
		header: 'Error message',
		flex:1,
		dataIndex: 'msgError',					
		editor: {
			xtype: 'textfield',
			allowBlank: false	
		}
	},  {
		header: 'Image Naming Convention',
		flex:1,
		itemId:'imgConvention',
		dataIndex: 'imgConvention',		
		editor: {
			xtype: 'textfield',
			allowBlank: false,
			disabled: true
		}

	}],
	plugins: { // plugin for editing the row
		ptype: 'ux.rowediting',
		clicksToEdit: 2,
		pluginId: 'rowEditing',
		errorSummary: false
	},
	tbar: [{
		iconCls: 'icon-user-add',
		itemId: 'imgPreviewAddBtn',
		text: 'Add Image Preview'	
	}, {
		iconCls: 'icon-user-delete',
		itemId: 'imgPreviewDeleteBtn',
		text: 'Remove Image Preview'	
	}, {
		iconCls: 'icon-help',
		itemId: 'imgPreviewHelp',
		tooltip: Globals.imagePreviewHelp										
	}]

});

