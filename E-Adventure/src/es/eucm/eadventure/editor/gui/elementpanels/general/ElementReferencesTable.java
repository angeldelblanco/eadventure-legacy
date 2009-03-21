package es.eucm.eadventure.editor.gui.elementpanels.general;

import java.awt.Component;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.scene.ElementContainer;
import es.eucm.eadventure.editor.control.controllers.scene.ReferencesListDataControl;
import es.eucm.eadventure.editor.gui.elementpanels.book.IconTextPanel;
import es.eucm.eadventure.editor.gui.otherpanels.ElementReferenceSelectionListener;
import es.eucm.eadventure.editor.gui.otherpanels.ScenePreviewEditionPanel;

public class ElementReferencesTable extends JTable implements ElementReferenceSelectionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ReferencesListDataControl dataControl;
	
	private ScenePreviewEditionPanel spep;
	
	public ElementReferencesTable (ReferencesListDataControl dControl, ScenePreviewEditionPanel spep){
		super();
		this.spep = spep;
		
		this.setModel( new ElementsTableModel() );
		this.getColumnModel( ).setColumnSelectionAllowed( false );
		this.setDragEnabled( false );
		this.getColumnModel().getColumn(0).setMaxWidth( 55 );
		this.getColumnModel().getColumn(0).setPreferredWidth(50);
		
		TableColumn tc = this.getColumnModel().getColumn(1); 
		tc.setMaxWidth(30);
		tc.setCellEditor(this.getDefaultEditor(Boolean.class));
		tc.setCellRenderer(this.getDefaultRenderer(Boolean.class));
		

		this.getModel().addTableModelListener(new TableModelListener(){ 
			public void tableChanged(TableModelEvent tme) { 
				if (tme.getType() == TableModelEvent.UPDATE) { 
					int row = tme.getFirstRow(); 
					int col = tme.getColumn(); 
					List<ElementContainer> references = dataControl.getAllReferencesDataControl();
					if (col == 1){ 
						references.get(row).setVisible(((Boolean) ElementReferencesTable.this.getModel().getValueAt(row, col)).booleanValue());
						ElementReferencesTable.this.spep.repaint();
					} 
				} 
			} 
		}); 

		
		this.getColumnModel().getColumn(2).setCellRenderer(
				new ElementsReferencesTableCellRenderer());
		this.getSelectionModel( ).setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		this.dataControl = dControl;
		this.setSize(200, 150);
	}
	
	
	private class ElementsTableModel extends AbstractTableModel{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public int getColumnCount( ) {
			return 3;
		}

		public int getRowCount( ) {
			return dataControl.getAllReferencesDataControl().size();
		}

		public Object getValueAt( int rowIndex, int columnIndex ) {
			if (columnIndex == 0)
				return Integer.toString( rowIndex );
			List<ElementContainer> references = dataControl.getAllReferencesDataControl();
			if (columnIndex ==2 )
				return references.get( rowIndex );
			if (columnIndex == 1) {
				return new Boolean(references.get( rowIndex ).isVisible());
			}
			return null;
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex==2)
				return TextConstants.getText( "ElementList.Title" );
			if (columnIndex==0)
				return TextConstants.getText( "ElementList.Layer" );
			return "";
		}
		
		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			if (columnIndex == 1) {
				Boolean bvalue = (Boolean) value;
				List<ElementContainer> references = dataControl.getAllReferencesDataControl();
				references.get(rowIndex).setVisible(bvalue.booleanValue());
			}
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			return column == 1;
		}
	}
	
	private class ElementsReferencesTableCellRenderer extends DefaultTableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
		 *      java.lang.Object, boolean, boolean, int, int)
		 */
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			if (value instanceof ElementContainer){
				
				String elementName;
				if (((ElementContainer)value).isPlayer()){
					elementName = "player";
					return new IconTextPanel("img/icons/npc.png", elementName,isSelected);
				}
				else {
					elementName = ((ElementContainer)value).getErdc().getElementId();
					if (((ElementContainer)value).getErdc().getType( ) == Controller.ITEM_REFERENCE){
						return new IconTextPanel("img/icons/item.png", elementName, isSelected);				
					} else if (((ElementContainer)value).getErdc().getType( )  == Controller.ATREZZO_REFERENCE){
						return new IconTextPanel("img/icons/Atrezzo-1.png", elementName,isSelected);
					} else if (((ElementContainer)value).getErdc().getType( )  == Controller.NPC_REFERENCE){
						return new IconTextPanel("img/icons/npc.png", elementName,isSelected);
					} else 
					return null;
				}
			} else {
				return new JLabel(value.toString( ));
			}
		}

	}


	public void elementReferenceSelected(int layer) {
		if (layer != -1)
			this.changeSelection(layer, 1, false, false);
		else
			this.clearSelection();
	}

}