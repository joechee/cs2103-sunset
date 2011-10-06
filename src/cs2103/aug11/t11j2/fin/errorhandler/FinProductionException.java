package cs2103.aug11.t11j2.fin.errorhandler;

/**
 * Exception class for defensive exceptions These are exceptions that shouldn't
 * even be thrown
 * 
 * @author Koh Zi Chun
 */
public class FinProductionException extends Exception {
	private static final long serialVersionUID = -4695616989446674617L;

	public FinProductionException(String s) {
		super(s);
	}
}
