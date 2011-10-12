package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler to mark a task as uncompleted (unFin :() Usage: unfin [task number]
 * 
 * @author Koh Zi Chun
 */
public class UnfinCommandHandler implements CommandParser.ICommandHandler {
	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("unfin");
				add("uf");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {
		if (arguments.isEmpty()) {
			return showHelp(context);
		}
		int taskIndex;
		try {
			taskIndex = Integer.parseInt(arguments.split("\\s")[0]);
		} catch (NumberFormatException nfe) {
			return CommandResult.invalidTaskIndex;
		}

		if (taskIndex < 0 || taskIndex > context.getTaskList().size()) {
			return CommandResult.invalidTaskIndex;
		}
		Task todelete = context.getTaskList().get(taskIndex - 1);

		FinApplication.INSTANCE.unfinTask(todelete.getUniqId());

		return new CommandResult(this, arguments,
				CommandResult.RenderType.Task, todelete);
	}
	CommandResult showHelp(UIContext context) throws FinProductionException {
		HelpCommandHandler helpCmdHandler = new HelpCommandHandler();
		return helpCmdHandler.executeCommands( helpCmdHandler.getCommandStrings().get(0),
				"unfin", context);
	}
}
