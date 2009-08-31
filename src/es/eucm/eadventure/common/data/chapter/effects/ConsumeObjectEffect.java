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
package es.eucm.eadventure.common.data.chapter.effects;

import es.eucm.eadventure.common.data.HasTargetId;

/**
 * An effect that consumes an object in the inventory
 */
public class ConsumeObjectEffect extends AbstractEffect implements  HasTargetId {

	/**
	 * Id of the item to be consumed
	 */
	private String idTarget;

	/**
	 * Creates a new ConsumeObjectEffect.
	 * 
	 * @param idTarget
	 *            the id of the object to be consumed
	 */
	public ConsumeObjectEffect( String idTarget ) {
	    	super();
		this.idTarget = idTarget;
	}

	public int getType( ) {
		return CONSUME_OBJECT;
	}

	/**
	 * Returns the idTarget
	 * 
	 * @return String containing the idTarget
	 */
	public String getTargetId( ) {
		return idTarget;
	}

	/**
	 * Sets the new idTarget
	 * 
	 * @param idTarget
	 *            New idTarget
	 */
	public void setTargetId( String idTarget ) {
		this.idTarget = idTarget;
	}
	
	public Object clone() throws CloneNotSupportedException {
		ConsumeObjectEffect coe = (ConsumeObjectEffect) super.clone();
		coe.idTarget = (idTarget != null ? new String(idTarget) : null);
		return coe;
	}
}
