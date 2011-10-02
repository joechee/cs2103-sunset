package cs2103.aug11.t11j2.fin.parser;

import java.util.UUID;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;

public class CommandParser {
	
	private static final String INVALID_ARGUMENTS_ERROR = "Invalid arguments provided.";
	private static final String MYSTERY_ERROR = "Congratulations. You broke it.";
	
	private static FinApplication app = FinApplication.INSTANCE;
	
	private enum COMMAND_TYPE {
		ADD, DELETE, GET_TASKS, INVALID, ERROR;
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
		default:
			return MYSTERY_ERROR;
		}
	}
	
	private static COMMAND_TYPE getCmdType(String command) {
		if (command.equals("")) {
			return COMMAND_TYPE.INVALID;
		} else if (command.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
		} else if (command.equalsIgnoreCase("delete") || command.equalsIgnoreCase("del")) {
			return COMMAND_TYPE.DELETE;
		} else if (command.equalsIgnoreCase("get")) {
			return COMMAND_TYPE.GET_TASKS;
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
}
