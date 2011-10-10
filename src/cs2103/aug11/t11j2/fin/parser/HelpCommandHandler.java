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
		if (arguments.equals("edit")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "I need help for edit!");			
		} else if(arguments.equals("show")) {
			return new CommandResult(this, "",
					CommandResult.RenderType.String, "I need help for show!");						
		}
		
		return showDefaultHelp();			
		
	}

	private CommandResult showDefaultHelp() throws FinProductionException {
		final String help = "Fin. is a ToDo manager that will allow you to keep track of your daily tasks the way you want it to be tracked.\n\n" +
				"add <task>\t\tAdds <task> to your task list\n" +
				"show <filters>\t\tShows the task list with <filters>. Examples of <filters> can be \"tasks due on Friday\"\n" +
				"search <patterns>\tSearch the tasks containing the (all) the <patterns>\n" +
				"delete <task>\t\tDeletes a task from your tasklist\n" +
				"tag <task> <tag>\tTags a <task> with <tag>\n" +
				"untag <task> <tag>\tUntags a <task> with <tag>\n" +
				"fin <task>\tMarks a task as completed\n" +
				"unfin <task>\t";
		return new CommandResult(this, "",
				CommandResult.RenderType.String, help);
	}
	
	
}
