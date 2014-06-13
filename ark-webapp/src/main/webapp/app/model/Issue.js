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
Ext.define('Ark.model.Issue', {
    extend: 'Ext.data.Model',
    
    idProperty: 'id',
    
    fields: [
       { name: 'id', type: 'int'}, 
       { name: 'name', type: 'string' },
       { name: 'shortDate', type: 'date', convert: Ark.lib.Util.convertToUTC },  
       { name: 'testIssue', type: 'boolean' }  
    ],
    
    
    equals: function(other) {
    	if(other && other.self.getName() === this.self.getName()) {
    		if(this.get('id') == other.get('id') && this.get('name') == other.get('name'))	
    			return true;
    		else 
    			return false;
    	}
    	return false;
    }
    
});
