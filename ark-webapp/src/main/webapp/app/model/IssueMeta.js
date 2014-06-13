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
Ext.define('Ark.model.IssueMeta', {
    extend: 'Ext.data.Model',
   
    requires: ['Ark.model.Image'],
   
    fields: [
             { name: 'id', type: 'int'},
             { name: 'referenceId' }, 
             { name: 'onSaleDate', type: 'date', convert: Ark.lib.Util.convertToUTC },
             { name: 'shortDate', type: 'date', persist: false, convert: Ark.lib.Util.convertToUTC },
             { name: 'volume' },
             
             { name: 'folioId'},
             
             /** TODO make a relationship with Issue instead of duplicating **/
             { name: 'issueNameId', type: 'int'},
             { name: 'issue', persist: false },
             { name: 'testIssue', type: 'boolean', persist: false},
             
             { name: 'active', type: 'boolean' },

             { name: 'applicationId', type: 'int'},
             { name: 'paymentId', type: 'int'},
             { name: 'price', type: 'float'},
             
             { name: 'publishedDate', type: 'date', persist: false, convert: Ark.lib.Util.convertToUTC }, // don't send these fields to the server since there it is not modified
             { name: 'issue', persist: false },
             { name: 'device', persist: false },
             { name: 'coverStory', persist: false },
             
             { name: 'modifiable', type: 'boolean', persist: false },
    ]
    
});
