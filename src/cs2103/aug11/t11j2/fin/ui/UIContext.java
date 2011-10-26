package cs2103.aug11.t11j2.fin.ui;

import java.util.ArrayList;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.Fin;
import cs2103.aug11.t11j2.fin.datamodel.Task;

public class UIContext {
	private List<Task> taskList = new ArrayList<Task>();
	private String status = "";
	private String filter = "";
	
	private Fin.IFinApplication finApplication = null;
	
	
	UIContext(Fin.IFinApplication finApplication) {
		this.finApplication = finApplication;
	}
	
	UIContext() {
		this(null);
	}

	public List<Task> getTaskList() {
		return taskList;
	}

	public Fin.IFinApplication getFinApplication() {
		return finApplication;
	}
	
	void setFinApplication(Fin.IFinApplication finApplication) {
		this.finApplication = finApplication;
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

	public String getFilter() {
		return this.filter;
	}

}
