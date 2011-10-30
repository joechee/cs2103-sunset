package cs2103.aug11.t11j2.fin.ui;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.application.Fin.IUserInterface;
import cs2103.aug11.t11j2.fin.application.FinApplicationTour;
import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.gui.FinCLIComposite;
import cs2103.aug11.t11j2.fin.gui.FinCLIInputEvent;
import cs2103.aug11.t11j2.fin.gui.FinCLIInputListener;
import cs2103.aug11.t11j2.fin.gui.FinFooter;
import cs2103.aug11.t11j2.fin.parser.*;

public class GUI implements IUserInterface {
	private static boolean EXIT = false;
	
	// shell for SWT
	Shell shell = null;
	static FinCLIComposite cli;
	
	final private UIContext context = new UIContext(FinApplication.INSTANCE);
	private String lastFilter = "";
	private FinTour finTour = null;
	private boolean isInTour = false;
	
	// Help table constant
	private static final int TABLE_BORDER_WIDTH = 3;

	private Composite createFooter(Composite shell) {
		Composite footer = new Composite(shell, SWT.NONE);

		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true,
				false);
		gridData.heightHint = 30;
		footer.setLayoutData(gridData);
		
		GridLayout layout = new GridLayout(6, false);
		layout.marginTop = 0;
	    footer.setLayout(layout);


		// creates gradient for footer
		Image newImage = new Image(shell.getDisplay(), 1, 30);
		GC gc = new GC(newImage);
		gc.setForeground(new Color(null, 43, 72, 153));
		gc.setBackground(new Color(null, 36, 64, 128));
		gc.fillGradientRectangle(0, 0, 1, 30, true);
		gc.dispose();
		footer.setBackgroundImage(newImage);

		final Shell tip = new Shell(shell.getShell(), SWT.TOOL | SWT.ON_TOP | SWT.NO_FOCUS );
		tip.setVisible(false);
		tip.setLayout(new RowLayout());
		
		
		// remove toolTip
		Listener removeTipListener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (tip.isVisible() == false) return;
				Object w = tip.getDisplay().getCursorControl();
				for (Control c : tip.getChildren()) {
					if (c.equals(w)) return;
				}
				
				tip.setVisible(false);
				if (cli.isInHint()) cli.removeHint();
			}
		};
		
		tip.addListener(SWT.MouseExit,removeTipListener);

		FinFooter finFooter = new FinFooter(footer, cli, 
				// mouse over
				new Listener() {
					@Override
					public void handleEvent(Event event) {
						Button b = (Button)event.widget;
						if (event.text.equals("add")) {
							cli.setHint("add <task>", "add a new task");
						} else if(event.text.equals("show")) {
							cli.setHint("show", "show all the tasks in Fin.");
							
							
							tip.setBackground (new Color(null, FinConstants.BACKGROUND_COLOR));
							
							for (Control c : tip.getChildren()) {
								c.dispose();
							}
							
							List<String> hashTags = context.getFinApplication().getHashTags();
							String filter = " " + context.getFilter() + " ";

							for (String ht : hashTags) {
								Button hashButton = new Button(tip, SWT.TOGGLE | SWT.FLAT);
								hashButton.setText("#" + ht);
								
								if (filter.contains(" " + ht + " " ) || filter.contains(" #" + ht + " " )) {
									hashButton.setSelection(true);
								}
								hashButton.setBackground (new Color(null, FinConstants.BACKGROUND_COLOR));
								
								hashButton.addSelectionListener(new SelectionListener() {
									@Override
									public void widgetSelected(SelectionEvent e) {
										StringBuilder sb = new StringBuilder();
										boolean first = true;
										for (Control c : tip.getChildren()) {
											if (((Button)c).getSelection() == false) continue;
											
											if (!first) sb.append(" ");
											first = false;
											sb.append(((Button)c).getText());
										}
										cli.runInput("show " + sb);
									}
									
									@Override
									public void widgetDefaultSelected(SelectionEvent e) {
									}
								});
							}

							tip.layout(true);
							
							Rectangle rect = b.getBounds();
							Point pt = b.toDisplay(rect.x, rect.y);

							Point size = tip.computeSize(300, SWT.DEFAULT);
							tip.setBounds(pt.x - rect.x, pt.y + 30 - size.y, 300, size.y);
							tip.setVisible(true);
							
						} else if (event.text.equals("help")) {
							cli.setHint("help", "show help for Fin.");
						}
					}
				},
				// mouse out
				new Listener() {
					@Override
					public void handleEvent(Event event) {
						Button b = (Button)event.widget;
						if (event.text.equals("show")) {
							Point pt = b.toDisplay(event.x, event.y);
							if (tip.getBounds().contains(pt)) {
								return;
							} else {
								tip.setVisible(false);
								cli.removeHint();
							}
						} else if (cli.isInHint()) {
							cli.removeHint();
						}
					}
				}, 
				// mouse click
				new Listener() {
					@Override
					public void handleEvent(Event event) {
						if (event.text.equals("add")) {
							cli.setText("add ");
							cli.forceFocus();
						} else if(event.text.equals("show")) {
							cli.runInput("show");							
						} else if (event.text.equals("help")) {
							cli.runInput("help");
						}
					}
				});

		
		Label l = new Label(footer, SWT.NONE);
		l.setText("Fin.");
		l.setFont(new Font(shell.getDisplay(), FinConstants.FOOTER_FONT, FinConstants.DEFAULT_FONTSIZE, SWT.BOLD));

		l.setBounds(5, 5, 30, 20);
	    l.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, true, 1, 1));

		l.setForeground(new Color(null, 255, 255, 255));
		l.setBackgroundImage(newImage);
				
		footer.layout(true);

		return footer;
	}
	
	void initShell(boolean fileLoaded) {
		final Display display = new Display();
		shell = new Shell(display);
		
		context.setDisplay(display);

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
				handerUserInput(event.input);
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
		
		if (!fileLoaded) {
			startTour();
		}


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
		
		if (this.isInTour && finTour != null) {
			boolean toReturn = renderCommandResult(feedback);
			boolean lastStep = finTour.onUserCommand(feedback);
			
			if (lastStep == true) {
				endTour();
			}
			
			return toReturn;
		} else {
			return renderCommandResult(feedback);
		}
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
			if (this.isInTour) {
				endTour();
				runCommandAndRender("show");				
			} else {
				echo("Thank you for using Fin.\n");
				echo("Goodbye!\n");
			}
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
		case ERROR:
			renderErrorResult(cmdRes);
			break;
		
		case TOUR:
			if (cmdRes.getCommand() instanceof TourCommandHandler) {
				startTour();
			} else if(cmdRes.getCommand() instanceof EndTourCommandHandler){
				endTour();
				runCommandAndRender("show");
			}
			break;
		
		case HELPTABLE:
			renderHelpTable(cmdRes);
		
		}

		return false;
	}
	

	@SuppressWarnings("unchecked")
	private void renderHelpTable(CommandResult cmdRes) {
		List<HelpTablePair>helpTable = (List<HelpTablePair>) cmdRes.getReturnObject();
		String echoString = "";
		int optimalBreadth = findOptimalTableBreadth(helpTable);
		
		for (HelpTablePair p: helpTable) {
			echoString+=padWhiteSpace(p.getUsage(), optimalBreadth);
			echoString+=p.getDescription();
			echoString+="\n";
		}
		echo(echoString);
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

	private void startTour() {
		context.setFinApplication(FinApplicationTour.INSTANCE);
		finTour = new FinTour(this, context);
		isInTour = true;
		
		finTour.beginTour();
	}
	
	private void endTour() {
		context.setFinApplication(FinApplication.INSTANCE);
		isInTour = false;
	}

	/**
	 * Renders a string based result (help, joke etc.)
	 */
	private void renderStringResult(CommandResult cmdRes) {
		echo((String) cmdRes.getReturnObject() + "\n");
		refreshContext();
	}

	/**
	 * Renders a error based result. Error uses string)
	 */
	private void renderErrorResult(CommandResult cmdRes) {
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
		if (cmdRes.getCommand() instanceof ShowCommandHandler) {
			updateContext(cmdRes);
			printTaskList();
		} else if (cmdRes.getCommand() instanceof UndeleteCommandHandler){
			String echoString = "";
			@SuppressWarnings("unchecked")
			List<Task >taskList = (List<Task>) cmdRes.getReturnObject();
			if (taskList.size() == 1) {
				echoString += "Task: " + taskList.get(0).getTaskName() + " re-added!\n";
			} else {
				echoString += "Tasks: ";
				for (int i = 0; i < taskList.size();i++) {
					if (i == 0) {
						echoString += (taskList.get(i).getTaskName());
					} else {
						echoString += (", "+ taskList.get(i).getTaskName());
					}

				}
				echoString += " re-added!\n";
			}
			
			
			refreshContext();
			printTaskList();
			echo(echoString);
		}

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
				String filter = context.getFilter();
				runCommandAndRender("show " + lastFilter);
				echo("There are no tasks that matches your filter ("+filter+")\n");
			}
		} else {
			String filter = context.getFilter();
			if (filter.length() > 0) {
				echo(filter);
			}
			cli.addTaskList(newContext);
		}
	}

	private void handerUserInput(String userInput){ 
		if (runCommandAndRender(userInput)) {
			EXIT = true;
		}
	}
	
	/**
	 * Adds a message to the output buffer and refreshes the window.
	 * Refresh is slow so messages should be concatenated before echo
	 * is called to optimize for speed.
	 * 
	 * @param promptMessage
	 */
	@Override
	public void echo(String promptMessage) {
		cli.echo(promptMessage);
		cli.refresh();
	}
	
	@Override
	public void clearScreen() {
		cli.clear();
	}
	
	
	@Override
	public void mainLoop(boolean fileLoaded) {
		initShell(fileLoaded);
	}
}
