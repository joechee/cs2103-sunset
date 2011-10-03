package cs2103.aug11.t11j2.fin.functest;

import java.io.*;
import java.util.*;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.application.FinSerializer;
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.jotd.Joke;
import cs2103.aug11.t11j2.fin.storage.Serializer;
import cs2103.aug11.t11j2.fin.parser.CommandParser;
/* A set of routines to test components
 * Please create a new method to test for separate components. 
 * 
 * Notes: 
 * - I've committed snakeyaml-1.9.jar into the project folder. Essentially this is the library
 *   for snakeYAML, which is a possible way in which we could store our task data. To use the 
 *   library in your build, click Project, then click Properties, then click on the libraries
 *   tab, and add the JAR file into the project. The errors in Serializer should then disappear.
 *   
 * - When committing, just commit from the src folder. Don't commit the .project files
 * 
 */
public class test {
	public static void main(String[] args) throws IOException {
		testJOTD();
		//testSerializer();
		//testParser();

	}
	
	static public void testParser() {
		System.out.println(CommandParser.parse("add hello"));
		
		
	}
	
	public static void testJOTD() throws IOException {
		Joke testJoke = new Joke();
		System.out.println(testJoke.generate());
	}

	
	static public void testSerializer() throws IOException {
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
		
			FinApplication.INSTANCE.add( task1, null );
			FinApplication.INSTANCE.add( task2, null );
			FinApplication.INSTANCE.add(task3, task1.getUniqId());
			FinApplication.INSTANCE.add(task4, task1.getUniqId());

			
		System.out.println("Serializing test...");

		
			FinSerializer fs = new FinSerializer();
			fs.serialize("test.yaml");
		
		System.out.println("Serializing test complete!");
		
		System.out.println("***");
		System.out.println("Deserializing test...");
		
		fs.unserialize("test.yaml");
		dfs(null, 0);
		
		
		
		System.out.println("Deserializing test complete!");
		System.out.println("***");
		
		System.out.println("Test complete!");

		
	}
	
	public static void dfs(UUID parent, int cnt) {
		List<Task> ft = FinApplication.INSTANCE.getTasks(parent);
		for (Task t : ft) {
			for (int i=0;i<cnt;++i) System.out.print(" ");
			System.out.println(t.getTaskName());
			dfs(t.getUniqId(), cnt+3);
		}
	}
}

