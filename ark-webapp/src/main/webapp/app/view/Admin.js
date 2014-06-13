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
 * This is the view for the Admin tab. It contains
 * a 'parent' tab panel and the childen tabs are as follows:
 * 1) Create Issue Name For Publication - This tab corresponds to the view that is involved
 * in creating an issue name for a particular publication
 * 2) Create Issues - This tab corresponds to the view that is involved in
 * creating meta data for a particular publication + applications 
 * 
 * This component has an alias xtype of 'admin'.
 */


Ext.define('Ark.view.Admin', {
	extend: 'Ark.view.IssueManager',

	alias: 'widget.admin', // alias names
    itemId: 'admin',
    title: 'Admin',
    rowMenu: Ext.create('Ext.menu.Menu', {
        height: 58,
        width: 150,
        itemId: 'admin-ctx-menu',
        
        items: [{
            text: 'Delete Folio',
            handler: function(arguments) { 
            	this.ownerCt.fireEvent('deleteFolio', arguments);
            }
        }, {
            text: '(Re)Publish Folio',
            handler: function(arguments) {
            	this.ownerCt.fireEvent('publishFolio', arguments);
            }
        }, {
            text: 'Unpublish Folio',
            itemId: 'unpublishBtn',
            handler: function(arguments) {
            	this.ownerCt.fireEvent('unpublishFolio', arguments);
            }
        }]
    })
    
});

