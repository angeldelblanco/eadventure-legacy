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
package es.eucm.eadventure.editor.gui.elementpanels.conversation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import es.eucm.eadventure.common.data.chapter.conversation.node.ConversationNodeView;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.controllers.conversation.ConversationDataControl;
import es.eucm.eadventure.editor.gui.displaydialogs.ConversationDialog;
import es.eucm.eadventure.editor.gui.elementpanels.conversation.representation.GraphicRepresentation;

public class ConversationPanelMouseListener implements MouseListener, MouseMotionListener {

    /**
     * Graphic representation of the conversation ADT (it can change with
     * different ADTs, such as tree, graphs...)
     */
    private GraphicRepresentation conversationRepresentation;

    /**
     * Conversation controller.
     */
    private ConversationDataControl conversationDataControl;

    /**
     * Link to the principal panel
     */
    private ConversationEditionPanel conversationPanel;

    private RepresentationPanel representationPanel;

    public ConversationPanelMouseListener( GraphicRepresentation conversationRepresentation, ConversationDataControl conversationDataControl, ConversationEditionPanel conversationPanel2, RepresentationPanel representationPanel ) {

        this.conversationRepresentation = conversationRepresentation;
        this.conversationDataControl = conversationDataControl;
        this.conversationPanel = conversationPanel2;
        this.representationPanel = representationPanel;
    }

    public void mouseClicked( MouseEvent e ) {

        // Takes the node in which the click has been made (null if none) and send it to the principal panel
        ConversationNodeView clickedNode = conversationRepresentation.getNodeInPosition( e.getPoint( ) );

        // If state is normal, select the clicked node
        if( representationPanel.getState( ) == RepresentationPanel.NORMAL ) {
            conversationPanel.setSelectedNode( clickedNode );

            // If some node was selected and the right button was clicked, show the menu
            if( clickedNode != null && e.getButton( ) == MouseEvent.BUTTON3 ) {
            }

            // If the right button was pressed, show a dialog with the "Preview conversation" option only
            else if( e.getButton( ) == MouseEvent.BUTTON3 ) {
                // Create the popup menu
                JPopupMenu nodePopupMenu = new JPopupMenu( );

                // Add the preview options
                JMenuItem previewConversationItem = new JMenuItem( TC.get( "Conversation.OptionPreviewConversation" ) );
                previewConversationItem.addActionListener( new PreviewConversationActionListener( true ) );
                nodePopupMenu.add( previewConversationItem );

                // Display the menu
                nodePopupMenu.show( e.getComponent( ), e.getX( ), e.getY( ) );
            }
        }

        // If we are waiting for a destination node to move the selected one
        else if( representationPanel.getState( ) == RepresentationPanel.WAITING_SECOND_NODE_TO_MOVE ) {
            if( clickedNode != null && conversationDataControl.moveNode( conversationPanel.getSelectedNode( ), clickedNode ) )
                representationPanel.updateRepresentation( );
            representationPanel.changeState( RepresentationPanel.NORMAL );
        }

        // If we are waiting for a destination node to link with the selected one
        else if( representationPanel.getState( ) == RepresentationPanel.WAITING_SECOND_NODE_TO_LINK ) {
            if( clickedNode != null && conversationDataControl.linkNode( conversationPanel.getSelectedNode( ), clickedNode ) ) {
                conversationPanel.reloadOptions( );
                representationPanel.updateRepresentation( );
            }
            representationPanel.changeState( RepresentationPanel.NORMAL );
        }
    }

    public void mousePressed( MouseEvent e ) {

        // Spread the call into the conversation representation
        conversationRepresentation.mousePressed( e.getPoint( ) );
    }

    public void mouseReleased( MouseEvent e ) {

        // Spread the call into the conversation representation
        if( conversationRepresentation.mouseReleased( ) ) {
            representationPanel.setPreferredSize( conversationRepresentation.getConversationSize( ) );
            conversationPanel.reloadScroll( );
            conversationPanel.repaint( );
        }
    }

    public void mouseDragged( MouseEvent e ) {

        // Spread the call into the conversation representation
        if( conversationRepresentation.mouseDragged( e.getPoint( ) ) )
            representationPanel.repaint( );

        int x = conversationPanel.getScrollXValue( );
        if( e.getPoint( ).x - x < 20 )
            conversationPanel.changeScrollX( -20 );
        if( e.getPoint( ).x - x > conversationPanel.getScrollSize( ).width - 30 )
            conversationPanel.changeScrollX( 20 );

        int y = conversationPanel.getScrollYValue( );
        if( e.getPoint( ).y - y < 20 )
            conversationPanel.changeScrollY( -20 );
        if( e.getPoint( ).y - y > conversationPanel.getScrollSize( ).height - 30 )
            conversationPanel.changeScrollY( 20 );

    }

    // Not implemented
    public void mouseEntered( MouseEvent e ) {

    }

    public void mouseExited( MouseEvent e ) {

    }

    public void mouseMoved( MouseEvent e ) {

    }

    /**
     * Listener for the "Preview conversation" and "Preview conversation from
     * this node" options
     */
    private class PreviewConversationActionListener implements ActionListener {

        /**
         * True if the conversation must be played from the root node, false
         * from the selected node.
         */
        private boolean completePreview;

        /**
         * Constructor.
         * 
         * @param completePreview
         *            True if the conversation must be played from the root
         *            node, false from the selected node.
         */
        public PreviewConversationActionListener( boolean completePreview ) {

            this.completePreview = completePreview;
        }

        public void actionPerformed( ActionEvent e ) {

            // If it is a complete preview, show the dialog for the root node
            if( completePreview )
                new ConversationDialog( conversationDataControl, conversationDataControl.getRootNode( ) );

            // If not, take the selected node
            else
                new ConversationDialog( conversationDataControl, conversationPanel.getSelectedNode( ) );
        }
    }
}
