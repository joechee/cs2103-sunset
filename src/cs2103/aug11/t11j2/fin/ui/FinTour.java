package cs2103.aug11.t11j2.fin.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cs2103.aug11.t11j2.fin.application.Fin;
import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.application.Fin.IUserInterface;
import cs2103.aug11.t11j2.fin.parser.AddCommandHandler;
import cs2103.aug11.t11j2.fin.parser.CommandResult;
import cs2103.aug11.t11j2.fin.parser.DeleteCommandHandler;
import cs2103.aug11.t11j2.fin.parser.EditCommandHandler;
import cs2103.aug11.t11j2.fin.parser.FinCommandHandler;
import cs2103.aug11.t11j2.fin.parser.ShowCommandHandler;


public class FinTour extends IFinAutomation {
		
	FinTour(final IUserInterface UI, UIContext context) {
		
		super(UI, context);
				
		// add some default tasks
		Fin.IFinApplication finApplication = this.context.getFinApplication();
		finApplication.clearEnvironment();
		finApplication.add(new Task("Add your First Task! #impt"));
		
		/*
		 * Step 1: Shows tour message
		 * add some task
		 */
		steps.add(new Step() {
			boolean isUserGenerated = false;
			String args = "Complete tour of Fin.";
			@Override
			public void initStep() {
				UI.clearScreen();
				UI.runCommandAndRender("show");
				UI.echo("Welcome to the Fin. guided tour! (you may exit anytime by typing end)");
				UI.echo("Let's get started!");
				UI.echo("Try typing the following: add " + args);
				isUserGenerated = true;
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				final boolean isTaskResult = cmdRes.getRenderType() == CommandResult.RenderType.TASK;
				final boolean isAddCommand =  cmdRes.getCommand() instanceof AddCommandHandler;
				final boolean isMatched = cmdRes.getArgument().equalsIgnoreCase(args);

				
				// add command success
				if (isTaskResult && isAddCommand && isMatched) {
					return nextStep();
				}
				if (isUserGenerated) {
					isUserGenerated = false;
					UI.echo("Come on! Add the task already!");
					UI.echo("Type the following: add " + args);
					isUserGenerated = true;
				}
				return false;
			}
		});
	

		/*
		 * Step 2: Relative dates
		 */
		final DateFormat df = new SimpleDateFormat("d MMM yyyy");
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		steps.add(new Step() {
			boolean isUserGenerated = false;
						
			String args = "Read \"Living in the Airport\" by " + df.format(calendar.getTime());
			@Override
			public void initStep() {
				UI.echo("Simple, no? ");
				UI.echo("Now for a few features of Fin.");
				UI.echo("Type the following line: add " + args);
				isUserGenerated = true;
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				final boolean isTaskResult = cmdRes.getRenderType() == CommandResult.RenderType.TASK;
				final boolean isAddCommand =  cmdRes.getCommand() instanceof AddCommandHandler;
				final boolean isMatched = cmdRes.getArgument().equalsIgnoreCase(args);
				
				// add command success
				if (isTaskResult && isAddCommand && isMatched) {
					return nextStep();
				}
				
				if (isUserGenerated) {
					isUserGenerated = false;
					UI.echo("Go on, type the following line: add " + args);
					isUserGenerated = true;
				}
				return false;
			}
		});
		
		/*
		 * Step 3
		 */
		final DateFormat df2 = new SimpleDateFormat("d MMM");
		calendar.add(Calendar.DAY_OF_YEAR, 8);
		steps.add(new Step() {
			boolean isUserGenerated = false;
			String args = "#fetch Harry from #airport on "+ df2.format(calendar.getTime()) +" #impt";
			@Override
			public void initStep() {
				UI.echo("See what we did there?");
				UI.echo("Fin. intelligently converts dates to make dates more readable.");
				UI.echo("Another tool to help you identify todos is the ability to tag items.");
				UI.echo("Type the following: add " + args);
				isUserGenerated = true;
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				final boolean isTaskResult = cmdRes.getRenderType() == CommandResult.RenderType.TASK;
				final boolean isAddCommand =  cmdRes.getCommand() instanceof AddCommandHandler;
				final boolean isMatched = cmdRes.getArgument().equalsIgnoreCase(args);
				
				// add command success
				if (isTaskResult && isAddCommand && isMatched) {
					return nextStep();
				}
				
				if (isUserGenerated) {
					isUserGenerated = false;
					UI.echo("And the phrase is ... add " + args);
					isUserGenerated = true;
				}
				return false;
			}
		});
		
		/*
		 * Step 4
		 */
		steps.add(new Step() {
			boolean isUserGenerated = false;
			String args = "photo trial at the #airport";
			
			@Override
			public void initStep() {
				UI.echo("Tags help to organize your tasks and have been coloured to make them more identifiable.");
				UI.echo("The #impt tag is a special tag that gives a task higher priority.");
				UI.echo("Thus, while Fin. normally sorts tasks by date, the #impt tag will give a task higher priority.");
				UI.echo("For now, just type the following: add " + args);
				isUserGenerated = true;
			}
			
			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				final boolean isTaskResult = cmdRes.getRenderType() == CommandResult.RenderType.TASK;
				final boolean isAddCommand =  cmdRes.getCommand() instanceof AddCommandHandler;
				final boolean isMatched = cmdRes.getArgument().equalsIgnoreCase(args);
				
				// add command success
				if (isTaskResult && isAddCommand && isMatched) {
					return nextStep();
				}
				
				if (isUserGenerated) {
					isUserGenerated = false;
					UI.echo("\"add " + cmdRes.getArgument() + "\" is not the same as \"add " + args + "\".");
					isUserGenerated = true;
				}
				return false;
			}
		});
		
		/*
		 * Step 5
		 */
		steps.add(new Step() {
			boolean isUserGenerated = false;
			String args = "airport";
			
			@Override
			public void initStep() {
				UI.echo("What's the use of a tag if all it does it look pretty?");
				UI.echo("Quite a lot in fact!");
				UI.echo("Type the following: show " + args);
				isUserGenerated = true;
			}
			
			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				final boolean isCorrectType = cmdRes.getRenderType() == CommandResult.RenderType.TASKLIST;
				final boolean isCorrectCommand = cmdRes.getCommand() instanceof ShowCommandHandler;
				final boolean isMatched = cmdRes.getArgument().equalsIgnoreCase(args);
				
				// add command success
				if (isCorrectType && isCorrectCommand && isMatched) {
					return nextStep();
				}
				
				if (isUserGenerated) {
					isUserGenerated = false;
					UI.echo("Tsk, just type: show " + args + ", already");
					isUserGenerated = true;
				}
				return false;
			}
		});
		
		/*
		 * Step 6
		 */
		steps.add(new Step() {
			boolean isUserGenerated = false;
			String args = "1";
			@Override
			public void initStep() {
				UI.echo("WOAH! How unexpected!");
				UI.echo("The show command searches tasks that have matching arguments.");
				UI.echo("This is handy when you're dealing with many tasks and just want to look at a few");
				UI.echo("You'll also want to clear tasks from your todo list if you've already done them.");
				UI.echo("Type the following: fin " + args);
				isUserGenerated = true;
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				final boolean isCorrectType = cmdRes.getRenderType() == CommandResult.RenderType.TASK;
				final boolean isCorrectCommand = cmdRes.getCommand() instanceof FinCommandHandler;
				final boolean isMatched = cmdRes.getArgument().equalsIgnoreCase(args);
				
				// add command success
				if (isCorrectType && isCorrectCommand && isMatched) {
					return nextStep();
				}
				
				if (isUserGenerated) {
					isUserGenerated = false;
					UI.echo("fin as in sharks fin. It's not that hard! Type: fin " + args);
					isUserGenerated = true;
				}
				return false;
			}
		});
		
		/*
		 * Step 7
		 */
		steps.add(new Step() {
			boolean isUserGenerated = false;
			String args = "fin";
			
			@Override
			public void initStep() {
				UI.echo("Poof! It became CoCo Crunch(TM)!");
				UI.echo("It's not gone for good however.");
				UI.echo("The fin command acts like a tag on the task.");
				UI.echo("Type the following: show " + args);
				isUserGenerated = true;
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				final boolean isCorrectType = cmdRes.getRenderType() == CommandResult.RenderType.TASKLIST;
				final boolean isCorrectCommand = cmdRes.getCommand() instanceof ShowCommandHandler;
				final boolean isMatched = cmdRes.getArgument().equalsIgnoreCase(args);
				
				// add command success
				if (isCorrectType && isCorrectCommand && isMatched) {
					return nextStep();
				}
				
				if (isUserGenerated) {
					isUserGenerated = false;
					UI.echo("It's \"show " + args + "\".");
					isUserGenerated = true;
				}
				return false;
			}
		});
		
		/*
		 * Step 8
		 */
		steps.add(new Step() {
			boolean isUserGenerated = false;
			String args = "";
			
			@Override
			public void initStep() {
				UI.echo("To return to the main task list, just type: show");
				isUserGenerated = true;
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				final boolean isCorrectType = cmdRes.getRenderType() == CommandResult.RenderType.TASKLIST;
				final boolean isCorrectCommand = cmdRes.getCommand() instanceof ShowCommandHandler;
				final boolean isMatched = cmdRes.getArgument().equalsIgnoreCase(args);
				
				// add command success
				if (isCorrectType && isCorrectCommand && isMatched) {
					return nextStep();
				}
				
				if (isUserGenerated) {
					isUserGenerated = false;
					UI.echo("It's not that hard to just type \"show\".");
					isUserGenerated = true;
				}
				return false;
			}
		});
		
		/*
		 * Step 9
		 */
		steps.add(new Step() {
			boolean isUserGenerated = false;
			String args = "1";
			
			@Override
			public void initStep() {
				UI.runCommandAndRender("add The British are coming #impt by today");
				UI.echo("Oh no! Where did this message come from! It must be a bug!");
				UI.echo("Squash the bug! Delete this task by typing: del " + args);
				isUserGenerated = true;
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				final boolean isCorrectType = cmdRes.getRenderType() == CommandResult.RenderType.TASK;
				final boolean isCorrectCommand = cmdRes.getCommand() instanceof DeleteCommandHandler;
				final boolean isMatched = cmdRes.getArgument().equalsIgnoreCase(args);
				
				// add command success
				if (isCorrectType && isCorrectCommand && isMatched) {
					return nextStep();
				}
				
				if (isUserGenerated) {
					isUserGenerated = false;
					UI.echo("D-E-L-E-T-E it: del " + args);
					isUserGenerated = true;
				}
				return false;
			}
		});
		
		/*
		 * Step 10
		 */
		steps.add(new Step() {
			boolean isUserGenerated = false;
			String args = "4 to photo trail at the #airport";
			
			@Override
			public void initStep() {
				UI.echo("The menace has been vanquished. Peace reigns once more..");
				UI.echo("Unfortunately for you, there are THINGS TO DO!");
				UI.echo("While deleting a task and typing it in all over again may sound pleasant to some, it's not the way we roll.");
				UI.echo("**--GRAMMAR SENSES TINGLING--**");
				UI.echo("That's terrible. It seems you've made a typo in task 4 and now it's up to YOU to fix it!!");
				UI.echo("Type: edit " + args);
				isUserGenerated = true;
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				final boolean isCorrectType = cmdRes.getRenderType() == CommandResult.RenderType.TASK;
				final boolean isCorrectCommand = cmdRes.getCommand() instanceof EditCommandHandler;
				final boolean isMatched = cmdRes.getArgument().equalsIgnoreCase(args);
				
				// add command success
				if (isCorrectType && isCorrectCommand && isMatched) {
					return nextStep();
				}
				
				if (isUserGenerated) {
					isUserGenerated = false;
					UI.echo("The command is: edit " + args);
					isUserGenerated = true;
				}
				return false;
			}
		});
		
		/*
		 * Final Step: Help
		 */
		steps.add(new Step() {
			@Override
			public void initStep() {
				UI.echo("Whew. That was close.");
				UI.echo("That about wraps up our tour. However, remember that you can always take this tour again!");
				UI.echo("When in doubt, DON'T PANIC. There is always the \"help\" command to save the day.");
				UI.echo("We hope you've enjoyed touring Fin.");
				UI.echo("To end this tour, type: end");
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				return false;
			}
		});


	}
	
}
