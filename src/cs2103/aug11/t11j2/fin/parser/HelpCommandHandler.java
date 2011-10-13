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
public class HelpCommandHandler implements ICommandHandler {

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
			ICommandHandler commandHandler = findCommandHandlers(arguments);
			if (commandHandler != null) {
				return commandHandler.showHelp();
			} else {
				return showDefaultHelp();
			}
		} 
	}
	
	/**
	 * Returns the first Command Handler that can be invoked using the arguments.
	 * @param arguments
	 * @return
	 */
	
	private ICommandHandler findCommandHandlers(String arguments) {
		
		for (ICommandHandler i: CommandParser.INSTANCE.getCommandHandlers()) {
			for (String j: i.getCommandStrings()) {
				if (j.equals(arguments)) {
					return i;
				}
			}
		}
		return null;
	}

	private CommandResult showDefaultHelp() throws FinProductionException {
		String help = "Fin. is a ToDo manager that will allow you to keep track of your daily tasks the way you want it to be tracked.\n\n";
		for (ICommandHandler i: CommandParser.INSTANCE.getCommandHandlers()) {
			if (!i.showAbridgedHelp().isEmpty()) {
				help = help+i.showAbridgedHelp()+"\n";
			}
		}
		help = help + "\nFor more detailed help on a specific command, type \"help <command>\"";
			
		return new CommandResult(this, "",
				CommandResult.RenderType.String, help);
	}
	
	@Override
	public String showAbridgedHelp() {
		return "help\t\t\t\t\tBrings you to this page";
	}

	@Override
	public CommandResult showHelp() throws FinProductionException {
		// TODO Auto-generated method stub
		return new CommandResult(this, "",
				CommandResult.RenderType.String, "help\n\tBrings you to this page. Hope that helped!");
	}
	
	
	
	
}
