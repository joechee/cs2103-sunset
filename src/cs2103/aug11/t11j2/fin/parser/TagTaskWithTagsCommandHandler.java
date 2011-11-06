package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler for "tag" command Usage: tag [task index] [tags]
 * adds the [tags] to the index-th task. eg: tag 7 cs math.
 */
public class TagTaskWithTagsCommandHandler extends ICommandHandler {

	@Override
	@SuppressWarnings("serial")
	List<String> getCommandStrings() {
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
	CommandResult executeCommands(String command, String arguments,
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

		if (taskIndex <= 0 || taskIndex > context.getTaskList().size()) {
			return new CommandResult(this, arguments, CommandResult.RenderType.ERROR, "Invalid Task Index!");
		}
		Task task = context.getTaskList().get(taskIndex - 1);
		
		int numOfTags = tokens.length;
		for (int i=1; i<numOfTags; i++)
			context.getFinApplication().addTag(task,tokens[i]);
		
		return new CommandResult(this, arguments,
				CommandResult.RenderType.TASK, task);
	}
	

	
	@Override
	public String getHelp() {
		return "tag <task number> <tag>\n\tTags a <task> with <tag>";
	}
	
	public HelpTablePair getHelpTablePair() {
		return new HelpTablePair("tag <task number> <tag>", "Tags a <task> with <tag>");
	}
	
	
	

}
