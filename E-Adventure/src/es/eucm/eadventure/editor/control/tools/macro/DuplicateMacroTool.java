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
package es.eucm.eadventure.editor.control.tools.macro;

import es.eucm.eadventure.common.data.chapter.effects.Macro;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.macro.MacroDataControl;
import es.eucm.eadventure.editor.control.controllers.macro.MacroListDataControl;
import es.eucm.eadventure.editor.control.tools.Tool;

public class DuplicateMacroTool extends Tool {

    private MacroListDataControl dataControl;

    private MacroDataControl newMacro;

    private int selected;

    public DuplicateMacroTool( MacroListDataControl dataControl2, int selected ) {

        this.dataControl = dataControl2;
        this.selected = selected;
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

        if( dataControl.duplicateElement( dataControl.getMacros( ).get( selected ) ) ) {
            newMacro = dataControl.getLastMacro( );
            return true;
        }
        return false;
    }

    @Override
    public boolean redoTool( ) {

        dataControl.getMacros( ).add( newMacro );
        dataControl.getMacrosList( ).add( (Macro) newMacro.getContent( ) );
        Controller.getInstance( ).getIdentifierSummary( ).addMacroId( newMacro.getId( ) );
        Controller.getInstance( ).updatePanel( );
        return true;
    }

    @Override
    public boolean undoTool( ) {

        dataControl.deleteElement( newMacro, false );
        Controller.getInstance( ).getIdentifierSummary( ).deleteMacroId( newMacro.getId( ) );
        Controller.getInstance( ).updatePanel( );
        return true;
    }
}