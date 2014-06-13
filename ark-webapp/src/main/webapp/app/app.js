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

Ext.Loader.setConfig({
	disableCaching: true, // set this to false to enable breakpoints while debugging
	
	paths: {
		'Ext.ux': 'extjs/ux',
		'Ark.lib': 'app/lib'
	},
	
	enabled: true
});


Ext.require(['Ark.lib.Constant', 'Ark.lib.User', 'Ark.lib.Util']); // load singletons

Ext.Img.override({
	initRenderTpl: Ext.emptyFn
});


Ext.Ajax.timeout = 120000; // 120 seconds


Ext.override(Ext.LoadMask, {
    onHide: function() { this.callParent(); }
});  

Ext.override(Ext.grid.Scroller, {
	afterRender : function() {
		var me = this;
		me.callParent();

		me.mon(me.scrollEl, 'scroll', me.onElScroll, me);
		Ext.cache[me.el.id].skipGarbageCollection = true;
		Ext.cache[me.scrollEl.id].skipGarbageCollection = true;
	}
});

Ext.override(Ext.form.field.Text, { 
	validator:function(text){  	
		if(this.allowBlank==false && text.length>=1 && Ext.util.Format.trim(text).length==0)
			return "Can not only contain whitespaces";
		else
			return true;
	}
});  


Ext.data.AbstractStore.override({
	sync: function(config) 
	{
		config = config || {};

		var defaults = {
				callback: Ext.emptyFn
		};

		config = Ext.apply(defaults, config);

		var me        = this,
		options   = {},
		toCreate  = me.getNewRecords(),
		toUpdate  = me.getUpdatedRecords(),
		toDestroy = me.getRemovedRecords(),
		needsSync = false;

		if (toCreate.length > 0) {
			options.create = toCreate;
			needsSync = true;
		}

		if (toUpdate.length > 0) {
			options.update = toUpdate;
			needsSync = true;
		}

		if (toDestroy.length > 0) {
			options.destroy = toDestroy;
			needsSync = true;
		}

		if (needsSync && me.fireEvent('beforesync', options) !== false) {
			var batch = me.proxy.batch(options, me.getBatchListeners());

			batch.on('complete', Ext.bind(config.callback, this, [this, options]), this, {single:true});
		}
	}
});  


Ext.override(Ext.grid.RowEditor, {
	loadRecord: function(record) {
		var me = this,
		form = me.getForm(),
		valid = form.isValid();
		form.loadRecord(record);
		if(me.errorSummary) {
			me[valid ? 'hideToolTip' : 'showToolTip']();
		}
		Ext.Array.forEach(me.query('>displayfield'), function(field) {
			me.renderColumnData(field, record);
		}, me);
	}
});

if(typeof Ext != 'undefined'){ // allow copying cells for grids
	Ext.core.Element.prototype.unselectable = function(){return this;};
	Ext.view.TableChunker.metaRowTpl = [
	                                    '<tr class="' + Ext.baseCSSPrefix + 'grid-row {addlSelector} {[this.embedRowCls()]}" {[this.embedRowAttr()]}>',
	                                    '<tpl for="columns">',
	                                    '<td class="{cls} ' + Ext.baseCSSPrefix + 'grid-cell ' + Ext.baseCSSPrefix + 'grid-cell-{columnId} {{id}-modified} {{id}-tdCls} {[this.firstOrLastCls(xindex, xcount)]}" {{id}-tdAttr}><div class="' + Ext.baseCSSPrefix + 'grid-cell-inner ' + Ext.baseCSSPrefix + 'unselectable" style="{{id}-style}; text-align: {align};">{{id}}</div></td>',
	                                    '</tpl>',
	                                    '</tr>'
	                                    ];
}



/**
 * Overrides the constructor of Ext.app.Application to 
 * perform a global override on Ext.data.proxy.Ajax. This is done so that we can have
 * a universal ajax proxy exception listener where it has access to the application reference where
 * we can fire application wide events. Also Ext.grid.Panel is overridden to include an
 * additional method that allows us to add an additional columns dynamically.
 */
Ext.override(Ext.app.Application, {
	constructor:function(config) {
		var me = this;

		Ext.override(Ext.data.proxy.Ajax, {
			constructor: function() {
				this.callOverridden(arguments);
				this.addListener('exception',function(proxy,response) { // register a listener that fire an application wide 'ajaxException' event
					me.fireEvent('ajaxException', proxy, response);
				});
			}
		});

		Ext.override(Ext.grid.Panel, { // allow columns to be added to a grid dynamically at the end of the grid
			addColumn: function(column) {
				var me = this,
				headerCt = me.headerCt;

				if (column) {
					headerCt.suspendLayout = true;
					headerCt.add(column);
					headerCt.suspendLayout = false;
					me.forceComponentLayout();
				}
			}
		});				

		this.callOverridden(arguments); // invoke original application constructor
	}
}),



Ext.application({
	name: 'Ark',
	autoCreateViewport: true,
	controllers: ['ViewPort', 'Login', 'Main']
});
