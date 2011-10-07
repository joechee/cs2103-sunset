package cs2103.aug11.t11j2.fin.parseTask;

import java.util.*;
import java.math.*;
import java.io.*;
import cs2103.aug11.t11j2.fin.datamodel.Task;

public class TaskParser {

	/**
	 * @param args
	 */

	final private static String[] DAYS_OF_WEEK = { "MONDAY", "TUESDAY",
			"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY" };
	final private static String[] MONTHS = { "JANUARY", "FEBRUARY", "MARCH",
			"APRIL", "MAY", "JUNE", "JULY", "AUGST", "SEPTEMBER", "OCTOBER",
			"NOVEMBER", "DECEMBER" };
	final private static String WEEK_NEXT = "NEXT";
	final private static String WEEK_THIS = "THIS";
	final private static String WEEK = "WEEK";
	final private static String PERIOD_TODAY = "TODAY";
	final private static String PERIOD_TOMORROW = "TOMORROW";
	final private static String PERIOD_IN = "IN";
	final private static String PERIOD_DAY = "DAY";
	final private static String PERIOD_WEEK = "WEEK";
	final private static String PERIOD_MONTH = "MONTH";
	final private static int DAYS_A_WEEK = 7;
	final private static int MONTHS_A_YEAR = 12;
	final private static int DAYS_A_MONTH = 31; // maximum;
	final private static int HOURS_A_DAY = 24;
	final private static int MINUTES_AN_HOUR = 60;
	final private static int SECONDS_A_MINUTE = 60;
	final private static int SUNDAY = 6;
	final private static int NUM_INVALID = -1;
	final private static int NOT_IN_STRING = -1;
	final private static int DEFAULT_PERCENTAGE = 0;
	final private static int DEFAULT_PRIORITY = 0;
	final private static String EMPTY_STRING = "";
	final private static String[] KEYWORDS = { "#", "DUE ", "BY ", "BEFORE ",
			"^", "@", "$", "%" }; // taskName or Tag, dueTime, dueTime,
									// ^importance, @Location, *priority,
									// %percentageComplete;
	final private static int NUM_KEYWORDS = 8;
	final private static String TIME_DONT_CARE = "*";
	final private static String IMPORTANCE_LOW = "LOW";
	final private static String IMPORTANCE_HIGH = "HIGH";
	final private static String IMPORTANCE_NORMAL = "NORMAL";
	final private static String IMPORTANCE_IMPORTANT = "IMPORTANT";
	
	final private static String WARNING_INVALID_DAY_OF_WEEK = "Warning: Invalid Day of Week";
	final private static String WARNING_INVALID_MONTH = "Warning: Invalid Month";
	final private static String ERROR_NO_TAG = "Invalid Task: Please add tags, each word of which starsts with #";
	final private static String ERROR_NO_TIME = "Invalid Task: Please indicate a time in the form of HH:MM:SS or HH:MM or *(not specified)";
	final private static String ERROR_NO_DATE = "Invalid Task: Please indicate a date in the form of MM DD YYYY or MM DD or Next/This Day_Of_Week";
	final private static String ERROR_NO_PRIORITY = "invalid Task: Please indicate a priority, which is bracked number (X)";

	private TaskParser() {

	}

	public static Task parse(String str) {
		String taskName = new String();
		List<String> tags = new ArrayList<String>();
		Task.EImportance importance = Task.EImportance.NORMAL;
		Date dueTime = new Date();
		int percentage = DEFAULT_PERCENTAGE;
		int pIndex = DEFAULT_PRIORITY;

		str = str.trim();

		int i;
		for (i = 0; i < NUM_KEYWORDS; i++)
			if (str.toUpperCase().indexOf(KEYWORDS[i]) == 0)
				break;
		if (i == NUM_KEYWORDS)
			str = "#" + str;

		boolean havTaskName = false;

		while (!str.isEmpty()) {
			for (i = 0; i < NUM_KEYWORDS; i++)
				if (str.toUpperCase().indexOf(KEYWORDS[i]) == 0)
					break;
			switch (i) {
			case 0:
				if (!havTaskName) {
					taskName = getTaskName(str);
					havTaskName = true;
				} else
					addTags(tags, str);
				str = removeTillKeywords(str);
				break;
			case 1:
				dueTime = getDueTime(str);
				str = removeTillKeywords(str);
				break;
			case 2:
				dueTime = getDueTime(str);
				str = removeTillKeywords(str);
				break;
			case 3:
				dueTime = getDueTime(str);
				str = removeTillKeywords(str);
				break;
			case 4:
				int importanceIndex = getImportanceIndex(str);
				importance = getImportance(importanceIndex);
				str = removeTillKeywords(str);
				break;
			case 5:
			case 6:
				pIndex = getPriority(str);
				str = removeTillKeywords(str);
				break;
			case 7:
			default:
				str = removeTillKeywords(str);
			}
			str = str.trim();
		}

		System.out.println(taskName);
		for (i = 0; i < tags.size(); i++)
			System.out.print(tags.get(i) + ", ");
		System.out.println();
		System.out.println(importance);
		System.out.println(dueTime);
		System.out.println(percentage);
		System.out.println(pIndex);
		Task task = new Task(taskName, tags, importance, dueTime, percentage,
				pIndex);
		return task;
	}

	protected static String removeTillKeywords(String str) {
		int index = getNextKeyword(str);
		if (index == str.length())
			return EMPTY_STRING;
		str = str.substring(index);
		return str;
	}

	protected static void addTags(List<String> tags, String str) {
		int index = getNextKeyword(str);
		str = str.substring(0, index);
		str = str.trim();
		tags.add(str);
	}

	protected static int getNextKeyword(String str) {
		str = str.toUpperCase();
		int plus = getFirstToken(str).length()+1; //the length of the first token plus a white space
		str = removeFirstToken(str);
		if(str.isEmpty())
			return plus+NOT_IN_STRING;
		int i, index, min = str.length();
		for (i = 0; i < NUM_KEYWORDS; i++) {
			index = str.indexOf(KEYWORDS[i]);
			if (index == NOT_IN_STRING)
				index = str.length();
			if (index < min)
				min = index;
		}
		return min + plus;
	}

	protected static String getTaskName(String str) {
		int index = getNextKeyword(str);
		str = str.substring(0, index);
		str = str.trim();
		return str;
	}

	protected static int getImportanceIndex(String str) {
		int index = getNextKeyword(str);
		str = str.substring(1, index);
		str = str.trim();
		if (str.isEmpty())
			return -1;
		String importance = getFirstToken(str).toUpperCase();
		if (IMPORTANCE_LOW.indexOf(importance)==0)
			return 0;
		else if (IMPORTANCE_NORMAL.indexOf(importance)==0)
			return 1;
		else if (IMPORTANCE_HIGH.indexOf(importance)==0)
			return 2;
		else if (IMPORTANCE_IMPORTANT.indexOf(importance)==0)
			return 2;	
		else
			return -1;
	}

	protected static Task.EImportance getImportance(int index) {
		if (index == 1 || index == -1)
			return Task.EImportance.NORMAL;
		else if (index == 0)
			return Task.EImportance.LOW;
		else
			return Task.EImportance.HIGH;
	}

	protected static Date getDueTime(String str) {
		str=str.toUpperCase();
		int index = getNextKeyword(str);
		str = str.substring(0, index);
		str = removeFirstToken(str);
		str = str.trim();
		Calendar cal = Calendar.getInstance();
		Date date;
		boolean timeIndicated = setTime(cal, str);
		if (timeIndicated)
			str = removeFirstToken(str);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		setDate(cal, str);
		date = cal.getTime();
		return date;

	}

	protected static boolean setTime(Calendar cal, String str) {
		String strTime = getFirstToken(str);
		if(strTime.isEmpty())return false;
		if (strTime.equals(TIME_DONT_CARE)) {
			cal.set(Calendar.HOUR_OF_DAY, HOURS_A_DAY - 1);
			cal.set(Calendar.MINUTE, MINUTES_AN_HOUR - 1);
			cal.set(Calendar.SECOND, SECONDS_A_MINUTE - 1);
			return true;
		} else {
			int hour, minute, second;
			int index = strTime.indexOf(':');
			if (index == NOT_IN_STRING) {
				if (Character.isDigit(strTime.charAt(0))) {
					/*
					 * add in am, pm, afternoon, morning?
					 */
					return true;
				}
				cal.set(Calendar.HOUR_OF_DAY, HOURS_A_DAY - 1);
				cal.set(Calendar.MINUTE, MINUTES_AN_HOUR - 1);
				cal.set(Calendar.SECOND, SECONDS_A_MINUTE - 1);
				return false;
			}
			hour = toInt(strTime.substring(0, index));
			strTime = strTime.substring(index + 1);
			index = strTime.indexOf(':');
			if (index != NOT_IN_STRING) {
				minute = toInt(strTime.substring(0, index));
				strTime = strTime.substring(index + 1);
				second = toInt(strTime);
			} else {
				minute = toInt(strTime);
				second = SECONDS_A_MINUTE - 1;
			}
			hour = restrictIn(hour, HOURS_A_DAY);
			minute = restrictIn(minute, MINUTES_AN_HOUR);
			second = restrictIn(minute, SECONDS_A_MINUTE);
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
			cal.set(Calendar.SECOND, second);
			return true;
		}
	}

	protected static void setDate(Calendar cal, String str) {
		if (str.isEmpty())
			return;
		String strDate = getFirstToken(str);		
		// next/this week/day_of_week.
		if (strDate.equals(WEEK_NEXT) || strDate.equals(WEEK_THIS)) {
			if (strDate.equals(WEEK_NEXT))
				cal.add(Calendar.DAY_OF_WEEK, DAYS_A_WEEK);
			str = removeFirstToken(str);
			str = str.trim();
			if (str.isEmpty())
				return;
			strDate = getFirstToken(str);
			int dayOfWeek = getDayOFWeek(strDate) + 2; // It is 1..7 for Sunday
														// to Friday in Calendar
														// Constants.
			int today = cal.get(Calendar.DAY_OF_WEEK);
			if (today == 1)
				today = 8; // SUNDAY
			cal.add(Calendar.DAY_OF_WEEK, dayOfWeek - today);
		}
		//today
		else if (strDate.equals(PERIOD_TODAY)){
			return;
		}
		//tomorrow
		else if (strDate.equals(PERIOD_TOMORROW)){
			cal.add(Calendar.DAY_OF_MONTH, 1);
			return;
		}
		//in X days/weeks/months
		else if (strDate.equals(PERIOD_IN)){
			str = removeFirstToken(str);
			str = str.trim();
			if (str.isEmpty())
				return;
			strDate = getFirstToken(str);
			int amount = toInt(strDate);
			if (amount == NUM_INVALID)
				amount = 0;
			str = removeFirstToken(str);
			str = str.trim();
			if (str.isEmpty())
				return;
			strDate = getFirstToken(str);
			if (strDate.indexOf(PERIOD_DAY) == 0){
				cal.add(Calendar.DAY_OF_MONTH, amount);
			}
			else if (strDate.indexOf(PERIOD_WEEK) ==0){
				cal.add(Calendar.DAY_OF_MONTH, amount*DAYS_A_WEEK);
			}
			else if (strDate.indexOf(PERIOD_MONTH) ==0){
				cal.add(Calendar.MONTH, amount);
			}
			return;
		}
		//explicit date
		else {
			int month, day, year;
			month = getMonth(strDate);
			str = removeFirstToken(str);
			str = str.trim();
			if (str.isEmpty())
				return;
			strDate = getFirstToken(str);
			day = toInt(strDate);
			str = removeFirstToken(str);
			str = str.trim();
			if (str.isEmpty())
				return;
			strDate = getFirstToken(str);
			year = toInt(strDate);
			if (year == NUM_INVALID)
				year = cal.get(Calendar.YEAR);
			month = restrictIn(month, MONTHS_A_YEAR);
			day = restrictIn(day, DAYS_A_MONTH);
			cal.set(year, month, day);
		}
	}

	protected static int getDayOFWeek(String str) {
		if (str.indexOf(WEEK) != NOT_IN_STRING)
			return SUNDAY;
		for (int index = 0; index < DAYS_A_WEEK; index++)
			if (DAYS_OF_WEEK[index].indexOf(str) != NOT_IN_STRING)
				return index;
		System.out.println(WARNING_INVALID_DAY_OF_WEEK);
		return NUM_INVALID;
	}

	protected static int getMonth(String str) {
		for (int index = 0; index < MONTHS_A_YEAR; index++)
			if (MONTHS[index].indexOf(str) != NOT_IN_STRING)
				return index;
		System.out.println(WARNING_INVALID_MONTH);
		return NUM_INVALID;
	}

	protected static int getPriority(String str) {
		int index = getNextKeyword(str);
		str = str.substring(1, index);
		str = str.trim();
		return toInt(str);
	}

	protected static String getFirstToken(String str) {
		return str.split(" ")[0];
	}

	protected static String removeFirstToken(String str) {
		int index = str.indexOf(" ");
		if (index == NOT_IN_STRING)
			return EMPTY_STRING;
		str = str.substring(index + 1);
		return str;
	}

	protected static int toInt(String str) {
		int value;
		try {
			value = Integer.parseInt(str);
		} catch (Exception e) {
			value = NUM_INVALID;
		}
		return value;
	}

	protected static int restrictIn(int value, int maxi) {
		value = (value % maxi + maxi) % maxi;
		return value;
	}

	/*
	 * public static void main(String [] args){ String str; Scanner cin= new
	 * Scanner(System.in); while(true){ str=cin.nextLine(); parse(str); } }
	 */

}