package cs2103.aug11.t11j2.fin.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.datamodel.Task;

public class TaskContainer extends ScrolledComposite {
	Composite display;
	int taskCount = 0;
	public TaskContainer(Composite parent, int style) {
		super(parent, style);
		
		display = new Composite(this, SWT.NONE);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		gridLayout.marginTop = gridLayout.marginLeft = gridLayout.marginRight = gridLayout.marginBottom = -5;

		display.setLayout(gridLayout);
		display.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));

		this.setContent(display);
		this.setExpandVertical(true);
		this.setExpandHorizontal(true);
		this.setBackground(new Color(null, FinConstants.BACKGROUND_COLOR));

		this.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, true));

		this.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				resize();
			}
		});
	}
	
	private void resize() {
		Rectangle r = this.getClientArea();
		this.setMinSize(display.computeSize(r.width, SWT.DEFAULT));
	}
	
	public void addTask(Task t) {
		final TaskControl taskControl = new TaskControl(display, SWT.NONE, t, ++taskCount);
		
		taskControl.addMouseListener(new MouseListener(){
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseUp(MouseEvent e) {
				MessageBox mb = new MessageBox(taskControl.getShell());
				mb.setMessage("Hello");
				mb.setText("World");
				mb.open();
			}
		});
		
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				false);
		taskControl.setLayoutData(gridData);
		
		display.layout(true);
	}

}
