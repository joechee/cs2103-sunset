package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler for "add" command Usage: add [task] Parses given task and
 * throws it into Fin environment
 * 
 * @author Koh Zi Chun
 * 
 */
public class AddCommandHandler implements ICommandHandler {

	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("a");
				add("ad");
				add("add");
				add("+");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {
		
		
		if (arguments.trim().length() == 0) {
			return showHelp();
		} else {
			try {
				Task newtask = new Task(arguments);
				FinApplication.INSTANCE.add(newtask);
				return new CommandResult(this, arguments,
						CommandResult.RenderType.Task, newtask);
			} catch (Exception IllegalArgumentException) {
				return new CommandResult(this,arguments,
						CommandResult.RenderType.Error,"Illegal placeholder used!");
			}
		}
	}
	


	@Override
	public String showAbridgedHelp() {
		return "add <task>\t\t\t\tAdds <task> to your task list";
	}

	@Override
	public CommandResult showHelp() throws FinProductionException {
		String result = "add <task>\n\tAdds <task> to your task list";
		return new CommandResult(this, "",
				CommandResult.RenderType.String, result);		
	}
}
