package cs2103.aug11.t11j2.fin.application;

import static org.junit.Assert.*;
import org.junit.Test;


public class FinApplicationTest {
	@Test
	public void TestDelete(){
		Task a = new Task("");
		FinApplication.INSTANCE.add(a);
		System.out.println(FinApplication.INSTANCE.getTasks());
		assertSame(FinApplication.INSTANCE.getTasks().get(0),a);
		
		
	}

}
