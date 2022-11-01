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
package info.magnolia.training.fullstack.templating.advanced.templates.components;

import info.magnolia.cms.core.AggregationState;
import info.magnolia.cms.util.SelectorUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.context.WebContext;
import info.magnolia.dam.api.DamException;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.configured.ConfiguredTemplateDefinition;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.templating.freemarker.Directives;
import info.magnolia.templating.functions.TemplatingFunctions;

import java.awt.image.ImagingOpException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.lang3.StringUtils;

/**
 * This class is optional and represents the configuration for the training-templating module.
 * By exposing simple getter/setter/adder methods, this bean can be configured via content2bean
 * using the properties and node from <tt>config:/modules/training-templating</tt>.
 * If you don't need this, simply remove the reference to this class in the module descriptor xml.
 */
public class RedirectPageModel<RD extends ConfiguredTemplateDefinition> extends RenderingModelImpl<RD> {

	 	protected static final String TARGET_LINK_PROPERTY_NAME = "targetPageLink";
	 
	    final private TemplatingFunctions templatingFunctions;
	 
	    @Inject
	    public RedirectPageModel(Node content, RD definition, RenderingModel<?> parent, TemplatingFunctions templatingFunctions) {
	        super(content, definition, parent);
	        this.templatingFunctions = templatingFunctions;
	        this.templatingFunctions.no
		        
	    }
	 
	    @Override
	    public String execute() {
	    	//System.out.println(MgnlContext.getUser().getName());
	    	//System.out.println(MgnlContext.getParameters());
	
	        // In edit mode we want to render the template, so in NOT-Edit mode we do the redirect.
	        if (!templatingFunctions.isEditMode()) {
	        	
	            String redirectTo = "";
	            
	            try {
	                redirectTo = getRedirectLinkManuallyDefined();
	                
	                if (StringUtils.isBlank(redirectTo)) {
	                    redirectTo = getRedirectLinkToFirstChild();
	                }
	             
	                final WebContext webContext = MgnlContext.getWebContext();
	                if (StringUtils.isNotBlank(redirectTo)) {
	                	webContext.getResponse().sendRedirect(redirectTo);
	                	
	                    /*
	                     * TODO 3
	                     * Do:
	                     * - get from the 'webContext' the response object: send a redirect to the 'redirectTo' value
	                     */
	                } else {
	                	webContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
	                    /*
	                     * TODO 4
	                     * Do:
	                     * - get from the 'webContext' the response object: Return a 404 error code
	                     * (hint: get the 404 over 'HttpServletResponse')
	                     */
	                }
	             
	            } catch (RepositoryException e) {
	                throw new RuntimeException(e);
	            } catch (IOException e) {
					e.printStackTrace();
				} 
	 
	            // Tell the renderer to skip the rendering after triggering the redirect on the response.
	            return RenderingModel.SKIP_RENDERING;
	        }
	        return super.execute();
	    }
	 
	    public String getRedirectLinkToFirstChild() throws RepositoryException {
	        // Little starting help: - collect all children of the current page of type 'mgnl:page'
	        Iterable<Node> allChildren = NodeUtil.getNodes(content, NodeTypes.Page.NAME);
	        Iterator<Node> childrenList = allChildren.iterator();
	        if(childrenList.hasNext()) {
	        	return templatingFunctions.link(childrenList.next());
	        }
	        /*
	         * TODO 1
	         * Do:
	         * - get the iterator of 'allChildren'
	         * - if there is a 'next' item in the iterator, get the next item -> its the first child
	         * - link over to the 'templatingFunctions' to the first child node found and return it.
	         */
	        return null;
	    }
	     
	    public String getRedirectLinkManuallyDefined() throws RepositoryException {
	        // Little starting help: - check if 'content' has a property 'path' and that it's not empty
	        if (content.hasProperty(TARGET_LINK_PROPERTY_NAME) && StringUtils.isNoneBlank(PropertyUtil.getString(content, TARGET_LINK_PROPERTY_NAME))) {
	        	String textProperty = content.getProperty(this.TARGET_LINK_PROPERTY_NAME).getString();
	    		// System.out.println(this.content.getParent().getName());
	    		if (StringUtils.isNotBlank(textProperty)) {
	    			Node redirectNode = NodeUtil.getNodeByIdentifier(RepositoryConstants.WEBSITE, textProperty);
	    			return templatingFunctions.link(redirectNode);
	    		}
	        	
	        }
	        return null;
	    }
	 
	    public String getTest() {
	        return "test succeeded!";
	    }

}
