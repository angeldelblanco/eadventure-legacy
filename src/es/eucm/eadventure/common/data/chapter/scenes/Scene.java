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
package es.eucm.eadventure.common.data.chapter.scenes;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.auxiliar.AssetsImageDimensions;
import es.eucm.eadventure.common.data.Positioned;
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.Trajectory;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;
import es.eucm.eadventure.common.data.chapter.elements.Barrier;

/**
 * This class holds the data for a scene in eAdventure
 */
public class Scene extends GeneralScene implements Positioned {

    /**
     * The value of player layer when hasn't layer. This may
     */
    public static final int PLAYER_WITHOUT_LAYER = -1;

    /**
     * The value of player layer in 1� person adventures
     */
    public static final int PLAYER_NO_ALLOWED = -2;

    /**
     * The tag for the background image
     */
    public static final String RESOURCE_TYPE_BACKGROUND = "background";

    /**
     * The tag for the foreground image
     */
    public static final String RESOURCE_TYPE_FOREGROUND = "foreground";

    /**
     * The tag for the hard map image
     */
    public static final String RESOURCE_TYPE_HARDMAP = "hardmap";

    /**
     * The tag for the background music
     */
    public static final String RESOURCE_TYPE_MUSIC = "bgmusic";

    /**
     * Default X position for the player
     */
    private int defaultX;

    /**
     * Default Y position for the player
     */
    private int defaultY;

    /**
     * The position in which will be drown the player
     */
    private int playerLayer;

    /**
     * Set if it is allow to use the player layer or not. Its default value it
     * is true.
     */
   // private boolean allowPlayerLayer;

    /**
     * List of exits
     */
    private List<Exit> exits;

    /**
     * List of item references
     */
    private List<ElementReference> itemReferences;

    /**
     * List of atrezzo references
     */
    private List<ElementReference> atrezzoReferences;

    /**
     * List of character references
     */
    private List<ElementReference> characterReferences;

    /**
     * List of active areas
     */
    private List<ActiveArea> activeAreas;

    /**
     * List of barriers
     */
    private List<Barrier> barriers;

    private Trajectory trajectory;

    private float playerScale;

    /**
     * Creates a new Scene
     * 
     * @param id
     *            the scene's id
     */
    public Scene( String id ) {

        super( GeneralScene.SCENE, id );

        //defaultX = Integer.MIN_VALUE;
        //defaultY = Integer.MIN_VALUE;
        defaultX = AssetsImageDimensions.BACKGROUND_MAX_WIDTH/2;
        defaultY = AssetsImageDimensions.BACKGROUND_MAX_HEIGHT/2;
        exits = new ArrayList<Exit>( );
        itemReferences = new ArrayList<ElementReference>( );
        atrezzoReferences = new ArrayList<ElementReference>( );
        characterReferences = new ArrayList<ElementReference>( );
        activeAreas = new ArrayList<ActiveArea>( );
        barriers = new ArrayList<Barrier>( );
        playerLayer = PLAYER_WITHOUT_LAYER;
        //allowPlayerLayer = true;
        playerScale = 1.0f;
    }

    /**
     * @return the trajectory
     */
    public Trajectory getTrajectory( ) {

        return trajectory;
    }

    /**
     * @param trajectory
     *            the trajectory to set
     */
    public void setTrajectory( Trajectory trajectory ) {

        this.trajectory = trajectory;
    }

    /**
     * Returns whether this scene has a default position for the player
     * 
     * @return true if this scene has a default position for the player, false
     *         otherwise
     */
    public boolean hasDefaultPosition( ) {

        return ( defaultX != Integer.MIN_VALUE ) && ( defaultY != Integer.MIN_VALUE );
    }

    /**
     * Returns the horizontal coordinate of the default position for the player
     * 
     * @return the horizontal coordinate of the default position for the player
     */
    public int getPositionX( ) {

        return defaultX;
    }

    /**
     * Returns the vertical coordinate of the default position for the player
     * 
     * @return the vertical coordinate of the default position for the player
     */
    public int getPositionY( ) {

        return defaultY;
    }

    /**
     * Returns the list of exits
     * 
     * @return the list of exits
     */
    public List<Exit> getExits( ) {

        return exits;
    }

    /**
     * Returns the list of item references
     * 
     * @return the list of item references
     */
    public List<ElementReference> getItemReferences( ) {

        return itemReferences;
    }

    /**
     * Returns the list of atrezzo item references
     * 
     * @return the list of atrezzo item references
     */
    public List<ElementReference> getAtrezzoReferences( ) {

        return atrezzoReferences;
    }

    /**
     * Returns the list of character references
     * 
     * @return the list of character references
     */
    public List<ElementReference> getCharacterReferences( ) {

        return characterReferences;
    }

    /**
     * Changes the player's default position
     * 
     * @param defaultX
     *            the horizontal coordinate
     * @param defaultY
     *            the vertical coordinate
     */
    public void setDefaultPosition( int defaultX, int defaultY ) {

        this.defaultX = defaultX;
        this.defaultY = defaultY;
    }

    /**
     * Adds an exit to the list of exits
     * 
     * @param exit
     *            the exit to add
     */
    public void addExit( Exit exit ) {

        exits.add( exit );
    }

    /**
     * Adds an item reference to the list of item references
     * 
     * @param itemReference
     *            the item reference to add
     */
    public void addItemReference( ElementReference itemReference ) {

        itemReferences.add( itemReference );
    }

    /**
     * Adds an atrezzo item reference to the list of atrezzo item references
     * 
     * @param atrezzoReference
     *            the atrezzo item reference to add
     */
    public void addAtrezzoReference( ElementReference itemReference ) {

        atrezzoReferences.add( itemReference );
    }

    /**
     * Adds a character reference to the list of character references
     * 
     * @param characterReference
     *            the character reference to add
     */
    public void addCharacterReference( ElementReference characterReference ) {

        characterReferences.add( characterReference );
    }

    /**
     * Adds an active area
     * 
     * @param activeArea
     *            the active area to add
     */
    public void addActiveArea( ActiveArea activeArea ) {

        activeAreas.add( activeArea );
    }

    /**
     * @return the activeAreas
     */
    public List<ActiveArea> getActiveAreas( ) {

        return activeAreas;
    }

    /**
     * Adds a new barrier
     * 
     * @param barrier
     *            the barrier to add
     */
    public void addBarrier( Barrier barrier ) {

        barriers.add( barrier );
    }

    /**
     * @return the barriers
     */
    public List<Barrier> getBarriers( ) {

        return barriers;
    }

    public boolean isForcedPlayerLayer(){
        return playerLayer > PLAYER_WITHOUT_LAYER;
    }
    
    public boolean isAllowPlayerLayer( ) {

        return playerLayer != PLAYER_NO_ALLOWED;
    }

    /**
     * Change if player layer can be used. If it can not, change the player
     * layer value to PLAYER_NO_ALLOWED
     * 
     * @param allowPlayerLayer
     * 
     */
 /*   public void setAllowPlayerLayer( boolean allowPlayerLayer ) {

        this.allowPlayerLayer = allowPlayerLayer;
        if( !allowPlayerLayer )
            playerLayer = PLAYER_NO_ALLOWED;
    }*/

    /**
     * Returns the player layer
     * 
     * @return current player layer
     */
    public int getPlayerLayer( ) {

        return playerLayer;
    }

    /**
     * Change the current player layer if it is allow to do that.
     * 
     * @param playerLayer
     *            the current player layer
     */
    public void setPlayerLayer( int playerLayer ) {

        this.playerLayer = playerLayer;
      //  if( playerLayer == PLAYER_NO_ALLOWED )
        //    allowPlayerLayer = false;

    }

    public void setPlayerScale( float scale ) {

        this.playerScale = scale;
    }

    public float getPlayerScale( ) {

        return playerScale;
    }

    @Override
    public Object clone( ) throws CloneNotSupportedException {

        Scene s = (Scene) super.clone( );
        if( activeAreas != null ) {
            s.activeAreas = new ArrayList<ActiveArea>( );
            for( ActiveArea aa : activeAreas )
                s.activeAreas.add( (ActiveArea) aa.clone( ) );
        }
        //s.allowPlayerLayer = allowPlayerLayer;
        if( atrezzoReferences != null ) {
            s.atrezzoReferences = new ArrayList<ElementReference>( );
            for( ElementReference er : atrezzoReferences )
                s.atrezzoReferences.add( (ElementReference) er.clone( ) );
        }
        if( barriers != null ) {
            s.barriers = new ArrayList<Barrier>( );
            for( Barrier b : barriers )
                s.barriers.add( (Barrier) b.clone( ) );
        }
        if( characterReferences != null ) {
            s.characterReferences = new ArrayList<ElementReference>( );
            for( ElementReference er : characterReferences )
                s.characterReferences.add( (ElementReference) er.clone( ) );
        }
        s.defaultX = defaultX;
        s.defaultY = defaultY;
        if( exits != null ) {
            s.exits = new ArrayList<Exit>( );
            for( Exit e : exits )
                s.exits.add( (Exit) e.clone( ) );
        }
        if( itemReferences != null ) {
            s.itemReferences = new ArrayList<ElementReference>( );
            for( ElementReference er : itemReferences )
                s.itemReferences.add( (ElementReference) er.clone( ) );
        }
        s.playerLayer = playerLayer;
        s.playerScale = playerScale;
        s.trajectory = ( trajectory != null ? (Trajectory) trajectory.clone( ) : null );
        return s;
    }

    public void setPositionX( int newX ) {

        this.defaultX = newX;
    }

    public void setPositionY( int newY ) {

        this.defaultY = newY;
    }

}
