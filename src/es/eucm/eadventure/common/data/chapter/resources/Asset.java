/**
 * <e-Adventure> is an <e-UCM> research project.
 * <e-UCM>, Department of Software Engineering and Artificial Intelligence.
 * Faculty of Informatics, Complutense University of Madrid (Spain).
 * @author Del Blanco, A., Marchiori, E., Torrente, F.J.
 * @author Moreno-Ger, P. & Fern�ndez-Manj�n, B. (directors)
 * @year 2009
 * Web-site: http://e-adventure.e-ucm.es
 */

/*
    Copyright (C) 2004-2009 <e-UCM> research group

    This file is part of <e-Adventure> project, an educational game & game-like 
    simulation authoring tool, availabe at http://e-adventure.e-ucm.es. 

    <e-Adventure> is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    <e-Adventure> is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with <e-Adventure>; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*/
package es.eucm.eadventure.common.data.chapter.resources;

/**
 * Defines an asset of any type (background, slides, image, icon, [...],
 * or the animatinos of the characters.
 */
public class Asset implements Cloneable {
    
    /**
     * String with the type of the asset
     */
    private String type;
    
    /**
     * Path of the asset
     */
    private String path;
    
    /**
     * Creates a new asset
     * @param type the type of the asset
     * @param path the path of the asset
     */
    public Asset( String type, String path ) {
        this.type = type;
        this.path = path;
    }

    /**
     * Returns the type of the asset.
     * @return the type of the asset
     */
    public String getType( ) {
        return type;
    }

    /**
     * Returns the path of the asset.
     * @return the path of the asset
     */
    public String getPath( ) {
        return path;
    }
    
	public Object clone() throws CloneNotSupportedException {
		Asset a = (Asset) super.clone();
		a.path = (path != null ? new String(path) : null);
		a.type = (type != null ? new String(type) : null);
		return a;
	}
}
