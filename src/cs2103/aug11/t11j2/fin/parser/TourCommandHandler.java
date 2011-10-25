package cs2103.aug11.t11j2.fin.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.parser.JokeCommandHandler.Joke;
import cs2103.aug11.t11j2.fin.ui.UIContext;

public class TourCommandHandler extends ICommandHandler {
	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("tour");
				add("tutorial");
				add("t");
				add("tut");
				add("guide");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments, UIContext context) throws FinProductionException {
		return CommandResult.invalidTaskIndex;
	}
	
	@Override
	public String getAbridgedHelp() {
		return "";
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
}
