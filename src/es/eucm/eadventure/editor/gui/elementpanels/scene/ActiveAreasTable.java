package es.eucm.eadventure.editor.gui.elementpanels.scene;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.scene.ActiveAreasListDataControl;
import es.eucm.eadventure.editor.control.tools.structurepanel.RenameElementTool;
import es.eucm.eadventure.editor.gui.elementpanels.general.tables.AuxEditCellRendererEditor;
import es.eucm.eadventure.editor.gui.elementpanels.general.tables.ConditionsCellRendererEditor;
import es.eucm.eadventure.editor.gui.elementpanels.general.tables.DocumentationCellRendererEditor;
import es.eucm.eadventure.editor.gui.elementpanels.general.tables.StringCellRendererEditor;
import es.eucm.eadventure.editor.gui.otherpanels.IrregularAreaEditionPanel;
import es.eucm.eadventure.editor.gui.otherpanels.ScenePreviewEditionPanel;

public class ActiveAreasTable extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected ActiveAreasListDataControl dataControl;
	
	protected IrregularAreaEditionPanel iaep;
	
	protected ScenePreviewEditionPanel spep;
	
	public ActiveAreasTable (ActiveAreasListDataControl dControl, IrregularAreaEditionPanel iaep2, JSplitPane previewAuxSplit){
		super();
		this.spep = iaep2.getScenePreviewEditionPanel();
		this.iaep = iaep2;
		this.dataControl = dControl;
		
		this.setModel( new ElementsTableModel() );
		this.getColumnModel( ).setColumnSelectionAllowed( false );
		this.setDragEnabled( false );
		this.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				int col = columnAtPoint(e.getPoint());
				int row = rowAtPoint(e.getPoint());
				TableCellRenderer r =getCellRenderer(row, col);
				//System.out.println("MOUSE TABLE: "+row+" , "+col);
				if (r instanceof ConditionsCellRendererEditor){
					ConditionsCellRendererEditor condRE = (ConditionsCellRendererEditor)r;
				}
			}
			
		});
		
		
		
		this.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (getSelectedRow() >= 0) {
					iaep.setRectangular(dataControl.getActiveAreas().get(getSelectedRow()));
					iaep.repaint();
				}
			}
		});
		
		this.getColumnModel().getColumn(0).setCellEditor(new StringCellRendererEditor());
		this.getColumnModel().getColumn(0).setCellRenderer(new StringCellRendererEditor());
		
		this.getColumnModel().getColumn(1).setCellRenderer(new ConditionsCellRendererEditor());
		this.getColumnModel().getColumn(1).setCellEditor(new ConditionsCellRendererEditor());
		this.getColumnModel().getColumn(1).setMaxWidth(120);
		this.getColumnModel().getColumn(1).setMinWidth(120);
		
		String text = TextConstants.getText("ActiveAreasList.EditActions");
		this.getColumnModel().getColumn(2).setCellRenderer(new AuxEditCellRendererEditor(previewAuxSplit, ActiveAreasListPanel.VERTICAL_SPLIT_POSITION, text));
		this.getColumnModel().getColumn(2).setCellEditor(new AuxEditCellRendererEditor(previewAuxSplit, ActiveAreasListPanel.VERTICAL_SPLIT_POSITION, text));
		this.getColumnModel().getColumn(2).setMaxWidth(105);
		this.getColumnModel().getColumn(2).setMinWidth(105);

		this.getColumnModel().getColumn(3).setCellRenderer(new DocumentationCellRendererEditor());
		this.getColumnModel().getColumn(3).setCellEditor(new DocumentationCellRendererEditor());
		this.getColumnModel().getColumn(3).setMaxWidth(140);
		this.getColumnModel().getColumn(3).setMinWidth(140);
		
		this.getSelectionModel( ).setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		
		this.setRowHeight(22);
		this.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				setRowHeight(22);
				if (getSelectedRow() != -1)
					setRowHeight(getSelectedRow(), 26);
			}
		});
		
		this.setSize(200, 150);
	}
	
	
	public void processMouseEvent(MouseEvent e){
		int col = columnAtPoint(e.getPoint());
		int row = rowAtPoint(e.getPoint());
		if (row!=-1 && col!=-1){
			TableCellRenderer r =getCellRenderer(row, col);
			//System.out.println("MOUSE TABLE: "+row+" , "+col);
			if (r instanceof ConditionsCellRendererEditor){
				ConditionsCellRendererEditor condRE = (ConditionsCellRendererEditor)r;
				JPanel panel = (JPanel)condRE.getTableCellRendererComponent(this, condRE.getCellEditorValue(), true, true,row, col);
				panel.requestFocusInWindow();
				e.setSource(panel);
				panel.dispatchEvent(e);
			}		
		}
		super.processMouseEvent(e);
	}
	
	private class ElementsTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		
		public int getColumnCount( ) {
			return 4;
		}

		public int getRowCount( ) {
			return dataControl.getActiveAreas().size();
		}
				
		public Object getValueAt( int rowIndex, int columnIndex ) {
			if (columnIndex == 0)
				return dataControl.getActiveAreas().get(rowIndex).getId();
			if (columnIndex == 1)
				return dataControl.getActiveAreas().get(rowIndex).getConditions();
			if (columnIndex == 3)
				return dataControl.getActiveAreas().get(rowIndex);
			return null;
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0)
				return TextConstants.getText( "ActiveAreasList.Id" );
			if (columnIndex == 1)
				return TextConstants.getText( "ActiveAreasList.Conditions" );
			if (columnIndex == 2)
				return TextConstants.getText( "ActiveAreasList.Actions");
			if (columnIndex == 3)
				return TextConstants.getText( "ActiveAreasList.Documentation");
			return "";
		}
		
		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				Controller.getInstance().addTool(new RenameElementTool(dataControl.getActiveAreas().get(rowIndex), (String) value));
			}
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			return getSelectedRow() == row;
		}
	}
}
