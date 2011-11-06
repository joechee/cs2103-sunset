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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


public class StorageTest {


	@Test
	public void testSerializer() throws IOException {
		System.out.println("Test Serializer");
		System.out.println("***");


		System.out.println("Serializing test...");

		Serializer fs = new Serializer();
		fs.unserialize("test.yaml");

		System.out.println("Serializing test complete!");

		System.out.println("***");
		System.out.println("Deserializing test...");

		fs.unserialize("test.yaml");

		System.out.println("Deserializing test complete!");
		System.out.println("***");

		System.out.println("Test complete!");
	}
	
	@Test
	public void testUnserializer() throws IOException {
		Map<String, Object> dict = new HashMap<String,Object>();
		System.out.println(dict.get("0"));
		
	}



}
