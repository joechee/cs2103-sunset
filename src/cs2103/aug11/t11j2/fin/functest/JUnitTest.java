package cs2103.aug11.t11j2.fin.functest;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.application.FinSerializer;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.datamodel.Task.EImportance;
import cs2103.aug11.t11j2.fin.jotd.Joke;
import cs2103.aug11.t11j2.fin.parser.CommandParser;


public class JUnitTest {
	@Test
	public void testFailure() throws Exception {
		fail();
	}
	
	@Test
	public void testJOTD() throws IOException {
		Joke testJoke = new Joke();
		assertEquals("Joke","Abortion brings out the kid in you.",testJoke.generate());
	}
	
	@Test
	public void testTask() throws Exception{
		Task test = new Task("test",new ArrayList<String>(),EImportance.LOW,null,0,0);
	
		assertEquals("taskName","test",test.getTaskName());
		assertEquals("tags",new ArrayList<String>(),test.getTags());
		assertEquals("Importance",EImportance.LOW,test.getImportance());
		assertEquals("dueDate",null,test.getDueDate());		
		assertSame("percentageCompleted",0,test.getPercentageCompleted());		
		assertSame("pIndex",0,test.getpIndex());
		
	}
	
	@Test
	public void testMain() throws Exception {
		
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
	@Test
	public void testParser() throws IOException {
		System.out.println(CommandParser.INSTANCE.parse("add hello"));
		
		
	}

}
