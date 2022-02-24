package com.dispenda.perspectives;

import org.eclipse.swt.SWT;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.dispenda.object.Preferences;
import com.dispenda.views.MainView;
import com.dispenda.views.ToolbarView;

public class MainPerspective implements IPerspectiveFactory {
	
	public static final String ID = MainPerspective.class.getName();

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.addStandaloneView(MainView.ID,  false, IPageLayout.TOP, 0.08f, editorArea);
		layout.getViewLayout(MainView.ID).setCloseable(false); 
		
		if(Preferences.ORIENTATION == SWT.HORIZONTAL){
			layout.addStandaloneView(ToolbarView.ID,false, IPageLayout.TOP, 0.13f, editorArea);
			IFolderLayout folder = layout.createFolder("messages", IPageLayout.BOTTOM, 0.75f, editorArea);
			folder.addPlaceholder(MainView.ID + ":*");
		}else{
			layout.addStandaloneView(ToolbarView.ID,false, IPageLayout.LEFT, 0.07f, editorArea);
			IFolderLayout folder = layout.createFolder("messages", IPageLayout.RIGHT, 0.75f, editorArea);
			folder.addPlaceholder(MainView.ID + ":*");
		}
	}
}
