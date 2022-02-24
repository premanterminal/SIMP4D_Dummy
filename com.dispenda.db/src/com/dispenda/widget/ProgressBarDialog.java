package com.dispenda.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class ProgressBarDialog extends Dialog {
    private Label processMessageLabel; //info of process finish 
    private Label lineLabel;
    private Composite progressBarComposite;
    private CLabel message;
    private ProgressBar progressBar = null;
    private int iStep = 0;
    private static Shell shell;
    protected volatile boolean isClosed = false;//closed state
    
    protected int executeTime = 1;//process times
    protected String processMessage = "process......";//procress info
    protected String shellTitle = "Progress...";
    protected boolean mayCancel = true; //cancel
    protected int processBarStyle = SWT.SMOOTH | SWT.HORIZONTAL; //process bar style

    public void setMayCancel(boolean mayCancel) {
        this.mayCancel = mayCancel;
    }
    
    public void setProcessMessage(String processInfo) {
    	this.processMessage = processInfo;
    }

    public ProgressBarDialog(Shell parent) {
        super(parent);
    }
        
    public void open(int iMax) {
        createContents(); //create window
        shell.open();
        shell.layout();
        this.setProgressStepMax(iMax);
    }
        
    public void close(){    	 
        shell.close();
    }
        
    public void setProgressStepMax(int iMax){
        progressBar.setMaximum(iMax);
    }

    public void setProgressStep(String strMessage){
        try {
			Thread.sleep((long)(Math.random() * 300));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		processMessageLabel.setText(strMessage);
        progressBar.setSelection(iStep+=5);
    }    
     
    public void setProgressEnd(int iTimer){
        int i = iStep;
        while(progressBar.getSelection() != progressBar.getMaximum()){
        	try {
					Thread.sleep((long)(Math.random() * iTimer));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
        	progressBar.setSelection(i++);
        }
        shell.close();
    }

    public void createContents() {
        shell = new Shell(getParent(), SWT.TITLE | SWT.PRIMARY_MODAL);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 10;
     
        shell.setLayout(gridLayout);
        shell.setSize(586, 159);
        shell.setText(shellTitle);
        shell.setLocation(centerScreen(getParent(), shell
    				.getBounds().width, shell.getBounds().height));

        final Composite composite = new Composite(shell, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        composite.setLayout(new GridLayout());

        message = new CLabel(composite, SWT.NONE);
//            message.setImage(processImage);
        message.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        message.setText(processMessage);

        progressBarComposite = new Composite(shell, SWT.NONE);
        progressBarComposite.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        progressBarComposite.setLayout(new FillLayout());

        progressBar = new ProgressBar(progressBarComposite, processBarStyle);
        progressBar.setMaximum(executeTime);

        processMessageLabel = new Label(shell, SWT.NONE);
        processMessageLabel.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        lineLabel = new Label(shell, SWT.HORIZONTAL | SWT.SEPARATOR);
        lineLabel.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            
    }

    private Point centerScreen(Composite parent, int compW, int compH) {
    	int tempH;
    	int tempW;

    	tempW = (parent.getDisplay().getBounds().width - compW) / 2;
    	tempH = (parent.getDisplay().getBounds().height - compH) / 2;

    	return new Point(tempW, tempH);
    }

    protected void cleanUp(){
          
    }
        

    protected void doBefore(){
          
    }
        
    protected void doAfter(){
          
    }
    
    public void setShellTitle(String shellTitle) {
    	this.shellTitle = shellTitle;
    }

    public void setProcessBarStyle(boolean pStyle) {
        if(pStyle)
        	this.processBarStyle = SWT.SMOOTH;
        else
        	this.processBarStyle = SWT.NONE;
    }
 }