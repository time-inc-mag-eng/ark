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
 * This controller is responsible for displaying the two main views 
 * depending on the login status of the user. If the user is not logged in 
 * it will display the Login view otherwise, the Main view is displayed.
 */
Ext.define("Ark.controller.ViewPort", {
	extend: 'Ext.app.Controller',
	
	views: [ 'Login', 'Application', 'Main' ],
	
	model: [ 'Application' ],
    
	requires: [ 'Ark.lib.ErrorHandler', 'Ark.lib.User' ],
	
	stores: [ 'Applications' ],
	
	refs: [{
		selector: '#loginWindow', // get the instance form of the associated login view
		ref: 'loginWindow'
	}, {
		selector: 'viewport',
		ref: 'mainViewPort'
	}, {
		selector: 'viewport > panel',
		ref: 'mainPanel'
	}],
	
    init: function() {    	
        this.control({ 
            'viewport': { // select the one and only viewport
                render: this.onViewPortRendered
            }
        });
        
        // application wide events
        this.application.on({
        	userstatuschange: this.onUserStatusChanged,
        	scope: this
        });
        
        this.application.on({
        	ajaxException: this.handleAjaxException,
        	scope: this
        });
        
        
    },

    handleAjaxException: function(proxy,response) {
    	var me = this;
    	Ark.lib.ErrorHandler.handleError(response,me.application);
    },
    
	onViewPortRendered: function(viewPort) { // called everytime the page reloads
		
		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"Loading..."});
		myMask.show();	
		
		var me = this;

		Ext.Ajax.request({ // comment this ajax request to bypass logging
			url: 'getUser.noah',
			success: function(response) {
				if(response.responseText) {
					var role = Ext.decode(response.responseText).result.role;	
					me.application.fireEvent('userstatuschange', role);
				} else {
					me.application.fireEvent('userstatuschange', Ark.lib.User.NO_ACCESS);
				}	
				myMask.hide();
			},
			failure: function() {
				
			}
		});
	},
	
	onUserStatusChanged: function(role) {
		if(role) {
			Ark.lib.User.setRole(role);
			if(this.getLoginWindow())  // remove the login window if a new session
				this.getLoginWindow().destroy();
			
			if(!this.getMainPanel()) {
				this.getMainViewPort().add(Ext.create('widget.main')); // add the main panel
			}						
		} else {
			if(this.getMainPanel())
				this.getMainPanel().destroy();
			
			if(!this.getLoginWindow())
				this.getMainViewPort().add(Ext.create('widget.login'));
		}
		
		this.getMainViewPort().doLayout();		
	}			
});
