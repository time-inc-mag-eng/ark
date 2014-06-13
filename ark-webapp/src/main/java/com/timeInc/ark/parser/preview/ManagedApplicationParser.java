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
package com.timeInc.ark.parser.preview;


import com.timeInc.ark.application.ApplicationEntity.ApplicationVisitor;
import com.timeInc.ark.application.CdsApplication;
import com.timeInc.ark.application.DpsApplication;
import com.timeInc.ark.application.ManagedApplication;
import com.timeInc.ark.application.ScpApplication;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.parser.preview.validator.FilePreviewValidator;
import com.timeInc.ark.parser.preview.validator.FolioPreviewValidator;
import com.timeInc.ark.parser.preview.validator.NewsstandValidator;
import com.timeInc.ark.parser.preview.validator.PreviewValidator;
import com.timeInc.mageng.util.misc.Status;

/**
 * Parser that validates the PreviewParsedData by ensuring the required {@link com.timeInc.ark.parser.Parser.PreviewParser.ImageKey} are available depending on the IssueMeta's application setting.
 */
public class ManagedApplicationParser extends RegexPreviewParser {
	protected ManagedApplicationParser() {
		super(new NewsstandValidator(new FilePreviewValidator()));
	}
	
	protected ManagedApplicationParser(PreviewValidator validator) {
		super(validator);
	}
	
	/* (non-Javadoc)
	 * @see com.timeInc.ark.parser.preview.RegexPreviewParser#validate(com.timeInc.ark.parser.preview.PreviewParsedData, com.timeInc.ark.issue.IssueMeta)
	 */
	@Override
	public Status validate(final PreviewParsedData parsedData, IssueMeta meta) {
		Status managedVal = meta.getApplication().accept(new ApplicationVisitor<Status>() {
			@Override
			public Status visit(CdsApplication app) {
				return checkIfAppHasManagedPreview(app);
			}

			@Override
			public Status visit(DpsApplication app) {
				return checkIfAppHasManagedPreview(app);
			}

			@Override
			public Status visit(ScpApplication app) {
				return null;
			}
			
			private Status checkIfAppHasManagedPreview(ManagedApplication<?,?> mApp) {
				if(mApp.getFeed() != null) {
					return checkIfAllKeyExist(parsedData, ImageKey.NEWSSTAND);
				}
				
				return Status.getSuccess();
			}
		});
		
		if(managedVal.isError())
			return managedVal;
		else
			return super.validate(parsedData, meta);
	}
	
	/**
	 * The Class CdsPreviewParser.
	 */
	public static class CdsPreviewParser extends ManagedApplicationParser {
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.parser.preview.ManagedApplicationParser#validate(com.timeInc.ark.parser.preview.PreviewParsedData, com.timeInc.ark.issue.IssueMeta)
		 */
		@Override
		public Status validate(final PreviewParsedData parsedData, IssueMeta meta) {
			Status validation = meta.getApplication().accept(new ApplicationVisitor<Status>() {
				@Override
				public Status visit(CdsApplication app) {
					return checkIfAllKeyExist(parsedData, ImageKey.COVERSMALL, ImageKey.COVERLARGE_PORTRAIT, ImageKey.HORIZONTAL, ImageKey.VERTICAL);
				}

				@Override
				public Status visit(DpsApplication app) {
					return Status.getSuccess();
				}

				@Override
				public Status visit(ScpApplication app) {
					return Status.getSuccess();
				}
			});
			
			if(validation.isError())
				return validation;
			else
				return super.validate(parsedData, meta);
		}
	}
	
	/**
	 * The Class DpsPreviewParser.
	 */
	public static class DpsPreviewParser extends ManagedApplicationParser {
		protected DpsPreviewParser() {
			super(new NewsstandValidator(new FolioPreviewValidator(new FilePreviewValidator())));
		}
		
		/* (non-Javadoc)
		 * @see com.timeInc.ark.parser.preview.ManagedApplicationParser#validate(com.timeInc.ark.parser.preview.PreviewParsedData, com.timeInc.ark.issue.IssueMeta)
		 */
		@Override
		public Status validate(final PreviewParsedData parsedData, IssueMeta meta) {
			Status validation = meta.getApplication().accept(new ApplicationVisitor<Status>() {
				@Override
				public Status visit(CdsApplication app) {
					return Status.getSuccess();
				}

				@Override
				public Status visit(DpsApplication app) {
					return checkIfAllKeyExist(parsedData, ImageKey.COVERLARGE_LANDSCAPE, ImageKey.COVERLARGE_PORTRAIT);
				}

				@Override
				public Status visit(ScpApplication app) {
					return Status.getSuccess();
				}
			});
			
			if(validation.isError())
				return validation;
			else
				return super.validate(parsedData, meta);
		}
	}
	
	
	protected static Status checkIfAllKeyExist(PreviewParsedData parsedData, ImageKey... keys) {
		for(ImageKey key : keys) {
			if(!parsedData.hasImage(key)) 
				return Status.getFailure("Application is missing image key:" + key);
		}
		
		return Status.getSuccess();
	}
}
