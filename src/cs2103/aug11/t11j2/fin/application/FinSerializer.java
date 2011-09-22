package cs2103.aug11.t11j2.fin.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cs2103.aug11.t11j2.fin.application.FinApplication.TaskTree;
import cs2103.aug11.t11j2.fin.storage.Serializer;

/**
 * 
 * @version 0.1
 * @author Koh Zi Chun
 * @author Acer Wei Jing
 * @author Alex Liew
 * @author Joe Chee
 */
public class FinSerializer {
	public void serialize(String filename) throws IOException {
		Serializer sr = new Serializer();
		List<Object> oLyst = FinSerializer.TaskTreeListToList(FinApplication.INSTANCE.taskList);		
		
		sr.serialize(oLyst, filename);
	}
	
	
	private static List<Object> TaskTreeListToList(List<TaskTree> ttl) {
		List<Object> tr = new ArrayList<Object>();
		for (TaskTree t : ttl) {
			tr.add(FinSerializer.TaskTreeToObject(t));
		}
		return tr;
	}
	private static Map<String, Object> TaskTreeToObject(TaskTree tt) {
		Map<String, Object> o = tt.task.toObject();
		
		if (tt.children.size() > 0) {
			o.put("Subtasks", FinSerializer.TaskTreeListToList(tt.children));
		}
		return o;
	}
}
