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
package es.eucm.eadventure.editor.control.controllers.macro;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.auxiliar.ReportDialog;
import es.eucm.eadventure.common.data.chapter.effects.Macro;
import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.DataControl;
import es.eucm.eadventure.editor.control.controllers.Searchable;
import es.eucm.eadventure.editor.data.support.VarFlagSummary;

public class MacroListDataControl extends DataControl {

    /**
     * List of macros.
     */
    private List<Macro> macrosList;

    /**
     * List of macro controllers.
     */
    private List<MacroDataControl> macrosDataControlList;

    /**
     * Constructor.
     * 
     * @param macrosList
     *            List of macros
     */
    public MacroListDataControl( List<Macro> macrosList ) {

        this.macrosList = macrosList;

        // Create subcontrollers
        macrosDataControlList = new ArrayList<MacroDataControl>( );
        for( Macro macro : macrosList )
            macrosDataControlList.add( new MacroDataControl( macro ) );
    }

    /**
     * Returns the list of macro controllers.
     * 
     * @return Macro controllers
     */
    public List<MacroDataControl> getMacros( ) {

        return macrosDataControlList;
    }

    /**
     * Returns the last macro controller from the list.
     * 
     * @return Last macro controller
     */
    public MacroDataControl getLastMacro( ) {

        return macrosDataControlList.get( macrosDataControlList.size( ) - 1 );
    }

    /**
     * Returns the info of the macros contained in the list.
     * 
     * @return Array with the information of the macros. It contains the
     *         identifier of each macro, and the number of actions
     */
    public String[][] getMacrosInfo( ) {

        String[][] macrosInfo = null;

        // Create the list for the macros
        macrosInfo = new String[ macrosList.size( ) ][ 2 ];

        // Fill the array with the info
        for( int i = 0; i < macrosList.size( ); i++ ) {
            Macro macro = macrosList.get( i );
            macrosInfo[i][0] = macro.getId( );
            macrosInfo[i][1] = Integer.toString( Controller.getInstance( ).countIdentifierReferences( macro.getId( ) ) );
        }

        return macrosInfo;
    }

    @Override
    public Object getContent( ) {

        return macrosList;
    }

    @Override
    public int[] getAddableElements( ) {

        return new int[] { Controller.MACRO };
    }

    @Override
    public boolean canAddElement( int type ) {

        // It can always add new macros
        return type == Controller.MACRO;
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
    public boolean addElement( int type, String macroId ) {

        boolean elementAdded = false;

        if( type == Controller.MACRO ) {

            // Show a dialog asking for the macro id
            if( macroId == null )
                macroId = controller.showInputDialog( TextConstants.getText( "Operation.AddMacroTitle" ), TextConstants.getText( "Operation.AddMacroMessage" ), TextConstants.getText( "Operation.AddMacroDefaultValue" ) );

            // If some value was typed and the identifier is valid
            if( macroId != null && controller.isElementIdValid( macroId ) ) {
                // Add thew new macro
                Macro newMacro = new Macro( macroId );
                macrosList.add( newMacro );
                macrosDataControlList.add( new MacroDataControl( newMacro ) );
                controller.getIdentifierSummary( ).addMacroId( macroId );
                //controller.dataModified( );
                elementAdded = true;
            }
        }

        return elementAdded;
    }

    @Override
    public boolean duplicateElement( DataControl dataControl ) {

        if( !( dataControl instanceof MacroDataControl ) )
            return false;

        try {
            Macro newElement = (Macro) ( ( (Macro) ( dataControl.getContent( ) ) ).clone( ) );
            String id = newElement.getId( );
            int i = 1;
            do {
                id = newElement.getId( ) + i;
                i++;
            } while( !controller.isElementIdValid( id, false ) );
            newElement.setId( id );
            macrosList.add( newElement );
            macrosDataControlList.add( new MacroDataControl( newElement ) );
            controller.getIdentifierSummary( ).addMacroId( id );
            return true;
        }
        catch( CloneNotSupportedException e ) {
            ReportDialog.GenerateErrorReport( e, true, "Could not clone macro" );
            return false;
        }
    }

    @Override
    public String getDefaultId( int type ) {

        return TextConstants.getText( "Operation.AddMacroDefaultValue" );
    }

    @Override
    public boolean deleteElement( DataControl dataControl, boolean askConfirmation ) {

        boolean elementDeleted = false;
        String macroId = ( (MacroDataControl) dataControl ).getId( );
        String references = String.valueOf( controller.countIdentifierReferences( macroId ) );

        // Ask for confirmation
        if( !askConfirmation || controller.showStrictConfirmDialog( TextConstants.getText( "Operation.DeleteElementTitle" ), TextConstants.getText( "Operation.DeleteElementWarning", new String[] { macroId, references } ) ) ) {
            if( macrosList.remove( dataControl.getContent( ) ) ) {
                macrosDataControlList.remove( dataControl );
                controller.deleteIdentifierReferences( macroId );
                controller.getIdentifierSummary( ).deleteMacroId( macroId );
                //controller.dataModified( );
                elementDeleted = true;
            }
        }

        return elementDeleted;
    }

    @Override
    public boolean moveElementUp( DataControl dataControl ) {

        boolean elementMoved = false;
        int elementIndex = macrosList.indexOf( dataControl.getContent( ) );

        if( elementIndex > 0 ) {
            macrosList.add( elementIndex - 1, macrosList.remove( elementIndex ) );
            macrosDataControlList.add( elementIndex - 1, macrosDataControlList.remove( elementIndex ) );
            //controller.dataModified( );
            elementMoved = true;
        }

        return elementMoved;
    }

    @Override
    public boolean moveElementDown( DataControl dataControl ) {

        boolean elementMoved = false;
        int elementIndex = macrosList.indexOf( dataControl.getContent( ) );

        if( elementIndex < macrosList.size( ) - 1 ) {
            macrosList.add( elementIndex + 1, macrosList.remove( elementIndex ) );
            macrosDataControlList.add( elementIndex + 1, macrosDataControlList.remove( elementIndex ) );
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

        // Iterate through each macro
        for( MacroDataControl macroDataControl : macrosDataControlList )
            macroDataControl.updateVarFlagSummary( varFlagSummary );
    }

    @Override
    public boolean isValid( String currentPath, List<String> incidences ) {

        boolean valid = true;

        // Update the current path
        currentPath += " >> " + TextConstants.getElementName( Controller.GLOBAL_STATE_LIST );

        // Iterate through the macros
        for( MacroDataControl macroDataControl : macrosDataControlList ) {
            String macroPath = currentPath + " >> " + macroDataControl.getId( );
            valid &= macroDataControl.isValid( macroPath, incidences );
        }

        return valid;
    }

    @Override
    public int countAssetReferences( String assetPath ) {

        int count = 0;

        // Iterate through each macro
        for( MacroDataControl macroDataControl : macrosDataControlList )
            count += macroDataControl.countAssetReferences( assetPath );

        return count;
    }

    @Override
    public void getAssetReferences( List<String> assetPaths, List<Integer> assetTypes ) {

        // Iterate through each macro
        for( MacroDataControl macroDataControl : macrosDataControlList )
            macroDataControl.getAssetReferences( assetPaths, assetTypes );
    }

    @Override
    public void deleteAssetReferences( String assetPath ) {

        // Iterate through each macro
        for( MacroDataControl macroDataControl : macrosDataControlList )
            macroDataControl.deleteAssetReferences( assetPath );
    }

    @Override
    public int countIdentifierReferences( String id ) {

        int count = 0;

        // Iterate through each macro
        for( MacroDataControl macroDataControl : macrosDataControlList )
            count += macroDataControl.countIdentifierReferences( id );

        return count;
    }

    @Override
    public void replaceIdentifierReferences( String oldId, String newId ) {

        // Iterate through each macro
        for( MacroDataControl macroDataControl : macrosDataControlList )
            macroDataControl.replaceIdentifierReferences( oldId, newId );
    }

    @Override
    public void deleteIdentifierReferences( String id ) {

        // Spread the call to every macro
        for( MacroDataControl macroDataControl : macrosDataControlList )
            macroDataControl.deleteIdentifierReferences( id );
    }

    @Override
    public boolean canBeDuplicated( ) {

        return false;
    }

    @Override
    public void recursiveSearch( ) {

        for( DataControl dc : this.macrosDataControlList )
            dc.recursiveSearch( );
    }

    @Override
    public List<Searchable> getPathToDataControl( Searchable dataControl ) {

        return getPathFromChild( dataControl, macrosDataControlList );
    }

    public List<Macro> getMacrosList( ) {

        return this.macrosList;
    }

}
