package cs2103.aug11.t11j2.fin.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;
import cs2103.aug11.t11j2.fin.application.*;

public class AddCommandHandlerTest implements Fin.IUserInterface{
	
	UIContext context = new UIContext(FinApplication.INSTANCE);
	AddCommandHandler add = new AddCommandHandler();
	
	ArrayList <String> commands = new ArrayList<String>();
	ArrayList <String> arguments = new ArrayList<String>();
	ArrayList <Object> expected = new ArrayList<Object>();
	Object actual;
	CommandResult res;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecuteCommands() throws FinProductionException {
		String exp;
		int passed = 0;
		
		commands.add("+");
		arguments.add("do this by next Sun");
		exp = "do this [next Sunday]";
		expected.add(exp);
		
		commands.add("add");
		arguments.add("do this #impt by next Sunday");
		exp = "do this #impt [next Sunday]";
		expected.add(exp);
		
		commands.add("");
		arguments.add("do this by next Sunday");
		exp = "do this [next Sunday]";
		expected.add(exp);
		
		commands.add("+");
		arguments.add("do this #impt due 1 Dec");
		exp = "do this #impt [01 Dec]";
		expected.add(exp);
		
		int n = commands.size();
		for (int i=0; i<n; i++){
			res = add.executeCommands(commands.get(i), arguments.get(i), context);
			actual = res.getReturnObject().toString();			
			boolean isEqual = checkEqual(expected.get(i), actual);			
			if (isEqual) 
				passed++;
			else
				System.out.println("Passed " + passed + " out of " + n);				
			Assert.assertFalse(!isEqual);
		}
		System.out.println("Passed " + passed + " out of " + n);
	}
	
	boolean checkEqual (Object obj1, Object obj2){
		if (obj1.getClass() != obj2.getClass()){
			return false;
		}
		if (obj1 instanceof String){
			return obj1.equals(obj2);
		}
		// List
		List<Task> list1 = (ArrayList<Task>)obj1;
		List<Task> list2 = (ArrayList<Task>)obj2;
		if (list1.size() != list2.size()){
			return false;
		}
		int n = list1.size();
		for (int i=0; i<n; i++){
			String str1, str2;
			str1 = list1.get(i).toString();
			str2 = list2.get(i).toString();
			if (!str1.equals(str2))
				return false;
		}		
		return true;		
	}

	@Test
	public void testGetHelp() {
		String expected = "add <task>\n\tAdds <task> to your task list";
		String actual = add.getHelp();		
		assert(expected.equals(actual));
	}


	public void mainLoop(boolean fileLoaded) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean runCommandAndRender(String userArgs) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void echo(String promptMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initUI(boolean fileLoaded) {
		// TODO Auto-generated method stub
		
	}

}
