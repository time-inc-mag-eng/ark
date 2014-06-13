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
Ext.define("Ark.controller.Video", {
	extend: 'Ext.app.Controller',
	
    requires: ['Ext.ux.window.IframeWindow'],
	
	refs: [{
		selector: 'video publication',
		ref: 'publication'
	}, {
		selector: 'video issue',
		ref: 'issue'
	},{
		selector: '#mediaUploadBtn',
		ref : 'uploadBtn'
	}, {
		ref: 'formPanel', 
		selector: 'video'
	}, {
		ref: 'grid',
		selector: 'video grid'
	}],
	
    init: function() { 
    	this.control({
    		'video': {
       			progressComplete: this.progressDone,
    			polledProgress: this.updateProgress
    		},
    		'video publication' : { 
    			select: this.pubCompSelected
    		},
    		'video issue' : {
    			select: this.issueCompSelect
    		},
			'video #mediaUploadBtn' : {
				click : this.upload
			}, 
			'video grid' : {
				beforerender : this.addPreviewOrigin
			}
    	});   	  	
    },
    
    
    currentProgress: null,
    
    
    addPreviewOrigin: function(grid) {
		grid.addColumn({
			xtype : 'actioncolumn',
			width : 20,

			items : [ {
				getClass : function(v, metadata, record) {
						this.items[0].tooltip = 'Click to preview media';
						return 'avail-col';
				},
				handler : function(grid, row, col) { // TODO dont send request to cds if a previous request was made for that contains the ids
					var rec = grid.getStore().getAt(row);
					
					var originPreviewLink = rec.get('previewUrl');

					new Ext.ux.window.IframeWindow({ // TODO: hack - fix so it autogenerates iframe
						width: '80%',
						height: '80%',
						maximizable: true,
						title: "Preview",
						html: '<iframe style="overflow:auto;width:100%;height:100%;" frameborder="0"  src=\"' + originPreviewLink + '?time=' + new Date().getTime() + '\"></iframe>'
					}).show();
				}
			} ]
		});    	
    },
    
	handleRemoteException: function() {
		this.reset();
	},
	
	reset: function() {
		this.getPublication().reset();
		this.getIssue().reset();
		this.getPublication().lastQuery = null;
	},
    
    pubCompSelected: function(pubComp) { 
    	var issueComp = this.getIssue();
    	
		Ext.apply(issueComp.getStore().getProxy().extraParams,{ // add pubid parameter to request
			pubId: pubComp.getValue(),
			action: 'baseitems',
			tabType: 'media' });
			
	    	issueComp.getStore().load({
	    		scope: this,
	    		callback: function() {
	    			issueComp.clearValue();
	    			issueComp.clearInvalid();
	    			issueComp.setDisabled(false);    			
	    		}
	    	});    	
    },
    
    updateProgress: function(status, id) {
    	Ext.MessageBox.updateProgress(status.get('percent') / 100, status.get('description') + ' - ' + status.get('percent') + '%');
    },
    
    progressDone: function(successArr, failedArr) {
    	Ext.MessageBox.hide();

    	var resultMsg = "";
    	
    	var me = this;
    	
    	Ext.Array.forEach(failedArr, function(failedStatus) {
    		resultMsg += failedStatus.get('description') + "<br>";
    	});
    	
        Ext.MessageBox.show({
        	title: 'Result',
            width:600,
            msg: Ext.isEmpty(resultMsg) ? "Success" : resultMsg,
            buttons: Ext.MessageBox.OK,
			fn: function() {
				me.getGrid().getStore().load();
			}
       });
    },
    
    upload: function() {
		var form = this.getFormPanel().getForm();
		var me = this;
		if(form.isValid()) {
			this.getFormPanel().submit({
				method: 'POST',
				url: 'restricted/mediaUpload.noah',
				scope: this,
				params: {
					issueId: this.getIssue().getValue()
				},
				submitEmptyText : false,
				waitTitle:'Uploading media file', 
				waitMsg:'Please wait...',
				success: function(form,action) { 
					Ext.Msg.show({
						progress: true,
						closable: false,
						title: 'Progress',
						autoScroll: true,
						progressText: 'Initializing....',
						width: 600,
					});				
				},
				failure: function(form,action) {
					Ark.lib.ErrorHandler.handleFormError(action,me.application);
				}	          	
			});
		}
    },
    
    issueCompSelect: function(issueComp) {
    	var grid = this.getGrid();

    	grid.enable();
    	
		Ext.apply(grid.getStore().getProxy().extraParams,{ // add pubid parameter to request
			issueId: issueComp.getValue()});
   	
		var store = grid.getStore();
		
		store.load();
    }
});
