package cs2103.aug11.t11j2.fin.gui;
/**
 * @author Wei Jing
 */
import java.util.EventObject;

public class FinCLIInputEvent extends EventObject {
	private static final long serialVersionUID = -227747326324453267L;
	public String input;

	public FinCLIInputEvent(Object source, String s) {
		super(source);

		this.input = s;
	}
}
