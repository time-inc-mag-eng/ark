/*******************************************************************************
 * Copyright 2014 Time Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.timeInc.ark.api.ww;

import java.util.List;

import com.timeInc.ark.api.ww.CdsConfig.Auth;
import com.timeInc.ark.api.ww.CdsConfig.ContentLocation;
import com.timeInc.ark.api.ww.CdsConfig.MetaData;
import com.timeInc.ark.api.ww.CdsConfig.PreviewLocation;
import com.timeInc.ark.issue.remote.CdsMetaData;

/**
 * Creates an issue on Kiosk and retrieves CDSMetaData from CDP
 */
public interface WoodwingService {
	/**
	 * Create an issue on kiosk using the specified parameters.
	 * @param auth the authentication needed for this application
	 * @param appId the application to create the issue in
	 * @param previews the preview image locations
	 * @param content the content of the issue
	 * @param metaData the metadata associated with the issue
	 * @throws RuntimeException if the creation failed
	 */
	void createIssue(Auth auth, String appId, PreviewLocation previews, ContentLocation content, MetaData metaData );
	List<CdsMetaData> getIssues(Auth auth, String appId);
}

