package cs2103.aug11.t11j2.fin.application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.datamodel.TaskSortByDueDate;

/**
 * Fin Application class to handle guided tour
 * does not do serialization and creates a sandbox environment 
 * for the tour
 * 
 * @author Koh Zi Chun
 *
 */
public class FinApplicationTour implements Fin.IFinApplication {
	final static public FinApplicationTour INSTANCE = new FinApplicationTour();
	
	private FinApplicationTour() {
		
	}
	
	String taskFileName = "";

	private List<Task> taskList = new ArrayList<Task>();
	private Map<UUID, Task> taskMap = new HashMap<UUID, Task>();

	// Each hashTag points to a collection of Tasks
	Map<String, List<Task>> hashTags = new HashMap<String, List<Task>>();
	
	// 
	private Stack<List<Task>> undeleteStack = new Stack<List<Task>>();

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
		Task deletedTask = removeTask(taskUID);
		List<Task> deletedTaskLyst = new ArrayList<Task>();
		deletedTaskLyst.add(deletedTask);
		undeleteStack.push(deletedTaskLyst);
		return true;
	}
	
	/**
	 * Internal code for deleting a given task (by UID) from the environment
	 * 
	 * @param taskUID
	 * @return the task that was deleted
	 * @throws IllegalArgumentException
	 */	
	private Task removeTask(UUID taskUID) {
		Task todelete = taskMap.get(taskUID);
		if (todelete != null) {
			taskMap.remove(taskUID);
			taskList.remove(todelete);
		} else {
			throw new IllegalArgumentException("TaskUID does not exist!");
		}
		return todelete;		
	}
	
	/**
	 * Deletes all tasks and updates the undelete stack
	 * @param taskUID
	 * @return true if deletes were successful
	 */
	public boolean deleteTasks(Collection<UUID> taskUID) {
		List<Task> deletedTaskLyst = new ArrayList<Task>();
		for (UUID deleteID: taskUID) {
			Task deletedTask = removeTask(deleteID);
			deletedTaskLyst.add(deletedTask);
		}
		undeleteStack.push(deletedTaskLyst);
		return true;
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
	@Override
	public void clearEnvironment() {
		taskMap.clear();
		taskList.clear();
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
	 * Undoes the last delete action
	 * 
	 * @return null if undelete is successful, a list of tasks if undelete stack is empty.
	 */	
	public List<Task> undelete() {
		if (undeleteStack.isEmpty()) {
			return null;
		} else {
			List<Task> deletedTasks = undeleteStack.pop();
			for (Task task: deletedTasks) {
				add(task);
			}
			return deletedTasks;
		}
		
	}
}
