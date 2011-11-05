package cs2103.aug11.t11j2.fin.application;

import static org.junit.Assert.*;

import org.junit.Test;


public class FinSerializerTest {
	@Test
	public void TestUnserialize() {
		
		assertEqual(FinSerializer.serialize("test.txt",), test);
		
	}

}
