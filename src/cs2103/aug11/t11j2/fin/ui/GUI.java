package cs2103.aug11.t11j2.fin.ui;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import cs2103.aug11.t11j2.fin.application.Fin.IUserInterface;
import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.application.FinApplicationSandbox;
import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.gui.FinCLIComposite;
import cs2103.aug11.t11j2.fin.gui.FinCLIInputEvent;
import cs2103.aug11.t11j2.fin.gui.FinCLIInputListener;
import cs2103.aug11.t11j2.fin.gui.FinFooter;
import cs2103.aug11.t11j2.fin.gui.TaskStyledText;
import cs2103.aug11.t11j2.fin.parser.AddCommandHandler;
import cs2103.aug11.t11j2.fin.parser.CommandParser;
import cs2103.aug11.t11j2.fin.parser.CommandResult;
import cs2103.aug11.t11j2.fin.parser.DeleteAllCommandHandler;
import cs2103.aug11.t11j2.fin.parser.DeleteCommandHandler;
import cs2103.aug11.t11j2.fin.parser.EditCommandHandler;
import cs2103.aug11.t11j2.fin.parser.EndTourCommandHandler;
import cs2103.aug11.t11j2.fin.parser.HelpCommandHandler;
import cs2103.aug11.t11j2.fin.parser.HelpTablePair;
import cs2103.aug11.t11j2.fin.parser.ICommandHandler;
import cs2103.aug11.t11j2.fin.parser.ShowCommandHandler;
import cs2103.aug11.t11j2.fin.parser.TourCommandHandler;
import cs2103.aug11.t11j2.fin.parser.UndeleteCommandHandler;
/**
 * The GUI front-end for Fin.
 * 
 * @author Joe Chee
 *
 */

public class GUI implements IUserInterface {
	// Logger
	private Logger logger = Logger.getLogger(this.getClass());
	
	private static boolean EXIT = false;
	
	// shell for SWT
	static Shell shell = null;
	static FinCLIComposite cli;
	
	final private UIContext context = new UIContext(FinApplication.INSTANCE);
	
	private IFinAutomation finTour = null;
	private boolean isInTour = false;
	
	// Help table constant
	private static final int TABLE_BORDER_WIDTH = 3;
	
	// Label
	private Label finFooterLabel = null;
	
	// Footer
	private Composite footer;
	
	public GUI() {
		logger.debug("GUI object created");
	}

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
		Color skyBlue = new Color(null, 43, 72, 153);
		Color navyBlue = new Color(null, 36, 64, 128);
		gc.setForeground(skyBlue);
		gc.setBackground(navyBlue);
		gc.fillGradientRectangle(0, 0, 1, 30, true);
		gc.dispose();
		footer.setBackgroundImage(newImage);

		final Shell tip = new Shell(shell.getShell(), SWT.TOOL | SWT.ON_TOP | SWT.NO_FOCUS );
		tip.setVisible(false);
		tip.setLayout(new RowLayout());
		tip.setBackground (new Color(null, FinConstants.CLI_FOREGROUND_COLOR));
		
		
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

		new FinFooter(footer, cli, 
				// mouse over
				new Listener() {
					@Override
					public void handleEvent(Event event) {
						Button b = (Button)event.widget;
						if (event.text.equals("add")) {
							HelpTablePair addHelp = new AddCommandHandler().getHelpTablePair();
							cli.setHint(addHelp.getUsage(), addHelp.getDescription());	
						} else if(event.text.equals("show")) {
							HelpTablePair showHelp = new ShowCommandHandler().getHelpTablePair();
							cli.setHint(showHelp.getUsage(), showHelp.getDescription());	

							for (Control c : tip.getChildren()) {
								c.dispose();
							}
							
							List<String> hashTags = context.getFinApplication().getHashTags();
							String filter = " " + context.getFilter() + " ";

							for (final String ht : hashTags) {
								final Button hashButton = new Button(tip, SWT.TOGGLE | SWT.FLAT);
								hashButton.setText("#" + ht);
								
								if (filter.contains(" " + ht + " " ) || filter.contains(" #" + ht + " " )) {
									hashButton.setSelection(true);
								}
								hashButton.setBackground (new Color(null, FinConstants.CLI_FOREGROUND_COLOR));
								
								// when user clicks the button
								hashButton.addSelectionListener(new SelectionListener() {
									@Override
									public void widgetSelected(SelectionEvent e) {
										StringBuilder sb = new StringBuilder();
										boolean first = true;
										// get list of controls that user have selected
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
								
								hashButton.addPaintListener(new PaintListener() {
									@Override
									public void paintControl(PaintEvent e) {
										
										int width = hashButton.getSize().x;
										int height = hashButton.getSize().y;
										
										if (hashButton.getSelection() == true) {
											e.gc.setBackground(new Color(null, FinConstants.BLACK_COLOR));
										} else {
											e.gc.setBackground(new Color(null, FinConstants.DARKGRAY_COLOR));
										}
										e.gc.fillRoundRectangle(0, 0, width, height, 5, 5);

//										e.gc.setForeground(new Color(null, FinConstants.FOREGROUND_COLOR));
//										e.gc.drawRoundRectangle(0, 0, width-1, height-1, 8, 8);

										float[] rgb = TaskStyledText.generateColor("#"+ht);
										e.gc.setForeground(new Color(null, (int)(rgb[0]*255), (int)(rgb[1]*255), (int)(rgb[2]*255)));
										e.gc.drawText("#" + ht, 5, 5);
									}		
								});
								hashButton.redraw();
							}
							
							if (hashTags.size() > 0) {
								tip.layout(true);
								
								Rectangle rect = b.getBounds();
								Point pt = b.toDisplay(rect.x, rect.y);
	
								Point size = tip.computeSize(300, SWT.DEFAULT);
								tip.setBounds(pt.x - rect.x, pt.y + 5 - size.y, 300, size.y);
								tip.setVisible(true);
							}
							
						} else if (event.text.equals("help")) {
							HelpTablePair helpHelp = new HelpCommandHandler().getHelpTablePair();
							cli.setHint(helpHelp.getUsage(), helpHelp.getDescription());	
						}
					}
				},
				// mouse out
				new Listener() {
					@Override
					public void handleEvent(Event event) {
						Button b = (Button)event.widget;
						if (event.text.equals("show")) {
							Control over = tip.getDisplay().getCursorControl();
							for (Control c : tip.getChildren()) {
								if (c.equals(over)) return;
							}
							
							if (tip.equals(over) || b.equals(over)) {
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
							tip.setVisible(false);
							for (Control c : tip.getChildren()) {
								c.dispose();
							}
							cli.runInput("show");	
						} else if (event.text.equals("help")) {
							cli.runInput("help");
						}
					}
				});

		
		finFooterLabel = new Label(footer, SWT.NONE);
		finFooterLabel.setText("Fin.");
		finFooterLabel.setFont(new Font(shell.getDisplay(), FinConstants.FOOTER_FONT, FinConstants.DEFAULT_FONTSIZE, SWT.BOLD));

		finFooterLabel.setBounds(5, 5, 30, 20);
	    finFooterLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, true, 1, 1));

		finFooterLabel.setForeground(new Color(null, 255, 255, 255));
		finFooterLabel.setBackgroundImage(newImage);
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
		
		footer = createFooter(shell);
				

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
		logger.debug("Running command: "+userArgs);
		CommandResult feedback = null;
		feedback = runCommand(userArgs);
		boolean toReturn;

		if (this.isInTour && finTour != null) {
			toReturn = renderCommandResult(feedback);
			if (this.isInTour) {
				boolean lastStep = finTour.onUserCommand(feedback);
				
				if (lastStep == true) {
					endTour();
				}				
			}
		} else {
			toReturn = renderCommandResult(feedback);
		}
		return toReturn;
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
		} catch (NullPointerException e) {
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
				break;
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
			}
			break;

		case TEST:
			startAutomatedTest();
			break;
		
		case HELPTABLE:
			renderHelpTable(cmdRes);
			break;
			
		case NULL:
			if (cmdRes.getCommand() instanceof DeleteAllCommandHandler) {
				runCommandAndRender("show");
			}
		}
		
		return false;
	}
	
	private void startAutomatedTest() {
		assert(shell != null);

		FinApplicationSandbox.INSTANCE.clearEnvironment();
		context.setFinApplication(FinApplicationSandbox.INSTANCE);
		finTour = new FinTester(this, context);

		isInTour = true;
		
		final int time=0;
		Runnable timer = new Runnable() {
			public void run() {
				boolean notEnded = finTour.nextStep();
				if (notEnded == false) {
					shell.getDisplay().timerExec(time, this);
				} else {
					stopAutomatedTest();
				}
			}
		};
		
		finFooterLabel.setText("Fin. Automated Test");
		footer.layout(true);
		
		shell.getDisplay().timerExec(time, timer);
		finTour.beginStep();
	}
	
	private void stopAutomatedTest() {
		context.setFinApplication(FinApplication.INSTANCE);
		isInTour = false;

		finFooterLabel.setText("Fin.");
		footer.layout(true);
		
		this.runCommandAndRender("show");
		
		if (finTour instanceof FinTester) {
			int errors = ((FinTester)finTour).getNumberOfErrors();
			if (errors == 0) {
				echo("Test has completed and all test cases passed!");
			} else {
				StringBuilder sb = new StringBuilder();
				
				sb.append("Test has completed with the following errors: \n");
				for (String s : ((FinTester)finTour).getErrors()) {
					sb.append(" - ");
					sb.append(s);
					sb.append("\n");
				}
				
				echo(sb.toString());
			}
		}
	}
	

	@SuppressWarnings("unchecked")
	private void renderHelpTable(CommandResult cmdRes) {
		List<HelpTablePair>helpTable = (List<HelpTablePair>) cmdRes.getReturnObject();

		StringBuilder echoString = new StringBuilder();
		int optimalBreadth = findOptimalTableBreadth(helpTable);
		
		for (HelpTablePair p: helpTable) {
			echoString.append(padWhiteSpace(p.getUsage(), optimalBreadth));
			echoString.append(p.getDescription());
			echoString.append("\n");
		}
		
		echo(echoString.toString());
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
	
	/**
	 * Pads white space to the end of a string until it reaches the length
	 * @param usage
	 * @param length
	 * @return string of length length with spaces appended to it
	 */
	private String padWhiteSpace(String usage, int length) {
		StringBuilder sb = new StringBuilder(usage);
		while (sb.length()<length) {
			sb.append(" ");
		}
		return sb.toString();
	}

	private void startTour() {
		context.setFinApplication(FinApplicationSandbox.INSTANCE);
		finTour = new FinTour(this, context);
		isInTour = true;
		finTour.beginStep();
		finFooterLabel.setText("Fin. Tour");
		footer.layout(true);
	}
	
	private void endTour() {
		context.setFinApplication(FinApplication.INSTANCE);
		isInTour = false;
		finFooterLabel.setText("Fin.");
		footer.layout(true);
		shell.update();
		runCommandAndRender("show");

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
		logger.info("User evoked: "+userInput);
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

	public void tourStepEcho(String promptMessage) {
		cli.echoWithColor(promptMessage, new Color(null, FinConstants.TOUR_STEP_COLOR));
		cli.refresh();
	}
	
	public void clearScreen() {
		cli.clear();
	}
		
	@Override
	public void initUI(boolean fileLoaded) {
		initShell(fileLoaded);
	}
}
