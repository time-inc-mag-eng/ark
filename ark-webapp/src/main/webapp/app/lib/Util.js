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
Ext.define('Ark.lib.Util', {
	singleton: true,
	
	convertToUTC: function(value) {
		if(typeof value == 'string' && value.match(/^(\d{4}\-\d\d\-\d\d([tT][\d:\.]*)?)([zZ])?$/)) { // iso8601 utc
			var d = new Date(value); // constructing to a date will convert it to localized time so add the offset
			return Ext.Date.add(d, Ext.Date.MINUTE, d.getTimezoneOffset());
		} else if(value instanceof Date){ // date is in localized time convert to utc before saving to store
			return Ext.Date.add(value, Ext.Date.MINUTE, value.getTimezoneOffset());
		} else
			return value;
	},
	
    /**
     * Returns a date rendering function that can be reused to apply a date format multiple times efficiently
     * @param {String} format Any valid date format string. Defaults to {@link Ext.Date#defaultFormat}.
     * @return {Function} The date formatting function
     */
    utcToLocaleTimeRenderer : function(format) {
        return function(v) { 
        	if(!v)
        		return "N/A";
        	
           	var timeDiffFromUTC = -v.getTimezoneOffset();
        	var x = Ext.Date.add(v, Ext.Date.MINUTE, timeDiffFromUTC);
            return Ext.util.Format.date(x, format);
        };
    }
	
});
