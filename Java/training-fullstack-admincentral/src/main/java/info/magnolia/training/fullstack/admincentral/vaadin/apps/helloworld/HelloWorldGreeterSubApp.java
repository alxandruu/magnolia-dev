/**
 * This file Copyright (c) 2019 Magnolia International
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
package info.magnolia.training.fullstack.admincentral.vaadin.apps.helloworld;
 
import info.magnolia.ui.api.app.SubAppContext;
import info.magnolia.ui.api.location.Location;
import info.magnolia.ui.framework.app.BaseSubApp;

import javax.inject.Inject;
 
// As the presenter's duty, it implements the View's Listener providing the View's callback method to the presenter.
public class HelloWorldGreeterSubApp extends BaseSubApp<HelloWorldGreeterSubAppView> {
    private String name;
 
    // The View is injected into the presenter's constructor. The according View's implementation is defined by IOC component provider.
    @Inject
    public HelloWorldGreeterSubApp(final SubAppContext subAppContext, final HelloWorldGreeterSubAppView view) {
        super(subAppContext, view);
    }
    // Extracts the name to greet from the {@link Location}s parameter and passes it to the {@link HelloWorldGreeterSubAppView}.
    @Override
    protected void onSubAppStart() {
        // Extracting the parameter from the location
        this.name = getCurrentLocation().getParameter();
        // Call the view to display something.
        getView().setGreeting(name);
    }
 
    // Sets the label on the tab.
    @Override
    public String getCaption() {
        return name;
    }
 
    // Checks if the current instance of the subapp is already handling the current parameter.
    @Override
    public boolean supportsLocation(Location location) {
        String newUser = location.getParameter();
        return getCurrentLocation().getParameter().equals(newUser);
    }
    
    // Try to find out when this method is called after the system called the method 'supportsLocation'. Test the behavior by returning manually 'true' or 'false' in the method 'supportsLocation' and watch the log's output.
    @Override
    public void locationChanged(Location location) {
        String currentUser = getCurrentLocation().getParameter();
        String newUser = location.getParameter();
        System.out.println("Method 'locationChanged' was called with Location's parameter: "+newUser);
        if (!currentUser.equals(newUser)) {
            System.out.println("In method 'locationChanged' a parameter change was detected. Current user is '" + currentUser + "' and new user is '" + newUser + "'.");
            HelloWorldGreeterSubAppView view = getView();
            // Updating implicit the tab's caption
            name = newUser;
            // Updating the the View's implemented methods
            view.setGreeting(newUser);
        }
        super.locationChanged(location);
    }
} 