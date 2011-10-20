package cs2103.aug11.t11j2.fin.gui.footer;

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
import cs2103.aug11.t11j2.fin.datamodel.Task;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

public class FinFooter {
	
	static final String [] OUTPUT_TEXT = {"add", "del", "search", "help"};
	static final String [] LABEL_TEXT = {"Add", "Delete", "Search", "Help"};
	static final String WITHIN_ONE_DAY = "WITHIN 24 HOURS";
	static final int STRING_NOT_FOUND = -1;
	static final int DEFAULT_X = 20;
	static final int DEFAULT_DY = -100;
	static final int DEFAULT_WIDTH = 50;
	static final int DEFAULT_HEIGHT = 30;
	static final int DEFAULT_GAP = 10;
		
	public List <Button> buttonList = new ArrayList<Button>();	
	public List <Label> labelList = new ArrayList<Label>();
	static Text input;
	
	/*
	 * @param display, shell: the devices containing the footer.
	 * @param Text: the device receiving message from the footer.
	 * @param x, y: location of the footer.
	 * @param width, height: size of the labels.
	 * @param gap: the gap between the labels.
	 */	
	public FinFooter(Display display, Shell shell, Text text,
			int x, int y, int width, int height, int gap){

		input = text;
		Color color = shell.getBackground();
		Color fcolor = new Color(display, 0, 0, 0);
		int n = LABEL_TEXT.length;
		for (int i=0; i<n; i++){
			this.buttonList.add(new Button(shell, SWT.PUSH));
			InputStream inputStream;
			inputStream = this.getClass().getResourceAsStream(LABEL_TEXT[i]+".bmp");
			Image img = new Image(display, inputStream);
		
			formatButton(this.buttonList.get(i), x, y, 
						 width, height, color, fcolor, img);
			x = x + width + gap;
			addListener(i);
			addMouseoverEffect(this.buttonList.get(i));
		}
		int countOutstanding = getOutstandingTask();
		fcolor = new Color(display, 255, 0, 0);
		addOutstandingNum(shell, x, y, width, height, color, fcolor, countOutstanding);
		x = x + width + 10;
		fcolor = new Color(display, 105, 0, 255);
		addLabelOutstanding(shell,  x, y, width*4, height, 
				color, fcolor, "Tasks "+WITHIN_ONE_DAY);
		x = x + width +gap;
	}

	public FinFooter(Display display, Shell shell, Text text){
		int x = DEFAULT_X;
		int y = shell.getSize().y + DEFAULT_DY;
		int width = DEFAULT_WIDTH;
		int height = DEFAULT_HEIGHT;
		int gap = DEFAULT_GAP;
		input = text;
		Color color = shell.getBackground();
		Color fcolor = new Color(display, 0, 0, 0);
		int n = LABEL_TEXT.length;
		for (int i=0; i<n; i++){
			this.buttonList.add(new Button(shell, SWT.PUSH));
			InputStream inputStream;
			inputStream = this.getClass().getResourceAsStream(LABEL_TEXT[i]+".bmp");
			Image img = new Image(display, inputStream);
		
			formatButton(this.buttonList.get(i), x, y, 
						 width, height, color, fcolor, img);
			x = x + width + gap;
			addListener(i);
			addMouseoverEffect(this.buttonList.get(i));
		}
		int countOutstanding = getOutstandingTask();
		fcolor = new Color(display, 255, 0, 0);
		addOutstandingNum(shell, x, y, width, height, color, fcolor, countOutstanding);
		x = x + width + 10;
		fcolor = new Color(display, 105, 0, 255);
		addLabelOutstanding(shell,  x, y, width*4, height, 
				color, fcolor, "Tasks "+WITHIN_ONE_DAY);
		x = x + width +gap;
	}
	
	void formatButton(Button bt, int x, int y, int width, int height,
			Color color, Color fcolor, Image img){
		
		bt.setLocation(x, y);
		bt.setSize(width, height);
		bt.setBackground(color);
		bt.setForeground(fcolor);
		bt.setImage(img);
	}
	
	void formatLabel(Label label, int x, int y, int width, int height,
			Color color, Color fcolor, String labelText){
		
		label.setLocation(x-5, y+5);
		label.setSize(width+10, height+10);
		label.setBackground(color);
		label.setForeground(fcolor);
		label.setText(labelText);
	}
	
	void addListener(int index){
		
		switch(index){
		
		case 0:	this.buttonList.get(index).addMouseListener(
					new MouseAdapter(){
						public void mouseDown(MouseEvent e){
							input.setFocus();
							input.setText("");
							input.append("add ");
						}						
					});
				break;
		case 1: this.buttonList.get(index).addMouseListener(
				new MouseAdapter(){
					public void mouseDown(MouseEvent e){
						input.setFocus();
						input.setText("");
						input.append("del ");
					}						
				});
				break;
		case 2: this.buttonList.get(index).addMouseListener(
				new MouseAdapter(){
					public void mouseDown(MouseEvent e){
						input.setFocus();
						input.setText("");
						input.append("search ");
					}						
				});
				break;
		case 3: this.buttonList.get(index).addMouseListener(
				new MouseAdapter(){
					public void mouseDown(MouseEvent e){
						input.setFocus();
						input.setText("");
						input.append("help\n");
					}						
				});
				break;		
		}			
	}
	
	void addMouseoverEffect(Button bt){
		final Button targetBt = bt;
		final Color preFcolor = bt.getBackground();
		targetBt.addListener(SWT.MouseEnter,
				new Listener(){
					@Override
					public void handleEvent(Event e){
						Rectangle rt = targetBt.getBounds();
						rt.x-=5;
						rt.y-=5;
						rt.height+=10;
						rt.width+=10;
						targetBt.setBounds(rt);
						
					}
				});
		targetBt.addListener(SWT.MouseExit,
				new Listener(){
					@Override
					public void handleEvent(Event e){
						Rectangle rt = targetBt.getBounds();
						rt.x+=5;
						rt.y+=5;
						rt.height-=10;
						rt.width-=10;
						targetBt.setBounds(rt);
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