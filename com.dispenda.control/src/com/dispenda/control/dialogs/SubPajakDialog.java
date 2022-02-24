package com.dispenda.control.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.model.BidangUsaha;
import com.dispenda.model.BidangUsahaProvider;
import org.eclipse.swt.widgets.Spinner;

public class SubPajakDialog extends Dialog {
	
	private BidangUsaha subPajak;
	private Integer idPajak;
	private Text txtNamaSubPajak;
	private Integer index;
	private Text txtKodeSubPajak;
	private Spinner spnTarif;
	private Spinner spnBunga;
	private Spinner spnDenda;
	private Spinner spnKenaikan1;
	private Spinner spnKenaikan2;

	/**
	 * @wbp.parser.constructor
	 */
	public SubPajakDialog(Shell parentShell, Integer idPajak) {
		super(parentShell);
		this.idPajak = idPajak;
	}

	public SubPajakDialog(Shell parentShell,BidangUsaha subPajak,Integer index) {
		super(parentShell);
		this.subPajak = subPajak;
		this.index = index;
	}
	
	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(SWT.NONE|SWT.APPLICATION_MODAL);
	}
	
	@Override
	protected Control createButtonBar(Composite parent) {
		return null;
	}
	
	@Override
	protected Control createDialogArea(Composite main) {
		Composite parent = (Composite) super.createDialogArea(main);
		main.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		main.setBackgroundMode(SWT.INHERIT_FORCE);
		
		Composite compo = new Composite(parent, SWT.NONE);
		GridLayout gl_compo = new GridLayout(2, false);
		gl_compo.horizontalSpacing = 30;
		gl_compo.verticalSpacing = 20;
		compo.setLayout(gl_compo);
		compo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Label lblKodeSubPajak = new Label(compo, SWT.NONE);
		lblKodeSubPajak.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblKodeSubPajak.setText("Kode Bidang Usaha");
		
		txtKodeSubPajak = new Text(compo, SWT.BORDER);
		txtKodeSubPajak.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		txtKodeSubPajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNamaSubPajak = new Label(compo, SWT.NONE);
		lblNamaSubPajak.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblNamaSubPajak.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNamaSubPajak.setText("Nama Bidang Usaha");
		
		txtNamaSubPajak = new Text(compo, SWT.BORDER);
		txtNamaSubPajak.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		GridData gd_txtNamaKota = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtNamaKota.widthHint = 230;
		txtNamaSubPajak.setLayoutData(gd_txtNamaKota);
		
		Label lblTarif = new Label(compo, SWT.NONE);
		lblTarif.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblTarif.setText("Tarif");
		
		spnTarif = new Spinner(compo, SWT.BORDER);
		spnTarif.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		
		Label lblBunga = new Label(compo, SWT.NONE);
		lblBunga.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblBunga.setText("Bunga");
		
		spnBunga = new Spinner(compo, SWT.BORDER);
		spnBunga.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		
		Label lblDenda = new Label(compo, SWT.NONE);
		lblDenda.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblDenda.setText("Denda");
		
		spnDenda = new Spinner(compo, SWT.BORDER);
		spnDenda.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		
		Label lblKenaikan = new Label(compo, SWT.NONE);
		lblKenaikan.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblKenaikan.setText("Kenaikan 1");
		
		spnKenaikan1 = new Spinner(compo, SWT.BORDER);
		spnKenaikan1.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		
		Label lblKenaikan_1 = new Label(compo, SWT.NONE);
		lblKenaikan_1.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblKenaikan_1.setText("Kenaikan 2");
		
		spnKenaikan2 = new Spinner(compo, SWT.BORDER);
		spnKenaikan2.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(checkFields()){
					if(subPajak!=null){
						BidangUsahaProvider.INSTANCE.getSelectedBidangUsaha(index).setNamaBidUsaha(txtNamaSubPajak.getText());
						BidangUsahaProvider.INSTANCE.getSelectedBidangUsaha(index).setKodeBidUsaha(txtKodeSubPajak.getText());
						BidangUsahaProvider.INSTANCE.getSelectedBidangUsaha(index).setTarif(spnTarif.getSelection());
						BidangUsahaProvider.INSTANCE.getSelectedBidangUsaha(index).setBunga(spnBunga.getSelection());
						BidangUsahaProvider.INSTANCE.getSelectedBidangUsaha(index).setDenda(spnDenda.getSelection());
						BidangUsahaProvider.INSTANCE.getSelectedBidangUsaha(index).setKenaikan1(spnKenaikan1.getSelection());
						BidangUsahaProvider.INSTANCE.getSelectedBidangUsaha(index).setKenaikan2(spnKenaikan2.getSelection());
						if(ControllerFactory.getMainController().getCpBidangUsahaDAOImpl().saveBidangUsaha(BidangUsahaProvider.INSTANCE.getSelectedBidangUsaha(index)))
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Save Data", "Penyimpanan Data berhasil dilakukan.");
					}else{
						BidangUsaha subPajak = new BidangUsaha(null,txtKodeSubPajak.getText(),txtNamaSubPajak.getText(),idPajak,spnTarif.getSelection(),
								spnBunga.getSelection(),spnDenda.getSelection(),spnKenaikan1.getSelection(),spnKenaikan2.getSelection(),0);
						if(ControllerFactory.getMainController().getCpBidangUsahaDAOImpl().saveBidangUsaha(subPajak)){
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Save Data", "Penyimpanan Data berhasil dilakukan.");
							subPajak.setIdSubPajak(ControllerFactory.getMainController().getCpBidangUsahaDAOImpl().getLastIdBidangUsaha());
							BidangUsahaProvider.INSTANCE.addItem(subPajak);
						}
					}
					getShell().close();
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Save Data", "Ada data yang belum di isi. Silahkan isi data yang kosong dengan benar.");
			}
		});
		GridData gd_btnSave = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnSave.widthHint = 120;
		btnSave.setLayoutData(gd_btnSave);
		btnSave.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		btnSave.setText("SAVE");
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getShell().close();
			}
		});
		btnCancel.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		GridData gd_btnNewButton_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_1.widthHint = 120;
		btnCancel.setLayoutData(gd_btnNewButton_1);
		btnCancel.setText("CANCEL");
		
		if(subPajak!=null){
			txtNamaSubPajak.setText(subPajak.getNamaBidUsaha());
			txtKodeSubPajak.setText(subPajak.getKodeBidUsaha());
			spnBunga.setSelection(subPajak.getBunga());
			spnDenda.setSelection(subPajak.getDenda());
			spnKenaikan1.setSelection(subPajak.getKenaikan1());
			spnKenaikan2.setSelection(subPajak.getKenaikan2());
			spnTarif.setSelection(subPajak.getTarif());
		}
		
		return parent;
	}

	private boolean checkFields(){
		boolean result = false;
		if(txtNamaSubPajak.getText().equalsIgnoreCase("")||txtKodeSubPajak.getText().equalsIgnoreCase(""))
			result = false;
		else
			result = true;
		return result;
	}
}