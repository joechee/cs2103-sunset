package cs2103.aug11.t11j2.fin.gui.listener;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

public class TestClass implements HotkeyListener {
	public void test() {
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

	@Override
	public void onHotKey(int arg0) {
		System.out.println("HotkeyPressed!");
		
	}

	public void dispose() {
		JIntellitype.getInstance().cleanUp();
		System.exit(0);
		
	}

}
