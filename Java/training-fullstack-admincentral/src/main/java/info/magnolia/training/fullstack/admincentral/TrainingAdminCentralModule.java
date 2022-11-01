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
package info.magnolia.training.fullstack.admincentral;

import static info.magnolia.repository.RepositoryConstants.CONFIG;

import info.magnolia.module.ModuleLifecycle;
import info.magnolia.module.ModuleLifecycleContext;
import info.magnolia.observation.WorkspaceEventListenerRegistration;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

/**
 * This class is optional and represents the configuration for the training-templating module.
 * By exposing simple getter/setter/adder methods, this bean can be configured via content2bean
 * using the properties and node from <tt>config:/modules/training-templating</tt>.
 * If you don't need this, simply remove the reference to this class in the module descriptor xml.
 */
public class TrainingAdminCentralModule implements ModuleLifecycle, EventListener {

    private Map<String, ShowCaseSubBean> showCaseModuleConfigs = new HashMap<String, ShowCaseSubBean>();

    @Override
    public void start(ModuleLifecycleContext moduleLifecycleContext) {
        // The 'start' method is called on ModuleLifecycleContext.PHASE_SYSTEM_STARTUP and on ModuleLifecycleContext.PHASE_MODULE_RESTART
        // We want to register the JCR Observation only once -> using ModuleLifecycleContext.PHASE_SYSTEM_STARTUP
        // ModuleLifecycleContext.PHASE_MODULE_RESTART is called on every module's config/module's bean change.
        if (moduleLifecycleContext.getPhase() == ModuleLifecycleContext.PHASE_SYSTEM_STARTUP) {
            System.out.println("Entering the start-up phase of 'ModuleLifecycle' within the training class 'TrainingAdminCentralModule'.");
            try {
                // Observing any changes in the JCR 'config' workspace
                // Waiting 0.5 seconds for more changes, waiting in maximum 2 seconds no matter if more changes are detected.
                WorkspaceEventListenerRegistration.observe(CONFIG, "/", this).withDelay(500L, 2000L).register();
                System.out.println("JCR Observation successsfully registered on the 'config' workspace within the training class 'TrainingAdminCentralModule'.");
            } catch (RepositoryException e) {
                System.out.println("Could not register observstion due to: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop(ModuleLifecycleContext moduleLifecycleContext) {
        if (moduleLifecycleContext.getPhase() == ModuleLifecycleContext.PHASE_SYSTEM_SHUTDOWN) {
            System.out.println("Entering the shut-down phase of 'ModuleLifecycle' within the training class 'TrainingAdminCentralModule'.");
        }
    }

    @Override
    public void onEvent(EventIterator events) {
        // This class also implements the EventListener, so catching directly here the thrown JCR observation Events.
        // Of course you can implement any explicit EventListener, no need to do it in the module class itself.
        while (events.hasNext()) {
            Event event = events.nextEvent();
            try {
                String path = event.getPath();
                int eventType = event.getType();
                // The Event's type is defined in javax.jcr.observation.Event
                System.out.println("JCR Observation within the training class 'TrainingAdminCentralModule' detected a change in path: " + path + " of event type: " + eventType);
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
            // For the sake of not producing too much console output. Just syso the first event's path -> the main/initial change
            break;
        }
    }

    public Map<String, ShowCaseSubBean> getShowCaseModuleConfigs() {
        return showCaseModuleConfigs;
    }

    public void setShowCaseModuleConfigs(Map<String, ShowCaseSubBean> showCaseModuleConfig) {
        this.showCaseModuleConfigs = showCaseModuleConfig;
        // Just some code for demonstration. Usually the 'set' method does not contain any additional logic!
        sysoOfAllConfigs();
    }

    public void addShowCaseModuleConfig(String name, ShowCaseSubBean showCaseSubBean) {
        this.showCaseModuleConfigs.put(name, showCaseSubBean);
        // Just some code for demonstration. Usually the 'add' method does not contain any additional logic!
        sysoOfAllConfigs();
    }

    private void sysoOfAllConfigs() {
        System.out.println("### A list of all configured 'show case module configurations':");
        int i = 1;
        for (String key : showCaseModuleConfigs.keySet()) {
            ShowCaseSubBean subBean = showCaseModuleConfigs.get(key);
            System.out.println("###  Config #" + i + " contains the values: name='" + key + "'; weCanDefineInHere='" + subBean.getWeCanDefineInHere() + "'; whateverWeNeed='" + subBean.getWhateverWeNeed() + "'");
            ++i;
        }
    }

}
