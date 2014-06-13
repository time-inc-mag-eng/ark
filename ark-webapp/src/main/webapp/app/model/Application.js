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
Ext.define('Ark.model.Application', {
    extend: 'Ext.data.Model',
    
    
    fields: [
       { name: 'id', type: 'int'}, 
       { name: 'name', sortType: Ext.data.SortTypes.asUCText },
       { name: 'referenceId_Header'}, 
       { name: 'metaId', type: 'int' },
       { name: 'maxCoverStory', type: 'int'},
       { name: 'defaultPrice', type: 'float'},
       { name: 'type'}
    ],
    
    proxy: {
    	type: 'ajax',
    	url: 'restricted/getApplications.noah',

    	reader: { // associated stores will be of type JSONStore
    		type: 'json',
    		root: 'result',
    		successProperty: 'success'
    	}
    }
});
