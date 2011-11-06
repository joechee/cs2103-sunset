package cs2103.aug11.t11j2.fin.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import cs2103.aug11.t11j2.fin.application.Fin;
import cs2103.aug11.t11j2.fin.application.Task;

/**
 * Context to hold state specific to the UI.
 * 
 * @author Alex Liew
 *
 */
public class UIContext {
	private List<Task> taskList = new ArrayList<Task>();
	private String filter = "";
	
	private Fin.IFinApplication finApplication = null;
	
	private Display display = null;
	
	UIContext(Fin.IFinApplication finApplication) {
		this.finApplication = finApplication;
	}
	
	UIContext() {
		this(null);
	}

	void setDisplay(Display display) {
		this.display = display;
	}
	
	public Display getDisplay() {
		return this.display;
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

	void setFilter(String filter) {
		this.filter = filter;
	}

	public String getFilter() {
		return this.filter;
	}

}
