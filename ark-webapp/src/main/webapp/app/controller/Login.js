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
Ext.define("Ark.controller.Login", {
	extend: 'Ext.app.Controller',

    requires: ['Ark.lib.ErrorHandler'],	
	
	refs: [{
		selector: '#loginForm', // get the instance form of the associated login view
		ref: 'loginForm'
	}, {
		selector: '#loginForm #loginPassword', // get the instance form of the associated login view
		ref: 'password'
	}, {
		selector: '#loginForm #loginUserName', // get the instance form of the associated login view
		ref: 'username'
	}],	

    init: function() { // add event listeners that pertain to the login work flow
    	this.control({
    		'#loginSubmitButton' : {
    			click: this.submitHandler
    		},
    		'#loginUserName' : {
    			specialkey: this.keyPressFieldHandler
    		},
    		'#loginPassword' : {
    			specialkey: this.keyPressFieldHandler
    		}
    	});
    },
    
    
    /**
     * Performs a login request when the enter button is pressed
     * on the text field of the login form. A request will not be submitted
     * if either field is empty.
     */
    keyPressFieldHandler: function(obj, e, eOpts) {
    	if(e.getKey() === Ext.EventObject.ENTER && !this.getLoginForm().getForm().hasInvalidField()) { // enter button pressed
    		this.submitHandler();
    	}
    },

    /**
     * Handler that delegates to submitLogin to handle 
     * the login request.
     */
    submitHandler: function() {
		this.submitLogin(this.getLoginForm().getForm());
	}, 
    
	/** 
	 * Helper function that performs the login request. A successful JSON response of the following
	 * format is expected: 
		 {
		    success: true,
			isAdmin: true or false
		}
	 * A successful response will fire an application wide event called
	 * 'userstatuschange'. The exeScope is the object scope when the callback functions are called.
	 */
    submitLogin: function(form) {
    	var me = this;
    	
    	form.submit({
    		method: 'POST',
    		scope: this,
    		waitTitle:'Please wait', 
            waitMsg:'Authenticating...',
            
            success: function(form,action) { 
               	var role = action.result.result.role;
            	this.application.fireEvent('userstatuschange', role);
            },
            
            failure: function(form,action) {
            	Ark.lib.ErrorHandler.handleFormError(action, me.application);
            	me.getPassword().reset();
            }	          	
    	});
    }
});
