package cs2103.aug11.t11j2.fin.gui.listener;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

public class GlobalHotkeyListener implements HotkeyListener {
	/**
	 * Initialises the GlobalHotkeyListener
	 */
	public void initialize() {
		JIntellitype.getInstance();

		// OPTIONAL: check to see if an instance of this application is already
	        // running, use the name of the window title of this JFrame for checking
		if (JIntellitype.checkInstanceAlreadyRunning("MyApp")) {
			System.out.println("An instance of this application is already running");
			System.exit(1);
		}
		
		JIntellitype.getInstance().addHotKeyListener(this);
		
		JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_WIN, (int)'A');
	}
	
	/**
	 * Important to clean up resources after listener is used.
	 */
	public void dispose() {
		JIntellitype.getInstance().cleanUp();
		System.exit(0);
	}
	/**
	 * Code to run onHotKey
	 * @see com.melloware.jintellitype.HotkeyListener#onHotKey(int)
	 */

	@Override
	public void onHotKey(int identifier) {
		// TODO Auto-generated method stub
		
	}

}
