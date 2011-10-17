package cs2103.aug11.t11j2.fin.gui.footer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
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
	
	static final String [] OUTPUT_TEXT = {"add", "search", "show", "help"};
	static final String [] LABEL_TEXT = {"Add", "Search", "Show", "Help"};
	static String WITHIN_ONE_DAY = "WITHIN 24 HOURS";
	static int STRING_NOT_FOUND = -1;
	
	public List <Label> labelList = new ArrayList<Label>();	
	static Text input;
	
	/*
	 * @param display, shell: the devices containing the footer.
	 * @param Text: the device receiving message from the footer.
	 * @param x, y: location of the footer.
	 * @param width, height: size of the labels.
	 * @param gap: the gap between the labels.
	 */
	public FinFooter( Display display, Shell shell, Text text,
			int x, int y, int width, int height, int gap){

		input = text;
		Color color = shell.getBackground();
		Color fcolor = new Color(display, 80, 70, 60);
		int n = LABEL_TEXT.length;
		for (int i=0; i<n; i++){
			System.out.println(i + " " + LABEL_TEXT[i]);
			this.labelList.add(new Label(shell, SWT.PUSH));
			formatLabel(this.labelList.get(i), x, y, 
						 width, height, color, fcolor, LABEL_TEXT[i]);
			x = x + width + gap;
			addListener(i);
			addMouseoverEffect(this.labelList.get(i));
		}
		int countOutstanding = getOutstandingTask();
		fcolor = new Color(display, 255, 0, 0);
		addOutstandingNum(shell, x, y, width, height, color, fcolor, countOutstanding);
		x = x + width;
		fcolor = new Color(display, 105, 0, 255);
		addLabelOutstanding(shell,  x, y, width*3, height, 
				color, fcolor, "Tasks "+WITHIN_ONE_DAY);
		x = x + width +gap;
	}
	
	void formatLabel(Label label, int x, int y, int width, int height,
			Color color, Color fcolor, String name){
		
		label.setLocation(x, y);
		label.setSize(width, height);
		label.setBackground(color);
		label.setForeground(fcolor);
		label.setText(name);
	}
	
	void addListener(int index){
		
		switch(index){
		
		case 0:	this.labelList.get(index).addMouseListener(
					new MouseAdapter(){
						public void mouseDown(MouseEvent e){
							input.setFocus();
							input.setText("");
							input.append("add ");
						}						
					});
				break;
		case 1: this.labelList.get(index).addMouseListener(
				new MouseAdapter(){
					public void mouseDown(MouseEvent e){
						input.setFocus();
						input.setText("");
						input.append("search ");
					}						
				});
				break;
		case 2: this.labelList.get(index).addMouseListener(
				new MouseAdapter(){
					public void mouseDown(MouseEvent e){
						input.setFocus();
						input.setText("");
						input.append("show ");
					}						
				});
				break;
		case 3: this.labelList.get(index).addMouseListener(
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
	
	void addMouseoverEffect(Label label){
		final Label targetLabel = label;
		final Color preFcolor = label.getForeground();
		targetLabel.addListener(SWT.MouseEnter,
				new Listener(){
					@Override
					public void handleEvent(Event e){
						Color fcolor = new Color(null, 255, 255, 255);
						targetLabel.setForeground(fcolor);
					}
				});
		targetLabel.addListener(SWT.MouseExit,
				new Listener(){
					@Override
					public void handleEvent(Event e){
						targetLabel.setForeground(preFcolor);
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
