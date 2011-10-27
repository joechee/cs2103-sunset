package cs2103.aug11.t11j2.fin.parser;

import java.util.List;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

public abstract class ICommandHandler {
	public abstract List<String> getCommandStrings();

	abstract CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException;
	
	
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

	
	
	abstract public String getHelp();
	
	/**
	 * Returns a pair that describes what the command does -- the usage and the description.
	 * 
	 * @return HelpTablePair
	 */
	
	abstract public HelpTablePair getHelpTablePair();
	
	public String autoComplete(String fullCommand, String command, String arguments, UIContext context) {
		return null;
	}
}