package cs2103.aug11.t11j2.fin.ui;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.application.Fin.IUserInterface;
import cs2103.aug11.t11j2.fin.application.FinApplicationTour;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.gui.FinCLIComposite;
import cs2103.aug11.t11j2.fin.gui.FinCLIInputEvent;
import cs2103.aug11.t11j2.fin.gui.FinCLIInputListener;
import cs2103.aug11.t11j2.fin.parser.AddCommandHandler;
import cs2103.aug11.t11j2.fin.parser.CommandParser;
import cs2103.aug11.t11j2.fin.parser.CommandResult;
import cs2103.aug11.t11j2.fin.parser.DeleteCommandHandler;
import cs2103.aug11.t11j2.fin.parser.EditCommandHandler;
import cs2103.aug11.t11j2.fin.parser.ICommandHandler;
import cs2103.aug11.t11j2.fin.parser.ShowCommandHandler;

public class GUI implements IUserInterface {
	private static boolean EXIT = false;
	
	// shell for SWT
	Shell shell = null;
	static FinCLIComposite cli;
	
	private UIContext context = new UIContext(FinApplication.INSTANCE);

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

		//shell.setSize(800, 500);
		shell.open();

		cli = new FinCLIComposite(shell, SWT.NONE);
		cli.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		cli.addUserInputListener(new FinCLIInputListener() {
			@Override
			public void userInput(FinCLIInputEvent event) {
				if (runCommandAndRender(event.input)) {
					EXIT = true;
				}
			}

			@Override
			public void onChange(FinCLIInputEvent event) {
				String complete = CommandParser.INSTANCE.autoComplete(event.input, context);
				if (complete != null) {
					cli.setAutoComplete(complete);
				}
			}
		});
		
		createFooter(shell);

		

	    ImageData finIconData = new ImageData(this.getClass().getResourceAsStream("fin_icon.png"));
	    Image finIcon = new Image(display, finIconData);
	    GC gc = new GC(finIcon);
	    gc.dispose();

	    shell.setText("Fin.");
	    shell.setImage(finIcon);
		shell.layout(true);

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
	 * Checks if a user input resembles a task 
	 * 
	 * @param (String) user input
	 * @return true if the user input resembles a task
	 */
	private static boolean looksLikeTask(String argument) {
		boolean threeOrMoreWords = argument.split("\\s").length > 2;
		boolean moreThanTenChar = argument.length() > 10;
		
		return threeOrMoreWords && moreThanTenChar;
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
			context.setFilter(cmdResult.getArgument());
		}

		return different;
	}

	/**
	 * Attempts to evaluate a given math expression
	 * 
	 * @param math expression
	 * @return null if math expression is not valid and string containing the result if it is
	 */
	private static String evaluateMathExpression(String expression) {
	    try {
		    ScriptEngineManager mgr = new ScriptEngineManager();
		    ScriptEngine engine = mgr.getEngineByName("JavaScript");
			return engine.eval(expression).toString();
		} catch (ScriptException e) {
			return null;
		}		
	}
	
	/**
	 * @return true if exitCommand is returned and false otherwise.
	 */
	private boolean renderCommandResult(CommandResult cmdRes) {
		switch (cmdRes.getRenderType()) {
		case STRING:
			renderStringResult(cmdRes);
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
		
		case UNRECOGNIZED_COMMAND:
			String expr = evaluateMathExpression(cmdRes.getArgument());
			if (expr != null) {
				echo(expr);
			} else if (looksLikeTask(cmdRes.getArgument())) {
				runCommandAndRender("add " + cmdRes.getArgument());
			} else {
				echo("Command not recognized!\n");
			}
			break;
		
		case TOUR:
			startTour();
		}

		flushOutput();
		return false;
	}
	
	/**
	 * Starts the tour of Fin.
	 */
	private void startTour() {
		context.setFinApplication(FinApplicationTour.INSTANCE);
	}

	/**
	 * Renders a string based result (help, joke etc.)
	 */
	private void renderStringResult(CommandResult cmdRes) {
		echo((String) cmdRes.getReturnObject() + "\n");
		refreshContext();
	}

	/**
	 * Renders a task based result (add, edit, delete etc.)
	 */
	private void renderTaskResult(CommandResult cmdRes) {
		String taskName = ((Task) cmdRes.getReturnObject()).getTaskName();
		ICommandHandler commandHandler = cmdRes.getCommand();

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
		} else if (commandHandler instanceof EditCommandHandler) {
			refreshContext();
			printTaskList();

			echo("Task: " + taskName + " edited!\n");			
		} else {
			refreshContext();
			printTaskList();
		}
	}

	/**
	 * Render a task list based result, i.e list of tasks (show)
	 */
	private void renderTaskListResult(CommandResult cmdRes) {
		updateContext(cmdRes);
		printTaskList();
	}

	/**
	 * print the current list of tasks in context
	 */
	private void printTaskList() {
		List<Task> taskList = context.getTaskList();

		List<Task> imptTask = new ArrayList<Task>();
		List<Task> normalTask = new ArrayList<Task>();
		List<Task> newContext = new ArrayList<Task>();

		// shows the important task above
		// followed by normal task
		for (Task t : taskList) {
			if (t.isImportant()) {
				imptTask.add(t);
			} else {
				normalTask.add(t);
			}
		}

		for (Task t : imptTask) newContext.add(t);
		for (Task t : normalTask) newContext.add(t);
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
				flushOutput();
			}
			cli.addTaskList(newContext);
		}
	}


	// output buffer
	private static StringBuilder outputBuffer = new StringBuilder();
	
	/**
	 * adds a message to the output buffer
	 * @param promptMessage
	 */
	public void echo(String promptMessage) {
		cli.echo(promptMessage);
		cli.refresh();
	}
	
	/**
	 * flushes output buffer
	 */
	private void flushOutput() {
		if (outputBuffer.length() > 0){ 
			outputBuffer = new StringBuilder();
		}
	}

	
	@Override
	public void mainLoop() {
		initShell();
	}
}
