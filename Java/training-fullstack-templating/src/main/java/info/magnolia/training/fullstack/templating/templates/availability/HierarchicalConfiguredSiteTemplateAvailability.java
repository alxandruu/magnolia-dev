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
package info.magnolia.training.fullstack.templating.templates.availability;

import info.magnolia.config.source.yaml.YamlReader;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.map2bean.Map2BeanTransformer;
import info.magnolia.module.resources.ResourceLinker;
import info.magnolia.module.site.templates.ConfiguredSiteTemplateAvailability;
import info.magnolia.objectfactory.Components;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.rendering.template.registry.TemplateDefinitionRegistry;
import info.magnolia.resourceloader.Resource;
import info.magnolia.resourceloader.ResourceChangeHandler;
import info.magnolia.resourceloader.ResourceOriginChange;
import info.magnolia.transformer.TransformationResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A custom templates availability allowing to define available templates in more ways than OTB.
 * It provides two additions to the OTB {@link ConfiguredSiteTemplateAvailability} class:
 * 1. Yaml capability for defining the 'TemplateAvailability' configuration, important for light development.
 * 2. Hierarchy based 'TemplateAvailability' possibilities.
 * The two hierarchically configuration possibilities:
 * A) Per level/depth in the website hierarchy:
 * - level 1 : allow template X & Y
 * - level 2 : allow template Y & Z
 * - level 3-10 : allow template D
 * B. Depending on the used parent page node's template:
 * - template X can be added if parent page uses template X OR is ROOT
 * - template Y can be added if parent page uses template X
 * - template D can be added if parent page uses template X OR Y
 */
public class HierarchicalConfiguredSiteTemplateAvailability extends ConfiguredSiteTemplateAvailability {

    private static final Logger log = LoggerFactory.getLogger(HierarchicalConfiguredSiteTemplateAvailability.class);

    // Constants used for Hierarchy based configurations
    public static final String PARENT_ROOT_VALUE = "ROOT";
    public static final String VALUES_SEPARATOR = ";";
    public static final String KEYS_WILDCARD = "-";
    public static final String JCR_CONFIGURED_SLASH_REPLACEMENT = "+";
    public static final String JCR_CONFIGURED_COLON_REPLACEMENT = "@";
    
    // Beans
    protected Hierarchy hierarchy;
    protected String configurationResourcePath;
    protected boolean enableByRenderType;
    protected boolean enableTemplates;
    protected boolean enableHierarchy;

    // Inner used objects
    private final ResourceLinker linker;
    private final Map2BeanTransformer map2BeanTransformer;
    private final YamlReader yamlReader;

    @Inject
    public HierarchicalConfiguredSiteTemplateAvailability(final ResourceLinker linker, Map2BeanTransformer map2BeanTransformer, YamlReader yamlReader) {
        this.linker = linker;
        this.map2BeanTransformer = map2BeanTransformer;
        this.yamlReader = yamlReader;
    }

    /**
     * Checks if the templateDefinition can be used/made available for the current page node.
     * 
     * @param node Page node to check if the 'templateDefinition' is available for it.
     * @param templateDefinition the TemplateDefinition to check if it should be available for the page node.
     * @return true if template is available.
     */
    @Override
    public boolean isAvailable(Node node, TemplateDefinition templateDefinition) {
        // Backward compatibility, calling super logic.
        // Proceed logic if 'super==false' therefore only returning on true.
        if (isEnableAll() || isEnableTemplates() || isEnableByRenderType()) {
            // Code from super -> can't use super.isAvailable() because it returns by default true...
            if ((isEnableAll() || getEnableAllWithRenderType().contains(templateDefinition.getRenderType())) && StringUtils.substringAfter(templateDefinition.getId(), ":").startsWith("pages/")) {
                return true;
            }
        }

        // Proceed custom hierarchical logic only if hierarchical based configuration is enabled.
        if (isEnableHierarchy()) {
            if (getHierarchy().isLevelsEnabled() && checkLevelCompliance(getHierarchy().getLevels(), node, templateDefinition)) {
                return true;
            }
            if (getHierarchy().isParentsEnabled() && checkParentsCompliance(getHierarchy().getParents(), node, templateDefinition)) {
                return true;
            }
        }
        return false;
    };

    /**
     * Checks the compliance of the TemplateDefinition with the 'levels' availability configuration.
     * 
     * @param levels the level based template availability configuration.
     * @param pageNode the page node where the 'templateDefinition' is checked for its compliance.
     * @param templateDefinition to check against the levels configuration.
     * @return true fulfilling the compliance.
     */
    protected boolean checkLevelCompliance(Map<String, String> levels, Node pageNode, TemplateDefinition templateDefinition) {
        String depthAsString = StringUtils.EMPTY;
        int depth = -1;
        try {
            depth = pageNode.getDepth();
            depthAsString = Integer.toString(depth);
        } catch (RepositoryException e) {
            log.error("An error occurred while trying to get the depth of the jcr node {}.", pageNode, e);
        }
        // Specific level value is the key in the map, returning its value or null
        String allowedValueForLevel = levels.get(depthAsString);

        // Checking for wild-card levels configuration as example '4-10' levels
        String wildcardKey = StringUtils.EMPTY;
        for (String key : levels.keySet()) {
            if(key.contains(KEYS_WILDCARD)){
                wildcardKey = key;
            }
        }
        if (allowedValueForLevel == null && wildcardKey.contains(KEYS_WILDCARD)) {
            String[] fromToLevels = StringUtils.split(wildcardKey, KEYS_WILDCARD);
            int fromLevel;
            int toLevel;
            try {
                fromLevel = Integer.valueOf(fromToLevels[0]);
                toLevel = Integer.valueOf(fromToLevels[1]);
            } catch (NumberFormatException e) {
                log.error("The used wildcard value {} could not be tanformed into two 'int' values. The wildcard value should be alike alike '4-10' for being transform-able into the two ints 4 and 10.", wildcardKey, e);
                return false;
            }
            if (depth >= fromLevel && depth <= toLevel) {
                allowedValueForLevel = levels.get(wildcardKey);
            }
        }
        return hasValueInTemplateTypeOrSubtypeOrId(templateDefinition, allowedValueForLevel);
    }

    /**
     * Checks the compliance of the TemplateDefinition with the 'parents' availability configuration.
     * 
     * @param parents the parents based template availability configuration
     * @param nodeToCheck the page node where the 'templateDefinition' is checked for its compliance.
     * @param templateDefinition to check against the 'parents' configuration.
     * @return true fulfilling the compliance.
     */
    protected boolean checkParentsCompliance(Map<String, String> parents, Node nodeToCheck, TemplateDefinition templateDefinition) {
        try {
            boolean isRoot = nodeToCheck.getPath().equals("/");
            for (Map.Entry<String, String> levelEntry : parents.entrySet()) {
                String possibleTemplate = levelEntry.getKey();
                String allowedParentTemplates = levelEntry.getValue();

                possibleTemplate = replaceInvalidProperyNameCharacters(possibleTemplate);

                // Checking if the templateDefinition contains the current key/template value of the availability configuration
                boolean isPossibleTemplate = hasValueInTemplateTypeOrSubtypeOrId(templateDefinition, possibleTemplate);
                if (isPossibleTemplate) {
                    // Parent is root node AND the ROOT replacement character value is defined as allowed parent.
                    if (isRoot && allowedParentTemplates.contains(PARENT_ROOT_VALUE)) {
                        return true;
                    }

                    // Parent has no template attached
                    String parentTemplateId = NodeTypes.Renderable.getTemplate(nodeToCheck);
                    if (parentTemplateId == null) {
                        return false;
                    }

                    TemplateDefinition parentTemplateDefinition = Components.getComponent(TemplateDefinitionRegistry.class).getProvider(parentTemplateId).get();
                    boolean isAllowedParentTemplate = hasValueInTemplateTypeOrSubtypeOrId(parentTemplateDefinition, allowedParentTemplates);
                    if (isAllowedParentTemplate) {
                        return true;
                    }
                }
            }
        } catch (RepositoryException e) {
            log.error("An error occurred while trying to access the parent page node of the jcr node {}.", nodeToCheck, e);
        }

        return false;
    }

    /**
     * This method replaces in a property name invalid characters of a template id.
     * Using template ids for JCR property names do not allow the columns and slash character.
     * As the template id is used for defining the key of the map following characters have to be replaced ": -> @" and "/ -> +'
     * Input example: training-templating-website@pages+default
     * Output example: training-templating-website:pages/default
     * 
     * @param templateIdPropertyName the property name containing the template id with the characters '@' and '+'.
     * @return the property name of a template with the valid and replaced characters ':' and '/'.
     */
    protected String replaceInvalidProperyNameCharacters(String templateIdPropertyName) {
        templateIdPropertyName = StringUtils.replace(templateIdPropertyName, JCR_CONFIGURED_COLON_REPLACEMENT, ":");
        templateIdPropertyName = StringUtils.replace(templateIdPropertyName, JCR_CONFIGURED_SLASH_REPLACEMENT, "/");
        return templateIdPropertyName;
    }

    /**
     * Checks if the TemplateDefinition contains the passed 'value' in either of these three possibilities:
     * 1. the template's type
     * 2. the template's subtype
     * 3. the template's id
     * 
     * @param templateDefinition the TemplateDefinition currently checked for availability.
     * @param value the value to check if the TemplateDefinition contains it.
     * @return contains the value either as template type or template subtype or the template id itself.
     */
    protected boolean hasValueInTemplateTypeOrSubtypeOrId(TemplateDefinition templateDefinition, String value) {
        if (StringUtils.isNotBlank(value) && templateDefinition.getId().contains(":pages/")) {
            String templateId = templateDefinition.getId();
            String templateType = templateDefinition.getType();
            String templateSubtype = templateDefinition.getSubtype();

            String[] values = StringUtils.split(value, VALUES_SEPARATOR);
            for (int i = 0; i < values.length; i++) {
                String valueTrimmed = values[i].trim();
                if (valueTrimmed.equals(templateType) || valueTrimmed.equals(templateSubtype) || valueTrimmed.equals(templateId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the resource of the Yaml based template availability configuration from the provided 'resourcePath'.
     * This method is called the first time when a resource path is set {@code setConfigurationResourcePath()}.
     * 
     * @param resourcePath the path of the Yaml based template availability configuration.
     */
    protected void getResouceAndPopulate(String resourcePath) {
        Resource yamlTemplateAvailabilityConfig = linker.getResource(resourcePath);
        populateFromYamlConfiguration(yamlTemplateAvailabilityConfig, yamlReader, map2BeanTransformer);

        if (StringUtils.isNoneBlank(resourcePath)) {
            yamlTemplateAvailabilityConfig.getOrigin().registerResourceChangeHandler(new ResourceChangeHandler() {

                @Override
                public void onResourceChanged(ResourceOriginChange change) {
                    if (change.getType().equals(ResourceOriginChange.Type.MODIFIED)) {
                        Resource changedResource = change.getRelatedOrigin().getByPath(configurationResourcePath);
                        populateFromYamlConfiguration(changedResource, yamlReader, map2BeanTransformer);
                    }
                }
            });
        }
    }

    /**
     * Populates the provided Yaml based configuration into this class.
     * If a JCR based configuration is used instead of a Yaml file, the population is done automatically via the Content2Bean mechanism.
     * 
     * @param yamlTemplateAvailabilityConfig The resource representing the Yaml based template availability configuration.
     * @param yamlReader Reads a Yaml and provides a map representation.
     * @param map2BeanTransformer Transforms the Yaml's map representation into a provided bean class definition.
     */
    protected void populateFromYamlConfiguration(Resource yamlTemplateAvailabilityConfig, YamlReader yamlReader, Map2BeanTransformer map2BeanTransformer) {
        if (yamlTemplateAvailabilityConfig != null) {
            try {
                Map<String, Object> readToMap = yamlReader.readToMap(yamlTemplateAvailabilityConfig);
                TransformationResult<HierarchicalConfiguredSiteTemplateAvailability> transform = map2BeanTransformer.transform(readToMap, HierarchicalConfiguredSiteTemplateAvailability.class);
                HierarchicalConfiguredSiteTemplateAvailability populatedHierarchicalBean = transform.get();

                setEnableAll(populatedHierarchicalBean.isEnableAll());
                setEnableTemplates(populatedHierarchicalBean.isEnableTemplates());
                setEnableByRenderType(populatedHierarchicalBean.isEnableByRenderType());
                setEnableHierarchy(populatedHierarchicalBean.isEnableHierarchy());
                setTemplates(populatedHierarchicalBean.getTemplates());
                if (isEnableByRenderType()) {
                    setEnableAllWithRenderType(populatedHierarchicalBean.getEnableAllWithRenderType());
                }
                setHierarchy(populatedHierarchicalBean.getHierarchy());

            } catch (IOException e) {
                log.error("The resource with path {} could not be read from the file system.", yamlTemplateAvailabilityConfig.getPath(), e);
            }
        }
    }

    /*
     * All bean and population related getters and setters.
     */
    public String getConfigurationResourcePath() {
        return configurationResourcePath;
    }

    /**
     * Is the bean setter for the Yaml based template availability configuration.
     * But also triggers the code of getting the Yaml resource file and populates it into 'this' class.
     * 
     * @param configurationResourcePath the path to the Yaml based templates availability configuration.
     */
    public void setConfigurationResourcePath(String configurationResourcePath) {
        this.configurationResourcePath = configurationResourcePath;
        getResouceAndPopulate(this.configurationResourcePath);
    }

    public boolean isEnableByRenderType() {
        return enableByRenderType;
    }

    public void setEnableByRenderType(boolean enableByRenderType) {
        this.enableByRenderType = enableByRenderType;
    }

    public boolean isEnableTemplates() {
        return enableTemplates;
    }

    public void setEnableTemplates(boolean enableTemplates) {
        this.enableTemplates = enableTemplates;
    }

    public boolean isEnableHierarchy() {
        return enableHierarchy;
    }

    public void setEnableHierarchy(boolean enableHierarchy) {
        this.enableHierarchy = enableHierarchy;
    }

    public Hierarchy getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(Hierarchy hierarchy) {
        this.hierarchy = hierarchy;
    }

    /**
     * The sub bean of the @class HierarchicalConfiguredSiteTemplateAvailability used for the 'hierarchy' node to populate.
     * The actual and complete configuration for the hierarchical templates availability.
     */
    public static class Hierarchy {
        private boolean levelsEnabled;
        private boolean parentsEnabled;

        private Map<String, String> levels = new HashMap<String, String>();
        private Map<String, String> parents = new HashMap<String, String>();

        public Map<String, String> getLevels() {
            return levels;
        }

        public void setLevels(Map<String, String> levels) {
            this.levels = levels;
        }

        public void addLevel(String name, String value) {
            this.levels.put(name, value);
        }

        public Map<String, String> getParents() {
            return parents;
        }

        public void setParents(Map<String, String> parents) {
            this.parents = parents;
        }

        public void addParent(String name, String value) {
            this.parents.put(name, value);
        }

        public boolean isLevelsEnabled() {
            return levelsEnabled;
        }

        public void setLevelsEnabled(boolean levelsEnabled) {
            this.levelsEnabled = levelsEnabled;
        }

        public boolean isParentsEnabled() {
            return parentsEnabled;
        }

        public void setParentsEnabled(boolean parentsEnabled) {
            this.parentsEnabled = parentsEnabled;
        }
    }

}
