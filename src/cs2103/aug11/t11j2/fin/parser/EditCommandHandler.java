package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler for "edit" command Usage: edit [tasknumber] [task] Edits a
 * given task to a new task
 * 
 * @author Koh Zi Chun
 * 
 */
public class EditCommandHandler implements CommandParser.ICommandHandler {

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
		String[] tokens = arguments.split("\\s",3);
		
		if (tokens.length < 2) {
			return showEditHelp(context);
		}

		try {
			taskIndex = Integer.parseInt(tokens[0]);
		} catch (NumberFormatException nfe) {
			return CommandResult.invalidTaskIndex;
		}

		if (taskIndex < 0 || taskIndex > context.getTaskList().size()) {
			return CommandResult.invalidTaskIndex;
		}
		
		Task task = context.getTaskList().get(taskIndex - 1);
		
		return executeEditMethods(tokens, task, context);
	}
	
	CommandResult executeEditMethods(String[] tokens, Task task, UIContext context) throws FinProductionException  {
		tokens[1] = tokens[1].trim();
		
		if (tokens[1].equals("to")) {
			// replace current task with new task
			FinApplication.INSTANCE.deleteTask(task.getUniqId());

			AddCommandHandler addCmdHandler = new AddCommandHandler();
			return addCmdHandler.executeCommands(addCmdHandler.getCommandStrings()
					.get(0), tokens[2], context);
			
		} else if (tokens[1].equals("at")) {
			// add tag to current task
			task.addTag(tokens[2]);

			return new CommandResult(this, tokens[0] + " " + tokens[1] + " " + tokens[2],
					CommandResult.RenderType.Task, task);
		} else if (tokens[1].equals("rt")) {
			// remove tag from task
			task.removeTag(tokens[2]);			
			return new CommandResult(this, tokens[0] + " " + tokens[1] + " " + tokens[2],
					CommandResult.RenderType.Task, task);
		} else {
			return showEditHelp(context);
		}
	}
	
	CommandResult showEditHelp(UIContext context) throws FinProductionException {
		HelpCommandHandler helpCmdHandler = new HelpCommandHandler();
		return helpCmdHandler.executeCommands( helpCmdHandler.getCommandStrings().get(0),
				"edit", context);
	}
}
