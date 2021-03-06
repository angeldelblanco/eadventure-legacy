/*******************************************************************************
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the e-UCM
 *          research group.
 *   
 *    Copyright 2005-2012 e-UCM research group.
 *  
 *     e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *  
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *  
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *  
 *  ****************************************************************************
 * This file is part of eAdventure, version 1.5.
 * 
 *   You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *  
 *  ****************************************************************************
 *       eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *  
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *  
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with Adventure.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.eucm.eadventure.editor.gui.elementpanels.adaptation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import es.eucm.eadventure.common.data.adaptation.AdaptationProfile;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.config.SCORMConfigData;
import es.eucm.eadventure.editor.control.controllers.adaptation.AdaptationRuleDataControl;
import es.eucm.eadventure.editor.gui.Updateable;
import es.eucm.eadventure.editor.gui.auxiliar.components.JFiller;

public class UOLPropertiesPanel extends JPanel implements Updateable {

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
    private JTable propertiesTable;

    /**
     * Scroll panel that holds the table
     */
    private JScrollPane tableScrollPanel;

    /**
     * Move property up ( /\ ) button
     */
    private JButton movePropertyUpButton;

    /**
     * Move property down ( \/ ) button
     */
    private JButton movePropertyDownButton;

    /**
     * "Insert property" button
     */
    private JButton insertPropertyButton;

    /**
     * "Delete property" button
     */
    private JButton deletePropertyButton;

    /* Methods */

    /**
     * Constructor
     * 
     * @param adaptationRuleDataControl
     *            Data controller to edit the lines
     * 
     * @param scorm12
     *            Show if it is a Scorm 1.2 profile, to take its data model
     * 
     * @param scorm2004
     *            Show if it is a Scorm 2004 profile, to take its data model
     */
    public UOLPropertiesPanel( AdaptationRuleDataControl adpDataControl, boolean scorm12, boolean scorm2004 ) {

        // Set the initial values
        this.adaptationRuleDataControl = adpDataControl;

        // Set a GridBagLayout
        setLayout( new BorderLayout( ) );

        /* Common elements (for Node and Option panels) */
        // Create the table with an empty model
        propertiesTable = new JTable( new NodeTableModel( ) );

        // Column size properties
        propertiesTable.setAutoCreateColumnsFromModel( false );
      
        // Set the operations values 
        JComboBox operations = new JComboBox( AdaptationProfile.getOperations( ) );
        propertiesTable.getColumnModel( ).getColumn( 1 ).setCellEditor( new DefaultCellEditor( operations ) );
        propertiesTable.getColumnModel( ).getColumn( 1 ).setMaxWidth( 60 );

        // if is SCORM profile, fill the fields of the SCORM data model which can be accessed
        if( scorm12 ) {

            JComboBox actionValuesCB = new JComboBox(  SCORMConfigData.getPartsOfModel12( SCORMConfigData.READ ).toArray() );
            propertiesTable.getColumnModel( ).getColumn( 0 ).setCellEditor( new DefaultCellEditor( actionValuesCB ) );
        }

        if( scorm2004 ) {

            JComboBox actionValuesCB = new JComboBox( SCORMConfigData.getPartsOfModel2004( SCORMConfigData.READ ).toArray() );
            propertiesTable.getColumnModel( ).getColumn( 0 ).setCellEditor( new DefaultCellEditor( actionValuesCB ) );

        }

        // Selection properties
        propertiesTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        propertiesTable.setCellSelectionEnabled( false );
        propertiesTable.setColumnSelectionAllowed( false );
        propertiesTable.setRowSelectionAllowed( true );

        // Misc properties
        //propertiesTable.setTableHeader( null );
        propertiesTable.setIntercellSpacing( new Dimension( 1, 1 ) );

        // Add selection listener to the table
        propertiesTable.getSelectionModel( ).addListSelectionListener( new NodeTableSelectionListener( ) );

        // Table scrollPane
        tableScrollPanel = new JScrollPane( propertiesTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );

        // Up and down buttons
        movePropertyUpButton = new JButton( new ImageIcon( "img/icons/moveNodeUp.png" ) );
        movePropertyUpButton.setContentAreaFilled( false );
        movePropertyUpButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        movePropertyUpButton.setBorder( BorderFactory.createEmptyBorder( ) );
        movePropertyUpButton.setToolTipText( TC.get( "UOLProperties.MoveUp" ) );
        movePropertyUpButton.addActionListener( new ListenerButtonMoveLineUp( ) );
        movePropertyUpButton.setEnabled( false );

        movePropertyDownButton = new JButton( new ImageIcon( "img/icons/moveNodeDown.png" ) );
        movePropertyDownButton.setContentAreaFilled( false );
        movePropertyDownButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        movePropertyDownButton.setBorder( BorderFactory.createEmptyBorder( ) );
        movePropertyDownButton.setToolTipText( TC.get( "UOLProperties.MoveDown" ) );
        movePropertyDownButton.addActionListener( new ListenerButtonMoveLineDown( ) );
        movePropertyDownButton.setEnabled( false );

        /* End of common elements */

        /* Dialogue panel elements */
        insertPropertyButton = new JButton( new ImageIcon( "img/icons/addNode.png" ) );
        insertPropertyButton.setContentAreaFilled( false );
        insertPropertyButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        insertPropertyButton.setBorder( BorderFactory.createEmptyBorder( ) );
        insertPropertyButton.setToolTipText( TC.get( "UOLProperties.InsertProperty" ) );
        insertPropertyButton.addActionListener( new ListenerButtonInsertLine( ) );

        deletePropertyButton = new JButton( new ImageIcon( "img/icons/deleteNode.png" ) );
        deletePropertyButton.setContentAreaFilled( false );
        deletePropertyButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        deletePropertyButton.setBorder( BorderFactory.createEmptyBorder( ) );
        deletePropertyButton.setToolTipText( TC.get( "UOLProperties.DeleteProperty" ) );
        deletePropertyButton.addActionListener( new ListenerButtonDeleteLine( ) );

        addComponents( );
    }

   
    /**
     * Removes all elements in the panel, and sets a dialogue node panel
     */
    private void addComponents( ) {

        removeAll( );
        movePropertyUpButton.setEnabled( false );
        movePropertyDownButton.setEnabled( false );
        deletePropertyButton.setEnabled( false );

        add( tableScrollPanel, BorderLayout.CENTER );

        JPanel buttonsPanel = new JPanel( );
        buttonsPanel.setLayout( new GridBagLayout( ) );
        GridBagConstraints c = new GridBagConstraints( );
        c.gridx = 0;
        c.gridy = 0;
        buttonsPanel.add( insertPropertyButton, c );
        c.gridy = 1;
        buttonsPanel.add( movePropertyUpButton, c );
        c.gridy = 2;
        buttonsPanel.add( movePropertyDownButton, c );
        c.gridy = 4;
        buttonsPanel.add( deletePropertyButton, c );
        c.gridy = 3;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 2.0;
        buttonsPanel.add( new JFiller( ), c );

        add( buttonsPanel, BorderLayout.EAST );
    }

    /**
     * Listener for the move line up ( /\ ) button
     */
    private class ListenerButtonMoveLineUp implements ActionListener {

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed( ActionEvent e ) {

            // Take the selected row, and the selected node
            int selectedRow = propertiesTable.getSelectedRow( );

            // If the line was moved
            if( adaptationRuleDataControl.moveUOLPropertyUp( selectedRow ) ) {

                // Move the selection along with the line
                propertiesTable.setRowSelectionInterval( selectedRow - 1, selectedRow - 1 );
                propertiesTable.scrollRectToVisible( propertiesTable.getCellRect( selectedRow - 1, 0, true ) );

            }
        }
    }

    /**
     * Listener for the move line down ( \/ ) button
     */
    private class ListenerButtonMoveLineDown implements ActionListener {

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed( ActionEvent e ) {

            // Take the selected row, and the selected node
            int selectedRow = propertiesTable.getSelectedRow( );

            // If the line was moved
            if( adaptationRuleDataControl.moveUOLPropertyDown( selectedRow ) ) {

                // Move the selection along with the line
                propertiesTable.setRowSelectionInterval( selectedRow + 1, selectedRow + 1 );
                propertiesTable.scrollRectToVisible( propertiesTable.getCellRect( selectedRow + 1, 0, true ) );

            }
        }
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
            int selectedRow = propertiesTable.getSelectedRow( );

            // If no row is selected, set the insertion position at the end
            if( selectedRow == -1 )
                selectedRow = propertiesTable.getRowCount( ) - 1;

            // Insert the dialogue line in the selected position
            adaptationRuleDataControl.addBlankUOLProperty( selectedRow + 1 );

            // Select the inserted line
            propertiesTable.setRowSelectionInterval( selectedRow + 1, selectedRow + 1 );
            propertiesTable.scrollRectToVisible( propertiesTable.getCellRect( selectedRow + 1, 0, true ) );

            // Update the table
            propertiesTable.revalidate( );
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
            int selectedRow = propertiesTable.getSelectedRow( );

            // Delete the selected line
            adaptationRuleDataControl.deleteUOLProperty( selectedRow );

            // If there are no more lines, clear selection (this disables the "Delete line" button)
            if( adaptationRuleDataControl.getUOLPropertyCount( ) == 0 )
                propertiesTable.clearSelection( );

            // If the deleted line was the last one, select the new last line in the node
            else if( adaptationRuleDataControl.getUOLPropertyCount( ) == selectedRow )
                propertiesTable.setRowSelectionInterval( selectedRow - 1, selectedRow - 1 );

            // Update the table
            propertiesTable.revalidate( );
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
                movePropertyUpButton.setEnabled( false );
                movePropertyDownButton.setEnabled( false );
                deletePropertyButton.setEnabled( false );
            }

            // If there is a line selected
            else {
                // Enable all options
                movePropertyUpButton.setEnabled( true );
                movePropertyDownButton.setEnabled( true );
                deletePropertyButton.setEnabled( true );
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
         * Constructor
         */
        public NodeTableModel( ) {

        }

        @Override
        public String getColumnName( int columnIndex ) {

            String name = "";
            if( columnIndex == 0 )
                name = "Id";
            else if( columnIndex == 1 )
                name = "Op";
            else if( columnIndex == 2 )
                name = "Value";
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
                rowCount = adaptationRuleDataControl.getUOLPropertyCount( );

            return rowCount;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount( ) {

            // All line tables has three columns
            return 3;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getColumnClass(int)
         */
        @Override
        public Class<?> getColumnClass( int c ) {

            return getValueAt( 0, c ).getClass( );
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#isCellEditable(int, int)
         */
        @Override
        public boolean isCellEditable( int rowIndex, int columnIndex ) {

            return true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
         */
        @Override
        public void setValueAt( Object value, int rowIndex, int columnIndex ) {

            // If the value isn't an empty string
            if( value != null ) {
                if( !value.toString( ).trim( ).equals( "" ) ) {
                    if( rowIndex < adaptationRuleDataControl.getUOLPropertyCount( ) ) {
                        // If the name is being edited, and it has really changed
                        if( columnIndex == 0 ){
                            UOLPropertiesPanel.this.propertiesTable.updateUI();
                            adaptationRuleDataControl.setUOLPropertyId( rowIndex, value.toString( ) );
                        }
                        // If the comparison operation is being edited, and it has really changed
                        if( columnIndex == 1 )
                            adaptationRuleDataControl.setUOLPropertyOp( rowIndex, AdaptationProfile.getOpName( value ) );

                        // If the text is being edited, and it has really changed
                        if( columnIndex == 2 )
                            adaptationRuleDataControl.setUOLPropertyValue( rowIndex, value.toString( ) );

                        fireTableCellUpdated( rowIndex, columnIndex );
                    }
                }
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
                case 0:
                    // Id of the property
                    value = adaptationRuleDataControl.getUOLPropertyId( rowIndex );
                    break;
                case 1:
                    // Comparison operation value
                    value = AdaptationProfile.getOpRepresentation( adaptationRuleDataControl.getUOLPropertyOp( rowIndex ) );
                    break;

                case 2:
                    // Property value
                    value = adaptationRuleDataControl.getUOLPropertyValue( rowIndex );
                    break;
            }

            return value;
        }
    }

    // the call is not spread 
    public boolean updateFields( ) {
	
        int selected = propertiesTable.getSelectedRow( );
        if( propertiesTable.getCellEditor( ) != null ) {
            propertiesTable.getCellEditor( ).cancelCellEditing( );
        }
        ( (AbstractTableModel) propertiesTable.getModel( ) ).fireTableDataChanged( );
         
        propertiesTable.getSelectionModel( ).setSelectionInterval( selected, selected );
        
        return true;
    }
}
