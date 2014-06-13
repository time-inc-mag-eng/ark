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
Ext.define("Ark.controller.Main", {
	extend: 'Ext.app.Controller',

	refs: [{
		selector: '#mainTab', // the textfield issue name in the create issue name for publication sub-tab
		ref: 'mainTab'
	}],	
	
	init: function() { 
		this.control({
			'#mainTab' : { // selector for the publication component
				beforerender: this.handleBeforeRender
			}, 
			'#logoutBtn' : {
				click: this.logout
			}
		});
		
        // application wide events
        this.application.on({
        	remoteException: this.handleremoteException,
        	scope: this
        });	
	},
	
	
	handleremoteException: function() {
		var mainTab = this.getMainTab();
		var xtype = null;
		
		if(mainTab)
			 xtype = mainTab.getActiveTab().getXType();
		if(xtype) {
			var controller = null;
			
			if(xtype == 'content') {
				controller = this.getController('Content');
			} else if(xtype == 'preview') {
				controller = this.getController('Preview');
			} else if(xtype == 'log') {
				controller = this.getController('Log');
			} else if(xtype == 'admin') {
				controller = this.getController('Admin');
			} else if(xtype == 'video') {
				controller = this.getController('Video');
			}
		/*	 else if(xtype == 'superAdmin') {
					controller = this.getController('SuperAdmin');
				}
			*/
			if(controller) {
				controller.handleRemoteException();
			}
		}
	},
	
	initController: function(controlName) {
		var controller = this.application.getController(controlName);
		controller.init();
	},


	handleBeforeRender: function(tabpanel) {
		var role = Ark.lib.User.getRole();

		var count = 0;
		
		if(role == Ark.lib.User.ADMIN || role == Ark.lib.User.SUPER_ADMIN || role == Ark.lib.User.USER) {
			this.initController('Content'); // init controllers
			tabpanel.insert(count++, Ext.create('widget.content'));

			this.initController('Preview');
			tabpanel.insert(count++, Ext.create('widget.preview'));
			
			this.initController('Video');
			tabpanel.insert(count++, Ext.create('widget.video'));

			this.initController('Log');
			tabpanel.insert(count++, Ext.create('widget.log'));
			
			if(role == Ark.lib.User.ADMIN || role == Ark.lib.User.SUPER_ADMIN) {
				this.initController('Admin');
				tabpanel.insert(count++, Ext.create('widget.admin'));
			}
				
			if(role == Ark.lib.User.SUPER_ADMIN) {
				this.initController('SuperAdmin');
				tabpanel.insert(count++, Ext.create('widget.superAdmin'));
			}
			
		} else if(role == Ark.lib.User.MEDIA) {
			this.initController('Video');
			tabpanel.insert(count++, Ext.create('widget.video'));
		} else if(role == Ark.lib.User.ISSUE_MANAGER) {
			this.initController('IssueManager');
			tabpanel.insert(count++, Ext.create('widget.issue-manager'));
		}
	},

	logout: function() {
		var me = this;

		Ext.Ajax.request({
			url: 'logout',
			success: function(response) {
				me.application.fireEvent('userstatuschange', Ark.lib.User.NO_ACCESS);
			}
		});
	}

});
