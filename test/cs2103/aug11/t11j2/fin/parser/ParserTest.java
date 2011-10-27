package cs2103.aug11.t11j2.fin.parser;

import java.io.IOException;

import org.junit.Test;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;

public class ParserTest {
	@Test
	public void testParser() throws IOException {
		System.out.println(CommandParser.INSTANCE.parse("add hello", null));
	}
	
	@Test
	public void testHelpCommandHandler() throws FinProductionException {
		HelpCommandHandler test = new HelpCommandHandler();
		test.showHelp();
	}

}
