package cs2103.aug11.t11j2.fin.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.datamodel.Task;

public class TaskControl extends Composite {
	Task task;
	TaskStyledText taskText;
	StyledText dueBy;
	StyledText taskNumber;
	Button deleteCheckbox;
	Integer taskPosition;

	Composite parent = null;
    
	private static StyledText initTaskNumber(Composite parent, Task task,
			Integer taskPosition) {
		
		StyledText taskNumber = new StyledText(parent, SWT.READ_ONLY);
		taskNumber.setText(taskPosition.toString());

		
		// if task is important, give it a red !
		if (task.isImportant()) {
			String taskText = taskPosition.toString() + "!";
			taskNumber.setText(taskText);

			StyleRange redImpt = new StyleRange();
			redImpt.foreground = new Color(null, 255, 0, 0);
			redImpt.start = taskText.length() - 1;
			redImpt.length = 1;

			taskNumber.setStyleRange(redImpt);
		}
		
		// if task is completed, give it a strikethrough
		if (task.isFin()) {
			StyleRange taskComplete = new StyleRange();
			taskComplete.strikeout = true;
			taskComplete.strikeoutColor = new Color(null, 255, 0, 0);
			taskComplete.start = 0;
			taskComplete.length = taskPosition.toString().length();

			taskNumber.setStyleRange(taskComplete);
		}
		
		taskNumber.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));
		taskNumber.setForeground(new Color(null, 255, 255, 255));
		taskNumber.setFont(new Font(parent.getDisplay(), "Segoe UI", 18,
				SWT.BOLD));
		taskNumber.setEnabled(false);

		return taskNumber;
	}

	private static StyledText initDueBy(Composite parent, Task task) {
		StyledText dueBy = new StyledText(parent, SWT.READ_ONLY);

		Date due = task.getDueDate();
		if (due != null) {
			DateFormat df = new SimpleDateFormat("dd MMM");
			dueBy.setText(df.format(due));
		}
		dueBy.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));
		dueBy.setForeground(new Color(null, 255, 255, 255));
		dueBy.setFont(new Font(parent.getDisplay(), FinConstants.DEFAULT_FONT, FinConstants.DEFAULT_FONTSIZE, SWT.NORMAL));

		dueBy.setEnabled(false);
		return dueBy;
	}
	
	private static Button initDeleteButton(final Composite parent, boolean isFin) {
		final Button button = new Button(parent, SWT.CHECK);
		
		button.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));
		button.setVisible(false);
		button.setSelection(isFin);

		button.addListener(SWT.MouseEnter, new Listener(){
			@Override
			public void handleEvent(Event event) {
				Composite fincli = parent.getParent();
				while(fincli != null && !(fincli instanceof FinCLIComposite)) {
					fincli = fincli.getParent();
				}
				
				if (button.getSelection() == false) {
					if (fincli instanceof FinCLIComposite) {
						((FinCLIComposite)fincli).setHint("fin " + ((TaskControl)parent).taskPosition);
					}
				} else {
					if (fincli instanceof FinCLIComposite) {
						((FinCLIComposite)fincli).setHint("unfin " + ((TaskControl)parent).taskPosition);
					}
				}
			}
		});
		
		button.addListener(SWT.MouseExit, new Listener(){
			@Override
			public void handleEvent(Event event) {
				Composite fincli = parent.getParent();
				while(fincli != null && !(fincli instanceof FinCLIComposite)) {
					fincli = fincli.getParent();
				}
				if (fincli instanceof FinCLIComposite) {
					((FinCLIComposite)fincli).removeHint();
				}
			}			
		});
		
		button.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Composite fincli = parent.getParent();
				while(fincli != null && !(fincli instanceof FinCLIComposite)) {
					fincli = fincli.getParent();
				}
				if (fincli instanceof FinCLIComposite) {
					((FinCLIComposite)fincli).removeHint();
				
					if (button.getSelection() == false) {
						((FinCLIComposite)fincli).runInput("unfin " + ((TaskControl)parent).taskPosition);
					} else {
						((FinCLIComposite)fincli).runInput("fin " + ((TaskControl)parent).taskPosition);
					}
				}
			}
		});
		
		return button;
	}

	public TaskControl(Composite parent, int style, Task task,
			Integer taskPosition) {

		super(parent, style);
		
		this.task = task;
		this.parent = parent;
		this.taskNumber = initTaskNumber(this, task, taskPosition);
		this.dueBy = initDueBy(this, task);
		this.taskText = new TaskStyledText(this, SWT.NONE, task);
		this.deleteCheckbox = initDeleteButton(this, task.isFin());
		this.taskPosition = taskPosition;
		
		// dispose
		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				TaskControl.this.dispose();
			}
		});

		// on parent resize
		this.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				TaskControl.this.controlResized(e);
			}
		});
		
		final Composite parentClass = this;
		
		this.addListener(SWT.MouseExit, new Listener(){
			@Override
			public void handleEvent(Event event) {
				Rectangle rect = parentClass.getClientArea();
				if (rect.contains(event.x, event.y)) {
					return;
				}
				deleteCheckbox.setVisible(false);				
			}
			
		});
		this.addListener(SWT.MouseEnter, new Listener(){
			@Override
			public void handleEvent(Event event) {
				deleteCheckbox.setVisible(true);				
			}
		});
		

		this.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));
		resize();
	}

	void controlResized(ControlEvent e) {
		resize();
	}

	void resize() {
		Point taskTextExtent = taskText.computeSize(SWT.DEFAULT, SWT.DEFAULT,
				false);
		Point taskNumberExtent = taskNumber.computeSize(SWT.DEFAULT,
				SWT.DEFAULT, false);
		Point dueByExtent = dueBy.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
		Point checkExtent = deleteCheckbox.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
		
		int width = this.parent.getClientArea().width;
		
		deleteCheckbox.setBounds(10, 10, checkExtent.x, checkExtent.y);
		taskNumber.setBounds(checkExtent.x + 20, 0, taskNumberExtent.x, taskNumberExtent.y);
		taskText.setBounds(checkExtent.x + 20 + taskNumberExtent.x + 20, taskNumberExtent.y
				- taskTextExtent.y - 2, taskTextExtent.x, taskTextExtent.y);
		dueBy.setBounds(width - dueByExtent.x - 5, taskNumberExtent.y
				- dueByExtent.y - 2, dueByExtent.x, dueByExtent.y);
	}

	public Point computeSize(int wHint, int hHint, boolean changed) {
		Point taskTextExtent = taskText.computeSize(SWT.DEFAULT, SWT.DEFAULT,
				false);
		Point taskNumberExtent = taskNumber.computeSize(SWT.DEFAULT,
				SWT.DEFAULT, false);

		int width = this.parent.getClientArea().width;
		int height = max(taskNumberExtent.y, taskTextExtent.y);

		if (wHint != SWT.DEFAULT) {
			width = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			height = hHint;
		}
		return new Point(width, height);
	}

	private int max(int a, int b) {
		return (a > b ? a : b);
	}
}
