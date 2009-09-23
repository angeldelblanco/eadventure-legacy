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
package es.eucm.eadventure.engine.core.control.gamestate;

import java.awt.Graphics2D;

import es.eucm.eadventure.engine.core.control.functionaldata.functionaleffects.FunctionalEffect;
import es.eucm.eadventure.engine.core.control.functionaldata.functionaleffects.FunctionalMoveObjectEffect;
import es.eucm.eadventure.engine.core.control.functionaldata.functionaleffects.FunctionalPlayAnimationEffect;
import es.eucm.eadventure.engine.core.control.functionaldata.functionaleffects.FunctionalShowTextEffect;
import es.eucm.eadventure.engine.core.gui.GUI;

/**
 * A game main loop while the effects of an action are being performed
 */
public class GameStateRunEffects extends GameState {

    /**
     * The current effect being executed
     */
    private FunctionalEffect currentExecutingEffect;

    /**
     * Distinguish when the State run effects are called from a conversation
     */
    private boolean fromConversation;

    /**
     * Constructor
     */
    public GameStateRunEffects( boolean fromConversation ) {

        super( );
        currentExecutingEffect = null;
        this.fromConversation = fromConversation;
    }

    /*
     * (non-Javadoc)
     * @see es.eucm.eadventure.engine.engine.control.gamestate.GameState#EvilLoop(long, int)
     */
    @Override
    public void mainLoop( long elapsedTime, int fps ) {

        game.getActionManager( ).setElementOver( null );
        game.getActionManager( ).setExitCustomized( null, null );

        // Toggle the HUD off and set the default cursor
        GUI.getInstance( ).toggleHud( false );
        GUI.getInstance( ).setDefaultCursor( );

        if( game.getFunctionalScene( ) != null )
            game.getFunctionalScene( ).update( elapsedTime );
        GUI.getInstance( ).update( elapsedTime );

        Graphics2D g = GUI.getInstance( ).getGraphics( );
        g.clearRect( 0, 0, GUI.WINDOW_WIDTH, GUI.WINDOW_HEIGHT );

        // Draw the scene
        if( game.getFunctionalScene( ) != null )
            game.getFunctionalScene( ).draw( );

        // If is show text effect, call to its draw method
        if( currentExecutingEffect instanceof FunctionalShowTextEffect )
            ( (FunctionalShowTextEffect) currentExecutingEffect ).draw( );
        GUI.getInstance( ).drawScene( g, elapsedTime );

        GUI.getInstance( ).drawHUD( g );

        // Draw the FPS
        //g.setColor( Color.WHITE );
        //g.drawString( Integer.toString( fps ), 780, 14 );

        // If no effect is being executed, or it has stopped running
        if( currentExecutingEffect == null || !currentExecutingEffect.isStillRunning( ) ) {

            // Delete the current effect
            currentExecutingEffect = null;

            /*
            // If no more effects must be executed, switch the state
            if( game.isEmptyFIFOinStack() ) {
                System.gc( );
                GUI.getInstance().toggleHud( true );
                // Look if there are some stored state, and change to correct one.               
                game.setAndPopState( );
                               
            } */

            boolean stop = false;
            // Execute effects while are instantaneous

            while( !stop /*&& !game.isEmptyFIFOinStack()*/) {
                FunctionalEffect currentEffect = game.getFirstElementOfTop( );
                if( currentEffect == null ) {
                    System.gc( );
                    GUI.getInstance( ).toggleHud( true );
                    stop = true;
                    // Look if there are some stored conversation state, and change to correct one.               
                    game.evaluateState( );

                }
                else {
                    // Check if all conditions associated to effect are OK
                    if( currentEffect.isAllConditionsOK( ) ) {
                        currentEffect.triggerEffect( );
                        stop = !currentEffect.isInstantaneous( );
                        if( stop )
                            currentExecutingEffect = currentEffect;
                    }
                }
            }
        }

        // Special conditions for the play animation effect
        // FIXME Edu: �Mover esto de aqui?
        else if( currentExecutingEffect != null && currentExecutingEffect.isStillRunning( ) ) {
            if( currentExecutingEffect instanceof FunctionalPlayAnimationEffect ) {
                ( (FunctionalPlayAnimationEffect) currentExecutingEffect ).draw( g );
                ( (FunctionalPlayAnimationEffect) currentExecutingEffect ).update( elapsedTime );
            }
            if (currentExecutingEffect instanceof FunctionalMoveObjectEffect) {
                ((FunctionalMoveObjectEffect) currentExecutingEffect).update(elapsedTime);
            }
        }

        GUI.getInstance( ).endDraw( );
        g.dispose( );
    }
}
