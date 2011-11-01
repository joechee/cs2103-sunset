package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * Command to delete all shown task at the moment
 * 
 * @author Koh Zi Chun
 *
 */
public class DeleteAllCommandHandler extends ICommandHandler {

	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("delall");
				add("deleteall");
				add("da");
				add("dall");
				add("dela");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {
		Collection<UUID> toDeleteUUID = new ArrayList<UUID>();
		for (Task t : context.getTaskList()) {
			toDeleteUUID.add(t.getUniqId());		
		}
		context.getFinApplication().deleteTasks(toDeleteUUID);	
		// after a delete all, invoke a show
		ShowCommandHandler showCmdHandler = new ShowCommandHandler();
		return new CommandResult(this, arguments, CommandResult.RenderType.NULL, null);
	}
	


	@Override
	public CommandResult showHelp() throws FinProductionException {
		return new CommandResult(this, "",
				CommandResult.RenderType.STRING, "");
	}

	@Override
	public String getHelp() {
		return "";
	}
	
	public HelpTablePair getHelpTablePair() {
		return null;
	}

}
