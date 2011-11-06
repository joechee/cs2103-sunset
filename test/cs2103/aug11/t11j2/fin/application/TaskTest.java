package cs2103.aug11.t11j2.fin.application;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class TaskTest {
	/**
	 * Pls try not to test this close to midnight >.<
	 */
	@Test
	public void TestEmpty() {
		Task testEmpty = new Task("");
		assertNotNull(testEmpty.getDateAdded());
		assertNull(testEmpty.getDueDate());
		assertTrue(testEmpty.getEditableTaskName().isEmpty());
		assertTrue(testEmpty.getTags().isEmpty());
		assertTrue(testEmpty.getTaskName().isEmpty());
		assertNotNull(testEmpty.getUniqId());
		try {
			testEmpty.hasTags(null);
			fail("No Exception Thrown for a null object!");
		} catch (NullPointerException e) {
			assertTrue(e.getStackTrace()[0].getMethodName() == "hasTags");
		} catch (Exception e) {
			fail("Unexpected Exception thrown for testEmpty.hasTags!");
		}
	}
	
	@Test
	public void TestDateParse() {
		Task testDate = new Task("CS2103 tutorial due by 31 Dec");
		Task testDate2 = new Task("IS1103 tutorial by 31 Dec");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH,11);     //11 represents December, 0 represents January
		cal.set(Calendar.DAY_OF_MONTH,30);
		assertTrue(testDate.getDueDate().after(cal.getTime()));
		assertTrue(testDate2.getDueDate().after(cal.getTime()));
		assertTrue(testDate.getEditableTaskName().equals("CS2103 tutorial due 31 Dec"));
		assertTrue(testDate2.getEditableTaskName().equals("IS1103 tutorial due 31 Dec"));
		assertNotNull(testDate.getDateAdded());
		assertTrue(testDate.getTags().isEmpty());
		assertNotNull(testDate.getUniqId());
		assertNotNull(testDate2.getUniqId());
	}
	
	@Test
	public void TestTagsParse() {
		Task test = new Task("#CS2103 tutorial due by 31 Dec");
		Task test2 = new Task("#IS1103 tutorial by 31 Dec");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH,11);     //11 represents December, 0 represents January
		cal.set(Calendar.DAY_OF_MONTH,30);
		assertTrue(test.getDueDate().after(cal.getTime()));
		assertTrue(test2.getDueDate().after(cal.getTime()));
		assertTrue(test.getEditableTaskName().equals("#CS2103 tutorial due 31 Dec"));
		assertTrue(test2.getEditableTaskName().equals("#IS1103 tutorial due 31 Dec"));
		assertNotNull(test.getDateAdded());
		assertTrue(test.getTags().contains("cs2103"));
		assertTrue(test2.getTags().contains("is1103"));
		assertNotNull(test.getUniqId());
		assertNotNull(test2.getUniqId());
	}
	
	@Test
	public void TestMultipleTagsParse() {
		Task test = new Task("#CS4232 #CS2103 tutorial due by 31 Dec");
		Task test2 = new Task("#CS3230 #IS1103 tutorial by 31 Dec");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH,11);     //11 represents December, 0 represents January
		cal.set(Calendar.DAY_OF_MONTH,30);
		assertTrue(test.getDueDate().after(cal.getTime()));
		assertTrue(test2.getDueDate().after(cal.getTime()));
		assertTrue(test.getEditableTaskName().equals("#CS4232 #CS2103 tutorial due 31 Dec"));
		assertTrue(test2.getEditableTaskName().equals("#CS3230 #IS1103 tutorial due 31 Dec"));
		assertNotNull(test.getDateAdded());
		assertTrue(test.getTags().contains("cs4232"));
		assertTrue(test.getTags().contains("cs2103"));
		assertTrue(test2.getTags().contains("cs3230"));
		assertTrue(test2.getTags().contains("is1103"));
		assertNotNull(test.getUniqId());
		assertNotNull(test2.getUniqId());
	}

}
