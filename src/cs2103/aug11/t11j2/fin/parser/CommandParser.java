package cs2103.aug11.t11j2.fin.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;

public class CommandParser {
	
	private static final String INVALID_ARGUMENTS_ERROR = "Invalid arguments provided.";
	private static final String MYSTERY_ERROR = "Congratulations. You broke it.";
	
	private static Map<String, ICommandHandler> commandHandlers = new HashMap<String,ICommandHandler>(); 
	
	private enum COMMAND_TYPE {
		ADD, DELETE, GET_TASKS, EXIT, HELP, EMPTY, ERROR;
	}
	
	public static String parse(String userArgs) {
		String feedback = "";
		String command = "";
		
		command = getCommand(userArgs);
		feedback = runCommand(command, userArgs);
		
		return feedback;
	}
	
	private static String getCommand(String userArgs) {
		String command = tokenize(userArgs)[0];
		return command;
	}

	private static String[] tokenize(String userCommand) {
		return userCommand.trim().split("\\s+");
	}
	
	private static String runCommand(String command, String userArgs) {
		COMMAND_TYPE cmd = getCmdType(command);
		String cmdArgs = userArgs.replaceFirst(command, "").trim();
		
		switch (cmd) {
		case ADD:
			return add(cmdArgs);
		case DELETE:
			return delete(cmdArgs);
		case GET_TASKS:
			return getTasks(cmdArgs);
		case EXIT:
			System.exit(0);
		case HELP:
			return getHelp();
		case EMPTY:
			return "";
		case ERROR:
			return INVALID_ARGUMENTS_ERROR;
		default:
			return MYSTERY_ERROR;
		}
	}
	
	private static COMMAND_TYPE getCmdType(String command) {
		if (command.equals("")) {
			return COMMAND_TYPE.EMPTY;
		} else if (command.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
		} else if (command.equalsIgnoreCase("delete") || command.equalsIgnoreCase("del")) {
			return COMMAND_TYPE.DELETE;
		} else if (command.equalsIgnoreCase("get")) {
			return COMMAND_TYPE.GET_TASKS;
		} else if (command.equalsIgnoreCase("help")) {
			return COMMAND_TYPE.HELP;
		} else if (command.equalsIgnoreCase("exit")) {
			return COMMAND_TYPE.EXIT;
		} else {
			return COMMAND_TYPE.ERROR;
		}
	}
	
	private static String add(String cmdArgs) {
		String feedback = "";
		
//		Task newTask = parseTask(cmdArgs);
//		app.add(newTask, newTask.getUniqId());
		
		return feedback;
	}
	
	private static String delete(String cmdArgs) {
		String feedback = "";
		boolean deleted = false;
		UUID id = UUID.fromString(cmdArgs);
				
		app.deleteTask(id);
		
		if (deleted) {
			feedback = "Task deleted successfully.";
		} else {
			feedback = "No such task.";
		}
		
		return feedback;
	}
	
	private static String getTasks(String cmdArgs) {
		String feedback = "";
		
		return feedback;
	}
	
	private static String getHelp() {
		// get help listing from FinApp?
		String help = "The available commands are: add, delete, get, help, exit.";
		
		return help;
	}
}
