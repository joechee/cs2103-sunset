package cs2103.aug11.t11j2.fin.parser;

import java.util.List;

import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;

public class CommandResult {
	public static enum RenderType {
		TASKLIST, TASK, STRING, UNRECOGNIZED_COMMAND, ERROR_INVALID_TASK_INDEX, EXIT, ERROR
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

	CommandResult(ICommandHandler command, String argument,
			RenderType renderType, Object returnObject)
			throws FinProductionException {
		this.command = command;
		this.argument = argument;

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
		
		case ERROR:
			// Ensures that returnObejct is of type String
			if (!(returnObject instanceof String)) {
				throw (new FinProductionException(
						"Command result type is invalid for Error"));
			}

			this.renderType = renderType;
			this.returnObject = returnObject;

			break;
		}
	}

	public final static CommandResult invalidTaskIndex = new CommandResult(
			RenderType.ERROR_INVALID_TASK_INDEX);
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
