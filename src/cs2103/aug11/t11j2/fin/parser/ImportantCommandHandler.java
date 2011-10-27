package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

public class ImportantCommandHandler extends ICommandHandler {
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
				context.getFinApplication().flagTask(i.getUniqId());
			}
			return new CommandResult(this, arguments,
					CommandResult.RenderType.STRING, "Marked all displayed tasks as important!");
		}
		int taskIndex;

		try {
			taskIndex = Integer.parseInt(arguments.split("\\s")[0]);
		} catch (NumberFormatException nfe) {
			return new CommandResult(this, arguments, CommandResult.RenderType.ERROR, "Invalid Task Index!");
		}

		if (taskIndex < 0 || taskIndex > context.getTaskList().size()) {
			return new CommandResult(this, arguments, CommandResult.RenderType.ERROR, "Invalid Task Index!");
		}

		Task todelete = context.getTaskList().get(taskIndex - 1);
		FinApplication.INSTANCE.flagTask(todelete.getUniqId());

		return new CommandResult(this, arguments,
				CommandResult.RenderType.TASK, todelete);
	}



	
	@Override
	public String getHelp() {
		return "important <task>\n\tMarks a task as important";
	}

	@Override
	public CommandResult showHelp() throws FinProductionException {
		String result = getHelp();
		result = result + "\nAliases: ";
		for (String i: getCommandStrings()) {
			result = result + i + ", ";
		}
		result=result.substring(0,result.length()-2);
		return new CommandResult(this, "",
				CommandResult.RenderType.STRING, result);
	}
	
	public HelpTablePair getHelpTablePair() {
		return new HelpTablePair("important <task>", "Marks a task as important");
	}
}
