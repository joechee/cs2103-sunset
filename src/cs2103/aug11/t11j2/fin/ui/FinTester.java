package cs2103.aug11.t11j2.fin.ui;

import java.util.LinkedList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.Fin.IUserInterface;
import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.parser.AddCommandHandler;
import cs2103.aug11.t11j2.fin.parser.CommandResult;
import cs2103.aug11.t11j2.fin.parser.DeleteCommandHandler;
import cs2103.aug11.t11j2.fin.parser.FinCommandHandler;
import cs2103.aug11.t11j2.fin.parser.ImportantCommandHandler;
import cs2103.aug11.t11j2.fin.parser.ShowCommandHandler;
import cs2103.aug11.t11j2.fin.parser.UndeleteCommandHandler;
/**
 * 
 * @author Wei Jing
 *
 */
public class FinTester extends IFinAutomation {
	private int errors = 0;
	private List<String> errorList = null;
	
	FinTester(final IUserInterface UI, UIContext context) {
		super(UI, context);
		context.getFinApplication().clearEnvironment();
	
		errors = 0;
		errorList = new LinkedList<String>();
		
		final List<String> tasksToAdd = new LinkedList<String>();
		tasksToAdd.add("add this is a random #impt task");
		tasksToAdd.add("add #cs2103 party due tmrw");
		tasksToAdd.add("add #cs2103 #presentation due today");
		tasksToAdd.add("add #cs4232 midterms on 3 december");
		tasksToAdd.add("add #cs4232 #presentation by next wed");
		tasksToAdd.add("add cs4232 this is something random by 25 dec 2011");
		tasksToAdd.add("add cs2103 this is something random too by next monday");
		tasksToAdd.add("add cs2103 cs4232 random too by next monday");
		
		for (final String s : tasksToAdd) {
			// add the task
			steps.add(new Step() {
				@Override
				public void initStep() {
					UI.runCommandAndRender(s);
				}

				@Override
				public boolean onUserAction(CommandResult cmdRes) {
					if (!(cmdRes.getCommand() instanceof AddCommandHandler)) {
						addError("Running the command " + s + " does not result in a AddCommandHandler command being returned");
					} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASK) {
						addError("Running the command " + s + " does not result in a TASK RenderType being returned");
					}
					return false;
				}
			});

			// run show after adding each task
			steps.add(new Step() {
				@Override
				public void initStep() {
					UI.runCommandAndRender("show");
				}

				@Override
				public boolean onUserAction(CommandResult cmdRes) { 
					Integer count = tasksToAdd.indexOf(s) + 1;
					if (!(cmdRes.getCommand() instanceof ShowCommandHandler)) {
						addError("Running the command show after \"" + s + "\" does not result in a ShowCommandHandler command being returned");
					} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASKLIST) {
						addError("Running the command show after \"" + s + "\" does not result in a TASKLIST RenderType being returned");
					} else {
						@SuppressWarnings("unchecked")
						List<Task> taskList = (List<Task>)cmdRes.getReturnObject();
						if (taskList.size() != count) {
							addError("Running the command show after \"" + s + "\"" +
									" does not result in a a list of Tasks of size "+ count.toString());
						}
					}
					return false;
				}
			});
		}
		

		steps.add(new Step() {
			// delete 1, which will be the random but impt task
			@Override
			public void initStep() {
				UI.runCommandAndRender("delete 1");
			}
			@Override
			public boolean onUserAction(CommandResult cmdRes) { 
				if (!(cmdRes.getCommand() instanceof DeleteCommandHandler)) {
					addError("Running the command delete 1 does not result in a DeleteCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASK) {
					addError("Running the command delete 1 does not result in a TASK RenderType being returned");
				}
				return false;
			}
		});

		// run show after deleting the task
		steps.add(new Step() {
			@Override
			public void initStep() {
				UI.runCommandAndRender("show");
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) { 
				Integer count = tasksToAdd.size() - 1;
				if (!(cmdRes.getCommand() instanceof ShowCommandHandler)) {
					addError("Running the command show after delete 1 does not result in a ShowCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASKLIST) {
					addError("Running the command show after delete 1 does not result in a TASKLIST RenderType being returned");
				} else {
					@SuppressWarnings("unchecked")
					List<Task> taskList = (List<Task>)cmdRes.getReturnObject();
					if (taskList.size() != count) {
						addError("Running the command show after delete 1" +
								" does not result in a a list of Tasks of size "+ count.toString());
					}
				}
				return false;
			}
		});
		
		// run show #cs4232
		steps.add(new Step() {
			final String command = "show #cs4232";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) { 
				Integer count = 2;
				if (!(cmdRes.getCommand() instanceof ShowCommandHandler)) {
					addError("Running the command "+command+" does not result in a ShowCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASKLIST) {
					addError("Running the command "+command+" does not result in a TASKLIST RenderType being returned");
				} else {
					@SuppressWarnings("unchecked")
					List<Task> taskList = (List<Task>)cmdRes.getReturnObject();
					if (taskList.size() != count) {
						addError("Running the command " + command +
								" does not result in a a list of Tasks of size "+ count.toString());
					}
				}
				return false;
			}
		});

		// run show presentation #cs2103
		steps.add(new Step() {
			final String command = "show presentation #cs2103";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) { 
				Integer count = 1;
				if (!(cmdRes.getCommand() instanceof ShowCommandHandler)) {
					addError("Running the command "+command+" does not result in a ShowCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASKLIST) {
					addError("Running the command "+command+" does not result in a TASKLIST RenderType being returned");
				} else {
					@SuppressWarnings("unchecked")
					List<Task> taskList = (List<Task>)cmdRes.getReturnObject();
					if (taskList.size() != count) {
						addError("Running the command " + command +
								" does not result in a a list of Tasks of size "+ count.toString());
					}
				}
				return false;
			}
		});

		// run show #cs4232 #presentation
		steps.add(new Step() {
			final String command = "show #cs4232 #presentation";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) { 
				Integer count = 1;
				if (!(cmdRes.getCommand() instanceof ShowCommandHandler)) {
					addError("Running the command "+command+" does not result in a ShowCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASKLIST) {
					addError("Running the command "+command+" does not result in a TASKLIST RenderType being returned");
				} else {
					@SuppressWarnings("unchecked")
					List<Task> taskList = (List<Task>)cmdRes.getReturnObject();
					if (taskList.size() != count) {
						addError("Running the command " + command +
								" does not result in a a list of Tasks of size "+ count.toString());
					}
				}
				return false;
			}
		});

		// run show cs4232
		steps.add(new Step() {
			final String command = "show cs4232";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) { 
				Integer count = 4;
				if (!(cmdRes.getCommand() instanceof ShowCommandHandler)) {
					addError("Running the command "+command+" does not result in a ShowCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASKLIST) {
					addError("Running the command "+command+" does not result in a TASKLIST RenderType being returned");
				} else {
					@SuppressWarnings("unchecked")
					List<Task> taskList = (List<Task>)cmdRes.getReturnObject();
					if (taskList.size() != count) {
						addError("Running the command " + command +
								" does not result in a a list of Tasks of size "+ count.toString());
					}
				}
				return false;
			}
		});

		// run impt
		steps.add(new Step() {
			final String command = "impt 2";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) { 
				if (!(cmdRes.getCommand() instanceof ImportantCommandHandler)) {
					addError("Running the command "+command+" does not result in a ImportantCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASK) {
					addError("Running the command "+command+" does not result in a TASK RenderType being returned");
				} else {
					Task t = (Task)cmdRes.getReturnObject();
					if (t.isImportant() != true) {
						addError("Running the command "+command+" does not result in a task being marked as impt");
					}
				}
				return false;
			}
		});

		// run show cs4232 present
		steps.add(new Step() {
			final String command = "show cs4232 random";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) { 
				Integer count = 2;
				if (!(cmdRes.getCommand() instanceof ShowCommandHandler)) {
					addError("Running the command "+command+" does not result in a ShowCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASKLIST) {
					addError("Running the command "+command+" does not result in a TASKLIST RenderType being returned");
				} else {
					@SuppressWarnings("unchecked")
					List<Task> taskList = (List<Task>)cmdRes.getReturnObject();
					if (taskList.size() != count) {
						addError("Running the command " + command +
								" does not result in a a list of Tasks of size "+ count.toString());
					}
				}
				return false;
			}
		});

		// run impt all
		steps.add(new Step() {
			final String command = "impt all";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) { 
				if (!(cmdRes.getCommand() instanceof ImportantCommandHandler)) {
					addError("Running the command "+command+" does not result in a ImportantCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.STRING) {
					addError("Running the command "+command+" does not result in a STRING RenderType being returned");
				}
				return false;
			}
		});
		
		// run show all again
		steps.add(new Step() {
			final String command = "show";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) { 
				Integer count = tasksToAdd.size() - 1;
				if (!(cmdRes.getCommand() instanceof ShowCommandHandler)) {
					addError("Running the command "+command+" does not result in a ShowCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASKLIST) {
					addError("Running the command "+command+" does not result in a TASKLIST RenderType being returned");
				} else {
					@SuppressWarnings("unchecked")
					List<Task> taskList = (List<Task>)cmdRes.getReturnObject();
					if (taskList.size() != count) {
						addError("Running the command " + command +
								" does not result in a list of Tasks of size "+ count.toString());
					}
					
					int cnt = 0;
					for (Task t : taskList) {
						if (t.isImportant()) ++cnt;
					}
					if (cnt != 3) {
						addError("Running the command " + command +
								" does not result in a list of Tasks with 3 important tasks");						
					}
				}
				return false;
			}
		});

		// run deletes
		steps.add(new Step() {
			final String command = "delete 4";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) { 
				if (!(cmdRes.getCommand() instanceof DeleteCommandHandler)) {
					addError("Running the command "+command+" does not result in a DeleteCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASK) {
					addError("Running the command "+command+" does not result in a TASK RenderType being returned");
				} 
				return false;
			}
		});
		steps.add(new Step() {
			final String command = "delete 4";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) { 
				if (!(cmdRes.getCommand() instanceof DeleteCommandHandler)) {
					addError("Running the command "+command+" does not result in a DeleteCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASK) {
					addError("Running the command "+command+" does not result in a TASK RenderType being returned");
				} 
				return false;
			}
		});

		// run delete all
		steps.add(new Step() {
			final String command = "delete all";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				Integer count = 0;
				if (!(cmdRes.getCommand() instanceof ShowCommandHandler)) {
					addError("Running the command "+command+" does not result in a ShowCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASKLIST) {
					addError("Running the command "+command+" does not result in a TASKLIST RenderType being returned");
				} else {
					@SuppressWarnings("unchecked")
					List<Task> taskList = (List<Task>)cmdRes.getReturnObject();
					if (taskList.size() != count) {
						addError("Running the command " + command +
								" does not result in a list of Tasks of size "+ count.toString());
					}
				}

				return false;
			}
		});

		// run undelete
		steps.add(new Step() {
			final String command = "undelete";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				Integer count = 5;
				if (!(cmdRes.getCommand() instanceof UndeleteCommandHandler)) {
					addError("Running the command "+command+" does not result in a DeleteAllCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASKLIST) {
					addError("Running the command "+command+" does not result in a TASKLIST RenderType being returned");
				} else {
					@SuppressWarnings("unchecked")
					List<Task> taskList = (List<Task>)cmdRes.getReturnObject();
					if (taskList.size() != count) {
						addError("Running the command " + command +
								" does not result in a list of Tasks of size "+ count.toString());
					}
				}
				return false;
			}
		});

		// run undelete again
		steps.add(new Step() {
			final String command = "undelete";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				Integer count = 1;
				if (!(cmdRes.getCommand() instanceof UndeleteCommandHandler)) {
					addError("Running the command "+command+" does not result in a DeleteAllCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASKLIST) {
					addError("Running the command "+command+" does not result in a TASKLIST RenderType being returned");
				} else {
					@SuppressWarnings("unchecked")
					List<Task> taskList = (List<Task>)cmdRes.getReturnObject();
					if (taskList.size() != count) {
						addError("Running the command " + command +
								" does not result in a list of Tasks of size "+ count.toString());
					}
				}
				return false;
			}
		});

		
		// run fin
		steps.add(new Step() {
			final String command = "fin 1";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				if (!(cmdRes.getCommand() instanceof FinCommandHandler)) {
					addError("Running the command "+command+" does not result in a FinCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASK) {
					addError("Running the command "+command+" does not result in a Task RenderType being returned");
				} else {
					Task task = (Task)cmdRes.getReturnObject();
					if (task.isFin() != true) {
						addError("Running the command " + command +
								" does not result a finished task");
					}
				}
				return false;
			}
		});

		// run fin
		steps.add(new Step() {
			final String command = "fin 2";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				if (!(cmdRes.getCommand() instanceof FinCommandHandler)) {
					addError("Running the command "+command+" does not result in a FinCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASK) {
					addError("Running the command "+command+" does not result in a Task RenderType being returned");
				} else {
					Task task = (Task)cmdRes.getReturnObject();
					if (task.isFin() != true) {
						addError("Running the command " + command +
								" does not result a finished task");
					}
				}
				return false;
			}
		});

		// run fin
		steps.add(new Step() {
			final String command = "fin 4";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				if (!(cmdRes.getCommand() instanceof FinCommandHandler)) {
					addError("Running the command "+command+" does not result in a FinCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASK) {
					addError("Running the command "+command+" does not result in a Task RenderType being returned");
				} else {
					Task task = (Task)cmdRes.getReturnObject();
					if (task.isFin() != true) {
						addError("Running the command " + command +
								" does not result a finished task");
					}
				}
				return false;
			}
		});

		// run show fin
		steps.add(new Step() {
			final String command = "show fin";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) { 
				Integer count = 3;
				if (!(cmdRes.getCommand() instanceof ShowCommandHandler)) {
					addError("Running the command "+command+" does not result in a ShowCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASKLIST) {
					addError("Running the command "+command+" does not result in a TASKLIST RenderType being returned");
				} else {
					@SuppressWarnings("unchecked")
					List<Task> taskList = (List<Task>)cmdRes.getReturnObject();
					if (taskList.size() != count) {
						addError("Running the command " + command +
								" does not result in a list of Tasks of size "+ count.toString());
					}
				}
				return false;
			}
		});

		// run show #impt #fin
		steps.add(new Step() {
			final String command = "show #impt #fin";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				Integer count = 2;
				if (!(cmdRes.getCommand() instanceof ShowCommandHandler)) {
					addError("Running the command "+command+" does not result in a ShowCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASKLIST) {
					addError("Running the command "+command+" does not result in a TASKLIST RenderType being returned");
				} else {
					@SuppressWarnings("unchecked")
					List<Task> taskList = (List<Task>)cmdRes.getReturnObject();
					if (taskList.size() != count) {
						addError("Running the command " + command +
								" does not result in a list of Tasks of size "+ count.toString());
					}
				}
				return false;
			}
		});

		// run show all again
		steps.add(new Step() {
			final String command = "show";
			@Override
			public void initStep() {
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) { 
				Integer count = tasksToAdd.size() - 5;
				if (!(cmdRes.getCommand() instanceof ShowCommandHandler)) {
					addError("Running the command "+command+" does not result in a ShowCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASKLIST) {
					addError("Running the command "+command+" does not result in a TASKLIST RenderType being returned");
				} else {
					@SuppressWarnings("unchecked")
					List<Task> taskList = (List<Task>)cmdRes.getReturnObject();
					if (taskList.size() != count) {
						addError("Running the command " + command +
								" does not result in a list of Tasks of size "+ count.toString());
					}
				}
				return false;
			}
		});
		
		
		steps.add(new Step() {
			final String command = "add "+ FinConstants.DUEDATE_PLACEHOLDER;
			@Override
			public void initStep() {
				
				UI.runCommandAndRender(command);
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				if (!(cmdRes.getCommand() instanceof AddCommandHandler)) {
					addError("Running the command "+command+" does not result in a AddCommandHandler command being returned");
				} else if(cmdRes.getRenderType() != CommandResult.RenderType.TASK) {
					addError("Running the command "+command+" does not result in a TASK RenderType being returned");
				} else {
					Task task = (Task) cmdRes.getReturnObject();
					if (!task.toString().equals(FinConstants.DUEDATE_PLACEHOLDER)) {
						addError("Characters in Task are not escaped properly!");
					}
				}
				return false;
			}
		});
		
		steps.add(new Step() {
			@Override
			public void initStep() {
				UI.echo("Test has concluded");
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				return true;
			}
		});
	}

	protected void addError(String string) {
		this.errors++;
		this.errorList.add(string);
	}
	
	public int getNumberOfErrors() {
		return this.errors;
	}
	
	public List<String> getErrors() {
		return this.errorList;
	}

	
}
