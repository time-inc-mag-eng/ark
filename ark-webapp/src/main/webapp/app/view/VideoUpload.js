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
Ext.define('Ark.view.VideoUpload', {
	extend: 'Ark.lib.ProgressForm',

	requires: ['Ark.view.Issue', 'Ark.view.Publication', 'Ark.store.MediaLocations', 'Ark.lib.MultiFileInput'],

	title: 'Media Upload',
	alias: 'widget.video',
	itemId: 'video',
	
	config: {
        progressUrl: 'restricted/getProgressStatus.noah?type=media'
	},

	layout: {
		type: 'vbox',
		align : 'stretch',
		pack  : 'start'
	},

	defaults: {
		margin: '0 10 5 0',
		msgTarget: 'side',
		labelWidth: 120
	},

	items: [{
		xtype: 'publication' // the drop downs for publication and application
	}, {
		xtype:'issue'
	}, {
		xtype: 'multifilefield',
		name: 'form-file',
		emptyText: 'Attach file(s)',
		fieldLabel: 'Media File',
		buttonText	: '',
		buttonConfig: {iconCls: 'upload-icon'},
		allowBlank	: false			
	}, {
		width: '100%',
		flex: 1,
		xtype: 'grid',
		title: 'Media Information',
		store: {
			type: 'mediaLocations'
		},
		disabled: true,
		forceFit: true,
		height: '100',
		loadMask: true,
		columns: [{
			xtype: 'rownumberer',
			width: 50,
			sortable: false
		}, {
			text: 'Name',
			dataIndex: 'fileName',
			flex: 2
		}, {
			text: 'Link',
			flex: 7,
			dataIndex: 'cdnUrl',
    		renderer: function(val) {
    			return '<div style="word-wrap: break-word; white-space:normal !important;">'+ val +'</div>';
    		},	
			sortable: false
		}, {
			text: 'Modified',
			flex: 2,
			dataIndex: 'modified',
			renderer: Ark.lib.Util.utcToLocaleTimeRenderer('F d, Y - g:i a T')
		}]		
	}],

	buttons: [{
		text: 'Upload',
		itemId: 'mediaUploadBtn'
	}] 

});
