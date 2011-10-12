package cs2103.aug11.t11j2.fin.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.storage.Serializer;
import cs2103.aug11.t11j2.fin.ui.UIContext;

public class JokeCommandHandler implements CommandParser.ICommandHandler {
	class Joke {
		List<String> jokeList;
		Serializer jokeSerial;

		public Joke() throws IOException {
			jokeList = new ArrayList<String>();
			jokeSerial = new Serializer();
			// System.out.println(jokeSerial.unserialize("Joke.yaml").get(1));
			List<Object> serialList = jokeSerial.unserialize("Joke.yaml");
			Iterator<Object> serialListIterator = serialList.iterator();
			while (serialListIterator.hasNext()) {
				jokeList.add((String) serialListIterator.next());
			}

		}

		public String generate() {
			Random RNG = new Random();
			return jokeList.get(RNG.nextInt(jokeList.size()));
		}

		public void addJoke(String joke) throws IOException {
			jokeList.add(joke);
			saveJokes();
		}

		public void saveJokes() throws IOException {
			jokeSerial.serialize((new ArrayList<Object>(jokeList)).iterator(),
					"Joke.yaml");
		}
	}

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
	
	CommandResult showHelp(UIContext context) throws FinProductionException {
		HelpCommandHandler helpCmdHandler = new HelpCommandHandler();
		return helpCmdHandler.executeCommands( helpCmdHandler.getCommandStrings().get(0),
				"joke", context);
	}
	

}
