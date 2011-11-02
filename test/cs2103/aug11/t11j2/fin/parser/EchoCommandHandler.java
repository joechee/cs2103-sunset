package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

public class EchoCommandHandler extends ICommandHandler {
	List<String> getCommandStrings() {
		return new ArrayList<String>() { { // aliases
				add("echo");
				add("repeat");
				add("e");
			} };
	}
	
	CommandResult executeCommands(String command, String arguments, UIContext context) 
			throws FinProductionException {
		return new CommandResult(this, arguments,
				CommandResult.RenderType.STRING, arguments);
	}

	public String getHelp() {
		return "This command echos whatever you pass to it!";
	}
	
	public HelpTablePair getHelpTablePair() {
		return new HelpTablePair("echo <string>", 
				"Echos the string");
	}

}
