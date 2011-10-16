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

public class FinFooter {
	
	public static final String [] labelText = {"add", "search", "show", "help"};
	
	public List <Label> labelList = new ArrayList<Label>();
	
	public static Text input;
	
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
		int n = labelText.length;
		for (int i=0; i<n; i++){
			System.out.println(i + " " + labelText[i]);
			this.labelList.add(new Label(shell, SWT.PUSH));
			formatlabel(this.labelList.get(i), x, y, 
						 width, height, color, fcolor, labelText[i]);
			x = x + width + gap;
			addListener(i);
		}
	}
	
	public void formatlabel(Label label, int x, int y, int width, int height,
			Color color, Color fcolor, String name){
		
		label.setLocation(x, y);
		label.setSize(width, height);
		label.setBackground(color);
		label.setForeground(fcolor);
		label.setText(name);
	}
	
	public void addListener(int index){
		
		switch(index){
		
		case 0:	this.labelList.get(index).addMouseListener(
					new MouseAdapter(){
						public void mouseDown(MouseEvent e){
							input.setFocus();
							input.setText("");
							input.append("add");
						}						
					});
				break;
		case 1: this.labelList.get(index).addMouseListener(
				new MouseAdapter(){
					public void mouseDown(MouseEvent e){
						input.setFocus();
						input.setText("");
						input.append("search");
					}						
				});
				break;
		case 2: this.labelList.get(index).addMouseListener(
				new MouseAdapter(){
					public void mouseDown(MouseEvent e){
						input.setFocus();
						input.setText("");
						input.append("show");
					}						
				});
				break;
		case 3: this.labelList.get(index).addMouseListener(
				new MouseAdapter(){
					public void mouseDown(MouseEvent e){
						input.setFocus();
						input.setText("");
						input.append("help"+(char)13);
					}						
				});
				break;
		
		}
			
	}
}
