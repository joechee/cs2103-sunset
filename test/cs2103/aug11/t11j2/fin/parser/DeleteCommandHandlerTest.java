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

import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.application.FinApplicationSandbox;
import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;
import cs2103.aug11.t11j2.fin.ui.UIContextTest;

public class DeleteCommandHandlerTest {
	UIContext context = new UIContextTest(FinApplicationSandbox.INSTANCE);
	DeleteCommandHandler del = new DeleteCommandHandler();
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
		String exp;
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
		
		cmd = "+";
		args = "what is by 1 Dec";
		add.executeCommands(cmd, args, context);
		
		int m = context.getTaskList().size();
		System.out.println(m);
		
		//test cases
		commands.add("del");
		arguments.add("-1");
		exp = "Invalid Task Index!";
		expected.add(exp);
		
		commands.add("del");
		arguments.add("7");
		exp = "Invalid Task Index!";
		expected.add(exp);
		
		commands.add("del");
		arguments.add("aaa");
		exp = "Invalid Task Index!";
		expected.add(exp);
		
		commands.add("del");
		arguments.add("0x1");
		exp = "Invalid Task Index!";
		expected.add(exp);
		
		commands.add("del");
		arguments.add("0");
		exp = "Invalid Task Index!";
		expected.add(exp);
		
		commands.add("del");
		arguments.add("4");
		exp = "Invalid Task Index!";
		expected.add(exp);
		
		commands.add("del");
		arguments.add("1");
		exp = "Invalid Task Index!";
		expected.add(exp);
		
		commands.add("del");
		arguments.add("all");
		ArrayList <String> taskList = new ArrayList<String> ();
		
		taskList.add("do this [next Sunday]");
		taskList.add("I do not know [next Sunday]");		
		taskList.add("what is [01 Dec]");
		taskList.add("what is this");
		expected.add(taskList);
		
		int n = commands.size();
		for (int i=0; i<n; i++){
			System.out.println(arguments.get(i));
			res = del.executeCommands(commands.get(i), arguments.get(i), context);
			actual = res.getReturnObject();	
			
			System.out.println(actual);
			System.out.println(expected.get(i));
			
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
		List<Object> list1 = (ArrayList<Object>)obj1;
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

	@Test
	public void testGetHelp() {
		String expected = "delete <task number>\n\tDeletes a task from your task list";
		String actual = del.getHelp();	
		System.out.println(expected);
		System.out.println(actual);
		boolean isEqual = actual.equals(expected);
		Assert.assertFalse(!isEqual);
	}

}
