package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.parseTask.TaskParser;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler for "edit" command Usage: edit [tasknumber] [task] Edits a
 * given task to a new task
 * 
 * @author Koh Zi Chun
 * 
 */
public class EditCommandHandler implements ICommandHandler {

	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("e");
				add("ed");
				add("edi");
				add("edit");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {

		int taskIndex;
		String[] tokens = arguments.split("\\s");
		
		try {
			taskIndex = Integer.parseInt(tokens[0]);
		} catch(NumberFormatException nfe) {
			return CommandResult.invalidTaskIndex;
		}
		
		if (taskIndex < 0 || taskIndex > context.getTaskList().size()) {
			return CommandResult.invalidTaskIndex;
		}

		Task todelete = context.getTaskList().get(taskIndex);
		FinApplication.INSTANCE.deleteTask( todelete.getUniqId() );

		String newTaskString = arguments.replaceFirst(tokens[0], "").trim();
		
		AddCommandHandler addCmdHandler = new AddCommandHandler();
		return addCmdHandler.executeCommands(addCmdHandler.getCommandStrings().get(0), 
				newTaskString, context);
	}
}
