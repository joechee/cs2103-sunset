package cs2103.aug11.t11j2.fin.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.Fin;
import cs2103.aug11.t11j2.fin.application.Fin.IUserInterface;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.parser.AddCommandHandler;
import cs2103.aug11.t11j2.fin.parser.CommandResult;
import cs2103.aug11.t11j2.fin.parser.DeleteCommandHandler;
import cs2103.aug11.t11j2.fin.parser.ICommandHandler;

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
		finApplication.add(new Task("Do this by tomorrow"));
		finApplication.add(new Task("Do that in 10 days"));
		finApplication.add(new Task("Do this #impt by tomorrow"));
		
		/*
		 * Step 1: Shows tour message
		 * add some task
		 */
		tourSteps.add(new Step() {
			@Override
			public void initStep() {
				UI.clearScreen();
				UI.runCommandAndRender("show");
				UI.echo("Welcome to the Fin. guided tour!\n");
				UI.echo("Try adding some task dude\n");
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				final boolean isTaskResult = cmdRes.getRenderType() == CommandResult.RenderType.TASK;
				final boolean isAddCommand =  cmdRes.getCommand() instanceof AddCommandHandler;

				
				// add command success
				if (isTaskResult && isAddCommand) {
					return nextStep();
				}
				return false;
			}
		});
	
		/*
		 * Step 2: delete task
		 */
		tourSteps.add(new Step() {
			@Override
			public void initStep() {
				UI.echo("Good! you learn how to add task!\n");
				UI.echo("Now learn how to delete some task");
			}

			@Override
			public boolean onUserAction(CommandResult cmdRes) {
				final boolean isTaskResult = cmdRes.getRenderType() == CommandResult.RenderType.TASK;
				final boolean isDeleteCommand =  cmdRes.getCommand() instanceof DeleteCommandHandler;
				
				// add command success
				if (isTaskResult && isDeleteCommand) {
					return nextStep();
				}
				return false;
			}
		});

		/*
		 * Final Step
		 */
		tourSteps.add(new Step() {
			@Override
			public void initStep() {
				UI.clearScreen();
				UI.echo("You have learnt the basics of Fin!\n");
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
