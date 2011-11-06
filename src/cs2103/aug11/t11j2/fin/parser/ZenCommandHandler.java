package cs2103.aug11.t11j2.fin.parser;

/**
 * @author Joe Chee
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

public class ZenCommandHandler extends ICommandHandler {
	List<String> zenList;
	ZenCommandHandler() {
		zenList = new ArrayList<String>();
		Random RNG = new Random();
		String[] noun = {"air","water","earth","sound","silence","emptiness", 
				"happiness","sadness","meaning","character","love","hate","direction",
				"point","Chuck Norris", "chair","pain","light","heavy","big","virtue",
				"space","life","42","Sylvester Stallone","Bruce Lee", "Yo momma", "fat",
				"null","void","integer","linked-list","darkness",
				"tree","hash-table","forest","death","eternity", "zen", "person"};
		for (int i = 0; i < 100; i++) {
			zenList.add("Zen master says \""+ noun[RNG.nextInt(noun.length)] + " is " +
					noun[RNG.nextInt(noun.length)] + "\".");
		}
	}
	@SuppressWarnings("serial")
	@Override
	List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("zen");
				add("z");
			}
		};
	}
	@Override
	CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {
		Random RNG = new Random();
		String joke = zenList.get(RNG.nextInt(zenList.size()));

		return new CommandResult(this, arguments,
				CommandResult.RenderType.STRING, joke);
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
	
	@Override
	public HelpTablePair getHelpTablePair() {
		return null;
	}

}
