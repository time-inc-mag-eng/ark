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
Ext.define('Ark.model.ImagePreview', {
    extend: 'Ext.data.Model',
    idProperty: 'imgConvention',
    fields: [ 
             { name: 'imgConvention'},
             { name: 'msgError'},  	
             { name: 'regex'}            
    ],
  
    proxy: {
    	type: 'ajax',
    	url: 'restricted/getImagePreviews.noah',

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
    			create: 'restricted/editImagePreviews.noah?write=create' ,
    			update: 'restricted/editImagePreviews.noah?write=update',
    			destroy: 'restricted/editImagePreviews.noah?write=delete'
    		} 
    }});
