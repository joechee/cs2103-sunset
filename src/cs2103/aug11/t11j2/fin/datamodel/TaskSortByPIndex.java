package cs2103.aug11.t11j2.fin.datamodel;

import java.util.Comparator;

/**
 * Comparator that sorts Tasks by PIndex
 */
public class TaskSortByPIndex implements Comparator<Task> {

	@Override
	public int compare(Task o1, Task o2) {
		return o1.getpIndex().compareTo(o2.getpIndex());
	}

}
