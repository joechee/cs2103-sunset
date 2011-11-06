package cs2103.aug11.t11j2.fin.parseTask;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class DateParserTest {
	@SuppressWarnings("deprecation")
	@Test
	public void testDateParser () {
		
		List<String> toTest = new ArrayList<String>();
		List<Date> correctDates = new ArrayList<Date>();
		
		// test of dates with years
		toTest.add("task due by 25 dec 2011");
		correctDates.add(new Date("25 Dec 2011"));
		
		toTest.add("task due by 25 DEC 2012");
		correctDates.add(new Date("25 Dec 2012"));
		
		toTest.add("task due by 25 DECemBer 2012");
		correctDates.add(new Date("25 Dec 2012"));
		
		
		toTest.add("task due by 25/12/2011");
		correctDates.add(new Date("25 Dec 2011"));
		
		toTest.add("task due by 25/12/2012");
		correctDates.add(new Date("25 Dec 2012"));
		
		
		toTest.add("task due by dec 25 2011");
		correctDates.add(new Date("25 Dec 2011"));
		
		toTest.add("task due by dec 25 2012");
		correctDates.add(new Date("25 Dec 2012"));
		
		toTest.add("task due by decEMber 25 2012");
		correctDates.add(new Date("25 Dec 2012"));

		
		
		toTest.add("task due on 25 dec 2012");
		correctDates.add(new Date("25 Dec 2012"));
		
		toTest.add("task due 25 dec 2011");
		correctDates.add(new Date("25 Dec 2011"));
		
		toTest.add("task by 25 dec 2011");
		correctDates.add(new Date("25 Dec 2011"));
		
		toTest.add("task on 25 dec 2011");
		correctDates.add(new Date("25 Dec 2011"));
		
		
		// test of dates without years
		toTest.add("task due by 26 Dec");
		correctDates.add(new Date("26 Dec 2011"));
		
		toTest.add("task due by 26 december");
		correctDates.add(new Date("26 Dec 2011"));
		
		toTest.add("task due by 26/12");
		correctDates.add(new Date("26 Dec 2011"));
		
		toTest.add("task due by dEC 26");
		correctDates.add(new Date("26 Dec 2011"));
		
		toTest.add("task due by december 26");
		correctDates.add(new Date("26 Dec 2011"));
	
		
		toTest.add("task due on dEC 26");
		correctDates.add(new Date("26 Dec 2011"));

		toTest.add("task due dEC 26");
		correctDates.add(new Date("26 Dec 2011"));
		
		toTest.add("task by dEC 26");
		correctDates.add(new Date("26 Dec 2011"));
		
		toTest.add("task on dEC 26");
		correctDates.add(new Date("26 Dec 2011"));

		// test of due tmrw
		toTest.add("task due tmrw");
		correctDates.add(new Date("24 Oct 2011"));

		toTest.add("task due tomorrow");
		correctDates.add(new Date("24 Oct 2011"));

		// test of due today
		toTest.add("task due today");
		correctDates.add(new Date("23 Oct 2011"));

		
		// test of due in [x] days
		toTest.add("task due in 2 days");
		correctDates.add(new Date("25 Oct 2011"));
		toTest.add("task due in 5 days");
		correctDates.add(new Date("28 Oct 2011"));
		toTest.add("task due in 365 days");
		correctDates.add(new Date("22 Oct 2012"));

		
		DateParser dp = new DateParser(new Date("23 Oct 2011"));
		for(int i=0;i<toTest.size();++i) {
			dp.parse(toTest.get(i));
			
			System.out.println("Parsing: " + toTest.get(i));
			assertEquals(dp.getParsedDate().getDate(), correctDates.get(i).getDate());
			assertEquals(dp.getParsedDate().getMonth(), correctDates.get(i).getMonth());
			assertEquals(dp.getParsedDate().getYear(), correctDates.get(i).getYear());
		}
		
		toTest.clear();
		correctDates.clear();
		
		
		
		// test of due by this [day_of_week]
		toTest.add("task due by monday");
		toTest.add("task due this mon");
		toTest.add("task by TUESDAY");
		toTest.add("task due on tue");
		toTest.add("task due this WeDnesDAY");
		toTest.add("task due coming WED");
		toTest.add("task coming ThursDay");
		toTest.add("task due by thur");
		toTest.add("task due by FRIDay");
		toTest.add("task due by fri");
		toTest.add("task due by SAturday");
		toTest.add("task due by sat");
		toTest.add("task due by sUNday");
		toTest.add("task due by sun");
		
		// test when today is a sunday
		correctDates.add(new Date("24 Oct 2011"));
		correctDates.add(new Date("24 Oct 2011"));
		correctDates.add(new Date("25 Oct 2011"));
		correctDates.add(new Date("25 Oct 2011"));
		correctDates.add(new Date("26 Oct 2011"));
		correctDates.add(new Date("26 Oct 2011"));
		correctDates.add(new Date("27 Oct 2011"));
		correctDates.add(new Date("27 Oct 2011"));
		correctDates.add(new Date("28 Oct 2011"));
		correctDates.add(new Date("28 Oct 2011"));
		correctDates.add(new Date("29 Oct 2011"));
		correctDates.add(new Date("29 Oct 2011"));
		correctDates.add(new Date("30 Oct 2011"));
		correctDates.add(new Date("30 Oct 2011"));
		

		dp = new DateParser(new Date("23 Oct 2011")); // a sunday
		for(int i=0;i<toTest.size();++i) {
			dp.parse(toTest.get(i));
			
			System.out.println("Parsing: " + toTest.get(i));
			assertEquals(dp.getParsedDate().getDate(), correctDates.get(i).getDate());
			assertEquals(dp.getParsedDate().getMonth(), correctDates.get(i).getMonth());
			assertEquals(dp.getParsedDate().getYear(), correctDates.get(i).getYear());
		}

		
		// test when today is a wednesday
		correctDates.clear();
		correctDates.add(new Date("24 Oct 2011"));
		correctDates.add(new Date("24 Oct 2011"));
		correctDates.add(new Date("25 Oct 2011"));
		correctDates.add(new Date("25 Oct 2011"));
		correctDates.add(new Date("26 Oct 2011"));
		correctDates.add(new Date("26 Oct 2011"));
		correctDates.add(new Date("20 Oct 2011"));
		correctDates.add(new Date("20 Oct 2011"));
		correctDates.add(new Date("21 Oct 2011"));
		correctDates.add(new Date("21 Oct 2011"));
		correctDates.add(new Date("22 Oct 2011"));
		correctDates.add(new Date("22 Oct 2011"));
		correctDates.add(new Date("23 Oct 2011"));
		correctDates.add(new Date("23 Oct 2011"));
		
		dp = new DateParser(new Date("19 Oct 2011")); // a wednesday
		for(int i=0;i<toTest.size();++i) {
			dp.parse(toTest.get(i));
			
			System.out.println("Parsing: " + toTest.get(i));
			assertEquals(dp.getParsedDate().getDate(), correctDates.get(i).getDate());
			assertEquals(dp.getParsedDate().getMonth(), correctDates.get(i).getMonth());
			assertEquals(dp.getParsedDate().getYear(), correctDates.get(i).getYear());
		}

		toTest.clear();

	
		
		// test of due next [day_of_week]
		toTest.add("task due next monday");
		toTest.add("task by next TUESDAY");
		toTest.add("task due next WeDnesDAY");
		toTest.add("task due next ThursDay");
		toTest.add("task due next FRIDay");
		toTest.add("task due next SAturday");
		toTest.add("task due next sUNday");

		correctDates.clear();
		// test when today is a sunday
		correctDates.add(new Date("31 Oct 2011"));
		correctDates.add(new Date("25 Oct 2011"));
		correctDates.add(new Date("26 Oct 2011"));
		correctDates.add(new Date("27 Oct 2011"));
		correctDates.add(new Date("28 Oct 2011"));
		correctDates.add(new Date("29 Oct 2011"));
		correctDates.add(new Date("30 Oct 2011"));

		dp = new DateParser(new Date("23 Oct 2011")); // a sunday
		for(int i=0;i<toTest.size();++i) {
			dp.parse(toTest.get(i));
			
			System.out.println("Parsing: " + toTest.get(i));
			assertEquals(dp.getParsedDate().getDate(), correctDates.get(i).getDate());
			assertEquals(dp.getParsedDate().getMonth(), correctDates.get(i).getMonth());
			assertEquals(dp.getParsedDate().getYear(), correctDates.get(i).getYear());
		}


		correctDates.clear();
		// test when today is a wed
		correctDates.add(new Date("24 Oct 2011"));
		correctDates.add(new Date("25 Oct 2011"));
		correctDates.add(new Date("26 Oct 2011"));
		correctDates.add(new Date("27 Oct 2011"));
		correctDates.add(new Date("28 Oct 2011"));
		correctDates.add(new Date("29 Oct 2011"));
		correctDates.add(new Date("30 Oct 2011"));

		dp = new DateParser(new Date("19 Oct 2011")); // a wed
		for(int i=0;i<toTest.size();++i) {
			dp.parse(toTest.get(i));
			
			System.out.println("Parsing: " + toTest.get(i));
			assertEquals(dp.getParsedDate().getDate(), correctDates.get(i).getDate());
			assertEquals(dp.getParsedDate().getMonth(), correctDates.get(i).getMonth());
			assertEquals(dp.getParsedDate().getYear(), correctDates.get(i).getYear());
		}

	}

}
