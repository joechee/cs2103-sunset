package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler for "Untag" command Usage: untag [task index] [tags]
 * removes the [tags] from the index-th task. eg: untag 7 cs math.
 */
public class UntagTaskWithTagsCommandHandler implements CommandParser.ICommandHandler {

	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("untag");
				add("deletetag");
				add("deltag");
				add("dtag");
				add("utag");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {

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
		FinApplication.INSTANCE.deleteTask(task.getUniqId());
		
		int numOfTags = tokens.length;
		for (int i=1; i<numOfTags; i++)
			task.removeTag(tokens[i]);
		
		FinApplication.INSTANCE.add(task);		
		
		return new CommandResult(this, arguments,
				CommandResult.RenderType.Task, task);
	}
}
