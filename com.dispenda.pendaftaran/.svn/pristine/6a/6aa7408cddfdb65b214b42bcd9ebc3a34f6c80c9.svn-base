package com.dispenda.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.dispenda.pendaftaran.views.PendaftaranView;

public class PendaftaranPerspective implements IPerspectiveFactory {
	
	public static final String ID = PendaftaranPerspective.class.getName();

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.addStandaloneView(PendaftaranView.ID,  false, IPageLayout.LEFT, 1f, editorArea);
//		layout.addPlaceholder(arg0, arg1, arg2, arg3)
	}
}
