package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * 
 * @author Joe Chee
 *
 */
public class UnImportantCommandHandler extends ICommandHandler {
	@Override
	@SuppressWarnings("serial")
	List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("unflag");
				add("normal");
				add("norm");
				add("nrml");
				add("n");
				add("ui");
				add("unf");
				add("unfl");
				add("unimpt");
				add("uni");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {
		if (arguments.isEmpty()) {
			return showHelp();
		} else if (arguments.equals("all")) {
			for (Task i: context.getTaskList()) {
				context.getFinApplication().unflagTask(i.getUniqId());
			}
			return new CommandResult(this, arguments,
					CommandResult.RenderType.STRING, "Removed important tag from all tasks!");
		}
		int taskIndex;
		try {
			taskIndex = Integer.parseInt(arguments.split("\\s")[0]);
		} catch (NumberFormatException nfe) {
			return new CommandResult(this, arguments, CommandResult.RenderType.ERROR, "Invalid Task Index!");
		}

		if (taskIndex <= 0 || taskIndex > context.getTaskList().size()) {
			return new CommandResult(this, arguments, CommandResult.RenderType.ERROR, "Invalid Task Index!");
		}

		Task todelete = context.getTaskList().get(taskIndex - 1);
		context.getFinApplication().unflagTask(todelete.getUniqId());

		return new CommandResult(this, arguments,
				CommandResult.RenderType.TASK, todelete);
	}
	



	
	@Override
	public String getHelp() {
		return "unimportant <task>\n\tUnmarks an important task";
	}
	
	@Override
	public HelpTablePair getHelpTablePair() {
		return new HelpTablePair("unimpt <task number>", "Unmarks an important task");
	}

}
