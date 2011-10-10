package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler to delete a task Usage: fin [task number]
 * 
 * @author Koh Zi Chun
 */
public class DeleteCommandHandler implements CommandParser.ICommandHandler {
	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("delete");
				add("del");
				add("de");
				add("d");
				add("rm");
				add("rem");
				add("remove");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {

		if (arguments.toLowerCase().equals("all")) {
			// delete all
			DeleteAllCommandHandler delallCmdHandler = new DeleteAllCommandHandler();
			return delallCmdHandler.executeCommands(delallCmdHandler.getCommandStrings().get(0), "",
					context);
		} else if (arguments.trim().length() == 0){
			HelpCommandHandler helpCmdHandler = new HelpCommandHandler();
			return helpCmdHandler.executeCommands( helpCmdHandler.getCommandStrings().get(0),
					"delete", context);
		} else {
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
			FinApplication.INSTANCE.deleteTask(todelete.getUniqId());
	
			return new CommandResult(this, arguments,
					CommandResult.RenderType.Task, todelete);
		}
	}
}
