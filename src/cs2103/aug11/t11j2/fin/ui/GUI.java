package cs2103.aug11.t11j2.fin.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.application.Fin.IUserInterface;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.gui.FinCLIComposite;
import cs2103.aug11.t11j2.fin.gui.FinCLIInputEvent;
import cs2103.aug11.t11j2.fin.gui.FinCLIInputListener;
import cs2103.aug11.t11j2.fin.parser.AddCommandHandler;
import cs2103.aug11.t11j2.fin.parser.CommandParser;
import cs2103.aug11.t11j2.fin.parser.CommandResult;
import cs2103.aug11.t11j2.fin.parser.DeleteCommandHandler;
import cs2103.aug11.t11j2.fin.parser.ShowCommandHandler;

public class GUI implements IUserInterface {
	private static boolean EXIT = false;
	
	// shell for SWT
	Shell shell = null;
	static FinCLIComposite cli;
	private static final String WELCOME_MESSAGE = "Welcome to Fin. Task Manager!\n";
	private static UIContext context = new UIContext();

	private static Composite createFooter(Composite shell) {
		Composite footer = new Composite(shell, SWT.RIGHT_TO_LEFT);

		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true,
				false);
		gridData.heightHint = 30;
		footer.setLayoutData(gridData);

		Label l = new Label(footer, SWT.LEFT_TO_RIGHT);
		l.setText("Fin.");
		l.setFont(new Font(shell.getDisplay(), "Segoe UI", 12, SWT.BOLD));

		l.setBounds(5, 5, 30, 20);

		// creates gradient for footer
		Image newImage = new Image(shell.getDisplay(), 1, 30);
		GC gc = new GC(newImage);
		gc.setForeground(new Color(null, 43, 72, 153));
		gc.setBackground(new Color(null, 36, 64, 128));
		gc.fillGradientRectangle(0, 0, 1, 30, true);
		gc.dispose();
		footer.setBackgroundImage(newImage);

		l.setForeground(new Color(null, 255, 255, 255));
		l.setBackgroundImage(newImage);

		footer.layout(true);
		return footer;
	}

	void initShell() {
		final Display display = new Display();
		shell = new Shell(display);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		gridLayout.marginTop = gridLayout.marginLeft = gridLayout.marginRight = gridLayout.marginBottom = -5;
		shell.setLayout(gridLayout);

		shell.setSize(800, 500);
		shell.open();

		cli = new FinCLIComposite(shell, SWT.NONE);
		cli.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		cli.addUserInputListener(new FinCLIInputListener() {
			@Override
			public void UserInput(FinCLIInputEvent event) {
				CommandResult feedback = null;
				feedback = runCommand(event.input);
				if (renderCommandResult(feedback)) {
					EXIT = true;
				}

			}
		});
		
		createFooter(shell);

		shell.layout(true);

		displayWelcomeMessage();
		refreshContext();
		displayTasks();
		cli.forceFocus();


 		while (!EXIT && !shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
 		if (!shell.isDisposed()) {
 	 		shell.dispose();
 		}

		display.dispose();
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

	private static void displayTasks() {
		CommandResult feedback = null;
		feedback = runCommand("show");

		renderCommandResult(feedback);
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
			echo("Command not recognized!\n");
		}

		refresh();
		return false;
	}

	private static void renderString(CommandResult cmdRes) {
		echo((String) cmdRes.getReturnObject() + "\n");
		refresh();
		refreshContext();
	}

	private static void renderTaskResult(CommandResult cmdRes) {
		String taskName = ((Task) cmdRes.getReturnObject()).getTaskName();
		CommandParser.ICommandHandler commandHandler = cmdRes.getCommand();

		if (commandHandler instanceof AddCommandHandler) {
			if (refreshContext() == false) {
				context.setFilter("");
				refreshContext();
			}
			printTaskList();

			echo("Task: " + taskName + " added!\n");
		} else if (commandHandler instanceof DeleteCommandHandler) {
			refreshContext();
			printTaskList();

			echo("Task: " + taskName + " deleted!\n");
		} else {
			refreshContext();
			printTaskList();
		}
	}

	private static void renderTaskListResult(CommandResult cmdRes) {
		updateContext(cmdRes);
		printTaskList();
	}

	private static void printTaskList() {
		List<Task> taskList = context.getTaskList();

		List<Task> finTask = new ArrayList<Task>();
		List<Task> imptTask = new ArrayList<Task>();
		List<Task> normalTask = new ArrayList<Task>();
		List<Task> newContext = new ArrayList<Task>();

		for (Task t : taskList) {
			if (t.isFin()) {
				finTask.add(t);
			} else if (t.isImportant()) {
				imptTask.add(t);
			} else {
				normalTask.add(t);
			}
		}

		if (imptTask.size() > 0) {
			for (Task t : imptTask) {
				newContext.add(t);
			}
		}
		if (normalTask.size() > 0) {
			for (Task t : normalTask) {
				newContext.add(t);
			}
		}
		if (finTask.size() > 0) {
			for (Task t : finTask) {
				newContext.add(t);
			}
		}

		context.setTaskList(newContext);

		cli.clear();
		if (taskList.size() == 0) {
			if (context.getFilter().length() == 0) {
				echo("There are no tasks\n");
			} else {
				echo("There are no tasks that matches your filter ("+context.getFilter()+")\n");
			}
		} else {
			String filter = context.getFilter();
			if (filter.length() > 0) {
				echo(filter);
				refresh();
			}
			cli.addTaskList(newContext);
		}
	}

	private static void displayWelcomeMessage() {
		echo(WELCOME_MESSAGE);
		refresh();
	}

	private static void refresh() {
		if (b.length() > 0){ 
			System.out.println(b.toString());
			cli.echo(b.toString());
			cli.refresh();
			b = new StringBuilder();
		}
	}

	private static StringBuilder b = new StringBuilder();

	private static void echo(String promptMessage) {
		b.append(promptMessage);
	}

	@Override
	public void mainLoop() {
		initShell();
	}
}
