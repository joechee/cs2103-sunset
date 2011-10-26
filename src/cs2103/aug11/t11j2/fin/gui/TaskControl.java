package cs2103.aug11.t11j2.fin.gui;

import java.io.InputStream;
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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.gdip.Rect;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.datamodel.Task;

/**
 * Control for each individual task. It will highlight the 
 * tags, and create the controls for edit, delete, mark as impt, and fin
 *  
 * @author Koh Zi Chun
 */
public class TaskControl extends Composite {
	Task task;
	TaskStyledText taskText;
	StyledText dueBy;
	StyledText taskNumber;
	Button deleteCheckbox;
	Integer taskPosition;
	
	Composite quickActions = null;

	Composite parent = null;
	
	
	private Listener onFinEnter;
	private Listener onFinExit;
	private Listener onFinClick;
	
	private Listener onImptEnter;
	private Listener onImptExit;
	private Listener onImptClick;
	
	private Listener onDeleteEnter;
	private Listener onDeleteExit;
	private Listener onDeleteClick;
	

	private static Canvas createQuickButton(Composite parent, InputStream image, InputStream imageHover) {
	    ImageData delImageData = new ImageData(image);
	    ImageData delImageOverData = new ImageData(imageHover);
	    final Image delImage = new Image(parent.getDisplay(), delImageData);
	    final Image delImageOver = new Image(parent.getDisplay(), delImageOverData);

		final Canvas canvas = new Canvas(parent, SWT.None);
		canvas.setData(false);
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if ((Boolean) canvas.getData() == false) {
					e.gc.drawImage(delImage,-2,-2);
				} else {
					e.gc.drawImage(delImageOver,-2,-2);
				}
			}
	    });
		canvas.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));
		
		canvas.addListener(SWT.MouseEnter, new Listener() {
			@Override
			public void handleEvent(Event e) {
				canvas.setData(true);
				canvas.redraw();
			}
		});
		
		canvas.addListener(SWT.MouseExit, new Listener() {
			@Override
			public void handleEvent(Event e) {
				canvas.setData(false);
				canvas.redraw();
			}
		});
		
		canvas.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));
		
		return canvas;
	}

	
	private static void initDeleteEvent(Canvas deleteCanvas, final TaskControl parent) {
	    deleteCanvas.addListener(SWT.MouseEnter, new Listener() {
	    	@Override
	    	public void handleEvent(Event e) {
	    		if (parent.onDeleteEnter != null) {
	    			parent.onDeleteEnter.handleEvent(e);
	    		}
	    	}
	    });
	    deleteCanvas.addListener(SWT.MouseExit, new Listener() {
	    	@Override
	    	public void handleEvent(Event e) {
	    		if (parent.onDeleteExit != null) {
	    			parent.onDeleteExit.handleEvent(e);
	    		}
	    	}
	    });
	    deleteCanvas.addListener(SWT.MouseUp, new Listener() {
	    	@Override
	    	public void handleEvent(Event e) {
	    		if (parent.onDeleteClick != null) {
	    			parent.onDeleteClick.handleEvent(e);
	    		}
	    	}
	    });
	}
	
	private static void initImptEvent(Canvas imptCanvas, final TaskControl parent) {
	    imptCanvas.addListener(SWT.MouseEnter, new Listener() {
	    	@Override
	    	public void handleEvent(Event e) {
	    		if (parent.onImptEnter != null) {
	    			parent.onImptEnter.handleEvent(e);
	    		}
	    	}
	    });
	    imptCanvas.addListener(SWT.MouseExit, new Listener() {
	    	@Override
	    	public void handleEvent(Event e) {
	    		if (parent.onImptExit != null) {
	    			parent.onImptExit.handleEvent(e);
	    		}
	    	}
	    });
	    imptCanvas.addListener(SWT.MouseUp, new Listener() {
	    	@Override
	    	public void handleEvent(Event e) {
	    		if (parent.onImptClick != null) {
	    			parent.onImptClick.handleEvent(e);
	    		}
	    	}
	    });
	}
	
	private static Composite initQuickActions(final TaskControl parent, Task task) {
		final Composite tr = new Composite(parent, SWT.NONE);
				
	    GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
	    tr.setLayout(gridLayout);
	    
	    
	    Canvas deleteCanvas = createQuickButton(tr, parent.getClass().getResourceAsStream("del.png"), 
	    		parent.getClass().getResourceAsStream("delo.png"));
	    
	    // if current task is important, set the default impt button to the highlighted one
	    Canvas imptCanvas;
	    if (task.isImportant()) {
	    	imptCanvas = createQuickButton(tr, parent.getClass().getResourceAsStream("impto.png"), 
	    			parent.getClass().getResourceAsStream("impt.png"));	    	
	    } else {
	    	imptCanvas = createQuickButton(tr, parent.getClass().getResourceAsStream("impt.png"), 
	    			parent.getClass().getResourceAsStream("impto.png"));
	    }
	    
	    
	    initDeleteEvent(deleteCanvas, parent);
		initImptEvent(imptCanvas, parent);

		tr.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));
		
		return tr;
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
		
		taskNumber.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));
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
		dueBy.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));
		dueBy.setForeground(new Color(null, 255, 255, 255));
		dueBy.setFont(new Font(parent.getDisplay(), FinConstants.DEFAULT_FONT, FinConstants.DEFAULT_FONTSIZE, SWT.NORMAL));

		dueBy.setEnabled(false);
		return dueBy;
	}
	
	
		
	private static Button initFinButton(final TaskControl parent, boolean isFin) {
		final Button button = new Button(parent, SWT.CHECK);
		
		button.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));
		button.setVisible(false);
		button.setSelection(isFin);

		button.addListener(SWT.MouseEnter, new Listener(){
			@Override
			public void handleEvent(Event event) {
				if (parent.onFinEnter != null) {
					parent.onFinEnter.handleEvent(event);
				}
			}
		});
		
		button.addListener(SWT.MouseExit, new Listener(){
			@Override
			public void handleEvent(Event event) {
				if (parent.onFinExit != null) {
					parent.onFinExit.handleEvent(event);
				}
			}			
		});
		
		button.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (parent.onFinClick != null) {
					parent.onFinClick.handleEvent(event);
				}
			}
		});
		
		return button;
	}
	
	private static TaskStyledText initTaskText(final TaskControl parent, Task task) {
		final TaskStyledText taskText = new TaskStyledText(parent, SWT.NONE, task);
		final String originalString = taskText.getText();
		
		taskText.addListener(SWT.FocusIn, new Listener() {
			@Override
			public void handleEvent(Event e) {
				Composite fincli = parent.getParent();
				while(fincli != null && !(fincli instanceof FinCLIComposite)) {
					fincli = fincli.getParent();
				}
				
				String editText = taskText.task.getEditableTaskName();
				if (fincli instanceof FinCLIComposite) {
					((FinCLIComposite)fincli).setHint("edit " + parent.taskPosition + " to " + editText); 
				}
				taskText.editMode(editText);				
			}
		});
		
		final Listener completeEdit = new Listener() {
			@Override
			public void handleEvent(Event e) {
				Composite fincli = parent.getParent();
				
				while(fincli != null && !(fincli instanceof FinCLIComposite)) {
					fincli = fincli.getParent();
				}
				
				if (e.doit && !taskText.isInRenderMode() && taskText.hasChange()) {
					if (fincli instanceof FinCLIComposite) {
						((FinCLIComposite)fincli).runInput("edit " + parent.taskPosition + " to " + taskText.getText());
						((FinCLIComposite)fincli).removeHint();
					}
				} else {
					taskText.renderMode(originalString);
					if (fincli instanceof FinCLIComposite) {
						((FinCLIComposite)fincli).removeHint();
					}
					((FinCLIComposite)fincli).forceFocus();
				}
			}
		};
		
		// when user focus out of control, save the edit
		taskText.addListener(SWT.FocusOut, completeEdit);
		taskText.addListener(SWT.KeyUp, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (e.keyCode == SWT.CR || e.keyCode == 13 || e.keyCode == SWT.KEYPAD_CR) {
					completeEdit.handleEvent(e);
				} else if (e.keyCode == SWT.ESC) {
					e.doit = false;
					completeEdit.handleEvent(e);
				}
			}
		});
		
		taskText.addListener(SWT.KeyDown, new Listener() {
			@Override
			public void handleEvent(Event e) {
				Composite fincli = parent.getParent();
				while(fincli != null && !(fincli instanceof FinCLIComposite)) {
					fincli = fincli.getParent();
				}
				
				if (fincli instanceof FinCLIComposite) {
					((FinCLIComposite)fincli).setHint("edit " + parent.taskPosition + " to " + taskText.getText()); 
				}
			}
		});
		
		taskText.addListener(SWT.MouseEnter, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (taskText.isInRenderMode()) {
					Composite fincli = parent.getParent();
					while(fincli != null && !(fincli instanceof FinCLIComposite)) {
						fincli = fincli.getParent();
					}
					
					if (fincli instanceof FinCLIComposite) {
						((FinCLIComposite)fincli).setHint("edit " + parent.taskPosition + " to " + taskText.getText()); 
					}					
				}
			}			
		});
		
		taskText.addListener(SWT.MouseExit, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (taskText.isInRenderMode()) {
					Composite fincli = parent.getParent();
					while(fincli != null && !(fincli instanceof FinCLIComposite)) {
						fincli = fincli.getParent();
					}
					
					if (fincli instanceof FinCLIComposite) {
						((FinCLIComposite)fincli).removeHint(); 
					}					
				}
			}						
		});
		
		return taskText;
	}
	public boolean mouseOver = false;
	public TaskControl(Composite parent, int style, Task task,
			Integer taskPosition) {

		super(parent, style);
		
		this.task = task;
		this.parent = parent;
		this.taskNumber = initTaskNumber(this, task, taskPosition);
		this.dueBy = initDueBy(this, task);
		this.deleteCheckbox = initFinButton(this, task.isFin());
		this.taskPosition = taskPosition;
		this.quickActions = initQuickActions(this, task);
		this.taskText = initTaskText(this, task);
		
		// dispose
		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				TaskControl.this.dispose();
			}
		});

		// on parent resize
		this.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				TaskControl.this.controlResized(e);
			}
		});
				

		this.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));
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
		Point checkExtent = deleteCheckbox.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
		
		int width = this.parent.getClientArea().width;
		
		deleteCheckbox.setBounds(10, 10, checkExtent.x, checkExtent.y);
		taskNumber.setBounds(checkExtent.x + 20, 0, taskNumberExtent.x, taskNumberExtent.y);
		
		int taskTextLeft = checkExtent.x + 20 + taskNumberExtent.x + 20;
		taskText.setBounds(taskTextLeft, taskNumberExtent.y
				- taskTextExtent.y - 2, width - taskTextLeft - dueByExtent.x, taskTextExtent.y);
		
		if (mouseOver) {
			quickActions.setBounds(width - taskNumberExtent.y * 2, 1, (taskNumberExtent.y-2) * 2, (taskNumberExtent.y-2));
			dueBy.setBounds(0,0,0,0);
			deleteCheckbox.setVisible(true);
		} else {
			quickActions.setBounds(0,0,0,0);
			dueBy.setBounds(width - dueByExtent.x - 5, taskNumberExtent.y
					- dueByExtent.y - 2, dueByExtent.x, dueByExtent.y);
			deleteCheckbox.setVisible(false);
		}
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

	
	/**
	 * Whether the current task is flagged as important
	 * @return true if task is important and false otherwise
	 */
	public boolean isImportant() {
		return task.isImportant();
	}
	/**
	 * Whether the current task is flagged as finished
	 * @return true if task is finished and false otherwise
	 */
	public boolean isFin() {
		return task.isFin();
	}


	public void setOnFinEnter(Listener onFinEnter) {
		this.onFinEnter = onFinEnter;
	}
	public void setOnFinExit(Listener onFinExit) {
		this.onFinExit = onFinExit;
	}
	public void setOnFinClick(Listener onFinClick) {
		this.onFinClick = onFinClick;
	}

	public void setOnDeleteEnter(Listener onDeleteEnter) {
		this.onDeleteEnter = onDeleteEnter;
	}
	public void setOnDeleteExit(Listener onDeleteExit) {
		this.onDeleteExit = onDeleteExit;
	}
	public void setOnDeleteClick(Listener onDeleteClick) {
		this.onDeleteClick = onDeleteClick;
	}

	public void setOnImptEnter(Listener onImptEnter) {
		this.onImptEnter = onImptEnter;
	}
	public void setOnImptExit(Listener onImptExit) {
		this.onImptExit = onImptExit;
	}
	public void setOnImptClick(Listener onImptClick) {
		this.onImptClick = onImptClick;
	}
}
