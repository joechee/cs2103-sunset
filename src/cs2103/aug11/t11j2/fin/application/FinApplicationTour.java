package cs2103.aug11.t11j2.fin.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cs2103.aug11.t11j2.fin.datamodel.Task;

/**
 * Fin Application class to handle guided tour
 * does not do serialization and creates a sandbox environment 
 * for the tour
 * 
 * @author Koh Zi Chun
 *
 */
public class FinApplicationTour implements Fin.IFinApplication {
	final static public FinApplicationTour INSTANCE = new FinApplicationTour();
	
	private FinApplicationTour() {
		
	}
	
	@Override
	public void add(Task task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Task> getTasksWithTag(String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getTasksWithoutTag(String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getTasksWithoutTags(List<String> tags) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getTasksWithTags(List<String> tags) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteTask(UUID taskUID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean flagTask(UUID taskUID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unflagTask(UUID taskUID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean finTask(UUID taskUID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unfinTask(UUID taskUID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void loadEnvironment(String filename) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getHashTags() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearEnvironment() {
		// TODO Auto-generated method stub
		
	}

}
