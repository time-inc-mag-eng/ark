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
Ext.define('Ark.model.ScpApplication', {
    extend: 'Ext.data.Model',
    
    idProperty:'id',
    fields: [
       { name: 'id'}, 
       { name: 'userName'}, 
       { name: 'appName'},
       { name: 'password'},
       { name: 'type'},
       { name: 'server'},     
       { name: 'port'},
       { name: 'dirPath'},
       { name: 'contentType'},
       { name: 'contentParserId'},
       { name: 'pathNaming'},
       { name: 'requireContentFirst', type: 'boolean'},
       { name: 'sendMsg', type: 'boolean'},
       { name: 'active', type: 'boolean'},
       { name: 'productId'},
       { name: 'pubShortName'},
       { name: 'defaultPrice', type: 'float'},
       { name: 'imagePreviews'},
       { name: 'previewNaming'}, 
       { name: 'contentEmail', type: 'boolean'},      
       { name: 'previewEmail', type: 'boolean'},
       { name: 'contentRecepients'},
       { name: 'contentEmailSubject'},
       { name: 'contentEmailBody'},
       { name: 'previewRecepients'},
       { name: 'previewEmailSubject'},
       { name: 'previewEmailBody'}
      
    ],

  /*  associations : [ {
        type : 'hasMany',
        model : 'Ark.model.ImagePreview',
        name : 'imagePreviews',
        associationKey:'imagePreviews'//
    } ],*/
    
    proxy: {
    	type: 'ajax',
    	url: 'restricted/getScpAppData.noah',

    	reader: { // associated stores will be of type JSONStore
    		type: 'json',
    		root: 'result',
    		successProperty: 'success'
    	},
    	writer: {
    		type: 'json',
    		encode: true,
    		root: 'data',
    		writeAllFields: true,
    		allowSingle: false
    	},
    	 api: { // write operations
    			create: 'restricted/createScpApp.noah?write=create' ,
    			update: 'restricted/createScpApp.noah?write=update'
    			//destroy: 'restricted/getScpAppData.noah?write=delete'
    		} 
    }
   	
});
