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
Ext.define('Ark.controller.Admin', {
	extend: 'Ark.controller.IssueManager',

	containerSelector: '#admin',

	refs: [ {
		selector : '#admin-ctx-menu',
		ref : 'contextMenu'
	}],
	
    init: function() { 
    	this.control({
    		'#createIssueTab #issueGrid': { 
    			itemcontextmenu: this.contextMenuHandler
    		}, 
    		'#admin-ctx-menu': { 
    			deleteFolio: this.deleteFolioFn,
    			publishFolio: this.republishFn,
    			unpublishFolio: this.unpublishFn,
    		}	
    	});
    	
    	this.callParent(arguments);   	
    },
    
    contextMenuHandler: function(view, record, item, index, e, eOpts) {
    	e.stopEvent();
    	
    	
    	var sm = this.getIssueCreateGrid().getSelectionModel();
    	sm.deselectAll(); // only 1 row can be selected at any given moment
    	sm.select(index);
    	
    	var cm = this.getContextMenu();
    	
    	
    	if(!record.get('folioId')) {
    		cm.setDisabled(true);
    		Ext.Msg.alert('Error', 'DPS context menu not applicable.');
    	} else  {
    		var unpubBtn = cm.getComponent('unpublishBtn');
    		
    		if(!record.get('publishedDate')) {
    			unpubBtn.setDisabled(true); 
    		} else {
    			unpubBtn.setDisabled(false);
    		}
    			
    		cm.setDisabled(false);
    	}
    	
    	cm.showAt(e.getXY());
    },
    
    
    republishFn: function() {
    	this.sendUpdateFolioRequest("(Re)publishing a folio", "publish");
    },
    
    
    deleteFolioFn: function() {
    	this.sendUpdateFolioRequest("Deleting a folio", "delete");
    },
    
    unpublishFn: function() {
    	this.sendUpdateFolioRequest("Unpublishing a folio ", "unpublish");
    },
    
    sendUpdateFolioRequest: function(userMsg, action) {
    	
    	var me = this;
    	
    	Ext.Msg.show({
    	     title:'Confirm',
    	     msg: userMsg + ' may potentially have unwanted side effects if it is a live issue. Do you still want to proceed?',
    	     buttons: Ext.Msg.YESNO,
    	     icon: Ext.Msg.QUESTION,
    	     fn: function(btn, text) {
    	    	    if (btn == 'yes') {
    	    	    	var rec = me.getSelectedRow();
    	    	    	
    	    	    	Ext.MessageBox.show({
    	    	    		msg: userMsg,
    	    	    		width: 200,
    	    	    		title: 'In progress',
    	    	    		wait: true,
    	    	    		closable: false,
    	    	    		waitConfig: {
    	    	    			interval: 200
    	    	    		}
    	    	    	});
    	    	    	
    	    			Ext.Ajax.request({
    	    				url: 'restricted/updateFolio.noah',
    	    				
    	    				params: {
    	    					metaId: rec.getId(),
    	    					action: action
    	    				},
    	    				
    	    				success: function(response) {	
    	    					var result = Ext.decode(response.responseText).result;
    	    					
    	    					Ext.MessageBox.hide();
    	    					
    	    					Ext.Msg.show({
    	    						title: 'Result',
    	    						autoScroll: true,
    	    						msg: result,
    	    						width: 600,
    	    						buttons: Ext.Msg.OK,
    	    						fn: function() {
    	    							me.getPagingToolBar().doRefresh();
    	    						}
    	    					});	
    	    				},
    	    				
    	    				failure: function(response) { 
    	    					Ark.lib.ErrorHandler.handleError(response, me.application);
    	    				}
    	    			});
    	    	    }
    	     }
    	});

    }, 
    
    getSelectedRow: function() {
    	return this.getIssueCreateGrid().getSelectionModel().getSelection()[0];
    }
});

