package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.parser.CommandParser.ICommandHandler;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * Command to delete all shown task at the moment
 * 
 * @author Koh Zi Chun
 *
 */
public class DeleteAllCommandHandler implements ICommandHandler {

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
		
		for (Task t : context.getTaskList()) {
			FinApplication.INSTANCE.deleteTask(t.getUniqId());			
		}

		// after a delete all, invoke a show
		ShowCommandHandler showCmdHandler = new ShowCommandHandler();
		return showCmdHandler.executeCommands(showCmdHandler.getCommandStrings()
				.get(0), "", context);
	}
	
	CommandResult showHelp(UIContext context) throws FinProductionException {
		HelpCommandHandler helpCmdHandler = new HelpCommandHandler();
		return helpCmdHandler.executeCommands( helpCmdHandler.getCommandStrings().get(0),
				"deleteall", context);
	}

	@Override
	public String showAbridgedHelp() {
		return "";
	}

	@Override
	public CommandResult showHelp() throws FinProductionException {
		// TODO Auto-generated method stub
		return new CommandResult(this, "",
				CommandResult.RenderType.String, "");
	}

}
