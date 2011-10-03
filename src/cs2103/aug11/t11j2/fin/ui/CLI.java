package cs2103.aug11.t11j2.fin.ui;

import cs2103.aug11.t11j2.fin.parser.CommandParser;
import cs2103.aug11.t11j2.fin.parser.CommandResult;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author alexljz
 *
 */
public class CLI {
	
	private static final String PROMPT = "> ";
	private static final boolean RUN = true;
	private static final String WELCOME_MESSAGE = "Welcome to Fin. Task Manager!";
	
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String args[]) {
		displayWelcomeMessage();
		
		String userArgs;
		CommandResult feedback = null;
		
		while (RUN) {
			promptUser(PROMPT);
			userArgs = getInput();
			try {
				feedback = CommandParser.INSTANCE.parse(userArgs);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			renderCommandResult(feedback);
		}
	}


	private static void displayWelcomeMessage() {
		promptUser(WELCOME_MESSAGE);
	}
	
	private static void renderCommandResult(CommandResult cmdRes){
		switch(cmdRes.getRenderType()) {
		case String:
			promptUser((String)cmdRes.getReturnObject());
			break;
		case TaskList:
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

