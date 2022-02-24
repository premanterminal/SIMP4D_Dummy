package com.dispenda.sspd.perspectives;

import org.eclipse.swt.SWT;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.dispenda.object.Preferences;
import com.dispenda.sspd.views.AntriView;
import com.dispenda.sspd.views.SSPDView;

public class PembayaranPerspective implements IPerspectiveFactory {
	
	public static final String ID = PembayaranPerspective.class.getName();

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.addStandaloneView("com.dispenda.views.MainView",  false, IPageLayout.TOP, 0.08f, editorArea);
		layout.getViewLayout("com.dispenda.views.MainView").setCloseable(false);
		
		if(Preferences.ORIENTATION == SWT.HORIZONTAL){
			layout.addStandaloneView("com.dispenda.views.ToolbarView",false, IPageLayout.TOP, 0.13f, editorArea);
			IFolderLayout folder = layout.createFolder("messages", IPageLayout.LEFT, 0.6f, editorArea);
			folder.addPlaceholder(AntriView.ID + ":*");
			folder.addView(AntriView.ID);
			layout.addView(SSPDView.ID, IPageLayout.RIGHT, 0.40f, editorArea);
		}else{
			layout.addStandaloneView("com.dispenda.views.ToolbarView",false, IPageLayout.LEFT, 0.07f, editorArea);
			IFolderLayout folder = layout.createFolder("messages", IPageLayout.RIGHT, 0.6f, editorArea);
			folder.addPlaceholder(AntriView.ID + ":*");
			folder.addView(SSPDView.ID);
			layout.addView(AntriView.ID, IPageLayout.RIGHT, 0.40f, editorArea);
		}
		
		
		
	}
	
	
}
