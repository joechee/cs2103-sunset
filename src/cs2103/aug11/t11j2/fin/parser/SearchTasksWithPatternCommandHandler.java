package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler for the "search tasks" command Usage: search [filters]
 * 
 */
public class SearchTasksWithPatternCommandHandler implements ICommandHandler {
	
	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("find");
				add("search");
				add("sear");
				add("lookfor");
				add("lookup");
				add("check");
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
	
	public static List<Task> filterTasksWithPatterns(List<Task>tasks, String filters){
		List <String> patterns = tokenize(filters);
		List <Task> tasksAfterFilter = new ArrayList<Task>();
		int numOfTasks = tasks.size();
		int numOfPatterns = patterns.size();
		for (int i=0; i<numOfTasks; i++){
			boolean filtered = false; //whether the task[i] is filtered out;
			String taskString = tasks.get(i).toString().toUpperCase();
			for (int j=0; j<numOfPatterns; j++){
				if (taskString.indexOf(patterns.get(j).toUpperCase())==-1){
					filtered = true;
					break;
				}
			}
			if(!filtered){
				tasksAfterFilter.add(tasks.get(i));
			}
		}
		return tasksAfterFilter;		
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {
		if (arguments.isEmpty()) {
			return showHelp();
		}
		List<Task> tasks = null;
		tasks = FinApplication.INSTANCE.getTasks();
		String contextFilter = context.getFilter();
		String filters = arguments.trim() + " " + contextFilter;
		tasks = filterTasksWithPatterns(tasks, filters);
		return new CommandResult(this, filters,
				CommandResult.RenderType.TaskList, tasks);
	}
	

	@Override
	public String showAbridgedHelp() {
		return "search <patterns>\t\t\tSearch the tasks containing the (all) the <patterns>";
	}

	@Override
	public CommandResult showHelp() throws FinProductionException {
		// TODO Auto-generated method stub
		final String result = "search <patterns>\n\tSearch the tasks containing the (all) the <patterns>";
		return new CommandResult(this, "",
				CommandResult.RenderType.String, result);
	}

}
