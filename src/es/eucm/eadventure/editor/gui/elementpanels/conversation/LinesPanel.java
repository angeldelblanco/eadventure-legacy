/*******************************************************************************
 * <e-Adventure> (formerly <e-Game>) is a research project of the <e-UCM>
 *         research group.
 *  
 *   Copyright 2005-2010 <e-UCM> research group.
 * 
 *   You can access a list of all the contributors to <e-Adventure> at:
 *         http://e-adventure.e-ucm.es/contributors
 * 
 *   <e-UCM> is a research group of the Department of Software Engineering
 *         and Artificial Intelligence at the Complutense University of Madrid
 *         (School of Computer Science).
 * 
 *         C Profesor Jose Garcia Santesmases sn,
 *         28040 Madrid (Madrid), Spain.
 * 
 *         For more info please visit:  <http://e-adventure.e-ucm.es> or
 *         <http://www.e-ucm.es>
 * 
 * ****************************************************************************
 * 
 * This file is part of <e-Adventure>, version 1.2.
 * 
 *     <e-Adventure> is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     <e-Adventure> is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with <e-Adventure>.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.eucm.eadventure.editor.gui.elementpanels.conversation;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.conversation.node.ConversationNodeView;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.Searchable;
import es.eucm.eadventure.editor.control.controllers.conversation.ConversationDataControl;
import es.eucm.eadventure.editor.control.controllers.conversation.GraphConversationDataControl;
import es.eucm.eadventure.editor.gui.DataControlsPanel;
import es.eucm.eadventure.editor.gui.auxiliar.components.JFiller;
import es.eucm.eadventure.editor.gui.elementpanels.general.TableScrollPane;

/**
 * This class is the panel used to display and edit nodes. It holds node
 * operations, like adding and removing lines, editing end effects, remove links
 * and reposition lines and children
 */
class LinesPanel extends JPanel implements DataControlsPanel {
  
    /**
     * Required
     */
    private static final long serialVersionUID = 1L;

    /**
     * Conversation data controller.
     */
    private ConversationDataControl conversationDataControl;

    /**
     * Reference to the principal panel
     */
    private ConversationEditionPanel conversationPanel;

    /**
     * Border of the panel
     */
    private TitledBorder border;

    /**
     * Table in which the node lines are represented
     */
    private LinesTable lineTable;

    /**
     * Scroll panel that holds the table
     */
    private TableScrollPane tableScrollPanel;

    /**
     * Move line up ( /\ ) button
     */
    private JButton moveLineUpButton;

    /**
     * Move line down ( \/ ) button
     */
    private JButton moveLineDownButton;

    /**
     * "Insert line" button
     */
    private JButton insertLineButton;

    /**
     * "Delete line" button
     */
    private JButton deleteLineButton;

    /**
     * "Delete option" button
     */
    private JButton deleteOptionButton;

    private JButton insertOptionButton;

    /**
     * Select the player or any exists character to speak selected conversation
     * line
     */
    //private JComboBox charactersComboBox; 
    /* Methods */

    /**
     * Constructor
     * 
     * @param conversationEditionPanel
     *            Link to the principal panel, for sending signals
     * @param conversationDataControl
     *            Data controller to edit the lines
     */
    public LinesPanel( ConversationEditionPanel conversationEditionPanel, ConversationDataControl conversationDataControl ) {

        // Set the initial values
        this.conversationPanel = conversationEditionPanel;
        this.conversationDataControl = conversationDataControl;

        // Create and set border (titled border in this case)
        border = BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ), TC.get( "LinesPanel.NoNodeSelected" ), TitledBorder.CENTER, TitledBorder.TOP );
        setBorder( border );

        // Set a GridBagLayout
        setLayout( new GridBagLayout( ) );

        /* Common elements (for Node and Option panels) */
        lineTable = new LinesTable( conversationDataControl, this );
        lineTable.getSelectionModel( ).addListSelectionListener( new NodeTableSelectionListener( ) );

        // Table scrollPane
        tableScrollPanel = new TableScrollPane( lineTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );

        // Create the set of values for the characters

        // Up and down buttons
        moveLineUpButton = new JButton( new ImageIcon( "img/icons/moveNodeUp.png" ) );
        moveLineUpButton.setContentAreaFilled( false );
        moveLineUpButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        moveLineUpButton.setBorder( BorderFactory.createEmptyBorder( ) );
        moveLineUpButton.setFocusable( false );
        moveLineUpButton.setToolTipText( TC.get( "Conversations.MoveLineUp" ) );
        moveLineUpButton.addActionListener( new ListenerButtonMoveLineUp( ) );

        moveLineDownButton = new JButton( new ImageIcon( "img/icons/moveNodeDown.png" ) );
        moveLineDownButton.setContentAreaFilled( false );
        moveLineDownButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        moveLineDownButton.setBorder( BorderFactory.createEmptyBorder( ) );
        moveLineDownButton.setFocusable( false );
        moveLineDownButton.setToolTipText( TC.get( "Conversations.MoveLineDown" ) );
        moveLineDownButton.addActionListener( new ListenerButtonMoveLineDown( ) );

        /* End of common elements */

        /* Dialogue panel elements */
        insertLineButton = new JButton( new ImageIcon( "img/icons/addNode.png" ) );
        insertLineButton.setContentAreaFilled( false );
        insertLineButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        insertLineButton.setBorder( BorderFactory.createEmptyBorder( ) );
        insertLineButton.setFocusable( false );
        insertLineButton.setToolTipText( TC.get( "Conversations.InsertLine" ) );
        insertLineButton.addActionListener( new ListenerButtonInsertLine( ) );
        deleteLineButton = new JButton( new ImageIcon( "img/icons/deleteNode.png" ) );
        deleteLineButton.setContentAreaFilled( false );
        deleteLineButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        deleteLineButton.setBorder( BorderFactory.createEmptyBorder( ) );
        deleteLineButton.setToolTipText( TC.get( "Conversations.DeleteLine" ) );
        deleteLineButton.addActionListener( new ListenerButtonDeleteLine( ) );
        /* End of dialogue panel elements */

        /* Option panel elements */
        deleteOptionButton = new JButton( new ImageIcon( "img/icons/deleteNode.png" ) );
        deleteOptionButton.setContentAreaFilled( false );
        deleteOptionButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        deleteOptionButton.setBorder( BorderFactory.createEmptyBorder( ) );
        deleteOptionButton.setToolTipText( TC.get( "Conversations.DeleteOption" ) );
        deleteOptionButton.addActionListener( new ListenerButtonDeleteOption( ) );
        insertOptionButton = new JButton( new ImageIcon( "img/icons/addNode.png" ) );
        insertOptionButton.setContentAreaFilled( false );
        insertOptionButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        insertOptionButton.setBorder( BorderFactory.createEmptyBorder( ) );
        insertOptionButton.setFocusable( false );
        insertOptionButton.setToolTipText( TC.get( "Conversations.InsertLine" ) );
        insertOptionButton.addActionListener( new ListenerButtonInsertOption( ) );
        /* End of option panel elements */
    }

    /**
     * Removes all elements in the panel, and sets a dialogue node panel
     */
    private void setDialoguePanel( ) {

        removeAll( );
        moveLineUpButton.setEnabled( false );
        moveLineDownButton.setEnabled( false );
        deleteLineButton.setEnabled( false );

        JPanel buttonsPanel = new JPanel( );
        buttonsPanel.setLayout( new GridBagLayout( ) );
        GridBagConstraints c = new GridBagConstraints( );
        c.gridx = 0;
        c.gridy = 0;
        buttonsPanel.add( insertLineButton, c );
        c.gridy = 1;
        buttonsPanel.add( moveLineUpButton, c );
        c.gridy = 2;
        buttonsPanel.add( moveLineDownButton, c );
        c.gridy = 4;
        buttonsPanel.add( deleteLineButton, c );
        c.gridy = 3;
        c.weighty = 2.0;
        c.fill = GridBagConstraints.VERTICAL;
        buttonsPanel.add( new JFiller( ), c );

       
        setLayout( new BorderLayout( ) );
        
        // add "keepshowing" option only if user doesn't select "keepShowing" option in adventure data panel
        if (!Controller.getInstance( ).isKeepShowing( )){
            JCheckBox waitUserInteraction = new JCheckBox( TC.get( "Conversation.WaitUserInteraction" ), conversationDataControl.isKeepShowingDialogueActivate( conversationPanel.getSelectedNode( ) ) );
            waitUserInteraction.addActionListener( new ActionListener( ) {

                public void actionPerformed( ActionEvent arg0 ) {

                    conversationDataControl.setKeepShowingDialogueOptions( conversationPanel.getSelectedNode( ) );
                }
            } );
            add(waitUserInteraction, BorderLayout.NORTH );
        }
        add( buttonsPanel, BorderLayout.EAST );
        add( tableScrollPanel, BorderLayout.CENTER );
    }

    /**
     * Removes all elements in the panel,and sets a option node panel
     */
    private void setOptionPanel( ) {

        // Remove all elements
        removeAll( );

        // Disable all buttons
        moveLineUpButton.setEnabled( false );
        moveLineDownButton.setEnabled( false );
        deleteOptionButton.setEnabled( false );

        JPanel buttonsPanel = new JPanel( );
        buttonsPanel.setLayout( new GridBagLayout( ) );
        GridBagConstraints c = new GridBagConstraints( );
        c.gridx = 0;
        c.gridy = 0;
        buttonsPanel.add( insertOptionButton, c );
        c.gridy++;
        buttonsPanel.add( deleteOptionButton, c );
        c.anchor = GridBagConstraints.SOUTH;
        c.gridy++;
        buttonsPanel.add( moveLineUpButton, c );
        c.gridy++;
        buttonsPanel.add( moveLineDownButton, c );

        JCheckBox randomOrder = new JCheckBox( TC.get( "Conversation.OptionRandomly" ), conversationDataControl.isRandomActivate( conversationPanel.getSelectedNode( ) ) );
        randomOrder.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent arg0 ) {

                conversationDataControl.setRandomlyOptions( conversationPanel.getSelectedNode( ) );
            }
        } );

        JCheckBox showUserResponse = new JCheckBox( TC.get( "Conversation.ShowUserOption" ), conversationDataControl.isShowUserOptionActivate( conversationPanel.getSelectedNode( ) ) );
        showUserResponse.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent arg0 ) {

                conversationDataControl.setShowUserOptionOptions( conversationPanel.getSelectedNode( ) );
            }
        } );
        
   
        JCheckBox preListening = new JCheckBox( TC.get( "Conversation.PreListening" ),  conversationDataControl.isPreListeningActivate( conversationPanel.getSelectedNode( ) )  );
        preListening.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent arg0 ) {
            
                conversationDataControl.setPreListeningOptions( conversationPanel.getSelectedNode( ) );
            }
        } );  
         
        JPanel checkboxPanel = new JPanel();
       // checkboxPanel.setLayout( new BorderLayout( ) );
        checkboxPanel.add( randomOrder);
        
        // add "keepshowing" option only if user doesn't select "keepShowing" option in adventure data panel
        if (!Controller.getInstance( ).isKeepShowing( )){
            JCheckBox keepQuestionShowing = new JCheckBox( TC.get( "Conversation.KeepShowing" ), conversationDataControl.isKeepShowingOptionsNodeActivate( conversationPanel.getSelectedNode( ) ) );
            keepQuestionShowing.addActionListener( new ActionListener( ) {

                public void actionPerformed( ActionEvent arg0 ) {
                
                    conversationDataControl.setKeepShowingOptionNodeOptions( conversationPanel.getSelectedNode( ) );
                }
            } );  
            checkboxPanel.add( keepQuestionShowing);
        }

        checkboxPanel.add( showUserResponse);
        checkboxPanel.add( preListening);
        setLayout( new BorderLayout( ) );
        add( buttonsPanel, BorderLayout.EAST );
        add( tableScrollPanel, BorderLayout.CENTER );
        add( checkboxPanel, BorderLayout.NORTH );
    }
    
    public ConversationNodeView getSelectedNode(){
        return conversationPanel.getSelectedNode( );
    }
    /**
     * Called when a new node has been selected, it is taken from the principal
     * panel
     */
    public void newSelectedNode( ) {

        // Take the selected node from the principal panel
        ConversationNodeView selectedNode = conversationPanel.getSelectedNode( );

        // Set the new model for the table
        lineTable.newSelectedNode( selectedNode );

        // If no node has been selected, remove all elements and change the panel title
        if( selectedNode == null ) {
            border.setTitle( TC.get( "LinesPanel.NoNodeSelected" ) );
            removeAll( );
            conversationPanel.getMenuPanel( ).setVisible( false );
        }

        // If a dialogue node has been selected, set the dialogue node panel and change the panel title
        else if( selectedNode.getType( ) == ConversationNodeView.DIALOGUE ) {
            border.setTitle( TC.get( "LinesPanel.DialogueNode" ) );
            conversationPanel.getMenuPanel( ).setDialoguePanel( );
            setDialoguePanel( );
        }

        // If a option node has been selected, set the option node panel and change the panel title
        else if( selectedNode.getType( ) == ConversationNodeView.OPTION ) {
            border.setTitle( TC.get( "LinesPanel.OptionNode" ) );
            conversationPanel.getMenuPanel( ).setOptionPanel( );
            setOptionPanel( );
        }

        // Repaint the panel
        repaint( );
    }

    /**
     * Reload options at the menu, enabling, disabling and selecting them
     */
    public void reloadOptions( ) {

        lineTable.revalidate( );
    }

    /**
     * Listener for the move line up button
     */
    private class ListenerButtonMoveLineUp implements ActionListener {

        public void actionPerformed( ActionEvent e ) {

            // Take the selected row, and the selected node
            int selectedRow = lineTable.getSelectedRow( );
            ConversationNodeView selectedNode = conversationPanel.getSelectedNode( );

            // If the line was moved
            if( conversationDataControl.moveNodeLineUp( selectedNode, selectedRow ) ) {

                // Move the selection along with the line
                lineTable.setRowSelectionInterval( selectedRow - 1, selectedRow - 1 );
                lineTable.scrollRectToVisible( lineTable.getCellRect( selectedRow - 1, 0, true ) );

                // Update the view if the node is an option node
                if( selectedNode.getType( ) == ConversationNodeView.OPTION )
                    conversationPanel.changePerformedInNode( );
            }
        }
    }

    /**
     * Listener for the move line down button
     */
    private class ListenerButtonMoveLineDown implements ActionListener {

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed( ActionEvent e ) {

            // Take the selected row, and the selected node
            int selectedRow = lineTable.getSelectedRow( );
            ConversationNodeView selectedNode = conversationPanel.getSelectedNode( );

            // If the line was moved
            if( conversationDataControl.moveNodeLineDown( selectedNode, selectedRow ) ) {

                // Move the selection along with the line
                lineTable.setRowSelectionInterval( selectedRow + 1, selectedRow + 1 );
                lineTable.scrollRectToVisible( lineTable.getCellRect( selectedRow + 1, 0, true ) );

                // Update the view if the node is an option node
                if( selectedNode.getType( ) == ConversationNodeView.OPTION )
                    conversationPanel.changePerformedInNode( );
            }
        }
    }

    /**
     * Listener for the "Insert line" button
     */
    private class ListenerButtonInsertLine implements ActionListener {

        public void actionPerformed( ActionEvent e ) {

            // Take the selected row, and the selected node
            int selectedRow = lineTable.getSelectedRow( );
            ConversationNodeView selectedNode = conversationPanel.getSelectedNode( );

            // Set the default name for the new line
            String name = ConversationLine.PLAYER;

            // If no row is selected, set the insertion position at the end
            if( selectedRow == -1 )
                selectedRow = lineTable.getRowCount( ) - 1;

            // If there is a row selected, pick the name of the row (so the inserted row has the same name as the last
            // one)
            else
                name = selectedNode.getLineName( selectedRow );

            // Insert the dialogue line in the selected position
            conversationDataControl.addNodeLine( selectedNode, selectedRow + 1, name, ( (GraphConversationDataControl) conversationDataControl ).getAllConditions( ).get( selectedNode ) );

            // Select the inserted line
            lineTable.setRowSelectionInterval( selectedRow + 1, selectedRow + 1 );
            lineTable.scrollRectToVisible( lineTable.getCellRect( selectedRow + 1, 0, true ) );

            // Update the table
            lineTable.revalidate( );
        }
    }
    
   

    public void editNextLine( ) {

        final int selectedRow = lineTable.getSelectedRow( );
        if( selectedRow == lineTable.getRowCount( ) - 1 ) {
            ConversationNodeView selectedNode = conversationPanel.getSelectedNode( );
            if( selectedNode.getType( ) == ConversationNodeView.DIALOGUE ) {
                String name = selectedNode.getLineName( selectedRow );
                conversationDataControl.addNodeLine( selectedNode, selectedRow + 1, name, ( (GraphConversationDataControl) conversationDataControl ).getAllConditions( ).get( selectedNode ) );
            }
            else
                conversationDataControl.addChild( selectedNode, ConversationNodeView.DIALOGUE, ( (GraphConversationDataControl) conversationDataControl ).getAllConditions( ) );
        }
        //( (AbstractTableModel) lineTable.getModel( ) ).fireTableDataChanged( );
        lineTable.changeSelection( selectedRow + 1, selectedRow + 1, false, false );
        SwingUtilities.invokeLater( new Runnable( ) {

            public void run( ) {

                if( lineTable.editCellAt( selectedRow + 1, 1 ) )
                    ( (JScrollPane) lineTable.getEditorComponent( ) ).requestFocusInWindow( );
            }
        } );
    }

    /**
     * Listener for the "Insert line" button
     */
    private class ListenerButtonInsertOption implements ActionListener {

        public void actionPerformed( ActionEvent e ) {

            // Take the selected row, and the selected node
            int selectedRow = lineTable.getSelectedRow( );
            ConversationNodeView selectedNode = conversationPanel.getSelectedNode( );

            // If no row is selected, set the insertion position at the end
            if( selectedRow == -1 )
                selectedRow = lineTable.getRowCount( ) - 1;

            // Insert the dialogue line in the selected position
            conversationDataControl.addChild( selectedNode, ConversationNodeView.DIALOGUE, ( (GraphConversationDataControl) conversationDataControl ).getAllConditions( ) );

            // Update the conversation panel and reload the options
            conversationPanel.reloadOptions( );
            conversationPanel.updateRepresentation( );

            // Select the inserted line
            lineTable.setRowSelectionInterval( selectedRow + 1, selectedRow + 1 );
            lineTable.scrollRectToVisible( lineTable.getCellRect( selectedRow + 1, 0, true ) );

            // Update the table
            lineTable.revalidate( );
        }
    }

    /**
     * Listener for the "Delete line" button
     */
    private class ListenerButtonDeleteLine implements ActionListener {

        public void actionPerformed( ActionEvent e ) {

            // Take the selected row, and the selected node
            int selectedRow = lineTable.getSelectedRow( );
            ConversationNodeView selectedNode = conversationPanel.getSelectedNode( );

            // Delete the selected line
            conversationDataControl.deleteNodeLine( selectedNode, selectedRow, ( (GraphConversationDataControl) conversationDataControl ).getAllConditions( ).get( selectedNode ) );

            // If there are no more lines, clear selection (this disables the "Delete line" button)
            if( selectedNode.getLineCount( ) == 0 ) {
                lineTable.clearSelection( );
                tableScrollPanel.updateUI( );
            }

            // If the deleted line was the last one, select the new last line in the node
            else if( selectedNode.getLineCount( ) == selectedRow )
                lineTable.setRowSelectionInterval( selectedRow - 1, selectedRow - 1 );

            // Update the table
            lineTable.revalidate( );
        }
    }

    /**
     * Listener for the "Delete option" button
     */
    private class ListenerButtonDeleteOption implements ActionListener {

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed( ActionEvent e ) {

            // Take the selected row, and the selected node
            int selectedRow = lineTable.getSelectedRow( );
            ConversationNodeView selectedNode = conversationPanel.getSelectedNode( );

            // If the option was successfully deleted
            if( conversationDataControl.deleteNodeOption( selectedNode, selectedRow ) ) {

                // If there are no more children, clear selection (this disables the "Delete option" button)
                if( selectedNode.getChildCount( ) == 0 )
                    lineTable.clearSelection( );

                // If the deleted child was the last one, select the new last child in the node
                else if( selectedNode.getChildCount( ) == selectedRow )
                    lineTable.setRowSelectionInterval( selectedRow - 1, selectedRow - 1 );

                // If there is a selection in the table (there are more children) select the children
                if( lineTable.getSelectedRow( ) >= 0 )
                    conversationPanel.setSelectedChild( conversationPanel.getSelectedNode( ).getChildView( lineTable.getSelectedRow( ) ) );
                // If there is no selection delete the children
                else
                    conversationPanel.setSelectedChild( null );

                // Update the view and the table
                conversationPanel.changePerformedInNode( );
                lineTable.revalidate( );
            }
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
                moveLineUpButton.setEnabled( false );
                moveLineDownButton.setEnabled( false );
                deleteOptionButton.setEnabled( false );
                deleteLineButton.setEnabled( false );
            }

            // If there is a line selected
            else {
                // Enable all options
                moveLineUpButton.setEnabled( true );
                moveLineDownButton.setEnabled( true );
                deleteOptionButton.setEnabled( true );
                deleteLineButton.setEnabled( true );

                // If the node is an option node
                if( conversationPanel.getSelectedNode( ).getType( ) == ConversationNodeView.OPTION ) {
                    int selectedRow = lsm.getMinSelectionIndex( );

                    // Take the selected child (at the same position of the selected line) and set it in the principal
                    // panel
                    // so it become painted in the conversational panel
                    conversationPanel.updateRepresentation( );
                    ConversationNodeView selectedChild = conversationPanel.getSelectedNode( ).getChildView( selectedRow );
                    conversationPanel.setSelectedChild( selectedChild );
                }
            }
        }
    }

    public void setSelectedItem( List<Searchable> path ) {

        if( path.size( ) > 0 && path.get( path.size( ) - 1 ) instanceof es.eucm.eadventure.editor.control.controllers.conversation.ConversationLine ) {
            es.eucm.eadventure.editor.control.controllers.conversation.ConversationLine line = (es.eucm.eadventure.editor.control.controllers.conversation.ConversationLine) path.get( path.size( ) - 1 );
            lineTable.changeSelection( line.getLine( ), 0, false, false );
        }
    }

    
    public LinesTable getLineTable( ) {
    
        return lineTable;
    }

}
