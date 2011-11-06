package cs2103.aug11.t11j2.fin.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cs2103.aug11.t11j2.fin.application.FinApplicationSandbox;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;
import cs2103.aug11.t11j2.fin.ui.UIContextTest;

public class TagTaskWithTagsCommandHandlerTest {

	UIContext context = new UIContextTest(FinApplicationSandbox.INSTANCE);
	TagTaskWithTagsCommandHandler tag = new TagTaskWithTagsCommandHandler();
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
		FinApplicationSandbox.INSTANCE.clearEnvironment();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecuteCommands() throws FinProductionException {
		int passed = 0;
		
		//add in tasks first
		String cmd, args;
		cmd = "+";
		args = "do this by next Sun";
		add.executeCommands(cmd, args, context);
		
		cmd = "+";
		args = "what is this";
		add.executeCommands(cmd, args, context);
		
		cmd = "+";
		args = "I do not know by next Sun";
		add.executeCommands(cmd, args, context);
	
		//test cases	
		
		commands.add("tag");
		arguments.add("6 acer");
		expected.add("Invalid Task Index!");
		
		commands.add("tag");
		arguments.add("1 acer");
		expected.add("Invalid Task Index!");
	
		commands.add("tag");
		arguments.add("1 what is this tag");
		expected.add("Invalid Task Index!");		
		
		
		int n = commands.size();
		for (int i=0; i<n; i++){			
			res = tag.executeCommands(commands.get(i), arguments.get(i), context);
			actual = res.getReturnObject();	
			System.out.println(arguments.get(i));
			System.out.println(expected.get(i));
			System.out.println(actual);
			boolean isEqual = checkEqual(expected.get(i), actual);			
			Assert.assertFalse(!isEqual);
			passed++;
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
		@SuppressWarnings("unchecked")
		List<Object> list1 = (ArrayList<Object>)obj1;
		@SuppressWarnings("unchecked")
		List<Object> list2 = (ArrayList<Object>)obj2;
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


}
