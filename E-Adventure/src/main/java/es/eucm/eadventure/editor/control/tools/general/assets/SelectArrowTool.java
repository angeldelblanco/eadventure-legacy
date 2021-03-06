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
package es.eucm.eadventure.editor.control.tools.general.assets;

import es.eucm.eadventure.common.auxiliar.AssetsConstants;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.adventure.CustomArrow;
import es.eucm.eadventure.common.data.adventure.DescriptorData;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.AssetsController;
import es.eucm.eadventure.editor.data.AssetInformation;

public class SelectArrowTool extends SelectResourceTool {

    private static final String AUDIO="audio";
    
    protected AdventureData adventureData;

    protected String type;

    protected boolean removed;

    protected static AssetInformation[] createAssetInfoArray( String type ) {

        AssetInformation[] array = new AssetInformation[ 1 ];
        if (type.startsWith( DescriptorData.SOUND_PATH )){
            array[0] = new AssetInformation(  "", type, true, AssetsConstants.CATEGORY_AUDIO, AssetsController.FILTER_NONE );
        } else {
            array[0] = new AssetInformation( "", type, true, AssetsConstants.CATEGORY_BUTTON, AssetsController.FILTER_NONE );
        }
        return array;
    }

    protected static Resources createResources( AdventureData adventureData, String type ) {

        Resources resources = new Resources( );
        boolean introduced = false;
        for( int i = 0; i < adventureData.getArrows( ).size( ); i++ ) {
            CustomArrow customArrow = adventureData.getArrows( ).get( i );
            if( customArrow.getType( ).equals( type ) ) {
                resources.addAsset( type, customArrow.getPath( ) );
                introduced = true;
                break;
            }
        }

        if( !introduced ) {
            resources.addAsset( type, null );
        }
        
        return resources;
    }

    public SelectArrowTool( AdventureData adventureData, String type ) throws CloneNotSupportedException {

        super( createResources( adventureData, type ), createAssetInfoArray( type ), Controller.RESOURCES, 0 );
        this.adventureData = adventureData;
        this.type = type;
    }

    @Override
    public boolean undoTool( ) {

        boolean done = super.undoTool( );
        if( !done )
            return false;
        else {
            for( int i = 0; i < adventureData.getArrows( ).size( ); i++ ) {
                if( adventureData.getArrows( ).get( i ).getType( ).equals( type ) ) {
                    if( removed )
                        adventureData.getArrows( ).remove( i );
                    else{
                        adventureData.getArrows( ).get( i ).setPath( resources.getAssetPath( type ) );
                    }
                    break;

                }
            }
            controller.updatePanel( );
            controller.dataModified( );
            return true;
        }

    }

    @Override
    public boolean redoTool( ) {

        if( removed )
            adventureData.addArrow( type, "" );
        boolean done = super.redoTool( );
        if( !done )
            return false;
        else {
            for( int i = 0; i < adventureData.getArrows( ).size( ); i++ ) {
                if( adventureData.getArrows( ).get( i ).getType( ).equals( type ) ) {
                    adventureData.getArrows( ).get( i ).setPath( resources.getAssetPath( type ) );
                }
            }
            controller.updatePanel( );
            return true;
        }
    }

    @Override
    public boolean doTool( ) {

      //  if( resources.getAssetPath( type ).equals( "NULL" ) ) {
        if( resources.getAssetPath( type ) == null ) {  
            removed = false;
        }
        else {
            for( int i = 0; i < adventureData.getArrows( ).size( ); i++ ) {
                CustomArrow arrow = adventureData.getArrows( ).get( i );
                if( arrow.getType( ).equals( type ) ) {
                    adventureData.getArrows( ).remove( arrow );
                    break;
                }
            }
            removed = true;
        }
        boolean done = super.doTool( );
        if( !done )
            return false;
        else {
            setArrow( type, resources.getAssetPath( type ) );
            return true;
        }
    }

    public void setArrow( String type, String path ) {

        CustomArrow arrow = new CustomArrow( type, path );
        CustomArrow temp = null;
        for( CustomArrow cb : adventureData.getArrows( ) ) {
            if( cb.equals( arrow ) )
                temp = cb;
        }
        if( temp != null )
            adventureData.getArrows( ).remove( temp );
        adventureData.addArrow( arrow );
    }
}
