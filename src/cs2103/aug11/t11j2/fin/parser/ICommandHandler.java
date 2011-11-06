package cs2103.aug11.t11j2.fin.parser;

import java.util.List;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * All Command Handlers have to extend ICommandHandler. All classes that extend ICommandHandler have to implement the <code>executeCommands</code>,
 * <code>getHelp</code> and <code>getHelpTablePair</code> abstract methods.</br>
 * They may choose to override the <code>autoComplete</code> method if they want to implement autocompletion. 
 * 
 * @author Joe Chee
 */
public abstract class ICommandHandler {
	/**
	 * Returns a list of aliases associated with the command.
	 * 
	 * @return All the possible aliases for this command.
	 * @see CommandParser#installCommand(ICommandHandler commandHandler) 
	 */
	abstract List<String> getCommandStrings();
	
	
	/**
	 * <p>Returns the CommandResult with the help string associated with the command handler, together with
	 * the alias for the command.
	 * 
	 * <p>Override this method and return an empty string if the commandHandler
	 * wants to hide the help from the user.
	 * 
	 * @param none
	 * @return CommandResult with RenderType of String containing a help text String for the command.	
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
	 * Executes the current command based on the user typed command (e.g "show", "add", etc.)
	 * and arguments and on the current UI context.
	 * 
	 * @param command - the given command
	 * @param arguments - the given arguments
	 * @param context the current UIContext of the user interface
	 * @return CommandResult after evaluation of command
	 * @throws FinProductionException
	 */
	abstract CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException;	
	
	/**
	 * Returns a String that will be displayed when the user types help [command].
	 * 
	 * @return The help string associated with the command.
	 */
	abstract public String getHelp();
	
	/**
	 * Returns a pair that describes what the command does -- the usage and the description.
	 * 
	 * @return The pair of [usage, description] associated with the command.
	 */
	abstract public HelpTablePair getHelpTablePair();
	
	/**
	 * Returns a hint of the autocomplete string that should have the fullCommand
	 * string as its prefix.
	 * 
	 * @param fullCommand - the full string that the user typed in
	 * @param command - the command string
	 * @param arguments - the arguments string
	 * @param context - the current UIContext of the user interface
	 * @return The autocomplete hint associated with fullCommand.
	 */
	public String autoComplete(String fullCommand, String command, String arguments, UIContext context) {
		return null;
	}
}