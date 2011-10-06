package cs2103.aug11.t11j2.fin.application;



import cs2103.aug11.t11j2.fin.ui.*;

/* Usage:
 * Fin -<UI> <filename>
 * Arguments can be swapped around.
 */

public class Fin {

	
	public static void main(String[] args) {
		try {
			parseArgs(args);
		} catch (IllegalArgumentException e) {
			System.out.print(e.getMessage());
		}

	}
	


	private static void parseArgs(String[] args) {
		IUserInterface UI = FinConstants.DEFAULT_UI;
		for (String i: args) {
			if (i.toCharArray()[0] == '-') {
				UI = parseUI(i);
			} else {
				FinApplication.INSTANCE.loadEnvironment(checkFilename(i));
			}
		}
		UI.mainLoop();
	}



	private static String checkFilename(String i) throws IllegalArgumentException {
		if (i == null) {
			throw new IllegalArgumentException("Invalid Input!");
		} else if (i.split(".").length == 0) {
			return i+FinConstants.fileExtension;
		}
		return null;
	}

	private static IUserInterface parseUI(String UIString) throws IllegalArgumentException {
		if (UIString.equals("-")) {
			throw new IllegalArgumentException ("No UI provided!");
		}
		UIString = UIString.toUpperCase().split("-")[1];
		if (UIString.equals("CLI")){
			return (IUserInterface) new CLI();
		} else if (UIString.equals("NUI")){
			return (IUserInterface) null;
		} else {
			throw new IllegalArgumentException ("UI not recognised!");
		}
	}
}
