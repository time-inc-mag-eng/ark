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
Ext.define('Ark.view.IssueManager', {
	extend: 'Ext.container.Container',

	
	requires: [  'Ext.ux.grid.plugin.RowEditing', 
		            'Ext.ux.form.field.DateTime','Ext.ux.grid.PageSize', 
		            'Ext.ux.grid.FiltersFeature','Ark.view.Application', 
		            'Ark.view.Publication', 'Ark.view.Issue', 
		            'Ark.store.PagedIssueMetas', 'Ark.store.Payments',
		            'Ark.store.Issues'],


	alias: 'widget.issue-manager', // alias names
	itemId: 'issue-manager',
	title: 'Manage Issue',

	layout: 'fit',

	defaults: {
		style: 'background-color:#dfe8f5;', // blue background for child components
		bodyStyle: 'background-color:#dfe8f5'
	},


	items: {
		tabPosition: 'bottom',
		xtype : 'tabpanel',
		plain: true,
		margin: 5,
		border: true,
		itemId: 'subtab',

		defaults: {
			border: false,
			style: 'background-color:#dfe8f5;', // blue background for child components
			bodyStyle: 'background-color:#dfe8f5;'
		},


		items:[{
			title: 'Create Issues', // the create issue name tab
			itemId: 'issueNameTab',

			layout: {
				type: 'vbox',
				align : 'stretch',
				pack  : 'start'
			},

			defaults: {
				margin: '5 10 0 5',
				msgTarget: 'side',
				labelWidth: 120
			},

			items: [{
				xtype: 'publication' // the drop downs for publication and application
			}, {
				margin: '5 10 5 5',
				xtype: 'grid',
				title: 'Information',
				disabled: true,
				multiSelect: true,
				itemId: 'issueNameGrid',
				flex: 1,
				store: {
					type: 'issues' 			
				},

				columns: [{
					header: 'Name',
					flex: 3,
					dataIndex: 'name',
					editor: {
						xtype: 'textfield',
						fieldLabel: '',
						regex: /^[^\/\\`?*<>|:]*$/,
						regexText: 'Invalid character set \\/`?*<>|:',
						allowBlank: false	
					}
				}, {
					header: Ark.lib.Constant.shortDateHeaderName,
					flex: 1,
					dataIndex: 'shortDate',
					renderer: Ark.lib.Util.utcToLocaleTimeRenderer('m/d/Y g:i a T'),
					editor: {
						xtype: 'datetimefield',
						allowBlank: false	
					}
				},  {
					header: Ark.lib.Constant.testIssueNameHeader,
					flex: 1,
					xtype: 'booleancolumn',
					trueText: 'Yes',
					falseText: 'No',	
					dataIndex: 'testIssue',
					editor: {
						xtype: 'checkbox'
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
					itemId: 'issueNameAddBtn',
					text: 'Add Issue'	
				}, {
					iconCls: 'icon-user-delete',
					itemId: 'issueNameDeleteBtn',
					text: 'Remove Issue'	
				}, {
					iconCls: 'icon-user-save',
					itemId: 'issueNameSaveBtn',
					text: 'Save All'										
				}],
				bbar: [{
					iconCls: 'refresh',
					itemId: 'issueNameRefreshBtn',
					tooltip: 'Refresh'

				}]
			}]
		}, {
			title: 'Create Metadata for Issues',
			itemId: 'createIssueTab',

			layout: {
				type: 'vbox',
				align : 'stretch',
				pack  : 'start'
			},

			defaults: {
				margin: '5 10 0 5',
				msgTarget: 'side',
				labelWidth: 120
			},

			items: [{
				xtype: 'publication' // the drop downs for publication and application
			},{
				xtype: 'application'
			}, { // the issue create grid
				xtype: 'grid',
				disabled: true,
				title: 'Information',
				margin: '5 10 5 5',

				flex: 1,
				multiSelect: true,
				itemId: 'issueGrid',
				features: {
					ftype: 'filters', // filters for short date and sale date
					encode: true
				},					
				store: {
					type: 'pagedissuemetas' // the grid is paged link it with a paged-aware store				
				},
				columns: [{
					header: Ark.lib.Constant.issueNameHeaderName,
					flex: 3,
					dataIndex: 'issueNameId',
					editor: {
						xtype: 'issue',
						fieldLabel: '',
						disabled: false
					}
				}, {
					dataIndex: 'issueNameId',
					hidden: true,
					sortable: false
				}, {
					header: Ark.lib.Constant.shortDateHeaderName,
					flex: 2.5,
					dataIndex: 'shortDate',
					sortable: true,
					renderer: Ark.lib.Util.utcToLocaleTimeRenderer('m/d/Y g:i a T'), 
					filter: { // apply a date filter to the sale date
						type: 'date'
					}

				}, {
					header: Ark.lib.Constant.saleDateHeaderName,
					renderer: Ark.lib.Util.utcToLocaleTimeRenderer('m/d/Y g:i a T'),
					filter: { // apply a date filter to the sale date
						type: 'date'
					},
					dataIndex: 'onSaleDate', // the on sale date can be set with a date/time value
					flex: 3,
					editor: {
						xtype: 'datetimefield',
						allowBlank: false	
					}
				}, {
					header: 'Issue Name 2nd line',
					flex: 3,
					dataIndex: 'volume',
					editor: {
						xtype: 'textfield'
					}
				},{
					header: Ark.lib.Constant.referenceIdHeaderName,
					flex: 4,
					dataIndex: 'referenceId',
					editor: {
						xtype: 'textfield',
						allowBlank: false
					}
				}, {
					header: 'Payment', // the payment combo for the grid
					flex: 1,
					dataIndex: 'paymentId',
					sortable: false,
					editor: {
						xtype: 'combo',
						editable: false,
						allowBlank: false,
						forceSelection: true,
						queryMode: 'local', 
						valueField: 'id', // store fields
						displayField: 'name',
						store: {
							type: 'payments'
						}
					}
				}, {
					header: 'Price',
					dataIndex: 'price',
					renderer: Ext.util.Format.usMoney,
					editor: {
						allowBlank: false,
						xtype: 'textfield',
						regex: /^(\d*\.\d{1,2}|\d+)$/
					},
					flex: 1
				}, {
					header: Ark.lib.Constant.testIssueNameHeader,
					flex: 0.5,
					xtype: 'booleancolumn',
					trueText: 'Yes',
					falseText: 'No',	
					dataIndex: 'testIssue',
					sortable: false
				}, {
					header: 'Active',
					flex: 1,
					xtype: 'booleancolumn',
					trueText: 'Yes',
					falseText: 'No',	
					dataIndex: 'active',
					editor: {
						xtype: 'checkbox'
					}
				}],

				bbar: Ext.create('Ext.PagingToolbar', { // the bottom bar - paging toolbar 
					displayInfo: true,
					itemId: 'pagingTb',
					displayMsg: 'Displaying issues {0} - {1} of {2}',		            
					emptyMsg: "No issues to display",
					items:[{
						itemId: 'removeFilterBtn',
						text: 'Clear Filters',
						tooltip: 'Removed filters applied'
					}],
					plugins: Ext.create('Ext.ux.grid.PageSize') // dynamic page size plugin
				}),
				plugins: { // plugin for editing the row
					ptype: 'ux.rowediting',
					clicksToEdit: 2,
					pluginId: 'rowEditing',
					errorSummary: false
				},
				tbar: [{
					iconCls: 'icon-user-add',
					itemId: 'issueAddBtn',
					text: 'Add Metadata'	
				}, {
					iconCls: 'icon-user-delete',
					itemId: 'issueDeleteBtn',
					text: 'Remove Metadata'	
				}, {
					iconCls: 'icon-user-save',
					itemId: 'issueSaveBtn',
					text: 'Save All'										
				}]												
			}]
		}]
	}
});

