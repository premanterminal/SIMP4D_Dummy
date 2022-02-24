package com.dispenda.tunggakan.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.dispenda.tunggakan.views.TunggakanWpView;
import com.dispenda.tunggakan.views.TunggakanSptpdView;

public class TunggakanPerspective implements IPerspectiveFactory {
	
	public static final String ID = TunggakanPerspective.class.getName();

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		IFolderLayout folder = layout.createFolder("messages", IPageLayout.TOP, 0.75f, editorArea);
		folder.addPlaceholder(TunggakanWpView.ID + ":*");
		folder.addView(TunggakanWpView.ID);
		folder.addView(TunggakanSptpdView.ID);
//		layout.addView(TunggakanPanelView.ID, IPageLayout.TOP, 0.3f, editorArea);.
//		layout.addView(TunggakanSPTPDView.ID, IPageLayout.LEFT|IPageLayout.BOTTOM, 0.4f, editorArea);
//		layout.addView(TunggakanSKPDKBView.ID, IPageLayout.RIGHT|IPageLayout.BOTTOM, 0.4f, editorArea);

	}
}
