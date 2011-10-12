package cs2103.aug11.t11j2.fin.storage;

/* 
 * Ok guys, I've inserted JUnit inside here.
 * 
 * Usage:
 * fail(<message>) -- makes the test case fail. useful to check if a portion of code is run
 * assertEquals(<Any message you want to show>, <expected value>, <actual value>) 
 * assertsEquals([String message], expected, actual, tolerance)
 * assertNull([message], object)
 * assertNotNull([message], object)
 * assertSame([String], expected, actual)
 * assertNotSame([String], expected, actual)
 * assertTrue([message], boolean condition)
 * 
 * For more info, check out http://www.vogella.de/articles/JUnit/article.html#usingjunit_asserts
 */

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.application.FinSerializer;
import cs2103.aug11.t11j2.fin.datamodel.Task;


public class StorageTest {
	@Test
	public void testFailure() throws Exception {
		fail();
	}

	@Test
	public void testSerializer() throws IOException {
		System.out.println("Test Serializer");
		System.out.println("***");
		Task task1 = new Task();
		task1.setTaskName("hellO!");
		Task task2 = new Task();
		task2.setTaskName("omg! no way!");
		Task task3 = new Task();
		task3.setTaskName("hello world child1");
		Task task4 = new Task();
		task4.setTaskName("hello world child2");

		FinApplication.INSTANCE.add(task1);
		FinApplication.INSTANCE.add(task2);

		System.out.println("Serializing test...");

		FinSerializer fs = new FinSerializer();
		fs.serialize("test.yaml");

		System.out.println("Serializing test complete!");

		System.out.println("***");
		System.out.println("Deserializing test...");

		fs.unserialize("test.yaml");

		System.out.println("Deserializing test complete!");
		System.out.println("***");

		System.out.println("Test complete!");
	}



}
