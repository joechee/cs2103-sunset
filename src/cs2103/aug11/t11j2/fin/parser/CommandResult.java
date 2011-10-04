package cs2103.aug11.t11j2.fin.parser;

import java.util.List;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;

public class CommandResult {
	public static enum RenderType {
		TaskList, Task, String, UnrecognizedCommand
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
		this.renderType = renderType;
		this.returnObject = false;
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
		case TaskList:
			// Ensures that returnObject is of type List<String>
			if (!(returnObject instanceof List<?>)
					|| (((List<?>) returnObject).size() > 0 && !(((List<?>) returnObject)
							.get(0) instanceof String))) {
				throw (new FinProductionException(
						"Command result type is invalid for TaskList"));
			}

			this.renderType = renderType;
			this.returnObject = returnObject;

			break;

		case String:
			// Ensures that returnObject is of type String
			if (!(returnObject instanceof String)) {
				throw (new FinProductionException(
						"Command result type is invalid for String"));
			}

			this.renderType = renderType;
			this.returnObject = returnObject;

			break;

		}
	}

	public static CommandResult unrecognizedCommandResult = new CommandResult(
			RenderType.UnrecognizedCommand);

}
