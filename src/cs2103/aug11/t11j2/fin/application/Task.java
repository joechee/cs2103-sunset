package cs2103.aug11.t11j2.fin.application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.log4j.Logger;

import cs2103.aug11.t11j2.fin.parseTask.DateParser;

public class Task {
	
	private String taskName;
	private List<String> tags = new ArrayList<String>();
	private Date timeDue;
	private Date timeAdded;
	private UUID uniqId;
	private boolean finished;
	private boolean important;
	private Logger logger = Logger.getLogger(this.getClass());

	// Constructors
	
	/**
	 * Parses the taskname and creates a task with the appropriate date and task description.
	 *
	 **/
	
	public Task(String taskName) {
		taskName = sanitizeString(taskName);
		
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
		parseTags();
		logger.debug("Task Object created!");
	}
	
	/**
	 * Parses the Task from data from the serializer
	 *
	 **/	

	Task(Map<String, Object> dict) {
		if (dict.get("Name") == null) {
			dict.put("Name","Corrupted task");
		}
		if (dict.get("UID") == null) {
			dict.put("UID", UUID.randomUUID().toString());
		}
		
		
		try {
			this.uniqId = UUID.fromString((String) dict.get("UID"));
		} catch (IllegalArgumentException e) {
			this.uniqId = UUID.randomUUID();
		}

		this.taskName = (String) dict.get("Name");
		try {
			this.timeAdded = (Date) dict.get("DateAdded");
		} catch (ClassCastException e) {
			logger.error("Task date added is corrupted! Trying to repair file...");
			Calendar now = Calendar.getInstance();
			this.timeAdded = now.getTime();
		} 
		
		try {
			this.timeDue = (Date) dict.get("DueDate");
		} catch (ClassCastException e) {
			logger.debug("Task date due is corrupted! Trying to repair file...");
			Calendar now = Calendar.getInstance();
			this.timeDue = now.getTime();
		} 


		parseTags();
		logger.debug("Task Object created from file!");
	}

	// Parsing methods
	
	/**
	 * looks at the current task string, extracts all the valid hash tags and
	 * add it to the list of tags the Task object has
	 */
	private void parseTags() {
		String[] tokens = tokenize(this.getTaskName());

		// parse hashTags of task
		for (String s : tokens) {
			if (isHashTag(s)) {
				this.tags.add(sanitizeHashTag(s));
			}
		}
		
		if (this.hasTag(FinConstants.IMPORTANT_HASH_TAG)) {
			this.important = true;
		}
		
		if (this.hasTag(FinConstants.FIN_HASH_TAG)) {
			this.finished = true;
		}
	}
	/**
	 * Sanitizes the hash tag, i.e. Returns a string that has only the valid characters in a hash tag.
	 * @param s
	 * @return string
	 */

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
	

	public String getEditableTaskName() {
		Date dueDate = this.getDueDate();
		
		String taskName = this.taskName;
		if ((dueDate != null)
				&& (taskName.contains(FinConstants.DUEDATE_PLACEHOLDER))) {
			return unsanitizeString(replaceWithDate(taskName,dueDate));
		} else {
			return getTaskName();
		}		
	}

	
	public String getTaskName() {
		Date dueDate = this.getDueDate();
		String taskName = this.taskName;
		if (dueDate != null) {
			taskName = replaceWithDate(taskName, dueDate);
		} 
		return unsanitizeString(taskName);
	}
	


	private String replaceWithDate(String taskName, Date dueDate) {
		return replaceWord(taskName,FinConstants.DUEDATE_PLACEHOLDER,"["
				+ DateParser.naturalDateFromNow(dueDate) + "]");
		
	}
	
	private String replaceWord(String taskName, String find, String replace) {
		taskName = " "+taskName;
		String returnName = taskName.replace(" "+find, " "+replace);
		if (returnName.equals(taskName)) {
			returnName = taskName.replace(FinConstants.DUEDATE_PLACEHOLDER + " ", replace+" ");
		} 
		if (returnName.equals(taskName)) {
			returnName = taskName.replace(FinConstants.DUEDATE_PLACEHOLDER, replace);			
		}
		
		if (returnName.equals(taskName)) {
			logger.error("No String replaced even though date was in! Possible sync issue");
		}
		
		return returnName.substring(1);
	}
	
	


	boolean addTag(String tag) {
		if (hasTag(tag)) {
			return false;
		}
		String sanitizedTag = sanitizeHashTag(tag);
		this.tags.add(sanitizedTag);
		this.taskName = this.taskName + " " + FinConstants.HASH_TAG_CHAR + sanitizedTag;
		return true;
	}

	void edit(String taskName) {
		assert(taskName!=null);
		taskName = sanitizeString(taskName);
		DateParser dateParser = new DateParser();
		Date dueDate = null;
		tags.clear();
		boolean parsed = dateParser.parse(taskName);

		if (parsed) {
			taskName = dateParser.getParsedString();
			dueDate = dateParser.getParsedDate();
		}

		this.taskName = taskName;
		this.timeDue = dueDate;

		parseTags();
	}

	boolean removeTag(String tag) {
		if (this.tags.remove(tag.toLowerCase().trim())) {
			this.taskName = " " + this.taskName;			
			this.taskName = this.taskName.replaceAll("(?i)"
					+ " "+FinConstants.HASH_TAG_CHAR + tag.toLowerCase() + "\\s*",
					"");
			if (this.taskName.startsWith(" ")) {
				this.taskName = this.taskName.substring(1);
			}

		}
		return true;
	}

	public List<String> getTags() {
		List<String> newTags = new ArrayList<String>();
		for (String t : tags) {
			newTags.add(t);
		}
		return newTags;
	}

	void setDueDate(Date dueDate) {
		this.timeDue = dueDate;
		if (!this.taskName.contains(FinConstants.DUEDATE_PLACEHOLDER)) {
			this.taskName = this.taskName.concat(" "
					+ FinConstants.DUEDATE_PLACEHOLDER);
		}
	}

	void setDueDate(String string) {
		DateParser dp = new DateParser();
		boolean parsed = dp.parse(string);

		if (parsed) {
			this.setDueDate(dp.getParsedDate());
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


		if (this.getDueDate() != null) {
			tr.put("DueDate", this.getDueDate());
		}

		return tr;
	}

	boolean fin() {
		this.finished = true;
		return this.addTag(FinConstants.FIN_HASH_TAG);
	}

	boolean unfin() {
		this.finished = false;
		return this.removeTag(FinConstants.FIN_HASH_TAG);
	}

	void flag() {
		this.important = true;
		this.addTag(FinConstants.IMPORTANT_HASH_TAG);
	}

	void unflag() {
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
	
	private String sanitizeString(String s) {
		s = s.replace(Character.toString(FinConstants.ESCAPE_CHAR), 
				Character.toString(FinConstants.ESCAPE_CHAR)+FinConstants.ESCAPE_CHAR);
		s = s.replace(FinConstants.DUEDATE_PLACEHOLDER, FinConstants.ESCAPE_CHAR + FinConstants.DUEDATE_PLACEHOLDER);
		return s;
	}
	
	private String unsanitizeString(String s) {
		s = s.replace(FinConstants.ESCAPE_CHAR + FinConstants.DUEDATE_PLACEHOLDER,FinConstants.DUEDATE_PLACEHOLDER);
		s = s.replace(Character.toString(FinConstants.ESCAPE_CHAR)+FinConstants.ESCAPE_CHAR, 
				Character.toString(FinConstants.ESCAPE_CHAR));
		return s;
	}

	@Override
	public String toString() {
		return getTaskName();
	}
}