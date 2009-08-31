/**
 * <e-Adventure> is an <e-UCM> research project. <e-UCM>, Department of Software
 * Engineering and Artificial Intelligence. Faculty of Informatics, Complutense
 * University of Madrid (Spain).
 * 
 * @author Del Blanco, A., Marchiori, E., Torrente, F.J. (alphabetical order) *
 * @author L�pez Ma�as, E., P�rez Padilla, F., Sollet, E., Torijano, B. (former
 *         developers by alphabetical order)
 * @author Moreno-Ger, P. & Fern�ndez-Manj�n, B. (directors)
 * @year 2009 Web-site: http://e-adventure.e-ucm.es
 */

/*
 * Copyright (C) 2004-2009 <e-UCM> research group
 * 
 * This file is part of <e-Adventure> project, an educational game & game-like
 * simulation authoring tool, available at http://e-adventure.e-ucm.es.
 * 
 * <e-Adventure> is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * <e-Adventure> is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * <e-Adventure>; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */
package es.eucm.eadventure.editor.control.controllers.globalstate;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.auxiliar.ReportDialog;
import es.eucm.eadventure.common.data.chapter.conditions.GlobalState;
import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.DataControl;
import es.eucm.eadventure.editor.control.controllers.Searchable;
import es.eucm.eadventure.editor.data.support.VarFlagSummary;

public class GlobalStateListDataControl extends DataControl {

    /**
     * List of globalStates.
     */
    private List<GlobalState> globalStatesList;

    /**
     * List of globalState controllers.
     */
    private List<GlobalStateDataControl> globalStatesDataControlList;

    /**
     * Constructor.
     * 
     * @param globalStatesList
     *            List of globalStates
     */
    public GlobalStateListDataControl( List<GlobalState> globalStatesList ) {

        this.globalStatesList = globalStatesList;

        // Create subcontrollers
        globalStatesDataControlList = new ArrayList<GlobalStateDataControl>( );
        for( GlobalState globalState : globalStatesList )
            globalStatesDataControlList.add( new GlobalStateDataControl( globalState ) );
    }

    /**
     * Returns the list of globalState controllers.
     * 
     * @return GlobalState controllers
     */
    public List<GlobalStateDataControl> getGlobalStates( ) {

        return globalStatesDataControlList;
    }

    /**
     * Returns the last globalState controller from the list.
     * 
     * @return Last globalState controller
     */
    public GlobalStateDataControl getLastGlobalState( ) {

        return globalStatesDataControlList.get( globalStatesDataControlList.size( ) - 1 );
    }

    /**
     * Returns the info of the globalStates contained in the list.
     * 
     * @return Array with the information of the globalStates. It contains the
     *         identifier of each globalState, and the number of actions
     */
    public String[][] getGlobalStatesInfo( ) {

        String[][] globalStatesInfo = null;

        // Create the list for the globalStates
        globalStatesInfo = new String[ globalStatesList.size( ) ][ 2 ];

        // Fill the array with the info
        for( int i = 0; i < globalStatesList.size( ); i++ ) {
            GlobalState globalState = globalStatesList.get( i );
            globalStatesInfo[i][0] = globalState.getId( );
            globalStatesInfo[i][1] = Integer.toString( Controller.getInstance( ).countIdentifierReferences( globalState.getId( ) ) );
        }

        return globalStatesInfo;
    }

    @Override
    public Object getContent( ) {

        return globalStatesList;
    }

    @Override
    public int[] getAddableElements( ) {

        return new int[] { Controller.GLOBAL_STATE };
    }

    @Override
    public boolean canAddElement( int type ) {

        // It can always add new globalStates
        return type == Controller.GLOBAL_STATE;
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
    public boolean addElement( int type, String globalStateId ) {

        boolean elementAdded = false;

        if( type == Controller.GLOBAL_STATE ) {

            // Show a dialog asking for the globalState id
            if( globalStateId == null )
                globalStateId = controller.showInputDialog( TextConstants.getText( "Operation.AddGlobalStateTitle" ), TextConstants.getText( "Operation.AddGlobalStateMessage" ), TextConstants.getText( "Operation.AddGlobalStateDefaultValue" ) );

            // If some value was typed and the identifier is valid
            if( globalStateId != null && controller.isElementIdValid( globalStateId ) ) {
                // Add thew new globalState
                GlobalState newGlobalState = new GlobalState( globalStateId );
                globalStatesList.add( newGlobalState );
                globalStatesDataControlList.add( new GlobalStateDataControl( newGlobalState ) );
                controller.getIdentifierSummary( ).addGlobalStateId( globalStateId );
                //controller.dataModified( );
                elementAdded = true;
            }
        }

        return elementAdded;
    }

    @Override
    public boolean duplicateElement( DataControl dataControl ) {

        if( !( dataControl instanceof GlobalStateDataControl ) )
            return false;

        try {
            GlobalState newElement = (GlobalState) ( ( (GlobalState) ( dataControl.getContent( ) ) ).clone( ) );
            String id = newElement.getId( );
            int i = 1;
            do {
                id = newElement.getId( ) + i;
                i++;
            } while( !controller.isElementIdValid( id, false ) );
            newElement.setId( id );
            globalStatesList.add( newElement );
            globalStatesDataControlList.add( new GlobalStateDataControl( newElement ) );
            controller.getIdentifierSummary( ).addGlobalStateId( id );
            return true;
        }
        catch( CloneNotSupportedException e ) {
            ReportDialog.GenerateErrorReport( e, true, "Could not clone global state" );
            return false;
        }
    }

    @Override
    public String getDefaultId( int type ) {

        return TextConstants.getText( "Operation.AddGlobalStateDefaultValue" );
    }

    @Override
    public boolean deleteElement( DataControl dataControl, boolean askConfirmation ) {

        boolean elementDeleted = false;
        String globalStateId = ( (GlobalStateDataControl) dataControl ).getId( );
        String references = String.valueOf( controller.countIdentifierReferences( globalStateId ) );

        // Ask for confirmation
        if( !askConfirmation || controller.showStrictConfirmDialog( TextConstants.getText( "Operation.DeleteElementTitle" ), TextConstants.getText( "Operation.DeleteElementWarning", new String[] { globalStateId, references } ) ) ) {
            if( globalStatesList.remove( dataControl.getContent( ) ) ) {
                globalStatesDataControlList.remove( dataControl );
                controller.deleteIdentifierReferences( globalStateId );
                controller.getIdentifierSummary( ).deleteGlobalStateId( globalStateId );
                //controller.dataModified( );
                elementDeleted = true;
            }
        }

        return elementDeleted;
    }

    @Override
    public boolean moveElementUp( DataControl dataControl ) {

        boolean elementMoved = false;
        int elementIndex = globalStatesList.indexOf( dataControl.getContent( ) );

        if( elementIndex > 0 ) {
            globalStatesList.add( elementIndex - 1, globalStatesList.remove( elementIndex ) );
            globalStatesDataControlList.add( elementIndex - 1, globalStatesDataControlList.remove( elementIndex ) );
            //controller.dataModified( );
            elementMoved = true;
        }

        return elementMoved;
    }

    @Override
    public boolean moveElementDown( DataControl dataControl ) {

        boolean elementMoved = false;
        int elementIndex = globalStatesList.indexOf( dataControl.getContent( ) );

        if( elementIndex < globalStatesList.size( ) - 1 ) {
            globalStatesList.add( elementIndex + 1, globalStatesList.remove( elementIndex ) );
            globalStatesDataControlList.add( elementIndex + 1, globalStatesDataControlList.remove( elementIndex ) );
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
    public void updateVarFlagSummary( VarFlagSummary varFlagSummary ) {

        // Iterate through each globalState
        for( GlobalStateDataControl globalStateDataControl : globalStatesDataControlList )
            globalStateDataControl.updateVarFlagSummary( varFlagSummary );
    }

    @Override
    public boolean isValid( String currentPath, List<String> incidences ) {

        boolean valid = true;

        // Update the current path
        currentPath += " >> " + TextConstants.getElementName( Controller.GLOBAL_STATE_LIST );

        // Iterate through the globalStates
        for( GlobalStateDataControl globalStateDataControl : globalStatesDataControlList ) {
            String globalStatePath = currentPath + " >> " + globalStateDataControl.getId( );
            valid &= globalStateDataControl.isValid( globalStatePath, incidences );
        }

        return valid;
    }

    @Override
    public int countAssetReferences( String assetPath ) {

        int count = 0;

        // Iterate through each globalState
        for( GlobalStateDataControl globalStateDataControl : globalStatesDataControlList )
            count += globalStateDataControl.countAssetReferences( assetPath );

        return count;
    }

    @Override
    public void getAssetReferences( List<String> assetPaths, List<Integer> assetTypes ) {

        // Iterate through each globalState
        for( GlobalStateDataControl globalStateDataControl : globalStatesDataControlList )
            globalStateDataControl.getAssetReferences( assetPaths, assetTypes );
    }

    @Override
    public void deleteAssetReferences( String assetPath ) {

        // Iterate through each globalState
        for( GlobalStateDataControl globalStateDataControl : globalStatesDataControlList )
            globalStateDataControl.deleteAssetReferences( assetPath );
    }

    @Override
    public int countIdentifierReferences( String id ) {

        int count = 0;

        // Iterate through each globalState
        for( GlobalStateDataControl globalStateDataControl : globalStatesDataControlList )
            count += globalStateDataControl.countIdentifierReferences( id );

        return count;
    }

    @Override
    public void replaceIdentifierReferences( String oldId, String newId ) {

        // Iterate through each globalState
        for( GlobalStateDataControl globalStateDataControl : globalStatesDataControlList )
            globalStateDataControl.replaceIdentifierReferences( oldId, newId );
    }

    @Override
    public void deleteIdentifierReferences( String id ) {

        // Spread the call to every globalState
        for( GlobalStateDataControl globalStateDataControl : globalStatesDataControlList ) {
            globalStateDataControl.deleteIdentifierReferences( id );

        }
    }

    @Override
    public boolean canBeDuplicated( ) {

        return false;
    }

    @Override
    public void recursiveSearch( ) {

        for( DataControl dc : this.globalStatesDataControlList )
            dc.recursiveSearch( );
    }

    @Override
    public List<Searchable> getPathToDataControl( Searchable dataControl ) {

        return getPathFromChild( dataControl, globalStatesDataControlList );
    }

    public List<GlobalState> getGlobalStatesList( ) {

        return this.globalStatesList;
    }

}
