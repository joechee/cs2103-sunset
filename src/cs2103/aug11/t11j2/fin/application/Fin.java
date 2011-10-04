package cs2103.aug11.t11j2.fin.application;


import cs2103.aug11.t11j2.fin.application.FinConstants.UI;
import cs2103.aug11.t11j2.fin.ui.CLI;

/* Usage:
 * Fin -<UI> <filename>
 * Arguments can be swapped around.
 */

public class Fin {

	private static UI ui = FinConstants.DEFAULT_UI;
	private static String calendarFile = FinConstants.DEFAULT_FILE;
	
	private static class InputException extends Exception {

		private static final long serialVersionUID = 1L;
		public InputException(String e) {
			super(e);	
		}
		
	}
	

	
	public static void main(String[] args) {
		try {
			parseArgs(args);
			FinApplication.INSTANCE.loadEnvironment(calendarFile);
			LoadUI();
		} catch (InputException e) {
			System.out.print(e.getMessage());
		}

	}
	
	private static void LoadUI() throws InputException {
		switch (ui) {
		case CLI:
			CLI.mainLoop();
			return;
		default:
			throw new InputException("Type of UI not recognised");
				
		/*
		case GUI:
			GUI.mainLoop();
			return;
			
		case WeirdUI:
			WeirdUI.mainLoop();
			return;
		
		case JokeUI:
			JokeUI.mainLoop();
			return;
		*/
		}
		
	}

	public static void parseArgs(String[] args) throws InputException {
	
		
		for (String i: args) {
			if (i.toCharArray()[0] == '-') {
				parseUI(i);
			} else {
				calendarFile = i;
			}
		}
		
		
		
	}

	private static void parseUI(String UIString) throws InputException {
		
		if (UIString.equals("-")) {
			throw new InputException ("No UI provided!");
		}
		UIString = UIString.toUpperCase().split("-")[1];
		if (UIString.equals("CLI")){
			ui = UI.CLI;
		} else if (UIString.equals("GUI")){
			ui = UI.GUI;
		} else {
			throw new InputException ("UI not recognised!");
		}

		
		
	}

}
