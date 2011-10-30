package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler for "Untag" command Usage: untag [task index] [tags]
 * removes the [tags] from the index-th task. eg: untag 7 cs math.
 */
public class UntagTaskWithTagsCommandHandler extends ICommandHandler {

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
			return new CommandResult(this, arguments, CommandResult.RenderType.ERROR, "Invalid Task Index!");
		}

		if (taskIndex < 0 || taskIndex > context.getTaskList().size()) {
			return new CommandResult(this, arguments, CommandResult.RenderType.ERROR, "Invalid Task Index!");
		}
		Task task = context.getTaskList().get(taskIndex - 1);
		
		int numOfTags = tokens.length;
		for (int i=1; i<numOfTags; i++)
			context.getFinApplication().removeTag(task,tokens[i]);

		return new CommandResult(this, arguments,
				CommandResult.RenderType.TASK, task);
	}


	@Override
	public String getHelp() {
		return "untag <task number> <tag>\n\tUntags a <task> with <tag>";
	}
	
	@Override
	public HelpTablePair getHelpTablePair() {
		return new HelpTablePair("untag <task number> <tag>", "Untags the <task> with <tag>");
	}


}
