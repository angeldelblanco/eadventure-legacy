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
package es.eucm.eadventure.common.data.chapter.conversation.node;

import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.effects.Effects;

/**
 * Abstract class that comprises all the possible nodes for a conversation.
 * Initially, two classes implement this interface: DialogueNode and OptionNode
 */
public abstract class ConversationNode implements ConversationNodeView {

    private int xEditor;
    
    private int yEditor;
    
    public ConversationNode(){
        this.xEditor = -1;
        this.yEditor = -1;
    }
    
    /**
     * Returns the child in the specified position
     * 
     * @param index
     *            Index for extraction
     * @return The child conversation node selected
     */
    public abstract ConversationNode getChild( int index );

    /*
     * (non-Javadoc)
     * 
     * @see es.eucm.eadventure.common.data.chapterdata.conversation.node.ConversationNodeView#getChildView(int)
     */
    public ConversationNodeView getChildView( int index ) {

        return getChild( index );
    }

    /**
     * Adds a new child to the node, in the last position
     * 
     * @param child
     *            Node for insertion
     */
    public abstract void addChild( ConversationNode child );

    /**
     * Adds a new child to the node, in the specified position
     * 
     * @param index
     *            Index for insertion
     * @param child
     *            Node for insertion
     */
    public abstract void addChild( int index, ConversationNode child );

    /**
     * Removes the child in the specified position
     * 
     * @param index
     *            Index for removal
     * @return Reference to the removed child
     */
    public abstract ConversationNode removeChild( int index );

    /**
     * Returns the line in the specified position.
     * 
     * @param index
     *            Index for extraction
     * @return The conversation line selected
     */
    public abstract ConversationLine getLine( int index );

    /*
     * (non-Javadoc)
     * 
     * @see es.eucm.eadventure.common.data.chapterdata.conversation.node.ConversationNodeView#isPlayerLine(int)
     */
    public boolean isPlayerLine( int index ) {

        return getLine( index ).isPlayerLine( );
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.eucm.eadventure.common.data.chapterdata.conversation.node.ConversationNodeView#getLineName(int)
     */
    public String getLineName( int index ) {

        return getLine( index ).getName( );
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.eucm.eadventure.common.data.chapterdata.conversation.node.ConversationNodeView#getLineText(int)
     */
    public String getLineText( int index ) {

        return getLine( index ).getText( );
    }

    public String getAudioPath( int index ) {

        return getLine( index ).getAudioPath( );
    }

    public boolean hasAudioPath( int index ) {

        return getLine( index ).isValidAudio( );
    }

    /**
     * Adds a new line to the node, in the last position
     * 
     * @param line
     *            Line for insertion
     */
    public abstract void addLine( ConversationLine line );

    /**
     * Adds a new line to the node, in the specified position
     * 
     * @param index
     *            Index for insertion
     * @param line
     *            Line for insertion
     */
    public abstract void addLine( int index, ConversationLine line );

    /**
     * Removes the line in the specified position
     * 
     * @param index
     *            Index for removal
     * @return Reference to the removed line
     */
    public abstract ConversationLine removeLine( int index );

    /**
     * Sets the effects triggered when the conversation is finished (only
     * terminal nodes accept effects)
     * 
     * @param effects
     *            New effects
     */
    public abstract void setEffects( Effects effects );

    /**
     * Returns the effect triggered when the conversation is finished
     * 
     * @return The effect held by the node if it is terminal, null otherwise
     */
    public abstract Effects getEffects( );

    /**
     * Returns if the node has a valid effect set
     * 
     * @return True if the node has an effect (even if empty), false otherwise
     */
    public abstract boolean hasValidEffect( );

    public abstract void consumeEffect( );

    public abstract void resetEffect( );

    public abstract boolean isEffectConsumed( );

    /**
     * Set the voice for synthesize the specified line
     * 
     */
    public void setSynthesizerVoice( boolean synthesize, int line ) {

        getLine( line ).setSynthesizerVoice( synthesize );
    }

    /**
     * Get the voice for the specified line
     */
    public boolean getSynthesizerVoice( int line ) {

        return getLine( line ).getSynthesizerVoice( );
    }

    /**
     * This method is only used in OptionConversationNode. Make the options to
     * appear randomly
     */
    //public abstract void doRandom();
    @Override
    public Object clone( ) throws CloneNotSupportedException {

        ConversationNode cn = (ConversationNode) super.clone( );
        return cn;
    }

    
    public int getEditorX( ) {
    
        return xEditor;
    }

    
    public void setEditorX( int xEditor ) {
    
        this.xEditor = xEditor;
    }

    
    public int getEditorY( ) {
    
        return yEditor;
    }

    
    public void setEditorY( int yEditor ) {
    
        this.yEditor = yEditor;
    }

}
