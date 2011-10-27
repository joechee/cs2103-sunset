package cs2103.aug11.t11j2.fin.gui;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Scrollable;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.datamodel.Task;

public class TaskContainer extends Composite {
	int taskCount = 0;

	final private static ImageData imptImgData = new ImageData(
			TaskContainer.class.getResourceAsStream("impt.png"));
	
	final private static ImageData imptoImgData = new ImageData(
			TaskContainer.class.getResourceAsStream("impto.png"));
	
	final private static ImageData delImgData = new ImageData(
			TaskContainer.class.getResourceAsStream("del.png"));
	
	final private static ImageData deloImgData = new ImageData(
			TaskContainer.class.getResourceAsStream("delo.png"));
	
	private static Image imptImg = null;
	private static Image imptoImg = null;
	private static Image delImg = null;
	private static Image deloImg = null;
	
	public TaskContainer(Composite parent, int style) {
		super(parent, style);
		
		
		// initialize and cache Image 
		if (imptImg == null) imptImg = new Image(parent.getDisplay(), imptImgData);
		if (imptoImg == null) imptoImg = new Image(parent.getDisplay(), imptoImgData);
		if (delImg == null) delImg = new Image(parent.getDisplay(), delImgData);
		if (deloImg == null) deloImg = new Image(parent.getDisplay(), deloImgData);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		gridLayout.marginTop = gridLayout.marginLeft = gridLayout.marginRight = gridLayout.marginBottom = -5;

		this.setLayout(gridLayout);
		this.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));

		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				false);
		this.setLayoutData(gridData);
		
		final Composite self = this;
		
		this.addListener(SWT.MouseMove, new Listener() {
			@Override
			public void handleEvent(Event event) {
				try {
					for (Control c : self.getChildren() ) {
						Rectangle r = c.getBounds();
						if (r.contains(event.x, event.y)) { 
							if (c instanceof TaskControl) {
								
								if (previousOver != null) {
									previousOver.mouseOver = false;
									previousOver.resize();
									previousOver.setEnabled(false);
								}
							
								((TaskControl) c).mouseOver = true;
								((TaskControl) c).resize();
								((TaskControl) c).setEnabled(true);
								previousOver = ((TaskControl)c);
							}
						}
					}
				} catch (SWTException e) {
					// taskcontrol is disposed
				}

			}
		});
	}
	
	TaskControl previousOver = null;
	
	public void addTask(Task t) {
		final TaskControl taskControl = new TaskControl(this, SWT.NONE, t, ++taskCount, imptImg, imptoImg, delImg, deloImg);
		
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				false);
		taskControl.setLayoutData(gridData);
		taskControl.setEnabled(false);
		
		initTaskEvent(taskControl);
		
		this.layout(true);
	}
	
	private void initTaskEvent(final TaskControl taskControl) {

		// fin events deal with what happens when the user interacts
		// with the button to Fin. an event
		initFinEvent(taskControl);
		
		// delete events deal with what happens when user interacts
		// with the delete button
		initDeleteEvent(taskControl);
		
		// delete events deal with what happens when user interacts
		// with the impt button
		initImptEvent(taskControl);
	}
	
	private void initDeleteEvent(final TaskControl taskControl) {
		// when user mouse over: shows the hint
		taskControl.setOnDeleteEnter(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Composite fincli = taskControl.getParent();
				while(fincli != null && !(fincli instanceof FinCLIComposite)) {
					fincli = fincli.getParent();
				}
				
				if (fincli instanceof FinCLIComposite) {
					((FinCLIComposite)fincli).setHint("delete " + ((TaskControl)taskControl).taskPosition, 
							"Delete the task: " + ((TaskControl)taskControl).task.getTaskName());
				}
			}
		});
		
		taskControl.setOnDeleteExit(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Composite fincli = taskControl.getParent();
				while(fincli != null && !(fincli instanceof FinCLIComposite)) {
					fincli = fincli.getParent();
				}
				if (fincli instanceof FinCLIComposite) {
					((FinCLIComposite)fincli).removeHint();
				}
			}			
		});
		
		taskControl.setOnDeleteClick(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Composite fincli = taskControl.getParent();
				while(fincli != null && !(fincli instanceof FinCLIComposite)) {
					fincli = fincli.getParent();
				}
				if (fincli instanceof FinCLIComposite) {
					((FinCLIComposite)fincli).removeHint();
				
					((FinCLIComposite)fincli).runInput("delete " + ((TaskControl)taskControl).taskPosition);
				}
			}
		});
	}
	
	private void initFinEvent(final TaskControl taskControl) {
		// when user mouse over: shows the hint
		taskControl.setOnFinEnter(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Composite fincli = taskControl.getParent();
				while(fincli != null && !(fincli instanceof FinCLIComposite)) {
					fincli = fincli.getParent();
				}
				
				if (((Button)e.widget).getSelection() == false) {
					if (fincli instanceof FinCLIComposite) {
						((FinCLIComposite)fincli).setHint("fin " + ((TaskControl)taskControl).taskPosition,
								"Fin. task " + taskControl.task.getTaskName() + "!");
					}
				} else {
					if (fincli instanceof FinCLIComposite) {
						((FinCLIComposite)fincli).setHint("unfin " + ((TaskControl)taskControl).taskPosition,
								"UnFin-ish task " + taskControl.task.getTaskName() + " :(");
					}
				}
			}
		});
		
		taskControl.setOnFinExit(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Composite fincli = taskControl.getParent();
				while(fincli != null && !(fincli instanceof FinCLIComposite)) {
					fincli = fincli.getParent();
				}
				if (fincli instanceof FinCLIComposite) {
					((FinCLIComposite)fincli).removeHint();
				}
			}			
		});
		
		taskControl.setOnFinClick(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Composite fincli = taskControl.getParent();
				while(fincli != null && !(fincli instanceof FinCLIComposite)) {
					fincli = fincli.getParent();
				}
				if (fincli instanceof FinCLIComposite) {
					((FinCLIComposite)fincli).removeHint();
				
					if (((Button)e.widget).getSelection() == false) {
						((FinCLIComposite)fincli).runInput("unfin " + ((TaskControl)taskControl).taskPosition);
					} else {
						((FinCLIComposite)fincli).runInput("fin " + ((TaskControl)taskControl).taskPosition);
					}
				}
			}
		});
	}

	private void initImptEvent(final TaskControl taskControl) {
		// when user mouse over: shows the hint
		taskControl.setOnImptEnter(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Composite fincli = taskControl.getParent();
				while(fincli != null && !(fincli instanceof FinCLIComposite)) {
					fincli = fincli.getParent();
				}
				
				if (taskControl.isImportant()) {
					if (fincli instanceof FinCLIComposite) {
						((FinCLIComposite)fincli).setHint("unimpt " + ((TaskControl)taskControl).taskPosition,
								"Mark this task as unimportant");
					}
				} else {
					if (fincli instanceof FinCLIComposite) {
						((FinCLIComposite)fincli).setHint("impt " + ((TaskControl)taskControl).taskPosition,
								"Flag this task as important!");
					}
				}
			}
		});
		
		taskControl.setOnImptExit(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Composite fincli = taskControl.getParent();
				while(fincli != null && !(fincli instanceof FinCLIComposite)) {
					fincli = fincli.getParent();
				}
				if (fincli instanceof FinCLIComposite) {
					((FinCLIComposite)fincli).removeHint();
				}
			}			
		});
		
		taskControl.setOnImptClick(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Composite fincli = taskControl.getParent();
				while(fincli != null && !(fincli instanceof FinCLIComposite)) {
					fincli = fincli.getParent();
				}
				if (fincli instanceof FinCLIComposite) {
					((FinCLIComposite)fincli).removeHint();
				
					if (taskControl.isImportant()) {
						((FinCLIComposite)fincli).runInput("unimpt " + ((TaskControl)taskControl).taskPosition);
					} else {
						((FinCLIComposite)fincli).runInput("impt " + ((TaskControl)taskControl).taskPosition);
					}
				}
			}
		});
	}
}
