package cs2103.aug11.t11j2.fin.ui;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.parser.CommandParser;
import cs2103.aug11.t11j2.fin.parser.CommandResult;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * @author alexljz
 *
 */
public class CLI implements UI{
	
	private static final String PROMPT = "> ";
	private static final boolean RUN = true;
	private static final String WELCOME_MESSAGE = "Welcome to Fin. Task Manager!";
	private UIContext context;
	
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
			updateContext(feedback);
			renderContext();
			//renderCommandResult(feedback);
		}
	}
	
	private void initContext() {
		CommandResult feedback = null;
		try {
			feedback = CommandParser.INSTANCE.parse("show", context);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		updateContext(feedback);
	}
	
	//assumption that context has a status message + tasklist
	public void updateContext(CommandResult feedback) {
		// this should update the context using the provided CommandResult
		// how will a Task or String affect the context?
		switch (feedback.getRenderType()) {
		case String:
			//add string to bottom of context
			context.updateStatus((String)feedback.getReturnObject());
			break;
		case TaskList:
			//replace context with given tasklist
			context.setTaskList((List<Task>)feedback.getReturnObject());
			break;
		case Task:
			//indicate added task in task list
			context.focusTask((Task)feedback.getReturnObject());
			break;
		case UnrecognizedCommand:
		case InvalidTaskIndex:
		case Null:
			// all cases of String actually? add a error message to bottom of context to be displayed
			context.updateStatus(someErrorMsg);
			break;
		default:
			break;
		}
	}

	private void renderContext() {
		//takes context and renders it accordingly
		int focus = context.getFocused();
		display(context.getTaskList());
		display(context.getStatus());
	}

	private static void displayWelcomeMessage() {
		promptUser(WELCOME_MESSAGE);
	}
	
	@SuppressWarnings("unchecked")
	private static void renderCommandResult(CommandResult cmdRes){
		switch(cmdRes.getRenderType()) {
		case String:
			promptUser((String)cmdRes.getReturnObject());
			break;
		case TaskList:
			Integer count = 1;
			for (Task t : (List<Task>)cmdRes.getReturnObject()) {
				promptUser(count + ". " + t.getTaskName());
				promptUser("\n");
			}
			promptUser("\n");
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

