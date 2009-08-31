/**
 * <e-Adventure> is an <e-UCM> research project. <e-UCM>, Department of Software
 * Engineering and Artificial Intelligence. Faculty of Informatics, Complutense
 * University of Madrid (Spain).
 * 
 * @author Del Blanco, A., Marchiori, E., Torrente, F.J. (alphabetical order) *
 * @author L�pez Ma�as, E., P�rez Padilla, F., Sollet, E., Torijano, B. (former
 *         developers by alphabetical order)
 * @author Moreno-Ger, P. & Fern�ndez-Manj�n, B. (directors)
 * @year 2009 Web-site: http://e-adventure.e-ucm.es
 */

/*
 * Copyright (C) 2004-2009 <e-UCM> research group
 * 
 * This file is part of <e-Adventure> project, an educational game & game-like
 * simulation authoring tool, available at http://e-adventure.e-ucm.es.
 * 
 * <e-Adventure> is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * <e-Adventure> is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * <e-Adventure>; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */
package es.eucm.eadventure.editor.gui.elementpanels.general;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.control.controllers.EffectsController;
import es.eucm.eadventure.editor.gui.Updateable;
import es.eucm.eadventure.editor.gui.auxiliar.components.JFiller;
import es.eucm.eadventure.editor.gui.elementpanels.general.tables.ConditionsCellRendererEditor;
import es.eucm.eadventure.editor.gui.elementpanels.general.tables.EditEffectCellRenderEditor;

public class EffectsPanel extends JPanel implements Updateable {

    /**
     * Required.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Controller for the effects.
     */
    private EffectsController effectsController;

    /**
     * Table of the effects.
     */
    private JTable effectsTable;

    private JButton deleteButton;

    private JButton moveUpButton;

    private JButton moveDownButton;

    /**
     * Constructor.
     * 
     * @param effectsController
     *            Controller for the effects
     */
    public EffectsPanel( EffectsController effectsController ) {

        // Parent constructor
        super( );

        // Set the conditions controller and the icon
        this.effectsController = effectsController;

        // Set properties of the panel
        setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TextConstants.getText( "Effects.Title" ) ) );

        // Set the panel
        setLayout( new BorderLayout( ) );

        effectsTable = new JTable( new EffectsTableModel( ) );
        effectsTable.getColumnModel( ).getColumn( 0 ).setMaxWidth( 60 );

        // Effect info colummn
        effectsTable.getColumnModel( ).getColumn( 1 ).setCellRenderer( new TableCellRenderer( ) {

            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {

                return (JPanel) value;
            }

        } );

        // Edit button
        effectsTable.getColumnModel( ).getColumn( 2 ).setCellRenderer( new EditEffectCellRenderEditor( effectsTable ) );
        effectsTable.getColumnModel( ).getColumn( 2 ).setCellEditor( new EditEffectCellRenderEditor( effectsTable ) );
        effectsTable.getColumnModel( ).getColumn( 2 ).setMaxWidth( 120 );
        effectsTable.getColumnModel( ).getColumn( 2 ).setMinWidth( 120 );
        effectsTable.getColumnModel( ).getColumn( 2 ).setWidth( 120 );

        // Conditions  button
        effectsTable.getColumnModel( ).getColumn( 3 ).setCellRenderer( new ConditionsCellRendererEditor( ) );
        effectsTable.getColumnModel( ).getColumn( 3 ).setCellEditor( new ConditionsCellRendererEditor( ) );
        effectsTable.getColumnModel( ).getColumn( 3 ).setMaxWidth( 120 );
        effectsTable.getColumnModel( ).getColumn( 3 ).setMinWidth( 120 );
        effectsTable.getColumnModel( ).getColumn( 3 ).setWidth( 120 );
        effectsTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        effectsTable.getSelectionModel( ).addListSelectionListener( new ListSelectionListener( ) {

            public void valueChanged( ListSelectionEvent arg0 ) {

                effectsTable.setRowHeight( 20 );
                effectsTable.setRowHeight( effectsTable.getSelectedRow( ), 25 );
                updateButtons( );
            }
        } );

        JScrollPane tableScrollPane = new TableScrollPane( effectsTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        add( tableScrollPane, BorderLayout.CENTER );

        add( createButtonsPanel( ), BorderLayout.EAST );
    }

    private void updateButtons( ) {

        int selected = effectsTable.getSelectedRow( );
        if( selected == -1 ) {
            deleteButton.setEnabled( false );
            moveUpButton.setEnabled( false );
            moveDownButton.setEnabled( false );
        }
        else {
            deleteButton.setEnabled( effectsController.getEffectCount( ) > 0 );
            moveUpButton.setEnabled( selected > 0 );
            moveDownButton.setEnabled( selected < effectsTable.getRowCount( ) - 1 );
        }
    }

    private JPanel createButtonsPanel( ) {

        //Create the buttons panel (SOUTH)
        JPanel buttonsPanel = new JPanel( );
        JButton newButton = new JButton( new ImageIcon( "img/icons/addNode.png" ) );
        newButton.setContentAreaFilled( false );
        newButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        newButton.setBorder( BorderFactory.createEmptyBorder( ) );
        newButton.setToolTipText( TextConstants.getText( "ItemReferenceTable.AddParagraph" ) );
        newButton.addActionListener( new AddEffectListener( ) );

        deleteButton = new JButton( new ImageIcon( "img/icons/deleteNode.png" ) );
        deleteButton.setContentAreaFilled( false );
        deleteButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        deleteButton.setBorder( BorderFactory.createEmptyBorder( ) );
        deleteButton.setToolTipText( TextConstants.getText( "ItemReferenceTable.Delete" ) );
        deleteButton.addActionListener( new DeleteEffectListener( ) );
        deleteButton.setEnabled( false );

        moveUpButton = new JButton( new ImageIcon( "img/icons/moveNodeUp.png" ) );
        moveUpButton.setContentAreaFilled( false );
        moveUpButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        moveUpButton.setBorder( BorderFactory.createEmptyBorder( ) );
        moveUpButton.setToolTipText( TextConstants.getText( "ItemReferenceTable.MoveUp" ) );
        moveUpButton.addActionListener( new MoveUpEffectListener( ) );
        moveUpButton.setEnabled( false );

        moveDownButton = new JButton( new ImageIcon( "img/icons/moveNodeDown.png" ) );
        moveDownButton.setContentAreaFilled( false );
        moveDownButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        moveDownButton.setBorder( BorderFactory.createEmptyBorder( ) );
        moveDownButton.setToolTipText( TextConstants.getText( "ItemReferenceTable.MoveDown" ) );
        moveDownButton.addActionListener( new MoveDownEffectListener( ) );
        moveDownButton.setEnabled( false );

        buttonsPanel.setLayout( new GridBagLayout( ) );
        GridBagConstraints c = new GridBagConstraints( );
        c.gridx = 0;
        c.gridy = 0;
        buttonsPanel.add( newButton, c );
        c.gridy++;
        buttonsPanel.add( moveUpButton, c );
        c.gridy++;
        buttonsPanel.add( moveDownButton, c );
        c.gridy++;
        c.gridy++;
        buttonsPanel.add( deleteButton, c );

        c.gridy--;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1.0;
        buttonsPanel.add( new JFiller( ), c );

        return buttonsPanel;
    }

    /**
     * Listener for the add effect button.
     */
    private class AddEffectListener implements ActionListener {

        public void actionPerformed( ActionEvent e ) {

            if( effectsController.addEffect( ) ) {
                effectsTable.getSelectionModel( ).setSelectionInterval( effectsTable.getRowCount( ) - 1, effectsTable.getRowCount( ) - 1 );
                effectsTable.updateUI( );
                updateButtons( );
            }

        }
    }

    /**
     * Listener for the delete effect button.
     */
    private class DeleteEffectListener implements ActionListener {

        public void actionPerformed( ActionEvent e ) {

            int effectIndex = effectsTable.getSelectedRow( );

            if( effectIndex >= 0 ) {
                effectsController.deleteEffect( effectIndex );
                effectsTable.updateUI( );
                updateButtons( );
            }
        }
    }

    /**
     * Listener for the move up effect button.
     */
    private class MoveUpEffectListener implements ActionListener {

        public void actionPerformed( ActionEvent e ) {

            int effectIndex = effectsTable.getSelectedRow( );
            if( effectIndex >= 0 ) {
                if( effectsController.moveUpEffect( effectIndex ) ) {
                    effectsTable.changeSelection( effectIndex - 1, effectIndex - 1, false, false );
                    effectsTable.updateUI( );
                    updateButtons( );
                }
            }
        }
    }

    /**
     * Listener for the move down effect button.
     */
    private class MoveDownEffectListener implements ActionListener {

        public void actionPerformed( ActionEvent e ) {

            int effectIndex = effectsTable.getSelectedRow( );
            if( effectIndex >= 0 && effectIndex < effectsTable.getRowCount( ) - 1 ) {
                if( effectsController.moveDownEffect( effectIndex ) ) {
                    effectsTable.changeSelection( effectIndex + 1, effectIndex + 1, false, false );
                    effectsTable.updateUI( );
                    updateButtons( );
                }
            }
        }
    }

    /**
     * Table model to display conditions blocks.
     */
    private class EffectsTableModel extends AbstractTableModel {

        /**
         * Required.
         */
        private static final long serialVersionUID = 1L;

        public int getColumnCount( ) {

            // Four columns
            return 4;
        }

        public int getRowCount( ) {

            return effectsController.getEffectCount( );
        }

        @Override
        public String getColumnName( int columnIndex ) {

            String columnName = "";

            // The first column is the effect number
            if( columnIndex == 0 )
                columnName = TextConstants.getText( "Effects.EffectNumberColumnTitle" );

            // The second one the effect information
            else if( columnIndex == 1 )
                columnName = TextConstants.getText( "Effects.EffectDescriptionColumnTitle" );

            // The third one edit the effect
            else if( columnIndex == 2 )
                columnName = TextConstants.getText( "ActionList.EditEffect" );

            // The fourth one has the condition of each effect
            else if( columnIndex == 3 )
                columnName = TextConstants.getText( "ActionsList.Conditions" );

            return columnName;
        }

        public Object getValueAt( int rowIndex, int columnIndex ) {

            Object value = null;

            if( columnIndex == 0 )
                value = rowIndex + 1;

            else if( columnIndex == 1 ) {
                JPanel effectPanel = new JPanel( );
                JLabel icon = new JLabel( effectsController.getEffectIcon( rowIndex ) );
                JLabel text = new JLabel( effectsController.getEffectInfo( rowIndex ) );
                effectPanel.setLayout( new BoxLayout( effectPanel, BoxLayout.LINE_AXIS ) );
                effectPanel.add( icon );
                effectPanel.add( text );
                effectPanel.setOpaque( false );
                value = effectPanel;

            }
            else if( columnIndex == 2 )
                value = effectsController;

            else if( columnIndex == 3 )
                value = effectsController.getConditionController( rowIndex );

            return value;
        }

        @Override
        public boolean isCellEditable( int row, int column ) {

            boolean isEditable = false;
            if( column == 2 || column == 3 )
                isEditable = true;
            return isEditable && row == effectsTable.getSelectedRow( );
        }
    }

    public boolean updateFields( ) {

        updateButtons( );
        effectsTable.updateUI( );
        //effectsTable.setModel(new EffectsTableModel( ));
        return true;
    }

}
