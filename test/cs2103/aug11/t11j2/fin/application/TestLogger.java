package cs2103.aug11.t11j2.fin.application;

import org.apache.log4j.Logger;




public class TestLogger {
	public static Logger initializeLogger(){
		return FinLogger.initializeLogger(new Object());
	}


}
