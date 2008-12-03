package es.eucm.eadventure.editor.control.controllers;

import java.util.List;

import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.data.support.VarFlagSummary;

/**
 * This abstract class checks the operations that can be performed onto the data elements of the script. Each class must
 * hold the element that is going to be checked.
 * 
 * @author Bruno Torijano Bueno
 */
public abstract class DataControl {

	/**
	 * Link to the main controller.
	 */
	protected Controller controller;

	/**
	 * Constructor.
	 */
	protected DataControl( ) {
		controller = Controller.getInstance( );
	}

	/**
	 * Returns the content element of the data controller.
	 * 
	 * @return Element being controlled
	 */
	public abstract Object getContent( );

	/**
	 * Returns an array of int which holds the types of data that can be added as children to the given data type.
	 * 
	 * @return Array of data types
	 */
	public abstract int[] getAddableElements( );

	/**
	 * Returns whether the element accepts new elements at this time.
	 * 
	 * @return True if new elements can be added, false otherwise
	 */
	public boolean canAddElements( ) {
		int[] addableElements = getAddableElements( );
		boolean canAddElements = false;

		// If at least one type of element can be added, return true
		for( int type : addableElements )
			canAddElements = canAddElements || canAddElement( type );

		return canAddElements;
	}

	/**
	 * Returns wheter the element accepts new elements of a given type.
	 * 
	 * @param type
	 *            Type of element we want to check.
	 * @return True if the element can be added, false otherwise
	 */
	public abstract boolean canAddElement( int type );

	/**
	 * Returns whether the element can be deleted.
	 * 
	 * @return True if the element can be deleted, false otherwise
	 */
	public abstract boolean canBeDeleted( );
	
	/**
	 * Returns whether the element can be duplicated.
	 * 
	 * @return True if the element can be duplicated, false otherwise
	 */
	public abstract boolean canBeDuplicated( );


	/**
	 * Returns whether the element can be moved in the structure in which it's placed.
	 * 
	 * @return True if the element can be moved, false otherwise
	 */
	public abstract boolean canBeMoved( );

	/**
	 * Returns whether the element can be renamed.
	 * 
	 * @return True if the element can be renamed, false otherwises
	 */
	public abstract boolean canBeRenamed( );

	/**
	 * Adds a new element of the given type to the element.
	 * 
	 * @param type
	 *            The new type of element we want to add
	 * @return True if the element was added succesfully, false otherwise
	 */
	public abstract boolean addElement( int type );

	/**
	 * Deletes a given element from the current element.
	 * 
	 * @param dataControl
	 *            Data controller which contains the element
	 * @return True if the element was deleted, false otherwise
	 */
	public abstract boolean deleteElement( DataControl dataControl );

	/**
	 * Duplicates a given element from the current element.
	 * 
	 * @param dataControl
	 *            Data controller which contains the element
	 * @return True if the element was deleted, false otherwise
	 */
	//public abstract boolean duplicateElement( DataControl dataControl );

	
	/**
	 * Moves a given element to the previous position in the structure of the current element.
	 * 
	 * @param dataControl
	 *            Data controller which contains the element
	 * @return True if the element was moved, false otherwise
	 */
	public abstract boolean moveElementUp( DataControl dataControl );

	/**
	 * Moves a given element to the previous position in the structure of the current element.
	 * 
	 * @param dataControl
	 *            Data controller which contains the element
	 * @return True if the element was moved, false otherwise
	 */
	public abstract boolean moveElementDown( DataControl dataControl );

	/**
	 * Asks the user for a new ID for the element.
	 * 
	 * @return True if the element was renamed, false otherwise
	 */
	public abstract boolean renameElement( );

	/**
	 * Updates the given flag summary, adding the flag references contained in the elements. This method works
	 * recursively.
	 * 
	 * @param varFlagSummary
	 *            Flag summary to update. It is important to point that the main element must clear the flag summary
	 *            first, in order to provide clean data
	 */
	public abstract void updateFlagSummary( VarFlagSummary varFlagSummary );

	/**
	 * Returns if the data structure pending from the element is valid or not. This method works recursively.
	 * 
	 * @param currentPath
	 *            String with the path to the given element (including the element)
	 * @param incidences
	 *            List to store the incidences in the elements. Null if no incidences track must be stored
	 * @return True if the data structure pending from the element is valid, false otherwise
	 */
	public abstract boolean isValid( String currentPath, List<String> incidences );

	/**
	 * Counts all the references to a given asset. This method works recursively.
	 * 
	 * @param assetPath
	 *            Path to the asset (relative to the ZIP), without suffix in case of an animation or set of slides
	 * @return Number of references to the given asset
	 */
	public abstract int countAssetReferences( String assetPath );
	
	/**
	 * Produces a list with all the referenced assets in the data control. This method works recursively.
	 * 
	 * @param assetPaths
	 * 				The list with all the asset references. The list will only contain each asset path once, even if it is referenced more than once.
	 * @param assetTypes
	 * 				The types of the assets contained in assetPaths.
	 */
	public abstract void getAssetReferences( List<String> assetPaths, List<Integer> assetTypes);

	/**
	 * Searchs all the references to the given path, and deletes them. This method works recursively.
	 * 
	 * @param assetPath
	 *            Path to the asset (relative to the ZIP), without suffix in case of an animation or set of slides
	 */
	public abstract void deleteAssetReferences( String assetPath );

	/**
	 * Counts all the references to a given identifier. This method works recursively.
	 * 
	 * @param id
	 *            Identifier to which the references must be found
	 * @return Number of references to the given identifier
	 */
	public abstract int countIdentifierReferences( String id );

	/**
	 * Searchs all the references to a given identifier, and replaces them with another one. This method works
	 * recursively.
	 * 
	 * @param oldId
	 *            Identifier to be replaced
	 * @param newId
	 *            Identifier to replace the old one
	 */
	public abstract void replaceIdentifierReferences( String oldId, String newId );

	/**
	 * Searchs all the references to a given identifier and deletes them. This method works recursively.
	 * 
	 * @param id
	 *            Identifier to be deleted
	 */
	public abstract void deleteIdentifierReferences( String id );

}
