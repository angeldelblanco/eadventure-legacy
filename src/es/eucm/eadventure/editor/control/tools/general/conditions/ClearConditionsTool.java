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
package es.eucm.eadventure.editor.control.tools.general.conditions;

import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.tools.Tool;


/**
 * 
 * Clear conditions tool
 * 
 */
public class ClearConditionsTool extends Tool{

    Conditions conditions;
    Conditions conditionsOld;
    
    public ClearConditionsTool(Conditions conditions){
       try {
        this.conditionsOld = (Conditions)conditions.clone( );
        this.conditions = conditions;
       }
       catch( CloneNotSupportedException e ) {
           e.printStackTrace();
       }
    }
    
    
    @Override
    public boolean canRedo( ) {

        return true;
    }

    @Override
    public boolean canUndo( ) {

        return true;
    }

    @Override
    public boolean combine( Tool other ) {

        return false;
    }

    @Override
    public boolean doTool( ) {
        if (conditions!=null){
            conditions.getConditionsList( ).clear( );
            Controller.getInstance( ).updateVarFlagSummary( );
            Controller.getInstance( ).updatePanel( );
            return true;
        } else 
            return false;
    }

    @Override
    public boolean redoTool( ) {

        return doTool();
    }

    @Override
    public boolean undoTool( ) {

        try {
            if (conditionsOld!=null){  
                conditions = (Conditions)conditionsOld.clone( );
                Controller.getInstance( ).updateVarFlagSummary( );
                Controller.getInstance( ).updatePanel( );
            } else 
                return false;
            
        }
        catch( CloneNotSupportedException e ) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

}
