package com.dispenda.widget;

import java.util.EventObject;

import com.ibm.icu.util.CurrencyAmount;

public class MoneyChangeEvent extends EventObject {
	private static final long serialVersionUID = -7815430026338867768L;

	private final CurrencyAmount oldValue;
	private final CurrencyAmount newValue;

	public MoneyChangeEvent(Object source, CurrencyAmount oldValue, CurrencyAmount newValue) {
		super(source);
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public CurrencyAmount getNewValue() {
		return newValue;
	}

	public CurrencyAmount getOldValue() {
		return oldValue;
	}
}


