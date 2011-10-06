package cs2103.aug11.t11j2.fin.ui;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.datamodel.Task;

public class UIContext {
	private List<Task> taskList = new ArrayList<Task>();
	private String status = "";
	private String filter;

	public List<Task> getTaskList() {
		return taskList;
	}

	void setTaskList(List<Task> tasks) {
		this.taskList = tasks;
	}
	
	void setStatus(String newStatus) {
		status = newStatus;
	}
	
	String getStatus() {
		return status;
	}
	

	void setFilter(String filter) {
		this.filter = filter;
	}
	String getFilter() {
		return this.filter;
	}
	
}
