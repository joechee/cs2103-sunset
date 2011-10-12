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
					CommandResult.RenderType.String, "add <task>\n\tAdds <task> to your task list");			
		} else if(arguments.equals("show")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "show <filters>\n\tShows the task list with <filters>. Examples of <filters> can be \"tasks due on Friday\"");						
		} else if(arguments.equals("search")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "search <patterns>\n\tSearch the tasks containing the (all) the <patterns>");
		} else if(arguments.equals("delete")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "delete <task number>\n\tDeletes a task from your tasklist");
		} else if(arguments.equals("tag")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "tag <task number> <tag>\n\tTags a <task> with <tag>");
		} else if(arguments.equals("untag")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "untag <task number> <tag>\n\tUntags a <task> with <tag>");
		} else if(arguments.equals("fin")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "fin <task number>\n\tMarks a task as completed");
		} else if(arguments.equals("unfin")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "unfin <task number>\n\tUnmarks the completed task");
		} else if(arguments.equals("help")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "Are you joking?");
		} else if(arguments.equals("important")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "important <task>\n\tMarks a task as important");
		} else if(arguments.equals("unimportant")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "unimportant <task>\n\tUnmarks an important task");
		} else if(arguments.equals("edit")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "edit <task number> <command>\n\tEdits a task base on the following command\n" +
							"\t - edit <task> to <newtask>\tedits the task to new task\n" +
							"\t - edit <task> at <newtag>\tadds tag to tag\n" +
							"\t - edit <task> rt <newtag>\tremove tag from task\n" +
							"\t - edit <task> due <duedate>\tchange/add due date for a task\n" +
							"\t - edit <task> rd\t\tremove due date from a task");
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
