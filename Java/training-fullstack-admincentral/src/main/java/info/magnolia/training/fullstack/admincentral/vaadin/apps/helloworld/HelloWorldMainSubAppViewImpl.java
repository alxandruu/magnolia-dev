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
 
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
 
public class HelloWorldMainSubAppViewImpl implements HelloWorldMainSubAppView {
    private VerticalLayout layout = new VerticalLayout();
    private Listener listener;
 
    public HelloWorldMainSubAppViewImpl() {
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(new Label("Say Hello!"));
    }
 
    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
 
    @Override
    public Component asVaadinComponent() {
        return layout;
    }
 
    @Override
    public void addUser(final String user) {
        Button button = new Button("Say hello to " + user + "!");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                listener.greetUser(user);
            }
        });
        layout.addComponent(button);
    }
}