package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;

/**
 * @author Alex Liew
 */
import java.util.List;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * Command handler to handle command that initiates the Fin. guided tour.
 * 
 * @author Alex Liew
 *
 */
public class TourCommandHandler extends ICommandHandler {
	@Override
	@SuppressWarnings("serial")
	List<String> getCommandStrings() {
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
	public String getHelp() {
		return "tour\n\tStarts a tour of Fin.!";
	}
	
	@Override
	public HelpTablePair getHelpTablePair() {
		return new HelpTablePair("tour", "Starts a tour of Fin.!");
	}
}
