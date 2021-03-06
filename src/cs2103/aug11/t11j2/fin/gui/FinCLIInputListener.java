package cs2103.aug11.t11j2.fin.gui;
/**
 * Interface for Fin.-specific listener class
 * 
 * @author Wei Jing
 */
import java.util.EventListener;

public interface FinCLIInputListener extends EventListener {
	public void userInput(FinCLIInputEvent event);
	public void onChange(FinCLIInputEvent event);
}
