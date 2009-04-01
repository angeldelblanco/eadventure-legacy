package es.eucm.eadventure.common.loader.subparsers;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import es.eucm.eadventure.common.data.adaptation.AdaptationProfile;
import es.eucm.eadventure.common.data.adaptation.AdaptationRule;
import es.eucm.eadventure.common.data.adaptation.AdaptedState;
import es.eucm.eadventure.common.data.adaptation.UOLProperty;
import es.eucm.eadventure.common.data.chapter.Chapter;


public class AdaptationSubParser extends SubParser{
   

    /* Constants */
    private static final int NONE = 0;
    private static final int INITIAL_STATE = 1;
    private static final int ADAPTATION_RULE = 2;
    
    private int parsing = NONE;
    
    /**
     * Adaptation data
     */
    private AdaptedState initialState;
    private List<AdaptationRule> externalRules;
    private AdaptationRule rule_temp;
    
    /**
     * String to store the current string in the XML file
     */
    private StringBuffer currentString;
    
    /**
     * The adaptation profile to fill
     */
    private AdaptationProfile profile;
    
    
    /**
     * Default constructor
     */
    public AdaptationSubParser(Chapter chapter) {
	super(chapter);
	profile = new AdaptationProfile();
    }
    
    

    
    /*
     *  (non-Javadoc)
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement( String namespaceURI, String sName, String qName, Attributes attrs ) {

    	// Check if it is an scorm adaptation profile
    	if (qName.equals("adaptation")){
    		for( int i = 0; i < attrs.getLength( ); i++ ) {
    			 if (attrs.getQName(i).equals("scorm12")){
                 	profile.setScorm12( attrs.getValue(i).equals("yes") );
                 }
                 if (attrs.getQName(i).equals("scorm2004")){
                 	profile.setScorm2004(attrs.getValue(i).equals("yes"));
                 }
                 if (attrs.getQName(i).equals("name")){
                     profile.setName(attrs.getValue(i));
                 }
    		}
    	}
    	
        //Start parsing the initial state
        if (qName.equals( "initial-state" )) {
            parsing = INITIAL_STATE;
        }
        
        //Start parsing an adaptation rule
        else if (qName.equals( "adaptation-rule" )){
            parsing = ADAPTATION_RULE;
            rule_temp = new AdaptationRule();
        }
        
        //Initial scene
        else if( qName.equals( "initial-scene" ) ) {
            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getQName( i ).equals( "idTarget" ) ) {
                    if(parsing==INITIAL_STATE) {
                        initialState.setTargetId( attrs.getValue( i ) );
                    } else {
                        rule_temp.setInitialScene( attrs.getValue( i ));
                    }
                }
            }
        }
        
        // If the tag activates a flag
        else if( qName.equals( "activate" ) ) {
            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getQName( i ).equals( "flag" ) ) {
                    if (parsing==INITIAL_STATE) {
                        initialState.addActivatedFlag( attrs.getValue( i ) );
                    } else {
                        rule_temp.addActivatedFlag( attrs.getValue( i ) );
                    }
                    profile.addFlag ( attrs.getValue( i ) );
                }
            }
        }
        
        // If the tag deactivates a flag
        else if( qName.equals( "deactivate" ) ) {
            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getQName( i ).equals( "flag" ) ) {
                    if (parsing==INITIAL_STATE) {
                        initialState.addDeactivatedFlag( attrs.getValue( i ) );
                    } else {
                        rule_temp.addDeactivatedFlag( attrs.getValue( i ) );
                    }
                    profile.addFlag ( attrs.getValue( i ) );
                }
            }
        }
        
        // If the tag set-value a var
        else if( qName.equals( "set-value" ) ) {
        	String var = null;
        	String value = null;
            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getQName( i ).equals( "var" ) ) {
                	var = attrs.getValue( i );
                }
                else if( attrs.getQName( i ).equals( "value" ) ) {
                	value =  attrs.getValue ( i ) ;
                }
            }
                	
            if (parsing==INITIAL_STATE) {
                initialState.addVarValue(var, AdaptedState.VALUE+" "+value);
            } else {
                rule_temp.addVarValue(var, AdaptedState.VALUE+" "+value);
            }
            profile.addVar ( var );
   
        }
        
        // If the tag increment a var
        else if( qName.equals( "increment" ) ) {
        	String var = null;
        	int value = 0;
            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getQName( i ).equals( "var" ) ) {
                	var = attrs.getValue( i );
                }
                
            }
                	
            if (parsing==INITIAL_STATE) {
                initialState.addVarValue(var, AdaptedState.INCREMENT);
            } else {
                rule_temp.addVarValue(var, AdaptedState.INCREMENT);
            }
            profile.addVar ( var );
   
        }
        
        // If the tag decrement a var
        else if( qName.equals( "decrement" ) ) {
        	String var = null;
        	int value = 0;
            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getQName( i ).equals( "var" ) ) {
                	var = attrs.getValue( i );
                }
               
            }
                	
            if (parsing==INITIAL_STATE) {
                initialState.addVarValue(var, AdaptedState.DECREMENT);
            } else {
                rule_temp.addVarValue(var, AdaptedState.DECREMENT);
            }
            profile.addVar ( var );
   
        }

        
        //Property from the UoL
        else if (qName.equals( "property" )) {
            String id = null;
            String value =  null;
            String op = null;
            for( int i = 0; i < attrs.getLength( ); i++ ) {
                if( attrs.getQName( i ).equals( "id" ) ) {
                    id = attrs.getValue( i );  
                } else if (attrs.getQName( i ).equals( "value" ) ) {
                    value = attrs.getValue( i ); 
                }else if (attrs.getQName( i ).equals( "operation" ) ) {
                    op = attrs.getValue( i );
                }
            }
            rule_temp.addUOLProperty( new UOLProperty(id,value,op) );
        }
            
    }
    
    public void endElement( String namespaceURI, String localName, String qName )  {
        //Finish parsing the initial state
        if (qName.equals( "initial-state" )) {
            parsing = NONE;
            profile.setInitialState(initialState);
        }
        
        else if( qName.equals( "ruleDescription" ) ) {
            this.rule_temp.setDescription( currentString.toString( ).trim( ) );
        }

        //Finish parsing an adaptation rule
        else if (qName.equals( "adaptation-rule" )){
            parsing = NONE;
            profile.addRule( rule_temp );
        }
        
     // Reset the current string
        currentString = new StringBuffer( );
    }

    /*
     *  (non-Javadoc)
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters( char[] buf, int offset, int len ) {
        // Append the new characters
        currentString.append( new String( buf, offset, len ) );
        
    }
}
