package cs2103.aug11.t11j2.fin.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;

public class CommandParser {
	public static CommandParser INSTANCE = new CommandParser();
	
	private static final String INVALID_ARGUMENTS_ERROR = "Invalid arguments provided.";
	private static final String MYSTERY_ERROR = "Congratulations. You broke it.";
	
	private static Map<String, ICommandHandler> commandHandlers = new HashMap<String,ICommandHandler>();
	
	private CommandParser() {
		try {
			installCommand(new AddCommandHandler());
		} catch (FinProductionException e) {
			if (FinConstants.IS_PRODUCTION) {
				System.out.println("Unexpected error! You better go square it away");
				System.out.println(e);
			}
		}
	}
	
	void installCommand(ICommandHandler commandHandler) throws FinProductionException {
		for (String command : commandHandler.getCommandStrings()) {
			if (commandHandlers.containsKey(command)) {
				throw(new FinProductionException("Collision in command keywords"));
			}else {
				commandHandlers.put(command, commandHandler);
			}
		}
	}
		
	public static CommandResult parse(String userArgs) {
		String command = "";
		
		command = getCommand(userArgs);
		return runCommand(command, userArgs);		
	}
	
	private static String getCommand(String userArgs) {
		String command = tokenize(userArgs)[0];
		return command;
	}

	private static String[] tokenize(String userCommand) {
		return userCommand.trim().split("\\s+");
	}
	
	private static CommandResult runCommand(String command, String userArgs) {		
		ICommandHandler commandHandler = commandHandlers.get(command.toLowerCase());
		String cmdArgs = userArgs.replaceFirst(command, "").trim();
		
		CommandResult res = commandHandler.executeCommands(cmdArgs);
		
		return res;
	}
	
}
