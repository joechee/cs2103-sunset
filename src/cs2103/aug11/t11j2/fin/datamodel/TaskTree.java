package cs2103.aug11.t11j2.fin.datamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskTree {
	private Task task;
	private UUID parentUID;
	private List<TaskTree> children = new ArrayList<TaskTree>();
	
	public TaskTree(Task task, UUID parentUID) {
		this.setTask(task);
		this.setParentUID(parentUID);
	}

	public List<TaskTree> getChildren() {
		return children;
	}

	public void setChildren(List<TaskTree> children) {
		this.children = children;
	}

	public Task getTask() {
		return task;
	}

	void setTask(Task task) {
		this.task = task;
	}

	public UUID getParentUID() {
		return parentUID;
	}

	void setParentUID(UUID parentUID) {
		this.parentUID = parentUID;
	}
};
