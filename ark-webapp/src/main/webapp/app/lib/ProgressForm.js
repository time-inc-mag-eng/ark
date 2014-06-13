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
Ext.define('Ark.lib.ProgressForm', {
	extend: 'Ext.form.Panel',
	
	config: {
        progressParamName: 'progressId',
        progressUrl: 'restricted/getProgressStatus.noah',
        pollMsInterval: 10000,
        progressKeyId: 'id',
        progressValueId: 'cacheId'
    },

    constructor: function(config) {
    	this.callParent(arguments);
        this.initConfig(config);

        
        this.addEvents(
        	'onPollResponse',
        	'pollsComplete',
        	'pollError'
        );
        
        return this;
    },
    
    submit: function(options) {
    	var me = this;
    	var successFn = options.success;
    	
    	options.success = function(form, action) {
    		if(successFn) 
    			successFn(form, action);
    		
    		var mapping = me.getProgressMap(action.result.result);
    		
    		me.startPollingProgress(mapping);
    	};

    	this.callParent(arguments);
    },
    
    
    getProgressMap: function(progressMappingArr) {
    	var map = new Ext.util.HashMap();
    	
    	var me = this;
    	
    	Ext.Array.forEach(progressMappingArr, function(mapping) {
    		map.add(mapping[me.progressKeyId], mapping[me.progressValueId]);
    	});
    	
    	return map;
    },
    
    getParam: function(paramValue) {
    	var obj = new Object();
    	obj[this.progressParamName] = paramValue;
    	return obj;
    },
    
    startPollingProgress: function(mapping) {
    	var me = this;
    	
    	var progressRemaining = mapping.getCount();
    	
    	var failed = new Array();
    	
    	
    	var timer = function(id, progressId, initial) {
        	setTimeout(function() {
        		Ext.Ajax.request({
        			url: me.progressUrl,
        			method: 'GET',
        			params: me.getParam(progressId),
        			success: function(response) {		
        				var status = Ext.create('Ark.model.ProgressStatus', Ext.decode(response.responseText).result);
        				
    					me.fireEvent('polledProgress', status, id);
        				
        				if(!status.get('isDone')) 
        					timer(id, progressId, false);
        				else {
        					if(status.get('isError')) {
        						mapping.removeAtKey(id);
        						failed.push(status);
        					}
        					
        					if(--progressRemaining == 0) 
        						me.fireEvent('progressComplete', mapping.getKeys(), failed);
        				}
        			},
        			
        			timeout: 80000, 
        			
        			failure: function(response) {
        	    		me.fireEvent('pollingError', response);
        			}
        		});
        	}, initial == true ? 0 : me.pollMsInterval);    		
    	};
    	

    	
    	mapping.each(function(key, value, length) {
    		timer(key, value, true);
    	});
    	

    }
});
