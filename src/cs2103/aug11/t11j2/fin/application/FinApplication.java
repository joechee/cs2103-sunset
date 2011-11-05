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

import org.apache.log4j.Logger;

/**
 * FinApplication class that handles the environment instance of Fin
 * (essentially manages what task is in memory, serializing searching etc.)
 * 
 * Only a single instance of the environment should be running to prevent any
 * race conditions
 * 
 * @version 0.1
 * @author Koh Zi Chun
 */
public class FinApplication implements Fin.IFinApplication {
	final public static FinApplication INSTANCE = new FinApplication();
	Logger logger = Logger.getLogger(this.getClass());
	String taskFileName = "";
	// Each hashTag points to a collection of Tasks
	Map<String, List<Task>> hashTags = new HashMap<String, List<Task>>();
	
	private Map<UUID, Task> taskMap = new HashMap<UUID, Task>();
	private Stack<List<Task>> undeleteStack = new Stack<List<Task>>();
	
	FinApplication() {
		logger.debug("FinApplication Object created");
	}

	@Override
	public void add(Task task) {
		add(task,true);
	}
	
	@Override
	public void add(Task task, boolean save) {
		assert (task != null);
		taskMap.put(task.getUniqId(), task);

		for (String tag : task.getTags()) {
			addTaskToTag(tag, task);
		}
		logger.debug("Task added to internal datamodel!");
		if (save == true) {
			this.saveEnvironment();
		}
	}

	private boolean addTaskToTag(String tag, Task task) {
		assert (task != null);
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
		assert (task != null);
		assert (tag != null);

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
	 * @return List of Tasks sorted by pIndex
	 */
	@Override
	public List<Task> getTasks() {
		List<Task> tasks = new ArrayList<Task>(taskMap.values());
		Collections.sort(tasks, new TaskSortByDueDate());
		return tasks;
	}

	/**
	 * @return List of Task with tag sorted by pIndex
	 */
	@Override
	public List<Task> getTasksWithTag(String tag) {
		assert (tag != null);
		if (hashTags.containsKey(tag)) {
			List<Task> lt = hashTags.get(tag);
			Collections.sort(lt, new TaskSortByDueDate());
			return lt;
		} else {
			return new ArrayList<Task>();
		}
	}

	/**
	 * @return List of Task with tag sorted by pIndex
	 */
	@Override
	public List<Task> getTasksWithoutTag(String tag) {
		assert (tag != null);
		List<Task> lt = getTasks();
		List<Task> rt = new ArrayList<Task>();
		for (Task i : lt) {
			if (!i.getTags().contains(tag)) {
				rt.add(i);
			}
		}
		Collections.sort(rt, new TaskSortByDueDate());
		return rt;
	}

	/**
	 * @return List of Task with tag sorted by pIndex
	 */
	@Override
	public List<Task> getTasksWithoutTags(List<String> tags) {
		assert (tags != null);
		List<Task> lt = getTasks();
		List<Task> rt = new ArrayList<Task>();
		for (Task i : lt) {
			if (!i.getTags().containsAll(tags)) {
				rt.add(i);
			}
		}
		Collections.sort(rt, new TaskSortByDueDate());
		return rt;
	}

	/**
	 * @return List of Task with tags sorted by pIndex
	 */
	@Override
	public List<Task> getTasksWithTags(List<String> tags) {
		assert (tags != null);
		List<Task> filteredTasks = new ArrayList<Task>();
		for (Task t : taskMap.values()) {
			if (t.hasTags(tags)) {
				filteredTasks.add(t);
			}
		}
		Collections.sort(filteredTasks, new TaskSortByDueDate());
		return filteredTasks;
	}

	@Override
	public boolean deleteTask(UUID taskUID) {
		assert (taskUID != null);
		Task deletedTask = removeTask(taskUID);
		List<Task> deletedTaskLyst = new ArrayList<Task>();
		deletedTaskLyst.add(deletedTask);
		logger.debug("Task deleted from internal datamodel!");
		undeleteStack.push(deletedTaskLyst);
		logger.debug("Task added to undelete stack");
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
		assert (taskUID != null);
		Task todelete = taskMap.get(taskUID);
		if (todelete != null) {
			taskMap.remove(taskUID);
			for (String tag : todelete.getTags()) {
				removeTaskFromTag(tag, todelete);
			}
			this.saveEnvironment();
		} else {
			throw new IllegalArgumentException("TaskUID does not exist!");
		}
		return todelete;
	}

	public boolean deleteTasks(Collection<UUID> taskUID) {
		assert (taskUID != null);
		List<Task> deletedTaskLyst = new ArrayList<Task>();
		for (UUID deleteID : taskUID) {
			Task deletedTask = removeTask(deleteID);
			deletedTaskLyst.add(deletedTask);
		}
		logger.debug("Tasks deleted from internal datamodel!");
		undeleteStack.push(deletedTaskLyst);
		logger.debug("Tasks added to undelete stack!");
		return true;
	}

	@Override
	public boolean flagTask(UUID taskUID) {
		assert (taskUID != null);
		Task task = taskMap.get(taskUID);
		if (task != null) {
			task.flag();
			this.saveEnvironment();
			return true;
		} else {
			logger.fatal("Task selected is null! Error!");
			return false;
		}
	}

	@Override
	public boolean unflagTask(UUID taskUID) {
		assert (taskUID != null);
		Task task = taskMap.get(taskUID);

		if (task != null) {
			task.unflag();
			logger.debug("Tasks marked as unimportant!");
			this.saveEnvironment();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean finTask(UUID taskUID) {
		assert (taskUID != null);
		Task task = taskMap.get(taskUID);

		if (task != null) {
			boolean finTask = task.fin();
			if (finTask) {
				logger.debug("Tasks marked as finished!");
			} else {
				logger.warn("Task was already finished!");
			}

			this.saveEnvironment();
			return finTask;
		} else {
			return false;
		}
	}

	@Override
	public boolean unfinTask(UUID taskUID) {
		assert (taskUID != null);
		Task task = taskMap.get(taskUID);

		if (task != null) {
			if (task.unfin()) {
				logger.debug("Tasks marked as unfinished!");
			}
			this.saveEnvironment();
			return true;
		} else {
			logger.warn("Task was never finished to begin with!");
			return false;
		}
	}

	@Override
	public void clearEnvironment() {
		assert (taskMap != null);

		this.taskMap.clear();
		this.hashTags.clear();
		this.undeleteStack.clear();

		undeleteStack.clear();
		logger.debug("Environment Cleared!");
	}

	@Override
	public boolean loadEnvironment(String filename) throws IOException {
		assert (filename != null);
		if (filename.isEmpty()) {
			filename = Fin.DEFAULT_FILENAME;
		}
		FinSerializer fs = new FinSerializer();
		taskFileName = filename;

		try {
			this.clearEnvironment();
			List<Task> tasks = fs.unserialize(filename);
			for (Task t: tasks) {
				this.add(t,false);
			}
		} catch (FileNotFoundException fnfe) {
			clearEnvironment();
			logger.warn("Tasks file not found! Creating file...");
			fs.createFile(filename);
			logger.debug("Tasks file created!");
			return false;
		} catch (IOException e) {
			logger.warn("Error loading file... trying backup file");
			try {
				fs.unserialize(filename + ".bak");
				this.clearEnvironment();
				List<Task> tasks = fs.unserialize(filename+".bak");
				for (Task t: tasks) {
					this.add(t,false);
				}
				return true;
			} catch (IOException f) {
				logger.fatal("Files cannot be loaded. Please check if you have write access to the disk");
				return false;
			}

		}

		return true;
	}

	@Override
	public List<String> getHashTags() {
		List<String> tr = new ArrayList<String>();
		for (String s : hashTags.keySet()) {
			tr.add(s);
		}
		return tr;
	}

	/**
	 * Saves the current state of all tasks to a file that can be loaded by the
	 * <code>loadEnvironment</code> method.
	 * 
	 * @param none
	 * @see loadEnvironment
	 */
	protected void saveEnvironment() {
		FinSerializer fs = new FinSerializer();
		try {
			fs.serialize(taskFileName,getTasks());
			logger.info("File saved");
		} catch (IOException e) {
			if (FinConstants.IS_DEVELOPMENT) {
				e.printStackTrace();
			} else {
				logger.error("File being used by another process, saving delayed");
			}
		}

		try {
			fs.serialize(taskFileName+".bak",getTasks());
		} catch (IOException e) {
			if (FinConstants.IS_DEVELOPMENT) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Task> undelete() {
		if (undeleteStack.isEmpty()) {
			return null;
		} else {
			List<Task> deletedTasks = undeleteStack.pop();
			for (Task task : deletedTasks) {
				add(task);
			}
			return deletedTasks;
		}

	}

	@Override
	public void editTask(Task task, String string) {
		assert (task != null);
		assert (string != null);
		for (String tag : task.getTags()) {
			removeTaskFromTag(tag, task);
		}
		task.edit(string);
		for (String tag : task.getTags()) {
			addTaskToTag(tag, task);
		}
		logger.debug("Task edited!");
		this.saveEnvironment();
	}

	@Override
	public void addTag(Task task, String tag) {
		assert (task != null);
		assert (tag != null);
		if (task.addTag(tag)) {
			addTaskToTag(tag, task);
		}
		this.saveEnvironment();

	}

	@Override
	public void removeTag(Task task, String string) {
		assert (task != null);
		assert (string != null);
		task.removeTag(string);
		for (String tag : task.getTags()) {
			addTaskToTag(tag, task);
		}
		this.saveEnvironment();

	}

	@Override
	public void setDueDate(Task task, String string) {
		assert (task != null);
		assert (string != null);
		task.setDueDate(string);
		this.saveEnvironment();

	}
}