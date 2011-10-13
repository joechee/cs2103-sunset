package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler for "Untag" command Usage: untag [task index] [tags]
 * removes the [tags] from the index-th task. eg: untag 7 cs math.
 */
public class UntagTaskWithTagsCommandHandler implements ICommandHandler {

	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("untag");
				add("untags");
				add("deletetag");
				add("deletetags");
				add("deltags");
				add("deltag");
				add("dtag");
				add("utag");
				add("dt");
				add("ut");
				add("delt");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {
		if (arguments.isEmpty()) {
			return showHelp();
		}
		int taskIndex;
		String[] tokens = arguments.trim().split(" ");

		try {
			taskIndex = Integer.parseInt(tokens[0]);
		} catch (NumberFormatException nfe) {
			return CommandResult.invalidTaskIndex;
		}

		if (taskIndex < 0 || taskIndex > context.getTaskList().size()) {
			return CommandResult.invalidTaskIndex;
		}
		Task task = context.getTaskList().get(taskIndex - 1);
		
		int numOfTags = tokens.length;
		for (int i=1; i<numOfTags; i++)
			task.removeTag(tokens[i]);

		return new CommandResult(this, arguments,
				CommandResult.RenderType.Task, task);
	}


	@Override
	public String showAbridgedHelp() {
		return "untag <task number> <tag>\t\tUntags a <task> with <tag>";
	}

	@Override
	public CommandResult showHelp() throws FinProductionException {
		final String result = "untag <task number> <tag>\n\tUntags a <task> with <tag>";
		return new CommandResult(this, "",
				CommandResult.RenderType.String, result);
	}
}
