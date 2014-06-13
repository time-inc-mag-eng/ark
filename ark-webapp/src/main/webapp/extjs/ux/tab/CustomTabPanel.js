Ext.define('Ext.ux.tab.CustomTabPanel',{
    extend: 'Ext.tab.Panel',
    alias: 'widget.CustomTabPanel',
    initComponent: function() {
       var me = this;
       me.callParent(arguments);
       me.tabBar.weight = 2;
    }
});


