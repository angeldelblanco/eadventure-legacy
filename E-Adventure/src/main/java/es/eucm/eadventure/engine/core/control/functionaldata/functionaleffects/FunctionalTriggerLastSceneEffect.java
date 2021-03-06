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
package es.eucm.eadventure.engine.core.control.functionaldata.functionaleffects;

import es.eucm.eadventure.common.data.chapter.effects.TriggerSceneEffect;
import es.eucm.eadventure.engine.core.control.Game;

/**
 * Special case of FunctionalTriggerSceneEffect. Triggers the last scene
 * rendered on the screen
 * 
 * @author Javier
 * 
 */
public class FunctionalTriggerLastSceneEffect extends FunctionalTriggerSceneEffect {

    private static String getLastSceneId( ) {

        if( Game.getInstance( ).getLastScene( ) != null ) {
            return Game.getInstance( ).getLastScene( ).getNextSceneId( );
        }
        return null;
    }

    private static int getLastSceneX( ) {

        if( Game.getInstance( ).getLastScene( ) != null ) {
            return Game.getInstance( ).getLastScene( ).getDestinyX( );
        }
        return Integer.MIN_VALUE;
    }

    private static int getLastSceneY( ) {

        if( Game.getInstance( ).getLastScene( ) != null ) {
            return Game.getInstance( ).getLastScene( ).getDestinyY( );
        }
        return Integer.MIN_VALUE;
    }

    public FunctionalTriggerLastSceneEffect( ) {

        super( new TriggerSceneEffect( null, Integer.MIN_VALUE, Integer.MIN_VALUE ) );
    }

    @Override
    public void triggerEffect( ) {
        gameLog.effectEvent( getCode(), "t="+getLastSceneId( ), "x="+ getLastSceneX( ), "y="+ getLastSceneY( ));
        effect = new TriggerSceneEffect( getLastSceneId( ), getLastSceneX( ), getLastSceneY( ) );
        super.triggerEffect( );
    }

}
