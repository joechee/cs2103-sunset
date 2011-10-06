package cs2103.aug11.t11j2.fin.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler to delete a task
 * Usage: fin [task number]
 * 
 * @author Koh Zi Chun
 */
public class DeleteCommandHandler implements ICommandHandler {
	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
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

		int taskIndex;
		try {
			taskIndex = Integer.parseInt(arguments.split("\\s")[0]);
		} catch(NumberFormatException nfe) {
			return CommandResult.invalidTaskIndex;
		}
		
		if (taskIndex < 0 || taskIndex > context.getTaskList().size()) {
			return CommandResult.invalidTaskIndex;
		}
		
		Task todelete = context.getTaskList().get(taskIndex);
		FinApplication.INSTANCE.deleteTask( todelete.getUniqId() );
		
		return new CommandResult(this, arguments,
				CommandResult.RenderType.Task, todelete);
	}
}
