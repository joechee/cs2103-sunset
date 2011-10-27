package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

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
public class EditCommandHandler extends ICommandHandler {

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
		
		if (arguments.isEmpty()) {
			return showHelp();
		}

		int taskIndex;
		String[] tokens = arguments.split("\\s+",3);
		
		try {
			taskIndex = Integer.parseInt(tokens[0]);
		} catch (NumberFormatException nfe) {
			return new CommandResult(this, arguments, CommandResult.RenderType.ERROR, "Invalid Task Index!");
		}

		if (taskIndex < 0 || taskIndex > context.getTaskList().size()) {
			return new CommandResult(this, arguments, CommandResult.RenderType.ERROR, "Invalid Task Index!");
		}
		
		Task task = context.getTaskList().get(taskIndex - 1);
		
		return executeEditMethods(tokens, task, context);
	}
	
	CommandResult executeEditMethods(String[] tokens, Task task, UIContext context) throws FinProductionException  {
		tokens[1] = tokens[1].trim();
		
		if (tokens[1].equals("to")) {
			// replace current task with new task
			
			if (tokens.length == 2) return showHelp();
			
			context.getFinApplication().deleteTask(task.getUniqId());
			AddCommandHandler addCmdHandler = new AddCommandHandler();
			CommandResult res = addCmdHandler.executeCommands(addCmdHandler.getCommandStrings()
																.get(0), tokens[2], context);
			
			res.setCommand(this);
			return res;
			
		} else if (tokens[1].equals("at")) {
			// add tag to current task
			
			if (tokens.length == 2) return showHelp();

			task.addTag(tokens[2]);
			return new CommandResult(this, tokens[0] + " " + tokens[1] + " " + tokens[2],
					CommandResult.RenderType.TASK, task);
			
		} else if (tokens[1].equals("rt")) {
			// remove tag from task
			
			if (tokens.length == 2) return showHelp();

			task.removeTag(tokens[2]);
			return new CommandResult(this, tokens[0] + " " + tokens[1] + " " + tokens[2],
					CommandResult.RenderType.TASK, task);
			
		} else if (tokens[1].equals("due")) {
			// set new due date
			
			if (tokens.length == 2) return showHelp();

			task.setDueDate(tokens[1] + " " + tokens[2]);
			return new CommandResult(this, tokens[0] + " " + tokens[1] + " " + tokens[2],
					CommandResult.RenderType.TASK, task);			
		} else if (tokens[1].equals("undue")) {
			// remove due date
			
			task.removeDueDate();
			return new CommandResult(this, tokens[0] + " " + tokens[1],
					CommandResult.RenderType.TASK, task);			
		} else {
			return showHelp();
		}
	}
	


	
	@Override
	public String getHelp() {
		return "edit <task number> <command>\n\tEdits a task based on the following command\n" +
		"\t - edit <task> to <newtask>\tedits the task to new task\n" +
		"\t - edit <task> due <duedate>\tchange/add due date for a task\n" +
		"\t - edit <task> undue\t\tremove due date from a task";
	}
	
	@Override
	public HelpTablePair getHelpTablePair() {
		return new HelpTablePair("edit <task number> to <new task>", "Edits a task based on the following command");
	}
	
	
	private static final String[] editCommands = {"to", "at", "rt", "due", "undue"};
	@Override
	public String autoComplete(String fullCommand, String command, String arguments, UIContext context) {
		int taskIndex;
		String[] tokens = arguments.split("\\s+",3);
//		System.out.println(arguments);
		
		if (tokens.length >= 3) {
			return null;
		}

		try {
			taskIndex = Integer.parseInt(tokens[0]);
		} catch (NumberFormatException nfe) {
			return null;
		}

		if (taskIndex < 0 || taskIndex > context.getTaskList().size()) {
			return null;
		}
		Task task = context.getTaskList().get(taskIndex - 1);

		// only a valid task Index provided
		// auto complete "edit [] to [task]"
		if (tokens.length == 1) {
			return fullCommand.trim().replaceAll("\\s+$", "") + " to " + task.getEditableTaskName();
		}
		
		
		if (tokens[1].equals("to")) {
			// edit [] to provided
			// complete with task
			return fullCommand.trim().replaceAll("\\s+$", "") + " " + task.getEditableTaskName();
		} else {
			// complete with whatever task
			for (int i=0;i<editCommands.length;++i) {
				if (editCommands[i].startsWith(tokens[1]) && editCommands[i].length() != tokens[1].length()) {
					return fullCommand.trim().replaceAll("\\s+$", "").replaceAll(tokens[1]+"$", "") + editCommands[i];					
				}
			}
			
		}
		return null;
	}


}
