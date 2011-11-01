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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.application.FinSerializer;
import cs2103.aug11.t11j2.fin.application.Task;


public class StorageTest {
	@Test
	public void testFailure() throws Exception {
		fail();
	}

	@Test
	public void testSerializer() throws IOException {
		System.out.println("Test Serializer");
		System.out.println("***");


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
	public void testUnserializer() throws IOException {
		Map<String, Object> dict = new HashMap<String,Object>();
		System.out.println(dict.get("0"));
		
	}



}
