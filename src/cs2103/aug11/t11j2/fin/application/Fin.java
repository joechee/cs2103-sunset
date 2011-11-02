package cs2103.aug11.t11j2.fin.application;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cs2103.aug11.t11j2.fin.ui.CLI;
import cs2103.aug11.t11j2.fin.ui.GUI;

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

	public static Fin.IUserInterface DEFAULT_UI = null;
	public final static String DEFAULT_FILENAME = "fin.yaml";
	public static final String fileExtension = ".yaml";
	private static final String className = "cs2103.aug11.t11j2.fin.application";
	
	private static Logger logger;
	
	private static void initializeConstants() {
		DEFAULT_UI = new GUI();
	}

	
	
	
	public interface IUserInterface {
		/**
		 * Initializes the UI. Control is passed from 
		 * bootstrapper to UI and the work of the bootstrapper is done.
		 * 
		 * @param fileLoaded whether the bootstrapper found a pre-existing 
		 * yamp savefile and manages to load it successfully. Useful for
		 * starting a tour for first time User
		 * 
		 */
		void initUI(boolean fileLoaded);
		/**
		 * Executes and command and renders it on the UI
		 * 
		 * @param userArgs the command to run
		 * @return true if the application wants to exit
		 */
		boolean runCommandAndRender(String userArgs);
		/**
		 * Echos a message to the UI
		 * @param promptMessage
		 */
		void echo(String promptMessage);
		/**
		 * Clears the output of the UI. May not be applicable to some UI
		 */
		void clearScreen();
	}
	
	public interface IFinApplication {
		public void add(Task task);
		public List<Task> getTasksWithTag(String tag);
		public List<Task> getTasks();
		public List<Task> getTasksWithoutTag(String tag);
		public List<Task> getTasksWithoutTags(List<String> tags);
		public List<Task> getTasksWithTags(List<String> tags);
		public boolean deleteTask(UUID taskUID);
		public boolean deleteTasks(Collection<UUID> toDeleteUUID);
		public boolean flagTask(UUID taskUID);
		public boolean unflagTask(UUID taskUID);
		public boolean finTask(UUID taskUID);
		public boolean unfinTask(UUID taskUID);
		public boolean loadEnvironment(String filename) throws IOException;
		public void clearEnvironment();	
		public List<String> getHashTags();
		public void editTask(Task task, String string);
		public void addTag(Task task, String string);
		public void removeTag(Task task, String string);
		public void removeDueDate(Task task);
		public void setDueDate(Task task, String string);
		public List<Task> undelete() ;
	}

	public static void main(String[] args) {
		try {
			initializeLogger();
			initializeConstants();
			parseArgs(args);
		} catch (IllegalArgumentException e) {
			System.out.print(e.getMessage());
		} catch (IOException e) {
			System.out.print(e.getMessage());
			e.printStackTrace();
		} 
		logger.info("System exited cleanly");

	}


	private static void initializeLogger() {
		logger = Logger.getLogger(className);

		BasicConfigurator.configure();
		logger.info("Entering application.");
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
				logger.debug("UI selected");
			} else if ((args[i].equals("-file")) && (i + 1 < args.length)) {
				filename = args[i + 1];
				logger.debug("Filename taken from user input");
			}
		}
		
		boolean fileLoaded = FinApplication.INSTANCE.loadEnvironment(checkFilename(filename));
		UI.initUI(fileLoaded);
	}

	private static String checkFilename(String i)
			throws IllegalArgumentException {
		assert(i!=null);
		if (!i.endsWith(fileExtension)) {
			logger.log(Level.DEBUG, "Appended file extension to arguments");
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
