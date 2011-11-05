package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

/**
 * CommandHandler for "add" command Usage: add [task] Parses given task and
 * throws it into Fin environment
 * 
 * @author Koh Zi Chun
 * 
 */
public class AddCommandHandler extends ICommandHandler {
	
	@Override
	@SuppressWarnings("serial")
	List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("a");
				add("ad");
				add("add");
				add("+");
			}
		};
	}

	@Override
	public CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {
		
		
		if (arguments.trim().length() == 0) {
			return showHelp();
		} else {
			Task newtask = new Task(arguments);
			context.getFinApplication().add(newtask,true);
			return new CommandResult(this, arguments,
					CommandResult.RenderType.TASK, newtask);
		}
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
			if (Character.isLetterOrDigit(arguments.charAt(i)) || arguments.charAt(i) == FinConstants.HASH_TAG_CHAR) {
				sb.append(Character.toLowerCase(arguments.charAt(i)));
			} else {
				break;
			}
		}
		
		// if last typed filter is nothing ignore or first char is not #
		if (sb.length() == 0 || sb.charAt(sb.length()-1) != FinConstants.HASH_TAG_CHAR) {
			return null;
		}
		
		sb.deleteCharAt(sb.length() - 1);
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



	@Override
	public String getHelp() {
		return "add <task>\n\tAdds <task> to your task list";
	}
	
	public HelpTablePair getHelpTablePair() {
		return new HelpTablePair("add <task>", "Adds <task> to your task list");
	}
}
