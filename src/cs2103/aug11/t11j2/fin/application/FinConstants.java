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
	
	enum EDEVELOPMENT_MODE { 
		PRODUCTION, 
		DEPLOYMENT 
	};
	
	static final EDEVELOPMENT_MODE DEVELOPMENT_MODE = EDEVELOPMENT_MODE.PRODUCTION;
	static final char HASH_TAG_CHAR = '#';
	static final char META_TAG_CHAR = '$';
	
}
