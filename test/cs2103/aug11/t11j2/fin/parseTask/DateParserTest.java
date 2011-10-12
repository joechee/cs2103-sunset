package cs2103.aug11.t11j2.fin.parseTask;

import static org.junit.Assert.*;

import org.junit.Test;

public class DateParserTest {
	@Test
	public void testDateParser () {
	String s = "do something by 31/12";
	DateParser dp = new DateParser();
	
	
	assertEquals(dp.parse(s),true);	
	assertEquals(dp.getParsedString(),"do something #-#DUEDATE#-#");
	System.out.println(dp.getParsedDate());
	//TODO: Complete test cases.


	
	}

}
