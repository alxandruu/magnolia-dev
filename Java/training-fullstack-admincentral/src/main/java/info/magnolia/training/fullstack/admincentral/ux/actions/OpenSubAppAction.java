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
package info.magnolia.training.fullstack.admincentral.ux.actions;
 
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.templating.functions.TemplatingFunctions;
import info.magnolia.ui.ValueContext;
import info.magnolia.ui.api.action.AbstractAction;
import info.magnolia.ui.api.action.ActionExecutionException;
import info.magnolia.ui.api.location.DefaultLocation;
import info.magnolia.ui.api.location.Location;
import info.magnolia.ui.api.location.LocationController;
 
import javax.inject.Inject;
import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
 
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.appender.db.jdbc.JdbcDatabaseManager;

import com.vaadin.sass.internal.Definition;
 
/**
 * Opens the current selected node (passed as parameter) and opens it in the configured target app & subApp .
 */
public class OpenSubAppAction extends AbstractAction<OpenSubAppActionDefinition> {
  
   
    private final ValueContext<Item> valueContext;
    private final LocationController locationController;
    private final TemplatingFunctions templatingFunctions;
  
    
    public OpenSubAppAction(final OpenSubAppActionDefinition definition,  ValueContext<Item> valueContext,  LocationController locationController, TemplatingFunctions templatingFunctions) {
        super(definition);
        this.valueContext = valueContext;
        this.locationController = locationController;
        this.templatingFunctions = templatingFunctions;
        
        
    }
  
    @Override
    public void execute() throws ActionExecutionException {
        System.out.println("Action 'OpenSubAppAction' was clicked.");
        String pathToOpen = "/";
        try {
            
        	
        	
            // Get the clicked JCR Item
            Item jcrItem = valueContext.getSingleOrThrow();
            Node chosenJcrNode;
             
            // Item is a Node
            if (jcrItem.isNode()) {
                chosenJcrNode = (Node) jcrItem;
            } else { // Item is a property -> get parent Node
                chosenJcrNode = jcrItem.getParent();
                 
            }
 
            // In the context of 'website' workspace this means its a page node (true), otherwise it would be a component or area node (false).
            // This is needed for the call back to the pages app as it con't open directly a component node -> opens the page of it.
            boolean isContent = NodeUtil.isNodeType(chosenJcrNode, NodeTypes.Content.NAME);
             
            // No need of getting the parent 'mgnl:content' node for the parameters
            if (isContent || getDefinition().isUseContentSubNodes()) {
            	String path = chosenJcrNode.getPath();
                pathToOpen = path;
                System.out.println("Action 'OpenSubAppAction' reached the IF clause. Current 'pathToOpen' value is: " + pathToOpen);
  
            } else {
                
            	String path = chosenJcrNode.getParent().getPath();
            	String path2 =     	templatingFunctions.parent(chosenJcrNode, NodeTypes.Content.NAME).getPath();
                pathToOpen = path2;
                System.out.println("Action 'OpenSubAppAction' reached the ELSE clause. Current 'pathToOpen' value is: " + pathToOpen);
                System.out.println("Action 'OpenSubAppAction' reached the ELSE clause. Current 'pathToOpen2' value is: " + path);
            }
          
            if (StringUtils.isNotBlank(getDefinition().getAddToParameters())) {
                pathToOpen += getDefinition().getAddToParameters();
            }
  
        } catch (RepositoryException e) {
            throw new ActionExecutionException(e);
        }
  
        DefaultLocation location =  new DefaultLocation(Location.LOCATION_TYPE_APP, getDefinition().getAppName(), getDefinition().getSubAppId(), pathToOpen );
        locationController.goTo(location);
        
    }
}