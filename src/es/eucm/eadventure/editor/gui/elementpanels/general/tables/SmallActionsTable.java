package es.eucm.eadventure.editor.gui.elementpanels.general.tables;

import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.general.ActionDataControl;
import es.eucm.eadventure.editor.control.controllers.general.ActionsListDataControl;

public class SmallActionsTable extends JTable{
	
   	private static final long serialVersionUID = -777111416961485368L;

	private ActionsListDataControl dataControl;
	
	public SmallActionsTable (ActionsListDataControl dControl){
		super();
		this.dataControl = dControl;
		
		this.setModel( new ActionsTableModel() );
		this.getColumnModel( ).setColumnSelectionAllowed( false );
		this.setDragEnabled( false );
		
		this.getColumnModel().getColumn(0).setCellRenderer(new SmallActionCellRendererEditor());
		this.getColumnModel().getColumn(0).setCellEditor(new SmallActionCellRendererEditor());
		
		this.getSelectionModel( ).setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		this.setRowHeight(20);
		this.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				setRowHeight(20);
				if (getSelectedRow() != -1) {
					if (dataControl.getActions().get(getSelectedRow()).getType() == Controller.ACTION_CUSTOM ||
							dataControl.getActions().get(getSelectedRow()).getType() == Controller.ACTION_CUSTOM_INTERACT)
						setRowHeight(getSelectedRow(), 160);
					else
						setRowHeight(getSelectedRow(), 130);
				}
			}
		});
		
		this.setSize(200, 150);
	}
	
	
	private class ActionsTableModel extends AbstractTableModel{

	    private static final long serialVersionUID = -243535410363608581L;
		
		public int getColumnCount( ) {
			return 1;
		}

		public int getRowCount( ) {
			return dataControl.getActions().size();
		}

		public Object getValueAt( int rowIndex, int columnIndex ) {
		    List<ActionDataControl> actions = dataControl.getActions();
		    if (columnIndex == 0)
		    	return actions.get(rowIndex);
		    return null;
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex==0)
				return TextConstants.getText( "SmallActionList.Action" );
			return "";
		}
		
		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
		    	
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			return row == getSelectedRow();
		}
	}
}
