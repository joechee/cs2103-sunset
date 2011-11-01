package cs2103.aug11.t11j2.fin.parser;

import java.util.List;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * ICommandHandler abstract class that all Command Handlers have to extend.
 * The commandhandlers have to implement the <code>executeCommands</code>,
 * <code>getHelp</code> and <code>getHelpTablePair</code> abstract methods
 * and may choose to override the <code>autoComplete</code> method if
 * they want to implement autoCompletion. 
 * 
 * @author Joe Chee
 */
public abstract class ICommandHandler {
	/**
	 * @return a list of String representing all the possible commands aliases for this command 
	 */
	abstract List<String> getCommandStrings();
	
	
	/**
	 * returns the help string that will be displayed when the user
	 * types help [command]. it appends it with the aliases for the current 
	 * command.
	 * 
	 * Override this method and return an empty string if the commandHandler
	 * wants to hide the help from the user. 
	 * 
	 * @return full help string
	 * @throws FinProductionException
	 */
	CommandResult showHelp() throws FinProductionException {
		String result = getHelp();
		result = result + "\nAliases: ";
		for (String i: getCommandStrings()) {
			result = result + i + ", ";
		}
		result=result.substring(0,result.length()-2);
		return new CommandResult(this, "",
				CommandResult.RenderType.STRING, result);
	}
	
	/**
	 * Executes the current command. the user typed command (e.g show or sh)
	 * and arguments (everything after the command) are provided.  
	 * 
	 * @param command
	 * @param arguments
	 * @param current UIContext of the User Interface
	 * @return CommandResult after evaluation of command
	 * @throws FinProductionException
	 */
	abstract CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException;	
	
	/**
	 * Returns a string that will be displayed when the user types help [command].
	 * 
	 * @return help string
	 */
	abstract public String getHelp();
	
	/**
	 * Returns a pair that describes what the command does -- the usage and the description.
	 * 
	 * @return HelpTablePair
	 */
	abstract public HelpTablePair getHelpTablePair();
	
	/**
	 * Returns a hint of the autocomplete string that should have the fullCommand
	 * string as it's prefix
	 * 
	 * @param fullCommand the full string that the user typed in
	 * @param command the command string
	 * @param arguments the arguments string
	 * @param context the current UIContext of the User Interface
	 * @return autocomplete hint
	 */
	public String autoComplete(String fullCommand, String command, String arguments, UIContext context) {
		return null;
	}
}