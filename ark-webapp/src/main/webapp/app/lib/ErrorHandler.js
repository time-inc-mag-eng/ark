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
Ext.define('Ark.lib.ErrorHandler', {
	statics: {
		handleError: function(response,application) {
			if (response.responseText)	 {
				var jsonObj = Ext.decode(response.responseText,true);
				if (jsonObj && jsonObj.error) { // 200 response but an actual failure that is to be handled by the front end
					var rootJson = jsonObj.error.error == null ? jsonObj.error : jsonObj.error.error;
					
					var errorCode = rootJson.errorCode;
					var reason = rootJson.reason ;
					
					if(errorCode == 'invalid_session') {
						Ext.Msg.alert('Session expired', reason, function() {
							application.fireEvent('userstatuschange',Ark.lib.User.NO_ACCESS);
						});
					} else if(errorCode == 'info') {
							Ext.Msg.alert('Error', reason, baseInfoHandler);
					} else if(errorCode == 'constraint') {
						
						var failures = rootJson.customData;
						
						var failureStr = '';
						
						Ext.Array.pluck(failures, 'reason').forEach(function(reason) {
							failureStr += reason + '<br>';
						});
						
						Ext.Msg.alert('Modification error', failureStr, baseInfoHandler);	
					} else 
						Ext.Msg.alert('Error', reason, baseHandler);
				}
				else {
					Ext.Msg.alert('Error', response.statusText + ' ' + response.status, baseHandler);
				}
			}
			else { 
				Ext.Msg.alert('Error','Unknown error: Unable to understand response from the server. Maybe it is down?');
			}
			
			
			function baseInfoHandler() {
				application.fireEvent('remoteInfoException');
			}
			
			
			function baseHandler() {
				application.fireEvent('remoteException');
			}
		},
		
		handleFormError: function(action,application) {
			Ark.lib.ErrorHandler.handleError(action.response,application);
		}
	}	
});
