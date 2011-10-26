package cs2103.aug11.t11j2.fin.parser;

import java.util.List;

import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;

/**
 * A CommandResult object is what is returned by the commandparser to the UI
 * after every user-input. It has a list of rendertypes that describe what
 * the attached object's structure is like.
 * 
 * The UI is not obligated to implement rendering for all of the rendertypes,
 * and can choose to implement the rendering for a subset of them. This allows
 * renderTypes to be extended without breaking an old UI (just that the old
 * UI will not be able to render newer specifications)
 * 
 * @author Koh Zi Chun
 * @author Joe Chee
 */
public class CommandResult {
	public static enum RenderType {
		TASKLIST, TASK, STRING, UNRECOGNIZED_COMMAND, EXIT, TOUR, ERROR
	};

	private RenderType renderType;
	private Object returnObject;
	private ICommandHandler command;
	private String argument;

	public RenderType getRenderType() {
		return renderType;
	}

	public Object getReturnObject() {
		return returnObject;
	}

	private CommandResult(RenderType renderType) {
		this(renderType,"");
	}
	
	private CommandResult(RenderType renderType, String argument) {
		this.renderType = renderType;
		this.returnObject = false;
		this.argument = argument;
	}

	public ICommandHandler getCommand() {
		return command;
	}

	public String getArgument() {
		return argument;
	}

	/**
	 * The CommandResult object will contain the commandHandler
	 * that is used to execute the user's inputted command,
	 * the user's argument, the render type and an attached object.
	 * 
	 * @param command
	 * @param argument
	 * @param renderType
	 * @param returnObject
	 * @throws FinProductionException
	 */
	CommandResult(ICommandHandler command, String argument,
			RenderType renderType, Object returnObject)
			throws FinProductionException {
		this.command = command;
		this.argument = argument;

		// the constructure ensures that the object given conforms to a strict
		// structure for a given renderType. For example, the object for the
		// rendertype of TASKLIST must be a list of tasks.
		
		switch (renderType) {
		case TASKLIST:
			// Ensures that returnObject is of type List<Task>
			if (!(returnObject instanceof List<?>)
					|| (((List<?>) returnObject).size() > 0 && !(((List<?>) returnObject)
							.get(0) instanceof Task))) {
				throw (new FinProductionException(
						"Command result type is invalid for TaskList"));
			}

			this.renderType = renderType;
			this.returnObject = returnObject;

			break;

		case STRING:
			// Ensures that returnObject is of type String
			if (!(returnObject instanceof String)) {
				throw (new FinProductionException(
						"Command result type is invalid for String"));
			}

			this.renderType = renderType;
			this.returnObject = returnObject;

			break;

		case TASK:
			// Ensures that returnObject is of type Task
			if (!(returnObject instanceof Task)) {
				throw (new FinProductionException(
						"Command result type is invalid for Task"));
			}

			this.renderType = renderType;
			this.returnObject = returnObject;

			break;
		
		case TOUR:
			this.renderType = renderType;
			
			break;
		}
	}

	public final static CommandResult invalidTaskIndex = new CommandResult(
			CommandResult.RenderType.ERROR, "Invalid Task Index!");
	public final static CommandResult exitCommandResult = new CommandResult(
			RenderType.EXIT);

	public static CommandResult unrecognizedCommand(String command) {
		return new CommandResult(
				RenderType.UNRECOGNIZED_COMMAND, command);
	}
	
	void setCommand(ICommandHandler command) {
		this.command = command;
	}

}
