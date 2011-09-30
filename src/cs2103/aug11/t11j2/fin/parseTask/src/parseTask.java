import java.util.*;
import java.math.*;
import java.io.*;

public class parseTask {

	/**
	 * @param args
	 */
	
	final private static char TAG_SIGNAL = '#';
	final private static String TIME_DONT_CARE = "*";
	final private static String [] DAYS_OF_WEEK = {"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY"};
	final private static String [] MONTHS = {"JANUARY","FEBRARY","MARCH","APRIL","MAY","JUNE","JULY","AUGST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER"};
	final private static String WEEK_NEXT = "NEXT";
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
		
	final private static String WARNING_INVALID_DAY_OF_WEEK = "Warning: Invalid Day of Week";
	final private static String WARNING_INVALID_MONTH ="Warning: Invalid Month";
	
	public parseTask(){
		
	}
	
	public static void getTask(String str){
		String taskName;
		List<String> tags;
		Date dueTime;
		int percentageCompleted=0;
		Date addTime;
		String uniqId;
		int pIndex;
		
		str=str.toUpperCase();
		str=str+" ";
		
		taskName=getTaskName(str);		
		str=removeTaskName(str);
		
		tags=getTags(str);
		str=removeTags(str);
		
		dueTime=getTime(str);
		str=removeTime(str);
		
		addTime=getAddTime(str);
		
		//uniqId=totTask+1;
		
		pIndex=getPriority(str);
		
		//return Task(.....);
	}
	
	protected static String getTaskName(String str){
		String taskName = new String("");
		while(str.charAt(0)!=TAG_SIGNAL){
			taskName = taskName+getFirstToken(str);
			str=removeFirstToken(str);
		}
		return taskName;
	}
	
	protected static String removeTaskName(String str){
		while(str.charAt(0)!=TAG_SIGNAL){
			str=removeFirstToken(str);
		}
		return str;
	}
	
	protected static List<String> getTags(String str){
		List<String>tags = new ArrayList<String>();
		while(str.charAt(0)==TAG_SIGNAL){
			tags.add(getFirstToken(str));
			str=removeFirstToken(str));
		}		
		return tags;
	}
	
	protected static String removeTags(String str){
		while(str.charAt(0)==TAG_SIGNAL){
			str=removeFirstToken(str);
		}
		return str;
	}
	
	protected static Date getTime(String str){
		Calendar cal = Calendar.getInstance();
		Date date;
		setTime(cal, str);
		removeFirstToken(str);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		setDate(cal, str);
		return date;
		
	}
	
	protected static String removeTime(String str){
		String strDate;
		str=removeFirstToken(str); //remove time;
		str=removeFirstToken(str); //remove month or "next";
		str=removeFirstToken(str); //remove day or day of week;
		if(toInt(getFirstToken(str))!=NUM_INVALID))str=removeFirstToken(str); //remove year;
		return str;
	}
	
	protected static void setTime(Calendar cal, String str){
		String strTime = getFirstToken(str);
		if(strTime.equals(TIME_DONT_CARE)){
			cal.set(Calendar.HOUR_OF_DAY,23);
			cal.set(Calendar.MINUTE,59);
			cal.set(Calendar.SECOND,59);
		}
		else{
			int hour, minute, second;
			int index=strTime.indexOf(':');
			hour=toInt(strTime.substring(0,index));
			strTime=strTime.substring(index+1);
			index=strTime.indexOf(':');
			if(index!=NOT_IN_STRING){
				minute=toInt(strTime.substring(0,index);
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
		if(strDate.equals(WEEK_NEXT)){
			cal.add(Calendar.DAY_OF_WEEK,7);
			str=removeFirstToken(str);
			strDate=getFirstToken(str);
			int dayOfWeek=getDayOFWeek(strDate);
			dayOfWeek-=cal.get(Calendar.DAY_OF_WEEK);
			cal.add(Calendar.DAY_OF_WEEK,dayOfWeek);
		}
		else{
			int month, day, year;
			strDate=getFirstToken(str);
			month=getMonth(strDate);
			str=removeFirstToken(str);
			strDate=getFirstToken(str);
			day=toInt(strDate);
			str=removeFirstToken(str);
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
			
	protected static Date getAddTime(String str){
		Calendar cal= Calendar.getInstance();
		return cal.getTime();
	}
	
	protected static int getPriority(String str){
		return toInt(str);		
	}
	
	protected static String getFirstToken(String str){
		return str.split(" ,")[0];		
	}
	
	protected static String removeFirstToken(String str){
		int index=str.indexOf(" ");
		int tem=str.indexOf(',');
		if(tem==NOT_IN_STRING)tem=str.length();
		if(index>tem)index=tem;
		str.substring(index+1);
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

	}

}
