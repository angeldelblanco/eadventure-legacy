package es.eucm.eadventure.engine.core.control.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalNPC;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalPlayer;
import es.eucm.eadventure.engine.core.control.functionaldata.functionaleffects.FunctionalEffect;
import es.eucm.eadventure.engine.core.control.functionaldata.functionaleffects.FunctionalEffects;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.conversation.node.ConversationNode;
import es.eucm.eadventure.common.data.chapter.conversation.node.OptionConversationNode;
import es.eucm.eadventure.engine.core.gui.GUI;

/**
 * A game main loop during a conversation
 */
public class GameStateConversation extends GameState {
    
    /**
     * Left most point of the response text block
     */
    private final int RESPONSE_TEXT_X;
    
    /**
     * Upper most point of the response text block
     */
    private final int RESPONSE_TEXT_Y;
    
    /**
     * Number of response lines to display
     */
    private final int RESPONSE_TEXT_NUMBER_LINES;
    
    /**
     * Height of the text of the responses
     */
    private final int RESPONSE_TEXT_HEIGHT;
    
    /**
     * Ascent of the text of the response
     */
    private final int RESPONSE_TEXT_ASCENT;
    
    /**
     * Color of the normal response text
     */
    private static final Color RESPONSE_TEXT_NORMAL = Color.YELLOW;
    
    /**
     * Color of the highlighted response text
     */
    private static final Color RESPONSE_TEXT_HIGHLIGHTED = Color.RED;
    
    /**
     * Color of the border of the response text
     */
    private static final Color RESPONSE_TEXT_BORDER = Color.BLACK;
    
    /**
     * Constant for no mouse button clicked
     */
    private static final int MOUSE_BUTTON_NONE = 0;
    
    /**
     * Constant for left mouse button clicked
     */
    private static final int MOUSE_BUTTON_LEFT = 1;
    
    /**
     * Constant for right mouse button clicked
     */
    private static final int MOUSE_BUTTON_RIGHT = 2;
    
    /**
     * Current conversational node being played
     */
    private ConversationNode currentNode;
    
    /**
     * Index of the line being played
     */
    private int currentLine;
    
    /**
     * Index of the first line displayed in an option node
     */
    private int firstLineDisplayed;
    
    /**
     * Index of the option currently highlighted
     */
    private int optionHighlighted;
    
    /**
     * Last mouse button pressed
     */
    private int mouseClickedButton = MOUSE_BUTTON_NONE;
    
    /**
     * Variable to control the access to doRandom()
     */
    private boolean firstTime;
    
    /**
     * Store the option selected to use it when it come back to the running effects Game State
     */
    private int optionSelected;
    
    /**
     * 
     */
    private boolean isOptionSelected;
    
    /**
     * TODO modificar este comentario
     * utilizamos una variable booleana que se activa cuando pinchamos fuera del espacio que esta destinado a las opciones
     * para indicar que no se debe seleccionar dicha opcion, y que se repinten las opciones en pantalla
     */
    private boolean selectOut;
    
    /**
     * Creates a new GameStateConversation
     */
    public GameStateConversation( ) {
        // Set the values
        RESPONSE_TEXT_X = GUI.getInstance( ).getResponseTextX( );
        RESPONSE_TEXT_Y = GUI.getInstance( ).getResponseTextY( );
        RESPONSE_TEXT_NUMBER_LINES = GUI.getInstance( ).getResponseTextNumberLines( );
        RESPONSE_TEXT_ASCENT = GUI.getInstance( ).getGraphics( ).getFontMetrics( ).getAscent( );
        RESPONSE_TEXT_HEIGHT = RESPONSE_TEXT_ASCENT + 2;
        
        // Set the initial node
        currentNode = game.getConversation( ).getRootNode( );
        currentLine = 0;
        firstLineDisplayed = 0;
        optionHighlighted = -1;
        
        isOptionSelected = false;
        selectOut=false;
        // Push a new element in the Stack of effects
        game.addToTheStack(new ArrayList<FunctionalEffect>());
       
    }

    /*
     * (non-Javadoc)
     * @see es.eucm.eadventure.engine.engine.control.gamestate.GameState#EvilLoop(long, int)
     */
    public synchronized void mainLoop( long elapsedTime, int fps ) {
        
        // Toggles off the HUD
        GUI.getInstance( ).toggleHud( false );   
        GUI.getInstance( ).setDefaultCursor( );
        
        game.getFunctionalScene( ).update( elapsedTime );
        GUI.getInstance( ).update( elapsedTime );
        
        Graphics2D g = GUI.getInstance( ).getGraphics( );
        g.clearRect( 0, 0, GUI.WINDOW_WIDTH, GUI.WINDOW_HEIGHT );
        
        // Draw the scene
        game.getFunctionalScene( ).draw( );
        GUI.getInstance().drawScene( g );
                
        //g.setColor( Color.WHITE );
        //g.drawString( Integer.toString( fps ), 780, 14 );

        // If the current node is a dialoge node
        if( currentNode.getType( ) == ConversationNode.DIALOGUE ) {
            
            // If no button as pressed, keep the characters speaking lines
            if( mouseClickedButton == MOUSE_BUTTON_NONE ) {
                if( game.getCharacterCurrentlyTalking( ) == null || 
                  ( game.getCharacterCurrentlyTalking( ) != null &&
                   !game.getCharacterCurrentlyTalking( ).isTalking( ) ) ) {
                    playNextLine( );
                }
            }
            
            // If a left click was made, jump the line
            else if( mouseClickedButton == MOUSE_BUTTON_LEFT ) {
                playNextLine( );
                mouseClickedButton = MOUSE_BUTTON_NONE;
            }
            
            // If a right click was made, jump to the last line
            else if( mouseClickedButton == MOUSE_BUTTON_RIGHT ) {
                currentLine = currentNode.getLineCount( );
                playNextLine( );
                mouseClickedButton = MOUSE_BUTTON_NONE;
            }
            // Set the variable to control access to doRandom()
            firstTime = true;
        }
        
        // If it is a node option, display the different options
        else if( currentNode.getType( ) == ConversationNode.OPTION ) {
        	if (!isOptionSelected/*||selectOut*/){
        	if (firstTime){
        		((OptionConversationNode)currentNode).doRandom();
        		firstTime = false;
        	}
            // If we can paint all the lines in screen, do it
            if( currentNode.getLineCount( ) <= RESPONSE_TEXT_NUMBER_LINES ) {
                for( int i = 0; i < currentNode.getLineCount( ); i++ ) {
                    Color textColor = ( i == optionHighlighted? RESPONSE_TEXT_HIGHLIGHTED : RESPONSE_TEXT_NORMAL );
                    GUI.drawStringOnto(
                            g,
                            (i+1) + ".- " + currentNode.getLine( i ).getText( ),
                            RESPONSE_TEXT_X,
                            RESPONSE_TEXT_Y + i * RESPONSE_TEXT_HEIGHT + RESPONSE_TEXT_ASCENT,
                            false,
                            textColor,
                            RESPONSE_TEXT_BORDER,
                            true
                    );
                }
            }
            
            // If the lines don't fit in the screen, decompose them
            else {
                int indexLastLine = Math.min( firstLineDisplayed + RESPONSE_TEXT_NUMBER_LINES - 1, currentNode.getLineCount( ) );
                int i;
                
                for( i = firstLineDisplayed; i < indexLastLine; i++ ) {
                    Color textColor = ( ( i - firstLineDisplayed ) == optionHighlighted? RESPONSE_TEXT_HIGHLIGHTED : RESPONSE_TEXT_NORMAL );
                    GUI.drawStringOnto(
                            g,
                            (i+1) + ".- " + currentNode.getLine( i ).getText( ),
                            RESPONSE_TEXT_X,
                            RESPONSE_TEXT_Y + ( i - firstLineDisplayed ) * RESPONSE_TEXT_HEIGHT + RESPONSE_TEXT_ASCENT,
                            false,
                            textColor,
                            RESPONSE_TEXT_BORDER,
                            true
                    );
                }
                
                Color textColor = ( ( i - firstLineDisplayed ) == optionHighlighted? RESPONSE_TEXT_HIGHLIGHTED : RESPONSE_TEXT_NORMAL );
                GUI.drawStringOnto(
                        g,
                        "More...",
                        RESPONSE_TEXT_X,
                        RESPONSE_TEXT_Y + ( i - firstLineDisplayed ) * RESPONSE_TEXT_HEIGHT + RESPONSE_TEXT_ASCENT,
                        false,
                        textColor,
                        RESPONSE_TEXT_BORDER,
                        true
                );
            }
            
            
            //TODO MODIFIED
            //if( currentNode.isTerminal( ) ) {
        }else if (isOptionSelected){
        	

            if( game.getCharacterCurrentlyTalking( ) == null || 
                    ( game.getCharacterCurrentlyTalking( ) != null &&
                     !game.getCharacterCurrentlyTalking( ).isTalking( ) ) ) {
            			
        	
        	
        	if( currentNode.hasValidEffect( )&&!currentNode.isEffectConsumed( ) ) {
                currentNode.consumeEffect( );
                // Store the state in the stack. Later trigger the effects, 
                // it must come back to this conversation and continue it
                game.pushCurrentState(this);
                
                FunctionalEffects.storeAllEffects(currentNode.getEffects( ));
                GUI.getInstance().toggleHud( true );
                
            }
            
            else if ((!currentNode.hasValidEffect( ) || currentNode.isEffectConsumed( ) ) && currentNode.isTerminal( )){
                // Reset effects in nodes
                for (ConversationNode node: game.getConversation( ).getAllNodes( )){
                    node.resetEffect( );
                }
                GUI.getInstance().toggleHud( true );
                
                //The conversation has finished, pop its effects queue 
                game.endConversation();
                
                //FIXME esta l�nea la incluimos en el metodo game.endConversation()
                //game.setState ( Game.STATE_PLAYING);
            }
            
            //TODO MODIFIED: Antes no estaba el else if (era solo else)
            else if (!currentNode.isTerminal( )){
            	//TODO
            	
            	currentNode = currentNode.getChild( optionSelected );
                isOptionSelected = false;
                
            	
               // firstLineDisplayed = 0;
               // currentLine = 0;
            }
            
        }
     
        }
        }
        
        // Paint the FPS
        //g.setColor( Color.WHITE );
        //g.drawString( Integer.toString( fps ), 780, 0 );

        GUI.getInstance( ).endDraw( );
        g.dispose( );
    }

    @Override
    public synchronized void mouseClicked( MouseEvent e ) {
        if( currentNode.getType( ) == ConversationNode.OPTION && RESPONSE_TEXT_Y <= e.getY( ) && RESPONSE_TEXT_Y + currentNode.getLineCount() * RESPONSE_TEXT_HEIGHT + RESPONSE_TEXT_ASCENT >= e.getY()) {
            optionSelected = ( e.getY( ) - RESPONSE_TEXT_Y ) / RESPONSE_TEXT_HEIGHT;
           isOptionSelected = true;
            // If all the lines are in the screen, select normally
            if( currentNode.getLineCount( ) <= RESPONSE_TEXT_NUMBER_LINES ) {
                if( optionSelected < currentNode.getLineCount( ) ) {
                    if( game.getCharacterCurrentlyTalking( ) != null && game.getCharacterCurrentlyTalking( ).isTalking( ) )
                        game.getCharacterCurrentlyTalking( ).stopTalking( );

                    FunctionalPlayer player = game.getFunctionalPlayer( );
                    
                    //Add sound to options node
                    ConversationLine line = currentNode.getLine( optionSelected );
                    if (line.isValidAudio( ))
                        player.speak( line.getText(), line.getAudioPath( ) );
                    else //TODO aki tenemos que comprobar si hay que leer con TTS!!!!!!!
                    	/*
                    	 * 
                    	 */
                        player.speak( line.getText( ) );
                 

                    //player.speak( currentNode.getLine( optionSelected ).getText( ) );
                    game.setCharacterCurrentlyTalking( player );
                    
                 
                    //currentNode = currentNode.getChild( optionSelected );
                }
            }
            
            // If there are more lines
            else {
                optionSelected += firstLineDisplayed;
                
                int indexLastLine = Math.min( firstLineDisplayed + RESPONSE_TEXT_NUMBER_LINES - 1, currentNode.getLineCount( ) );
                
                if( optionSelected == indexLastLine ) {
                    firstLineDisplayed += RESPONSE_TEXT_NUMBER_LINES - 1;
                    if( firstLineDisplayed >= currentNode.getLineCount( ) )
                        firstLineDisplayed = 0;
                }
                
                else if( optionSelected < currentNode.getLineCount( ) ) {
                    if( game.getCharacterCurrentlyTalking( ) != null && game.getCharacterCurrentlyTalking( ).isTalking( ) )
                        game.getCharacterCurrentlyTalking( ).stopTalking( );

                    FunctionalPlayer player = game.getFunctionalPlayer( );
                    
                    //Add sound to options node
                    ConversationLine line = currentNode.getLine( optionSelected );
                    if (line.isValidAudio( ))
                        player.speak( line.getText(), line.getAudioPath( ) );
                    else//TODO aki hay que ver si lee de TTS
                    	/*
                    	 * 
                    	 */
                        player.speak( line.getText( ) );
                    //player.speak( currentNode.getLine( optionSelected ).getText( ) );
                    game.setCharacterCurrentlyTalking( player );

                    //currentNode = currentNode.getChild( optionSelected );
                }
            }
       }
        
        // If it is a dialogue node, keep the event
        else if( currentNode.getType( ) == ConversationNode.DIALOGUE ) {
            if( e.getButton() == MouseEvent.BUTTON1 ) {
                mouseClickedButton = MOUSE_BUTTON_LEFT;
            }
            
            else if( e.getButton() == MouseEvent.BUTTON3 ) {
                mouseClickedButton = MOUSE_BUTTON_RIGHT;
            }            
        }
    }

    @Override
    public void mouseMoved( MouseEvent e ) {
        if( RESPONSE_TEXT_Y <= e.getY( ) )
            optionHighlighted = ( e.getY( ) - RESPONSE_TEXT_Y ) / RESPONSE_TEXT_HEIGHT;
        else
            optionHighlighted = -1;
    }
    
    /**
     * Jumps to the next conversation line. If the current line was the last,
     * end the conversation and trigger the efects or jump to the next node
     */
    private void playNextLine( ) {
        if( game.getCharacterCurrentlyTalking( ) != null && game.getCharacterCurrentlyTalking( ).isTalking( ) )
            game.getCharacterCurrentlyTalking( ).stopTalking();
        
        if( currentLine < currentNode.getLineCount( ) ) {
            ConversationLine line = currentNode.getLine( currentLine );
            if( line.isPlayerLine( ) ) {
                FunctionalPlayer player = game.getFunctionalPlayer( );
                if (line.isValidAudio( ))
                    player.speak( line.getText(), line.getAudioPath( ) );
                else //TODO meter speakTTS
                    player.speak( line.getText( ) );
                game.setCharacterCurrentlyTalking( player );
            } else {
                FunctionalNPC npc = null;
                // TODO Revisar, la notacion del NPC no es buena
                if( line.getName().equals("NPC") )
                    npc = game.getCurrentNPC( );
                else
                    npc = game.getFunctionalScene( ).getNPC( line.getName( ) );
                
                if( npc != null ) {
                    if (line.isValidAudio( )){
                        npc.speak( line.getText( ), line.getAudioPath( ) );
                    }else //TODO meter speakTTS
                        npc.speak( line.getText( ) );
                    game.setCharacterCurrentlyTalking( npc );
                }
            }
            currentLine++;
        }
        
        else {
            
            //TODO MODIFIED
            //if( currentNode.isTerminal( ) ) {
            if( currentNode.hasValidEffect( )&&!currentNode.isEffectConsumed( ) ) {
                currentNode.consumeEffect( );
                // Store the state in the stack. Later trigger the effects, 
                // it must come back to this conversation and continue it
                game.pushCurrentState(this);
                
                FunctionalEffects.storeAllEffects(currentNode.getEffects( ));
                GUI.getInstance().toggleHud( true );
                
                //if (currentNode.isTerminal( ))
                //    game.setState( Game.STATE_RUN_EFFECTS );
                //else
                   // game.setState( Game.STATE_RUN_EFFECTS_FROM_CONVERSATION );
            }
            
            else if ((!currentNode.hasValidEffect( ) || currentNode.isEffectConsumed( ) ) && currentNode.isTerminal( )){
                // Reset effects in nodes
                for (ConversationNode node: game.getConversation( ).getAllNodes( )){
                    node.resetEffect( );
                }
                GUI.getInstance().toggleHud( true );
                //FIXME comprobar que esta bien
                //The conversation has finished, pop its effects queue 
                game.endConversation();
                
                //FIXME esta l�nea la incluimos en el metodo game.endConversation()
                //game.setState ( Game.STATE_PLAYING);
            }
            
            //TODO MODIFIED: Antes no estaba el else if (era solo else)
            else if (!currentNode.isTerminal( )){
                currentNode = currentNode.getChild( 0 );
                firstLineDisplayed = 0;
                currentLine = 0;
            }
        }
    }
}
