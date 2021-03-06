package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;
/**
 * Command handler to end the Fin. guided tour.
 *  
 * @author Alex Liew
 *
 */
public class EndTourCommandHandler extends ICommandHandler {
	@Override
	@SuppressWarnings("serial")
	List<String> getCommandStrings() {
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
		return "endtour\n\tEnds the current tour of Fin.!";
	}
	
	@Override
	public HelpTablePair getHelpTablePair() {
		return new HelpTablePair("endtour", "Ends the current tour of Fin.!");
	}

}
