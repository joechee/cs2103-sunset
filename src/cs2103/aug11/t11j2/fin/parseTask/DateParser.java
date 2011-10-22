package cs2103.aug11.t11j2.fin.parseTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs2103.aug11.t11j2.fin.application.FinConstants;

/**
 * Parse a date from natural language
 * 
 * @author Koh Zi Chun
 */
public class DateParser {
	private String parsedString;
	private Date parsedDate;

	class DateStringBufferPair {
		Date date;
		StringBuffer sb;
		
		DateStringBufferPair(Date d, StringBuffer sb) {
			this.date = d;
			this.sb = sb;
		}
	}
	
	interface IPatternHandler {
		DateStringBufferPair handleMatches(Matcher m);
	}
	
	class PatternChecker {
		Pattern pattern;
		IPatternHandler handler;
		
		PatternChecker(Pattern p, IPatternHandler h) {
			this.pattern = p;
			this.handler = h;
		}
	}

	List<PatternChecker> patternCheckers = new LinkedList<PatternChecker>();

	public DateParser() {

		// by/due tomorrow
		patternCheckers.add(new PatternChecker(
				Pattern.compile("(due\\sby|due|by)\\s*(tmrw|tomorrow|tmr|tml)", Pattern.CASE_INSENSITIVE),
				new IPatternHandler() {
					public DateStringBufferPair handleMatches(Matcher m) {
						if (m.find()) {							
							StringBuffer sb = new StringBuffer();
							
							Calendar calendar = Calendar.getInstance();
							calendar.add(Calendar.DAY_OF_YEAR, +1 );
							
							m.appendReplacement(sb, FinConstants.DUEDATE_PLACEHOLDER);
							m.appendTail(sb);
							
							return new DateStringBufferPair(
									calendar.getTime(), sb);								
						}
						return null;
					}
				}));

		// on/by/this [day_of_week], e.g by Thursday
		patternCheckers.add(new PatternChecker(
				Pattern.compile("(due)?\\s*(on|by|this)\\s*(\\w*)", Pattern.CASE_INSENSITIVE),
				new IPatternHandler() {
					public DateStringBufferPair handleMatches(Matcher m) {
						while (m.find()) {
							String s = m.group(3);
							int day_of_week = parseDayOfWeek(s);
							
							if (day_of_week != -1) {
								Calendar calendar = Calendar.getInstance();
								int today = calendar.get(Calendar.DAY_OF_WEEK);
								
								if (day_of_week < today) {
									continue;
								}
								calendar.add(Calendar.DAY_OF_YEAR, day_of_week - today);
								
								StringBuffer sb = new StringBuffer();
								m.appendReplacement(sb, FinConstants.DUEDATE_PLACEHOLDER);
								m.appendTail(sb);
								
								return new DateStringBufferPair(
										calendar.getTime(), sb);								
							}
						}
						return null;
					}
				}));

		// next week
		patternCheckers.add(new PatternChecker(
				Pattern.compile("(due\\sby|due\\son|due|on|by)?\\s*(next|nxt)\\s*(week|wk)",Pattern.CASE_INSENSITIVE),
				new IPatternHandler() {
					public DateStringBufferPair handleMatches(Matcher m) {
						if (m.find()) {							
							Calendar calendar = Calendar.getInstance();
							calendar.add(Calendar.DAY_OF_YEAR, +7 );

							StringBuffer sb = new StringBuffer();
							m.appendReplacement(sb, FinConstants.DUEDATE_PLACEHOLDER);
							m.appendTail(sb);
							
							return new DateStringBufferPair(
									calendar.getTime(), sb);								
						}
						return null;
					}
				}));

		// next [day_of_week], e.g next Thursday
		patternCheckers.add(new PatternChecker(
				Pattern.compile("(due\\sby|due\\son|due|on|by)?\\s*(next|nxt)\\s*(\\w*)",Pattern.CASE_INSENSITIVE),
				new IPatternHandler() {
					public DateStringBufferPair handleMatches(Matcher m) {
						while (m.find()) {
							String s = m.group(3);
							int day_of_week = parseDayOfWeek(s);
							
							if (day_of_week != -1) {
								Calendar calendar = Calendar.getInstance();
								int today = calendar.get(Calendar.DAY_OF_WEEK);
								calendar.add(Calendar.DAY_OF_YEAR, 7 + (day_of_week - today));
								DateFormat df = new SimpleDateFormat("dd MMM, EEE");
								
								StringBuffer sb = new StringBuffer();
								m.appendReplacement(sb, FinConstants.DUEDATE_PLACEHOLDER);
								m.appendTail(sb);
								
								return new DateStringBufferPair(
										calendar.getTime(), sb);								
							}
						}

						return null;
					}
				}));

		// in [number] days
		patternCheckers.add(new PatternChecker(
				Pattern.compile("(due\\sby|due\\sin|due|in|by)?\\s*(\\d*)\\s*(days)", Pattern.CASE_INSENSITIVE),
				new IPatternHandler() {
					public DateStringBufferPair handleMatches(Matcher m) {
						
						while (m.find()) {
							String s = m.group(2);
							int w = Integer.parseInt(s);
							
							if (w > 0) {
								Calendar calendar = Calendar.getInstance();
								calendar.add(Calendar.DAY_OF_YEAR, w);

								StringBuffer sb = new StringBuffer();
								m.appendReplacement(sb, FinConstants.DUEDATE_PLACEHOLDER);
								m.appendTail(sb);
								
								return new DateStringBufferPair(
										calendar.getTime(), sb);								
							}
						}
						
						return null;
					}
				}));

		// for using SimpleDateFormat
		patternCheckers.add(
				new PatternChecker(Pattern.compile("(due\\sby|due\\son|due|on|by)\\s*(\\w*[\\s|/]\\w*)"),
				new IPatternHandler() {
					public DateStringBufferPair handleMatches(Matcher m) {
						while (m.find()) {
							String t = m.group(2);
							List<DateFormat> totest = new LinkedList<DateFormat>();
							
							totest.add( new SimpleDateFormat("dd MMM") );
							totest.add( new SimpleDateFormat("dd/MM") );
							totest.add( new SimpleDateFormat("MMM dd") );
							
							for (DateFormat df : totest) {
								try {
									Date date = df.parse(t);
									
									StringBuffer sb = new StringBuffer();
									m.appendReplacement(sb, FinConstants.DUEDATE_PLACEHOLDER);
									m.appendTail(sb);

									Calendar cal = Calendar.getInstance();
									cal.setTime(date);
									
									int month = cal.get(Calendar.MONTH);
									int day = cal.get(Calendar.DAY_OF_MONTH);
									
									cal = Calendar.getInstance();
									cal.set(Calendar.MONTH, month);
									cal.set(Calendar.DAY_OF_MONTH, day);
									
									return new DateStringBufferPair( cal.getTime(), sb );
								} catch (ParseException e) {
									continue;
								}								
							}
							
						}
						return null;
					}
				}));
	}
	
	/** 
	 * Returns the day of the week (1: sun...7:sat) for a given 
	 * string representing the day (e.g Sunday, wed)
	 * 
	 * @param string represent the natural word for the day
	 * @return 0..6 representing the day, or -1 if not parsable
	 */
	protected static Integer parseDayOfWeek(String s) {
		DateFormat dateFormat = new SimpleDateFormat("EEE");
		try {
			Date d = dateFormat.parse(s);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			
			return cal.get(Calendar.DAY_OF_WEEK);
			
		} catch (ParseException e) {
			return -1;
		}
	}

	public boolean parse(String d) {
		
		for (PatternChecker patternChecker : patternCheckers) {
			Matcher m = patternChecker.pattern.matcher(d);
			DateStringBufferPair dateSBPair = patternChecker.handler.handleMatches(m);
			
			if (dateSBPair != null) {
				setParsedString(dateSBPair.sb.toString());
				setParsedDate(dateSBPair.date);
				return true;
			}
		}
		
		return false;
	}
	
	public String getParsedString() {
		return parsedString;
	}

	private void setParsedString(String parsedString) {
		this.parsedString = parsedString;
	}

	public Date getParsedDate() {
		return parsedDate;
	}

	private void setParsedDate(Date parsedDate) {
		this.parsedDate = parsedDate;
	}
	
	
	private static final long DAY = 24 * 60 * 60 * 1000;
	/**
	 * Returns a string representing a date from now in natural language 
	 * 
	 * @param date d
	 */
	public static String naturalDateFromNow(Date d) {
		final Calendar now = Calendar.getInstance(), due = Calendar.getInstance();
		final DateFormat df = new SimpleDateFormat("dd MMM");

		due.setTime(d);
		
		if (due.get(Calendar.YEAR) < now.get(Calendar.YEAR)) {
			return df.format(due.getTime());
		} 

		int diff = due.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
		
		if (due.get(Calendar.YEAR) > now.get(Calendar.YEAR)){
			diff = diff + (due.get(Calendar.YEAR) - now.get(Calendar.YEAR))* 365
					+ (findLeapYears(now.get(Calendar.YEAR),due.get(Calendar.YEAR)));
		}


		if (diff < 0) {
			return df.format(due.getTime());
		} else if (diff == 0) {
			return "DUE TODAY!";
		} else if (diff <= 1) {
			return "tomorrow";
		} else if (diff <= 5) {
			return "in " + diff + " days";
		} else if (diff <= 14) {
			DateFormat dayFormat = new SimpleDateFormat("EEEEEE");
			return "next " + dayFormat.format(due.getTime());
		} else {
			return df.format(due.getTime());
		}
	}
	
	private static boolean isLeapYear(int year) {
		if (year % 4 == 0) {
			if (year % 100 == 0) {
				if (year % 400 == 0) {
					return true;
				}
				return false;
			}
			return true;
		}
		return false;
	}
	
	private static int findLeapYears(int start, int end) {
		assert (end >= start);
		if (end<start) {
			return 0;
		}
		int leapYears = 0;
		for (int i = start; i < end; i++) {
			if (isLeapYear(i)) {
				leapYears++;
			}
		}
		
		return leapYears;
	}
	
}
