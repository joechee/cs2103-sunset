package cs2103.aug11.t11j2.fin.gui;

import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

import cs2103.aug11.t11j2.fin.parser.*;
import cs2103.aug11.t11j2.fin.application.FinApplication;
import cs2103.aug11.t11j2.fin.application.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

public class FinFooter {
	
	static final String [] OUTPUT_TEXT = {"add ", "show", "help"};
	static final String [] LABEL_TEXT = {"add", "show", "help"};
	static final String [] HINT_TEXT = {"add", "show", "help"};
	static final String [] HINT_EXPLAINATION_TEXT = {"add a new task", "shows all tasks or search by a filter", "shows help for Fin."};
	static final String WITHIN_ONE_DAY = "WITHIN 24 HOURS";
	static final int STRING_NOT_FOUND = -1;
	static final int DEFAULT_X = 20;
	static final int DEFAULT_DY = 5;
	static final int DEFAULT_WIDTH = 20;
	static final int DEFAULT_HEIGHT = 20;
	static final int OVER_WIDTH = 30;
	static final int OVER_HEIGHT = 30;
	static final int DEFAULT_GAP = 20;
	static final int HELP_INDEX = 2;
	static final Color DEFAULT_HINT_COLOR = new Color(null, 100, 100, 100);
	static final Color DEFAULT_TEXT_COLOR = new Color(null, 0, 0, 0);
		
	public List <Button> buttonList = new ArrayList<Button>();	
	public List <Label> labelList = new ArrayList<Label>();
	static FinCLIComposite finCLIComposite;
	static boolean isHint;
	
	static Image buttonImg[];
	static Image buttonOnImg[];
	
	Listener mouseOverListener = null;
	Listener mouseOutListener = null;
	Listener mouseClickListener = null;
	
	/*
	 * @param shell: the devices containing the footer.
	 * @param Text: the device receiving message from the footer.
	 */	
	public FinFooter(Composite shell, FinCLIComposite text, Listener mouseOverListener, Listener mouseOutListener, Listener mouseClickListener){
		int x = DEFAULT_X;
		int y = shell.getSize().y + DEFAULT_DY;
		int width = DEFAULT_WIDTH;
		int height = DEFAULT_HEIGHT;
		int gap = DEFAULT_GAP;
		
		this.mouseOverListener = mouseOverListener;
		this.mouseOutListener = mouseOutListener;
		this.mouseClickListener = mouseClickListener;
		
		finCLIComposite = text;
		
		initImg();
		int n = LABEL_TEXT.length;
		for (int i=0; i<n; i++){
			Button b = new Button(shell, SWT.PUSH | SWT.NO_FOCUS);
			GridData d = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
			d.exclude = true;
			b.setLayoutData(d);
			this.buttonList.add(b);		
			formatButton(this.buttonList.get(i), x, y, 
						 width, height, buttonImg[i]);
			x = x + width + gap;
			addListener(i);
			addMouseoverEffect(this.buttonList.get(i),i);
		}
	}
	
	void initImg(){
		int n = LABEL_TEXT.length;
		buttonImg = new Image [n];
		buttonOnImg = new Image[n];
		InputStream inputStream;
		
		for (int i=0; i<n; i++){
			inputStream = this.getClass().getResourceAsStream(LABEL_TEXT[i]+".png");
			buttonImg[i] = new Image(null, inputStream);
			
			inputStream = this.getClass().getResourceAsStream(LABEL_TEXT[i]+"o.png");
			buttonOnImg[i] = new Image(null, inputStream);
		}
		
	}

	void formatButton(Button bt, int x, int y, int width, int height,Image img){
		bt.setLocation(x, y);
		bt.setSize(width, height);
		bt.setImage(img);
	}
	
	void formatLabel(Label label, int x, int y, int width, int height,
			Color color, Color fcolor, String labelText){
		
		label.setLocation(x, y);
		label.setSize(width, height);
		label.setBackground(color);
		label.setForeground(fcolor);
		label.setText(labelText);
	}
	
	void addListener(final int index){
		final Listener mouseClickListener = this.mouseClickListener;
		
		this.buttonList.get(index).addListener(SWT.MouseDown,
				new Listener(){
					@Override
					public void handleEvent(Event e) {
						e.text = LABEL_TEXT[index];
						e.index = index;
						
						if (mouseClickListener != null) {
							mouseClickListener.handleEvent(e);
						}
					}						
				});				
	}
	
	void addMouseoverEffect(Button bt, final int index){
		final Button targetBt = bt;
		final Color preFcolor = bt.getBackground();
		
		final Listener mouseOverListener = this.mouseOverListener;
		final Listener mouseOutListener = this.mouseOutListener;
		
		targetBt.addListener(SWT.MouseEnter,
				new Listener(){
					@Override
					public void handleEvent(Event e){
						Rectangle rt = targetBt.getBounds();
						rt.x-=5;
						rt.y-=5;
						rt.height = OVER_HEIGHT;
						rt.width = OVER_WIDTH;
						targetBt.setBounds(rt);
						targetBt.setImage(buttonOnImg[index]);

						
						e.text = LABEL_TEXT[index];
						e.index = index;
						
						if (mouseOverListener != null) {
							mouseOverListener.handleEvent(e);
						}
						
					}
				});
		
		targetBt.addListener(SWT.MouseExit,
				new Listener(){
					@Override
					public void handleEvent(Event e){
						Rectangle rt = targetBt.getBounds();
						rt.x+=5;
						rt.y+=5;
						rt.height = DEFAULT_HEIGHT;
						rt.width = DEFAULT_WIDTH;
						targetBt.setBounds(rt);
						targetBt.setImage(buttonImg[index]);

						e.text = LABEL_TEXT[index];
						e.index = index;
						
						if (mouseOutListener != null) {
							mouseOutListener.handleEvent(e);
						}

					}
			
		});
	}
	
	int getOutstandingTask(){
		List<Task>tasks = FinApplication.INSTANCE.getTasks();
		int numOfTasks = tasks.size();
		int countOutstanding = 0;
		for (int i=0; i<numOfTasks; i++){
			String str = tasks.get(i).toString().toUpperCase();
			if (str.indexOf(WITHIN_ONE_DAY)>STRING_NOT_FOUND)
				countOutstanding++;
		}
		return countOutstanding;
	}
	
	void addOutstandingNum(Shell shell, int x, int y, 
			int width, int height, Color color, Color fcolor, int num){
		String labelText = num+" ";
		Label newLabel = new Label(shell, SWT.PUSH);
		formatLabel(newLabel, x, y, width, height, color, fcolor, labelText);
		newLabel.setAlignment(SWT.RIGHT);
		this.labelList.add(newLabel);
	}
	
	void addLabelOutstanding(Shell shell, int x, int y, 
			int width, int height, Color color, Color fcolor, String labelText){
		Label newLabel = new Label(shell, SWT.PUSH);
		formatLabel(newLabel, x, y, width, height, color, fcolor, labelText);
		newLabel.setAlignment(SWT.LEFT);
		this.labelList.add(newLabel);
	}
	
}
