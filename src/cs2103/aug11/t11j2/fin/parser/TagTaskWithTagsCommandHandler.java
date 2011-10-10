package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler for "tag" command Usage: tag [task index] [tags]
 * adds the [tags] to the index-th task. eg: tag 7 cs math.
 */
public class TagTaskWithTagsCommandHandler implements CommandParser.ICommandHandler {

	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("addtag");
				add("addtags");
				add("atag");
				add("addt");
				add("tag");
				add("moretag");
				add("retag");
				add("t");
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
		
		int numOfTags = tokens.length;
		for (int i=1; i<numOfTags; i++)
			task.addTag(tokens[i]);
		
		
		return new CommandResult(this, arguments,
				CommandResult.RenderType.Task, task);
	}
}
