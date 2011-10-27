package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

public class UndeleteCommandHandler extends ICommandHandler{

	@SuppressWarnings("serial")
	@Override
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("undelete");
				add("undel");
				add("unde");
				add("und");
				add("unrm");
				add("unrem");
				add("unremove");
				add("ud");
			}
		};
	}

	@Override
	CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {
		List<Task> tasksRegenerated = FinApplication.INSTANCE.undelete();
		if (tasksRegenerated == null) {
			return new CommandResult(this, arguments,
					CommandResult.RenderType.ERROR,"No task to undelete!");
		}
		
		for (Task task: tasksRegenerated) {
		}
		return new CommandResult(this, arguments,
				CommandResult.RenderType.TASKLIST, tasksRegenerated);
		
	}

	@Override
	public String getAbridgedHelp() {
		return "undelete \t\t\tUndoes your last delete action";
	}
	
	@Override
	public String getHelp() {
		return "undelete \n\tUndoes your last delete action";
	}
	
	public HelpTablePair getNewHelp() {
		return new HelpTablePair("undelete", "Undoes your last delete action");
	}
	
	
	

}
