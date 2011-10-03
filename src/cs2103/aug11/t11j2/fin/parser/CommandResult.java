package cs2103.aug11.t11j2.fin.parser;

import java.util.List;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;

public class CommandResult {
	public static enum RenderType {
		TaskList, String, UnrecognizedCommand
	};
	
	private RenderType renderType;
	private Object returnObject;
	
	public RenderType getRenderType() {
		return renderType;
	}
	public Object getReturnObject() {
		return returnObject;
	}

	CommandResult(RenderType renderType, Object returnObject) throws FinProductionException {
		switch (renderType) {
		case TaskList:
			// Ensures that returnObject is of type List<String>
			if (!(returnObject instanceof List<?>) ||
				(((List<?>)returnObject).size() > 0 &&
				!(((List<?>)returnObject).get(0) instanceof String))) {
					throw(new FinProductionException("Command result type is invalid for TaskList"));
			}
			
			this.renderType = renderType;
			this.returnObject = returnObject;
			
			break;
			
		case String:
			// Ensure that returnObject is of type String
			if (!(returnObject instanceof String)) {
				throw(new FinProductionException("Command result type is invalid for String"));
			}
			
			this.renderType = renderType;
			this.returnObject = returnObject;
			
			break;
			
		}
	}
	
	private CommandResult(RenderType renderType) {
		this.renderType = renderType;
		this.returnObject = false;
	}
	
	public static CommandResult unrecognizedCommandResult = new CommandResult(RenderType.UnrecognizedCommand); 
	
}
