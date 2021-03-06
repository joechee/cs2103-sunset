package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * Returns a string that shows the help file.
 * 
 * @author Koh Zi Chun
 * 
 */
public class HelpCommandHandler extends ICommandHandler {
	


	@Override
	@SuppressWarnings("serial")
	List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("?");
				add("help");
				add("h");
				add("f1");
				add("man");
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

	/**
	 * This is the default help method when the arguments to help can't be matched.
	 * 
	 * @return CommandResult containing a HELPTABLE RenderType and the List of HelpTablePair that contains help instructions for all commands.
	 * @throws FinProductionException
	 */
	private CommandResult showDefaultHelp() throws FinProductionException {
		List<HelpTablePair> helpTable = new ArrayList<HelpTablePair>(); 
		
		for (ICommandHandler i: CommandParser.INSTANCE.getCommandHandlers()) {
			if (i.getHelpTablePair() != null) {
				helpTable.add(i.getHelpTablePair());
			}
		}
		return new CommandResult(this, "",
				CommandResult.RenderType.HELPTABLE, helpTable);
	}
	


	@Override
	public String getHelp() {
		return "help\n\tBrings you to the help page. Hope that helped!";
	}
	
	@Override
	public HelpTablePair getHelpTablePair() {
		return new HelpTablePair("help", "Show help for Fin.");
	}
	
	
	
	
}
