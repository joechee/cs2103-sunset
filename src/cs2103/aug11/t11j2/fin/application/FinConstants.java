package cs2103.aug11.t11j2.fin.application;

/**
 * 
 * @version 0.1
 * @author Koh Zi Chun
 * @author Acer Wei Jing
 * @author Alex Liew
 * @author Joe Chee
 */
public final class FinConstants {
	
	/* Constants */
	public final static String DEFAULT_FILE = "fin.txt";
	
	/* Static Class */
	private FinConstants () { }
	
	public enum EDEVELOPMENT_MODE { 
		PRODUCTION, 
		DEPLOYMENT 
	};
	public enum EUI_MODE {
		CLI, GUI
	};
	
	public static final EDEVELOPMENT_MODE DEVELOPMENT_MODE = EDEVELOPMENT_MODE.PRODUCTION;
	public static final EUI_MODE UI_MODE = EUI_MODE.CLI;
	public static final boolean IS_PRODUCTION = (DEVELOPMENT_MODE == EDEVELOPMENT_MODE.PRODUCTION); 
	public static final char HASH_TAG_CHAR = '#';
	public static final char META_TAG_CHAR = '$';
	
}
