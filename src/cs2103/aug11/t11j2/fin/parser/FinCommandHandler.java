package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler to mark a task as completed (Fin!) Usage: fin [task number]
 * 
 * @author Koh Zi Chun
 */
public class FinCommandHandler extends ICommandHandler {
	@Override
	@SuppressWarnings("serial")
	List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("fin");
				add("fi");
				add("f");
				add("finito");
				add("finish");
				add("done");
				add("complete");
				add("-");
				add("x");
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
				context.getFinApplication().finTask(i.getUniqId());
			}
			return new CommandResult(this, arguments,
					CommandResult.RenderType.STRING, "Finished all tasks!");
		}

		int taskIndex;
		try {
			taskIndex = Integer.parseInt(arguments.split("\\s+")[0]);
		} catch (NumberFormatException nfe) {
			return new CommandResult(this, arguments, CommandResult.RenderType.ERROR, "Invalid Task Index!");
		}

		if (taskIndex <= 0 || taskIndex > context.getTaskList().size()) {
			return new CommandResult(this, arguments, CommandResult.RenderType.ERROR, "Invalid Task Index!");
		}

		Task todelete = context.getTaskList().get(taskIndex - 1);
		context.getFinApplication().finTask(todelete.getUniqId());

		return new CommandResult(this, arguments,
				CommandResult.RenderType.TASK, todelete);
	}
	

	
	@Override
	public String getHelp() {
		return "fin <task number>\n\tMarks a task as completed";
	}
	
	@Override
	public HelpTablePair getHelpTablePair() {
		return new HelpTablePair("fin <task number>", "Marks a task as completed (fin)");
	}

}
