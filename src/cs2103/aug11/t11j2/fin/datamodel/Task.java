package cs2103.aug11.t11j2.fin.datamodel;

import java.io.*;
import java.util.*;

public class Task {

	public enum EImportance {
		LOW("Low"), NORMAL("Normal"), HIGH("Important");

		String importance;

		EImportance(String importance) {
			this.importance = importance;
		}

		@Override
		public String toString() {
			return importance;
		}

		private static final Map<String, EImportance> stringToEnum = new HashMap<String, EImportance>();
		static {
			for (EImportance impt : values()) {
				stringToEnum.put(impt.toString(), impt);
			}
		}

		public static EImportance fromString(String importance) {
			EImportance tr = stringToEnum.get(importance);
			if (tr == null) {
				return NORMAL;
			} else {
				return tr;
			}
		}
	};

	private String taskName;
	private List<String> tags = new ArrayList<String>();
	private EImportance importance;
	private Date dueTime;
	private Integer percentageCompleted;
	private Date addTime;
	private UUID uniqId;
	private Integer pIndex;

	public Task(String taskName, List<String> tags, EImportance importance,
			Date dueDate, Integer percentageCompleted, Integer pIndex) {

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
		this.percentageCompleted = 0;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskName() {
		return taskName;
	}

	private static String formatTag(String tag) {
		/**
		 * Ensures tag are lowercases without spaces
		 */
		return tag.toLowerCase().split("\\s+")[0].trim();
	}
	public void addTag(String tag) {
		addTag(formatTag(tag));
	}
	public boolean hasTag(String tag) {
		return this.tags.contains(tag.toLowerCase().trim());
	}
	public boolean hasTags(List<String> tags) {
		boolean has = true;
		for (String t : tags) {
			if (hasTag(t) == false) {
				has = false;
				break;
			}
		}
		return has;
	}
	public void removeTag(String tag) {
		this.tags.remove(tag.toLowerCase().trim());
	}
	public List<String> getTags() {
		List<String> newTags = new ArrayList<String>();
		Collections.copy(newTags, tags);
		return newTags;
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

	public Task(Map<String, Object> dict) {
		this.taskName = (String) dict.get("Name");
		this.uniqId = UUID.fromString((String) dict.get("UID"));
		this.addTime = (Date) dict.get("DateAdded");
		this.pIndex = (Integer) dict.get("Priority");
		this.importance = EImportance.fromString( (String) dict.get("Importance") );
		this.percentageCompleted = (Integer) dict.get("Completed");
		this.dueTime = (Date) dict.get("DueDate");
	}

	public Map<String, Object> toDictionary() {
		Map<String, Object> tr = new TreeMap<String, Object>();

		tr.put("Name", this.getTaskName());
		tr.put("UID", this.getUniqId().toString());
		tr.put("DateAdded", this.getDateAdded());
		tr.put("Priority", this.getpIndex());
		tr.put("Importance", this.getImportance().importance);
		tr.put("Completed", this.getPercentageCompleted());

		if (this.getDueDate() != null) {
			tr.put("DueDate", this.getDueDate());
		}

		return tr;
	}
	
	public void fin() {
		this.setPercentageCompleted(100);
	}

}
