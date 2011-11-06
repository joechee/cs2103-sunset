package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * Main interface to parse commands and activate the relevant command handlers.
 * 
 * @author Koh Zi Chun
 *
 */
public class CommandParser {

	public static CommandParser INSTANCE = new CommandParser();

	private Map<String, ICommandHandler> commandHandlers = new HashMap<String, ICommandHandler>();
	private List<ICommandHandler> commandHandlerLyst = new ArrayList<ICommandHandler>();
	private Logger logger;

	protected CommandParser() {
		logger = Logger.getLogger(this.getClass());
		ArrayList<ICommandHandler> commandHandlers = new ArrayList<ICommandHandler>();
		
		commandHandlers.add(new HelpCommandHandler());
		commandHandlers.add(new ShowCommandHandler());
		commandHandlers.add(new AddCommandHandler());
		commandHandlers.add(new FinCommandHandler());
		commandHandlers.add(new UnfinCommandHandler());
		commandHandlers.add(new ImportantCommandHandler());
		commandHandlers.add(new UnImportantCommandHandler());
		commandHandlers.add(new DeleteCommandHandler());
		commandHandlers.add(new DeleteAllCommandHandler());
		commandHandlers.add(new JokeCommandHandler());
		commandHandlers.add(new EditCommandHandler());
		commandHandlers.add(new ExitCommandHandler());
		commandHandlers.add(new UntagTaskWithTagsCommandHandler());
		commandHandlers.add(new TagTaskWithTagsCommandHandler());
		commandHandlers.add(new UndeleteCommandHandler());
		commandHandlers.add(new TourCommandHandler());
		commandHandlers.add(new EndTourCommandHandler());
		commandHandlers.add(new ZenCommandHandler());
		// install the automated test suite if we are in development mode
		if (FinConstants.IS_DEVELOPMENT) {
			commandHandlers.add(new TestCommandHandler());
		}
		
		for (ICommandHandler i: commandHandlers) {
			try {
				installCommand(i);
			} catch (FinProductionException e) {
				if (FinConstants.IS_DEVELOPMENT) {
					logError(e);

				}
			}
		}

		logger.info("CommandParser Object created successfully.");
	}
	
	private void logError(FinProductionException e) {
		String errmsg = "Unexpected error! You better go square it away\n";
		errmsg += e + "\n";
		String stackTrace = "Stack Trace:";
		for (StackTraceElement j: e.getStackTrace()) {
			stackTrace += "\n" + j.toString();
		}
		logger.debug(errmsg + stackTrace);
	}

	/**
	 * Installs the command into the system so that it can be called by the <code>parse</code> method.
	 * @param commandHandler
	 * @throws FinProductionException
	 * @see #parse(String, UIContext)
	 */
	private void installCommand(ICommandHandler commandHandler)
			throws FinProductionException {
		if (commandHandler.getCommandStrings().isEmpty()) {
			throw (new FinProductionException ("CommandHandler that can't be accessed has been installed"));
		}
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
	
	/**
	 * parse a command from the UI
	 * 
	 * @param userArgs command that was input
	 * @param context current User Interface context
	 * @return
	 */
	public CommandResult parse(String userArgs, UIContext context) {
		String command = "";

		command = getCommand(userArgs);
		return runCommand(command, userArgs, context);
	}
	
	/**
	 * tries to complete a string that a user types halfway
	 * 
	 * @param userArgs
	 * @param context
	 * @return a string representing the suggested completion (should include the original string as prefix)
	 */
	public String autoComplete(String userArgs, UIContext context) {
		String command = getCommand(userArgs);

		// Check if the current command is installed in CommandParser
		if (!commandHandlers.containsKey(command.toLowerCase())) {
			return null;
		}

		// Get the installed CommandHandler
		ICommandHandler commandHandler = commandHandlers.get(command
				.toLowerCase());
		String cmdArgs = userArgs.replaceFirst(Pattern.quote(command), "").trim();
		
		return commandHandler.autoComplete(userArgs, command, cmdArgs, context);
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
			if (FinConstants.IS_DEVELOPMENT) {
				logError(e);
			}
		}

		return res;
	}
	
	List<ICommandHandler> getCommandHandlers(){
		return new ArrayList<ICommandHandler>(commandHandlerLyst);
	}

}
