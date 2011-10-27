package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

public class ZenCommandHandler extends ICommandHandler {
	List<String> jokeList;
	ZenCommandHandler() {
		jokeList = new ArrayList<String>();
		Random RNG = new Random();
		String[] noun = {"air","water","earth","fire","sound","silence","emptiness", 
				"happiness","sadness","meaning","character","love","hate","direction",
				"point","Chuck Norris", "chair",
				"space","life","42","Sylvester Stallone","Bruce Lee", "Yo momma", "fat",
				"null","void","bool","integer","float","double","linked-list","stack","queue",
				"tree","hash","hash-table","forest","death","eternity","cache"};
		for (int i = 0; i < 100; i++) {
			
			jokeList.add("Zen master says \""+ noun[RNG.nextInt(noun.length)] + " is " +
					noun[RNG.nextInt(noun.length)] + "\".");
		}
	}
	@Override
	public List<String> getCommandStrings() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public HelpTablePair getHelpTablePair() {
		// TODO Auto-generated method stub
		return null;
	}


}
