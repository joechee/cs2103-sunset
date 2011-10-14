package FinFooter_tial;

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
	
	public static final String [] buttonText = {"add", "search", "show", "help"};
	
	public List <Button> buttonList = new ArrayList<Button>();
	
	public static Text input;
	
	public FinFooter( Display display, Shell shell, Text text,
			int x, int y, int width, int height, int dis){

		input = text;
		Color color = new Color(display, 220, 220, 0);
		
		int n = buttonText.length;
		for (int i=0; i<n; i++){
			System.out.println(i + " " + buttonText[i]);
			this.buttonList.add(new Button(shell, SWT.PUSH));
			formatButton(this.buttonList.get(i), x, y, 
						 width, height, color, buttonText[i]);
			x = x + width + dis;
			addListener(i);
		}
	}
	
	public void formatButton(Button button, int x, int y, int width, int height,
			Color color, String name){
		
		button.setLocation(x, y);
		button.setSize(width, height);
		button.setBackground(color);
		button.setText(name);
	}
	
	public void addListener(int index){
		
		switch(index){
		
		case 0:	this.buttonList.get(index).addMouseListener(
					new MouseAdapter(){
						public void mouseDown(MouseEvent e){
							input.setFocus();
							input.setText("");
							input.append("add");
						}						
					});
				break;
		case 1: this.buttonList.get(index).addMouseListener(
				new MouseAdapter(){
					public void mouseDown(MouseEvent e){
						input.setFocus();
						input.setText("");
						input.append("search");
					}						
				});
				break;
		case 2: this.buttonList.get(index).addMouseListener(
				new MouseAdapter(){
					public void mouseDown(MouseEvent e){
						input.setFocus();
						input.setText("");
						input.append("show");
					}						
				});
				break;
		case 3: this.buttonList.get(index).addMouseListener(
				new MouseAdapter(){
					public void mouseDown(MouseEvent e){
						input.setFocus();
						input.setText("");
					}						
				});
				break;
		
		}
			
	}
}
