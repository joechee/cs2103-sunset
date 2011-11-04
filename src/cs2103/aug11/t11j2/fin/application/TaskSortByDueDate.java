package cs2103.aug11.t11j2.fin.application;

import java.util.Comparator;

/**
 * Comparator that sorts Tasks by due date and date added.
 */
class TaskSortByDueDate implements Comparator<Task> {

	@Override
	public int compare(Task o1, Task o2) {

		if (o1.getDueDate() == null && o2.getDueDate() == null) {
			// if both due dates not set, sort it by date added instead
			return o1.getDateAdded().compareTo(o2.getDateAdded());
		} else if (o1.getDueDate() == null) {
			// if o1's duedate not set, put o2 on top
			return 1;
		} else if (o2.getDueDate() == null) {
			// symmetric case
			return -1;
		}

		// put the stuff that is due first on top
		return o1.getDueDate().compareTo(o2.getDueDate());
	}

}
