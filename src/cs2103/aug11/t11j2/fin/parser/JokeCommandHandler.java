package cs2103.aug11.t11j2.fin.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.jotd.Joke;
import cs2103.aug11.t11j2.fin.ui.UIContext;

public class JokeCommandHandler implements ICommandHandler {

	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("joke");
				add("jotd");
				add("jok");
				add("jo");
				add("j");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {
		String joke = "";
		try {
			Joke testJoke = new Joke();
			joke = testJoke.generate();
		} catch (IOException e) {
			if (FinConstants.IS_PRODUCTION) {
				e.printStackTrace();
			}
			joke = "The Joke's on FIN";
		}
		return new CommandResult(this, arguments,
				CommandResult.RenderType.String, joke);
	}

}
