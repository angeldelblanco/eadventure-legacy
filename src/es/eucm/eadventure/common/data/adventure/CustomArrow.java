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
package es.eucm.eadventure.common.data.adventure;

public class CustomArrow implements Cloneable {

    private String type;
    
    private String path;
    
	/**
     * @return the type
     */
    public String getType( ) {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * @return the path
     */
    public String getPath( ) {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath( String path ) {
        this.path = path;
    }

    /**
     * @param type
     * @param path
     */
    public CustomArrow( String type, String path ) {
    	this.type = type;
        this.path = path;
    }
    
    public boolean equals(Object o) {
    	if (o == null || !(o instanceof CustomArrow))
    		return false;
    	CustomArrow button = (CustomArrow) o;
    	if (button.type.equals(type))
    		return true;
    	return false;
    }
 
    
	public Object clone() throws CloneNotSupportedException {
		CustomArrow cb = (CustomArrow) super.clone();
		cb.path = (path != null ? new String(path) : null);
		cb.type = (type != null ? new String(type) : null);
		return cb;
	}
}
