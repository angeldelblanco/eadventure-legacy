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

import java.util.Random;

import es.eucm.eadventure.common.data.chapter.effects.RandomEffect;

/**
 * Functional class for RandomEffect
 * 
 * @author Javier
 */
public class FunctionalRandomEffect extends FunctionalEffect {

    private static final Random r = new Random( );

    /**
     * Functional positive effect
     */
    private FunctionalEffect positiveEffect;

    /**
     * Functional negative effect
     */
    private FunctionalEffect negativeEffect;

    private boolean positive;

    /**
     * Effect to be triggered: Points to positiveEffect or negativeEffect
     */
    private FunctionalEffect effectTriggered;

    public FunctionalRandomEffect( RandomEffect effect ) {

        super( effect );
        this.positiveEffect = FunctionalEffect.buildFunctionalEffect( effect.getPositiveEffect( ) );
        this.negativeEffect = FunctionalEffect.buildFunctionalEffect( effect.getNegativeEffect( ) );
    }

    private FunctionalEffect getEffectToBeTriggered( ) {

        int number = r.nextInt( 100 );
        positive = number < ( (RandomEffect) effect ).getProbability( );

        if( positive )
            return positiveEffect;
        return negativeEffect;
    }

    @Override
    public boolean isInstantaneous( ) {

        if( effectTriggered != null )
            return effectTriggered.isInstantaneous( );
        return false;
    }

    @Override
    public boolean isStillRunning( ) {

        if( effectTriggered != null )
            return effectTriggered.isStillRunning( );
        return false;
    }

    @Override
    public void triggerEffect( ) {
        gameLog.effectEvent( getCode());
        effectTriggered = getEffectToBeTriggered( );
        if( effectTriggered != null ) {
            if( effectTriggered.isAllConditionsOK( ) )
                effectTriggered.triggerEffect( );
        }
    }
    @Override
    public FunctionalEffect getTriggerEffect(){
        
      return  effectTriggered;
    }
    
    @Override
    public boolean canSkip( ) {
        return effectTriggered.canSkip( );
    } 

    

}
