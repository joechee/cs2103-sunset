package cs2103.aug11.t11j2.fin.parseTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.datamodel.Task;

/**
 * Task Parser
 * 
 * @author Koh Zi Chun
 */
public class TaskParser2 {

	public static Task parse(String task) {
		Date due = null;
		
		DateParser dp = new DateParser();
		if (dp.parse(task)) {
			task = dp.getParsedString();
			due = dp.getParsedDate();
		}

		return new Task(task, null, due, 0, 0);
	}

}
