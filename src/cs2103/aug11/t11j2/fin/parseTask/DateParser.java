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
	 * Returns a string represeting a date from now in natural language 
	 * 
	 * @param date d
	 */
	public static String naturalDateFromNow(Date d) {
		final Calendar now = Calendar.getInstance(), due = Calendar.getInstance();
		final DateFormat df = new SimpleDateFormat("dd MMM");

		due.setTime(d);

		long diff = due.getTimeInMillis() - now.getTimeInMillis();

		if (diff < 0) {
			return df.format(due.getTime());
		} else if (diff <= DAY) {
			return "WITHIN 24 HOURS!";
		} else if (diff <= 2 * DAY) {
			return "tomorrow";
		} else if (diff <= 5 * DAY) {
			return "in " + (int) (Math.floor(diff / DAY) + 1) + " days";
		} else if (diff <= 14 * DAY) {
			DateFormat dayFormat = new SimpleDateFormat("EEEEEE");
			return "next " + dayFormat.format(due.getTime());
		} else {
			return df.format(due.getTime());
		}
	}
}
