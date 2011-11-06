package cs2103.aug11.t11j2.fin.ui;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.Fin.IUserInterface;
import cs2103.aug11.t11j2.fin.parser.CommandResult;

/**
 * 
 * @author Wei Jing
 *
 */
public abstract class IFinAutomation {
	interface Step {
		// this will initialize the step by augmenting the environment
		void initStep();
		
		// upon any user command, this will decide whether to proceed to the next step
		// if it returns true, it means it's the final step
		boolean onUserAction(CommandResult cmdRes) ;
	}

	protected UIContext context = null;
	protected IUserInterface UI = null;
	
	IFinAutomation(final IUserInterface UI, UIContext context) {
		this.context = context;
		this.UI = UI;
		
		this.currentStep = 0;
	}
	
	IFinAutomation() {
		
	}

	
	protected int currentStep = 0;
	protected List<Step> steps = new ArrayList<Step>();
	
	public boolean nextStep() {
		this.currentStep++;
		if (this.currentStep >= steps.size()) {
			return true;
		} else {
			Step currentStep = steps.get(this.currentStep);
			currentStep.initStep();
						
			return false;
		}
	}

	public void beginStep() {
		this.currentStep = -1;
		nextStep();
	}

	public boolean onUserCommand(CommandResult cmdRes) {
		if (this.currentStep > steps.size()) {
			return true;
		}
				
		Step currentStep = steps.get(this.currentStep);
		return currentStep.onUserAction(cmdRes);
	}

}
