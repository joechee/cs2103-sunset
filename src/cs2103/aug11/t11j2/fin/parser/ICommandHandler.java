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
				CommandResult.RenderType.String, result);
	}

	abstract public String getAbridgedHelp();
	
	abstract public String getHelp();
	
	public String autoComplete(String fullCommand, String command, String arguments, UIContext context) {
		return null;
	}
}