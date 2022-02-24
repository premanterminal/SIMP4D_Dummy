package com.dispenda.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ModifyListenerUsing {
  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setLayout(new RowLayout());

    Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);
    text.setText("Type somthing to modify.");
    text.addModifyListener(new ModifyListener(){

      public void modifyText(ModifyEvent arg0) {
        System.out.println("Text modified");
        
      }});
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
  }
}
