package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

public class EndTourCommandHandler extends ICommandHandler {
	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("end");
				add("endtour");
				add("stoptour");
				add("stop");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments, UIContext context) throws FinProductionException {
		return new CommandResult(this, arguments,
				CommandResult.RenderType.TOUR, "");
	}
	
	
	@Override
	public String getHelp() {
		return "tour\n\tEnds the current tour of Fin.!";
	}
	
	@Override
	public HelpTablePair getHelpTablePair() {
		return new HelpTablePair("tour", "Ends the current tour of Fin.!");
	}

}
