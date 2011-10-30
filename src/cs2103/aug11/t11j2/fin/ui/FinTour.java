package cs2103.aug11.t11j2.fin.ui;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.Fin;
import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.application.Fin.IUserInterface;
import cs2103.aug11.t11j2.fin.parser.AddCommandHandler;
import cs2103.aug11.t11j2.fin.parser.CommandResult;
import cs2103.aug11.t11j2.fin.parser.DeleteCommandHandler;
import cs2103.aug11.t11j2.fin.parser.EditCommandHandler;
import cs2103.aug11.t11j2.fin.parser.FinCommandHandler;
import cs2103.aug11.t11j2.fin.parser.ShowCommandHandler;


public class FinTour {
	
	interface Step {
		// this will initialize the step by augmenting the environment
		void initStep();
		
		// upon any user command, this will decide whether to proceed to the next step
		// if it returns true, it means it's the final step
		boolean onUserAction(CommandResult cmdRes) ;
	}
	
	private int currentStep = 0;
	private List<Step> tourSteps = new ArrayList<Step>();
	
	private UIContext context = null;
	private IUserInterface UI = null;
	
	FinTour(final IUserInterface UI, UIContext context) {
		this.currentStep = 0;
		this.context = context;
		this.UI = UI;
		
		// add some default tasks
		Fin.IFinApplication finApplication = this.context.getFinApplication();
		finApplication.clearEnvironment();
		finApplication.add(new Task("Add your First Task! #impt"));
		
		/*
		 * Step 1: Shows tour message
		 * add some task
		 */
		tourSteps.add(new Step() {
			boolean isUserGenerated = false;
			String args = "Complete tour of Fin.";
			@Override
			public void initStep() {
				UI.clearScreen();
				UI.runCommandAndRender("show");
				UI.echo("Welcome to the Fin. guided tour!");
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
					UI.echo("Come on! Add a task already!");
					UI.echo("Type the following: add " + args);
					isUserGenerated = true;
				}
				return false;
			}
		});
	
		/*
		 * Step 2: Relative dates
		 */
		tourSteps.add(new Step() {
			boolean isUserGenerated = false;
			String args = "Read \"Living in the Airport\" by 1 Nov 2011";
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
		tourSteps.add(new Step() {
			boolean isUserGenerated = false;
			String args = "#fetch Harry from #airport on 9 Nov #impt";
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
		tourSteps.add(new Step() {
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
		tourSteps.add(new Step() {
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
		tourSteps.add(new Step() {
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
		tourSteps.add(new Step() {
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
		tourSteps.add(new Step() {
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
		tourSteps.add(new Step() {
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
		tourSteps.add(new Step() {
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
		tourSteps.add(new Step() {
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
				return true;
			}
		});


	}

	private boolean nextStep() {
		this.currentStep++;
		
		if (this.currentStep >= tourSteps.size()) {
			return true;
		} else {
			Step currentStep = tourSteps.get(this.currentStep);
			currentStep.initStep();
						
			return (this.currentStep + 1 == tourSteps.size());
		}
	}
	
	public void beginTour() {
		this.currentStep = -1;
		nextStep();
	}
	
	public boolean onUserCommand(CommandResult cmdRes) {
		if (this.currentStep > tourSteps.size()) {
			return false;
		}
		
		Step currentStep = tourSteps.get(this.currentStep);
		return currentStep.onUserAction(cmdRes);
	}
}
