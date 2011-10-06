package cs2103.aug11.t11j2.fin.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler to mark a task as completed (Fin!) Usage: fin [task number]
 * 
 * @author Koh Zi Chun
 */
public class FinCommandHandler implements ICommandHandler {
	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("fin");
				add("fi");
				add("f");
				add("finito");
				add("finish");
				add("done");
				add("complete");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {

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
		FinApplication.INSTANCE.finTask(todelete.getUniqId());

		return new CommandResult(this, arguments,
				CommandResult.RenderType.Task, todelete);
	}
}
