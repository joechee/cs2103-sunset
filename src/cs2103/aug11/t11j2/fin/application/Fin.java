package cs2103.aug11.t11j2.fin.application;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.ui.*;

/**
 * Fin is the bootstrapper which handles input from the command line \\
 * and decided which User Interface to evoke upon starting Fin.
 * 
 * Usage:
 * Fin -ui ui -file filename
 * Arguments can be swapped around.
 * @author Joe Chee
 */

public class Fin {
	
	/* Constants */

	public static final Fin.IUserInterface DEFAULT_UI = new GUI();
	public final static String DEFAULT_FILENAME = "fin.yaml";
	public static final String fileExtension = ".yaml";
	
	public interface IUserInterface {
		void mainLoop();
	}
	
	public interface IFinApplication {
		public void add(Task task);
		public List<Task> getTasksWithTag(String tag);
		public List<Task> getTasks();
		public List<Task> getTasksWithoutTag(String tag);
		public List<Task> getTasksWithoutTags(List<String> tags);
		public List<Task> getTasksWithTags(List<String> tags);
		public boolean deleteTask(UUID taskUID);
		public boolean flagTask(UUID taskUID);
		public boolean unflagTask(UUID taskUID);
		public boolean finTask(UUID taskUID);
		public boolean unfinTask(UUID taskUID);
		public void loadEnvironment(String filename) throws IOException;
		public List<String> getHashTags();
	}

	public static void main(String[] args) {
		try {
			parseArgs(args);
		} catch (IllegalArgumentException e) {
			System.out.print(e.getMessage());
		} catch (IOException e) {
			System.out.print(e.getMessage());
			e.printStackTrace();
		}

	}

	private static void parseArgs(String[] args)
			throws IllegalArgumentException, IOException {
		assert (args != null);
		IUserInterface UI = DEFAULT_UI;
		String filename = DEFAULT_FILENAME;
		
		for (int i = 0; i < args.length; i++) {
			args[i] = args[i].toLowerCase();
		
			if ((args[i].equals("-ui")) && (i + 1 < args.length)) {
				UI = parseUI(args[i + 1]);
			} else if ((args[i].equals("-file")) && (i + 1 < args.length)) {
				filename = args[i + 1];
			}
		}
		
		FinApplication.INSTANCE.loadEnvironment(checkFilename(filename));
		UI.mainLoop();
	}

	private static String checkFilename(String i)
			throws IllegalArgumentException {
		if (i == null) {
			throw new IllegalArgumentException("Invalid Input!");
		} else if (!i.endsWith(".yaml")) {
			return i + fileExtension;
		}
		return i;
	}

	private static IUserInterface parseUI(String UIString)
			throws IllegalArgumentException {
		
		if (UIString.equals("CLI")) {
			return (IUserInterface) new CLI();
		} else if (UIString.equals("GUI")) {
			return (IUserInterface) new GUI();
		} else if (UIString.equals("NUI")) {
			return (IUserInterface) null;
		} else {
			throw new IllegalArgumentException("UI not recognised!");
		}
	}
}
