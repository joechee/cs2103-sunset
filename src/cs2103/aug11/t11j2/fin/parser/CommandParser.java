package cs2103.aug11.t11j2.fin.parser;

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

	}

	public static CommandParser INSTANCE = new CommandParser();

	private Map<String, ICommandHandler> commandHandlers = new HashMap<String, ICommandHandler>();

	private CommandParser() {
		try {
			installCommand(new ShowCommandHandler());
			installCommand(new AddCommandHandler());
			installCommand(new FinCommandHandler());
			installCommand(new UnfinCommandHandler());
			installCommand(new ImportantCommandHandler());
			installCommand(new UnImportantCommandHandler());
			installCommand(new DeleteCommandHandler());
			installCommand(new JokeCommandHandler());
			installCommand(new EditCommandHandler());
			installCommand(new HelpCommandHandler());
			installCommand(new ExitCommandHandler());
		} catch (FinProductionException e) {
			if (FinConstants.IS_PRODUCTION) {
				System.out
						.println("Unexpected error! You better go square it away");
				e.printStackTrace();
			}
		}
	}

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
			return CommandResult.unrecognizedCommandResult;
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

}
