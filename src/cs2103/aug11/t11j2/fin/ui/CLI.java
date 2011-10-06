package cs2103.aug11.t11j2.fin.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.parser.CommandParser;
import cs2103.aug11.t11j2.fin.parser.CommandResult;

/**
 * @author alexljz
 *
 */
public class CLI implements IUserInterface{
	
	private static final String PROMPT = "> ";
	private static final boolean RUN = true;
	private static final String WELCOME_MESSAGE = "Welcome to Fin. Task Manager!";
	private static UIContext context;
	
	private static Scanner sc = new Scanner(System.in);
	
	public void mainLoop() {
		displayWelcomeMessage();
		initContext();
		
		
		String userArgs;
		CommandResult feedback = null;
		
		while (RUN) {
			promptUser(PROMPT);
			userArgs = getInput();
			try {
				feedback = CommandParser.INSTANCE.parse(userArgs, context);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
//			renderContext();
			renderCommandResult(feedback);
		}
	}
	
	private static void initContext() {
		CommandResult feedback = null;
		try {
			feedback = CommandParser.INSTANCE.parse("show", context);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		updateContext(feedback);
	}
	
	public static void updateContext(CommandResult feedback) {
		switch (feedback.getRenderType()) {
		case TaskList:
			Object o = feedback.getReturnObject();
			List<Task> taskList = (List<Task>) o;
			context.setTaskList(taskList);
			break;
		case Task:
		case String:			
		case UnrecognizedCommand:
		case InvalidTaskIndex:
		case Null:
			break;
		default:
			break;
		}
	}

	/*private void renderContext() {
		//takes context and renders it accordingly
		int focus = context.getFocused();
		display(context.getTaskList());
		display(context.getStatus());
	}*/

	private static void displayWelcomeMessage() {
		promptUser(WELCOME_MESSAGE);
	}
	
	@SuppressWarnings("unchecked")
	private static void renderCommandResult(CommandResult cmdRes){
		switch(cmdRes.getRenderType()) {
		case String:
			promptUser((String)cmdRes.getReturnObject());
			initContext();
			break;
		case TaskList:
			updateContext(cmdRes);
			Integer count = 1;
			for (Task t : (List<Task>)cmdRes.getReturnObject()) {
				promptUser(count + ". " + t.getTaskName());
				promptUser("\n");
			}
			promptUser("\n");
			break;
		case Task:
			promptUser("Task: " + ((Task) cmdRes.getReturnObject()).getTaskName() + " added.");
			promptUser("\n");
			initContext();
			break;
		case UnrecognizedCommand:
			promptUser("Command not recognized!");
		}
	}
	private static void promptUser(String promptMessage) {
		System.out.print(promptMessage);
	}
	
	private static String getInput() {
		String userArgs = sc.nextLine();
		return userArgs;
	}	
}

