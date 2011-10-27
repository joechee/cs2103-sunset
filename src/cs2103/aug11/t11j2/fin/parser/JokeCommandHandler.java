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
	
	@SuppressWarnings("serial")
	private final List<String> jokeList = new ArrayList<String>(){
		{
			add("Nerds will never go hungry. They have pi.");
			add("Build a man a fire, and he'll be warm for the night. Set a man on fire, and he'll be warm for the rest of his life.");
			add("Abortion brings out the kid in you.");
			add("If life gives you melons, you probably have dyslexia.");
			add("Money can't buy happiness, but it can buy bubble wrap.");
			add("Menstruation, menopause, mental breakdowns... All of a woman's problems begin with men.");
			add("I wondered why the Frisbee was getting bigger, and then it hit me."); 
			add("Treat each day as your last; one day you will be right."); 
			add("Wear a watch and you'll always know what time it is. Wear two watches and you'll never be sure."); 
			add("I watched a guy pickpocket a midget, and couldn't believe how anyone could stoop so low.");
			add("Did you hear about the red ship and the blue ship that collided with each other? Both crews were marooned.");
			add("The Energizer Bunny was arrested, and charged with battery.");
			add("Did you hear about the restaurant on the moon? Great food but no atmosphere.");
		}
	};
	
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

		Random RNG = new Random();
		String joke = jokeList.get(RNG.nextInt(jokeList.size()));

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
	
	public HelpTablePair getNewHelp() {
		return null;
	}
}
