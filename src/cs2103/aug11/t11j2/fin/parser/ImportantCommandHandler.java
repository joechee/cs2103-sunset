package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

public class ImportantCommandHandler implements ICommandHandler {
	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("important");
				add("impt");
				add("i");
				add("im");
				add("flag");
				add("!");
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
				FinApplication.INSTANCE.flagTask(i.getUniqId());
			}
			return new CommandResult(this, arguments,
					CommandResult.RenderType.String, "Marked all displayed tasks as important!");
		}
		int taskIndex;

		try {
			taskIndex = Integer.parseInt(arguments.split("\\s")[0]);
		} catch (NumberFormatException nfe) {
			return CommandResult.invalidTaskIndex;
		}

		if (taskIndex < 0 || taskIndex > context.getTaskList().size()) {
			return CommandResult.invalidTaskIndex;
		}

		Task todelete = context.getTaskList().get(taskIndex - 1);
		FinApplication.INSTANCE.flagTask(todelete.getUniqId());

		return new CommandResult(this, arguments,
				CommandResult.RenderType.Task, todelete);
	}


	@Override
	public String showAbridgedHelp() {
		return "important\t\t\t\tMarks a task as important";
	}

	@Override
	public CommandResult showHelp() throws FinProductionException {
		// TODO Auto-generated method stub
		final String result = "important <task>\n\tMarks a task as important";
		return new CommandResult(this, "",
				CommandResult.RenderType.String, result);
	}
}
