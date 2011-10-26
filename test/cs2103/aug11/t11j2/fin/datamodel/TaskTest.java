package cs2103.aug11.t11j2.fin.datamodel;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import cs2103.aug11.t11j2.fin.application.FinConstants;


public class TaskTest {
	//TODO: Populate TaskTest
	@Test
	public void testTask() {
		Task task1 = new Task("");
		assertTrue(task1.getTags().isEmpty());
		
		System.out.println(task1.getTags());
		task1.addTag("");
		//should be sanitized
		System.out.println(task1.getTags());
		
		//task1.fin();
		//System.out.println(task1);
		//assertEquals(task1.getTags(),Arrays.asList(FinConstants.DUEDATE_PLACEHOLDER,FinConstants.FIN_HASH_TAG));
		//System.out.println(task1.getTags());
	}
}
