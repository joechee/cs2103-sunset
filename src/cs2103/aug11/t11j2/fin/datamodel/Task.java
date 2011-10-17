package cs2103.aug11.t11j2.fin.datamodel;

import java.util.*;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.parseTask.DateParser;

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
			for (EImportance importanceLevel : EImportance.values()) {
				stringToEnum.put(importanceLevel.toString(), importanceLevel);
			}
		}

		public static EImportance fromString(String importance) {
			EImportance importanceLevel = stringToEnum.get(importance);
			if (importanceLevel == null) {
				return NORMAL;
			} else {
				return importanceLevel;
			}
		}
	};

	private String taskName;
	private List<String> tags = new ArrayList<String>();
	private Date timeDue;
	private Date timeAdded;
	private UUID uniqId;
	private boolean finished;
	private boolean important;

	// Constructors
	
	public Task() {
		this.uniqId = UUID.randomUUID();
		this.timeAdded = new Date();
		this.finished = false;
		this.important = false;
	}
	
	public Task(String taskName) throws IllegalArgumentException{
		if (!validateTaskName(taskName)) {
			throw new IllegalArgumentException("Invalid string within task description!");
		}
		DateParser dateParser = new DateParser();
		Date dueDate = null;
		boolean parsed = dateParser.parse(taskName);

		if (parsed) {
			taskName = dateParser.getParsedString();
			dueDate = dateParser.getParsedDate();
		}

		this.taskName = taskName;
		this.timeDue = dueDate;
		this.uniqId = UUID.randomUUID();
		this.timeAdded = new Date();
		this.finished = false;
		this.important = false;		

		parseTags();
	}

	private boolean validateTaskName(String s) {
		if (s.contains(FinConstants.DUEDATE_PLACEHOLDER)){
			return false;
		}
		return true;
	}

	public Task(Map<String, Object> dict) {
		this.taskName = (String) dict.get("Name");
		this.uniqId = UUID.fromString((String) dict.get("UID"));
		this.timeAdded = (Date) dict.get("DateAdded");
		this.timeDue = (Date) dict.get("DueDate");
		this.finished = (Boolean) dict.get("Finished");
		this.important = (Boolean) dict.get("Important");

		parseTags();
	}

	// Parsing methods
	
	/**
	 * looks at the current task string, extracts all the valid hash tags and
	 * add it to the list of tags the Task object has
	 */
	private void parseTags() {
		String[] tokens = tokenize(this.taskName);

		// parse hashTags of task
		for (String s : tokens) {
			if (isHashTag(s)) {
				this.tags.add(sanitizeHashTag(s));
			}
		}
	}

	public static String sanitizeHashTag(String s) {  
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			if (Character.isLetterOrDigit(c)) {
				stringBuilder.append(Character.toLowerCase(c));
			}
		}

		return stringBuilder.toString();
	}

	private static String[] tokenize(String task) {
		return task.trim().split("\\s");
	}

	public static boolean isHashTag(String s) {
		if (s.isEmpty()) {
			return false;
		}
		
		boolean hasHashTag = s.charAt(0) == FinConstants.HASH_TAG_CHAR;

		if (!hasHashTag) {
			return false;
		}

		// ensures that hashtag comprises only letters or digits
		for (int i = 1; i < s.length(); ++i) {
			char c = s.charAt(i);
			if (!Character.isLetterOrDigit(c)) {
				return false;
			}
		}

		return true;
	}

	// Setter and Getter methods
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskName() {
		Date dueDate = this.getDueDate();
		if ((dueDate != null)
				&& (taskName.contains(FinConstants.DUEDATE_PLACEHOLDER))) {
			return taskName.replace(FinConstants.DUEDATE_PLACEHOLDER, "["
					+ DateParser.naturalDateFromNow(dueDate) + "]");
		} else {
			return taskName;
		}
	}

	public boolean addTag(String tag) {
		if (hasTag(tag)) {
			return false;
		}
		String sanitizedTag = sanitizeHashTag(tag);

		this.tags.add(sanitizedTag);
		this.taskName = this.taskName.trim() + " " + FinConstants.HASH_TAG_CHAR
				+ sanitizedTag;
		return true;
	}

	public void removeTag(String tag) {
		if (this.tags.remove(tag.toLowerCase().trim())) {
			this.taskName = this.taskName.replaceAll("(?i)"
					+ FinConstants.HASH_TAG_CHAR + tag.toLowerCase() + "\\s*",
					"");
		}
	}

	public List<String> getTags() {
		List<String> newTags = new ArrayList<String>();
		for (String t : tags) {
			newTags.add(t);
		}
		return newTags;
	}

	public void setDueDate(Date dueDate) {
		this.timeDue = dueDate;
		if (!this.taskName.contains(FinConstants.DUEDATE_PLACEHOLDER)) {
			this.taskName = this.taskName.concat(" "
					+ FinConstants.DUEDATE_PLACEHOLDER);
		}
	}

	public void setDueDate(String string) {
		DateParser dp = new DateParser();
		boolean parsed = dp.parse(string);

		if (parsed) {
			this.setDueDate(dp.getParsedDate());
		}
	}

	public void removeDueDate() {
		this.timeDue = null;
		if (this.taskName.contains(FinConstants.DUEDATE_PLACEHOLDER)) {
			this.taskName = this.taskName.replace(
					FinConstants.DUEDATE_PLACEHOLDER, "").trim();
		}
	}

	public Date getDueDate() {
		return timeDue;
	}

	public void setDateAdded(Date dateAdded) {
		this.timeAdded = dateAdded;
	}

	public Date getDateAdded() {
		return timeAdded;
	}

	public UUID getUniqId() {
		return uniqId;
	}

	public Map<String, Object> toDictionary() {
		Map<String, Object> tr = new TreeMap<String, Object>();

		tr.put("Name", this.taskName);
		tr.put("UID", this.getUniqId().toString());
		tr.put("DateAdded", this.getDateAdded());
		tr.put("Finished", this.isFin());
		tr.put("Important", this.isImportant());

		if (this.getDueDate() != null) {
			tr.put("DueDate", this.getDueDate());
		}

		return tr;
	}

	public boolean fin() {
		this.finished = true;
		return this.addTag(FinConstants.FIN_HASH_TAG);
	}

	public void unfin() {
		this.finished = false;
		this.removeTag(FinConstants.FIN_HASH_TAG);
	}

	public void flag() {
		this.important = true;
		this.addTag(FinConstants.IMPORTANT_HASH_TAG);
	}

	public void unflag() {
		this.important = false;
		this.removeTag(FinConstants.IMPORTANT_HASH_TAG);
	}
	
	// Query methods
	
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
	
	public boolean isFin() {
		return this.finished;
	}

	public boolean isImportant() {
		return this.important;
	}

	@Override
	public String toString() {
		return getTaskName();
	}
}