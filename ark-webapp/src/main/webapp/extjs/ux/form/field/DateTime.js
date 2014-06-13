Ext.define('Ext.ux.form.field.DateTime', {
	extend:'Ext.form.FieldContainer',
	mixins: {
		field: 'Ext.form.field.Field'
	},
	alias: 'widget.datetimefield',
	layout: 'hbox',
	width: 200,
	height: 22,
	combineErrors: true,
	msgTarget :'side',

	dateCfg:{},
	timeCfg:{},

	initComponent: function() {
		var me = this;
		me.buildField();
		me.callParent();
		this.dateField = this.down('datefield');
		this.timeField = this.down('timefield');
		
		this.dateField.enableBubble('change');
		this.timeField.enableBubble('change');
		
		me.initField();
	},

	//@private
	buildField: function(){
		this.items = [
		              Ext.apply({
		            	  xtype: 'datefield',
		            	  format: 'm/d/Y',
		            	  width: 80,
		            	  flex: 1
		              },this.dateCfg),
		              Ext.apply({
		            	  xtype: 'timefield',
		            	  editable: false,
		            	  width: 80,
		            	  increment: 5,
		            	  flex: 1
		              },this.timeCfg)
		              ];
	},

	getValue: function() {
		var value,date = this.dateField.getSubmitValue(),time = this.timeField.getSubmitValue();
		if(date) {
			if(time){
				var format = this.getFormat();
				value = Ext.Date.parse(date + ' ' + time,format);
			} else {
				value = this.dateField.getValue();
			}
		}
		return value;
	},

	setValue: function(value){
		if(value instanceof Date) {
           	var timeDiffFromUTC = -value.getTimezoneOffset();
        	var x = Ext.Date.add(value, Ext.Date.MINUTE, timeDiffFromUTC);
			
			this.dateField.setValue(x);
			this.timeField.setValue(x);			
		}
	},

	getSubmitData: function(){
		var value = this.getValue();
		var format = this.getFormat();
		return value ? Ext.Date.format(value, format) : null;
	},

	getFormat: function(){
		return (this.dateField.submitFormat || this.dateField.format) + " " + (this.timeField.submitFormat || this.timeField.format);
	}
});