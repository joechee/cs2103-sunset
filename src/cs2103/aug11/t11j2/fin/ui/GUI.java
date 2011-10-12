package cs2103.aug11.t11j2.fin.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;

import cs2103.aug11.t11j2.fin.application.Fin.IUserInterface;

public class GUI implements IUserInterface {

	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.mainLoop();
	}

	@Override
	public void mainLoop() {
		// TODO Auto-generated method stub
		Display display = new Display();
		final Shell shell = new Shell(display, SWT.NO_TRIM);
		shell.setText("Hello world");
		shell.open();
		//should have some layout design document which states the numbers and link it to this piece
		// of code. so people who view have an idea of what the magic numbers are i think. 
		Text box = new Text (shell, SWT.UNDERLINE_DOUBLE);
		

		Button cancel = new Button (shell, SWT.PUSH);
		cancel.setLocation(100,200);
		cancel.setText("CANCEL");
		cancel.pack();
		Label label = new Label(shell, SWT.NONE);
		label.setText("hello World");
		label.toDisplay(1,1);
		label.setSize(40,15);
		cancel.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Cancel");
			}
		});
		Listener l = new Listener() {

			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
					case SWT.MouseDown:
						System.out.println("o.O");
						break;
					case SWT.KeyDown:
						
						
						break;
					default:
						System.out.println(e.type);
				}
			}
			
		};
		shell.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.keyCode == 27) {
					shell.dispose();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		shell.addListener(SWT.MouseDown,l);
		shell.addListener(SWT.KeyDown,l);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}

		}

		
		display.dispose();
	}
}

	


