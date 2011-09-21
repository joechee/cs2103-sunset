package DataModel;

import java.io.*;
import java.util.*;


public class Task implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4205418282669977591L;
	
	public enum Importance {LOW, NORMAL, HIGH};
	
	private String taskName;
	private List<String> tags;
	private Importance importance;
	private Date dueTime;
	private Integer percentageCompleted;
	private Date addTime;
	private String uniqId;
	private Integer pIndex;
	
	public Task (String taskName, List<String> tags, Importance importance, Date dueDate, Integer percentageCompleted, Date dateAdded, String uniqId, Integer pIndex) {
		this.taskName = taskName;
		this.tags = tags;
		this.importance = importance;
		this.dueTime = dueDate;
		this.percentageCompleted = percentageCompleted;
		this.addTime = dateAdded;
		this.uniqId = uniqId;
		this.pIndex = pIndex;
	}
	
	public Task (String uniqId) {
		this.uniqId = uniqId;
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setImportance(Importance importance) {
		this.importance = importance;
	}
	public Importance getImportance() {
		return importance;
	}
	public void setDueDate(Date dueDate) {
		this.dueTime = dueDate;
	}
	public Date getDueDate() {
		return dueTime;
	}
	public void setPercentageCompleted(Integer percentageCompleted) {
		this.percentageCompleted = percentageCompleted;
	}
	public Integer getPercentageCompleted() {
		return percentageCompleted;
	}
	public void setDateAdded(Date dateAdded) {
		this.addTime = dateAdded;
	}
	public Date getDateAdded() {
		return addTime;
	}
	public String getUniqId() {
		return uniqId;
	}
	public void setpIndex(Integer pIndex) {
		this.pIndex = pIndex;
	}
	public Integer getpIndex() {
		return pIndex;
	}
	
	

}
