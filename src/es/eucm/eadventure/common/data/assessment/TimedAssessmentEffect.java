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
package es.eucm.eadventure.common.data.assessment;


public class TimedAssessmentEffect extends AssessmentEffect {

	protected Integer minTime;
	
	protected Integer maxTime;
	
    public TimedAssessmentEffect (){
    	super();
    	minTime = 0;
    	maxTime = 120;
    }

	/**
	 * @return the minTime
	 */
	public Integer getMinTime( ) {
		return minTime;
	}

	/**
	 * @param minTime the minTime to set
	 */
	public void setMinTime( Integer minTime ) {
		this.minTime = minTime;
	}

	/**
	 * @return the maxTime
	 */
	public Integer getMaxTime( ) {
		return maxTime;
	}

	/**
	 * @param maxTime the maxTime to set
	 */
	public void setMaxTime( Integer maxTime ) {
		this.maxTime = maxTime;
	}
    
	public boolean isMinTimeSet (){
		return this.minTime != Integer.MIN_VALUE;
	}
	
	public boolean isMaxTimeSet (){
		return this.maxTime != Integer.MAX_VALUE;
	}
	
	public Object clone() throws CloneNotSupportedException {
		TimedAssessmentEffect tae = (TimedAssessmentEffect) super.clone();
		tae.maxTime = maxTime;
		tae.minTime = minTime;
		return tae;
	} 
}
