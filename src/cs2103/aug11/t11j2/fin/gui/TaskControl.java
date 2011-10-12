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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.datamodel.Task;

public class TaskControl extends Composite {
	Task task;
	StyledText taskText;
	StyledText dueBy;
	StyledText taskNumber;

	Composite parent = null;

	private static StyledText initTaskNumber(Composite parent, Task task,
			Integer taskPosition) {
		StyledText taskNumber = new StyledText(parent, SWT.READ_ONLY);
		taskNumber.setText(taskPosition.toString());

		if (task.isImportant()) {

			String taskText = taskPosition.toString() + "!";
			taskNumber.setText(taskText);

			StyleRange redImpt = new StyleRange();
			redImpt.foreground = new Color(null, 255, 0, 0);
			redImpt.start = taskText.length() - 1;
			redImpt.length = 1;

			taskNumber.setStyleRange(redImpt);
		}
		if (task.isFin()) {
			StyleRange taskComplete = new StyleRange();
			taskComplete.strikeout = true;
			taskComplete.strikeoutColor = new Color(null, 255, 0, 0);
			taskComplete.start = 0;
			taskComplete.length = taskPosition.toString().length();

			taskNumber.setStyleRange(taskComplete);
		}
		taskNumber.setBackground(new Color(null, 0, 0, 0));
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
		dueBy.setBackground(new Color(null, 0, 0, 0));
		dueBy.setForeground(new Color(null, 255, 255, 255));
		dueBy.setFont(new Font(parent.getDisplay(), FinConstants.DEFAULT_FONT, FinConstants.DEFAULT_FONTSIZE, SWT.NORMAL));

		dueBy.setEnabled(false);
		return dueBy;
	}

	public TaskControl(Composite parent, int style, Task task,
			Integer taskPosition) {

		super(parent, style);
		this.task = task;

		this.parent = parent;

		this.taskNumber = initTaskNumber(this, task, taskPosition);
		this.dueBy = initDueBy(this, task);

		taskText = new StyledText(this, SWT.READ_ONLY);
		taskText.setText(task.toString());
		taskText.pack();
		taskText.setFont(new Font(parent.getDisplay(), FinConstants.DEFAULT_FONT, FinConstants.DEFAULT_FONTSIZE, SWT.NONE));
		taskText.setBackground(new Color(null, 0, 0, 0));
		taskText.setForeground(new Color(null, 255, 255, 255));
		taskText.setEnabled(false);

		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				TaskControl.this.dispose();
			}
		});

		addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				TaskControl.this.controlResized(e);
			}
		});

		this.setBackground(new Color(null, 0, 0, 0));

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

		int width = this.parent.getClientArea().width;
		
		taskNumber.setBounds(10, 0, taskNumberExtent.x, taskNumberExtent.y);

		taskText.setBounds(50, taskNumberExtent.y
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
