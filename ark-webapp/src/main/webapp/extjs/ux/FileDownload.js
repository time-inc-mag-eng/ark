Ext.define('Ext.ux.FileDownload', {
	extend: 'Ext.Component',
	alias: 'widget.FileDownloader',
	autoEl: {
		tag: 'iframe', 
		cls: 'x-hidden', 
		src: Ext.SSL_SECURE_URL
	},
	stateful: false,
	load: function(config){
		var e = this.getEl();
		e.dom.src = config.url + (config.params ? '?' + Ext.urlEncode(config.params) : '');
		e.dom.onload = function() {
			if(e.dom.contentDocument.body.childNodes[0] &&  e.dom.contentDocument.body.childNodes[0].wholeText == '500') {
				Ext.Msg.show({
					title: 'Error',
					msg: 'Internal Server Error.',
					buttons: Ext.Msg.OK,
					icon: Ext.MessageBox.ERROR   
				});
			}
		};
	}
});