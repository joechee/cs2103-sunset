package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * Command handler that handles the command to initiate self-diagnostic tests.
 * 
 * @author Joe Chee
 *
 */
public class TestCommandHandler extends ICommandHandler {
	@Override
	@SuppressWarnings("serial")
	List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("test");
				add("starttest");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments, UIContext context) throws FinProductionException {
		return new CommandResult(this, arguments,
				CommandResult.RenderType.TEST, "");
	}
	

	
	@Override
	public String getHelp() {
		return "";
	}
	
	@Override
	public CommandResult showHelp() throws FinProductionException {
		return new CommandResult(this, "",
				CommandResult.RenderType.STRING, "");
	}

	@Override
	public HelpTablePair getHelpTablePair() {
		return null;
	}
}
