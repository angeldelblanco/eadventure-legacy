package es.eucm.eadventure.common.loader.subparsers;

import org.xml.sax.Attributes;

import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.Effects;
import es.eucm.eadventure.common.data.chapter.elements.Atrezzo;
import es.eucm.eadventure.common.data.chapter.elements.Item;
import es.eucm.eadventure.common.data.chapter.resources.Resources;

/**
 * 
 * Class to subperse atrezzo items
 *
 */
public class AtrezzoSubParser extends SubParser{

	/* Attributes */

	/**
	 * Constant for reading nothing.
	 */
	private static final int READING_NONE = 0;

	/**
	 * Constant for reading resources tag.
	 */
	private static final int READING_RESOURCES = 1;

	/**
	 * Constant for subparsing nothing.
	 */
	private static final int SUBPARSING_NONE = 0;

	/**
	 * Constant for subparsing condition tag.
	 */
	private static final int SUBPARSING_CONDITION = 1;

	/**
	 * Constant for subparsing effect tag.
	 */
	private static final int SUBPARSING_EFFECT = 2;

	/**
	 * Store the current element being parsed.
	 */
	private int reading = READING_NONE;

	/**
	 * Stores the current element being subparsed.
	 */
	private int subParsing = SUBPARSING_NONE;

	/**
	 * Atrezzo object being parsed.
	 */
	private Atrezzo atrezzo;

	/**
	 * Current resources being parsed.
	 */
	private Resources currentResources;

	/**
	 * Current conditions being parsed.
	 */
	private Conditions currentConditions;

	/**
	 * Current effects being parsed.
	 */
	private Effects currentEffects;

	/**
	 * Subparser for effects and conditions.
	 */
	private SubParser subParser;

	/**
	 * Stores the action documentation;
	 */
	private String currentDocumentation;

	/**
	 * Stores an idTarget.
	 */
	private String currentIdTarget;

	/* Methods */

	/**
	 * Constructor.
	 * 
	 * @param chapter
	 *            Chapter data to store the read data
	 */
	public AtrezzoSubParser( Chapter chapter ) {
		super( chapter );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.cargador.subparsers.SubParser#startElement(java.lang.String, java.lang.String,
	 *      java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement( String namespaceURI, String sName, String qName, Attributes attrs ) {

		// If no element is being subparsed
		if( subParsing == SUBPARSING_NONE ) {
			// If it is a object tag, create the new object (with its id)
			if( qName.equals( "atrezzoobject" ) ) {
				String atrezzoId = "";

				for( int i = 0; i < attrs.getLength( ); i++ )
					if( attrs.getQName( i ).equals( "id" ) )
						atrezzoId = attrs.getValue( i );

				atrezzo = new Atrezzo( atrezzoId );
			}

			// If it is a resources tag, create the new resources and switch the state
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

				// If the asset is not an special one
//				if( !AssetsController.isAssetSpecial( path ) )
					currentResources.addAsset( type, path );
			}

			// If it is a condition tag, create new conditions and switch the state
			else if( qName.equals( "condition" ) ) {
				currentConditions = new Conditions( );
				subParser = new ConditionSubParser( currentConditions, chapter );
				subParsing = SUBPARSING_CONDITION;
			}

			// If it is a effect tag, create new effects and switch the state
			else if( qName.equals( "effect" ) ) {
				subParser = new EffectSubParser( currentEffects, chapter );
				subParsing = SUBPARSING_EFFECT;
			}
		}

		// If it is reading an effect or a condition, spread the call
		if( subParsing != SUBPARSING_NONE ) {
			//String id = this.atrezzo.getId( );
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

			// If it is an object tag, store the object in the game data
			if( qName.equals( "atrezzoobject" ) ) {
				chapter.addAtrezzo( atrezzo );
			}

			// If it is a resources tag, add it to the object
			else if( qName.equals( "resources" ) ) {
				atrezzo.addResources( currentResources );
				reading = READING_NONE;
			}

			// If it is a name tag, store the name in the object
			else if( qName.equals( "name" ) ) {
				atrezzo.setName( currentString.toString( ).trim( ) );
			}

			// If it is a documentation tag, hold the documentation in the current element
			else if( qName.equals( "documentation" ) ) {
				if( reading == READING_NONE )
					atrezzo.setDocumentation( currentString.toString( ).trim( ) );
				
			}

			// If it is a brief tag, store the brief description in the object
			else if( qName.equals( "brief" ) ) {
				atrezzo.setDescription( currentString.toString( ).trim( ) );
			}

			// If it is a detailed tag, store the detailed description in the object
			else if( qName.equals( "detailed" ) ) {
				atrezzo.setDetailedDescription( currentString.toString( ).trim( ) );
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
				// Store the conditions in the resources
				if( reading == READING_RESOURCES )
					currentResources.setConditions( currentConditions );

				// Switch state
				subParsing = SUBPARSING_NONE;
			}
		}

		// If an effect is being subparsed
		else if( subParsing == SUBPARSING_EFFECT ) {
			// Spread the call
			subParser.endElement( namespaceURI, sName, qName );

			// If the effect tag is being closed, switch the state
			if( qName.equals( "effect" ) ) {
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
