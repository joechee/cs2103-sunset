package cs2103.aug11.t11j2.fin.application;

import cs2103.aug11.t11j2.fin.datamodel.*;

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

	private class TaskTree extends Task {
		UUID parentUID;
		List<TaskTree> children = new ArrayList<TaskTree>();
	};

	List<TaskTree> taskList = new ArrayList<TaskTree>();
	Map<UUID, TaskTree> taskMap = new TreeMap<UUID, TaskTree>();

	/**
	 * Adds a task to the environment.
	 * 
	 * @param task
	 * @param parentUID
	 */
	void add(Task task, UUID parentUID) {
		TaskTree newTask = (TaskTree) task;
		newTask.parentUID = parentUID;

		if (parentUID != null) {
			TaskTree parentTask = taskMap.get(parentUID);
			if (parentTask != null) {
				parentTask.children.add(newTask);
			}
		} else {
			taskList.add(newTask);
		}

		taskMap.put(newTask.getUniqId(), newTask);
	}

	/**
	 * Get the list of (sub)task of a given parentUID
	 * 
	 * @param parentUID
	 * @return List of Tasks sorted by pIndex
	 */
	List<Task> getTasks(UUID parentUID) {
		List<Task> tasks = new ArrayList<Task>();

		if (parentUID != null) {
			TaskTree parentTask = taskMap.get(parentUID);
			if (parentTask != null) {
				for (Task t : parentTask.children) {
					tasks.add(t);
				}
			}
		} else {
			for (Task t : taskList) {
				tasks.add(t);
			}
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
	boolean deleteTask(UUID taskUID) {
		TaskTree todelete = taskMap.get(taskUID);
		
		if (todelete != null) {
			deleteChild(todelete);
			taskMap.remove(taskUID);
			
			return true;
		} else {
			return false;
		}
	}

	
	/**
	 * Deletes a given TaskTree object from it's parents reference
	 * 
	 * @param todelete
	 * @return true iff the child is deleted from parent
	 */
	private boolean deleteChild(TaskTree todelete) {
		if (todelete.parentUID == null) {
			return false;
		}
		
		TaskTree parent = taskMap.get(todelete.parentUID);
		if (parent != null) {
			return parent.children.remove(taskMap);
		} else {
			return false;
		}
	}

}