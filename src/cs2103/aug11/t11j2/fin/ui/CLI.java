package cs2103.aug11.t11j2.fin.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cs2103.aug11.t11j2.fin.application.Fin;
import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.parser.*;
/**
 * @author alexljz
 * 
 */
public class CLI implements Fin.IUserInterface {

	private static final String PROMPT_SYMBOL = "> ";
	private static final boolean RUN = true;
	private static String lastFilter = "";
	private static final String WELCOME_MESSAGE = "Welcome to Fin. Task Manager!\n";
	private static final int TABLE_BORDER_WIDTH = 3;
	private UIContext context = new UIContext(FinApplication.INSTANCE);

	private static Scanner sc = new Scanner(System.in);

	public void mainLoop(boolean fileLoaded) {
		displayWelcomeMessage();
		refreshContext();
		displayTasks();

		String userArgs;

		while (RUN) {
			showPrompt();
			userArgs = getInput();

			if (runCommandAndRender(userArgs)) break;
		}
	}

	/**
	 * refresh current context by calling show command
	 * 
	 * @return true if context has changed and false otherwise
	 */
	private boolean refreshContext() {
		CommandResult feedback = null;
		String arguments = context.getFilter();
		feedback = runCommand("show " + arguments.trim());

		return updateContext(feedback);
	}

	private void displayTasks() {
		runCommandAndRender("show");
	}
	
	@Override
	public boolean runCommandAndRender(String userArgs) {
		CommandResult feedback = null;
		feedback = runCommand(userArgs);
		
		return renderCommandResult(feedback);
	}

	private CommandResult runCommand(String command) {
		return CommandParser.INSTANCE.parse(command, context);
	}

	/**
	 * Update context based on a given cmdResult
	 * 
	 * @param cmdResult
	 * @return true if context changes and false otherwise
	 */
	@SuppressWarnings("unchecked")
	private boolean updateContext(CommandResult cmdResult) {
		boolean different = false;

		if (cmdResult.getRenderType() == CommandResult.RenderType.TASKLIST) {
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
			lastFilter = context.getFilter();
			context.setFilter(cmdResult.getArgument());
		}

		return different;
	}
	
	/**
	 * @return true if exitCommand is returned and false otherwise.
	 */
	private boolean renderCommandResult(CommandResult cmdRes) {
		switch (cmdRes.getRenderType()) {
		case STRING:
			renderString(cmdRes);
			break;
		case TASKLIST:
			renderTaskListResult(cmdRes);
			break;
		case TASK:
			renderTaskResult(cmdRes);
			break;
		case EXIT:
			echo("Thank you for using Fin.\n");
			echo("Goodbye!\n");
			return true;
		case ERROR:
			renderError(cmdRes);
			break;
		case UNRECOGNIZED_COMMAND:
			if (looksLikeTask(cmdRes.getArgument())) {
				runCommandAndRender("add " + cmdRes.getArgument());
			} else {
				echo("Command not recognized!\n\n");
			}
			break;
		case HELPTABLE:
			renderHelpTable(cmdRes);
			break;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private void renderHelpTable(CommandResult cmdRes) {
		List<HelpTablePair>helpTable = (List<HelpTablePair>) cmdRes.getReturnObject();

		int optimalBreadth = findOptimalTableBreadth(helpTable);
		for (HelpTablePair p: helpTable) {
			
			echo(padWhiteSpace(p.getUsage(), optimalBreadth));
			echo(p.getDescription());
			echo("\n");
		}
	}

	private int findOptimalTableBreadth(List<HelpTablePair> helpTable) {
		int optimal = 0;
		for (HelpTablePair p: helpTable) {
			if (optimal < p.getUsage().length()) {
				optimal = p.getUsage().length();
			}
		}
		return optimal + TABLE_BORDER_WIDTH;
	}

	private String padWhiteSpace(String usage, int table_breadth) {
		while (usage.length()<table_breadth) {
			usage += " ";
		}
		return usage;
	}

	private void renderError(CommandResult cmdRes) {
		echo((String) cmdRes.getReturnObject() + "\n\n");
		refreshContext();
	}

	private static boolean looksLikeTask(String argument) {
		boolean threeOrMoreWords = argument.split("\\s").length > 2;
		boolean moreThanTenChar = argument.length() > 10;

		return threeOrMoreWords && moreThanTenChar;
	}

	private void renderString(CommandResult cmdRes) {
		echo((String) cmdRes.getReturnObject() + "\n\n");
		refreshContext();
	}

	private void renderTaskResult(CommandResult cmdRes) {
		String taskName = ((Task) cmdRes.getReturnObject()).getTaskName();
		ICommandHandler commandHandler = cmdRes.getCommand();

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
	private void renderTaskListResult(CommandResult cmdRes) {
		ICommandHandler commandHandler = cmdRes.getCommand();
		if (commandHandler instanceof ShowCommandHandler) {
			updateContext(cmdRes);
			printTaskList();
		} else if (commandHandler instanceof UndeleteCommandHandler) {
			List<Task >taskList = (List<Task>) cmdRes.getReturnObject();
			if (taskList.size() == 1) {
				echo("Task: " + taskList.get(0).getTaskName() + " re-added!\n");
			} else {
				echo("Tasks: ");
				for (int i = 0; i < taskList.size();i++) {
					if (i == 0) {
						echo (taskList.get(i).getTaskName());
					} else {
						echo (", "+ taskList.get(i).getTaskName());
					}

				}
				echo(" re-added!\n");
			}
			
			
			refreshContext();
			printTaskList();
		}

	}

	private void printTaskList() {
		int count = 1;
		List<Task> taskList = context.getTaskList();

		List<Task> imptTask = new ArrayList<Task>();
		List<Task> normalTask = new ArrayList<Task>();
		List<Task> newContext = new ArrayList<Task>();
		
		for (Task t : taskList) {
			if (t.isImportant()) {
				imptTask.add(t);
			} else {
				normalTask.add(t);
			}
		}
		
		if (imptTask.size() > 0) {
			lineEchoWithBox(FinConstants.IMPORTANT_HASH_TAG);
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

		context.setTaskList(newContext);

		if (taskList.size() == 0) {
			if (context.getFilter().length() == 0) {
				echo("There are no tasks\n");
			} else {
				String filter = context.getFilter();
				runCommandAndRender("show " + lastFilter);
				echo("There are no tasks that matches your filter ("+filter+")\n");
			}
		}


	}

	private void displayWelcomeMessage() {
		echo(WELCOME_MESSAGE);
	}

	private void showPrompt() {
		echo(context.getFilter());
		echo(PROMPT_SYMBOL);
	}

	@Override
	public void echo(String promptMessage) {
		System.out.print(promptMessage);
	}
	
	@Override
	public void clearScreen() {
		
	}

	/**
	 * Outputs a line with a box surrounding it. Only works for single line input though.
	 * @param promptMessage
	 */
	private void lineEchoWithBox(String promptMessage) {
		for (int i = 0; i < promptMessage.length()+4; i++) {
			echo("#");
		}
		echo ("\n# ");
		echo(promptMessage);
		echo (" #\n");
		for (int i = 0; i < promptMessage.length()+4; i++) {
			echo("#");
		}
		echo("\n");
	}
	

	private String getInput() {
		String userArgs = sc.nextLine();
		return userArgs;
	}
}