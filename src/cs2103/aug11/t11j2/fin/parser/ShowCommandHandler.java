package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler for the "show tasks" command Usage: show [filter]
 * 
 * @author Koh Zi Chun
 */
public class ShowCommandHandler extends ICommandHandler {

	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("s");
				add("sh");
				add("sho");
				add("show");
				add("ls");
				add("list");
				add("view");
				add("v");
				add("vi");
				add("vie");
				add("viwe");
				
			}
		};
	}

	private static List<String> tokenize(String filters) {
		List<String> tokenList = new ArrayList<String>();
		String[] tokens = filters.trim().split("\\s");
		for (String token : tokens) {
			tokenList.add(token);
		}
		return tokenList;
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {
		
		List<Task> tasks = null;
		if (arguments.trim().length() == 0) {
			tasks = FinApplication.INSTANCE.getTasksWithoutTags(Arrays.asList(FinConstants.FIN_HASH_TAG));
		} else {
			tasks = FinApplication.INSTANCE.getTasksWithTags(tokenize(arguments));
		}

		return new CommandResult(this, arguments,
				CommandResult.RenderType.TaskList, tasks);
	}
	
	@Override
	public String getAbridgedHelp() {
		return "show <filters>\t\t\t\tShows the task list with <filters>. Examples of <filters> can be \"tasks due on Friday\"";
	}
	
	@Override
	public String getHelp() {
		return "show <filters>\n\tShows the task list with <filters>. Examples of <filters> can be \"tasks due on Friday\"";
	}

}
