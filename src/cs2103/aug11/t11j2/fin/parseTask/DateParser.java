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
	private Date dateNow;

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
		this(new Date());
	}
	
	/**
	 * DateParser Constructor
	 * Set the day of reference for parsing as given date "now"
	 * 
	 * @param now
	 */
	public DateParser(final Date now) {
		this.dateNow = now;

		// by/due tomorrow
		patternCheckers.add(new PatternChecker(
				Pattern.compile("\\s(due\\sby|due|by)\\s*(today)", Pattern.CASE_INSENSITIVE),
				new IPatternHandler() {
					public DateStringBufferPair handleMatches(Matcher m) {
						if (m.find()) {							
							StringBuffer sb = new StringBuffer();
							
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(dateNow);
							
							m.appendReplacement(sb, " "+FinConstants.DUEDATE_PLACEHOLDER);
							m.appendTail(sb);
							
							return new DateStringBufferPair(
									calendar.getTime(), sb);								
						}
						return null;
					}
				}));

		
		// by/due tomorrow
		patternCheckers.add(new PatternChecker(
				Pattern.compile("\\s(due\\sby|due|by)\\s*(tmrw|tomorrow|tmr|tml)", Pattern.CASE_INSENSITIVE),
				new IPatternHandler() {
					public DateStringBufferPair handleMatches(Matcher m) {
						if (m.find()) {							
							StringBuffer sb = new StringBuffer();
							
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(dateNow);
							calendar.add(Calendar.DAY_OF_YEAR, +1 );
							
							m.appendReplacement(sb, " "+FinConstants.DUEDATE_PLACEHOLDER);
							m.appendTail(sb);
							
							return new DateStringBufferPair(
									calendar.getTime(), sb);								
						}
						return null;
					}
				}));

		// on/by/this [day_of_week], e.g by Thursday
		patternCheckers.add(new PatternChecker(
				Pattern.compile("\\s(due)?\\s*(on|by|this|coming)\\s*(coming)?\\s*(\\w*)", Pattern.CASE_INSENSITIVE),
				new IPatternHandler() {
					public DateStringBufferPair handleMatches(Matcher m) {
						while (m.find()) {
							String s = m.group(4);
							int day_of_week = parseDayOfWeek(s);
							
							if (day_of_week != -1) {
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(dateNow);
								int today = calendar.get(Calendar.DAY_OF_WEEK);
								
								if (day_of_week <= today) {
									calendar.add(Calendar.DAY_OF_YEAR, 7 - (today - day_of_week));	
								} else {
									calendar.add(Calendar.DAY_OF_YEAR, day_of_week - today);
								}
								
								
								StringBuffer sb = new StringBuffer();
								m.appendReplacement(sb, " "+FinConstants.DUEDATE_PLACEHOLDER);
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
				Pattern.compile("\\s(due\\sby|due\\son|due|on|by)?\\s*(next|nxt)\\s*(week|wk)",Pattern.CASE_INSENSITIVE),
				new IPatternHandler() {
					public DateStringBufferPair handleMatches(Matcher m) {
						if (m.find()) {							
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(dateNow);
							calendar.add(Calendar.DAY_OF_YEAR, +7 );

							StringBuffer sb = new StringBuffer();
							m.appendReplacement(sb, " "+FinConstants.DUEDATE_PLACEHOLDER);
							m.appendTail(sb);
							
							return new DateStringBufferPair(
									calendar.getTime(), sb);								
						}
						return null;
					}
				}));

		// next [day_of_week], e.g next Thursday
		patternCheckers.add(new PatternChecker(
				Pattern.compile("\\s(due\\sby|due\\son|due|on|by)?\\s*(next|nxt)\\s*(\\w*)",Pattern.CASE_INSENSITIVE),
				new IPatternHandler() {
					public DateStringBufferPair handleMatches(Matcher m) {
						while (m.find()) {
							String s = m.group(3);
							int day_of_week = parseDayOfWeek(s);
							
							if (day_of_week != -1) {
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(dateNow);
								int today = calendar.get(Calendar.DAY_OF_WEEK);
								
								if (today == 1) { // sunday is given special treatment.. due to human's intepretation of "next tuesday" for e.g
									if (day_of_week == 1) { // next Sunday is necessarily in a week
										calendar.add(Calendar.DAY_OF_YEAR, 7);
									} else if (day_of_week == 2){ // if today is sunday, and we say next monday, we mean 8 days time
										calendar.add(Calendar.DAY_OF_YEAR, 8);
									} else {
										calendar.add(Calendar.DAY_OF_YEAR, (day_of_week - today));
									}
								} else {
									if (day_of_week == 1) { // if next sunday, will be the sunday one week from now
										calendar.add(Calendar.DAY_OF_YEAR, 7 + (8 - today));
									} else {
										calendar.add(Calendar.DAY_OF_YEAR, 7 + (day_of_week - today));
									}
								}
								
								StringBuffer sb = new StringBuffer();
								m.appendReplacement(sb, " "+FinConstants.DUEDATE_PLACEHOLDER);
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
				Pattern.compile("\\s(due\\sby|due\\sin|due|in|by)?\\s*(\\d*)\\s*(days)", Pattern.CASE_INSENSITIVE),
				new IPatternHandler() {
					public DateStringBufferPair handleMatches(Matcher m) {
						
						while (m.find()) {
							String s = m.group(2);
							int w = Integer.parseInt(s);
							
							if (w > 0) {
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(dateNow);
								calendar.add(Calendar.DAY_OF_YEAR, w);

								StringBuffer sb = new StringBuffer();
								m.appendReplacement(sb, " "+FinConstants.DUEDATE_PLACEHOLDER);
								m.appendTail(sb);
								
								return new DateStringBufferPair(
										calendar.getTime(), sb);								
							}
						}
						
						return null;
					}
				}));


		// for using SimpleDateFormat
		// 30 Oct 2011, 30/10/2011, Oct 30 2011 
		patternCheckers.add(
				new PatternChecker(Pattern.compile("\\s(due\\sby|due\\son|due|on|by)\\s*(\\w*[\\s|/]\\w*[\\s|/]\\w*)"),
				new IPatternHandler() {
					public DateStringBufferPair handleMatches(Matcher m) {
						while (m.find()) {
							String t = m.group(2);
							List<DateFormat> totest = new LinkedList<DateFormat>();
							totest.add( new SimpleDateFormat("dd MMM yyyy") );
							totest.add( new SimpleDateFormat("dd/MM/yyyy") );
							totest.add( new SimpleDateFormat("MMM dd yyyy") );
							
							for (DateFormat df : totest) {
								try {
									Date date = df.parse(t);

									StringBuffer sb = new StringBuffer();
									m.appendReplacement(sb, " "+FinConstants.DUEDATE_PLACEHOLDER);
									m.appendTail(sb);

									Calendar cal = Calendar.getInstance();
									cal.setTime(date);
									
									int month = cal.get(Calendar.MONTH);
									int day = cal.get(Calendar.DAY_OF_MONTH);
									int year = cal.get(Calendar.YEAR);
									
									cal = Calendar.getInstance();
									cal.set(Calendar.MONTH, month);
									cal.set(Calendar.DAY_OF_MONTH, day);
									cal.set(Calendar.YEAR, year);
									
									return new DateStringBufferPair( cal.getTime(), sb );
								} catch (ParseException e) {
									continue;
								}
							}
						}
						return null;
					}
				}));

		
		// for using SimpleDateFormat
		// 30 Oct, 30/10, Oct 30
		patternCheckers.add(
				new PatternChecker(Pattern.compile("\\s(due\\sby|due\\son|due|on|by)\\s*(\\w*[\\s|/]\\w*)"),
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
									m.appendReplacement(sb, " "+FinConstants.DUEDATE_PLACEHOLDER);
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
				//System.out.println("success!");
				Calendar c = Calendar.getInstance();
				c.setTime(dateSBPair.date);
				c.set(Calendar.HOUR_OF_DAY, 23);
				c.set(Calendar.MINUTE, 59);
				c.set(Calendar.SECOND, 59);
				dateSBPair.date = c.getTime();
				setParsedString(dateSBPair.sb.toString());
				setParsedDate(dateSBPair.date);
				//System.out.println(getParsedDate());
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
	
	
	/**
	 * Returns a string representing a date from now in natural language 
	 * 
	 * @param date d
	 */
	public static String naturalDateFromNow(Date d) {
		final Calendar now = Calendar.getInstance(), due = Calendar.getInstance();
		DateFormat df; 

		due.setTime(d);
		int dueYear = due.get(Calendar.YEAR);
		int nowYear = now.get(Calendar.YEAR);

		if (dueYear != nowYear) {
			// if year is not equal, we should include it in! 
			df = new SimpleDateFormat("dd MMM yy");
		} else {
			df = new SimpleDateFormat("dd MMM");
		}

		
		if (due.get(Calendar.YEAR) < now.get(Calendar.YEAR)) {
			return df.format(due.getTime());
		} 

		int diff = due.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
		
		if (due.get(Calendar.YEAR) > now.get(Calendar.YEAR)){
			diff = diff + (due.get(Calendar.YEAR) - now.get(Calendar.YEAR))* 365
					+ (findLeapYears(now.get(Calendar.YEAR),due.get(Calendar.YEAR)));
		}
		
		int dueDayOfWeek = (due.get(Calendar.DAY_OF_WEEK) + 5) % 7;
		int nowDayOfWeek = (now.get(Calendar.DAY_OF_WEEK) + 5) % 7;
		

		if (diff < 0) {
			return df.format(due.getTime());
		} else if (diff == 0) {
			return "TODAY";
		} else if (diff <= 1) {
			return "tomorrow";
		} else if (diff <= 5) {
			return "in " + diff + " days";
		} else if (diff < 14 - (nowDayOfWeek - dueDayOfWeek)) {
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
