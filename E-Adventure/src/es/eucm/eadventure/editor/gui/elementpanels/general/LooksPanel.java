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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.DataControl;
import es.eucm.eadventure.editor.control.controllers.DataControlWithResources;
import es.eucm.eadventure.editor.gui.Updateable;
import es.eucm.eadventure.editor.gui.auxiliar.components.JFiller;
import es.eucm.eadventure.editor.gui.elementpanels.general.tables.ResourcesTable;

public abstract class LooksPanel extends JPanel implements Updateable {

    private static final long serialVersionUID = 1L;

    private static final int HORIZONTAL_SPLIT_POSITION = 70;

    protected DataControlWithResources dataControl;

    protected JPanel lookPanel;

    protected GridBagConstraints cLook;

    /**
     * Combo box for the selected resources block.
     */
    protected ResourcesTable resourcesTable;

    protected JButton newResourcesBlock;

    protected JButton deleteResourcesBlock;

    protected JButton duplicateResourcesBlock;

    protected ResourcesPanel resourcesPanel;

    protected int selectedResourceGroup = 0;

    public LooksPanel( DataControlWithResources dControl ) {

        super( );
        this.dataControl = dControl;
        lookPanel = new JPanel( );
        lookPanel.setLayout( new GridBagLayout( ) );
        cLook = new GridBagConstraints( );

        // Create the combo resources blocks
        cLook.insets = new Insets( 5, 5, 5, 5 );
        cLook.fill = GridBagConstraints.HORIZONTAL;
        cLook.weightx = 1;
        cLook.gridy = 0;
        cLook.weighty = 0;
        cLook.anchor = GridBagConstraints.LINE_START;
        JPanel resourcesComboPanel = new JPanel( );
        resourcesComboPanel.setLayout( new GridLayout( ) );
        // Create the list of resources

        //resourcesComboBox = new JComboBox( getResourceNames() );
        resourcesTable = new ResourcesTable( dataControl, this );
        resourcesTable.setSelectedIndex( -1 );
        resourcesTable.getSelectionModel( ).addListSelectionListener( new ListSelectionListener( ) {

            public void valueChanged( ListSelectionEvent arg0 ) {

                if( resourcesTable.getSelectedRow( ) >= 0 ) {
                    updateResources( );
                    duplicateResourcesBlock.setEnabled( true );
                    if( dataControl.getResourcesCount( ) > 1 ) {
                        deleteResourcesBlock.setEnabled( true );
                    }
                    else {
                        deleteResourcesBlock.setEnabled( false );
                    }
                }
                else {
                    duplicateResourcesBlock.setEnabled( false );
                }
            }
        } );

        JScrollPane scroll = new JScrollPane( resourcesTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        scroll.setMinimumSize( new Dimension( 0, HORIZONTAL_SPLIT_POSITION ) );

        //resourcesComboBox.addActionListener( new ResourcesComboBoxListener( ) );

        //Create the add button
        newResourcesBlock = new JButton( new ImageIcon( "img/icons/addNode.png" ) );
        newResourcesBlock.setContentAreaFilled( false );
        newResourcesBlock.setMargin( new Insets( 0, 0, 0, 0 ) );
        newResourcesBlock.setBorder( BorderFactory.createEmptyBorder( ) );
        newResourcesBlock.setToolTipText( TextConstants.getText( "ResourcesList.AddResourcesBlock" ) );
        newResourcesBlock.addActionListener( new NewButtonListener( ) );

        this.deleteResourcesBlock = new JButton( new ImageIcon( "img/icons/deleteNode.png" ) );
        deleteResourcesBlock.setContentAreaFilled( false );
        deleteResourcesBlock.setMargin( new Insets( 0, 0, 0, 0 ) );
        deleteResourcesBlock.setBorder( BorderFactory.createEmptyBorder( ) );
        deleteResourcesBlock.setToolTipText( TextConstants.getText( "ResourcesList.DeleteResourcesBlock" ) );
        deleteResourcesBlock.setEnabled( false );
        deleteResourcesBlock.addActionListener( new DeleteButtonListener( ) );

        this.duplicateResourcesBlock = new JButton( new ImageIcon( "img/icons/duplicateNode.png" ) );
        duplicateResourcesBlock.setContentAreaFilled( false );
        duplicateResourcesBlock.setMargin( new Insets( 0, 0, 0, 0 ) );
        duplicateResourcesBlock.setBorder( BorderFactory.createEmptyBorder( ) );
        duplicateResourcesBlock.setToolTipText( TextConstants.getText( "ResourcesList.DuplicateResourcesBlock" ) );
        duplicateResourcesBlock.setEnabled( false );
        duplicateResourcesBlock.addActionListener( new DuplicateButtonListener( ) );

        JPanel blockControls = new JPanel( );

        JPanel buttonsPanel = new JPanel( );
        buttonsPanel.setLayout( new GridBagLayout( ) );
        GridBagConstraints c = new GridBagConstraints( );
        c.gridx = 0;
        c.gridy = 0;
        buttonsPanel.add( newResourcesBlock, c );
        c.gridy = 1;
        buttonsPanel.add( duplicateResourcesBlock, c );
        c.gridy = 3;
        buttonsPanel.add( deleteResourcesBlock, c );
        c.gridy = 2;
        c.weighty = 2.0;
        c.fill = GridBagConstraints.VERTICAL;
        buttonsPanel.add( new JFiller( ), c );

        blockControls.setLayout( new BorderLayout( ) );
        blockControls.add( scroll, BorderLayout.CENTER );
        blockControls.add( buttonsPanel, BorderLayout.EAST );

        //Create the resources Panel
        resourcesPanel = new ResourcesPanel( dataControl.getResources( ).get( dataControl.getSelectedResources( ) ) );
        resourcesPanel.setPreviewUpdater( this );
        cLook.gridy = 0;
        cLook.gridx = 0;
        cLook.gridwidth = 2;
        cLook.fill = GridBagConstraints.BOTH;
        cLook.weightx = 1;
        cLook.weighty = 0.0;
        lookPanel.add( resourcesPanel, cLook );

        // Create and set the preview panel
        cLook.gridy = 1;
        cLook.fill = GridBagConstraints.BOTH;
        cLook.weighty = 1;
        cLook.weightx = 1;
        cLook.gridwidth = 2;
        createPreview( );

        JSplitPane tableWithSplit = new JSplitPane( JSplitPane.VERTICAL_SPLIT, blockControls, lookPanel );
        tableWithSplit.setOneTouchExpandable( true );
        tableWithSplit.setDividerLocation( HORIZONTAL_SPLIT_POSITION );
        tableWithSplit.setContinuousLayout( true );
        tableWithSplit.setResizeWeight( 0 );
        tableWithSplit.setDividerSize( 10 );

        if( resourcesTable.getRowCount( ) == 1 )
            tableWithSplit.setDividerLocation( 0 );

        setLayout( new BorderLayout( ) );
        add( tableWithSplit, BorderLayout.CENTER );

        resourcesTable.setSelectedIndex( dataControl.getSelectedResources( ) );
    }

    public abstract void updatePreview( );

    protected abstract void createPreview( );

    private class DeleteButtonListener implements ActionListener {

        // XXX 8/5/2009: Revisado tool a�adir recursos (en todos los elementos)
        public void actionPerformed( ActionEvent e ) {

            if( resourcesTable.getSelectedIndex( ) >= 0 ) {
                dataControl.setSelectedResources( resourcesTable.getSelectedIndex( ) );
                int selectedBlock = dataControl.getSelectedResources( );
                DataControl resourcesToDelete = dataControl.getResources( ).get( selectedBlock );
                if( dataControl.deleteElement( resourcesToDelete, true ) ) {
                    dataControl.setSelectedResources( 0 );
                    resourcesTable.setSelectedIndex( 0 );
                    updateResources( );
                    resourcesTable.updateUI( );
                    ( (AbstractTableModel) resourcesTable.getModel( ) ).fireTableDataChanged( );
                }
            }
        }
    }

    private class DuplicateButtonListener implements ActionListener {

        public void actionPerformed( ActionEvent e ) {

            // XXX 8/5/2009: Revisado tool a�adir recursos (en todos los elementos)
            if( resourcesTable.getSelectedIndex( ) >= 0 ) {
                dataControl.setSelectedResources( resourcesTable.getSelectedIndex( ) );
                int selectedBlock = dataControl.getSelectedResources( );
                DataControl resourcesToDuplicate = dataControl.getResources( ).get( selectedBlock );
                if( dataControl.duplicateResources( resourcesToDuplicate ) ) {
                    dataControl.setSelectedResources( dataControl.getResourcesCount( ) - 1 );
                    resourcesTable.setSelectedIndex( dataControl.getResourcesCount( ) - 1 );
                    updateResources( );
                    ( (AbstractTableModel) resourcesTable.getModel( ) ).fireTableDataChanged( );
                    resourcesTable.changeSelection( dataControl.getResourcesCount( ) - 1, 0, false, false );
                }
            }
        }
    }

    private class NewButtonListener implements ActionListener {

        public void actionPerformed( ActionEvent e ) {

            // XXX 8/5/2009: Revisado tool a�adir recursos (en todos los elementos)
            if( dataControl.addElement( Controller.RESOURCES, null ) ) {
                dataControl.setSelectedResources( dataControl.getResourcesCount( ) - 1 );
                updateResources( );
                ( (AbstractTableModel) resourcesTable.getModel( ) ).fireTableDataChanged( );
                resourcesTable.changeSelection( dataControl.getResourcesCount( ) - 1, 0, false, false );
            }
        }
    }

    public void updateResources( ) {

        dataControl.setSelectedResources( resourcesTable.getSelectedIndex( ) );
        //multipleImagePanel.loadImage( sceneDataControl.getPreviewBackground( ) );
        //multipleImagePanel.repaint( );
        updatePreview( );
        lookPanel.remove( resourcesPanel );
        int selectedIndex = resourcesPanel.getSelectedIndex( );
        resourcesPanel = new ResourcesPanel( dataControl.getResources( ).get( dataControl.getSelectedResources( ) ), selectedIndex );
        resourcesPanel.setPreviewUpdater( this );
        GridBagConstraints cLook = new GridBagConstraints( );
        cLook.gridy = 0;
        cLook.gridx = 0;
        cLook.gridwidth = 2;
        cLook.fill = GridBagConstraints.BOTH;
        cLook.weightx = 1;
        cLook.weighty = 0;
        lookPanel.add( resourcesPanel, cLook );

        //repaint();
        //tabPanel.repaint( );
        //lookPanel.repaint( );
        resourcesPanel.repaint( );
        resourcesPanel.updateUI( );
    }

    public void updateResources( int group ) {

        this.selectedResourceGroup = group;
        updatePreview( );
    }

    public boolean updateFields( ) {

        resourcesTable.setSelectedIndex( dataControl.getSelectedResources( ) );
        updateResources( );
        resourcesTable.resetModel( );
        resourcesTable.setSelectedIndex( dataControl.getSelectedResources( ) );
        return true;
    }
}
