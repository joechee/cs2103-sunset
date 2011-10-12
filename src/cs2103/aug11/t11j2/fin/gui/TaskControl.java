package cs2103.aug11.t11j2.fin.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.CRC32;

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

    public static float[] HSVtoRGB(float h, float s, float v) {
        float m, n, f;
        int i;

        float[] hsv = new float[3];
        float[] rgb = new float[3];

        hsv[0] = h;
        hsv[1] = s;
        hsv[2] = v;

        if (hsv[0] == -1) {
            rgb[0] = rgb[1] = rgb[2] = hsv[2];
            return rgb;
        }
        
        i = (int) (Math.floor(hsv[0]));
        f = hsv[0] - i;
        if (i % 2 == 0) {
            f = 1 - f; // if i is even
        }
        
        m = hsv[2] * (1 - hsv[1]);
        n = hsv[2] * (1 - hsv[1] * f);
        switch (i) {
            case 6:
            case 0:
                rgb[0] = hsv[2];
                rgb[1] = n;
                rgb[2] = m;
                break;
            case 1:
                rgb[0] = n;
                rgb[1] = hsv[2];
                rgb[2] = m;
                break;
            case 2:
                rgb[0] = m;
                rgb[1] = hsv[2];
                rgb[2] = n;
                break;
            case 3:
                rgb[0] = m;
                rgb[1] = n;
                rgb[2] = hsv[2];
                break;
            case 4:
                rgb[0] = n;
                rgb[1] = m;
                rgb[2] = hsv[2];
                break;
            case 5:
                rgb[0] = hsv[2];
                rgb[1] = m;
                rgb[2] = n;
                break;
        }
        return rgb;
    }
    
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
	
	private static StyledText initTaskText(Composite parent, Task task) {
		StyledText taskText = new StyledText(parent, SWT.READ_ONLY);
		
		taskText.setText(task.toString());
		taskText.pack();
		taskText.setFont(new Font(parent.getDisplay(), FinConstants.DEFAULT_FONT, FinConstants.DEFAULT_FONTSIZE, SWT.NONE));
		taskText.setBackground(new Color(null, 0, 0, 0));
		taskText.setForeground(new Color(null, 255, 255, 255));
		taskText.setEnabled(false);
		
		parseAndStyleTaskText(taskText);
		
		return taskText;
	}

	private static void parseAndStyleTaskText(StyledText taskText) {
		String taskName = taskText.getText() + " ";
		StringBuilder sb = new StringBuilder();


		for (int i=0;i<taskName.length();++i) {
			if (Character.isWhitespace(taskName.charAt(i))) {
				if (sb.length() > 0 && Task.isHashTag(sb.toString())) {
					String tag = Task.sanitizeHashTag(sb.toString());
					if (tag.equals(FinConstants.IMPORTANT_HASH_TAG)) {
						StyleRange redImpt = new StyleRange();
						redImpt.foreground = new Color(null, FinConstants.RED_COLOR);
						redImpt.start = i-sb.length();
						redImpt.length = sb.length();

						taskText.setStyleRange(redImpt);
					} else if (tag.equals(FinConstants.FIN_HASH_TAG)) {
						StyleRange taskComplete = new StyleRange();
						taskComplete.strikeout = true;
						taskComplete.strikeoutColor = new Color(null, FinConstants.RED_COLOR);
						taskComplete.start = i-sb.length();
						taskComplete.length = sb.length();

						taskText.setStyleRange(taskComplete);						
					} else {						
						float[] rgb = generateColor(sb.toString());
						
						StyleRange hashTag = new StyleRange();
						hashTag.foreground = new Color(null, (int)(rgb[0]*255), (int)(rgb[1]*255), (int)(rgb[2]*255));
						hashTag.start = i-sb.length();
						hashTag.length = sb.length();
						
						taskText.setStyleRange(hashTag);
					}
				}
				sb = new StringBuilder();
			} else {
				sb.append(taskName.charAt(i));
			}
		}
	}

	private static float[] generateColor(String string) {
		float h = 0.0f;
		CRC32 crc = new CRC32();
		byte[] b = new byte[string.length()];
		
		for (int i=0;i<string.length();++i) {
			b[i] = (byte) Character.getNumericValue(string.charAt(i));
		}
		crc.update(b);
		//while (h > 6.0f) h -= 6.0f;
		float[] rgb = HSVtoRGB((float)(crc.getValue() % 10000) * 6.0f / 10000.0f , 0.5f, 1.0f);
		return rgb;
	}

	public TaskControl(Composite parent, int style, Task task,
			Integer taskPosition) {

		super(parent, style);
		this.task = task;

		this.parent = parent;

		this.taskNumber = initTaskNumber(this, task, taskPosition);
		this.dueBy = initDueBy(this, task);

		this.taskText = initTaskText(this, task);

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
