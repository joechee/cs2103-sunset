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

public class JokeCommandHandler extends ICommandHandler {
	class Joke {
		List<String> jokeList;
		Serializer jokeSerial;

		public Joke() throws IOException {
			jokeList = new ArrayList<String>();
			jokeSerial = new Serializer();

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
				CommandResult.RenderType.STRING, joke);
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
