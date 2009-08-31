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
package es.eucm.eadventure.common.data.chapter.resources;

import java.util.HashMap;

import es.eucm.eadventure.common.data.chapter.conditions.Conditions;

/**
 * The list of resources of an element in the game under certain conditions.
 */
public class Resources implements Cloneable {

    /**
     * Conditions of the resource
     */
    private Conditions conditions;

    /**
     * List of single assets stored in the resources structure
     */
    private HashMap<String, String> assets;

    /**
     * Creates a new Resources.
     */
    public Resources( ) {

        assets = new HashMap<String, String>( );
        conditions = new Conditions( );
    }

    /**
     * Inserts a new asset in the resources.
     * 
     * @param type
     *            Identifier of the asset
     * @param path
     *            Path of the asset
     */
    public boolean addAsset( String type, String path ) {

        boolean alreadyExists = existAsset( type ) && getAssetPath( type ).equals( path );
        // Remove the asset (if it was present), and insert the new one
        deleteAsset( type );
        assets.put( type, path );
        return !alreadyExists;
    }

    /**
     * Adds an asset to the resources.
     * 
     * @param asset
     *            the asset to be added
     */
    public boolean addAsset( Asset asset ) {

        return addAsset( asset.getType( ), asset.getPath( ) );
    }

    /**
     * Deletes the given asset from the resources block.
     * 
     * @param type
     *            Identifier of the asset
     */
    public void deleteAsset( String type ) {

        assets.remove( type );
    }

    /**
     * Returns the number of assets contained.
     * 
     * @return Number of assets
     */
    public int getAssetCount( ) {

        return assets.size( );
    }

    /**
     * Returns an array with all the types of the resources.
     * 
     * @return Array with the resources types
     */
    public String[] getAssetTypes( ) {

        return assets.keySet( ).toArray( new String[] {} );
    }

    /**
     * Returns an array with all the values of the resources.
     * 
     * @return Array with the resources values
     */
    public String[] getAssetValues( ) {

        return assets.values( ).toArray( new String[] {} );
    }

    /**
     * Returns the path associated with the given type.
     * 
     * @param type
     *            Identifier of the asset
     * @return Path to the asset if present, null otherwise
     */
    public String getAssetPath( String type ) {

        return assets.get( type );
    }

    /**
     * Returns the conditions to the resources.
     * 
     * @return the conditions to the resources
     */
    public Conditions getConditions( ) {

        return conditions;
    }

    /**
     * Sets the conditions to the resources.
     * 
     * @param conditions
     *            the new conditions.
     */
    public void setConditions( Conditions conditions ) {

        this.conditions = conditions;
    }

    /**
     * Returns whether exists an asset of the given type.
     * 
     * @param type
     *            the type of the asset
     * @return whether exists an asset of the given type
     */
    public boolean existAsset( String type ) {

        boolean existAsset = false;

        existAsset = assets.containsKey( type ) && assets.get( type ) != null;

        return existAsset;
    }

    @Override
    public Object clone( ) throws CloneNotSupportedException {

        Resources r = (Resources) super.clone( );
        if( assets != null ) {
            r.assets = new HashMap<String, String>( );
            for( String s : assets.keySet( ) ) {
                String s2 = ( assets.get( s ) != null ? new String( assets.get( s ) ) : null );
                r.assets.put( new String( s ), s2 );
            }
        }
        r.conditions = ( conditions != null ? (Conditions) conditions.clone( ) : null );
        return r;
    }

    /**
     * Deletes all the resources in the structure. Needed for undo tool
     * 
     * @return
     */
    public void clearAssets( ) {

        assets.clear( );
    }

}
