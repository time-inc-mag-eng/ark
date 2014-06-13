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
package com.timeInc.ark.issue.action;

import java.util.Date;

import com.timeInc.ark.application.DpsApplication;
import com.timeInc.ark.event.AbstractEvent;
import com.timeInc.ark.event.IssueMetaEvent;
import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.role.PublicationGroup;
import com.timeInc.ark.role.User;
import com.timeInc.mageng.util.event.EventManager;
import com.timeInc.mageng.util.exceptions.PrettyException;
import com.timeInc.mageng.util.misc.Status;

/**
 * Updates a folio for an @{link IssueMeta} that belongs to a {@link DpsApplication}
 */
public abstract class UpdateFolioAction extends IssueMetaAction<Status> {
	
	/**
	 * Instantiates a new update folio action.
	 *
	 * @param em the em
	 * @param user the user
	 */
	public UpdateFolioAction(EventManager<Class<? extends AbstractEvent>, AbstractEvent> em,  User user) {
		super(em, user);
	}

	@Override
	final protected void checkPrecondition(IssueMeta meta) {
		if(meta.getFolio() == null) {
			throw new IllegalArgumentException("IssueMeta:" + meta.getReferenceId() + " needs to have an associated folio");
		}
	}

	abstract Status executeFnForDpsApp(DpsApplication app, IssueMeta meta);


	@Override
	protected final Status executeFn(IssueMeta meta) {
		DpsApplication app = meta.getApplication(DpsApplication.class);
		return executeFnForDpsApp(app, meta);
	}


	/**
	 * Unpublishes a folio.
	 */
	public static class UnpublishFolio extends UpdateFolioAction  {
		
		/**
		 * Instantiates a new unpublish folio.
		 *
		 * @param em the em
		 * @param user the user
		 */
		public UnpublishFolio(EventManager<Class<? extends AbstractEvent>, AbstractEvent> em,  User user) {
			super(em, user);
		}

		@Override
		Status executeFnForDpsApp(final DpsApplication app, final IssueMeta meta) {
			return app.unpublish(meta);
		}

		@Override
		protected IssueMetaEvent getLogType(Status result, Date occurrence,
				PublicationGroup group, String issue, Date coverDate,
				String refid, String appName) {

			return new IssueMetaEvent.UnpublishFolio(user, occurrence, result.getDescription(), group, issue, coverDate, refid, appName);
		}

	}	


	/**
	 * The Class PublishFolioAdapter.
	 */
	public static class PublishFolioAdapter extends UpdateFolioAction {
		private final PublishAction publishAction;

		/**
		 * Instantiates a new publish folio adapter.
		 *
		 * @param em the em
		 * @param user the user
		 */
		public PublishFolioAdapter(EventManager<Class<? extends AbstractEvent>, AbstractEvent> em, User user) {
			super(em, user);

			this.publishAction = new PublishAction(em, user);
		}

		@Override
		Status executeFnForDpsApp(DpsApplication app, IssueMeta meta) {
			return publishAction.executeFn(meta);
		}


		@Override
		protected IssueMetaEvent getLogType(Status result, Date occurrence,
				PublicationGroup group, String issue, Date coverDate,
				String refid, String appName) {

			return publishAction.getLogType(result, occurrence, group, issue, coverDate, refid, appName);

		}

	}

	/**
	 * Deletes a folio.
	 */
	public static class DeleteFolio extends UpdateFolioAction  {
		private final UnpublishFolio unpublishAction;

		/**
		 * Instantiates a new delete folio.
		 *
		 * @param em the em
		 * @param user the user
		 * @param unpublishAction the unpublish action
		 */
		public DeleteFolio(EventManager<Class<? extends AbstractEvent>, AbstractEvent> em,  User user, UnpublishFolio unpublishAction) {
			super(em, user);
			this.unpublishAction = unpublishAction;
		}

		@Override
		Status executeFnForDpsApp(final DpsApplication app, final IssueMeta meta) {
			try {
				if(meta.isPublished()) {
					Status unpubStatus = unpublishAction.performActionOn(meta);
					if(!unpubStatus.isError()) {
						deleteFolio(meta, app);
					} else {
						return Status.getFailure("Could not delete folio because unpublishing failed.");
					}
				} else {
					deleteFolio(meta, app);
				}
			} catch(PrettyException ex) {
				return Status.getFailure(ex.getFriendlyMsg());
			}

			return Status.getSuccess();
		}


		private void deleteFolio(IssueMeta meta, DpsApplication app) {
			app.deleteFolio(meta.getFolio());
			meta.resetState();
		}

		@Override
		protected IssueMetaEvent getLogType(Status result, Date occurrence,
				PublicationGroup group, String issue, Date coverDate,
				String refid, String appName) {

			return new IssueMetaEvent.DeleteFolio(user, occurrence, result.getDescription(), group, issue, coverDate, refid, appName);
		}
	}
}
