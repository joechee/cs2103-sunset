package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler to delete a task Usage: fin [task number]
 * 
 * @author Koh Zi Chun
 */
public class DeleteCommandHandler extends ICommandHandler {
	@Override
	@SuppressWarnings("serial")
	List<String> getCommandStrings() {
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
		
		if (arguments.isEmpty()) {
			return showHelp();
		}

		if (arguments.toLowerCase().equals("all")) {
			// delete all
			DeleteAllCommandHandler delallCmdHandler = new DeleteAllCommandHandler();
			return delallCmdHandler.executeCommands(delallCmdHandler.getCommandStrings().get(0), "",
					context);
		} else {
			int taskIndex;
			try {
				taskIndex = Integer.parseInt(arguments.split("\\s")[0]);
			} catch (NumberFormatException nfe) {
				return new CommandResult(this, arguments, CommandResult.RenderType.ERROR, "Invalid Task Index!");
			}
	
			if (taskIndex <= 0 || taskIndex > context.getTaskList().size()) {
				return new CommandResult(this, arguments, CommandResult.RenderType.ERROR, "Invalid Task Index!");
			}
	
			Task todelete = context.getTaskList().get(taskIndex - 1);
			context.getFinApplication().deleteTask(todelete.getUniqId());
			return new CommandResult(this, arguments,
					CommandResult.RenderType.TASK, todelete);
		}
	}
	



	
	@Override
	public String getHelp() {
		return "delete <task number>\n\tDeletes a task from your task list";
	}
	
	@Override
	public HelpTablePair getHelpTablePair() {
		return new HelpTablePair("delete <task number>", "Deletes <task> from your task list");
	}
	
	


}
