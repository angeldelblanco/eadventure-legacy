/*******************************************************************************
 * <e-Adventure> (formerly <e-Game>) is a research project of the <e-UCM>
 *         research group.
 *  
 *   Copyright 2005-2010 <e-UCM> research group.
 * 
 *   You can access a list of all the contributors to <e-Adventure> at:
 *         http://e-adventure.e-ucm.es/contributors
 * 
 *   <e-UCM> is a research group of the Department of Software Engineering
 *         and Artificial Intelligence at the Complutense University of Madrid
 *         (School of Computer Science).
 * 
 *         C Profesor Jose Garcia Santesmases sn,
 *         28040 Madrid (Madrid), Spain.
 * 
 *         For more info please visit:  <http://e-adventure.e-ucm.es> or
 *         <http://www.e-ucm.es>
 * 
 * ****************************************************************************
 * 
 * This file is part of <e-Adventure>, version 1.2.
 * 
 *     <e-Adventure> is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     <e-Adventure> is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with <e-Adventure>.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.eucm.eadventure.editor.control.tools.scene;

import javax.swing.JTable;

import es.eucm.eadventure.editor.control.controllers.scene.BarrierDataControl;
import es.eucm.eadventure.editor.control.controllers.scene.BarriersListDataControl;
import es.eucm.eadventure.editor.control.tools.Tool;

/**
 * That tool edit the changes in barrier layer because of the fact of barriers's
 * movements in ScenePanel table
 * 
 */
public class MoveBarrierTool extends Tool {

    private BarriersListDataControl dataControl;

    private JTable table;

    private boolean moveUp;

    private BarrierDataControl element;

    private int position;

    public MoveBarrierTool( BarriersListDataControl dataControl2, JTable table2, boolean isMoveUp ) {

        this.dataControl = dataControl2;
        this.table = table2;
        this.moveUp = isMoveUp;
        position = table.getSelectedRow( );
        element = dataControl.getBarriers( ).get( position );
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

        action( moveUp );
        return true;
    }

    private void action( boolean up ) {

        // do moveDown 
        if( !up && dataControl.moveElementDown( element ) ) {
            table.getSelectionModel( ).setSelectionInterval( position + 1, position + 1 );
            table.updateUI( );
        }
        //do moveUp
        if( up && dataControl.moveElementUp( element ) ) {
            table.getSelectionModel( ).setSelectionInterval( position - 1, position - 1 );
            table.updateUI( );
        }
        moveUp = !moveUp;
    }

    @Override
    public boolean redoTool( ) {

        action( moveUp );
        return true;
    }

    @Override
    public boolean undoTool( ) {

        action( moveUp );
        return true;
    }

}
