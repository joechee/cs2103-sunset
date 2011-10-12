package cs2103.aug11.t11j2.fin.application;

import org.eclipse.swt.graphics.RGB;

/**
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
		PRODUCTION, DEPLOYMENT
	};

	public static final EDEVELOPMENT_MODE DEVELOPMENT_MODE = EDEVELOPMENT_MODE.PRODUCTION;
	public static final boolean IS_PRODUCTION = (DEVELOPMENT_MODE == EDEVELOPMENT_MODE.PRODUCTION);
	public static final char HASH_TAG_CHAR = '#';
	public static final char META_TAG_CHAR = '$';

	public static final String IMPORTANT_HASH_TAG = "impt";
	public static final String FIN_HASH_TAG = "fin";

	public static final String DUEDATE_PLACEHOLDER = "#-#DUEDATE#-#";

	public static final RGB BACKGROUND_COLOR = new RGB(10, 10, 10);
	public static final RGB FOREGROUND_COLOR = new RGB(255, 255, 255);
	public static final RGB BORDER_COLOR = new RGB(55, 55, 55);
	public static final RGB RED_COLOR = new RGB(255, 0, 0);
	public static final RGB HASHTAG_COLOR = new RGB(50, 150, 200);
	public static final RGB CLIHINT_COLOR = new RGB(100, 100, 100);

	public static final String DEFAULT_FONT = "consolas";
	public static final int DEFAULT_FONTSIZE = 12;

	public static final String INPUT_FONT = "Segoe UI";
	public static final int INPUT_FONTSIZE = 14;
}
