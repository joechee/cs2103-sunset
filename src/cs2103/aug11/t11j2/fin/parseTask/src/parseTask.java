import java.util.*;
import java.math.*;
import java.io.*;

public class parseTask {

	/**
	 * @param args
	 */
	
	final private static char TAG_SIGNAL = '#';
	
	public Task parseTask(String str){
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
		
		uniqId=totTask+1;
		
		pIndex=getPriority(str);
		
		return new Task(.....);
	}
	
	protected static String getTaskName(String str){
		String taskName = new String("");
		while(str.charAt(0)!=TAG_SIGNAL){
			taskName = taskName+getFirstToken(str);
			str=removeFirstToken(str);
		}
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
		return tags
	}
	
	protected static String removeTags(String str){
		while(str.charAt(0)==TAG_SIGNAL){
			str=removeFirstToken(str);
		}
		return str;
	}
	
	protected static Date getTime(String str){
		
	}
	
	protected static Date getAddTime(String str){
		Calendar cal= Calendar.getInstance();
		return cal.getTime();
	}
	
	protected static String getFirstToken(String str){
		return str.split(" ")[0];		
	}
	
	protected static String removeFirstToken(String str){
		return str.substring(str.indexOf(" ")+1);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
