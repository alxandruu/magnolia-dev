package info.magnolia.training.fullstack.templating.advanced.templates.components;

import javax.inject.Inject;

import info.magnolia.rendering.template.TemplateAvailability;
import info.magnolia.rendering.template.configured.ConfiguredTemplateDefinition;

/**
 * Definition class for Navigation component.
 */

public class AutoLinkListDefinition extends ConfiguredTemplateDefinition {
	private String workspace;
	private String nodeType;
	/*
	 * TODO 1 a) Do: - Create two private variables of type String: 'workspace' and
	 * 'nodeType'
	 */

	@Inject
	public AutoLinkListDefinition(TemplateAvailability templateAvailability) {
		super(templateAvailability);
	}

	/**
	 * Requires default ctor for proxies!
	 */
	public AutoLinkListDefinition() {
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	/*
	 * TODO 1 b) Do: - Create settes and getters for the two variables: Right click
	 * -> Source -> Generate getters and setters
	 */
	

}