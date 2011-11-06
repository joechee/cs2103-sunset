package cs2103.aug11.t11j2.fin.application;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class FinLogger extends Logger{
	
	protected FinLogger(String name) {
		super(name);
	}

	enum LogDestination {FILE,CONSOLE};
	//Change to LogDestination.CONSOLE to log to console
	private static Level LOGGING_LEVEL = Level.DEBUG;
	private static final String LOG_FILE = "fin.log";
	
	static Logger initializeLogger(Object o) {
		/*
		LogDestination dest = LogDestination.CONSOLE;
		
		try {
			Logger rootLogger = Logger.getRootLogger();
			Writer logDest = null;
			if (dest == LogDestination.FILE){
				logDest = new FileWriter(LOG_FILE);
			} else if (dest == LogDestination.CONSOLE){
				logDest = new PrintWriter(System.out);
			} 
			rootLogger.setLevel(LOGGING_LEVEL);
			
			assert (logDest != null);

			WriterAppender test = new WriterAppender(new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN),logDest);
			rootLogger.addAppender(test);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	

		*/
		return Logger.getLogger(o.getClass());
	}

}
