package cs2103.aug11.t11j2.fin.application;

import cs2103.aug11.t11j2.fin.datamodel.*;
import cs2103.aug11.t11j2.fin.ui.UIContext;

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

	List<Task> taskList = new ArrayList<Task>();
	Map<UUID, Task> taskMap = new TreeMap<UUID, Task>();
	UIContext context = new UIContext();

	/**
	 * Adds a task to the environment.
	 * 
	 * @param task
	 */
	public void add(Task task) {
		taskList.add(task);
		taskMap.put(task.getUniqId(), task);
	}

	/**
	 * Get the list of (sub)task of a given parentUID
	 * 
	 * @return List of Tasks sorted by pIndex
	 */
	public List<Task> getTasks() {
		List<Task> tasks = new ArrayList<Task>();

		for (Task t : taskList) {
			tasks.add(t);
		}

		Collections.sort(tasks, new TaskSortByPIndex());

		return tasks;
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
	 * Clears the current Fin environment (of all tasks etc.)
	 */
	void clearEnvironment() {
		taskMap.clear();
		taskList.clear();		
	}

	public UIContext getUIContext() {
		return context;
	}
}