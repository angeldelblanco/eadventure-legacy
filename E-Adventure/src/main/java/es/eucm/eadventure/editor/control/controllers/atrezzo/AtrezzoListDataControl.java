/*******************************************************************************
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the e-UCM
 *          research group.
 *   
 *    Copyright 2005-2012 e-UCM research group.
 *  
 *     e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *  
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *  
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *  
 *  ****************************************************************************
 * This file is part of eAdventure, version 1.5.
 * 
 *   You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *  
 *  ****************************************************************************
 *       eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *  
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *  
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with Adventure.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.eucm.eadventure.editor.control.controllers.atrezzo;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.auxiliar.ReportDialog;
import es.eucm.eadventure.common.data.chapter.elements.Atrezzo;
import es.eucm.eadventure.common.gui.TC;

import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.DataControl;
import es.eucm.eadventure.editor.control.controllers.Searchable;
import es.eucm.eadventure.editor.data.support.VarFlagSummary;

public class AtrezzoListDataControl extends DataControl {

    /**
     * List of atrezzo items.
     */
    private List<Atrezzo> atrezzoList;

    /**
     * List of atrezzo item controllers.
     */
    private List<AtrezzoDataControl> atrezzoDataControlList;

    /**
     * Constructor.
     * 
     * @param itemsList
     *            List of items
     */
    public AtrezzoListDataControl( List<Atrezzo> atrezzoList ) {

        this.atrezzoList = atrezzoList;

        // Create subcontrollers
        atrezzoDataControlList = new ArrayList<AtrezzoDataControl>( );
        for( Atrezzo atrezzo : atrezzoList )
            atrezzoDataControlList.add( new AtrezzoDataControl( atrezzo ) );
    }

    /**
     * Returns the list of atrezzo item controllers.
     * 
     * 
     * @return Atrezzo controllers
     */
    public List<AtrezzoDataControl> getAtrezzoList( ) {

        return atrezzoDataControlList;
    }

    /**
     * Returns the last atrezzo item controller from the list.
     * 
     * @return Last item controller
     */
    public AtrezzoDataControl getLastAtrezzo( ) {

        return atrezzoDataControlList.get( atrezzoDataControlList.size( ) - 1 );
    }

    /**
     * Returns the info of the atrezzo items contained in the list.
     * 
     * @return Array with the information of the atrezzo items. It contains the
     *         identifier of each atrezzo item.
     */
    public String[][] getItemsInfo( ) {

        String[][] atrezzoInfo = null;

        // Create the list for the items
        atrezzoInfo = new String[ atrezzoList.size( ) ][ 1 ];

        // Fill the array with the info
        for( int i = 0; i < atrezzoList.size( ); i++ )
            atrezzoInfo[i][0] = atrezzoList.get( i ).getId( );

        return atrezzoInfo;
    }

    @Override
    public boolean addElement( int type, String atrezzoId ) {

        boolean elementAdded = false;

        if( type == Controller.ATREZZO ) {

            // Show a dialog asking for the item id
            if( atrezzoId == null )
                atrezzoId = controller.showInputDialog( TC.get( "Operation.AddAtrezzoTitle" ), TC.get( "Operation.AddAtrezzoMessage" ), TC.get( "Operation.AddAtrezzoDefaultValue" ) );

            // If some value was typed and the identifier is valid
            if( atrezzoId != null && controller.isElementIdValid( atrezzoId ) ) {
                // Add thew new item
                Atrezzo newAtrezzo = new Atrezzo( atrezzoId );
                atrezzoList.add( newAtrezzo );
                atrezzoDataControlList.add( new AtrezzoDataControl( newAtrezzo ) );
                controller.getIdentifierSummary( ).addAtrezzoId( atrezzoId );
                //controller.dataModified( );
                elementAdded = true;
            }
        }

        return elementAdded;
    }

    @Override
    public boolean duplicateElement( DataControl dataControl ) {

        if( !( dataControl instanceof AtrezzoDataControl ) )
            return false;

        try {
            Atrezzo newElement = (Atrezzo) ( ( (Atrezzo) ( dataControl.getContent( ) ) ).clone( ) );
            String id = newElement.getId( );
            int i = 1;
            do {
                id = newElement.getId( ) + i;
                i++;
            } while( !controller.isElementIdValid( id, false ) );
            newElement.setId( id );
            atrezzoList.add( newElement );
            atrezzoDataControlList.add( new AtrezzoDataControl( newElement ) );
            controller.getIdentifierSummary( ).addAtrezzoId( id );
            return true;
        }
        catch( CloneNotSupportedException e ) {
            ReportDialog.GenerateErrorReport( e, true, "Could not clone atrezzo" );
            return false;
        }
    }

    @Override
    public String getDefaultId( int type ) {

        return TC.get( "Operation.AddAtrezzoDefaultValue" );
    }

    @Override
    public boolean canAddElement( int type ) {

        // It can always add new atrezzo items
        return type == Controller.ATREZZO;
    }

    @Override
    public boolean canBeDeleted( ) {

        return false;
    }

    @Override
    public boolean canBeDuplicated( ) {

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
    public int countAssetReferences( String assetPath ) {

        int count = 0;

        // Iterate through each atrezzo item
        for( AtrezzoDataControl atrezzoDataControl : atrezzoDataControlList )
            count += atrezzoDataControl.countAssetReferences( assetPath );

        return count;
    }

    @Override
    public int countIdentifierReferences( String id ) {

        int count = 0;

        // Iterate through each atrezzo item
        for( AtrezzoDataControl atrezzoDataControl : atrezzoDataControlList )
            count += atrezzoDataControl.countIdentifierReferences( id );

        return count;

    }

    @Override
    public void deleteAssetReferences( String assetPath ) {

        // Iterate through each atrezzo item
        for( AtrezzoDataControl atrezzoDataControl : atrezzoDataControlList )
            atrezzoDataControl.deleteIdentifierReferences( assetPath );

    }

    @Override
    public boolean deleteElement( DataControl dataControl, boolean askConfirmation ) {

        boolean elementDeleted = false;
        String atrezzoId = ( (AtrezzoDataControl) dataControl ).getId( );
        String references = String.valueOf( controller.countIdentifierReferences( atrezzoId ) );

        // Ask for confirmation
        if( !askConfirmation || controller.showStrictConfirmDialog( TC.get( "Operation.DeleteElementTitle" ), TC.get( "Operation.DeleteElementWarning", new String[] { atrezzoId, references } ) ) ) {
            if( atrezzoList.remove( dataControl.getContent( ) ) ) {
                atrezzoDataControlList.remove( dataControl );
                controller.deleteIdentifierReferences( atrezzoId );
                controller.getIdentifierSummary( ).deleteItemId( atrezzoId );
                //controller.dataModified( );
                elementDeleted = true;
            }
        }

        return elementDeleted;
    }

    @Override
    public void deleteIdentifierReferences( String id ) {

        // This method is empty

    }

    @Override
    public int[] getAddableElements( ) {

        return new int[] { Controller.ATREZZO };
    }

    @Override
    public void getAssetReferences( List<String> assetPaths, List<Integer> assetTypes ) {

        // Iterate through each atrezzo item
        for( AtrezzoDataControl atrezzoDataControl : atrezzoDataControlList )
            atrezzoDataControl.getAssetReferences( assetPaths, assetTypes );

    }

    @Override
    public Object getContent( ) {

        return atrezzoList;
    }

    @Override
    public boolean isValid( String currentPath, List<String> incidences ) {

        boolean valid = true;

        // Update the current path
        currentPath += " >> " + TC.getElement( Controller.ATREZZO_LIST );

        // Iterate through the atrezzo items
        for( AtrezzoDataControl atrezzoDataControl : atrezzoDataControlList ) {
            String itemPath = currentPath + " >> " + atrezzoDataControl.getId( );
            valid &= atrezzoDataControl.isValid( itemPath, incidences );
        }

        return valid;
    }

    @Override
    public boolean moveElementDown( DataControl dataControl ) {

        boolean elementMoved = false;
        int elementIndex = atrezzoList.indexOf( dataControl.getContent( ) );

        if( elementIndex < atrezzoList.size( ) - 1 ) {
            atrezzoList.add( elementIndex + 1, atrezzoList.remove( elementIndex ) );
            atrezzoDataControlList.add( elementIndex + 1, atrezzoDataControlList.remove( elementIndex ) );
            //controller.dataModified( );
            elementMoved = true;
        }

        return elementMoved;
    }

    @Override
    public boolean moveElementUp( DataControl dataControl ) {

        boolean elementMoved = false;
        int elementIndex = atrezzoList.indexOf( dataControl.getContent( ) );

        if( elementIndex > 0 ) {
            atrezzoList.add( elementIndex - 1, atrezzoList.remove( elementIndex ) );
            atrezzoDataControlList.add( elementIndex - 1, atrezzoDataControlList.remove( elementIndex ) );
            //controller.dataModified( );
            elementMoved = true;
        }

        return elementMoved;
    }

    @Override
    public String renameElement( String name ) {

        return null;
    }

    @Override
    public void replaceIdentifierReferences( String oldId, String newId ) {

        // Iterate through each item
        for( AtrezzoDataControl atrezzoDataControl : atrezzoDataControlList )
            atrezzoDataControl.replaceIdentifierReferences( oldId, newId );
    }

    @Override
    public void updateVarFlagSummary( VarFlagSummary varFlagSummary ) {

        // Iterate through each item
        for( AtrezzoDataControl atrezzoDataControl : atrezzoDataControlList )
            atrezzoDataControl.updateVarFlagSummary( varFlagSummary );
    }

    @Override
    public void recursiveSearch( ) {

        for( DataControl dc : this.atrezzoDataControlList )
            dc.recursiveSearch( );
    }

    @Override
    public List<Searchable> getPathToDataControl( Searchable dataControl ) {

        return getPathFromChild( dataControl, atrezzoDataControlList );
    }

}
