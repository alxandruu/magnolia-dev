package info.magnolia.training.fullstack.admincentral.ux.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
 
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
 
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import info.magnolia.commands.CommandsManager;
import info.magnolia.context.Context;
import info.magnolia.ui.ValueContext;
import info.magnolia.ui.api.app.AppContext;
import info.magnolia.ui.api.message.Message;
import info.magnolia.ui.api.message.MessageType;
import info.magnolia.ui.contentapp.action.MarkAsDeletedCommandAction;
import info.magnolia.ui.contentapp.async.AsyncActionExecutor;
import info.magnolia.ui.datasource.jcr.JcrDatasource;

public class MarkAsDeletedAndInformAction extends MarkAsDeletedCommandAction {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final ArrayList<String> markedNodesPaths;

	private final AppContext appContext;
	private MarkAsDeletedAndInformActionDefinition definition;

	@Inject
	public MarkAsDeletedAndInformAction(MarkAsDeletedAndInformActionDefinition definition,
			CommandsManager commandsManager, ValueContext<Node> valueContext, Context context,
			AsyncActionExecutor asyncActionExecutor, JcrDatasource datasource, AppContext appContext) {
		super(definition, commandsManager, valueContext, context, asyncActionExecutor, datasource);
		this.appContext = appContext;
		this.definition = definition;
		this.markedNodesPaths = resolveMarkedNodesPaths(valueContext);
	}

	@Override
	public void execute() {
		super.execute();
		sendMessage();
	}

	protected ArrayList<String> resolveMarkedNodesPaths(ValueContext<Node> valueContext) {
		List<Node> collect = valueContext.get().collect(Collectors.toList());
		Iterator<Node> iterator = collect.iterator();
		ArrayList<String> nodePaths = new ArrayList<String>();
		while (iterator.hasNext()) {
	          try {
			Node markedNode = iterator.next();
			nodePaths.add(markedNode.getPath());
			
	          } catch (RepositoryException e) {
	              log.error("No path detected of jcr item.", e);
	          }
		}
		return nodePaths;
	}

	protected void sendMessage() {
		System.out.println("Action 'MarkAsDeletedAndInformAction' was clicked. Here the message should be sent.");

		String messageStringFromNodesPaths = messageStringFromNodesPaths(markedNodesPaths);
		System.out.println("The current paths detected for the message are:" + messageStringFromNodesPaths);

		final Message message = new Message();
		message.setType(MessageType.INFO);
		message.setSubject("Page(s) Marked for Deletion.");
		message.setMessage("These page(s) " + messageStringFromNodesPaths + " were marked for deletion.");
		String defaultGroup = definition.getDefaultGroup();
		String defaultUser = definition.getDefaultUser();
		if (!StringUtils.isEmpty(defaultGroup)) {
			appContext.sendGroupMessage(defaultGroup, message);
			
		} else {
			appContext.sendUserMessage(defaultUser, message);
			
			
		}
	}

	protected static String messageStringFromNodesPaths(ArrayList<String> nodePaths) {
		String messageString = "[Error, no pages were marked!]";
		Iterator<String> iterator = nodePaths.listIterator();

		if (iterator.hasNext()) {
			messageString = "[";
		}

		while (iterator.hasNext()) {
			messageString += iterator.next();

			if (iterator.hasNext()) {
				messageString += ", ";
			} else {
				messageString += "]";
			}
		}
		return messageString;
	}
}
