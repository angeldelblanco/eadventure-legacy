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
 * An effect that decrements a var according to a given value
 */
public class DecrementVarEffect extends AbstractEffect implements HasTargetId {

	/**
	 * Name of the var
	 */
	private String idVar;
	
	/**
	 * Value to be decremented
	 */
	private int value;

	/**
	 * Creates a new Activate effect.
	 * 
	 * @param idVar
	 *            the id of the var to be activated
	 */
	public DecrementVarEffect( String idVar, int value ) {
	    	super();
		this.idVar = idVar;
		this.value = value;
	}

	public int getType( ) {
		return DECREMENT_VAR;
	}

	/**
	 * Returns the idVar
	 * 
	 * @return String containing the idVar
	 */
	public String getTargetId( ) {
		return idVar;
	}

	/**
	 * Sets the new idVar
	 * 
	 * @param idVar
	 *            New idVar
	 */
	public void setTargetId( String idVar ) {
		this.idVar = idVar;
	}

	/**
	 * @return the value
	 */
	public int getDecrement() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setDecrement(int value) {
		this.value = value;
	}
	
	public Object clone() throws CloneNotSupportedException {
		DecrementVarEffect dve = (DecrementVarEffect) super.clone();
		dve.idVar = (idVar != null ? new String(idVar) : null);
		dve.value = value;
		return dve;
	}
}
