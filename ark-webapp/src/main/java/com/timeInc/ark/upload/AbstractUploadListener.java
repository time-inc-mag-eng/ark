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
package com.timeInc.ark.upload;

import java.io.File;

import com.timeInc.ark.backup.Backup;
import com.timeInc.ark.event.AbstractEvent;
import com.timeInc.ark.event.IssueMetaEvent;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.packager.PackageInfo;
import com.timeInc.mageng.util.compression.Unpacker.Method;
import com.timeInc.mageng.util.event.EventManager;
import com.timeInc.mageng.util.file.FileUtil;

/**
 * Abstract implementation for events corresponding to {@link MetaUploadFacade}
 */
public abstract class AbstractUploadListener implements UploadListener {
	private final EventManager<Class<? extends AbstractEvent>, AbstractEvent> wfEvent;
	private final UserUploadConfig config;
	private final Backup backup;
	protected File backupPath;

	/**
	 * Instantiates a new abstract upload listener.
	 *
	 * @param wfEvent the wf event
	 * @param config the config
	 * @param backup the backup
	 */
	public AbstractUploadListener(EventManager<Class<? extends AbstractEvent>, AbstractEvent> wfEvent, UserUploadConfig config, Backup backup) {
		this.wfEvent = wfEvent;
		this.config = config;
		this.backup = backup;
	}
	

	/**
	 * Backs up the the uploaded file.
	 * @param meta
	 * @param packMetaData
	 */
	@Override
	public final void beforeUpload(IssueMeta meta, PackageInfo packMetaData) {
		
		File tempDir = FileUtil.createTempDirectory(config.getWorkingDirectory());
		
		File backupFile = backup.backup(meta, packMetaData,
				config.getUnpacker().unpack(Method.NONE), 
				config.getUnpacker().unpack(Method.DECOMPRESS_IF_POSSIBLE), 
				config.isNewUpload(),
				tempDir);
		
		setBackupPath(backupFile);
	}
	
	private void setBackupPath(File backupPath) {
		this.backupPath = backupPath;
	}
	
	/**
	 * Sends an event of type {@link AbstractUploadListener#getWorkFlowEvent(IssueMeta, String)} using 
	 * the original upload name and packaged upload name as the description.
	 * Invokes {@link AbstractUploadListener#finalizeSuccess(IssueMeta, PackageInfo)} for subclasses to implement
	 * custom logic to handle a success upload
	 * @param meta
	 * @param data
	 */
	@Override
	public final void success(IssueMeta meta, PackageInfo data) {
		sendEvent(getWorkFlowEvent(meta, "Uploaded:" + config.getUnpacker().unpack(Method.NONE).getName()  + " Delivered:" + data.getPrettyName()));
		finalizeSuccess(meta, data);
	}
	
	protected abstract void finalizeSuccess(IssueMeta meta, PackageInfo data);
	
	
	/**
	 * Sends an event of type {@link AbstractUploadListener#getWorkFlowEvent(IssueMeta, String)} using the 
	 * failure reason as the description
	 * @param meta
	 * @param reason the failure reason
	 */
	@Override
	public void fail(IssueMeta meta, String reason) {
		sendEvent(getWorkFlowEvent(meta, reason));
	}

	protected abstract IssueMetaEvent getWorkFlowEvent(IssueMeta meta, String reason);

	private void sendEvent(IssueMetaEvent event) {
			wfEvent.notify(event.getClass(), event);
	}
}
