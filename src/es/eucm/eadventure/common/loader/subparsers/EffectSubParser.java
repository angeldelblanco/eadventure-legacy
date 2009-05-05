package es.eucm.eadventure.common.loader.subparsers;

import org.xml.sax.Attributes;

import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.AbstractEffect;
import es.eucm.eadventure.common.data.chapter.effects.ActivateEffect;
import es.eucm.eadventure.common.data.chapter.effects.CancelActionEffect;
import es.eucm.eadventure.common.data.chapter.effects.ConsumeObjectEffect;
import es.eucm.eadventure.common.data.chapter.effects.DeactivateEffect;
import es.eucm.eadventure.common.data.chapter.effects.DecrementVarEffect;
import es.eucm.eadventure.common.data.chapter.effects.Effects;
import es.eucm.eadventure.common.data.chapter.effects.GenerateObjectEffect;
import es.eucm.eadventure.common.data.chapter.effects.IncrementVarEffect;
import es.eucm.eadventure.common.data.chapter.effects.MacroReferenceEffect;
import es.eucm.eadventure.common.data.chapter.effects.MoveNPCEffect;
import es.eucm.eadventure.common.data.chapter.effects.MovePlayerEffect;
import es.eucm.eadventure.common.data.chapter.effects.PlayAnimationEffect;
import es.eucm.eadventure.common.data.chapter.effects.PlaySoundEffect;
import es.eucm.eadventure.common.data.chapter.effects.RandomEffect;
import es.eucm.eadventure.common.data.chapter.effects.SetValueEffect;
import es.eucm.eadventure.common.data.chapter.effects.ShowTextEffect;
import es.eucm.eadventure.common.data.chapter.effects.SpeakCharEffect;
import es.eucm.eadventure.common.data.chapter.effects.SpeakPlayerEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerBookEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerConversationEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerCutsceneEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerLastSceneEffect;
import es.eucm.eadventure.common.data.chapter.effects.TriggerSceneEffect;
import es.eucm.eadventure.common.data.chapter.effects.WaitTimeEffect;

/**
 * Class to subparse effects
 */
public class EffectSubParser extends SubParser {

    	/* Constants */
    	/**
	 * Constant for no subparsing
	 */
	private static final int SUBPARSING_NONE = 0;
	
	/**
	 * Constant for subparsing conditions
	 */
	private static final int SUBPARSING_CONDITION = 1;
    
	/* Attributes */

	/**
	 * The current subparser being used
	 */
	private SubParser subParser;
	
	/**
	 * Indicates the current element being subparsed
	 */
	private int subParsing;
	
	/**
	 * Stores the current id target
	 */
	private String currentCharIdTarget;

	/**
	 * Stores the effects being parsed
	 */
	private Effects effects;
	
	/**
	 * Atributes for show-text effects
	 */
	
	int x = 0;
	int y = 0;
	int frontColor=0;
	int borderColor=0;
    /**
     * Constants for reading random-effect
     */
    private boolean positiveBlockRead = false;
    private boolean readingRandomEffect = false;
    private RandomEffect randomEffect;
    
    /**
     * Stores the current conditions being read
     */
    private Conditions currentConditions;
    
    /**
     * CurrentEffect. Stores the last created effect to add it later the conditions
     */
    private AbstractEffect currentEffect;
    
    /**
     * New effects
     */
    private AbstractEffect newEffect;

	/* Methods */

	/**
	 * Constructor
	 * 
	 * @param effects
	 *            Structure in which the effects will be placed
	 * @param chapter
	 *            Chapter data to store the read data
	 */
	public EffectSubParser( Effects effects, Chapter chapter ) {
		super( chapter );
		this.effects = effects;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.loader.subparsers.SubParser#startElement(java.lang.String, java.lang.String,
	 *      java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement( String namespaceURI, String sName, String qName, Attributes attrs ) {
	     newEffect = null;
	
		// If it is a cancel-action tag
		if( qName.equals( "cancel-action" ) ) {
			newEffect= new CancelActionEffect( ) ;
		}

		// If it is a activate tag
		else if( qName.equals( "activate" ) ) {
			for( int i = 0; i < attrs.getLength( ); i++ )
				if( attrs.getQName( i ).equals( "flag" ) ) {
					newEffect=  new ActivateEffect( attrs.getValue( i ) ) ;
					chapter.addFlag( attrs.getValue( i ) );
				}
		}

		// If it is a deactivate tag
		else if( qName.equals( "deactivate" ) ) {
			for( int i = 0; i < attrs.getLength( ); i++ )
				if( attrs.getQName( i ).equals( "flag" ) ) {
					newEffect=  new DeactivateEffect( attrs.getValue( i ) ) ;
					chapter.addFlag( attrs.getValue( i ) );
				}
		}
		
		// If it is a set-value tag
		else if( qName.equals( "set-value" ) ) {
			String var  = null;
			int value = 0;
			
			for( int i = 0; i < attrs.getLength( ); i++ ){
				
				if( attrs.getQName( i ).equals( "var" ) ) {
					var = attrs.getValue( i );
				} else if( attrs.getQName( i ).equals( "value" ) ) {
					value = Integer.parseInt( attrs.getValue( i ) );
				}
			}
			newEffect = new SetValueEffect ( var, value );
			chapter.addVar( var );
		}
		
		// If it is a set-value tag
		else if( qName.equals( "increment" ) ) {
			String var  = null;
			int value = 0;
			
			for( int i = 0; i < attrs.getLength( ); i++ ){
				
				if( attrs.getQName( i ).equals( "var" ) ) {
					var = attrs.getValue( i );
				} else if( attrs.getQName( i ).equals( "value" ) ) {
					value = Integer.parseInt( attrs.getValue( i ) );
				}
			}
			newEffect = new IncrementVarEffect ( var, value );
			chapter.addVar( var );
		}
		
		// If it is a decrement tag
		else if( qName.equals( "decrement" ) ) {
			String var  = null;
			int value = 0;
			
			for( int i = 0; i < attrs.getLength( ); i++ ){
				
				if( attrs.getQName( i ).equals( "var" ) ) {
					var = attrs.getValue( i );
				} else if( attrs.getQName( i ).equals( "value" ) ) {
					value = Integer.parseInt( attrs.getValue( i ) );
				}
			}
			newEffect = new DecrementVarEffect ( var, value );
			chapter.addVar( var );
		}
		
        // If it is a macro-reference tag
        else if( qName.equals( "macro-ref" ) ) {
        	// Id
        	String id = null;
            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getQName( i ).equals( "id" ) ) {
                	id = attrs.getValue( i );
                } 
            }
            // Store the inactive flag in the conditions or either conditions
            newEffect = new MacroReferenceEffect ( id );
        }


		// If it is a consume-object tag
		else if( qName.equals( "consume-object" ) ) {
			for( int i = 0; i < attrs.getLength( ); i++ )
				if( attrs.getQName( i ).equals( "idTarget" ) )
					newEffect=  new ConsumeObjectEffect( attrs.getValue( i ) ) ;
		}

		// If it is a generate-object tag
		else if( qName.equals( "generate-object" ) ) {
			for( int i = 0; i < attrs.getLength( ); i++ )
				if( attrs.getQName( i ).equals( "idTarget" ) )
					newEffect=  new GenerateObjectEffect( attrs.getValue( i ) ) ;
		}

		// If it is a speak-char tag
		else if( qName.equals( "speak-char" ) ) {

			// Store the idTarget, to store the effect when the tag is closed
			currentCharIdTarget = null;

			for( int i = 0; i < attrs.getLength( ); i++ )
				if( attrs.getQName( i ).equals( "idTarget" ) )
					currentCharIdTarget = attrs.getValue( i );
		}

		// If it is a trigger-book tag
		else if( qName.equals( "trigger-book" ) ) {
			for( int i = 0; i < attrs.getLength( ); i++ )
				if( attrs.getQName( i ).equals( "idTarget" ) )
					newEffect= new TriggerBookEffect( attrs.getValue( i ) ) ;
		}
		
		// If it is a trigger-last-scene tag
		else if( qName.equals( "trigger-last-scene" ) ) {
			newEffect=  new TriggerLastSceneEffect( ) ;
		}

		// If it is a play-sound tag
		else if( qName.equals( "play-sound" ) ) {
			// Store the path and background
			String path = "";
			boolean background = true;
			for( int i = 0; i < attrs.getLength( ); i++ ) {
				if( attrs.getQName( i ).equals( "background" ) )
					background = attrs.getValue( i ).equals( "yes" );
				else if( attrs.getQName( i ).equals( "uri" ) )
					path = attrs.getValue( i );
			}

			// Add the new play sound effect
			newEffect=  new PlaySoundEffect( background, path );
		}

		// If it is a trigger-conversation tag
		else if( qName.equals( "trigger-conversation" ) ) {
			for( int i = 0; i < attrs.getLength( ); i++ )
				if( attrs.getQName( i ).equals( "idTarget" ) )
					newEffect=  new TriggerConversationEffect( attrs.getValue( i ) ) ;
		}

		// If it is a trigger-cutscene tag
		else if( qName.equals( "trigger-cutscene" ) ) {
			for( int i = 0; i < attrs.getLength( ); i++ )
				if( attrs.getQName( i ).equals( "idTarget" ) )
					newEffect=  new TriggerCutsceneEffect( attrs.getValue( i ) ) ;
		}

		// If it is a trigger-scene tag
		else if( qName.equals( "trigger-scene" ) ) {
			String scene = "";
			int x = 0;
			int y = 0;
			for( int i = 0; i < attrs.getLength( ); i++ )
				if( attrs.getQName( i ).equals( "idTarget" ) )
					scene = attrs.getValue( i );
				else if( attrs.getQName( i ).equals( "x" ) )
					x = Integer.parseInt( attrs.getValue( i ) );
				else if( attrs.getQName( i ).equals( "y" ) )
					y = Integer.parseInt( attrs.getValue( i ) );

			newEffect=  new TriggerSceneEffect( scene, x, y );
		}

		// If it is a play-animation tag
		else if( qName.equals( "play-animation" ) ) {
			String path = "";
			int x = 0;
			int y = 0;
			for( int i = 0; i < attrs.getLength( ); i++ ) {
				if( attrs.getQName( i ).equals( "uri" ) )
					path = attrs.getValue( i );
				else if( attrs.getQName( i ).equals( "x" ) )
					x = Integer.parseInt( attrs.getValue( i ) );
				else if( attrs.getQName( i ).equals( "y" ) )
					y = Integer.parseInt( attrs.getValue( i ) );
			}

			// Add the new play sound effect
			newEffect=  new PlayAnimationEffect( path, x, y );
		}

		// If it is a move-player tag
		else if( qName.equals( "move-player" ) ) {
			int x = 0;
			int y = 0;
			for( int i = 0; i < attrs.getLength( ); i++ ) {
				if( attrs.getQName( i ).equals( "x" ) )
					x = Integer.parseInt( attrs.getValue( i ) );
				else if( attrs.getQName( i ).equals( "y" ) )
					y = Integer.parseInt( attrs.getValue( i ) );
			}

			// Add the new move player effect
			newEffect=  new MovePlayerEffect( x, y );
		}

		// If it is a move-npc tag
		else if( qName.equals( "move-npc" ) ) {
			String npcTarget = "";
			int x = 0;
			int y = 0;
			for( int i = 0; i < attrs.getLength( ); i++ ) {
				if( attrs.getQName( i ).equals( "idTarget" ) )
					npcTarget = attrs.getValue( i );
				else if( attrs.getQName( i ).equals( "x" ) )
					x = Integer.parseInt( attrs.getValue( i ) );
				else if( attrs.getQName( i ).equals( "y" ) )
					y = Integer.parseInt( attrs.getValue( i ) );
			}

			// Add the new move NPC effect
			newEffect=  new MoveNPCEffect( npcTarget, x, y ) ;
		}
		
        // Random effect tag
        else if( qName.equals( "random-effect" ) ) {
            int probability = 0;
            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getQName( i ).equals( "probability" ) )
                    probability = Integer.parseInt( attrs.getValue( i ) );
            }
            
            // Add the new random effect
            randomEffect =new RandomEffect( probability );
            newEffect = randomEffect;
            readingRandomEffect = true;
            positiveBlockRead = false;
        }
	// wait-time effect
        else if( qName.equals( "wait-time" ) ) {
		int time=0;
		for( int i = 0; i < attrs.getLength( ); i++ ) {
			if( attrs.getQName( i ).equals( "time" ) )
			    time = Integer.parseInt( attrs.getValue( i ) );
		}

		// Add the new move NPC effect
		newEffect=  new WaitTimeEffect( time  ) ;
	}
		
	// show-text effect
        else if( qName.equals( "show-text" ) ) {
		x = 0;
		y = 0;
		frontColor=0;
		borderColor=0;
		for( int i = 0; i < attrs.getLength( ); i++ ) {
			if( attrs.getQName( i ).equals( "x" ) )
				x = Integer.parseInt( attrs.getValue( i ) );
			else if( attrs.getQName( i ).equals( "y" ) )
				y = Integer.parseInt( attrs.getValue( i ) );
			else if( attrs.getQName( i ).equals( "frontColor" ) )
			    frontColor = Integer.parseInt( attrs.getValue( i ) );
			else if( attrs.getQName( i ).equals( "borderColor" ) )
			    borderColor = Integer.parseInt( attrs.getValue( i ) );
		}

		
	}// If it is a condition tag, create new conditions and switch the state
	else if( qName.equals( "condition" ) ) {
		currentConditions = new Conditions( );
		subParser = new ConditionSubParser( currentConditions, chapter );
		subParsing = SUBPARSING_CONDITION;
	}

		// Not reading Random effect: Add the new Effect if not null
		if (!readingRandomEffect && newEffect!=null){
			effects.add( newEffect );
			// Store current effect
			currentEffect = newEffect;
			
		}

		// Reading random effect
		if (readingRandomEffect){
			// When we have just created the effect, add it
			if (newEffect!=null && newEffect == randomEffect){
				effects.add( newEffect );
			} 
			// Otherwise, determine if it is positive or negative effect 
			else if (newEffect!=null && !positiveBlockRead){
				randomEffect.setPositiveEffect( newEffect );
				positiveBlockRead = true;
			}
			// Negative effect 
			else if (newEffect!=null && positiveBlockRead){
				randomEffect.setNegativeEffect( newEffect );
				positiveBlockRead = false;
				readingRandomEffect = false;
				randomEffect = null;
			}
			// Store current effect
			currentEffect = newEffect;
			

		}
		
		// If it is reading an effect or a condition, spread the call
		if( subParsing != SUBPARSING_NONE ) {
			subParser.startElement( namespaceURI, sName, qName, attrs );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.loader.subparsers.SubParser#endElement(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	public void endElement( String namespaceURI, String sName, String qName ) {
		
	 // If no element is being subparsed
		if( subParsing == SUBPARSING_NONE ) {
		    newEffect = null;
		
		// If it is a speak-player
		if( qName.equals( "speak-player" ) ) {
			// Add the effect and clear the current string
			newEffect = new SpeakPlayerEffect( currentString.toString( ).trim( ) ) ;
		}

		// If it is a speak-char
		else if( qName.equals( "speak-char" ) ) {
			// Add the effect and clear the current string
			newEffect = new SpeakCharEffect( currentCharIdTarget, currentString.toString( ).trim( ) ) ;
		}// If it is a show-text
		else if( qName.equals( "show-text" ) ) {
		    	// Add the new ShowTextEffect
			newEffect=  new ShowTextEffect( currentString.toString( ).trim( ), x, y ,frontColor, borderColor) ;
		}
		
		
		
		// Not reading Random effect: Add the new Effect if not null
		if (!readingRandomEffect && newEffect!=null){
			effects.add( newEffect );
			// Store current effect
			currentEffect = newEffect;
			
		}

		// Reading random effect
		if (readingRandomEffect){
			// When we have just created the effect, add it
			if (newEffect!=null && newEffect == randomEffect){
				effects.add( newEffect );
			} 
			// Otherwise, determine if it is positive or negative effect 
			else if (newEffect!=null && !positiveBlockRead){
				randomEffect.setPositiveEffect( newEffect );
				positiveBlockRead = true;
			}
			// Negative effect 
			else if (newEffect!=null && positiveBlockRead){
				randomEffect.setNegativeEffect( newEffect );
				positiveBlockRead = false;
				readingRandomEffect = false;
				randomEffect = null;
			}
			// Store current effect
			currentEffect = newEffect;
			

		}
		
		

		// Reset the current string
		currentString = new StringBuffer( );
	}
		// If a condition is being subparsed
		else if( subParsing == SUBPARSING_CONDITION ) {
			// Spread the call
			subParser.endElement( namespaceURI, sName, qName );

			// If the condition tag is being closed
			if( qName.equals( "condition" ) ) {
			    // Store the conditions in the effect
			    currentEffect.setConditions( currentConditions );

				// Switch state
				subParsing = SUBPARSING_NONE;
			}
		}

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.loader.subparsers.SubParser#characters(char[], int, int)
	 */
	public void characters( char[] buf, int offset, int len ) {
		// If no element is being subparsed
		if( subParsing == SUBPARSING_NONE )
			super.characters( buf, offset, len );

		// If it is reading an effect or a condition, spread the call
		else
			subParser.characters( buf, offset, len );
	}
	
}