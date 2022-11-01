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
import info.magnolia.ui.api.location.DefaultLocation;
import info.magnolia.ui.api.location.LocationController;
import info.magnolia.ui.framework.app.BaseSubApp;
import javax.inject.Inject;
 
// As the presenter's duty, it implements the View's Listener providing the View's callback method to the presenter.
public class HelloWorldMainSubApp extends BaseSubApp<HelloWorldMainSubAppView> implements HelloWorldMainSubAppView.Listener {
    private LocationController locationController;
     
    // The View is injected into the presenter's constructor. The according View's implementation is defined by IOC component provider.
    @Inject
    public HelloWorldMainSubApp(final SubAppContext subAppContext, HelloWorldMainSubAppView view, LocationController locationController) {
        super(subAppContext, view);
        this.locationController = locationController;
    }
     
    @Override
    protected void onSubAppStart() {
        // Register this presenter as the view's listener.
        getView().setListener(this);
        // Call the view to display something.
        getView().addUser("Lisa");
        getView().addUser("Mark");
    }
     
    // Implements the View's Listener method. Is the View's callback function to the presenter.
    @Override
    public void greetUser(String user) {
        locationController.goTo(new DefaultLocation(DefaultLocation.LOCATION_TYPE_APP, getAppContext().getName(), "greeter", user));
    }
}