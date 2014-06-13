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
Ext.define('Ark.view.ContentUpload', {
	extend: 'Ark.lib.ProgressForm',

	requires: [ 'Ark.store.IssueMetas', 'Ark.view.ApplicationMulti', 'Ark.view.Publication',
	            'Ark.view.Issue', 'Ext.ux.grid.column.Component' ],

	            alias: 'widget.content',
	            itemId: 'content',
	            title: 'Upload Content',

	            config: {
	            	progressUrl: 'restricted/getProgressStatus.noah?type=content'
	            },

	            layout: 'anchor', 
	            autoScroll:true,
	            defaultType: 'textfield',
	            defaults: {
	            	margin: '10 10 5 0',
	            	msgTarget: 'side',
	            	labelWidth: 120,
	            	anchor: '100%'
	            },
	            items: [{
	            	xtype: 'publication'

	            }, {
	            	xtype:'issue'

	            }, {
	            	height: 230,
	            	xtype: 'application-multi'
	            }, {
	            	xtype: 'filefield',
	            	name: 'form-file',
	            	itemId: 'contentfile',
	            	emptyText: 'Select content file',
	            	fieldLabel: 'Content Files',
	            	buttonText	: '',
	            	buttonConfig: {iconCls: 'upload-icon'},
	            	allowBlank	: false			
	            }, {
	            	xtype: 'checkbox',
	            	fieldLabel: 'Replace entire issue',
	            	name: 'newUpload',
	            	itemId: 'newUpload',
	            	hidden: false
	            }, {

	            	xtype: 'grid', 
	            	itemId: 'metagrid',
	            	store: {
	            		type: 'issuemetas'
	            	},	            		            
	            	height:200,
	            	margin: '20 25% 0 25%',
	            	title: 'Information',
	            	headerPosition: 'left',
	            	columns: [{
	            		header: Ark.lib.Constant.applicationHeaderName,  dataIndex: 'applicationId',
	            		flex: 3
	            	}, {
	            		header: Ark.lib.Constant.referenceIdHeaderName,
	            		dataIndex: 'referenceId',
	            		renderer: function(val) {
	            			return '<div style="word-wrap: break-word; white-space:normal !important;">'+ val +'</div>';
	            		},
	            		flex: 3
	            	},
	            	{
	            		header: 'Published Date',  dataIndex: 'publishedDate', renderer: Ark.lib.Util.utcToLocaleTimeRenderer('F d, Y - g:i a T'), flex: 3
	            	}],
	            	hidden: true		
	            }],

	            dockedItems: [{
	            	xtype: 'toolbar',
	            	dock: 'bottom',
	            	ui: 'footer',
	            	defaults: {minWidth: 75},
	            	items: [{
	            		xtype: 'button', 
	            		text: 'Republish',
	            		itemId: 'publishBtn',
	            		disabled: true
	            	},
	            	'->',{ 
	            		xtype: 'button', 
	            		text: 'Upload',
	            		itemId: 'submitBtn',
	            		disabled: true
	            	}, {
	            		xtype: 'button', 
	            		text: 'Reset',
	            		itemId: 'resetBtn'
	            	}]
	            }]
});
