package cs2103.aug11.t11j2.fin.application;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;

import cs2103.aug11.t11j2.fin.application.FinLogger.LogDestination;
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
	

	private static final Logger logger = FinLogger.initializeLogger(LogDestination.CONSOLE);
	
	
	
	
	public static Fin.IUserInterface DEFAULT_UI = new GUI();
	public final static String DEFAULT_FILENAME = "fin.yaml";
	public static final String fileExtension = ".yaml";
	
	


	
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
		/**
		 * Adds a task to the environment.
		 * 
		 * @param task object to add
		 */
		public void add(Task task);
		/**
		 * Get the list of tasks with a particular #tag
		 * 
		 * @param tag
		 * @return List of Task with tag. tag should not have a # prefixed to it
		 */
		public List<Task> getTasksWithTag(String tag);
		/**
		 * @return List of Tasks
		 */
		public List<Task> getTasks();
		/**
		 * Get the list of tasks without a particular #tag
		 * 
		 * @param tag
		 * @return List of Task with tag. tag should not have a # prefixed to it
		 */	
		public List<Task> getTasksWithoutTag(String tag);
		/**
		 * Get the list of tasks with a list of #tags
		 * 
		 * @param tag
		 * @return List of Task with tags. tags should not have a # prefixed to it
		 */
		public List<Task> getTasksWithTags(List<String> tags);
		/**
		 * Get the list of tasks that don't have a list of #tags
		 * 
		 * @param tag
		 * @return List of Task without a list of tags. tags should not have a # prefixed to it
		 */	
		public List<Task> getTasksWithoutTags(List<String> tags);
		/**
		 * Deletes a given task (by UID) from the environment
		 * 
		 * @param taskUID
		 * @return true iff the task is deleted
		 */
		public boolean deleteTask(UUID taskUID);
		/**
		 * Deletes all tasks and updates the undelete stack
		 * @param taskUID
		 * @return true if deletes were successful
		 */
		public boolean deleteTasks(Collection<UUID> toDeleteUUID);
		/**
		 * Mark a task as important / flag
		 * 
		 * @param taskUID
		 * @return true iff the task is flagged
		 */
		public boolean flagTask(UUID taskUID);
		/**
		 * Unmark a task as important / flag
		 * 
		 * @param taskUID
		 * @return true iff the task is unflagged
		 */
		public boolean unflagTask(UUID taskUID);
		/**
		 * Mark a task as completed
		 * 
		 * @param taskUID
		 * @return true iff the task is marked as finished
		 */
		public boolean finTask(UUID taskUID);
		/**
		 * Unmark a task as completed
		 * 
		 * @param taskUID
		 * @return true iff the task is unmarked as finished
		 */
		public boolean unfinTask(UUID taskUID);
		/**
		 * Loads the tasks that was previously saved by the <code>saveEnvironment</code> method from a file.
		 * @param filename
		 * @throws IOException
		 * @return true if environment is successfully loaded
		 * @see saveEnvironment
		 */
		public boolean loadEnvironment(String filename) throws IOException;
		/**
		 * Clears the current Fin environment (of all tasks and the undelete stack etc.)
		 * @param none
		 * @return void
		 */
		public void clearEnvironment();	
		/**
		 * Get the list of hashtags in the Fin Environment 
		 * 
		 * @return list of hashtags
		 */
		public List<String> getHashTags();
		/**
		 * Edits the task with the new string as the taskname. Parses the string.
		 * 
		 * @return void
		 */
		public void editTask(Task task, String string);
		/**
		 * Add a tag to the Task object.
		 * 
		 * @param Task to tag
		 * @param String representing the tag
		 * @return void
		 */
		public void addTag(Task task, String string);
		/**
		 * Remove a tag from the Task object.
		 * 
		 * @param Task to untag
		 * @param String representing the tag
		 * @return void
		 */
		public void removeTag(Task task, String string);

		/**
		 * Sets the current task's dueDate
		 * 
		 * @param Task to set
		 * @param String representing the new due date (in natural language)
		 * @return void
		 */
		public void setDueDate(Task task, String string);
		/**
		 * Undoes the last delete action
		 * 
		 * @return null if undelete is successful, a list of tasks if undelete stack is empty.
		 */
		public List<Task> undelete();
	}

	public static void main(String[] args) {
		try {
			logger.info("Entering application.");
			parseArgs(args);
		} catch (IllegalArgumentException e) {
			System.out.print(e.getMessage());
		} catch (IOException e) {
			System.out.print(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.fatal("Fatal Error!",e);
		}
		logger.info("System exited cleanly");

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
