package com.dispenda.widget;

import java.util.Date;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

public class DateTimeCellEditor extends CellEditor {

	private DateTime dateTime;
	
	public DateTimeCellEditor(Composite parent) {
		super(parent);
	}

	@Override
	protected Control createControl(Composite parent) {
		dateTime = new DateTime(parent, SWT.BORDER | SWT.DROP_DOWN);
		return dateTime;
	}

	@Override
	protected Object doGetValue() {
		@SuppressWarnings("deprecation")
		Date date = new Date(dateTime.getYear(),dateTime.getMonth(),dateTime.getDay());
		return date;
	}

	@Override
	protected void doSetFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doSetValue(Object value) {
		

	}

}
