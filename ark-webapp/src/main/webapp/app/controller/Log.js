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
Ext.define("Ark.controller.Log", {
	extend: 'Ext.app.Controller',
	
	
	refs: [{
		selector: 'log grid', 
		ref: 'grid'
	}, {
		selector: 'log #exportIframe',
		ref: 'exportFrame'
	}, {
		selector: 'log publication',
		ref: 'publication'
	}],
	
    init: function() { 
    	this.control({
    		'log publication' : { 
    			select: this.pubCompSelected
    		}, 
    		'log #excelBtn': {
    			click : this.exportToExcel
    		}, 
    		'log #logGrid' : {
    			beforerender: this.beforeLogGridRender
    		}
    	});   	  	
    },
    
    beforeLogGridRender: function() {
    	var pagingBar = this.getGrid().child('pagingtoolbar'); 
    	pagingBar.bindStore(this.getGrid().getStore()); 
    },
    
    exportToExcel: function() {
    	this.exportHelper('xls');

    },
    
    
    exportHelper: function(exportType) {
    	this.getExportFrame().load({
    		url: 'restricted/getLogExport.noah',
    		params: {
    			exportType: exportType,
    			pubId: this.getPublication().getValue()
    		}
    	});   	
    },
    
    
	handleRemoteException: function() {
		this.reset();
	},
	
	reset: function() {
		this.getPublication().reset();
		this.getPublication().lastQuery = null;
		this.getGrid().disable();
	},
    
    pubCompSelected: function(pubComp) {         
    	var grid = this.getGrid();
    	var numDays = pubComp.getStore().getById((pubComp.getValue())).get('numDaysReport');
    	grid.setTitle("Reporting for the past " + numDays + " day(s)." );
    
		Ext.apply(grid.getStore().getProxy().extraParams,{ // add pubid parameter to request
			pubId: pubComp.getValue()});
		
		grid.getStore().loadPage(1);
		grid.enable();
   	
    }
    
});
