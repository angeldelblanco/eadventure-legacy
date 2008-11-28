package es.eucm.eadventure.common.data.adaptation;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the adaptation data, which includes the flag activation and deactivation values,
 * along with the inital scene of the game 
 */
public class AdaptedState {

    /**
     * Id of the initial scene
     */
    private String initialScene;
    
    public static final String ACTIVATE = "activate";
    
    public static final String DEACTIVATE = "deactivate";
    
    /**
     * List of all flags and vars (in order)
     */
    private List<String> allFlagsVars;
    
    /**
     * List of deactivate/activate for flags (and value for vars)
     */
    private List<String> actionsValues;
    
    /**
     * Constructor
     */
    public AdaptedState( ) {
        initialScene = null;
        allFlagsVars = new ArrayList<String>( );
        actionsValues = new ArrayList<String>( );
    }
    
    /**
     * Returns the id of the initial scene
     * @return Id of the initial scene, null if none
     */
    public String getInitialScene( ) {
        return initialScene;
    }
    
    /**
     * Returns the list of flags and vars
     * @return List of the deactivated flags
     */
    public List<String> getFlagsVars( ) {
        return allFlagsVars;
    }
    
    /**
     * Sets the initial scene id
     * @param initialScene Id of the initial scene
     */
    public void setInitialScene( String initialScene ) {
        this.initialScene = initialScene;
    }
    
    /**
     * Adds a new flag to be activated
     * @param flag Name of the flag
     */
    public void addActivatedFlag( String flag ) {
        allFlagsVars.add( flag );
        actionsValues.add( ACTIVATE );
    }
    
    /**
     * Adds a new flag to be deactivated
     * @param flag Name of the flag
     */
    public void addDeactivatedFlag( String flag ) {
        allFlagsVars.add( flag );
        actionsValues.add( DEACTIVATE );
    }

    /**
     * Adds a new var
     * @param var
     * @param value
     */
    public void addVarValue ( String var, int value ){
    	allFlagsVars.add( var );
    	actionsValues.add( Integer.toString(value) );
    }
    
    public void removeFlagVar( int row ){
    	allFlagsVars.remove( row );
    	actionsValues.remove( row );
    }
    
    public void changeFlag (int row, String flag){
    	int nFlags = actionsValues.size( );
    	allFlagsVars.remove( row );
		if (row<nFlags-1)
			allFlagsVars.add( row, flag );
		else
			allFlagsVars.add( flag );

    }
    
    public void changeAction (int row){
    	int nFlags = actionsValues.size( );
    	if (actionsValues.get( row ).equals( ACTIVATE )){
    		actionsValues.remove( row );
    		
    		if (row<nFlags-1)
    			actionsValues.add( row, DEACTIVATE );
    		else
    			actionsValues.add( DEACTIVATE );
    	}
    	
    	else if (actionsValues.get( row ).equals( DEACTIVATE )){
    		actionsValues.remove( row );
    		
    		if (row<nFlags-1)
    			actionsValues.add( row, ACTIVATE );
    		else
    			actionsValues.add( ACTIVATE );
    	}

    }
    
    public void changeValue ( int row, int newValue ){
    	if ( row>=0 && row<=actionsValues.size() ){
    		actionsValues.remove( row );
    		actionsValues.add( row, Integer.toString(newValue) );
    	}
    }
    
    public String getAction (int i){
    	return actionsValues.get( i );
    }
    
    public int getValue (int i){
    	return Integer.parseInt(actionsValues.get(i));
    }
    
    public String getFlagVar (int i){
    	return allFlagsVars.get( i );
    }
    
    public boolean isEmpty(){
    	return allFlagsVars.size( )==0 && (initialScene==null || initialScene.equals( "" ));
    }

    /**
     * Returns the list of the activated flags
     * @return List of the activated flags
     */
    public List<String> getActivatedFlags( ) {
    	List<String> activatedFlags = new ArrayList<String>();
    	for ( int i=0; i<actionsValues.size(); i++){
    		if (actionsValues.get(i).equals(ACTIVATE)){
    			activatedFlags.add(allFlagsVars.get(i));
    		}
    	}
        return activatedFlags;
    }

    /**
     * Returns the list of the deactivated flags
     * @return List of the deactivated flags
     */
    public List<String> getDeactivatedFlags( ) {
       	List<String> deactivatedFlags = new ArrayList<String>();
    	for ( int i=0; i<actionsValues.size(); i++){
    		if (actionsValues.get(i).equals(DEACTIVATE)){
    			deactivatedFlags.add(allFlagsVars.get(i));
    		}
    	}    	
        return deactivatedFlags;
    }
    
    /**
     * Fills the argumented structures with the names of the vars and the values they must be set with
     * @param vars
     * @param values
     */
    public void getVarsValues ( List<String> vars, List<Integer> values){
    	for ( int i=0; i<actionsValues.size(); i++){
    		if ( !actionsValues.get(i).equals(ACTIVATE) && !actionsValues.get(i).equals(DEACTIVATE) ){
    			vars.add(allFlagsVars.get(i));
    			values.add(new Integer(actionsValues.get(i)));
    		}
    	}
    }
} 
