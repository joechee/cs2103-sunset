package cs2103.aug11.t11j2.fin.parseTask;

import java.util.*;
import java.math.*;
import java.io.*;
import cs2103.aug11.t11j2.fin.datamodel.Task;

public class parseTask {

	/**
	 * @param args
	 */
	
	final private static char TAG_SIGNAL = '#';
	final private static String TIME_DONT_CARE = "*";
	final private static String [] DAYS_OF_WEEK = {"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY"};
	final private static String [] MONTHS = {"JANUARY","FEBRARY","MARCH","APRIL","MAY","JUNE","JULY","AUGST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER"};
	final private static String WEEK_NEXT = "NEXT";
	final private static String WEEK_THIS = "THIS";
	final private static String WEEK = "WEEK";
	final private static int DAYS_A_WEEK = 7;
	final private static int MONTHS_A_YEAR = 12;
	final private static int DAYS_A_MONTH = 31; //maximum;
	final private static int HOURS_A_DAY = 24;
	final private static int MINUTES_AN_HOUR = 60;
	final private static int SECONDS_A_MINUTE = 60;
	final private static int SUNDAY = 6;
	final private static int NUM_INVALID = -1;
	final private static int NOT_IN_STRING = -1;
	final private static int DEFAULT_PERCENTAGE = 0;
	final private static String EMPTY_STRING = "";
		
	final private static String WARNING_INVALID_DAY_OF_WEEK = "Warning: Invalid Day of Week";
	final private static String WARNING_INVALID_MONTH ="Warning: Invalid Month";
	final private static String ERROR_NO_TAG = "Invalid Task: Please add tags, each word of which starsts with #";
	final private static String ERROR_NO_TIME = "Invalid Task: Please indicate a time in the form of HH:MM:SS or HH:MM or *(not specified)";
	final private static String ERROR_NO_DATE = "Invalid Task: Please indicate a date in the form of MM DD YYYY or MM DD or Next/This Day_Of_Week";
	final private static String ERROR_NO_PRIORITY = "invalid Task: Please indicate a priority, which is bracked number (X)";
	
	public parseTask(){
		
	}
	
	public static Task getTask(String str){
		String taskName;
		List<String> tags;
		Task.EImportance importance;
		Date dueTime;
		int percentage = DEFAULT_PERCENTAGE;
		int pIndex;
		
		str=str.trim();
		str=str.toUpperCase();
		
		taskName=getTaskName(str);		
		str=removeTaskName(str);
		
		if(str.isEmpty()){
			System.out.println(ERROR_NO_TAG);
			return null;
		}
		
		tags=getTags(str);
		str=removeTags(str);
		
		int importanceIndex=getImportanceIndex(str);
		if(importanceIndex>NUM_INVALID){
			str=removeFirstToken(str); //remove Importance;
			str=str.trim();
		}
		importance=getImportance(importanceIndex);
		
		
		if(str.isEmpty()){
			System.out.println(ERROR_NO_TIME);
			return null;
		}
		
		dueTime=getDueTime(str);
		str=removeDueTime(str);
		
		if(str.isEmpty()){
			System.out.println(ERROR_NO_PRIORITY);
			return null;
		}
		
		pIndex=getPriority(str);
		
		System.out.println(taskName);
		for(int i=0;i<tags.size();i++)System.out.print(tags.get(i)+" ");
		System.out.println();
		System.out.println(importance);
		System.out.println(dueTime.toString());
		System.out.println(pIndex);
		
		Task task= new Task(taskName, tags, importance, dueTime, percentage, pIndex);
		return task;
	}
	
	protected static String getTaskName(String str){
		String taskName = new String(EMPTY_STRING);
		while(!str.isEmpty() && str.charAt(0)!=TAG_SIGNAL){
			taskName=taskName+" "+getFirstToken(str);;
			str=removeFirstToken(str);
			str=str.trim();
		}
		taskName=taskName.trim();
		return taskName;
	}
	
	protected static String removeTaskName(String str){
		while(!str.isEmpty() && str.charAt(0)!=TAG_SIGNAL){
			str=removeFirstToken(str);
		}
		return str;
	}
	
	protected static List<String> getTags(String str){
		List<String>tags = new ArrayList<String>();
		while(!str.isEmpty() && str.charAt(0)==TAG_SIGNAL){
			tags.add(getFirstToken(str));
			str=removeFirstToken(str);
			str=str.trim();
		}		
		return tags;
	}
	
	protected static String removeTags(String str){
		while(!str.isEmpty() && str.charAt(0)==TAG_SIGNAL){
			str=removeFirstToken(str);
			str=str.trim();
		}
		return str;
	}
	
	protected static int getImportanceIndex(String str){
		if(str.isEmpty())return -1;
		String importance=getFirstToken(str);
		if(importance.equals("LOW"))return 0;
		else if(importance.equals("NORMAL"))return 1;
		else if(importance.equals("HIGH"))return 2;
		else return -1;
	}
	
	protected static Task.EImportance getImportance(int index){
		Task.EImportance importance;
		if(index==1 || index==-1)return Task.EImportance.NORMAL;
		else if (index==0)return Task.EImportance.LOW;
		else return Task.EImportance.HIGH;
	}
	
	protected static Date getDueTime(String str){
		Calendar cal = Calendar.getInstance();
		Date date;
		setTime(cal, str);
		str=removeFirstToken(str);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		setDate(cal, str);
		date=cal.getTime();
		return date;
		
	}
	
	protected static String removeDueTime(String str){
		str=removeFirstToken(str); //remove time;
		str=str.trim();
		str=removeFirstToken(str); //remove month or "next";
		str=str.trim();
		str=removeFirstToken(str); //remove day or day of week;
		str=str.trim();
		if(toInt(getFirstToken(str))!=NUM_INVALID)str=removeFirstToken(str); //remove year;
		str=str.trim();
		return str;
	}
	
	protected static void setTime(Calendar cal, String str){
		String strTime = getFirstToken(str);
		if(strTime.equals(TIME_DONT_CARE)){
			cal.set(Calendar.HOUR_OF_DAY,HOURS_A_DAY-1);
			cal.set(Calendar.MINUTE,MINUTES_AN_HOUR-1);
			cal.set(Calendar.SECOND,SECONDS_A_MINUTE-1);
		}
		else{
			int hour, minute, second;
			int index=strTime.indexOf(':');
			if(index==NOT_IN_STRING){
				System.out.println(ERROR_NO_TIME);
				return;
			}			
			hour=toInt(strTime.substring(0,index));
			strTime=strTime.substring(index+1);
			index=strTime.indexOf(':');
			if(index!=NOT_IN_STRING){
				minute=toInt(strTime.substring(0,index));
				strTime=strTime.substring(index+1);
				second=toInt(strTime);
			}
			else{
				minute=toInt(strTime);
				second=SECONDS_A_MINUTE-1;
			}
			hour=restrictIn(hour,HOURS_A_DAY);
			minute=restrictIn(minute,MINUTES_AN_HOUR);
			second=restrictIn(minute,SECONDS_A_MINUTE);
			cal.set(Calendar.HOUR_OF_DAY,hour);
			cal.set(Calendar.MINUTE,minute);
			cal.set(Calendar.SECOND,second);
		}
	}
	
	protected static void setDate(Calendar cal, String str){
		String strDate=getFirstToken(str);
		if(strDate.equals(WEEK_NEXT)||strDate.equals(WEEK_THIS)){
			if(strDate.equals(WEEK_NEXT))cal.add(Calendar.DAY_OF_WEEK,DAYS_A_WEEK);
			str=removeFirstToken(str);
			str=str.trim();
			strDate=getFirstToken(str);
			int dayOfWeek=getDayOFWeek(strDate)+2; //It is 1..7 for Sunday to Friday in Calendar Constants.
			int today=cal.get(Calendar.DAY_OF_WEEK);
			if(today==1)today=8; //SUNDAY
			cal.add(Calendar.DAY_OF_WEEK,dayOfWeek-today);
		}
		else{
			int month, day, year;
			strDate=getFirstToken(str);
			month=getMonth(strDate);
			str=removeFirstToken(str);
			str=str.trim();
			strDate=getFirstToken(str);
			day=toInt(strDate);
			str=removeFirstToken(str);
			str=str.trim();
			year=toInt(strDate);
			if(year==NUM_INVALID)year=cal.get(Calendar.YEAR);
			month=restrictIn(month,MONTHS_A_YEAR);
			day=restrictIn(day,DAYS_A_MONTH);
			cal.set(year,month,day);			
		}
	}
	
	protected static int getDayOFWeek(String str){
		if(str.indexOf(WEEK)!=NOT_IN_STRING)return SUNDAY;
		for(int index=0;index<DAYS_A_WEEK;index++)
			if(DAYS_OF_WEEK[index].indexOf(str)!=NOT_IN_STRING)return index;
		System.out.println(WARNING_INVALID_DAY_OF_WEEK);
		return NUM_INVALID;
	}
	
	protected static int getMonth(String str){
		for(int index=0;index<MONTHS_A_YEAR;index++)
			if(MONTHS[index].indexOf(str)!=NOT_IN_STRING)return index;
		System.out.println(WARNING_INVALID_MONTH);
		return NUM_INVALID;
	}
			
	protected static int getPriority(String str){
		str=str.substring(1,str.length()-1);
		return toInt(str);		
	}
	
	protected static String getFirstToken(String str){
		return str.split(" ")[0];		
	}
	
	protected static String removeFirstToken(String str){
		int index=str.indexOf(" ");
		if(index==NOT_IN_STRING)return EMPTY_STRING;
		str=str.substring(index+1);
		return str;
	}
	
	protected static int toInt(String str){
		int value;
		try{
			value=Integer.parseInt(str);
		}
		catch(Exception e){
			value=NUM_INVALID;
		}
		return value;		
	}
	
	protected static int restrictIn(int value, int maxi){
		value=(value % maxi +maxi)%maxi;
		return value;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner cin = new Scanner(System.in);
		String str;
		while(true){
			str=cin.nextLine();
			if(str.equals("exit"))break;
			getTask(str);
			System.out.println("============================");
		}
	}

}
