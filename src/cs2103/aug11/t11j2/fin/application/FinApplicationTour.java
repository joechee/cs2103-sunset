package cs2103.aug11.t11j2.fin.application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;


/**
 * Fin Application class to handle guided tour
 * does not do serialization and creates a sandbox environment 
 * for the tour
 * 
 * @author Koh Zi Chun
 *
 */
public class FinApplicationTour extends FinApplication {
	final public static FinApplicationTour INSTANCE = new FinApplicationTour();
	
	FinApplicationTour() {
		
	}

	
	@Override
	public boolean loadEnvironment(String filename) throws IOException {
		return true;
	}

	@Override
	protected void saveEnvironment() {
		return;
	}

}
