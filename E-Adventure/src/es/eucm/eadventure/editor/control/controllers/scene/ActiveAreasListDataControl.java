package es.eucm.eadventure.editor.control.controllers.scene;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.data.chapterdata.elements.ActiveArea;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.DataControl;
import es.eucm.eadventure.editor.data.supportdata.FlagSummary;
import es.eucm.eadventure.editor.gui.TextConstants;

public class ActiveAreasListDataControl extends DataControl {

	/**
	 * Scene controller that contains this element reference.
	 */
	private SceneDataControl sceneDataControl;

	/**
	 * List of activeAreas.
	 */
	private List<ActiveArea> activeAreasList;

	/**
	 * List of activeAreas controllers.
	 */
	private List<ActiveAreaDataControl> activeAreasDataControlList;
	
	/**
	 * Id of the next active area
	 */
	private int id = 0;

	/**
	 * Constructor.
	 * 
	 * @param sceneDataControl
	 *            Link to the parent scene controller
	 * @param activeAreasList
	 *            List of activeAreas
	 */
	public ActiveAreasListDataControl( SceneDataControl sceneDataControl, List<ActiveArea> activeAreasList ) {
		this.sceneDataControl = sceneDataControl;
		this.activeAreasList = activeAreasList;

		// Create subcontrollers
		activeAreasDataControlList = new ArrayList<ActiveAreaDataControl>( );
		for( ActiveArea activeArea : activeAreasList )
			activeAreasDataControlList.add( new ActiveAreaDataControl( sceneDataControl, activeArea ) );
		
		id = activeAreasList.size( ) +1;
	}

	/**
	 * Returns the list of activeAreas controllers.
	 * 
	 * @return List of activeAreas controllers
	 */
	public List<ActiveAreaDataControl> getActiveAreas( ) {
		return activeAreasDataControlList;
	}

	/**
	 * Returns the last activeArea controller from the list.
	 * 
	 * @return Last activeArea controller
	 */
	public ActiveAreaDataControl getLastActiveArea( ) {
		return activeAreasDataControlList.get( activeAreasDataControlList.size( ) - 1 );
	}

	/**
	 * Returns the id of the scene that contains this activeAreas list.
	 * 
	 * @return Parent scene id
	 */
	public String getParentSceneId( ) {
		return sceneDataControl.getId( );
	}

	@Override
	public Object getContent( ) {
		return activeAreasList;
	}

	@Override
	public int[] getAddableElements( ) {
		return new int[] { Controller.ACTIVE_AREA };
	}

	@Override
	public boolean canAddElement( int type ) {
		// It can always add new activeArea
		return type == Controller.ACTIVE_AREA;
	}

	@Override
	public boolean canBeDeleted( ) {
		return false;
	}

	@Override
	public boolean canBeMoved( ) {
		return false;
	}

	@Override
	public boolean canBeRenamed( ) {
		return false;
	}

	@Override
	public boolean addElement( int type ) {
		boolean elementAdded = false;

		if( type == Controller.ACTIVE_AREA ) {
			// Creamos una salida y su controlador
			ActiveArea newActiveArea = new ActiveArea( Integer.toString( id ), 0, 0, 20, 20 );
			id++;
			ActiveAreaDataControl newActiveAreaDataControl = new ActiveAreaDataControl( sceneDataControl, newActiveArea );

				activeAreasList.add( newActiveArea );
				activeAreasDataControlList.add( newActiveAreaDataControl );
				controller.dataModified( );
				elementAdded = true;

		}

		return elementAdded;
	}

	@Override
	public boolean deleteElement( DataControl dataControl ) {
		boolean elementDeleted = false;

		if( activeAreasList.remove( dataControl.getContent( ) ) ) {
			activeAreasDataControlList.remove( dataControl );
			controller.dataModified( );
			elementDeleted = true;
		}

		return elementDeleted;
	}

	@Override
	public boolean moveElementUp( DataControl dataControl ) {
		boolean elementMoved = false;
		int elementIndex = activeAreasList.indexOf( dataControl.getContent( ) );

		if( elementIndex > 0 ) {
			activeAreasList.add( elementIndex - 1, activeAreasList.remove( elementIndex ) );
			activeAreasDataControlList.add( elementIndex - 1, activeAreasDataControlList.remove( elementIndex ) );
			controller.dataModified( );
			elementMoved = true;
		}

		return elementMoved;
	}

	@Override
	public boolean moveElementDown( DataControl dataControl ) {
		boolean elementMoved = false;
		int elementIndex = activeAreasList.indexOf( dataControl.getContent( ) );

		if( elementIndex < activeAreasList.size( ) - 1 ) {
			activeAreasList.add( elementIndex + 1, activeAreasList.remove( elementIndex ) );
			activeAreasDataControlList.add( elementIndex + 1, activeAreasDataControlList.remove( elementIndex ) );
			controller.dataModified( );
			elementMoved = true;
		}

		return elementMoved;
	}

	@Override
	public boolean renameElement( ) {
		return false;
	}

	@Override
	public void updateFlagSummary( FlagSummary flagSummary ) {
		// Iterate through each activeArea
		for( ActiveAreaDataControl activeAreaDataControl : activeAreasDataControlList )
			activeAreaDataControl.updateFlagSummary( flagSummary );
	}

	@Override
	public boolean isValid( String currentPath, List<String> incidences ) {
		boolean valid = true;

		// Iterate through the activeAreas
		for( int i = 0; i < activeAreasDataControlList.size( ); i++ ) {
			String activeAreaPath = currentPath + " >> " + TextConstants.getElementName( Controller.ACTIVE_AREA ) + " #" + ( i + 1 );
			valid &= activeAreasDataControlList.get( i ).isValid( activeAreaPath, incidences );
		}

		return valid;
	}

	@Override
	public int countAssetReferences( String assetPath ) {
		int count = 0;

		// Iterate through each activeArea
		for( ActiveAreaDataControl activeAreaDataControl : activeAreasDataControlList )
			count += activeAreaDataControl.countAssetReferences( assetPath );

		return count;
	}

	@Override
	public void getAssetReferences( List<String> assetPaths, List<Integer> assetTypes ) {
		for( ActiveAreaDataControl activeAreaDataControl : activeAreasDataControlList )
			activeAreaDataControl.getAssetReferences( assetPaths, assetTypes );
		
	}
	
	@Override
	public void deleteAssetReferences( String assetPath ) {
		// Iterate through each activeArea
		for( ActiveAreaDataControl activeAreaDataControl : activeAreasDataControlList )
			activeAreaDataControl.deleteAssetReferences( assetPath );
	}

	@Override
	public int countIdentifierReferences( String id ) {
		int count = 0;

		// Iterate through each activeArea
		for( ActiveAreaDataControl activeAreaDataControl : activeAreasDataControlList )
			count += activeAreaDataControl.countIdentifierReferences( id );

		return count;
	}

	@Override
	public void replaceIdentifierReferences( String oldId, String newId ) {
		// Iterate through each activeArea
		for( ActiveAreaDataControl activeAreaDataControl : activeAreasDataControlList )
			activeAreaDataControl.replaceIdentifierReferences( oldId, newId );
	}

	@Override
	public void deleteIdentifierReferences( String id ) {
		// Spread the call to every activeArea
		for( ActiveAreaDataControl activeAreaDataControl : activeAreasDataControlList )
			activeAreaDataControl.deleteIdentifierReferences( id );

	}

	@Override
	public boolean canBeDuplicated( ) {
		return false;
	}

}
