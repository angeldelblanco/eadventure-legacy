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

import es.eucm.eadventure.common.data.chapter.effects.SpeakPlayerEffect;
import es.eucm.eadventure.engine.core.control.Game;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalConditions;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalPlayer;

/**
 * An effect that makes the player to speak a line of text.
 */
public class FunctionalSpeakPlayerEffect extends FunctionalEffect {

    /**
     * Creates a new FunctionalSpeakPlayerEffect.
     * 
     * @param line
     *            the text to be spoken
     */
    public FunctionalSpeakPlayerEffect( SpeakPlayerEffect effect ) {

        super( effect );
    }

    /*
     * (non-Javadoc)
     * @see es.eucm.eadventure.engine.engine.data.effects.Effect#triggerEffect()
     */
    @Override
    public void triggerEffect( ) {
        FunctionalConditions cond = new FunctionalConditions( effect.getConditions( ) );
        String audioPath = ((SpeakPlayerEffect)effect).getAudioPath( );
        if( cond.allConditionsOk( ) ) {
            FunctionalPlayer player = Game.getInstance( ).getFunctionalPlayer( );
            String line=( (SpeakPlayerEffect) effect ).getLine( );
            gameLog.effectEvent( getCode(), "l="+(line.length( )>5?line.substring( 0,5 ):line));
            if( player.isAlwaysSynthesizer( ) )
                player.speakWithFreeTTS( ( (SpeakPlayerEffect) effect ).getLine( ), player.getPlayerVoice( ), Game.getInstance( ).getGameDescriptor( ).isKeepShowing( )  );
            else if (audioPath!=null && !audioPath.equals( "" )) 
                player.speak(( (SpeakPlayerEffect) effect ).getLine( ), audioPath, Game.getInstance( ).getGameDescriptor( ).isKeepShowing( ) );
            else    
                player.speak( ( (SpeakPlayerEffect) effect ).getLine( ), Game.getInstance( ).getGameDescriptor( ).isKeepShowing( ) );
            Game.getInstance( ).setCharacterCurrentlyTalking( player );
        }
    }

    /*
     * (non-Javadoc)
     * @see es.eucm.eadventure.engine.engine.data.effects.Effect#isInstantaneous()
     */
    @Override
    public boolean isInstantaneous( ) {

        return false;
    }

    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.core.control.functionaldata.functionaleffects.FunctionalEffect#isStillRunning()
     */
    @Override
    public boolean isStillRunning( ) {

        if( Game.getInstance( ).getCharacterCurrentlyTalking( ) != null && !Game.getInstance( ).getCharacterCurrentlyTalking( ).isTalking( ) )
            Game.getInstance( ).setCharacterCurrentlyTalking( null );

        return Game.getInstance( ).getCharacterCurrentlyTalking( ) != null;
    }
    
    @Override
    public boolean canSkip( ) {
        return true;
    }

    @Override
    public void skip( ) {
        if( Game.getInstance( ).getCharacterCurrentlyTalking( ) != null && Game.getInstance( ).getCharacterCurrentlyTalking( ).isTalking( ) )
            Game.getInstance( ).getCharacterCurrentlyTalking( ).stopTalking( );
        Game.getInstance( ).setCharacterCurrentlyTalking( null );
    }


}
