package info.magnolia.training.fullstack.admincentral.ux.form;
 
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.ui.api.context.UiContext;
import info.magnolia.ui.api.i18n.I18NAuthoringSupport;
import info.magnolia.ui.form.field.definition.SelectFieldOptionDefinition;
import info.magnolia.ui.form.field.factory.SelectFieldFactory;
 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
 
import javax.inject.Inject;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
 
import org.apache.jackrabbit.commons.predicate.NodeTypePredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import com.vaadin.v7.data.Item;
 
/**
 * FieldFactory class that provides a list of all contacts to choose from.
 *
 */
public class ContactSelectFieldFactory extends SelectFieldFactory<ContactSelectFieldDefinition> {
    private static final Logger log = LoggerFactory.getLogger(ContactSelectFieldFactory.class);
 
    @Inject
    public ContactSelectFieldFactory(ContactSelectFieldDefinition definition, Item relatedFieldItem, UiContext uiContext, I18NAuthoringSupport i18nAuthoringSupport) {
        super(definition, relatedFieldItem, uiContext, i18nAuthoringSupport);
    }
 
    @Override
    public List<SelectFieldOptionDefinition> getOptions() {
        List<SelectFieldOptionDefinition> selectOptions = new ArrayList<SelectFieldOptionDefinition>();
        SelectFieldOptionDefinition optionA = new SelectFieldOptionDefinition();
        optionA.setValue("optionA");
        optionA.setLabel("Option A");
        selectOptions.add(optionA);
        SelectFieldOptionDefinition optionB = new SelectFieldOptionDefinition();
        optionB.setValue("optionB");
        optionB.setLabel("Option B");
        selectOptions.add(optionB);
        return selectOptions;
    }
 
    @Override
    protected Class<?> getDefaultFieldType() {
        return String.class;
    }
 
}