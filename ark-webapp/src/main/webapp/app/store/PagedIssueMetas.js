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
Ext.define('Ark.store.PagedIssueMetas', {
    extend: 'Ext.data.Store',
  
    model: 'Ark.model.IssueMeta',   
    alias: 'store.pagedissuemetas',
    
    proxy: {
    	type: 'ajax',
    	url: 'restricted/getIssueMetas.noah',
    	simpleSortMode: true,
    	reader: {
    		type: 'json',
    		root: 'data',
    		successProperty: 'success',
    		totalProperty: 'total'    		
    	},
    	writer: {
    		type: 'json',
    		encode: true,
    		root: 'data',
    		writeAllFields: true,
    		allowSingle: false
    	},
    	

    	api: { // write operations
    		create: 'restricted/writeIssueMeta.noah?write=create' ,
    		update: 'restricted/writeIssueMeta.noah?write=update',
    		destroy: 'restricted/writeIssueMeta.noah?write=delete'
    	} 	
    	
    },

    remoteSort: true, // filtering and sorting is done server side since this is a paged store
    remoteFilter: true, 
    pageSize: 15, // default page size
    sorters: [{ // default sort
    	property: 'onSaleDate',
    	direction: 'DESC'
    }]
});
    
