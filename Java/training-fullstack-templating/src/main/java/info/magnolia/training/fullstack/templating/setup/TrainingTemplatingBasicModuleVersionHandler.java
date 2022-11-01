/**
 * This file Copyright (c) 2020 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This file is dual-licensed under both the Magnolia
 * Network Agreement and the GNU General Public License.
 * You may elect to use one or the other of these licenses.
 *
 * This file is distributed in the hope that it will be
 * useful, but AS-IS and WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, or NONINFRINGEMENT.
 * Redistribution, except as permitted by whichever of the GPL
 * or MNA you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or
 * modify this file under the terms of the GNU General
 * Public License, Version 3, as published by the Free Software
 * Foundation.  You should have received a copy of the GNU
 * General Public License, Version 3 along with this program;
 * if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * 2. For the Magnolia Network Agreement (MNA), this file
 * and the accompanying materials are made available under the
 * terms of the MNA which accompanies this distribution, and
 * is available at http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.training.fullstack.templating.setup;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.CheckAndModifyPropertyValueTask;
import info.magnolia.module.delta.OrderNodeBeforeTask;
import info.magnolia.module.delta.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is optional and lets you manager the versions of your module,
 * by registering "deltas" to maintain the module's configuration, or other type of content.
 * If you don't need this, simply remove the reference to this class in the module descriptor xml.
 */
public class TrainingTemplatingBasicModuleVersionHandler extends DefaultModuleVersionHandler {
	
	@Override
	protected List<Task> getExtraInstallTasks(InstallContext installContext) {
		final List<Task> tasks = new ArrayList<Task>();
		
		//Deactivating the subscriber
        tasks.add(new CheckAndModifyPropertyValueTask("/modules/publishing-core/config/receivers/magnoliaPublic8080", "enabled", "true", "false"));

        // Setting rest-tools apiBasepath including context path.
        tasks.add(new CheckAndModifyPropertyValueTask("/modules/rest-tools/config", "apiBasepath", "http://localhost:8080/.rest", "http://localhost:8080/training-fullstack-webapp/.rest"));

        // Have the training-templating module at the top of the modules list -> not needed anymore, leaving in as example.
        // tasks.add(new OrderNodeBeforeTask("/modules/training-templating", "core"));

		return tasks;
	}

}