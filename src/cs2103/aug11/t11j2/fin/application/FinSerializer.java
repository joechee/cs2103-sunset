package cs2103.aug11.t11j2.fin.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import cs2103.aug11.t11j2.fin.storage.Serializer;

/**
 * 
 * FinSerializer handles the serializing of the Fin working environment (i.e all
 * task and the semantics relationship between them) into a list of objects.
 * 
 * Each task is represented as a dictionary (Map<String, Object>) and tasks with
 * subtasks will have a "Subtasks" attribute with the same (recursive)
 * definition.
 * 
 * This allows the main Storage Serializer to serialize the serialized object
 * into YAML (or JSON or XML if we want to change in the future).
 * 
 * @version 0.1
 * @author Koh Zi Chun
 * @author Acer Wei Jing
 * @author Alex Liew
 * @author Joe Chee
 */
public class FinSerializer {
	private Logger logger = Logger.getLogger(this.getClass());

	public void serialize(String filename, List<Task> tasks) throws IOException {
		Serializer sr = new Serializer();
		List<Object> oLyst = FinSerializer
				.taskTreeListToList(tasks);

		sr.serialize(oLyst.iterator(), filename);
	}

	/**
	 * Unserialize a file into the environment. Should clear the environment beforehand.
	 * 
	 * @param filename
	 * @throws IOException
	 * @return A list of tasks
	 */
	

	public List<Task> unserialize(String filename)
			throws IOException {
		logger.debug("Unserializing tasks");
		Serializer sr = new Serializer();
		List<Object> dictionaries = sr.unserialize(filename);
		return FinSerializer.parseDictionaries(dictionaries, null);
	}

	@SuppressWarnings("unchecked")
	private static List<Task> parseDictionaries(List<Object> dictionaries,
			UUID parentUID) {
		List<Task> taskList = new ArrayList<Task>();
		for (Object dict : dictionaries) {
			taskList.add(FinSerializer
					.parseDictionary((Map<String, Object>) dict, parentUID));
		}
		return taskList;
	}

	private static Task parseDictionary(Map<String, Object> dict, UUID parentUID) {
		Task task = new Task(dict);
		//FinApplication.INSTANCE.add(task,false);
		return task;
	}

	/**
	 * Converts a list of Task into a List of dictionaries for serialization
	 */
	private static List<Object> taskTreeListToList(List<Task> ttl) {
		List<Object> tr = new ArrayList<Object>();
		for (Task t : ttl) {
			tr.add(FinSerializer.taskToDictionary(t));
		}
		return tr;
	}

	/**
	 * Converts a given Task into a dictionary
	 */
	private static Map<String, Object> taskToDictionary(Task tt) {
		Map<String, Object> o = tt.toDictionary();
		return o;
	}

	public void createFile(String filename) throws IOException {
		Serializer sr = new Serializer();
		sr.createFile(filename);
	}
}
