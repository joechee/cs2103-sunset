package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * Commandhandler for the "exit" command.</br>
 * 
 * Usage: exit</br>
 * Returns an instance of CommandResult.exitCommandResult when executed.
 * 
 * @author Joe Chee
 *
 */
public class ExitCommandHandler extends ICommandHandler {

	/* 
	 * 
	 * @see cs2103.aug11.t11j2.fin.parser.ICommandHandler#getCommandStrings()
	 */
	@Override
	@SuppressWarnings("serial")
	List<String> getCommandStrings() {
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
	public String getHelp() {
		return "";
	}
	
	@Override
	public HelpTablePair getHelpTablePair() {
		return null;
	}

	@Override
	public CommandResult showHelp() throws FinProductionException {
		return new CommandResult(this, "",
				CommandResult.RenderType.STRING, "");
	}
	

}
