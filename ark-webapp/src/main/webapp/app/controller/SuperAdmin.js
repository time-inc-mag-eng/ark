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
Ext.define('Ark.controller.SuperAdmin', {
	extend: 'Ext.app.Controller',
	stores:['DpsServers', 'DpsApplications','ScpApplications','ImagePreviews'],
	models:['ScpApplication','ImagePreview'],         
	requires: ['Ark.view.superAdmin.SuperAdmin'],
	refs:[{
		ref:'appNames',
		selector:'#editAppTab application'		
	},{
		ref:'appType',
		selector:'#editAppTab #apptype'
	},{
		ref:'dpsServer',
		selector:'#dpsserver'
	},{
		ref:'imgPreviewGrid',
		selector:'imagePreviewGrid'
	},{
		ref:'editContentEmailCheckbox',
		selector:'#editAppTab #contentEmailCheckbox'
	},{
		ref:'editPreviewEmailCheckbox',
		selector:'#editAppTab #previewEmailCheckbox'
	},{
		ref:'editAppTab',
		selector: '#editAppTab'
	}],

	curApp: null,
	curPub: null,
	rec:null,
	flag:false,

	init : function() { 
		this.control({
			'#createAppTab dps':{
				added:this.loadDpsDefaultsHandler,
			},
			'#editAppTab publication' : {
				select: this.loadApplicationNamesHandler
			},
			'#createAppTab publication' : { 
				select: this.publicationNameHandler
			},
			'#editAppTab application' : { 
				select: this.loadApplicationDataHandler
			}, 	  
			'#superAdminTab':{
				tabchange:this.refreshPanel, 
				tabchange: this.clearForm
			},
			'#contentEmailCheckbox':{
				change: this.contentEmailCheckboxHandler
			},
			'#previewEmailCheckbox':{
				change: this.previewEmailCheckboxHandler
			}, 
			'#createAppTab #appTypeCombo':{
				select: this.appTypeHandler
			},
			'#editAppTab #submit':{
				click: this.submitEditingAppHandler	        		
			},
			'#createAppTab #submit':{
				click: this.createAppHandler	        		
			},
			'#imgPreviewAddBtn': {
				click: this.addImgPreview
			},
			'#imgPreviewDeleteBtn': {
				click: this.removeImgPreview
			},
			'#imgPreviewGrid': {
				beforeedit: this.disableImgNameConvention
			}
		});

	},

	clearForm: function (tabPanel){
/*		var tabform=tabPanel.getActiveTab();
		tabform=tabform.getForm();
		tabform.reset();*/
	},
	createAppHandler: function (button){
		var form=Ext.ComponentQuery.query('#createAppTab')[0];		  
		var store;
		if (form.down('#Scppanel'))
			store=this.getScpApplicationsStore();
		else store=this.getDpsApplicationsStore();			  
		form=form.getForm();
		if (form.isValid()) {
			store.add(form.getFieldValues());
			Ext.apply(store.getProxy().extraParams, { pubId: this.curPub});				
			var rec=store.getAt(0);
			rec.set('appName',form.findField('appName').getSubmitValue());			
			var imagePreviewsJson=Ext.encode(Ext.pluck(this.getImgPreviewGrid().getStore().data.items, 'data'));
			rec.set('imagePreviews',imagePreviewsJson);
			store.sync({
				callback: function(){		    	    	
					Ext.Msg.alert('Status', 'Application was created successfully.');
				}
			});			
			form.reset();
			this.refreshPanel();
			store.removeAll();
		}else 
			Ext.Msg.alert('Status', 'Please fill out all of the required fields.'); 

	},

	submitEditingAppHandler: function (button){

		var form=Ext.ComponentQuery.query('#editAppTab')[0];
		var store;
		if (form.down('#Scppanel')){
			store=this.getScpApplicationsStore();
		}  else {
			store=this.getDpsApplicationsStore();
		}
		form=form.getForm();			 
		this.rec.set(form.getFieldValues());
		if (form.isValid()) {
			if (this.getImagePreviewsStore().getUpdatedRecords().length>0 || 
					this.getImagePreviewsStore().getRemovedRecords().length>0 ||
					this.getImagePreviewsStore().getNewRecords().length>0){
				this.getImagePreviewsStore().sync({					 
					callback:	 function(){
						scope:this;
				store.sync({							
					callback: function(){
						store.removeAll();
						this.removeAll();
						Ext.Msg.alert('Status', 'Application was updated successfully.');
					}
				});
				store.removeAll();
				this.removeAll();
					}
				});

				this.getImagePreviewsStore().removed=[];
			}else{
				store.sync({
					callback: function(){		 
						Ext.Msg.alert('Status', 'Application was updated successfully.');
						store.removeAll();
					}
				});

			}

			form.reset();
			this.refreshPanel();			
		}else 
			Ext.Msg.alert('Status', 'Please fill out all of the required fields.');   			    		
	},

	loadDpsDefaultsHandler: function (){
		this.getDpsServersStore().load({
			scope:this,
			callback: function(records, operation, success){
				scope:this;
		var record=this.getDpsServersStore().getAt(0);
		this.getDpsServer().loadRecord(record);
			}});		 
	},

	loadApplicationDataHandler: function (appcomb){
		this.curApp=appcomb.getValue();
		var type=this.getAppNames().getStore().getById(this.curApp).get('type');
		this.getAppType().setValue(type);
		Ext.apply(this.getImagePreviewsStore().getProxy().extraParams, { // set parameters that fetch the list of applications for a certain pub
			appId: appcomb.getValue()});

		if (type.toString()=="dps"){
			Ext.apply(this.getDpsApplicationsStore().getProxy().extraParams, { // set parameters that fetch the list of applications for a certain pub
				appId: appcomb.getValue(),
				pubId: this.curPub});
			this.getDpsApplicationsStore().load({
				scope:this,
				callback: function(records, operation, success){
					scope:this;
			this.loadDps();
				}});
		} else {
			Ext.apply(this.getScpApplicationsStore().getProxy().extraParams, { // set parameters that fetch the list of applications for a certain pub
				appId: appcomb.getValue(),
				pubId: this.curPub});
			this.getScpApplicationsStore().load({
				scope:this,
				callback: function(records, operation, success){
					scope:this;
			this.loadScp();
				}});			 
		} 
	},

	loadScp: function () {
		scope:this;
	this.getAppType().setDisabled(false);
	this.refreshPanel();
	this.getEditAppTab().doLayout();	
	this.getEditAppTab().add({xtype:'Scp'});
	this.rec=this.getScpApplicationsStore().getAt(0);	 
	var ScpForm=Ext.ComponentQuery.query('#editAppTab')[0];	 	
	ScpForm.loadRecord(this.rec);
	this.loadImagePreviewGrid();


	},
	loadImagePreviewGrid: function(){
		this.getImagePreviewsStore().load();
		this.getImgPreviewGrid().reconfigure(this.getImagePreviewsStore());
	},

	loadDps: function (){
		scope:this;
	this.refreshPanel();
	this.getAppType().setDisabled(false);
	this.getEditAppTab().add({xtype:'dps'});
	this.rec=this.getDpsApplicationsStore().getAt(0);	 	
	this.getEditAppTab().loadRecord(this.rec);
	this.loadImagePreviewGrid();

	},	
	disableImgNameConvention: function (e) {	
		if (this.flag)
			this.getImgPreviewGrid().getPlugin('rowEditing').editor.form.findField('imgConvention').enable();
		else this.getImgPreviewGrid().getPlugin('rowEditing').editor.form.findField('imgConvention').disable();

	},
	addImgPreview: function (button){		 
		this.flag=true;
		var newImgPreview={
				regex:'',
				msgError:'',
				imgConvention:''
		};
		var rowEditor = this.getImgPreviewGrid().getPlugin('rowEditing');
		if(rowEditor.editing){
			return false;
		} // if there is a current edit; don't do anything just return

		rowEditor.startAdd(newImgPreview,0); // insert the new row into the the beginning and invoke the row editor	    	
		this.flag=false;
	},

	removeImgPreview: function (button){			
		var grid = this.getImgPreviewGrid();		
		var rowEditor = grid.getPlugin('rowEditing');   	
		rowEditor.cancelEdit();			 
		var sm = grid.getSelectionModel();
		Ext.Array.forEach(sm.getSelection(),function(model) {
			grid.getStore().remove(model); // remove automatically refreshes the grid's view
		});
	},

	publicationNameHandler: function (pub){
		this.curPub=pub.getValue(); 
	},

	loadApplicationNamesHandler: function(pub){				
		var appCombo = this.getAppNames();
		this.curPub=pub.getValue();
		Ext.apply(appCombo.getStore().getProxy().extraParams, { // set parameters that fetch the list of applications for a certain pub
			pubId: this.curPub,
			action: 'single'});					 
		appCombo.getStore().filter([{
			filterFn: function (item)
			{
				return  (item.get('type') =='scp' ||item.get('type') =='dps');
			}}]);
		appCombo.getStore().load();    	
		appCombo.enable();
		this.refreshPanel();
	},

	refreshPanel: function (tabPanel){
		var component=Ext.getCmp('dpsType');
		if (component){
			component.destroy();
		}			 			 
		component=Ext.getCmp('ScpType');
		if (component){
			component.destroy();
		}
	},

	appTypeHandler: function (combobox,value){
		var cmp=Ext.ComponentQuery.query('#createAppTab ')[0];
		switch(combobox.getValue()){
		case 1: 
			this.refreshPanel();
			cmp.add({xtype:'Scp'});					  		
			break;
		case 2: 
			this.refreshPanel();
			cmp.add({xtype:'dps'});								
			break;
		}
		cmp.doLayout(); 
	},
	contentEmailCheckboxHandler: function(checkbox){
		var tmp=checkbox.up('#emailContentUploadPanel');
		if (checkbox.checked){	
			tmp.down('contentEmailSettings').setDisabled(false);
		}else  tmp.down('contentEmailSettings').setDisabled(true);
	},
	previewEmailCheckboxHandler: function(checkbox){
		var tmp=checkbox.up('#emailPreviewPanel');
		if (checkbox.checked){						
			tmp.down('previewEmailSettings').setDisabled(false);
		}else   tmp.down('previewEmailSettings').setDisabled(true);
	}
});
