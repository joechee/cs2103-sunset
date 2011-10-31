package cs2103.aug11.t11j2.fin.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.application.Task;

public class FinCLIComposite extends Composite {
	Composite display;
	ScrolledComposite displaySC;
	StyledText input;
	Vector<FinCLIInputListener> userInputListeners = new Vector<FinCLIInputListener>();
	
	private List<String> userInputHistory = new ArrayList<String>();
	private int userInputHistoryPointer = 0;

	public FinCLIComposite(Composite parent, int style) {
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
		input = new StyledText(this, SWT.SINGLE);
		input.setLeftMargin(5);
		input.setTopMargin(3);

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

	private String previousString = "";
	void handlerUserInput() {
		
		input.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_TAB_NEXT && onAutoComplete) {
					e.doit = true;
					input.setSelection(input.getText().length());
					onAutoComplete = false;
				}
			}
		});
		
		input.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_UP) {
					if (userInputHistoryPointer > 0) {
						userInputHistoryPointer--;
						input.setText(userInputHistory.get(userInputHistoryPointer));
					}
					e.doit = false;
				} else if (e.keyCode == SWT.ARROW_DOWN) {
					if (userInputHistoryPointer < userInputHistory.size()) {
						userInputHistoryPointer++;
						if (userInputHistoryPointer < userInputHistory.size()) {
							input.setText(userInputHistory.get(userInputHistoryPointer));
						} else {
							input.setText("");
						}
					}
					e.doit = false;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (onAutoComplete) {
					if (e.keyCode == SWT.KEYPAD_CR || e.keyCode == 13 || e.keyCode == 10) {
						input.setSelection(input.getText().length());
						onAutoComplete = false;
					}
				} else if (e.keyCode == SWT.KEYPAD_CR || e.keyCode == 13 || e.keyCode == 10) {
					String userInput = input.getText().trim();
					if (userInput.length() > 0) {
						userInputHistory.add(userInput);
						userInputHistoryPointer = userInputHistory.size();
						
						runInput(userInput);
						input.setText("");
					}
				} else if (e.keyCode == SWT.ESC) {
					input.setText("");
				}
				
				if (!input.getText().equals(previousString) && e.keyCode != 8 && e.keyCode != SWT.DEL) {
					previousString = input.getText();
					onAutoComplete = false;
					onChange(previousString);
				} else {
					onAutoComplete = false;
				}
			}

		});
	}
		
	void resize() {
		Rectangle r = displaySC.getClientArea();
		displaySC.setMinSize(display.computeSize(r.width, SWT.DEFAULT));
	}

	/** 
	 * Echos the text onto the CLI
	 * 
	 * @param text text to echo
	 */
	public void echo(String text) {
		StyledText t = new StyledText(display, SWT.WRAP);

		t.setFont(new Font(this.getDisplay(), FinConstants.CLI_FONT, FinConstants.CLI_FONTSIZE, SWT.NORMAL));
		t.setText(text.trim());

		t.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));
		t.setForeground(new Color(null, FinConstants.CLI_FOREGROUND_COLOR));		
		

		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				false);
		gridData.verticalIndent = gridData.horizontalIndent = 10;
		t.setLayoutData(gridData);
		t.setEnabled(false);
	}

	/**
	 * Add a list of task to the FinCLI
	 * 
	 * @param taskList
	 */
	public void addTaskList(List<Task> taskList) {
		TaskContainer tc = new TaskContainer(display, SWT.NONE);
		for (Task t : taskList) {
			tc.addTask(t);
		}
		refresh();
	}

	/**
	 * Refresh the layout of the component
	 */
	public void refresh() {
		display.layout(true);
		resize();
		displaySC.setOrigin(0, display.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
	}

	/**
	 * Clear the screen of FinCLI control
	 */
	public void clear() {
		for (Control c : display.getChildren()) {
			c.dispose();
		}
	}

	public void addUserInputListener(FinCLIInputListener listener) {
		userInputListeners.addElement(listener);
	}

	public void removeUserInputListener(FinCLIInputListener listener) {
		userInputListeners.removeElement(listener);
	}
	
	private String beforeHint;
	private boolean isInHint = false;
	private boolean inputHasFocus;
	
	/**
	 * @return whether the control is currently showing a hint
	 */
	public boolean isInHint() {
		return this.isInHint;
	}
	
	/**
	 * Set the text in the input box to another text
	 * 
	 * @param text
	 */
	public void setText(String text) {
		if (this.isInHint) {
			removeHint();
		}
		input.setText(text);
	}
	
	/**
	 * Set hint in the input box to describe what a GUI action does
	 * 
	 * @param hint
	 */
	public void setHint(String hint) {
		setHint(hint, "");
	}
	
	/**
	 * Set hint with additional message in the inputbox, with an additional message
	 * to describe what the hint action does
	 * 
	 * @param hint
	 * @param additionalMessage
	 */
	public void setHint(String hint, String additionalMessage) {
		
		hint = hint.trim();
		additionalMessage = additionalMessage.trim();

		if (isInHint == false) {
			isInHint = true;
			this.beforeHint = input.getText();
		}
		
		this.inputHasFocus = input.isFocusControl();
		
		input.setEnabled(false);
		if (additionalMessage.length() == 0) {
			input.setText(hint);
		} else {
			input.setText(hint + " : "  + additionalMessage);

			StyleRange taskComplete = new StyleRange();
			taskComplete.font = new Font(this.getDisplay(), FinConstants.HINT_FONT, FinConstants.HINT_FONTSIZE, SWT.NORMAL);
			taskComplete.foreground = new Color(null, FinConstants.CLIHINTMESSAGE_COLOR);
			taskComplete.start = hint.length();
			taskComplete.length = additionalMessage.length() + 3;

			input.setStyleRange(taskComplete);
		}
		
		input.setForeground(new Color(null, FinConstants.CLIHINT_COLOR));
	}
	
	
	/**
	 * Remove hints from input box and revert what the original command was
	 */
	public void removeHint() {
		isInHint = false;
		input.setEnabled(true);
		input.setText(beforeHint);
		input.setForeground(new Color(null, FinConstants.FOREGROUND_COLOR));
		
		if (this.inputHasFocus) {
			this.forceFocus();
		}
	}
	
	/**
	 * force focus on the FinCli input
	 */
	public boolean forceFocus() {
		boolean focused = input.forceFocus();
		if (focused) {
			input.setSelection(input.getText().length());
		}
		return focused;
	}
	
	/**
	 * Programmatically invoke an onchange event
	 * 
	 * @param userInput
	 */
	public void onChange(String userInput) {
		for (FinCLIInputListener listener : userInputListeners) {
			listener.onChange(new FinCLIInputEvent(input, userInput));
		}
	}
	
	/**
	 * Programmatically run a user input on the command interface
	 * 
	 * @param userInput
	 */
	public void runInput(String userInput) {
		for (FinCLIInputListener listener : userInputListeners) {
			listener.userInput(new FinCLIInputEvent(input,
					userInput));
		}
	}
	
	// this marks if CLI is on autocomplete
	private boolean onAutoComplete = false;
	
	/**
	 * Set an autocomplete string. The right difference between current text and new 
	 * text will be selected
	 * 
	 * @param autoComplete new string to autocomplete
	 */
	public void setAutoComplete(String autoComplete) {
		String cur = input.getText();
		int start = cur.length();
		int m = Math.min(cur.length(), autoComplete.length());
		for (int i=0;i<m;++i) {
			if (cur.charAt(i) != autoComplete.charAt(i)) {
				start = i;
			}
		}
		
		input.setText(autoComplete);
		input.setSelection(start, autoComplete.length());
		this.previousString = autoComplete;
		if (start < autoComplete.length()) {
			onAutoComplete = true;
		}
	}

}
