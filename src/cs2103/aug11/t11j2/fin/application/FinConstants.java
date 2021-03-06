package cs2103.aug11.t11j2.fin.application;

import org.eclipse.swt.graphics.RGB;



/**
 * Contains standard constants for the Fin. application. 
 * 
 * @version 0.1
 * @author Koh Zi Chun
 * @author Acer Wei Jing
 * @author Alex Liew
 * @author Joe Chee
 */
public final class FinConstants {

	/* Static Class */
	private FinConstants() {
	}

	public enum EDEVELOPMENT_MODE {
		DEVELOPMENT, PRODUCTION
	};

	public static final EDEVELOPMENT_MODE DEVELOPMENT_MODE = EDEVELOPMENT_MODE.DEVELOPMENT;
	public static final boolean IS_DEVELOPMENT = (DEVELOPMENT_MODE == EDEVELOPMENT_MODE.DEVELOPMENT);
	public static final boolean IS_PRODUCTION = (DEVELOPMENT_MODE == EDEVELOPMENT_MODE.PRODUCTION);
	
	public static final char HASH_TAG_CHAR = '#';
	public static final char ESCAPE_CHAR = '\\';

	public static final String IMPORTANT_HASH_TAG = "impt";
	public static final String FIN_HASH_TAG = "fin";

	// Shouldn't start with HASH_TAG_CHAR/META_TAG_CHAR/ESCAPE_CHAR
	public static final String DUEDATE_PLACEHOLDER = "!-!DUEDATE!-!";

	public static final RGB DARKGRAY_COLOR = new RGB(35, 35, 35);
	public static final RGB BLACK_COLOR = new RGB(0, 0, 0);
	public static final RGB BACKGROUND_COLOR = new RGB(10, 10, 10);
	public static final RGB FOREGROUND_COLOR = new RGB(255, 255, 255);
	public static final RGB CLI_FOREGROUND_COLOR = new RGB(210, 210, 210);
	public static final RGB TOUR_STEP_COLOR = new RGB(255, 0, 0);
	public static final RGB BORDER_COLOR = new RGB(55, 55, 55);
	public static final RGB RED_COLOR = new RGB(255, 0, 0);
	public static final RGB HASHTAG_COLOR = new RGB(50, 150, 200);

	public static final RGB CLIHINT_COLOR = new RGB(85, 85, 85);
	public static final RGB CLIHINTMESSAGE_COLOR = new RGB(55, 55, 55);

	public static final String DEFAULT_FONT = "consolas";
	public static final int DEFAULT_FONTSIZE = 12;

	public static final String CLI_FONT = "consolas";
	public static final int CLI_FONTSIZE = 10;

	public static final String INPUT_FONT = "Calibri";
	public static final int INPUT_FONTSIZE = 14;

	public static final String HINT_FONT = "Calibri";
	public static final int HINT_FONTSIZE = 11;

	public static final String FOOTER_FONT = "Segoe UI";

}
