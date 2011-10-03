package cs2103.aug11.t11j2.fin.parser;

import java.util.List;

public interface ICommandHandler {
	
	public List<String> getCommandStrings();
	CommandResult executeCommands(String arguments) ;
}
