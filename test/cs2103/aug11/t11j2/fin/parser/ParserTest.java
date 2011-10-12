package cs2103.aug11.t11j2.fin.parser;

import java.io.IOException;

import org.junit.Test;

public class ParserTest {
	@Test
	public void testParser() throws IOException {
		System.out.println(CommandParser.INSTANCE.parse("add hello", null));
	}

}
