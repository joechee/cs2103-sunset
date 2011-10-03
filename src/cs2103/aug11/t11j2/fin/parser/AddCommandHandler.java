package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;

public class AddCommandHandler implements ICommandHandler {
	
	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>(){{
			add( "a" );
			add( "ad" );
			add( "add" );
		}};
	}

	@Override
	public CommandResult executeCommands(String arguments) {		
		return null;
	}
}

