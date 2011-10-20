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

import FinFooter_tial.FinFooter;



public class play_shell {

	/**
	 * @param args
	 */
	public static Text text;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(800,500);
		shell.setBackground(new Color(display,0,0,0));
		shell.open();
		text = new Text(shell, SWT.BORDER | SWT.H_SCROLL);
		text.setBounds(0, 350, 800, 50);
		FinFooter footButtons = new FinFooter(display, shell, text, 20, 420, 50, 30, 10);
		
		int n = footButtons.buttonList.size();
		
		while (!shell.isDisposed()){
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

}
