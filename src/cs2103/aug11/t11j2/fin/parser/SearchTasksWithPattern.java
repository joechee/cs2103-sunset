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
public class SearchTasksWithPattern {
	
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
	
	public static List<Task> filterTasksWithPatterns(List<Tasks>tasks, String filters){
		List <String> patterns = tokenize(filters);
		List <Task> tasksAfterFilter = new ArrayList<Task>();
		int numOfTasks = tasks.size();
		for (int i=0; i<numOfTasks; i++)
			filterd[i] = false;
		int numOfPatterns = patterns.size();
		for (int i=0; i<numOfTasks; i++){
			boolean filtered = false; //whether the task[i] is filtered out;
			String taskString = tasks[i].toString().toUpperCase();
			for (int j=0; j<numOfPatterns; j++){
				if (taskString.indexOf(pattern[j].toUpperCase())==-1){
					filtered = true;
				}
			}
			if(!filtered){
				tasksAfterFilter.add(tasks[i]);
			}
		}
		return tasksAfterFilter;		
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {

		List<Task> tasks = null;
		tasks = FinApplication.INSTANCE.getTasks();
		if (arguments.trim().length() == 0) {
			return tasks;
		}
		tasks = filterTasksWithPatterns(tasks, arguments.trim());
		return new CommandResult(this, arguments,
				CommandResult.RenderType.TaskList, tasks);
	}

}


}
