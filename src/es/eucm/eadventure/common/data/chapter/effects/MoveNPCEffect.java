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
 * An effect that makes a character to walk to a given position.
 */
public class MoveNPCEffect extends AbstractEffect implements HasTargetId {

	/**
	 * Id of the npc who will walk
	 */
	private String idTarget;

	/**
	 * The destination of the npc
	 */
	private int x;

	private int y;

	/**
	 * Creates a new FunctionalMoveNPCEffect.
	 * 
	 * @param idTarget
	 *            the id of the character who will walk
	 * @param x
	 *            X final position for the NPC
	 * @param y
	 *            Y final position for the NPC
	 */
	public MoveNPCEffect( String idTarget, int x, int y ) {
	    	super();
		this.idTarget = idTarget;
		this.x = x;
		this.y = y;
	}

	public int getType( ) {
		return MOVE_NPC;
	}

	/**
	 * Returns the id target.
	 * 
	 * @return Id target
	 */
	public String getTargetId( ) {
		return idTarget;
	}

	/**
	 * Sets the new id target.
	 * 
	 * @param idTarget
	 *            New id target
	 */
	public void setTargetId( String idTarget ) {
		this.idTarget = idTarget;
	}

	/**
	 * Returns the destiny x position.
	 * 
	 * @return Destiny x coord
	 */
	public int getX( ) {
		return x;
	}

	/**
	 * Returns the destiny y position.
	 * 
	 * @return Destiny y coord
	 */
	public int getY( ) {
		return y;
	}

	/**
	 * Sets the new destiny position
	 * 
	 * @param x
	 *            New destiny X coordinate
	 * @param y
	 *            New destiny Y coordinate
	 */
	public void setDestiny( int x, int y ) {
		this.x = x;
		this.y = y;
	}

	public Object clone() throws CloneNotSupportedException {
		MoveNPCEffect npe = (MoveNPCEffect) super.clone();
		npe.idTarget = (idTarget != null ? new String(idTarget) : null);
		npe.x = x;
		npe.y = y;
		return npe;
	}
}