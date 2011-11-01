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

/**
 * FinApplication class that handles the environment instance
 * of Fin (essentially manages what task is in memory, serializing
 * searching etc.)
 * 
 * Only a single instance of the environment should be running 
 * to prevent any race conditions
 *  
 * @version 0.1
 * @author Koh Zi Chun
 */
public class FinApplication implements Fin.IFinApplication {
	final public static FinApplication INSTANCE = new FinApplication();
	
	FinApplication () {
		
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
		assert(task!=null);
		taskList.add(task);
		taskMap.put(task.getUniqId(), task);

		for (String tag : task.getTags()) {
			addTaskToTag(tag, task);
		}
		this.saveEnvironment();
	}
	
	private boolean addTaskToTag(String tag, Task task) {
		assert(task != null);
		List<Task> taskListOfTags;
		if (hashTags.containsKey(tag)) {
			taskListOfTags = hashTags.get(tag);
		} else {
			taskListOfTags = new ArrayList<Task>();
			hashTags.put(tag, taskListOfTags);
		}
		if (taskListOfTags.contains(task)) {
			return false;
		} else {
			taskListOfTags.add(task);
			return true;
		}
		
	}
	
	private void removeTaskFromTag(String tag, Task task) {
		assert(task != null);
		assert(tag != null);
		
		List<Task> taskListOfTags;
		if (hashTags.containsKey(tag)) {
			taskListOfTags = hashTags.get(tag);
		} else {
			taskListOfTags = new ArrayList<Task>();
			hashTags.put(tag, taskListOfTags);
		}
		taskListOfTags.remove(task);
		if (taskListOfTags.isEmpty()) {
			hashTags.remove(tag);
		}
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
		assert(tag!=null);
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
		assert(tag!=null);
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
		assert(tags!=null);
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
		assert(tags!=null);
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
		assert(taskUID!=null);
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
		assert(taskUID!=null);
		Task todelete = taskMap.get(taskUID);
		if (todelete != null) {
			taskMap.remove(taskUID);
			taskList.remove(todelete);
			for (String tag : todelete.getTags()) {
				removeTaskFromTag(tag, todelete);
			}
			this.saveEnvironment();
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
		assert(taskUID != null);
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
		assert(taskUID != null);
		Task task = taskMap.get(taskUID);

		if (task != null) {
			task.flag();
			this.saveEnvironment();
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
		assert(taskUID!=null);
		Task task = taskMap.get(taskUID);

		if (task != null) {
			task.unflag();
			this.saveEnvironment();
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
		assert(taskUID!=null);
		Task task = taskMap.get(taskUID);

		if (task != null) {
			boolean finTask = task.fin();
			this.saveEnvironment();
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
		assert(taskUID!=null);
		Task task = taskMap.get(taskUID);

		if (task != null) {
			task.unfin();
			this.saveEnvironment();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Clears the current Fin environment (of all tasks and the undelete stack etc.)
	 * @param none
	 * @return void
	 */
	@Override
	public void clearEnvironment() {
		assert(taskMap!=null);
		assert(taskList!=null);
		
		this.taskMap.clear();
		this.taskList.clear();
		this.hashTags.clear();
		this.undeleteStack.clear();
		
		undeleteStack.clear();
		this.saveEnvironment();
	}
	
	/**
	 * Loads the tasks that was previously saved by the <code>saveEnvironment</code> method from a file.
	 * @param filename
	 * @throws IOException
	 * @return true if environment is successfully loaded
	 * @see saveEnvironment
	 */
	@Override
	public boolean loadEnvironment(String filename) throws IOException {
		assert(filename!=null);
		if (filename.isEmpty()) {
			filename = Fin.DEFAULT_FILENAME;
		}
		FinSerializer fs = new FinSerializer();
		taskFileName = filename;

		try {
			fs.unserialize(filename, true);
		} catch (FileNotFoundException fnfe) {
			clearEnvironment();
			fs.serialize(filename);
			return false;
		} catch (IOException e) {
			try {
				fs.unserialize(filename+".bak");
				return false;
			} catch (IOException f) {
				return false;
			}

		}
		
		return true;
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
	protected void saveEnvironment() {
		FinSerializer fs = new FinSerializer();
		try {
			fs.serialize(taskFileName);
		} catch (IOException e) {
			if (FinConstants.IS_DEVELOPMENT) {
				e.printStackTrace();
			} else {
				// TODO: handle saving
			}
		}
		
		try {
			fs.serialize(taskFileName+".bak");
		} catch (IOException e) {
			if (FinConstants.IS_DEVELOPMENT) {
				e.printStackTrace();
			} 
		}
	}
	/**
	 * Undoes the last delete action
	 * 
	 * @return null if undelete is successful, a list of tasks if undelete stack is empty.
	 */
	@Override
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
	/**
	 * Edits the task with the new string as the taskname. Parses the string.
	 * 
	 * @return void
	 * 
	 */

	@Override
	public void editTask(Task task, String string) {
		assert(task!=null);
		assert(string!=null);
		for (String tag : task.getTags()) {
			removeTaskFromTag(tag, task);
		}
		task.edit(string);
		for (String tag : task.getTags()) {
			addTaskToTag(tag, task);
		}
		this.saveEnvironment();
	}

	@Override
	public void addTag(Task task, String tag) {
		assert(task!=null);
		assert(tag!=null);
		if (task.addTag(tag)) {
			addTaskToTag(tag, task);
		}
		this.saveEnvironment();
		
	}

	@Override
	public void removeTag(Task task, String string) {
		assert(task!=null);
		assert(string!=null);
		task.removeTag(string);
		for (String tag : task.getTags()) {
			addTaskToTag(tag, task);
		}
		this.saveEnvironment();
		
	}

	@Override
	public void removeDueDate(Task task) {
		assert(task!=null);
		task.removeDueDate();
		this.saveEnvironment();
		
	}

	@Override
	public void setDueDate(Task task, String string) {
		assert(task!=null);
		assert(string!=null);
		task.setDueDate(string);
		this.saveEnvironment();
		
	}
}