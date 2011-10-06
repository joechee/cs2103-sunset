package cs2103.aug11.t11j2.fin.datamodel;

import java.util.Comparator;

/**
 * Comparator that sorts Tasks by PIndex
 */
public class TaskSortByPIndex implements Comparator<Task> {

	@Override
	public int compare(Task o1, Task o2) {
		// return o1.getpIndex().compareTo(o2.getpIndex());
		if (o1.getDueDate() == null && o2.getDueDate() == null) {
			return o1.getDateAdded().compareTo(o2.getDateAdded());
		} else if (o1.getDueDate() == null) {
			return 1;
		} else if (o2.getDueDate() == null) {
			return -1;
		}
		return o1.getDueDate().compareTo(o2.getDueDate());
	}

}
