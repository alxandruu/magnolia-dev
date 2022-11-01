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

import info.magnolia.context.MgnlContext;
import info.magnolia.context.WebContext;
import info.magnolia.jcr.util.ContentMap;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.form.validators.Validator;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.configured.ConfiguredTemplateDefinition;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.templating.functions.TemplatingFunctions;

import java.io.IOException;
import java.util.Iterator;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletResponse;
import javax.xml.validation.ValidatorHandler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.sass.internal.util.StringUtil;

/**
 * This class is optional and represents the configuration for the
 * training-templating module. By exposing simple getter/setter/adder methods,
 * this bean can be configured via content2bean using the properties and node
 * from <tt>config:/modules/training-templating</tt>. If you don't need this,
 * simply remove the reference to this class in the module descriptor xml.
 */
public class AutoLinkListModel<RD extends AutoLinkListDefinition> extends RenderingModelImpl<RD> {
	private Node redirectNode;
	private static final Logger log = LoggerFactory.getLogger(AutoLinkListModel.class);

	public AutoLinkListModel(Node content, RD definition, RenderingModel<?> parent) {
		super(content, definition, parent);
		this.redirectNode = null;
		
	}

	private Node targetNode = null;

	@Override
	public String execute() {
		try {
			this.resolveTarget();
		} catch (RepositoryException e) {
			log.error("Was not able to resolve target node: " + e);
			return null;
		}

		return "success";
	}

	public String getTest() {
		return "test succeeded!";
	}

	// Using our PropertyUtil & NodeUtil for convenience coding
	private void resolveTarget() throws RepositoryException {
		// System.out.println("==> " + PropertyUtil.getProperty(content, "targetNode").getString());
		// System.out.println(this.content.getParent().getName());

		 String idOfChosenTargetNode = PropertyUtil.getString(content, "targetNode");
		    if (StringUtils.isNotEmpty(idOfChosenTargetNode)) {
		        String workspaceName = getWorkspaceName();
		        if (workspaceName.equals("dam")) {
		            idOfChosenTargetNode = StringUtils.substringAfter(idOfChosenTargetNode, "jcr:");
		        }
		        Node nodeByIdentifier = NodeUtil.getNodeByIdentifier(workspaceName, idOfChosenTargetNode);
		        this.targetNode = nodeByIdentifier;
		    }
		
	}

	public String getWorkspaceName() {
		String workspace = definition.getWorkspace();
		
		if (StringUtils.isEmpty(workspace)) {
	        workspace = RepositoryConstants.WEBSITE;
	    }
			
		return workspace;
	}

	public ContentMap getTargetNode() {
		if (this.targetNode != null) {
			return new ContentMap(this.targetNode);
		}
		/*
		 * TODO 2 Do: - If private variable 'targetNode' is NOT 'null' then: --
		 * Transform 'targetNode' to a 'ContentMap' object and return it - If private
		 * variable 'targetNode' IS 'null' then: return 'null'
		 */
		return null;
	}
}
