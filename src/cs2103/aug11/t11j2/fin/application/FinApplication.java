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

	List<TaskTree> taskList = new ArrayList<TaskTree>();
	Map<UUID, TaskTree> taskMap = new TreeMap<UUID, TaskTree>();

	/**
	 * Adds a task to the environment.
	 * 
	 * @param task
	 * @param parentUID
	 */
	public void add(Task task, UUID parentUID) {
		TaskTree newTask = new TaskTree(task, parentUID);

		if (parentUID != null) {
			TaskTree parentTask = taskMap.get(parentUID);
			if (parentTask != null) {
				parentTask.getChildren().add(newTask);
			}
		} else {
			taskList.add(newTask);
		}

		taskMap.put(newTask.getTask().getUniqId(), newTask);
	}

	/**
	 * Get the list of (sub)task of a given parentUID
	 * 
	 * @param parentUID
	 * @return List of Tasks sorted by pIndex
	 */
	public List<Task> getTasks(UUID parentUID) {
		List<Task> tasks = new ArrayList<Task>();

		if (parentUID != null) {
			TaskTree parentTask = taskMap.get(parentUID);
			if (parentTask != null) {
				for (TaskTree t : parentTask.getChildren()) {
					tasks.add(t.getTask());
				}
			}
		} else {
			for (TaskTree t : taskList) {
				tasks.add(t.getTask());
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
	public boolean deleteTask(UUID taskUID) {
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
	 * Clears the current Fin environment (of all tasks etc.)
	 */
	void clearEnvironment() {
		taskMap.clear();
		taskList.clear();		
	}

	
	/**
	 * Deletes a given TaskTree object from it's parents reference
	 * 
	 * @param todelete
	 * @return true iff the child is deleted from parent
	 */
	private boolean deleteChild(TaskTree todelete) {
		if (todelete.getParentUID() == null) {
			return false;
		}
		
		TaskTree parent = taskMap.get(todelete.getParentUID());
		if (parent != null) {
			return parent.getChildren().remove(taskMap);
		} else {
			return false;
		}
	}
}