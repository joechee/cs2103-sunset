package cs2103.aug11.t11j2.fin.parser;

import java.util.List;

public class CommandResult {
	public static enum RenderType {
		TaskList, String
	};
	
	private RenderType renderType;
	private Object returnObject;
	
	public RenderType getRenderType() {
		return renderType;
	}
	public Object getReturnObject() {
		return returnObject;
	}

	CommandResult(RenderType renderType, Object returnObject) throws Exception {
		switch (renderType) {
		case TaskList:
			// Ensures that returnObject is of type List<String>
			if (!(returnObject instanceof List<?>) ||
				(((List<?>)returnObject).size() > 0 &&
				!(((List<?>)returnObject).get(0) instanceof String))) {
					throw(new Exception("Command result type is invalid for TaskList"));
			}
			
			this.renderType = renderType;
			this.returnObject = returnObject;
			
			break;
			
		case String:
			// Ensure that returnObject is of type String
			if (!(returnObject instanceof String)) {
				throw(new Exception("Command result type is invalid for String"));
			}
			
			this.renderType = renderType;
			this.returnObject = returnObject;
			
			break;
		}
	}
	
}
