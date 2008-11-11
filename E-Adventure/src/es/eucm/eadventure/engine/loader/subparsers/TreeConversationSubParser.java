package es.eucm.eadventure.engine.loader.subparsers;

import java.util.Vector;

import org.xml.sax.Attributes;

import es.eucm.eadventure.engine.core.control.functionaldata.functionaleffects.Effects;
import es.eucm.eadventure.engine.core.data.gamedata.GameData;
import es.eucm.eadventure.engine.core.data.gamedata.conversation.Conversation;
import es.eucm.eadventure.engine.core.data.gamedata.conversation.node.DialogueNode;
import es.eucm.eadventure.engine.core.data.gamedata.conversation.node.Node;
import es.eucm.eadventure.engine.core.data.gamedata.conversation.node.OptionNode;
import es.eucm.eadventure.engine.core.data.gamedata.conversation.util.ConversationLine;

/**
 * Class to subparse tree conversations
 */
public class TreeConversationSubParser extends SubParser {
    
    /* Attributes */
    
    /**
     * Constant for subparsing nothing
     */
    private static final int SUBPARSING_NONE = 0;
    
    /**
     * Constant for subparsing effect tag
     */
    private static final int SUBPARSING_EFFECT = 1;
    
    /**
     * Normal state
     */
    private static final int STATE_NORMAL = 0;

    /**
     * Waiting for an option state
     */
    private static final int STATE_WAITING_OPTION = 1;

    /**
     * Stores the current element being subparsed
     */
    private int subParsing = SUBPARSING_NONE;
    
    /**
     * State of the recognizer
     */
    private int state = STATE_NORMAL;

    /**
     * Stores the different conversations, in trees form
     */
    private Conversation conversation;

    /**
     * Stores the current node
     */
    private Node currentNode;

    /**
     * Stores the past optional nodes, for back tracking
     */
    private Vector<Node> pastOptionNodes;

    /**
     * Current effect (of the current node)
     */
    private Effects currentEffects;
    
    /**
     * The subparser for the effect
     */
    private SubParser effectSubParser;

    /**
     * Name of the last non-player character readed, "NPC" is no name were found
     */
    private String characterName;

    /**
     * Path of the audio track for a conversation line
     */
    private String audioPath;
    
    /* Methods */

    /**
     * Constructor
     */
    public TreeConversationSubParser( GameData gameData ) {
        super( gameData );
    }

    /*
     *  (non-Javadoc)
     * @see conversationaleditor.xmlparser.ConversationParser#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement( String namespaceURI, String sName, String qName, Attributes attrs ) {

        // If no element is being subparsed
        if( subParsing == SUBPARSING_NONE ) {
            
            // If it is a "conversation" we pick the name, so we can build the tree later
            if( qName.equals( "tree-conversation" ) ) {
                // Store the name
                String conversationName = "";
                for( int i = 0; i < attrs.getLength( ); i++ )
                    if( attrs.getQName( i ).equals( "id" ) )
                        conversationName = attrs.getValue( i );
                
    
                // Create a dialogue node (which will be the root node) and add it to a new tree
                // The content of the tree will be built adding nodes directly to the root
                currentNode = new DialogueNode( );
                conversation = new Conversation( conversationName, currentNode );
                pastOptionNodes = new Vector<Node>( );
            }
    
         // If it is a non-player character line, store the character name and audio path (if present)
            else if( qName.equals( "speak-char" ) ) {
                // Set default name to "NPC"
                characterName = "NPC";
                audioPath="";

                for (int i=0 ; i<attrs.getLength( ); i++){
                    // If there is a "idTarget" attribute, store it
                    if( attrs.getQName( i ).equals( "idTarget" ) )
                        characterName = attrs.getValue( i );
                
                    // If there is a "uri" attribute, store it as audio path
                    if( attrs.getQName( i ).equals( "uri" ) )
                        audioPath = attrs.getValue( i );
                }
            }
            
            // If it is a player character line, store the audio path (if present)
            else if( qName.equals( "speak-player" ) ) {
                audioPath="";

                for (int i=0 ; i<attrs.getLength( ); i++){
                
                    // If there is a "uri" attribute, store it as audio path
                    if( attrs.getQName( i ).equals( "uri" ) )
                        audioPath = attrs.getValue( i );
                }
            }

    
            // If it is a point with a set of possible responses, create a new OptionNode
            else if( qName.equals( "response" ) ) {
                // Create a new OptionNode, and link it to the current node
                Node nuevoNodoOpcion = new OptionNode( );
                currentNode.addChild( nuevoNodoOpcion );
    
                // Change the actual node for the option node recently created
                currentNode = nuevoNodoOpcion;
            }
    
            // If we are about to read an option, change the state of the recognizer, so we can read the line of the option
            else if( qName.equals( "option" ) ) {
                state = STATE_WAITING_OPTION;
            }
    
            // If it is an effect tag, create new effect, new subparser and switch state
            else if( qName.equals( "effect" ) ) {
                currentEffects = new Effects( );
                effectSubParser = new EffectSubParser( currentEffects, gameData );
                subParsing = SUBPARSING_EFFECT;
            }
    
            // If there is a go back, link the current node (which will be a DialogueNode) with the last OptionNode stored
            else if( qName.equals( "go-back" ) ) {
                currentNode.addChild( pastOptionNodes.lastElement( ) );
            }
        }
        
        // If an effect element is being subparsed, spread the call
        if( subParsing == SUBPARSING_EFFECT ) {
            effectSubParser.startElement( namespaceURI, sName, qName, attrs );
        }
    }

    /*
     *  (non-Javadoc)
     * @see conversationaleditor.xmlparser.ConversationParser#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    public void endElement( String namespaceURI, String sName, String qName ) {

        // If no element is being subparsed
        if( subParsing == SUBPARSING_NONE ) {
            // If the conversation ends, store it in the game data
            if( qName.equals( "tree-conversation" ) ) {
                gameData.addConversation( conversation );
            }
            
            // If the tag is a line said by the player, add it to the current node
            else if( qName.equals( "speak-player" ) ) {
                // Store the read string into the current node, and then delete the string. The trim is performed so we don't
                // have to worry with indentations or leading/trailing spaces
                ConversationLine line =new ConversationLine( "Player", new String( currentString ).trim( ) );
                if (audioPath!=null && !this.audioPath.equals( "" )){
                    line.setAudioPath( audioPath );
                }
                
                currentNode.addLine( line );

    
                // If we were waiting an option, create a new DialogueNode
                if( state == STATE_WAITING_OPTION ) {
                    // Create a new DialogueNode, and link it to the current node (which will be a OptionNode)
                    Node newDialogueNode = new DialogueNode( );
                    currentNode.addChild( newDialogueNode );
    
                    // Add the current node (OptionNode) in the list of past option nodes, and change the current node
                    pastOptionNodes.add( currentNode );
                    currentNode = newDialogueNode;
    
                    // Go back to the normal state
                    state = STATE_NORMAL;
                }
    
            }
    
            // If the tag is a line said by a non-player character, add it to the current node
            else if( qName.equals( "speak-char" ) ) {
                // Store the read string into the current node, and then delete the string. The trim is performed so we don't
                // have to worry with indentations or leading/trailing spaces
                ConversationLine line =new ConversationLine( characterName, new String( currentString ).trim( ) );
                if (audioPath!=null && !this.audioPath.equals( "" )){
                    line.setAudioPath( audioPath );
                }
                currentNode.addLine( line );

            }
    
            // If an "option" tag ends, go back to keep working on the last OptionNode
            else if( qName.equals( "option" ) ) {
                // Se the current node to the last OptionNode stored
                currentNode = pastOptionNodes.remove( pastOptionNodes.size( ) - 1 );
            }
            
            // Reset the current string
            currentString = new StringBuffer( );
        }

        // If an effect tag is being subparsed
        else if( subParsing == SUBPARSING_EFFECT ) {
            // Spread the call
            effectSubParser.endElement( namespaceURI, sName, qName );
             
            // If the effect is being closed, insert the effect into the current node
            if( qName.equals( "effect" ) ) {
                currentNode.setEffects( currentEffects );
                subParsing = SUBPARSING_NONE;
            }
        }
    }
    
    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.loader.subparsers.SubParser#characters(char[], int, int)
     */
    public void characters( char[] buf, int offset, int len ) {
        // If no element is being subparsed
        if( subParsing == SUBPARSING_NONE )
            super.characters( buf, offset, len );
        
        // If an effect is being subparsed
        else if( subParsing == SUBPARSING_EFFECT ) 
            effectSubParser.characters( buf, offset, len );
    }
}
