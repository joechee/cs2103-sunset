package cs2103.aug11.t11j2.fin.application;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
		Task testDate = new Task("this is due by 31 Dec");
		assertNotNull(testDate.getDateAdded());
		assertNull(testDate.getDueDate());
		assertTrue(testDate.getEditableTaskName().isEmpty());
		assertTrue(testDate.getTags().isEmpty());
		assertTrue(testDate.getTaskName().isEmpty());
		assertNotNull(testDate.getUniqId());
		
	}

}
