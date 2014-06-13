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
Ext.define('Ark.lib.Constant', {
	singleton: true,

	saleDateHeaderName: 'Publication Date',
	shortDateHeaderName: 'Cover Date',
	referenceIdHeaderName: 'Reference Id',
	applicationHeaderName: 'Application',
	issueNameHeaderName: 'Issue',
	testIssueNameHeader: 'Test'
});
Ext.define('Globals', { 
    singleton: true, 
    imagePreviewHelp:'These image conventions are required for DPS application: COVER_LARGE_LANDSCAPE, COVER_LARGE. '+
    'At least one image preview is required for SCP application.',
    help:'These variables are allowed: ${pub}, ${issue}, ${saledate}, ${shortpub}, ${shortdate}, ${app}; '
    	+'e.g. /foldername/${issue}.txt',
    vendorName: 'This field is used for file naming part, and DPS applications publication name',
    emailHelp: 'These variables are allowed: ${pub}, ${issue}, ${saledate}, ${shortpub}, ${shortdate}, ${app}. '
}); 
