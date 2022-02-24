package com.dispenda.object;

import com.dispenda.model.UserModul;

public enum GlobalVariable {
	INSTANCE;
	public String[] item = new String[6];
//	public String[] arsip = new String[6];
	public static final UserModul userModul = new UserModul();
}
