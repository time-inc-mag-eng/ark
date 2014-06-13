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
package com.timeInc.ark.util.progress;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import com.timeInc.mageng.util.progress.ProgressListener;
import com.timeInc.mageng.util.progress.wrappers.CountingOutputStream;

/**
 * A class that decorates a HttpEntity to keep track of the number of bytes uploaded
 * and inform the ProgressListener. 
 * 
 *
 */
public class HttpClient4CountingEntity implements HttpEntity {
    private final HttpEntity delegate;
    private final ProgressListener listener;
    
    
	/**
	 * Instantiates a new http client4 counting entity.
	 *
	 * @param delegate the delegate
	 * @param listener the listener
	 */
	public HttpClient4CountingEntity(HttpEntity delegate, ProgressListener listener) {
		this.delegate = delegate;
		this.listener = listener;
	}

	/* (non-Javadoc)
	 * @see org.apache.http.HttpEntity#consumeContent()
	 */
	@Override
	@Deprecated
	public void consumeContent() throws IOException {
		delegate.consumeContent();
	}

	/* (non-Javadoc)
	 * @see org.apache.http.HttpEntity#getContent()
	 */
	@Override
	public InputStream getContent() throws IOException, IllegalStateException {
		return delegate.getContent();
	}

	/* (non-Javadoc)
	 * @see org.apache.http.HttpEntity#getContentEncoding()
	 */
	@Override
	public Header getContentEncoding() {
		return delegate.getContentEncoding();
	}

	/* (non-Javadoc)
	 * @see org.apache.http.HttpEntity#getContentLength()
	 */
	@Override
	public long getContentLength() {
		return delegate.getContentLength();
	}

	/* (non-Javadoc)
	 * @see org.apache.http.HttpEntity#getContentType()
	 */
	@Override
	public Header getContentType() {
		return delegate.getContentType();
	}

	/* (non-Javadoc)
	 * @see org.apache.http.HttpEntity#isChunked()
	 */
	@Override
	public boolean isChunked() {
		return delegate.isChunked();
	}

	/* (non-Javadoc)
	 * @see org.apache.http.HttpEntity#isRepeatable()
	 */
	@Override
	public boolean isRepeatable() {
		return delegate.isRepeatable();
	}

	/* (non-Javadoc)
	 * @see org.apache.http.HttpEntity#isStreaming()
	 */
	@Override
	public boolean isStreaming() {
		return delegate.isStreaming();
	}

	/* (non-Javadoc)
	 * @see org.apache.http.HttpEntity#writeTo(java.io.OutputStream)
	 */
	@Override
	public void writeTo(OutputStream out) throws IOException {
		this.delegate.writeTo(new CountingOutputStream(out, this.listener, getContentLength()));		
	}

}
