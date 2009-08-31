/**
 * <e-Adventure> is an <e-UCM> research project.
 * <e-UCM>, Department of Software Engineering and Artificial Intelligence.
 * Faculty of Informatics, Complutense University of Madrid (Spain).
 * @author Del Blanco, A., Marchiori, E., Torrente, F.J.
 * @author Moreno-Ger, P. & Fern�ndez-Manj�n, B. (directors)
 * @year 2009
 * Web-site: http://e-adventure.e-ucm.es
 */

/*
    Copyright (C) 2004-2009 <e-UCM> research group

    This file is part of <e-Adventure> project, an educational game & game-like 
    simulation authoring tool, availabe at http://e-adventure.e-ucm.es. 

    <e-Adventure> is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    <e-Adventure> is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with <e-Adventure>; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*/
package es.eucm.eadventure.editor.gui.elementpanels.adaptation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import es.eucm.eadventure.common.data.adaptation.AdaptedState;
import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.adaptation.AdaptationRuleDataControl;
import es.eucm.eadventure.editor.control.controllers.scene.ElementContainer;
import es.eucm.eadventure.editor.gui.Updateable;
import es.eucm.eadventure.editor.gui.auxiliar.components.JFiller;
import es.eucm.eadventure.editor.gui.editdialogs.VarDialog;
import es.eucm.eadventure.editor.gui.elementpanels.general.tables.ConditionsCellRendererEditor;
import es.eucm.eadventure.editor.gui.elementpanels.scene.ElementReferencesTable;

/**
 * This class is the panel used to display and edit nodes. It holds node operations, like adding and removing lines,
 * editing end effects, remove links and reposition lines and children
 */
class GameStatePanel extends JPanel implements Updateable{

	/**
	 * Required
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Adaptation rule data controller.
	 */
	private AdaptationRuleDataControl adaptationRuleDataControl;

	/**
	 * Table in which the node lines are represented
	 */
	private JTable actionFlagsTable;

	/**
	 * Scroll panel that holds the table
	 */
	private JScrollPane tableScrollPanel;

	/**
	 * "Insert property" button
	 */
	private JButton insertActionFlagButton;

	/**
	 * "Delete property" button
	 */
	private JButton deleteActionFlagButton;
	
	/**
	 * Combo box to show the flags and vars names in chapter
	 */
//	private JComboBox flagsCB;

	/**
	 * The table model
	 */
	private NodeTableModel tableModel;
	
	/**
	 * Como box for type of flags/var actions
	 */
	private JComboBox actionValues;
	

	/* Methods */

	/**
	 * Constructor
	 * 
	 * @param principalPanel
	 *            Link to the principal panel, for sending signals
	 * @param adaptationRuleDataControl
	 *            Data controller to edit the lines
	 */
	public GameStatePanel( AdaptationRuleDataControl adpDataControl ) {
		// Set the initial values
		this.adaptationRuleDataControl = adpDataControl;

		// Set a GridBagLayout
		setLayout( new BorderLayout() );

		/* Common elements (for Node and Option panels) */
		// Create the table with an empty model
		tableModel = new NodeTableModel( ) ;
		actionFlagsTable = new JTable( tableModel);

		// Column size properties
		actionFlagsTable.setAutoCreateColumnsFromModel( false );
		actionFlagsTable.getColumnModel( ).getColumn( 0 ).setMaxWidth( 60 );
		actionFlagsTable.getColumnModel( ).getColumn( 1 ).setMaxWidth( 60 );
		
		
		// Selection properties
		actionFlagsTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		actionFlagsTable.setCellSelectionEnabled( false );
		actionFlagsTable.setColumnSelectionAllowed( false );
		actionFlagsTable.setRowSelectionAllowed( true );
		actionFlagsTable.setIntercellSpacing( new Dimension( 1, 1 ) );

		// Add selection listener to the table
		actionFlagsTable.getSelectionModel( ).addListSelectionListener( new NodeTableSelectionListener( ) );
		actionFlagsTable.getSelectionModel( ).setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		//actionFlagsTable.getSelectionModel( ).setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		
		// set cell render and editor
		actionFlagsTable.getColumnModel().getColumn(0).setCellRenderer(actionFlagsTable.getDefaultRenderer(Boolean.class));
		actionFlagsTable.getColumnModel().getColumn(0).setCellEditor(actionFlagsTable.getDefaultEditor(Boolean.class));
		
		actionFlagsTable.getColumnModel().getColumn(1).setCellRenderer(actionFlagsTable.getDefaultRenderer(Boolean.class));
		actionFlagsTable.getColumnModel().getColumn(1).setCellEditor(actionFlagsTable.getDefaultEditor(Boolean.class));
		
		
		actionFlagsTable.getColumnModel().getColumn(2).setCellRenderer(new FlagsVarListRenderer());
		actionFlagsTable.getColumnModel().getColumn(2).setCellEditor(new FlagsVarListRenderer());
		
		
		actionFlagsTable.getColumnModel().getColumn(3).setCellRenderer(new FlagsVarListRenderer());
		actionFlagsTable.getColumnModel().getColumn(3).setCellEditor(new FlagsVarListRenderer());
		
		
		
		actionFlagsTable.setRowHeight(22);
		
		
		// Table scrollPane
		tableScrollPanel = new JScrollPane( actionFlagsTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		tableScrollPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ), TextConstants.getText( "AdaptationRule.GameState.ActionFlags" ) ));
		/* End of common elements */

		/* Dialogue panel elements */
		insertActionFlagButton = new JButton(new ImageIcon("img/icons/addNode.png"));
		insertActionFlagButton.setContentAreaFilled( false );
		insertActionFlagButton.setMargin( new Insets(0,0,0,0) );
		insertActionFlagButton.setBorder(BorderFactory.createEmptyBorder());
		insertActionFlagButton.setToolTipText( TextConstants.getText( "Operation.AdaptationPanel.InsertButton" ) );
		insertActionFlagButton.addActionListener( new ListenerButtonInsertLine( ) );
		
		deleteActionFlagButton = new JButton(new ImageIcon("img/icons/deleteNode.png"));
		deleteActionFlagButton.setContentAreaFilled( false );
		deleteActionFlagButton.setMargin( new Insets(0,0,0,0) );
		deleteActionFlagButton.setBorder(BorderFactory.createEmptyBorder());
		deleteActionFlagButton.setToolTipText( TextConstants.getText( "Operation.AdaptationPanel.DeleteButton" ) );
		deleteActionFlagButton.addActionListener( new ListenerButtonDeleteLine( ) );
		
		String[] scenes = Controller.getInstance( ).getIdentifierSummary( ).getSceneIds( );
		String[] isValues = new String[scenes.length+1];
		isValues[0] = TextConstants.getText("GeneralText.NotSelected");
		for (int i=0; i<scenes.length; i++){
			isValues[i+1]=scenes[i];
		}
		
		/* End of dialogue panel elements */

		addComponents();
	}

	/**
	 * Removes all elements in the panel, and sets a dialogue node panel
	 */
	private void addComponents( ) {
		removeAll( );
		deleteActionFlagButton.setEnabled( false );
		setLayout(new BorderLayout());
				
		add(tableScrollPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints( );
		c.gridx = 0;
		c.gridy = 0;
		buttonPanel.add( insertActionFlagButton, c );
		c.gridy = 2;
		buttonPanel.add( deleteActionFlagButton, c );
		c.gridy = 1;
		c.weighty = 2.0;
		c.fill = GridBagConstraints.VERTICAL;
		buttonPanel.add(new JFiller(), c);
		
		add(buttonPanel, BorderLayout.EAST);
	}




	/**
	 * Listener for the "Insert property" button
	 */
	private class ListenerButtonInsertLine implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed( ActionEvent e ) {
			// Take the selected row, and the selected node
			int selectedRow = actionFlagsTable.getSelectedRow( );

			// If no row is selected, set the insertion position at the end
			if( selectedRow == -1 )
				selectedRow = actionFlagsTable.getRowCount( ) - 1;

			
			// Insert the dialogue line in the selected position
			if (adaptationRuleDataControl.addFlagAction( selectedRow + 1 )){

				// Select the inserted line
				actionFlagsTable.setRowSelectionInterval( selectedRow + 1, selectedRow + 1 );
				actionFlagsTable.scrollRectToVisible( actionFlagsTable.getCellRect( selectedRow + 1, 0, true ) );
	
				// Update the table
				actionFlagsTable.revalidate( );
			}
		}
	}

	/**
	 * Listener for the "Delete line" button
	 */
	private class ListenerButtonDeleteLine implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed( ActionEvent e ) {
			// Take the selected row, and the selected node
			int selectedRow = actionFlagsTable.getSelectedRow( );

			// Delete the selected line
			adaptationRuleDataControl.deleteFlagAction( selectedRow );

			// If there are no more lines, clear selection (this disables the "Delete line" button)
			if( adaptationRuleDataControl.getFlagActionCount( ) == 0 )
				actionFlagsTable.clearSelection( );

			// If the deleted line was the last one, select the new last line in the node
			else if(adaptationRuleDataControl.getFlagActionCount( ) == selectedRow )
				actionFlagsTable.setRowSelectionInterval( selectedRow - 1, selectedRow - 1 );

			// Update the table
			actionFlagsTable.revalidate( );
		}
	}


	/**
	 * Private class managing the selection listener of the table
	 */
	private class NodeTableSelectionListener implements ListSelectionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
		 */
		public void valueChanged( ListSelectionEvent e ) {
			// Extract the selection model of the list
			ListSelectionModel lsm = (ListSelectionModel) e.getSource( );

			// If there is no line selected
			if( lsm.isSelectionEmpty( ) ) {
				// Disable all options
				deleteActionFlagButton.setEnabled( false );
			}

			// If there is a line selected
			else {
				// Enable all options
				deleteActionFlagButton.setEnabled( true );
			}
			
			
		}
	}

	/**
	 * Private class containing the model for the line table
	 */
	private class NodeTableModel extends AbstractTableModel {

		/**
		 * Required
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Store if each element in table is flag or variable (true for flags and false for vars)
		 */
		//private ArrayList<Boolean> isFlagVar;
		
		/**
		 * Constructor
		 */
		public NodeTableModel(  ) {
		   // isFlagVar = new ArrayList<Boolean>();
		}

		public String getColumnName ( int columnIndex ){
			String name = "";
			if (columnIndex == 0)
				name = "Var?";
			else if (columnIndex == 1)
				name = "Flag?";
			if (columnIndex == 2)
				name = "Action";
			else if (columnIndex == 3)
				name = "Var/Flag";
			return name;
		}
		
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount( ) {
			int rowCount = 0;

			// If there is a node, the number of rows is the same as the number of lines
			if( adaptationRuleDataControl != null )
				rowCount = adaptationRuleDataControl.getFlagActionCount( );

			return rowCount;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount( ) {
			// All line tables has four columns
			return 4;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getColumnClass(int)
		 */
		public Class<?> getColumnClass( int c ) {
			return getValueAt( 0, c ).getClass( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#isCellEditable(int, int)
		 */
		public boolean isCellEditable( int rowIndex, int columnIndex ) {
			return rowIndex ==	actionFlagsTable.getSelectedRow();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
		 */
		public void setValueAt( Object value, int rowIndex, int columnIndex ) {

			// If the value isn't an empty string
			if( value!=null && !value.toString( ).trim( ).equals( "" ) && columnIndex<=1) {
			    	
			    	if( columnIndex == 0){
			    	    // if not selected
			    	    if (adaptationRuleDataControl.isFlag(rowIndex)){
			    		
			    		if (actionFlagsTable.getSelectedRow()==rowIndex){
			    		String[] names = Controller.getInstance( ).getVarFlagSummary( ).getVars();
			    		// take any var if there are at least one 
			    		if (names.length==0){
			    		   //Controller.getInstance().showErrorDialog(TextConstants.getText("Error.NoVarsAvailable.Title"), TextConstants.getText("Error.NoVarsAvailable.Message"));
			    		    // change to var
			    		    adaptationRuleDataControl.change(rowIndex, "");
			    		}else 
			    		    // change to var
			    		    adaptationRuleDataControl.change(rowIndex, names[0]);
			    		}
			    	    }
			    	    
			    		
			    	}
			    
			    	if( columnIndex == 1){
			    	    // if not selected
			    	if (!adaptationRuleDataControl.isFlag(rowIndex)){
			    		
			    	if (actionFlagsTable.getSelectedRow()==rowIndex){
			    		String[] names = Controller.getInstance( ).getVarFlagSummary( ).getFlags();
			    		// take any flag if there are at least one 
			    		if (names.length==0){
			    		    //Controller.getInstance().showErrorDialog(TextConstants.getText("Error.NoFlagsAvailable.Title"), TextConstants.getText("Error.NoFlagsAvailable.Message"));
			    		    // change to flag
			    		    adaptationRuleDataControl.change(rowIndex, "");
			    		}   else
			    		 // change to flag
			    		    adaptationRuleDataControl.change(rowIndex, names[0]);		
			    	    }
			    	}
			    		
			    	}
			    

				fireTableRowsUpdated( rowIndex, rowIndex );
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt( int rowIndex, int columnIndex ) {
			Object value = null;

			// Return value depending of the selected row
			switch( columnIndex ) {
				
				case 0: // IsVar 
				    	
				    	value = !adaptationRuleDataControl.isFlag(rowIndex);
				    	break;
				    	
				case 1: // IsFlag 
				    	value = adaptationRuleDataControl.isFlag(rowIndex);
				    	break;
			
				case 2:
				case 3:
					value = adaptationRuleDataControl;
				    	break;
			}

			return value;
		}

	}
	
	
	
	

	
	
	

	public boolean updateFields() {
		int selection = actionFlagsTable.getSelectedRow();
		actionFlagsTable.clearSelection();
		deleteActionFlagButton.setEnabled( false );
		
		if (actionFlagsTable.getCellEditor() != null)
			actionFlagsTable.getCellEditor().stopCellEditing();
		((AbstractTableModel)actionFlagsTable.getModel()).fireTableDataChanged();
		//((AbstractTableModel)actionFlagsTable.getModel()).fireTableRowsUpdated(selection, selection);
		
		actionFlagsTable.getSelectionModel().setSelectionInterval(selection,selection);		
		return true;
	}
}
