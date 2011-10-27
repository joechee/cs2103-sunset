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
				add("search");
				add("sear");
				add("lookfor");
				add("lookup");
				add("check");
				add("find");
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
	
	public static List<Task> filterTasksWithPatterns(List<Task>tasks, List<String> filters){
		List <String> patterns = filters;
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
		
		List<Task> tasks = null;
		final List<Task> unfinishedTasks = context.getFinApplication().getTasksWithoutTags(Arrays.asList(FinConstants.FIN_HASH_TAG)); 
		if (arguments.trim().length() == 0) {
			tasks = unfinishedTasks;
			return new CommandResult(this, arguments,
					CommandResult.RenderType.TASKLIST, tasks);
		}
		
		
		List<String> tokenizedArguments = tokenize(arguments);
		removeTagChar(tokenizedArguments);
		
		tasks = FinApplication.INSTANCE.getTasksWithTags(tokenizedArguments);
		
		List<Task> searchTasks = unfinishedTasks;
		searchTasks = filterTasksWithPatterns(searchTasks, tokenizedArguments);
		for (Task i: searchTasks) {
			if (!tasks.contains(i)) {
				tasks.add(i);
			}
		}
		return new CommandResult(this, arguments,
				CommandResult.RenderType.TASKLIST, tasks);

	}
	
	private void removeTagChar(List<String> args) {
		for (int i = 0; i < args.size();i++) {
			if (args.isEmpty() && args.get(i).startsWith(" ")){
				args.set(i, args.get(i).substring(1));
			}
		}
	}

	@Override
	public String getAbridgedHelp() {
		return "show <params>\t\t\t\tShows all tasks with <params> within the description. Results with the corresponding tag is returned first.";
	}
	
	@Override
	public String getHelp() {
		return "show <params>\n\tShows all tasks with <params> within the description. Results with the corresponding tag is returned first. To show tags only, type show #<tag> instead of show <tag>";
	}

	@Override
	public String autoComplete(String fullCommand, String command, String arguments, UIContext context) {
		StringBuilder sb = new StringBuilder();

		// if last typed character is a whitespace, do not try to autocomplete
		if (Character.isWhitespace(fullCommand.charAt(fullCommand.length()-1))) {
			return null;
		}
		
		// get the last typed filter
		for (int i=arguments.length()-1;i>=0;--i) {
			if (Character.isLetterOrDigit(arguments.charAt(i))) {
				sb.append(Character.toLowerCase(arguments.charAt(i)));
			} else {
				break;
			}
		}
		
		// if last typed filter is nothing ignore
		if (sb.length() == 0) {
			return null;
		}
		
		sb.reverse();
		
		String lastToken = sb.toString();
		
		List<String> hashTags = context.getFinApplication().getHashTags();
		for (String s : hashTags) {
			if (s.startsWith(lastToken)) {
				return fullCommand.replaceAll(lastToken+"$", "") + s;
			}
		}
		
		return null;
	}
	
	public HelpTablePair getNewHelp() {
		return new HelpTablePair("show <params>", "Shows all tasks with <params> within the description. Results with the corresponding tag is returned first.");
	}
}
