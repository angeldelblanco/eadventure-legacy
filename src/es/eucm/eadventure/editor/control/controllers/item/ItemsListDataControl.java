package es.eucm.eadventure.editor.control.controllers.item;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.data.chapter.elements.Item;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.DataControl;
import es.eucm.eadventure.editor.control.controllers.cutscene.CutsceneDataControl;
import es.eucm.eadventure.editor.data.supportdata.FlagSummary;
import es.eucm.eadventure.editor.gui.TextConstants;

public class ItemsListDataControl extends DataControl {

	/**
	 * List of items.
	 */
	private List<Item> itemsList;

	/**
	 * List of item controllers.
	 */
	private List<ItemDataControl> itemsDataControlList;

	/**
	 * Constructor.
	 * 
	 * @param itemsList
	 *            List of items
	 */
	public ItemsListDataControl( List<Item> itemsList ) {
		this.itemsList = itemsList;

		// Create subcontrollers
		itemsDataControlList = new ArrayList<ItemDataControl>( );
		for( Item item : itemsList )
			itemsDataControlList.add( new ItemDataControl( item ) );
	}

	/**
	 * Returns the list of item controllers.
	 * 
	 * @return Item controllers
	 */
	public List<ItemDataControl> getItems( ) {
		return itemsDataControlList;
	}

	/**
	 * Returns the last item controller from the list.
	 * 
	 * @return Last item controller
	 */
	public ItemDataControl getLastItem( ) {
		return itemsDataControlList.get( itemsDataControlList.size( ) - 1 );
	}

	/**
	 * Returns the info of the items contained in the list.
	 * 
	 * @return Array with the information of the items. It contains the identifier of each item, and the number of
	 *         actions
	 */
	public String[][] getItemsInfo( ) {
		String[][] itemsInfo = null;

		// Create the list for the items
		itemsInfo = new String[itemsList.size( )][2];

		// Fill the array with the info
		for( int i = 0; i < itemsList.size( ); i++ ) {
			Item item = itemsList.get( i );
			itemsInfo[i][0] = item.getId( );
			itemsInfo[i][1] = TextConstants.getText( "ItemsList.ActionsNumber", String.valueOf( item.getActions( ).size( ) ) );
		}

		return itemsInfo;
	}

	@Override
	public Object getContent( ) {
		return itemsList;
	}

	@Override
	public int[] getAddableElements( ) {
		return new int[] { Controller.ITEM };
	}

	@Override
	public boolean canAddElement( int type ) {
		// It can always add new items
		return type == Controller.ITEM;
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

		if( type == Controller.ITEM ) {

			// Show a dialog asking for the item id
			String itemId = controller.showInputDialog( TextConstants.getText( "Operation.AddItemTitle" ), TextConstants.getText( "Operation.AddItemMessage" ), TextConstants.getText( "Operation.AddItemDefaultValue" ) );

			// If some value was typed and the identifier is valid
			if( itemId != null && controller.isElementIdValid( itemId ) ) {
				// Add thew new item
				Item newItem = new Item( itemId );
				itemsList.add( newItem );
				itemsDataControlList.add( new ItemDataControl( newItem ) );
				controller.getIdentifierSummary( ).addItemId( itemId );
				controller.dataModified( );
				elementAdded = true;
			}
		}

		return elementAdded;
	}

	@Override
	public boolean deleteElement( DataControl dataControl ) {
		boolean elementDeleted = false;
		String itemId = ( (ItemDataControl) dataControl ).getId( );
		String references = String.valueOf( controller.countIdentifierReferences( itemId ) );

		// Ask for confirmation
		if( controller.showStrictConfirmDialog( TextConstants.getText( "Operation.DeleteElementTitle" ), TextConstants.getText( "Operation.DeleteElementWarning", new String[] { itemId, references } ) ) ) {
			if( itemsList.remove( dataControl.getContent( ) ) ) {
				itemsDataControlList.remove( dataControl );
				controller.deleteIdentifierReferences( itemId );
				controller.getIdentifierSummary( ).deleteItemId( itemId );
				controller.dataModified( );
				elementDeleted = true;
			}
		}

		return elementDeleted;
	}

	@Override
	public boolean moveElementUp( DataControl dataControl ) {
		boolean elementMoved = false;
		int elementIndex = itemsList.indexOf( dataControl.getContent( ) );

		if( elementIndex > 0 ) {
			itemsList.add( elementIndex - 1, itemsList.remove( elementIndex ) );
			itemsDataControlList.add( elementIndex - 1, itemsDataControlList.remove( elementIndex ) );
			controller.dataModified( );
			elementMoved = true;
		}

		return elementMoved;
	}

	@Override
	public boolean moveElementDown( DataControl dataControl ) {
		boolean elementMoved = false;
		int elementIndex = itemsList.indexOf( dataControl.getContent( ) );

		if( elementIndex < itemsList.size( ) - 1 ) {
			itemsList.add( elementIndex + 1, itemsList.remove( elementIndex ) );
			itemsDataControlList.add( elementIndex + 1, itemsDataControlList.remove( elementIndex ) );
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
		// Iterate through each item
		for( ItemDataControl itemDataControl : itemsDataControlList )
			itemDataControl.updateFlagSummary( flagSummary );
	}

	@Override
	public boolean isValid( String currentPath, List<String> incidences ) {
		boolean valid = true;

		// Update the current path
		currentPath += " >> " + TextConstants.getElementName( Controller.ITEMS_LIST );

		// Iterate through the items
		for( ItemDataControl itemDataControl : itemsDataControlList ) {
			String itemPath = currentPath + " >> " + itemDataControl.getId( );
			valid &= itemDataControl.isValid( itemPath, incidences );
		}

		return valid;
	}

	@Override
	public int countAssetReferences( String assetPath ) {
		int count = 0;

		// Iterate through each item
		for( ItemDataControl itemDataControl : itemsDataControlList )
			count += itemDataControl.countAssetReferences( assetPath );

		return count;
	}
	
	@Override
	public void getAssetReferences( List<String> assetPaths, List<Integer> assetTypes ) {
		// Iterate through each item
		for( ItemDataControl itemDataControl : itemsDataControlList )
			itemDataControl.getAssetReferences( assetPaths, assetTypes );
	}


	@Override
	public void deleteAssetReferences( String assetPath ) {
		// Iterate through each item
		for( ItemDataControl itemDataControl : itemsDataControlList )
			itemDataControl.deleteAssetReferences( assetPath );
	}

	@Override
	public int countIdentifierReferences( String id ) {
		int count = 0;

		// Iterate through each item
		for( ItemDataControl itemDataControl : itemsDataControlList )
			count += itemDataControl.countIdentifierReferences( id );

		return count;
	}

	@Override
	public void replaceIdentifierReferences( String oldId, String newId ) {
		// Iterate through each item
		for( ItemDataControl itemDataControl : itemsDataControlList )
			itemDataControl.replaceIdentifierReferences( oldId, newId );
	}

	@Override
	public void deleteIdentifierReferences( String id ) {
		// Spread the call to every item
		for( ItemDataControl itemDataControl : itemsDataControlList )
			itemDataControl.deleteIdentifierReferences( id );
	}

	@Override
	public boolean canBeDuplicated( ) {
		// TODO Auto-generated method stub
		return false;
	}
}
