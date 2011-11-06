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
/**
 * Each task object models a task within the environment or memory. Mutation of the task 
 * internals can only be done with  * package private methods within Task. Only accessor 
 * methods are public.
 * 
 * @author Joe Chee
 *
 */
public class Task {

	private String parsedTaskName;
	private List<String> tags = new ArrayList<String>();
	private Date timeDue;
	private Date timeAdded;
	private UUID uniqId;
	private boolean finished;
	private boolean important;
	private Logger logger = Logger.getLogger(this.getClass());

	// Constructors

	/**
	 * Parses the taskname and creates a task with the appropriate date and task
	 * description.
	 * 
	 * @param taskName
	 *            - the input string containing the unparsed task
	 **/
	public Task(String taskName) {
		// prevent abuse of special characters
		taskName = sanitizeString(taskName);

		DateParser dateParser = new DateParser();
		Date dueDate = null;
		boolean hasDate = dateParser.parse(taskName);

		if (hasDate) {
			this.parsedTaskName = dateParser.getParsedString();
			dueDate = dateParser.getParsedDate();
		} else {
			this.parsedTaskName = taskName;
		}

		this.timeDue = dueDate;
		this.uniqId = UUID.randomUUID();
		this.timeAdded = new Date();
		parseTags();

		logger.debug("Task Object created!");
	}

	/**
	 * Parses the Task from data from the serializer.
	 * 
	 * @param dict
	 *            - the Map containing the data for the task
	 **/
	Task(Map<String, Object> dict) {
		if (dict.get("Name") == null) {
			dict.put("Name", "Corrupted task");
		}
		if (dict.get("UID") == null) {
			dict.put("UID", UUID.randomUUID().toString());
		}

		try {
			this.uniqId = UUID.fromString((String) dict.get("UID"));
		} catch (IllegalArgumentException e) {
			this.uniqId = UUID.randomUUID();
		}

		this.parsedTaskName = (String) dict.get("Name");
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
	 * Extracts all the valid hash tags from the current task name and adds it
	 * to the list of tags the Task object has
	 */
	private void parseTags() {
		String[] tokens = tokenize(this.getTaskName());

		// Adds parsed hashTags to the Task
		for (String s : tokens) {
			if (isHashTag(s)) {
				this.tags.add(sanitizeHashTag(s));
			}
		}

		// Flags task as Important if #impt tag is found
		if (this.hasTag(FinConstants.IMPORTANT_HASH_TAG)) {
			this.important = true;
		}

		// Flags task as Finished if #fin tag is found
		if (this.hasTag(FinConstants.FIN_HASH_TAG)) {
			this.finished = true;
		}
	}

	/**
	 * Sanitizes the hash tag, i.e. Returns a string that has only the valid
	 * characters in a hash tag.
	 * 
	 * @param s
	 *            - the string to be sanitized
	 * @return the sanitized string
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

	/**
	 * Tokenizes the task string, delimited by spaces.
	 * 
	 * @param task
	 *            - the task name for the task
	 * @return an array of tokens for the task name
	 */
	private static String[] tokenize(String task) {
		return task.trim().split("\\s");
	}

	/**
	 * Checks if the given string is a hashTag.
	 * 
	 * @param s
	 *            - the String to be checked
	 * @return true if String is a valid hash tag, false otherwise
	 */
	public static boolean isHashTag(String s) {
		if (s.isEmpty()) {
			return false;
		}

		char firstChar = s.charAt(0);
		boolean hasHashTag = (firstChar == FinConstants.HASH_TAG_CHAR);

		if (hasHashTag) {
			// ensures that hashtag comprises only letters or digits
			for (int i = 1; i < s.length(); ++i) {
				char c = s.charAt(i);
				if (!Character.isLetterOrDigit(c)) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	// Setter and Getter methods

	/**
	 * Returns unparsed but editable version of the task name.
	 * 
	 * @return the task name that represents the task object
	 */
	public String getEditableTaskName() {
		Date dueDate = this.getDueDate();

		if ((dueDate != null)
				&& (parsedTaskName.contains(FinConstants.DUEDATE_PLACEHOLDER))) {
			return unsanitizeString(replaceWord(this.parsedTaskName,
					FinConstants.DUEDATE_PLACEHOLDER,
					"due " + DateParser.naturalDateFromNow(dueDate)));
		} else {
			return getTaskName();
		}
	}

	/**
	 * Returns the unparsed task name.
	 * 
	 * @return the unparsed task name
	 */
	public String getTaskName() {
		Date dueDate = this.getDueDate();
		String taskName = "";
		if (dueDate != null) {
			taskName = replaceWithDate(this.parsedTaskName, dueDate);
		} else {
			taskName = this.parsedTaskName;
		}
		return unsanitizeString(taskName);
	}

	/**
	 * Replaces the date placeholder with the relative date from now.
	 * 
	 * @param parsedTaskName
	 *            - the parsed task name
	 * @param dueDate
	 *            - the date that the task is due
	 * @return the unparsed task name with the date placeholder replaced by the
	 *         relative date that the task is due
	 */
	private String replaceWithDate(String parsedTaskName, Date dueDate) {
		String unparsedTaskName = replaceWord(parsedTaskName,
				FinConstants.DUEDATE_PLACEHOLDER,
				"[" + DateParser.naturalDateFromNow(dueDate) + "]");

		if (parsedTaskName.equals(unparsedTaskName)) {
			logger.error("No string replaced even though date was in! Possible sync issue.");
		}
		return unparsedTaskName;

	}

	/**
	 * Replaces the given search string with the replacement string in the task
	 * name.
	 * 
	 * @param taskName
	 *            - the parsed/unparsed task name
	 * @param find
	 *            - the search string
	 * @param replace
	 *            - the replacement string
	 * @return the given task name with replacements done, if any
	 */
	private String replaceWord(String taskName, String find, String replace) {
		// pad with additional whitespace to ensure we do not replace something
		// at the start or end of a word
		taskName = " " + taskName;
		String returnName = taskName.replace(" " + find, " " + replace);

		// defensive check
		if (returnName.equals(taskName)) {
			returnName = taskName.replace(find + " ", replace + " ");
		}
		if (returnName.equals(taskName)) {
			returnName = taskName.replace(find, replace);
		}

		// returns the taskName with compensation for the whitespace added
		// initially
		return returnName.substring(1);
	}

	/**
	 * Replaces escape characters and due date placeholders that may be in the
	 * input string.</br> Used to sanitize the task name.
	 * 
	 * @param s
	 *            - string to be sanitized
	 * @return string that has been sanitized
	 */
	private String sanitizeString(String s) {
		s = s.replace(Character.toString(FinConstants.ESCAPE_CHAR),
				Character.toString(FinConstants.ESCAPE_CHAR)
						+ FinConstants.ESCAPE_CHAR);
		s = s.replace(FinConstants.DUEDATE_PLACEHOLDER,
				FinConstants.ESCAPE_CHAR + FinConstants.DUEDATE_PLACEHOLDER);
		return s;
	}

	/**
	 * Replaces the sanitized text with what should be the input text.</br> Used
	 * to unsanitize the task name.
	 * 
	 * @param s
	 *            - string that has been sanitized
	 * @return string that has been restored to pre-sanitized state.
	 */
	private String unsanitizeString(String s) {
		s = s.replace(FinConstants.ESCAPE_CHAR
				+ FinConstants.DUEDATE_PLACEHOLDER,
				FinConstants.DUEDATE_PLACEHOLDER);
		s = s.replace(Character.toString(FinConstants.ESCAPE_CHAR)
				+ FinConstants.ESCAPE_CHAR,
				Character.toString(FinConstants.ESCAPE_CHAR));
		return s;
	}

	/**
	 * Adds a tag to the task if it does not already have it.
	 * 
	 * @param tag
	 *            - tag to be added
	 * @return true, if tag was added successfully, false if tag was already
	 *         present
	 */
	boolean addTag(String tag) {
		String sanitizedTag = sanitizeHashTag(tag);

		if (hasTag(sanitizedTag)) {
			return false;
		} else {
			this.tags.add(sanitizedTag);
			this.parsedTaskName = this.parsedTaskName + " "
					+ FinConstants.HASH_TAG_CHAR + sanitizedTag;
			return true;
		}
	}

	/**
	 * Edits the task name of the Task.
	 * 
	 * @param taskName
	 *            - the new task name for the Task
	 */
	void edit(String taskName) {
		assert (taskName != null);

		// prevent abuse of special characters
		taskName = sanitizeString(taskName);
		DateParser dateParser = new DateParser();
		Date dueDate = null;
		tags.clear();
		boolean hasDate = dateParser.parse(taskName);

		if (hasDate) {
			this.parsedTaskName = dateParser.getParsedString();
			dueDate = dateParser.getParsedDate();
		} else {
			this.parsedTaskName = taskName;
		}
		this.timeDue = dueDate;
		this.timeAdded = new Date();
		parseTags();
	}

	/**
	 * Removes the given tag from the task if it has it.
	 * 
	 * @param tag
	 *            - the tag to be removed
	 * @return true, if the Task has the tag, false otherwise
	 */
	boolean removeTag(String tag) {
		String sanitizedTag = sanitizeHashTag(tag);

		if (this.tags.remove(sanitizedTag.toLowerCase().trim())) {
			this.parsedTaskName = " " + this.parsedTaskName;
			this.parsedTaskName = this.parsedTaskName.replaceAll("\\s"
					+ FinConstants.HASH_TAG_CHAR + sanitizedTag.toLowerCase(),
					" ");

			// compensate for added whitespace
			this.parsedTaskName = this.parsedTaskName.substring(1);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the list of tags associated with the task.
	 * 
	 * @return tags associated with this task
	 */
	public List<String> getTags() {
		List<String> newTags = new ArrayList<String>();
		for (String t : tags) {
			newTags.add(t);
		}
		return newTags;
	}

	/**
	 * Sets the due date to the date specified by the argument and adds a due
	 * date placeholder if not already present.
	 * 
	 * @param dueDate
	 *            - the new due date
	 * @return true if the task already has a due date, false otherwise
	 */
	boolean setDueDate(Date dueDate) {
		this.timeDue = dueDate;
		boolean hasDueDate = this.parsedTaskName
				.contains(FinConstants.DUEDATE_PLACEHOLDER);

		if (!hasDueDate) {
			this.parsedTaskName = this.parsedTaskName.concat(" "
					+ FinConstants.DUEDATE_PLACEHOLDER);
		}
		return hasDueDate;
	}

	/**
	 * Sets the due date if the string passed in can be parsed to a valid date.
	 * 
	 * @param string
	 *            - the string to be parsed into a date
	 * @return true if the string passed in can be parsed into a date, false
	 *         otherwise
	 */
	boolean setDueDate(String string) {
		DateParser dp = new DateParser();
		boolean isValidDate = dp.parse(string);

		if (isValidDate) {
			this.setDueDate(dp.getParsedDate());
		}
		return isValidDate;
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

		tr.put("Name", this.parsedTaskName);
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

	@Override
	public String toString() {
		return getTaskName();
	}
}