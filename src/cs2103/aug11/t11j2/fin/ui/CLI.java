package cs2103.aug11.t11j2.fin.ui;

import cs2103.aug11.t11j2.fin.parser.CommandParser;

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
		
		String userArgs, feedback = "";
		
		while (RUN) {
			promptUser(PROMPT);
			userArgs = getInput();
			feedback = CommandParser.parse(userArgs);
			promptUser(feedback + "\n");
		}
	}


	private static void displayWelcomeMessage() {
		System.out.println(WELCOME_MESSAGE);
	}
	
	private static void promptUser(String promptMessage) {
		System.out.print(promptMessage);
	}
	
	private static String getInput() {
		String userArgs = sc.nextLine();
		return userArgs;
	}
	
}
	
	
