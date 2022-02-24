package com.dispenda.widget;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.model.PendaftaranSuratIzinUsaha;
import com.dispenda.object.Preferences;

public class SuratIzinTableEditorComposite extends Composite {

  private static final Object[] CONTENT = new Object[] {
      new EditableTableItem("-", "-", new Date())};

/*  private static final String[] VALUE_SET = new String[] { "xxx", "yyy",
      "zzz" };*/

  private static final String NAME_PROPERTY = "name";

  private static final String VALUE_PROPERTY = "value";
  
  private static final String DATE_PROPERTY = "date";

  private TableViewer viewer;

private Table table;

  public SuratIzinTableEditorComposite(Composite parent) {
    super(parent, SWT.NULL);
    buildControls();
  }

  private class NewRowAction extends Action {
    private Timestamp dateNow;

	public NewRowAction() {
      super("Tambah Surat Izin");
    }

    public void run() {
    	dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
      EditableTableItem newItem = new EditableTableItem("-",
          "-", new Date());
      viewer.add(newItem);
    }
  }

  public Table getTable(){
	  return table;
  }
  
  protected void buildControls() {
    FillLayout compositeLayout = new FillLayout();
    setLayout(compositeLayout);

    table = new Table(this, SWT.BORDER|SWT.FULL_SELECTION);
    viewer = buildAndLayoutTable(table);

    attachContentProvider(viewer);
    attachLabelProvider(viewer);
    attachCellEditors(viewer, table);

    MenuManager popupMenu = new MenuManager();
    IAction newRowAction = new NewRowAction();
    popupMenu.add(newRowAction);
    Menu menu = popupMenu.createContextMenu(table);
    table.setMenu(menu);

    viewer.setInput(CONTENT);
  }

  private void attachLabelProvider(TableViewer viewer) {
    viewer.setLabelProvider(new ITableLabelProvider() {
      public Image getColumnImage(Object element, int columnIndex) {
        return null;
      }

      @SuppressWarnings("deprecation")
	public String getColumnText(Object element, int columnIndex) {
        switch (columnIndex) {
        case 0:
          return ((EditableTableItem) element).name;
        case 1:
//          Number index = ((EditableTableItem) element).value;
          return ((EditableTableItem) element).value;
        case 2:
        	Date date = ((EditableTableItem) element).date;
        	String strDate = date.getDate() + "-" + (date.getMonth()+1) + "-" + (date.getYear());
        	return strDate;
        default:
          return "Invalid column: " + columnIndex;
        }
      }

      public void addListener(ILabelProviderListener listener) {
      }

      public void dispose() {
      }

      public boolean isLabelProperty(Object element, String property) {
        return false;
      }

      public void removeListener(ILabelProviderListener lpl) {
      }
    });
  }

  private void attachContentProvider(TableViewer viewer) {
    viewer.setContentProvider(new IStructuredContentProvider() {
      public Object[] getElements(Object inputElement) {
        return (Object[]) inputElement;
      }

      public void dispose() {
      }

      public void inputChanged(Viewer viewer, Object oldInput,
          Object newInput) {
      }
    });
  }

  private TableViewer buildAndLayoutTable(final Table table) {
    TableViewer tableViewer = new TableViewer(table);

    table.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
    table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
    table.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
    
    TableLayout layout = new TableLayout();
    layout.addColumnData(new ColumnWeightData(50, 75, true));
    layout.addColumnData(new ColumnWeightData(50, 75, true));
    layout.addColumnData(new ColumnWeightData(50, 75, true));
    table.setLayout(layout);

    TableColumn nameColumn = new TableColumn(table, SWT.CENTER);
    nameColumn.setText("Keterangan");
    TableColumn valColumn = new TableColumn(table, SWT.CENTER);
    valColumn.setText("No. Surat");
    TableColumn dateColumn = new TableColumn(table, SWT.CENTER);
    dateColumn.setText("Tanggal");
    table.setHeaderVisible(true);
    return tableViewer;
  }

  private void attachCellEditors(final TableViewer viewer, Composite parent) {
    viewer.setCellModifier(new ICellModifier() {
      public boolean canModify(Object element, String property) {
        return true;
      }

      public Object getValue(Object element, String property) {
        if (NAME_PROPERTY.equals(property))
          return ((EditableTableItem) element).name;
        else if(VALUE_PROPERTY.equalsIgnoreCase(property))
          return ((EditableTableItem) element).value;
        else
        	return ((EditableTableItem) element).date;
      }

      public void modify(Object element, String property, Object value) {
        TableItem tableItem = (TableItem) element;
        EditableTableItem data = (EditableTableItem) tableItem
            .getData();
        if (NAME_PROPERTY.equals(property))
          data.name = value.toString();
        else if (VALUE_PROPERTY.equals(property))
          data.value = value.toString();
        else
        	data.date = (Date) value;

        viewer.refresh(data);
      }
    });

    viewer.setCellEditors(new CellEditor[] { new TextCellEditor(parent),
        new TextCellEditor(parent), new DateTimeCellEditor(parent) });

    viewer
        .setColumnProperties(new String[] { NAME_PROPERTY,
            VALUE_PROPERTY, DATE_PROPERTY });
  }
  
  @SuppressWarnings("deprecation")
  public void loadSuratIzin(List<PendaftaranSuratIzinUsaha> listIzinUsaha){
		table.removeAll();
		for (int i=0;i<listIzinUsaha.size();i++){
			PendaftaranSuratIzinUsaha izinUsaha = listIzinUsaha.get(i);
				String[] tgl = izinUsaha.getTanggalSurat().split("-");
				Date tglIzin = new Date(Integer.valueOf(tgl[2]), Integer.valueOf(tgl[1])-1, Integer.valueOf(tgl[0]));
				EditableTableItem item = new EditableTableItem(izinUsaha.getSuratIzin(), izinUsaha.getNoSurat(), tglIzin);
				viewer.add(item);
		}
	}

}

class EditableTableItem {
  public String name;

  public String value;
  
  public Date date;

  public EditableTableItem(String n, String v, Date d) {
    name = n;
    value = v;
    date = d;
  }
}
