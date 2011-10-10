package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * Returns a string that shows the help file
 * 
 * @author Koh Zi Chun
 * 
 */
public class HelpCommandHandler implements CommandParser.ICommandHandler {

	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("?");
				add("help");
				add("h");
				add("f1");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {

		arguments = arguments.trim().toLowerCase();
		
		if (arguments.length() == 0) {
			return showDefaultHelp();
		} else {
			return showHelpFor(arguments);
		}
	}

	private CommandResult showHelpFor(String arguments) throws FinProductionException {
		if (arguments.equals("add")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "add <task>\t\t\tAdds <task> to your task list\n");			
		} else if(arguments.equals("show")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "show <filters>\t\t\tShows the task list with <filters>. Examples of <filters> can be \"tasks due on Friday\"\n");						
		} else if(arguments.equals("search")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "search <patterns>\t\tSearch the tasks containing the (all) the <patterns>\n");
		} else if(arguments.equals("delete")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "delete <task number>\t\tDeletes a task from your tasklist\n");
		} else if(arguments.equals("tag")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "tag <task number> <tag>\t\tTags a <task> with <tag>\n");
		} else if(arguments.equals("untag")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "untag <task number> <tag>\tUntags a <task> with <tag>\n");
		} else if(arguments.equals("fin")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "fin <task number>\t\tMarks a task as completed\n");
		} else if(arguments.equals("unfin")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "unfin <task number>\t\tUnmarks the completed task\n");
		} else if(arguments.equals("help")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "Are you joking?");
		} else if(arguments.equals("important")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "important\t\t\t\tMarks a task as important\n");
		} else if(arguments.equals("unimportant")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "unimportant\t\t\t\tUnmarks an important task\n");
		} else if(arguments.equals("edit")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "edit <task number> to <new task> \t\tChanges a task to the new task");
		} 
		
		return showDefaultHelp();			
		
	}

	private CommandResult showDefaultHelp() throws FinProductionException {
		final String help = 
			"Fin. is a ToDo manager that will allow you to keep track of your daily tasks the way you want it to be tracked.\n\n" +
				"add <task>\t\t\t\tAdds <task> to your task list\n" +
				"show <filters>\t\t\t\tShows the task list with <filters>. Examples of <filters> can be \"tasks due on Friday\"\n" +
				"search <patterns>\t\t\tSearch the tasks containing the (all) the <patterns>\n" +
				"delete <task number>\t\t\tDeletes a task from your tasklist\n" +
				"tag <task number> <tag>\t\t\tTags a <task> with <tag>\n" +
				"untag <task number> <tag>\t\tUntags a <task> with <tag>\n" +
				"fin <task number>\t\t\tMarks a task as completed\n" +
				"unfin <task number>\t\t\tUnmarks the completed task\n" +
				"help\t\t\t\t\tBrings you to this page\n"+
				"important\t\t\t\tMarks a task as important\n" +
				"unimportant\t\t\t\tUnmarks an important task\n" +
				"edit <task number> to <new task> \tChanges a task to the new task";
		return new CommandResult(this, "",
				CommandResult.RenderType.String, help);
	}
	
	
}
