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
package es.eucm.eadventure.editor.control.tools.scene;

import java.util.HashMap;

import es.eucm.eadventure.common.data.chapter.Trajectory;
import es.eucm.eadventure.common.data.chapter.Trajectory.Node;
import es.eucm.eadventure.common.data.chapter.Trajectory.Side;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.tools.Tool;

public class SetNodeValuesTool extends Tool {

    private int oldX;

    private int oldY;

    private float oldScale;

    private int newX;

    private int newY;

    private float newScale;

    private Node node;

    private Trajectory trajectory;
    
    private HashMap<String, Float> oldLengths;
    
    public SetNodeValuesTool( Node node, Trajectory trajectory, int newX, int newY, float newScale ) {
        this.newX = newX;
        this.newY = newY;
        this.newScale = newScale;
        this.oldX = node.getX( );
        this.oldY = node.getY( );
        this.oldScale = node.getScale( );
        this.node = node;
        this.trajectory = trajectory;
        this.oldLengths = new HashMap<String, Float>();
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

        if( other instanceof SetNodeValuesTool ) {
            SetNodeValuesTool crvt = (SetNodeValuesTool) other;
            if( crvt.node != node )
                return false;
            newX = crvt.newX;
            newY = crvt.newY;
            newScale = crvt.newScale;
            timeStamp = crvt.timeStamp;
            return true;
        }
        return false;
    }

    @Override
    public boolean doTool( ) {
        node.setValues( newX, newY, newScale );
        if (newX != oldX || newY != oldY)
            for (Side side : trajectory.getSides( )) {
                if (side.getIDEnd( ).equals(node.getID( )) || side.getIDStart( ).equals( node.getID( ) ) ) {
                    oldLengths.put( side.getIDStart( ) + ";" + side.getIDEnd( ) , side.getLength( ) );
                    Node start = trajectory.getNodeForId( side.getIDStart( ) );
                    Node end = trajectory.getNodeForId( side.getIDEnd( ) );
                    double x = start.getX( ) - end.getX( );
                    double y = start.getY( ) - end.getY( );
                    side.setLenght( (float) Math.sqrt( Math.pow(x,2) + Math.pow( y,2 ) ) );
                    side.setRealLength( (float) Math.sqrt( Math.pow(x,2) + Math.pow( y,2 ) ) );
                }
            }
        return true;
    }

    @Override
    public boolean redoTool( ) {

        node.setValues( newX, newY, newScale );
        if (newX != oldX || newY != oldY)
            for (Side side : trajectory.getSides( )) {
                if (side.getIDEnd( ).equals(node.getID( )) || side.getIDStart( ).equals( node.getID( ) ) ) {
                    Node start = trajectory.getNodeForId( side.getIDStart( ) );
                    Node end = trajectory.getNodeForId( side.getIDEnd( ) );
                    double x = start.getX( ) - end.getX( );
                    double y = start.getY( ) - end.getY( );
                    side.setLenght( (float) Math.sqrt( Math.pow(x,2) + Math.pow( y,2 ) ) );
                    side.setRealLength( (float) Math.sqrt( Math.pow(x,2) + Math.pow( y,2 ) ) );
                }
            }
        Controller.getInstance( ).updatePanel( );
        return true;
    }

    @Override
    public boolean undoTool( ) {

        node.setValues( oldX, oldY, oldScale );
        if (newX != oldX || newY != oldY)
            for (Side side : trajectory.getSides( )) {
                if (side.getIDEnd( ).equals(node.getID( )) || side.getIDStart( ).equals( node.getID( ) ) ) {
                    Node start = trajectory.getNodeForId( side.getIDStart( ) );
                    Node end = trajectory.getNodeForId( side.getIDEnd( ) );
                    double x = start.getX( ) - end.getX( );
                    double y = start.getY( ) - end.getY( );
                    side.setRealLength( (float) Math.sqrt( Math.pow(x,2) + Math.pow( y,2 ) ) );
                }

                Float temp = oldLengths.get( side.getIDStart( ) + ";" + side.getIDEnd( ) );
                if (temp != null)
                    side.setLenght( temp );
            }
        
        Controller.getInstance( ).updatePanel( );
        return true;
    }

}
