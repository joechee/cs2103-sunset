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

	public Task(String taskName) {
		DateParser dp = new DateParser();
		Date due = null;
		if (dp.parse(taskName)) {
			taskName = dp.getParsedString();
			due = dp.getParsedDate();
		}

		this.taskName = taskName;
		this.dueTime = due;
		this.percentageCompleted = this.pIndex = 0;
		this.uniqId = UUID.randomUUID();
		this.addTime = new Date();

		parseTags();
	}

	Task(String taskName, EImportance importance, Date dueDate,
			Integer percentageCompleted, Integer pIndex) {

		this.taskName = taskName;
		this.importance = importance;
		this.dueTime = dueDate;
		this.percentageCompleted = percentageCompleted;
		this.addTime = new Date();
		this.uniqId = UUID.randomUUID();
		this.pIndex = pIndex;

		parseTags();

	}

	public Task(Map<String, Object> dict) {
		this.taskName = (String) dict.get("Name");
		this.uniqId = UUID.fromString((String) dict.get("UID"));
		this.addTime = (Date) dict.get("DateAdded");
		this.pIndex = (Integer) dict.get("Priority");
		this.importance = EImportance.fromString((String) dict
				.get("Importance"));
		this.percentageCompleted = (Integer) dict.get("Completed");
		this.dueTime = (Date) dict.get("DueDate");

		parseTags();
	}

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

	private static String sanitizeHashTag(String s) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			if (Character.isLetterOrDigit(c)) {
				sb.append(Character.toLowerCase(c));
			}
		}

		return sb.toString();
	}

	private static String[] tokenize(String task) {
		return task.trim().split("\\s");
	}

	private static boolean isHashTag(String s) {
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
		Date due = this.getDueDate();
		if (due != null && taskName.contains(FinConstants.DUEDATE_PLACEHOLDER)) {
			return taskName.replace(FinConstants.DUEDATE_PLACEHOLDER,
					"(" + DateParser.naturalDateFromNow(due) + ")");
		} else {
			return taskName;
		}
	}

	public boolean addTag(String tag) {
		if (hasTag(tag)) return false;
		String sanitizedTag = sanitizeHashTag(tag);

		this.tags.add(sanitizedTag);
		this.taskName = this.taskName.trim() + " " + FinConstants.HASH_TAG_CHAR + sanitizedTag;
		return true;
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
		if (this.tags.remove(tag.toLowerCase().trim())) {
			this.taskName = this.taskName.replaceAll(
					"(?i)"+FinConstants.HASH_TAG_CHAR + tag.toLowerCase() + "\\s*", "");
		}
	}

	public List<String> getTags() {
		List<String> newTags = new ArrayList<String>();
		for (String t : tags) {
			newTags.add(t);
		}
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

	public Map<String, Object> toDictionary() {
		Map<String, Object> tr = new TreeMap<String, Object>();

		tr.put("Name", this.taskName);
		tr.put("UID", this.getUniqId().toString());
		tr.put("DateAdded", this.getDateAdded());
		tr.put("Priority", this.getpIndex());
		// tr.put("Importance", this.getImportance().importance);
		tr.put("Completed", this.getPercentageCompleted());

		if (this.getDueDate() != null) {
			tr.put("DueDate", this.getDueDate());
		}

		return tr;
	}

	@Override
	public String toString() {
		return getTaskName();
	}

	public boolean fin() {
		this.setPercentageCompleted(100);
		return this.addTag(FinConstants.FIN_HASH_TAG);
	}

	public void unfin() {
		this.setPercentageCompleted(0);
		this.removeTag(FinConstants.FIN_HASH_TAG);
	}

	public void flag() {
		this.addTag(FinConstants.IMPORTANT_HASH_TAG);
	}

	public void unflag() {
		this.removeTag(FinConstants.IMPORTANT_HASH_TAG);
	}

	public boolean isFin() {
		return this.hasTag(FinConstants.FIN_HASH_TAG);
	}
	
	public boolean isImportant() {
		return this.hasTag(FinConstants.IMPORTANT_HASH_TAG);
	}

}
