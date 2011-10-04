package cs2103.aug11.t11j2.fin.parser;

import java.io.IOException;
import java.util.List;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;

public interface ICommandHandler {
	
	public List<String> getCommandStrings();

	CommandResult executeCommands(String arguments) throws IOException,
			FinProductionException ;
}
