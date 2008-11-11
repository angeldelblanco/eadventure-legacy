package es.eucm.eadventure.editor.data.adaptation;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
public class AdaptationRule {

	//ID
	private String id;
	
	// GameState
    private AdaptedState gameState;
    
    /**
     * List of properties to be set
     */
    private List<UOLProperty> uolState;    
    //Description
    private String description;
    
    public AdaptationRule() {
    	uolState = new ArrayList<UOLProperty>( );
        gameState = new AdaptedState();
    }
    
    /**
     * Adds a new assessment property
     * @param property Assessment property to be added
     */
    public void addUOLProperty( UOLProperty property ) {
        uolState.add( property );
    }
    
    public List<UOLProperty> getUOLProperties( ) {
        return uolState;
    }

    public void setInitialScene( String initialScene ) {
        gameState.setInitialScene( initialScene );
        
    }

    public void addActivatedFlag( String flag ) {
        gameState.addActivatedFlag( flag );
        
    }

    public void addDeactivatedFlag( String flag ) {
        gameState.addDeactivatedFlag( flag );
        
    }

    
    public AdaptedState getAdaptedState() {
        return gameState;
    }

	/**
	 * @return the description
	 */
	public String getDescription( ) {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription( String description ) {
		this.description = description;
	}

	public String getId( ) {
		return id;
	}

	public void setId( String generateId ) {
		this.id=generateId;
		
	}


    
}