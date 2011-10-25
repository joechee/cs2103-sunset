package cs2103.aug11.t11j2.fin.application;

import cs2103.aug11.t11j2.fin.datamodel.*;
import cs2103.aug11.t11j2.fin.ui.UIContext;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * FinApplication class that handles the environment instance
 * of Fin (essentially manages what task is in memory, serializing
 * searching etc.)
 *  
 * @version 0.1
 * @author Koh Zi Chun
 */
public class FinApplication implements Fin.IFinApplication {
	public static FinApplication INSTANCE = new FinApplication();
	
	private FinApplication () {
		
	}

	String taskFileName = "";

	private List<Task> taskList = new ArrayList<Task>();
	private Map<UUID, Task> taskMap = new HashMap<UUID, Task>();

	// Each hashTag points to a collection of Tasks
	Map<String, List<Task>> hashTags = new HashMap<String, List<Task>>();

	/**
	 * Adds a task to the environment.
	 * 
	 * @param task
	 */
	@Override
	public void add(Task task) {
		taskList.add(task);
		taskMap.put(task.getUniqId(), task);

		for (String tag : task.getTags()) {
			addTaskToTag(tag, task);
		}
		saveEnvironment();
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
	@Override
	public List<Task> getTasks() {
		List<Task> tasks = new ArrayList<Task>(taskList);

		Collections.sort(tasks, new TaskSortByDueDate());

		return tasks;
	}

	/**
	 * Get the list of tasks with a particular #tag
	 * 
	 * @param tag
	 * @return List of Task with tag sorted by pIndex
	 */
	@Override
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
	 * Get the list of tasks without a particular #tag
	 * 
	 * @param tag
	 * @return List of Task with tag sorted by pIndex
	 */	
	@Override
	public List<Task> getTasksWithoutTag(String tag) {
		List<Task> lt = getTasks();
		List<Task> rt = new ArrayList<Task>();
		for (Task i: lt) {
			if (!i.getTags().contains(tag)) {
				rt.add(i);
			}
		}
		Collections.sort(rt, new TaskSortByDueDate());
		return rt;
	}
	
	/**
	 * Get the list of tasks that don't have a list of #tags
	 * 
	 * @param tag
	 * @return List of Task with tag sorted by pIndex
	 */	
	@Override
	public List<Task> getTasksWithoutTags(List<String> tags) {
		List<Task> lt = getTasks();
		List<Task> rt = new ArrayList<Task>();
		for (Task i: lt) {
			if (!i.getTags().containsAll(tags)) {
				rt.add(i);
			}
		}
		Collections.sort(rt, new TaskSortByDueDate());
		return rt;
	}

	/**
	 * Get the list of tasks with a list of #tags
	 * 
	 * @param tag
	 * @return List of Task with tags sorted by pIndex
	 */
	@Override
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
	@Override
	public boolean deleteTask(UUID taskUID) {
		Task todelete = taskMap.get(taskUID);

		if (todelete != null) {
			taskMap.remove(taskUID);
			taskList.remove(todelete);
			saveEnvironment();
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
	@Override
	public boolean flagTask(UUID taskUID) {
		Task task = taskMap.get(taskUID);

		if (task != null) {
			task.flag();
			saveEnvironment();
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
	@Override
	public boolean unflagTask(UUID taskUID) {
		Task task = taskMap.get(taskUID);

		if (task != null) {
			task.unflag();
			saveEnvironment();
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
	@Override
	public boolean finTask(UUID taskUID) {
		Task task = taskMap.get(taskUID);

		if (task != null) {
			boolean finTask = task.fin();
			saveEnvironment();
			return finTask;
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
	@Override
	public boolean unfinTask(UUID taskUID) {
		Task task = taskMap.get(taskUID);

		if (task != null) {
			task.unfin();
			saveEnvironment();
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
		saveEnvironment();
	}
	
	/**
	 * Loads the tasks that was previously saved by the <code>saveEnvironment</code> method from a file.
	 * @param filename
	 * @throws IOException
	 * @see saveEnvironment
	 */
	@Override
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
	 * Get the list of hashtags in the Fin Environment 
	 * 
	 * @return list of hashtags
	 */
	@Override
	public List<String> getHashTags() {
		List<String> tr = new ArrayList<String>();
		for (String s : hashTags.keySet()) {
			tr.add(s);
		}
		return tr;
	}
	
	/**
	 * Saves the current state of all tasks to a file that can be loaded by the <code>loadEnvironment</code> method.
	 * @param none
	 * @see loadEnvironment 
	 */
	private void saveEnvironment() {
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
}