package cs2103.aug11.t11j2.fin.datamodel;

import java.io.*;
import java.util.*;

public class Task {

	public enum EImportance {
		LOW, NORMAL, HIGH
	};

	private String taskName;
	private List<String> tags;
	private EImportance importance;
	private Date dueTime;
	private Integer percentageCompleted;
	private Date addTime;
	private UUID uniqId;
	private Integer pIndex;

	public Task(String taskName, List<String> tags, EImportance importance,
			Date dueDate, Integer percentageCompleted,
			Integer pIndex) {

		this.taskName = taskName;
		this.tags = tags;
		this.importance = importance;
		this.dueTime = dueDate;
		this.percentageCompleted = percentageCompleted;
		this.addTime = new Date();
		this.uniqId = UUID.randomUUID();
		this.pIndex = pIndex;
	}

	public Task() {
		this.uniqId = UUID.randomUUID();
		
		this.importance = EImportance.NORMAL;
		this.addTime = new Date();
		this.pIndex = 0;
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

	public void setImportance(EImportance importance) {
		this.importance = importance;
	}

	public EImportance getImportance() {
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

	public UUID getUniqId() {
		return uniqId;
	}

	public void setpIndex(Integer pIndex) {
		this.pIndex = pIndex;
	}

	public Integer getpIndex() {
		return pIndex;
	}
	
	public Map<String, Object> toObject() {
		Map<String, Object> tr = new TreeMap<String, Object>();
		
		tr.put("Name", this.getTaskName());
		tr.put("UID", this.getUniqId());
		tr.put("DateAdded", this.getDateAdded());
		tr.put("Priority", this.getpIndex());
		tr.put("Importance", this.getImportance());

		if (this.getPercentageCompleted() != null) {
			tr.put("Completed", this.getPercentageCompleted());
		}
		if (this.getDueDate() != null) {
			tr.put("DueDate", this.getDueDate());
		}
		
		return tr;
	}

}
