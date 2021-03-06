package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler for the "undelete" command.
 * 
 * Usage: undelete 
 * Restores last deleted Task and returns the updated Task list to the Fin environment.
 * 
 * @author Wei Jing
 */
public class UndeleteCommandHandler extends ICommandHandler{

	@SuppressWarnings("serial")
	@Override
	List<String> getCommandStrings() {
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
				add("undo");
			}
		};
	}

	@Override
	CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {
		List<Task> tasksRegenerated = context.getFinApplication().undelete();
		if (tasksRegenerated == null) {
			return new CommandResult(this, arguments,
					CommandResult.RenderType.ERROR,"No task to undelete!");
		}
		
		return new CommandResult(this, arguments,
				CommandResult.RenderType.TASKLIST, tasksRegenerated);
		
	}


	
	@Override
	public String getHelp() {
		return "undelete \n\tUndoes your last delete action";
	}
	
	public HelpTablePair getHelpTablePair() {
		return new HelpTablePair("undelete", "Undoes your last delete action");
	}
	
	
	

}
