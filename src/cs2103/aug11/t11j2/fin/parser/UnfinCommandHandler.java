package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler to mark a task as uncompleted (unFin :() Usage: unfin [task number]
 * 
 * @author Koh Zi Chun
 */
public class UnfinCommandHandler extends ICommandHandler {
	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("unfin");
				add("uf");
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
				FinApplication.INSTANCE.unfinTask(i.getUniqId());
			}
			return new CommandResult(this, arguments,
					CommandResult.RenderType.STRING, "Removed the finished tag from all displayed tasks!");
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

		FinApplication.INSTANCE.unfinTask(todelete.getUniqId());

		return new CommandResult(this, arguments,
				CommandResult.RenderType.TASK, todelete);
	}

	@Override
	public String getAbridgedHelp() {
		return "unfin <task number>\t\t\tUnmarks the completed task";
	}
	
	@Override
	public String getHelp() {
		return "unfin <task number>\n\tUnmarks the completed task";
	}


}
