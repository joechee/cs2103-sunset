package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

public class CommandParser {

	public static interface ICommandHandler {
		public List<String> getCommandStrings();

		CommandResult executeCommands(String command, String arguments,
				UIContext context) throws FinProductionException;
		
		public String showAbridgedHelp();
		public CommandResult showHelp() throws FinProductionException;
	}

	public static CommandParser INSTANCE = new CommandParser();

	private Map<String, ICommandHandler> commandHandlers = new HashMap<String, ICommandHandler>();
	private List<ICommandHandler> commandHandlerLyst = new ArrayList<ICommandHandler>();

	private CommandParser() {
		try {
			installCommand(new HelpCommandHandler());
			installCommand(new ShowCommandHandler());
			installCommand(new AddCommandHandler());
			installCommand(new FinCommandHandler());
			installCommand(new UnfinCommandHandler());
			installCommand(new ImportantCommandHandler());
			installCommand(new UnImportantCommandHandler());
			installCommand(new DeleteCommandHandler());
			installCommand(new DeleteAllCommandHandler());
			installCommand(new JokeCommandHandler());
			installCommand(new EditCommandHandler());
			installCommand(new ExitCommandHandler());
			installCommand(new SearchTasksWithPatternCommandHandler());
			installCommand(new UntagTaskWithTagsCommandHandler());
			installCommand(new TagTaskWithTagsCommandHandler());
		} catch (FinProductionException e) {
			if (FinConstants.IS_PRODUCTION) {
				System.out
						.println("Unexpected error! You better go square it away");
				e.printStackTrace();
			}
		}
	}
	/**
	 * Installs the command into the system so that it can be called by the <code>parse</code> method.
	 * @param commandHandler
	 * @throws FinProductionException
	 * @see #parse(String, UIContext)
	 */

	void installCommand(ICommandHandler commandHandler)
			throws FinProductionException {
		for (String command : commandHandler.getCommandStrings()) {
			if (commandHandlers.containsKey(command)) {
				throw (new FinProductionException(
						"Collision in command keywords"));
			} else {
				commandHandlers.put(command, commandHandler);
			}
		}
		commandHandlerLyst.add(commandHandler);
	}

	public CommandResult parse(String userArgs, UIContext context) {
		String command = "";

		command = getCommand(userArgs);
		return runCommand(command, userArgs, context);
	}

	private String getCommand(String userArgs) {
		String command = tokenize(userArgs)[0];
		return command;
	}

	private String[] tokenize(String userCommand) {
		return userCommand.trim().split("\\s+");
	}

	private CommandResult runCommand(String command, String userArgs,
			UIContext context) {
		// Check if the current command is installed in CommandParser
		if (!commandHandlers.containsKey(command.toLowerCase())) {
			return CommandResult.unrecognizedCommand(userArgs);
		}

		// Get the installed CommandHandler
		ICommandHandler commandHandler = commandHandlers.get(command
				.toLowerCase());
		String cmdArgs = userArgs.replaceFirst(Pattern.quote(command), "").trim();

		CommandResult res = null;
		try {
			// Execute the CommandHandler with the arguments
			res = commandHandler.executeCommands(command, cmdArgs, context);
		} catch (FinProductionException e) {
			if (FinConstants.IS_PRODUCTION) {
				e.printStackTrace();
			}
		}

		return res;
	}
	
	List<ICommandHandler> getCommandHandlers(){
		return new ArrayList<ICommandHandler>(commandHandlerLyst);
	}

}
