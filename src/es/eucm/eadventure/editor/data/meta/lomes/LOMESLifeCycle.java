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
package es.eucm.eadventure.editor.data.meta.lomes;

import java.util.ArrayList;

import es.eucm.eadventure.editor.data.meta.LangString;
import es.eucm.eadventure.editor.data.meta.Vocabulary;
import es.eucm.eadventure.editor.data.meta.auxiliar.LOMContribute;
import es.eucm.eadventure.editor.data.meta.auxiliar.LOMESLifeCycleContribute;

public class LOMESLifeCycle {

    
    	public static final String VERSION_DEFAULT="V1.0";
	//2.1
	private LangString version;
	
	//2.2 
	private ArrayList<Vocabulary> status;
	
	//2.3 Contribute
	private LOMContribute contribute;
	
	public LOMESLifeCycle (){
		version = new LangString(VERSION_DEFAULT);
		contribute = null;
		status = new ArrayList<Vocabulary>();
	
}
	
	
	/***********************************ADD METHODS **************************/
	public void addVersion(LangString version){
		this.version=version;
	}
	
	public void addStatus(int index){
		this.status.add(new Vocabulary(Vocabulary.LC_STAUS_VALUE_2_2,Vocabulary.LOM_ES_SOURCE,index));
	}
	/*********************************** SETTERS **************************/
	
	public void setStatus(int index){
		this.status= new ArrayList<Vocabulary>();
		this.status.add( new Vocabulary(Vocabulary.LC_STAUS_VALUE_2_2,Vocabulary.LOM_ES_SOURCE,index) );
	}
	
	public void setVersion(LangString version){
		this.version=version;
	}
	
	

	
	/*********************************** GETTERS **************************/
	//VERSION
	public LangString getVersion(){
		return version;
	}
	
	public Vocabulary getStatus(){
		return status.get(0);
	}


	//CONTRIBUTION
	/**
	 * @return the contribute
	 */
	public LOMContribute getContribute() {
		return contribute;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(ArrayList<Vocabulary> status) {
		this.status = status;
	}


	/**
	 * @param contribute the contribute to set
	 */
	public void setContribute(LOMContribute contribute) {
		this.contribute = contribute;
	}
	
	
	
	
	
}
