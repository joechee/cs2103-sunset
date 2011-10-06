package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.parseTask.TaskParser;
import cs2103.aug11.t11j2.fin.parser.CommandResult.RenderType;
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
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {
		Task newtask = TaskParser.parse(arguments);
		FinApplication.INSTANCE.add(newtask);
		FinApplication.INSTANCE.saveEnvironment();

		return new CommandResult(this, arguments,
				CommandResult.RenderType.Task, newtask);
	}
}
