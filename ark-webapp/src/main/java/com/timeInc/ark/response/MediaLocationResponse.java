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
package com.timeInc.ark.response;

import java.util.Date;

import com.timeInc.ark.media.MediaLocation;

/**
 * Response returned that represents a MediaLocation
 */
public class MediaLocationResponse {
	private final int id;
	private final String fileName;
	private final Date modified;
	private final String cdnUrl;
	private final String previewUrl;

	/**
	 * Instantiates a new origin media response.
	 *
	 * @param location the location
	 */
	public MediaLocationResponse(MediaLocation location) {
		this.id = location.getId();
		this.fileName = location.getFileName();
		this.modified = location.getModified();
		this.cdnUrl = location.getCdnUrl();
		this.previewUrl = location.getPreviewUrl();
	}

	/**
	 * A factory for creating OriginMediaResponse objects.
	 */
	public static class OriginMediaResponseFactory implements ResponseFactory<MediaLocationResponse, MediaLocation> {
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.response.ResponseFactory#getInstance(java.lang.Object)
		 */
		@Override
		public MediaLocationResponse getInstance(MediaLocation location) {
			return new MediaLocationResponse(location);
		}
	}

}
