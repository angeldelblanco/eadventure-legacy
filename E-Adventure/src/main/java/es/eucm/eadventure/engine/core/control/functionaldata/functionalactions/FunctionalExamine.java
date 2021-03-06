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
package es.eucm.eadventure.engine.core.control.functionaldata.functionalactions;

import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.elements.Description;
import es.eucm.eadventure.engine.core.control.ActionManager;
import es.eucm.eadventure.engine.core.control.DebugLog;
import es.eucm.eadventure.engine.core.control.Game;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalDescriptions;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalElement;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalPlayer;

/**
 * The action to examine an object
 * 
 * @author Eugenio Marchiori
 * 
 */
public class FunctionalExamine extends FunctionalAction {

    /**
     * The element to be examined
     */
    FunctionalElement element;

    /**
     * Default constructor, using the original action and the element to
     * examine.
     * 
     * @param action
     * @param element
     */
    public FunctionalExamine( Action action, FunctionalElement element ) {

        super( );
        type = ActionManager.ACTION_EXAMINE;
        this.element = element;
        originalAction = element.getFirstValidAction( Action.EXAMINE );
        if( element.isInInventory( ) || originalAction == null ) {
            this.needsGoTo = false;
        }
        else if (originalAction!=null){
            this.needsGoTo = originalAction.isNeedsGoTo( );
            this.keepDistance = originalAction.getKeepDistance( );
        }
    }

    @Override
    public void drawAditionalElements( ) {

    }

    @Override
    public void setAnotherElement( FunctionalElement element ) {

    }

    @Override
    public void start( FunctionalPlayer functionalPlayer ) {

        
        Description description = new FunctionalDescriptions(element.getElement( ).getDescriptions( )).getDescription( );
       
        this.functionalPlayer = functionalPlayer;
        if( !element.examine( ) ) {
            if( functionalPlayer.isAlwaysSynthesizer( ) )
                functionalPlayer.speakWithFreeTTS( description.getDetailedDescription( ), functionalPlayer.getPlayerVoice( ) );
            else if (description.getDetailedDescriptionSoundPath( ) != null && !description.getDetailedDescriptionSoundPath( ).equals( "" )) 
                functionalPlayer.speak( description.getDetailedDescription( ), description.getDetailedDescriptionSoundPath( ) );
            else
                functionalPlayer.speak( description.getDetailedDescription( ), Game.getInstance().getGameDescriptor( ).isKeepShowing( ) );
        }
        finished = true;

        DebugLog.player( "Started Examine: " + element.getElement( ).getId( ) );
    }

    @Override
    public void stop( ) {

        finished = true;
    }

    @Override
    public void update( long elapsedTime ) {

    }

}
