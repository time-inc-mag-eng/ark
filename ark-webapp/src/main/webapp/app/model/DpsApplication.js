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
Ext.define('Ark.model.DpsApplication', {
    extend: 'Ext.data.Model',
    
    idProperty:'id',
    fields: [
       { name: 'id'}, 
       { name: 'appName'}, 
       { name: 'userName'}, 
       { name: 'password'},
       { name: 'type'},
       { name: 'rendition'}, 
       { name: 'productId'},
       { name: 'contentEmail', type: 'boolean'},      
       { name: 'previewEmail', type: 'boolean'},
       { name: 'contentRecepients'},
       { name: 'contentEmailSubject'},
       { name: 'contentEmailBody'},
       { name: 'previewRecepients'},
       { name: 'previewEmailSubject'},
       { name: 'previewEmailBody'},
       { name: 'requireContentFirst', type: 'boolean'},
       { name: 'sendMsg', type: 'boolean'},
       { name: 'active', type: 'boolean'},
       { name: 'productId'},
       { name: 'pubShortName'},
       { name: 'defaultPrice', type:'float'},
       { name: 'imagePreviews'},
       { name: 'dpsConsumerSecret'}, 
       { name: 'dpsConsumerKey'},
       { name: 'dpsUrl'},
       { name: 'dpsRetryCount'}
    ],
   

    proxy: {
    	type: 'ajax',
    	url: 'restricted/getDpsAppData.noah',

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
    			create: 'restricted/createDpsApp.noah?write=create' ,
    			update: 'restricted/createDpsApp.noah?write=update'
    			//destroy: 'restricted/getDpsAppData.noah?write=delete'
    		} 	
    }
  
});
