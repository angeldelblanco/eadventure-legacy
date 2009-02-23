package es.eucm.eadventure.common.loader.subparsers;

import java.awt.Point;

import org.xml.sax.Attributes;

import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.ExitLook;
import es.eucm.eadventure.common.data.chapter.InfluenceArea;
import es.eucm.eadventure.common.data.chapter.NextScene;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.Effects;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.data.chapter.scenes.Scene;

/**
 * Class to subparse scenes
 */
public class SceneSubParser extends SubParser {

	/* Attributes */

	/**
	 * Constant for reading nothing
	 */
	private static final int READING_NONE = 0;

	/**
	 * Constant for reading resources tag
	 */
	private static final int READING_RESOURCES = 1;

	/**
	 * Constant for reading exit tag
	 */
	private static final int READING_EXIT = 2;

	/**
	 * Constant for reading next-scene tag
	 */
	private static final int READING_NEXT_SCENE = 3;

	/**
	 * Constant for reading element reference tag
	 */
	private static final int READING_ELEMENT_REFERENCE = 4;

	/**
	 * Constant for subparsing nothing
	 */
	private static final int SUBPARSING_NONE = 0;

	/**
	 * Constant for subparsing condition tag
	 */
	private static final int SUBPARSING_CONDITION = 1;

	/**
	 * Constant for subparsing effect tag
	 */
	private static final int SUBPARSING_EFFECT = 2;
	
	/**
	 * Constant for subparsing active area
	 */
	private static final int SUBPARSING_ACTIVE_AREA = 3;
	
	/**
	 * Constant for subparsing active area
	 */
	private static final int SUBPARSING_BARRIER = 4;

	private static final int SUBPARSING_TRAJECTORY = 5;
	
	
	
	/**
	 * Stores the current element being parsed
	 */
	private int reading = READING_NONE;

	/**
	 * Stores the current element being subparsed
	 */
	private int subParsing = SUBPARSING_NONE;
	

	/**
	 * Stores the element being parsed
	 */
	private Scene scene;

	/**
	 * Stores the current resources being parsed
	 */
	private Resources currentResources;

	/**
	 * Stores the current exit being used
	 */
	private Exit currentExit;
	
    /**
     * Stores the current exit look being used
     */
    private ExitLook currentExitLook;

	/**
	 * Stores the current next-scene being used
	 */
	private NextScene currentNextScene;

	
	private Point currentPoint;
	
	/**
	 * Stores the current element reference being used
	 */
	private ElementReference currentElementReference;

	/**
	 * Stores the current conditions being parsed
	 */
	private Conditions currentConditions;

	/**
	 * Stores the current effects being parsed
	 */
	private Effects currentEffects;
	
	/**
	 * The subparser for the condition or effect tags
	 */
	private SubParser subParser;

	/* Methods */

	/**
	 * Constructor
	 * 
	 * @param chapter
	 *            Chapter data to store the read data
	 */
	public SceneSubParser( Chapter chapter ) {
		super( chapter );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.cargador.subparsers.SubParser#startElement(java.lang.String, java.lang.String,
	 *      java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement( String namespaceURI, String sName, String qName, Attributes attrs ) {

		// If no element is being parsed
		if( subParsing == SUBPARSING_NONE ) {

			// If it is a scene tag, create a new scene with its id
			if( qName.equals( "scene" ) ) {
				String sceneId = "";
				boolean initialScene = false;
				int playerLayer=-1;
				float playerScale = 1.0f;
				
				for( int i = 0; i < attrs.getLength( ); i++ ) {
					if( attrs.getQName( i ).equals( "id" ) )
						sceneId = attrs.getValue( i );
					if( attrs.getQName( i ).equals( "start" ) )
						initialScene = attrs.getValue( i ).equals( "yes" );
					if( attrs.getQName( i ).equals( "playerLayer" ) )
						playerLayer = Integer.parseInt( attrs.getValue( i ) );
					if (attrs.getQName( i ).equals( "playerScale" ) )
						playerScale = Float.parseFloat( attrs.getValue( i ));
				}

				scene = new Scene( sceneId );
				scene.setPlayerLayer(playerLayer);
				scene.setPlayerScale(playerScale);
				if( initialScene )
					chapter.setTargetId( sceneId );
			}

			// If it is a resources tag, create the new resources
			else if( qName.equals( "resources" ) ) {
				currentResources = new Resources( );
				reading = READING_RESOURCES;
			}

			// If it is an asset tag, read it and add it to the current resources
			else if( qName.equals( "asset" ) ) {
				String type = "";
				String path = "";

				for( int i = 0; i < attrs.getLength( ); i++ ) {
					if( attrs.getQName( i ).equals( "type" ) )
						type = attrs.getValue( i );
					if( attrs.getQName( i ).equals( "uri" ) )
						path = attrs.getValue( i );
				}

				currentResources.addAsset( type, path );
			}

			// If it is a default-initial-position tag, store it in the scene
			else if( qName.equals( "default-initial-position" ) ) {
				int x = 0, y = 0;

				for( int i = 0; i < attrs.getLength( ); i++ ) {
					if( attrs.getQName( i ).equals( "x" ) )
						x = Integer.parseInt( attrs.getValue( i ) );
					if( attrs.getQName( i ).equals( "y" ) )
						y = Integer.parseInt( attrs.getValue( i ) );
				}

				scene.setDefaultPosition( x, y );
			}

			// If it is an exit tag, create the new exit
			else if( qName.equals( "exit" ) ) {
				int x = 0, y = 0, width = 0, height = 0;
				boolean rectangular = true;

				for( int i = 0; i < attrs.getLength( ); i++ ) {
					if( attrs.getQName( i ).equals( "rectangular" ))
						rectangular = attrs.getValue( i ).equals("yes");
					if( attrs.getQName( i ).equals( "x" ) )
						x = Integer.parseInt( attrs.getValue( i ) );
					if( attrs.getQName( i ).equals( "y" ) )
						y = Integer.parseInt( attrs.getValue( i ) );
					if( attrs.getQName( i ).equals( "width" ) )
						width = Integer.parseInt( attrs.getValue( i ) );
					if( attrs.getQName( i ).equals( "height" ) )
						height = Integer.parseInt( attrs.getValue( i ) );
				}

				currentExit = new Exit( rectangular, x, y, width, height );
				reading = READING_EXIT;
			}
			
            else if( qName.equals( "exit-look" ) ) {
                currentExitLook = new ExitLook();
                String text=null;String cursorPath=null;
                for( int i = 0; i < attrs.getLength( ); i++ ) {
                    if( attrs.getQName( i ).equals( "text" ) )
                        text =  attrs.getValue( i ) ;
                    if( attrs.getQName( i ).equals( "cursor-path" ) )
                        cursorPath = attrs.getValue( i );
                }
                currentExitLook.setCursorPath( cursorPath );
                currentExitLook.setExitText( text );
            }


			// If it is a next-scene tag, create the new next scene
			else if( qName.equals( "next-scene" ) ) {
				String idTarget = "";
				int x = -1, y = -1;

				for( int i = 0; i < attrs.getLength( ); i++ ) {
					if( attrs.getQName( i ).equals( "idTarget" ) )
						idTarget = attrs.getValue( i );
					if( attrs.getQName( i ).equals( "x" ) )
						x = Integer.parseInt( attrs.getValue( i ) );
					if( attrs.getQName( i ).equals( "y" ) )
						y = Integer.parseInt( attrs.getValue( i ) );
				}

				currentNextScene = new NextScene( idTarget, x, y );
				reading = READING_NEXT_SCENE;
			}
			
			else if ( qName.equals("point")) {
				
				int x = 0;
				int y = 0;
				
				for (int i = 0; i < attrs.getLength(); i++) {
					if ( attrs.getQName( i ).equals( "x" ))
						x = Integer.parseInt( attrs.getValue( i ) );
					if ( attrs.getQName( i ).equals( "y" ))
						y = Integer.parseInt( attrs.getValue( i ) );
				}
				
				currentPoint = new Point(x, y);
			}

			// If it is a object-ref or character-ref, create the new element reference
			else if( qName.equals( "object-ref" ) || qName.equals( "character-ref" ) || qName.equals("atrezzo-ref")) {
				String idTarget = "";
				int x = 0, y = 0;
				float scale = 0;
				int layer = -1;
				int influenceX = 0, influenceY = 0, influenceWidth = 0, influenceHeight = 0;
				boolean hasInfluence = false;
				
				for( int i = 0; i < attrs.getLength( ); i++ ) {
					if( attrs.getQName( i ).equals( "idTarget" ) )
						idTarget = attrs.getValue( i );
					if( attrs.getQName( i ).equals( "x" ) )
						x = Integer.parseInt( attrs.getValue( i ) );
					if( attrs.getQName( i ).equals( "y" ) )
						y = Integer.parseInt( attrs.getValue( i ) );
					if( attrs.getQName( i ).equals( "scale" ))
						scale = Float.parseFloat( attrs.getValue( i ));
					if ( attrs.getQName( i ).equals( "layer" ))
						layer = Integer.parseInt( attrs.getValue( i ));
					if ( attrs.getQName( i ).equals( "hasInfluenceArea"))
						hasInfluence = attrs.getValue( i ).equals( "yes" );
					if ( attrs.getQName( i ).equals( "influenceX"))
						influenceX = Integer.parseInt(attrs.getValue(i));
					if ( attrs.getQName( i ).equals( "influenceY"))
						influenceY = Integer.parseInt(attrs.getValue(i));
					if ( attrs.getQName( i ).equals( "influenceWidth"))
						influenceWidth = Integer.parseInt(attrs.getValue(i));
					if ( attrs.getQName( i ).equals( "influenceHeight"))
						influenceHeight = Integer.parseInt(attrs.getValue(i));
				}

				currentElementReference = new ElementReference( idTarget, x, y , layer);
				if (hasInfluence) {
					InfluenceArea influenceArea = new InfluenceArea(influenceX, influenceY, influenceWidth, influenceHeight);
					currentElementReference.setInfluenceArea(influenceArea);
				}
				if (scale > 0.001 || scale < -0.001)
					currentElementReference.setScale(scale);
				reading = READING_ELEMENT_REFERENCE;
			}

			// If it is a condition tag, create the new condition, the subparser and switch the state
			else if( qName.equals( "condition" ) ) {
				currentConditions = new Conditions( );
				subParser = new ConditionSubParser( currentConditions, chapter );
				subParsing = SUBPARSING_CONDITION;
			}

			// If it is a effect tag, create the new effect, the subparser and switch the state
			else if( qName.equals( "effect" ) ) {
				currentEffects = new Effects( );
				subParser = new EffectSubParser( currentEffects, chapter );
				subParsing = SUBPARSING_EFFECT;
			}

			// If it is a post-effect tag, create the new effect, the subparser and switch the state
			else if( qName.equals( "post-effect" ) ) {
				currentEffects = new Effects( );
				subParser = new EffectSubParser( currentEffects, chapter );
				subParsing = SUBPARSING_EFFECT;
			}
			
			// If it is a post-effect tag, create the new effect, the subparser and switch the state
			else if( qName.equals( "active-area" ) ) {
				subParsing = SUBPARSING_ACTIVE_AREA;
				subParser = new ActiveAreaSubParser( chapter, scene,scene.getActiveAreas( ).size( ) );
			}
			
			// If it is a post-effect tag, create the new effect, the subparser and switch the state
			else if( qName.equals( "barrier" ) ) {
				subParsing = SUBPARSING_BARRIER;
				subParser = new BarrierSubParser( chapter, scene,scene.getBarriers( ).size( ) );
			}
			
			else if( qName.equals( "trajectory") ) {
				subParsing = SUBPARSING_TRAJECTORY;
				subParser = new TrajectorySubParser( chapter, scene);
			}

		}

		// If it is subparsing an effect or condition, spread the call
		if( subParsing != SUBPARSING_NONE ) {
			subParser.startElement( namespaceURI, sName, qName, attrs );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.cargador.subparsers.SubParser#endElement(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	public void endElement( String namespaceURI, String sName, String qName ) {

		// If no element is being subparsed
		if( subParsing == SUBPARSING_NONE ) {

			// If it is a scene tag, store the scene in the game data
			if( qName.equals( "scene" ) ) {
				chapter.addScene( scene );
			}

			// If it is a resources tag, add the resources to the scene
			else if( qName.equals( "resources" ) ) {
				scene.addResources( currentResources );
				reading = READING_NONE;
			}

			// If it is a name tag, store the name in the scene
			else if( qName.equals( "name" ) ) {
				scene.setName( currentString.toString( ).trim( ) );
			}

			// If it is a documentation tag, hold the documentation in the current element
			else if( qName.equals( "documentation" ) ) {
				if( reading == READING_NONE )
					scene.setDocumentation( currentString.toString( ).trim( ) );
				else if( reading == READING_EXIT )
					currentExit.setDocumentation( currentString.toString( ).trim( ) );
				else if( reading == READING_ELEMENT_REFERENCE )
					currentElementReference.setDocumentation( currentString.toString( ).trim( ) );
			}

			// If it is an exit tag, store the exit in the scene
			else if( qName.equals( "exit" ) ) {
				scene.addExit( currentExit );
				reading = READING_NONE;
			}
			
            // If it is an exit look tag, store the look in the exit
            else if( qName.equals( "exit-look" ) ) {
               
            	if (reading==READING_NEXT_SCENE)
                    currentNextScene.setExitLook( currentExitLook );
                else if (reading == READING_EXIT){
                    currentExit.setDefaultExitLook( currentExitLook );
                }
            }


			// If it is a next-scene tag, store the next scene in the current exit
			else if( qName.equals( "next-scene" ) ) {
				currentExit.addNextScene( currentNextScene );
				reading = READING_NONE;
			}
			
			else if ( qName.equals ( "point" )) {
				currentExit.addPoint(currentPoint);
			}

			// If it is a object-ref tag, store the reference in the scene
			else if( qName.equals( "object-ref" ) ) {
				scene.addItemReference( currentElementReference );
				reading = READING_NONE;
			}

			// If it is a character-ref tag, store the reference in the scene
			else if( qName.equals( "character-ref" ) ) {
				scene.addCharacterReference( currentElementReference );
				reading = READING_NONE;
			}
			// If it is a atrezzo-ref tag, store the reference in the scene
			else if( qName.equals( "atrezzo-ref" ) ) {
				scene.addAtrezzoReference( currentElementReference );
				reading = READING_NONE;
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
				
				// If we are parsing resources, add the condition to the current resources
				if( reading == READING_RESOURCES )
					currentResources.setConditions( currentConditions );

				// If we are parsing a next-scene, add the condition to the current next scene
				if( reading == READING_NEXT_SCENE )
					currentNextScene.setConditions( currentConditions );

				// If we are parsing an element reference, add the condition to the current element reference
				if( reading == READING_ELEMENT_REFERENCE )
					currentElementReference.setConditions( currentConditions );

				// Switch the state
				subParsing = SUBPARSING_NONE;
				
			}
			}
		

		// If an effect is being subparsed
		else if( subParsing == SUBPARSING_EFFECT ) {
			// Spread the call
			subParser.endElement( namespaceURI, sName, qName );

			// If the effect tag is being closed, add the effects to the current next scene and switch the state
			if( qName.equals( "effect" ) ) {
				currentNextScene.setEffects( currentEffects );
				subParsing = SUBPARSING_NONE;
			}

			// If the effect tag is being closed, add the post-effects to the current next scene and switch the state
			if( qName.equals( "post-effect" ) ) {
				currentNextScene.setPostEffects( currentEffects );
				subParsing = SUBPARSING_NONE;
			}
		}
		
		// If an active area is being subparsed
		else if (subParsing == SUBPARSING_ACTIVE_AREA){
			// Spread the call
			subParser.endElement( namespaceURI, sName, qName );
			
			if ( qName.equals( "active-area" )){
				subParsing = SUBPARSING_NONE;
			}
		}
		
		// If a barrier is being subparsed
		else if (subParsing == SUBPARSING_BARRIER){
			// Spread the call
			subParser.endElement( namespaceURI, sName, qName );
			
			if ( qName.equals( "barrier" )){
				subParsing = SUBPARSING_NONE;
			}
		}
		
		else if (subParsing == SUBPARSING_TRAJECTORY) {
			subParser.endElement( namespaceURI, sName, qName);
			if (qName.equals("trajectory")) {
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

		// If it is reading an effect or a condition
		else
			subParser.characters( buf, offset, len );
	}
}
