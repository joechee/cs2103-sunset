package cs2103.aug11.t11j2.fin.application;

import java.io.IOException;


/**
 * A sandbox environment for FinApplication
 * does not do serialization and is basically a 
 * dummy environment for the tour and automated test
 * 
 * @author Koh Zi Chun
 *
 */
public class FinApplicationSandbox extends FinApplication {
	final public static FinApplicationSandbox INSTANCE = new FinApplicationSandbox();
	FinApplicationSandbox() { }

	@Override
	public boolean loadEnvironment(String filename) throws IOException {
		return true;
	}
	@Override
	protected void saveEnvironment() {
		return;
	}
}
