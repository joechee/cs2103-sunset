package cs2103.aug11.t11j2.fin.gui;

import java.util.zip.CRC32;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.application.Task;

/**
 * A StyledText component that renders a task appropriately
 * It will highlight hashTags with colors
 * 
 * @author Koh Zi Chun
 *
 */
public class TaskStyledText extends StyledText {
	
	Task task;
	
	public TaskStyledText(Composite parent, int style, Task task) {
		super(parent, SWT.SINGLE);
		
		this.pack();
		this.setFont(new Font(parent.getDisplay(), FinConstants.DEFAULT_FONT, FinConstants.DEFAULT_FONTSIZE, SWT.NONE));
		this.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));
		this.setForeground(new Color(null, FinConstants.FOREGROUND_COLOR));
		this.setWordWrap(false);
		
		this.task = task;
		
		renderMode(task.getTaskName());
	}
	
	private boolean inRenderMode = false; 
	
	/**
	 * Set control to render mode, which is readonly
	 * 
	 * @param text of the current control
	 */
	public void renderMode(String text) {
		setInRenderMode(true);
		this.setEditable(false);
		this.setText(text);
		parseAndStyleTaskText(this);
	}

	
	private String beforeEdit = "";
	/** 
	 * Set control to editable 
	 * 
	 * @param text
	 */
	public void editMode(String text) {
		setInRenderMode(false);
		this.setText(text);
		this.setEditable(true);
		beforeEdit = this.getText();
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

	public boolean isInRenderMode() {
		return inRenderMode;
	}

	void setInRenderMode(boolean inRenderMode) {
		this.inRenderMode = inRenderMode;
	}

	/**
	 * @return Returns whether the task has been modified after being in editMode
	 */
	public boolean hasChange() {
		// TODO Auto-generated method stub
		return !beforeEdit.equals(this.getText());
	}
}
