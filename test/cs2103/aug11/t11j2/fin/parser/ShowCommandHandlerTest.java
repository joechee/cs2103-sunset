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
import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;
import cs2103.aug11.t11j2.fin.ui.UIContextTest;

public class ShowCommandHandlerTest {

	UIContext context = new UIContextTest(FinApplication.INSTANCE);
	ShowCommandHandler show = new ShowCommandHandler();
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
		
		ArrayList<String> taskList = new ArrayList<String>();
		
		int m = context.getTaskList().size();
		for (int i=0; i<m; i++){
			String str = context.getTaskList().get(i).toString();
			taskList.add(str);
		}		
		
		//test cases
		ArrayList<String> exp = new ArrayList<String> ();
			
		commands.add("show");
		arguments.add("");
		exp.clear();
		for (int i=0; i<m; i++){
			exp.add(i+1 + ". " + taskList.get(i));
		}
		expected.add(exp);
		
		commands.add("show");
		arguments.add("this");
		exp.clear();
		int j=1;
		for (int i=0; i<m; i++){
			if(taskList.get(i).indexOf("this")>-1){
				exp.add(j + ". " + taskList.get(i));
				j++;
			}
		}
		expected.add(exp);
		
		commands.add("show");
		arguments.add("t");
		exp.clear();
		j=1;
		for (int i=0; i<m; i++){
			if(taskList.get(i).indexOf("t")>-1){
				exp.add(j + ". " + taskList.get(i));
				j++;
			}
		}
		expected.add(exp);
				
		commands.add("show");
		arguments.add("ttttttt");
		expected.add("There are not tasks");
		
		int n = commands.size();
		for (int i=0; i<n; i++){
			System.out.println(arguments.get(i));
			res = show.executeCommands(commands.get(i), arguments.get(i), context);
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

}
