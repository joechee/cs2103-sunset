package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * Command to exit Fin Application
 * 
 * @author Joe Chee
 *
 */
public class ExitCommandHandler extends ICommandHandler {
	
	/**
	 * Used in conjunction with <code>CommandParser</code>. 
	 * 
	 * @return List<String>
	 * @see CommandParser#installCommand(ICommandHandler commandHandler)
	 */

	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("exit");
				add("quit");
				add("q");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {
		return CommandResult.exitCommandResult;
	}

	@Override
	public String getAbridgedHelp() {
		return "exit\t\t\t\t\tSaves your task list and terminates Fin.";
	}
	
	@Override
	public String getHelp() {
		return "";
	}

	@Override
	public CommandResult showHelp() throws FinProductionException {
		// TODO Auto-generated method stub
		return new CommandResult(this, "",
				CommandResult.RenderType.String, "");
	}
	

}
