package cs2103.aug11.t11j2.fin.gui;

import java.util.List;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.datamodel.Task;

public class FinCLI extends Composite {
	Composite display;
	ScrolledComposite displaySC;
	Text input;
	Vector<FinCLIInputListener> userInputListeners = new Vector<FinCLIInputListener>();

	public FinCLI(Composite parent, int style) {
		super(parent, style);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		gridLayout.marginTop = gridLayout.marginLeft = gridLayout.marginRight = gridLayout.marginBottom = -5;
		this.setLayout(gridLayout);

		createDisplay();
		createHR();
		createInput();

		handlerUserInput();	
	}

	private void createDisplay() {
		this.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));

		displaySC = new ScrolledComposite(this, SWT.V_SCROLL);
		display = new Composite(displaySC, SWT.NONE);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 10;
		gridLayout.marginTop = gridLayout.marginLeft = gridLayout.marginRight = gridLayout.marginBottom = -5;

		display.setLayout(gridLayout);
		display.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));

		displaySC.setContent(display);
		displaySC.setExpandVertical(true);
		displaySC.setExpandHorizontal(true);
		displaySC.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));

		displaySC.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, true));		

		displaySC.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				resize();
			}
		});
	}

	private void createInput() {
		input = new Text(this, SWT.NONE);

		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				false);
		gridData.heightHint = 30;
		input.setLayoutData(gridData);

		input.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));
		input.setFont(new Font(this.getDisplay(), FinConstants.INPUT_FONT, FinConstants.INPUT_FONTSIZE, SWT.NORMAL));
		input.setForeground(new Color(null, FinConstants.FOREGROUND_COLOR));
	}

	// creates a visual horizontal line between display and input
	private void createHR() {
		Label label = new Label(this, SWT.NONE);

		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				false);
		gridData.heightHint = 1;
		label.setLayoutData(gridData);

		label.setBackground(new Color(null, FinConstants.BORDER_COLOR));
	}

	void handlerUserInput() {
		input.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				return;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == 13) {
					String userInput = input.getText().trim();
					if (userInput.length() > 0) {
						for (FinCLIInputListener listener : userInputListeners) {
							listener.UserInput(new FinCLIInputEvent(input,
									userInput));
						}

						input.setText("");
					}
				}
			}

		});
	}

	void resize() {
		Rectangle r = displaySC.getClientArea();
		displaySC.setMinSize(display.computeSize(r.width, SWT.DEFAULT));
	}

	public void echo(String text) {
		StyledText t = new StyledText(display, SWT.WRAP);

		t.setFont(new Font(this.getDisplay(), FinConstants.DEFAULT_FONT, FinConstants.DEFAULT_FONTSIZE, SWT.NORMAL));
		t.setText(text.trim());

		t.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));
		t.setForeground(new Color(null, FinConstants.FOREGROUND_COLOR));
		

		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				false);
		gridData.verticalIndent = gridData.horizontalIndent = 10;
		t.setLayoutData(gridData);
	}

	public void addTaskList(List<Task> taskList) {
		TaskContainer tc = new TaskContainer(display, SWT.NONE);
		for (Task t : taskList) {
			tc.addTask(t);
		}
		refresh();
	}

	public void refresh() {
		display.layout(true);
		resize();
		displaySC.setOrigin(0, display.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
	}

	public void addUserInputListener(FinCLIInputListener listener) {
		userInputListeners.addElement(listener);
	}

	public void removeUserInputListener(FinCLIInputListener listener) {
		userInputListeners.removeElement(listener);
	}

}
