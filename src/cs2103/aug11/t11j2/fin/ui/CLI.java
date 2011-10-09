package cs2103.aug11.t11j2.fin.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cs2103.aug11.t11j2.fin.application.Fin;
import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.parser.AddCommandHandler;
import cs2103.aug11.t11j2.fin.parser.CommandParser;
import cs2103.aug11.t11j2.fin.parser.CommandResult;
import cs2103.aug11.t11j2.fin.parser.DeleteCommandHandler;
import cs2103.aug11.t11j2.fin.parser.ShowCommandHandler;

/**
 * @author alexljz
 * 
 */
public class CLI implements Fin.IUserInterface {

	private static final String PROMPT_SYMBOL = "> ";
	private static final boolean RUN = true;
	private static final String WELCOME_MESSAGE = "Welcome to Fin. Task Manager!";
	private static UIContext context = new UIContext();

	private static Scanner sc = new Scanner(System.in);

	public void mainLoop() {
		displayWelcomeMessage();
		refreshContext();

		String userArgs;
		CommandResult feedback = null;

		while (RUN) {
			showPrompt();
			userArgs = getInput();
			feedback = runCommand(userArgs);
			if (renderCommandResult(feedback)) break;
		}
	}

	/**
	 * refresh current context by calling show command
	 * 
	 * @return true if context has changed and false otherwise
	 */
	private static boolean refreshContext() {
		CommandResult feedback = null;
		String arguments = context.getFilter();
		feedback = runCommand("show " + arguments.trim());

		return updateContext(feedback);
	}

	private static CommandResult runCommand(String command) {
		return CommandParser.INSTANCE.parse(command, context);
	}

	/**
	 * Update context based on a given cmdResult
	 * 
	 * @param cmdResult
	 * @return true if context changes and false otherwise
	 */
	@SuppressWarnings("unchecked")
	private static boolean updateContext(CommandResult cmdResult) {
		boolean different = false;

		if (cmdResult.getRenderType() == CommandResult.RenderType.TaskList) {
			List<Task> taskList = (List<Task>) cmdResult.getReturnObject();

			if (context.getTaskList().size() == taskList.size()) {
				// see if all the original task and the new task are the same
				for (int i = 0; i < taskList.size(); ++i) {
					if (context.getTaskList().get(i) != taskList.get(i)) {
						different = true;
						break;
					}
				}
			} else {
				different = true;
			}

			// since there is a difference, we replace the old task list with
			// the new
			if (different == true) {
				context.setTaskList(taskList);
			}
		}

		if (cmdResult.getCommand() instanceof ShowCommandHandler) {
			context.setFilter(cmdResult.getArgument());
		}

		return different;
	}
	
	/**
	 * @return true if exitCommand is returned and false otherwise.
	 */
	private static boolean renderCommandResult(CommandResult cmdRes) {
		switch (cmdRes.getRenderType()) {
		case String:
			renderString(cmdRes);
			break;
		case TaskList:
			renderTaskListResult(cmdRes);
			break;
		case Task:
			renderTaskResult(cmdRes);
			break;
		case Exit:
			FinApplication.INSTANCE.saveEnvironment();
			echo("Thank you for using Fin.\n");
			echo("Goodbye!\n");
			return true;
		case UnrecognizedCommand:
			echo("Command not recognized!\n\n");
		}
		return false;
	}

	private static void renderString(CommandResult cmdRes) {
		echo((String) cmdRes.getReturnObject() + "\n\n");
		refreshContext();
	}

	private static void renderTaskResult(CommandResult cmdRes) {
		String taskName = ((Task) cmdRes.getReturnObject()).getTaskName();
		CommandParser.ICommandHandler commandHandler = cmdRes.getCommand();

		if (commandHandler instanceof AddCommandHandler) {
			echo("Task: " + taskName + " added!\n");
			echo("\n");

			if (refreshContext() == false) {
				context.setFilter("");
				refreshContext();
			}
			printTaskList();
		} else if (commandHandler instanceof DeleteCommandHandler) {
			echo("Task: " + taskName + " deleted!\n");
			echo("\n");
			refreshContext();
			printTaskList();
		} else {
			refreshContext();
			printTaskList();			
		}
	}

	@SuppressWarnings("unchecked")
	private static void renderTaskListResult(CommandResult cmdRes) {
		updateContext(cmdRes);
		printTaskList();
	}

	private static void printTaskList() {
		int count = 1;
		List<Task> taskList = context.getTaskList();

		List<Task> finTask = new ArrayList<Task>();
		List<Task> imptTask = new ArrayList<Task>();
		List<Task> normalTask = new ArrayList<Task>();
		List<Task> newContext = new ArrayList<Task>();
		
		for (Task t : taskList) {
			if (t.hasTag(FinConstants.FIN_HASH_TAG)) {
				finTask.add(t);
			} else if (t.hasTag(FinConstants.IMPORTANT_HASH_TAG)) {
				imptTask.add(t);
			} else {
				normalTask.add(t);
			}
		}
		
		if (imptTask.size() > 0) {
			echo("#important\n");
			for (Task t : imptTask) {
				newContext.add(t);
				echo("  " + count + ". " + t.getTaskName() + "\n");
				count++;				
			}
		}
		if (normalTask.size() > 0) {
			echo("\n");
			for (Task t : normalTask) {
				newContext.add(t);
				echo("  " + count + ". " + t.getTaskName() + "\n");
				count++;	
			}
			echo("\n");
		}
		if (finTask.size() > 0) {
			echo("#fin\n");
			for (Task t : finTask) {
				newContext.add(t);
				echo("  " + count + ". " + t.getTaskName() + "\n");
				count++;	
			}
		}
		
		context.setTaskList(newContext);

		if (taskList.size() == 0) {
			if (context.getFilter().length() == 0) {
				echo("There are no tasks\n");
			} else {
				echo("There are no tasks that matches your filter\n");
			}
		}

		echo("\n");
	}

	private static void displayWelcomeMessage() {
		echo(WELCOME_MESSAGE);
	}

	private static void showPrompt() {
		echo(context.getFilter());
		echo(PROMPT_SYMBOL);
	}

	private static void echo(String promptMessage) {
		System.out.print(promptMessage);
	}

	private static String getInput() {
		String userArgs = sc.nextLine();
		return userArgs;
	}
}