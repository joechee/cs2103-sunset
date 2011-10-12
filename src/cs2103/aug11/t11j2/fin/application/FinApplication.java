package cs2103.aug11.t11j2.fin.application;

import cs2103.aug11.t11j2.fin.datamodel.*;
import cs2103.aug11.t11j2.fin.ui.UIContext;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * 
 * @version 0.1
 * @author Koh Zi Chun
 * @author Acer Wei Jing
 * @author Alex Liew
 * @author Joe Chee
 */
public enum FinApplication {
	INSTANCE;

	String taskFileName = "";

	List<Task> taskList = new ArrayList<Task>();
	Map<UUID, Task> taskMap = new HashMap<UUID, Task>();
	UIContext context = new UIContext();

	// Each hashTag points to a collection of Tasks
	Map<String, List<Task>> hashTags = new HashMap<String, List<Task>>();

	/**
	 * Adds a task to the environment.
	 * 
	 * @param task
	 */
	public void add(Task task) {
		taskList.add(task);
		taskMap.put(task.getUniqId(), task);

		for (String tag : task.getTags()) {
			addTaskToTag(tag, task);
		}
	}

	private void addTaskToTag(String tag, Task task) {
		List<Task> taskListOfTags;

		if (hashTags.containsKey(tag)) {
			taskListOfTags = hashTags.get(tag);
		} else {
			taskListOfTags = new ArrayList<Task>();
			hashTags.put(tag, taskListOfTags);
		}

		taskListOfTags.add(task);
	}

	/**
	 * Get the list of tasks
	 * 
	 * @return List of Tasks sorted by pIndex
	 */
	public List<Task> getTasks() {
		List<Task> tasks = new ArrayList<Task>();

		for (Task t : taskList) {
			tasks.add(t);
		}

		Collections.sort(tasks, new TaskSortByDueDate());

		return tasks;
	}

	/**
	 * Get the list of tasks with a particular #tag
	 * 
	 * @param tag
	 * @return List of Task with tag sorted by pIndex
	 */
	public List<Task> getTasksWithTag(String tag) {
		if (hashTags.containsKey(tag)) {
			List<Task> lt = hashTags.get(tag);
			Collections.sort(lt, new TaskSortByDueDate());

			return lt;
		} else {
			return new ArrayList<Task>();
		}
	}

	/**
	 * Get the list of tasks with a list of #tags
	 * 
	 * @param tag
	 * @return List of Task with tags sorted by pIndex
	 */
	public List<Task> getTasksWithTags(List<String> tags) {
		List<Task> filteredTasks = new ArrayList<Task>();
		for (Task t : taskList) {
			if (t.hasTags(tags)) {
				filteredTasks.add(t);
			}
		}
		Collections.sort(filteredTasks, new TaskSortByDueDate());
		return filteredTasks;
	}

	/**
	 * Deletes a given task (by UID) from the environment
	 * 
	 * @param taskUID
	 * @return true iff the task is deleted
	 */
	public boolean deleteTask(UUID taskUID) {
		Task todelete = taskMap.get(taskUID);

		if (todelete != null) {
			taskMap.remove(taskUID);
			taskList.remove(todelete);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Mark a task as important / flag
	 * 
	 * @param taskUID
	 * @return true iff the task is flagged
	 */
	public boolean flagTask(UUID taskUID) {
		Task task = taskMap.get(taskUID);

		if (task != null) {
			task.flag();
			return true;
		} else {
			return false;
		}		
	}
	/**
	 * Unmark a task as important / flag
	 * 
	 * @param taskUID
	 * @return true iff the task is unflagged
	 */
	public boolean unflagTask(UUID taskUID) {
		Task task = taskMap.get(taskUID);

		if (task != null) {
			task.unflag();
			return true;
		} else {
			return false;
		}		
	}

	/**
	 * Mark a task as completed
	 * 
	 * @param taskUID
	 * @return true iff the task is marked as finished
	 */
	public boolean finTask(UUID taskUID) {
		Task task = taskMap.get(taskUID);

		if (task != null) {
			return task.fin();
		} else {
			return false;
		}
	}

	/**
	 * Unmark a task as completed
	 * 
	 * @param taskUID
	 * @return true iff the task is unmarked as finished
	 */
	public boolean unfinTask(UUID taskUID) {
		Task task = taskMap.get(taskUID);

		if (task != null) {
			task.unfin();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Clears the current Fin environment (of all tasks etc.)
	 * @param none
	 * @return void
	 */
	void clearEnvironment() {
		taskMap.clear();
		taskList.clear();
	}
	
	/**
	 * Loads the tasks that was previously saved by the <code>saveEnvironment</code> method from a file.
	 * @param filename
	 * @throws IOException
	 * @see saveEnvironment
	 */

	public void loadEnvironment(String filename) throws IOException {
		FinSerializer fs = new FinSerializer();
		taskFileName = filename;

		try {
			fs.unserialize(filename, true);
		} catch (FileNotFoundException fnfe) {
			clearEnvironment();
			fs.serialize(filename);
		}
	}
	
	/**
	 * Saves the current state of all tasks to a file that can be loaded by the <code>loadEnvironment</code> method.
	 * @param none
	 * @see loadEnvironment 
	 */

	public void saveEnvironment() {
		FinSerializer fs = new FinSerializer();
		try {
			fs.serialize(taskFileName);
		} catch (IOException e) {
			if (FinConstants.IS_PRODUCTION) {
				e.printStackTrace();
			} else {
				// TODO: handle saving
			}
		}
	}

	public UIContext getUIContext() {
		return context;
	}


}