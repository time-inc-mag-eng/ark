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
 * The view for the login screen. Contains a simple form panel within a window. 
 * This view gets added to the main viewport when a user has not logged in
 * or logged out.
 * See Ark.controller.Login for application logic. 
 */

Ext.define("Ark.view.Login", {
	extend: 'Ext.window.Window',

    alias: 'widget.login',
	
	width:300,
    height:130,
    itemId: 'loginWindow',
    closable: false,
    resizable: false,
    plain: true,
    border: false,
    autoShow: true,
	draggable: false,
	preventHeader: true,
	
    items : {
       	itemId: 'loginForm',
    	xtype: 'form',
    	url: 'login.noah',
    	frame: true,
    	frameHeader: true,
 	

        title: 'Please Sign In', 
        defaultType: 'textfield',
        	
        items: [{
        	itemId: 'loginUserName',
            fieldLabel: 'Username', 
            name: 'username',  // the parameter name
            allowBlank: false // validations
        },{
        	itemId: 'loginPassword',
            fieldLabel: 'Password', 
            name: 'password', 
            inputType: 'password', 
            allowBlank: false
        }],
        
        buttons: [{
        	itemId: 'loginSubmitButton',
            text:'Sign In', // submit button
            formBind: true	 
        }]    	
    }

});
