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
				add("tut");
				add("guide");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments, UIContext context) throws FinProductionException {
		return new CommandResult(this, arguments,
				CommandResult.RenderType.TOUR, "");
	}
	
	@Override
	public String getAbridgedHelp() {
		return "Starts a tour of Fin.!";
	}
	
	@Override
	public String getHelp() {
		return "Starts a tour of Fin.!";
	}

	@Override
	public CommandResult showHelp() throws FinProductionException {
		return new CommandResult(this, "",
				CommandResult.RenderType.STRING, "");
	}
}
