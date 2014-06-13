Ext.define('Ext.ux.window.IframeWindow', {
    extend: 'Ext.window.Window',
    alias: ['widget.iframewindow'],

    onRender: function() {
        this.bodyCfg = {
            tag: 'iframe',
            src: this.src,
            cls: this.bodyCls,
            style: {
                border: '0px none'
            }
        };
        Ext.ux.window.IframeWindow.superclass.onRender.apply(this, arguments);
    }

});

