package es.eucm.eadventure.engine.core.control.functionaldata;

import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;
import es.eucm.eadventure.common.data.chapter.elements.Barrier;
import es.eucm.eadventure.common.data.chapter.elements.Item;
import es.eucm.eadventure.common.data.chapter.elements.Atrezzo;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.data.chapter.resources.Asset;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.data.chapter.scenes.Scene;
import es.eucm.eadventure.engine.core.control.ActionManager;
import es.eucm.eadventure.engine.core.control.Game;
import es.eucm.eadventure.engine.core.control.ItemSummary;
import es.eucm.eadventure.engine.core.control.functionaldata.functionalactions.FunctionalExit;
import es.eucm.eadventure.engine.core.control.functionaldata.functionalactions.FunctionalGoTo;
import es.eucm.eadventure.engine.core.gui.GUI;
import es.eucm.eadventure.engine.multimedia.MultimediaManager;
import es.eucm.eadventure.engine.resourcehandler.ResourceHandler;

/**
 * A scene in the game
 */
public class FunctionalScene implements Renderable {
    
    /**
     * Margins of the scene (for use in the scroll)
     */
    private final static int MAX_OFFSET_X = 300;

    /**
     * Scene data
     */
    private Scene scene;
    
    /**
     * Resources being used in the scene
     */
    private Resources resources;
    
    /**
     * Background image for the scene
     */
    private Image background;
    
    /**
     * Foreground image for the scene
     */
    private Image foreground;
    
    /**
     * Background music
     */
    private long backgroundMusicId = -1;

    /**
     * Functional player present in the scene
     */
    private FunctionalPlayer player;

    /**
     * Functional items present in the scene
     */
    private ArrayList<FunctionalItem> items;

    /**
     * Functional characters present in the scene
     */
    private ArrayList<FunctionalNPC> npcs;
    
    /**
     * Functional areas present in the scene
     */
    private ArrayList<FunctionalActiveArea> areas;
    
    /**
     * Functional barriers present in the scene;
     */
    private ArrayList<FunctionalBarrier> barriers;
    
    /**
     * Functional atrezzo items present in the scene
     */
    private ArrayList<FunctionalAtrezzo> atrezzo;
    
    private FunctionalTrajectory trajectory;

    /**
     * Offset of the scroll.
     */
    private int offsetX;
    
    /**
     * Creates a new FunctionalScene loading the background music.
     * @param scene the scene's data
     * @param player the reference to the player
     */
    public FunctionalScene( Scene scene, FunctionalPlayer player ) {
        this( scene, player, -1 );
    }

    /**
     * Creates a new FunctionalScene with the given background music.
     * @param scene the scene's data
     * @param player the reference to the player
     * @param backgroundMusicId Background music identifier
     */
    public FunctionalScene( Scene scene, FunctionalPlayer player, long backgroundMusicId ) {
        this.scene = scene;
        this.player = player;

        // Create lists for the characters, items and active areas
        npcs = new ArrayList<FunctionalNPC>( );
        items = new ArrayList<FunctionalItem>( );
        atrezzo = new ArrayList<FunctionalAtrezzo>( );
        areas = new ArrayList<FunctionalActiveArea>( );
        barriers = new ArrayList<FunctionalBarrier>( );
        trajectory = new FunctionalTrajectory(scene.getTrajectory());        	
        
        // Pick the item summary
        Chapter gameData = Game.getInstance( ).getCurrentChapterData( );
        ItemSummary itemSummary = Game.getInstance( ).getItemSummary( );
        
        // Select the resources
        resources = createResourcesBlock( );

        // Load the background image
        background = null;
        if( resources.existAsset( Scene.RESOURCE_TYPE_BACKGROUND ) )
            background = MultimediaManager.getInstance( ).loadImageFromZip( resources.getAssetPath( Scene.RESOURCE_TYPE_BACKGROUND ), MultimediaManager.IMAGE_SCENE );

        // Load the foreground image
        foreground = null;
        if( background!=null && resources.existAsset( Scene.RESOURCE_TYPE_FOREGROUND ) ){
            BufferedImage bufferedBackground = (BufferedImage) background;
            BufferedImage foregroundHardMap = (BufferedImage) MultimediaManager.getInstance( ).loadImageFromZip( resources.getAssetPath( Scene.RESOURCE_TYPE_FOREGROUND ), MultimediaManager.IMAGE_SCENE );
            BufferedImage bufferedForeground = (BufferedImage)GUI.getInstance( ).getGraphicsConfiguration( ).createCompatibleImage( foregroundHardMap.getWidth( null ), foregroundHardMap.getHeight( null ), Transparency.BITMASK );
            
            for(int i = 0; i < foregroundHardMap.getWidth( null ); i++){
                for(int j = 0; j < foregroundHardMap.getHeight( null ); j++){
                    if( foregroundHardMap.getRGB( i, j ) == 0xFFFFFFFF )
                        bufferedForeground.setRGB( i, j, 0x00000000 );
                    else
                        bufferedForeground.setRGB( i, j, bufferedBackground.getRGB( i, j ) );
                }                
            }
            
            foreground = bufferedForeground;
        }
        

        // Load the background music (if it is not loaded)
        this.backgroundMusicId = backgroundMusicId;
        if( backgroundMusicId == -1 )
            playBackgroundMusic( );

        // Add the functional items
        for( ElementReference itemReference : scene.getItemReferences( ) )
            if( new FunctionalConditions(itemReference.getConditions( )).allConditionsOk( ) )
                if( itemSummary.isItemNormal( itemReference.getIdTarget( ) ) )
                    for( Item currentItem : gameData.getItems( ) )
                        if( itemReference.getIdTarget( ).equals( currentItem.getId( ) ) ) {
                            FunctionalItem fitem = new FunctionalItem( currentItem, itemReference.getX( ), itemReference.getY( ) );
                        	fitem.setScale(itemReference.getScale());
                        	fitem.setLayer(itemReference.getLayer());
                            items.add( fitem );
                        }
        // Add the functional characters
        for( ElementReference npcReference : scene.getCharacterReferences( ) )
            if( new FunctionalConditions(npcReference.getConditions( )).allConditionsOk( ) )
                for( NPC currentNPC : gameData.getCharacters( ) )
                    if( npcReference.getIdTarget( ).equals( currentNPC.getId( ) ) ) {
                        FunctionalNPC fnpc = new FunctionalNPC( currentNPC, npcReference.getX( ), npcReference.getY( ) );
                    	fnpc.setScale(npcReference.getScale());
                    	fnpc.setLayer(npcReference.getLayer());
                        npcs.add( fnpc );
                    }
        // Add the functional active areas
        for( ActiveArea activeArea : scene.getActiveAreas( ) )
            if( new FunctionalConditions(activeArea.getConditions( )).allConditionsOk( ) )
                this.areas.add( new FunctionalActiveArea( activeArea ) );
        
        // Add the functional barriers
        for( Barrier barrier : scene.getBarriers( ) )
            if( new FunctionalConditions(barrier.getConditions( )).allConditionsOk( ) )
                this.barriers.add( new FunctionalBarrier( barrier ) );

     // Add the functional atrezzo items
        for( ElementReference atrezzoReference : scene.getAtrezzoReferences( ) )
            if( new FunctionalConditions(atrezzoReference.getConditions( )).allConditionsOk( ) )
                    for( Atrezzo currentAtrezzo : gameData.getAtrezzo() )
                        if( atrezzoReference.getIdTarget( ).equals( currentAtrezzo.getId( ) ) ) {
                            FunctionalAtrezzo fatrezzo = new FunctionalAtrezzo( currentAtrezzo, atrezzoReference.getX( ), atrezzoReference.getY( ));
                        	fatrezzo.setScale(atrezzoReference.getScale());
                            fatrezzo.setLayer(atrezzoReference.getLayer());
                        	atrezzo.add( fatrezzo );
                        }

        updateOffset( );
    }

    /**
     * Creates a new FunctionalScene only with a given background music.
     * 
     * @param backgroundMusicId Background music identifier
     */
    public FunctionalScene(long backgroundMusicId ) {
        // Load the background music (if it is not loaded)
        this.backgroundMusicId = backgroundMusicId;
        if( backgroundMusicId == -1 )
            playBackgroundMusic( );
    }

    
    /**
     * Update the resources and elements of the scene, depending on
     * the state of the flags.
     */
    public void updateScene( ) {
        
        // Update the resources and the player's resources
        updateResources( );
        player.updateResources( );
        
        // Pick the game data
        Chapter gameData = Game.getInstance( ).getCurrentChapterData( );
            
        // Check the item references of the scene
        for( ElementReference itemReference : scene.getItemReferences( ) ) {
            
            // For every item that should be there
            if( new FunctionalConditions(itemReference.getConditions( )).allConditionsOk( ) ) {
                boolean found = false;
                
                // If the functional item is present, update its resources
                for( FunctionalItem currentItem : items ) {
                    if( itemReference.getIdTarget( ).equals( currentItem.getItem( ).getId( ) ) ) {
                        currentItem.updateResources( );
                        found = true;
                    }
                }
                
                // If it was not found, search for it and add it
                if( !found ) {
                    if( Game.getInstance( ).getItemSummary( ).isItemNormal( itemReference.getIdTarget( ) ) ) {
                        for( Item currentItem : gameData.getItems( ) ) {
                            if( itemReference.getIdTarget( ).equals( currentItem.getId( ) ) ) {
                                items.add( new FunctionalItem( currentItem, itemReference.getX( ), itemReference.getY( ), itemReference.getLayer() ) );
                            }
                        }
                    }
                }
            }
        }

        // Check the character references of the scene
        for( ElementReference npcReference : scene.getCharacterReferences( ) ) {
            
            // For every item that should be there
            if( new FunctionalConditions(npcReference.getConditions( )).allConditionsOk( ) ) {
                boolean found = false;
                
                // If the functional character is present, update its resources
                for( FunctionalNPC currentNPC : npcs ) {
                    if( npcReference.getIdTarget( ).equals( currentNPC.getNPC( ).getId( ) ) ) {
                        currentNPC.updateResources( );
                        found = true;
                    }
                }
                
                // If it was not found, search for it and add it
                if( !found ) {
                    for( NPC currentNPC : gameData.getCharacters( ) ) {
                        if( npcReference.getIdTarget( ).equals( currentNPC.getId( ) ) ) {
                            npcs.add( new FunctionalNPC( currentNPC, npcReference.getX( ), npcReference.getY( ), npcReference.getLayer() ) );
                        }
                    }
                }
            }
        }
        
        // Check the active areas of the scene
        for( ActiveArea activeArea : scene.getActiveAreas( ) ) {
            
            // For every item that should be there
            if( new FunctionalConditions(activeArea.getConditions( )).allConditionsOk( ) ) {
                boolean found = false;
                
                // If the functional item is present, update its resources
                for( FunctionalActiveArea currentFunctionalActiveArea : areas ) {
                    if( activeArea.getId( ).equals( currentFunctionalActiveArea.getItem( ).getId( ) ) ) {
                        found = true;
                        break;
                    }
                }
                
                // If it was not found, search for it and add it
                if( !found ) {
                    areas.add( new FunctionalActiveArea( activeArea ) );
                }
            }
        }
        
        // Check the atrezzo item references of the scene
        for( ElementReference atrezzoReference : scene.getAtrezzoReferences( ) ) {
            
            // For every atrezzo item that should be there
            if( new FunctionalConditions(atrezzoReference.getConditions( )).allConditionsOk( ) ) {
                boolean found = false;
                
                // If the functional atrezzo item is present, update its resources
                for( FunctionalAtrezzo currentAtrezzo : atrezzo ) {
                    if( atrezzoReference.getIdTarget( ).equals( currentAtrezzo.getAtrezzo( ).getId( ) ) ) {
                        currentAtrezzo.updateResources( );
                        found = true;
                    }
                }
                
                // If it was not found, search for it and add it
                if( !found ) {
                    //if( Game.getInstance( ).getAtrezzoItemSummary( ).isFlagsNormal( atrezzoReference.getIdTarget( ) ) ) {
                        for( Atrezzo currentAtrezzo : gameData.getAtrezzo( ) ) {
                            if( atrezzoReference.getIdTarget( ).equals( currentAtrezzo.getId( ) ) ) {
                                atrezzo.add( new FunctionalAtrezzo( currentAtrezzo, atrezzoReference.getX( ), atrezzoReference.getY( ), atrezzoReference.getLayer()) );
                            }
                        }
                    //}
                }
            }
        }

        
        // Create a list with the items to remove
        ArrayList<FunctionalItem> itemsToRemove = new ArrayList<FunctionalItem>( );
        for( FunctionalItem currentItem : items ) {
            boolean keepItem = false;
            
            // For every present item, check if it must be kept
            for( ElementReference itemReference : scene.getItemReferences( ) ) {
                if( itemReference.getIdTarget( ).equals( currentItem.getItem( ).getId( ) ) &&
                		new FunctionalConditions(itemReference.getConditions( )).allConditionsOk( ) ) {
                    keepItem = true;
                }
            }
            
            // If it must not be kept, add it to the remove list
            if( !keepItem )
                itemsToRemove.add( currentItem );
        }
        
        // Remove the elements
        for( FunctionalItem itemToRemove : itemsToRemove )
            items.remove( itemToRemove );
        
        
        // Create a list with the characters to remove
        ArrayList<FunctionalNPC> npcsToRemove = new ArrayList<FunctionalNPC>( );
        for( FunctionalNPC currentNPC : npcs ) {
            boolean keepNPC = false;
            
            // For every present character, check if it must be kept
            for( ElementReference npcReference : scene.getCharacterReferences( ) ) {
                if( npcReference.getIdTarget( ).equals( currentNPC.getNPC( ).getId( ) ) &&
                		new FunctionalConditions(npcReference.getConditions( )).allConditionsOk( ) ) {
                    keepNPC = true;
                }
            }
            
            // If it must not be kept, add it to the remove list
            if( !keepNPC )
                npcsToRemove.add( currentNPC );
        }
        
        // Remove the elements
        for( FunctionalNPC npcToRemove : npcsToRemove )
            npcs.remove( npcToRemove );
        
        // Create a list with the active areas to remove
        ArrayList<FunctionalActiveArea> activeAreasToRemove = new ArrayList<FunctionalActiveArea>( );
        for( FunctionalActiveArea currentActiveArea : areas ) {
            boolean keepActiveArea = false;
            
            // For every present item, check if it must be kept
            for( ActiveArea activeArea : scene.getActiveAreas( ) ) {
                if( activeArea.getId( ).equals( currentActiveArea.getItem( ).getId( ) ) &&
                		new FunctionalConditions(activeArea.getConditions( )).allConditionsOk( ) ) {
                    keepActiveArea = true;
                }
            }
            
            // If it must not be kept, add it to the remove list
            if( !keepActiveArea )
                activeAreasToRemove.add( currentActiveArea );
        }
        
        // Remove the elements
        for( FunctionalActiveArea areaToRemove : activeAreasToRemove )
            areas.remove( areaToRemove );
        
        // Create a list with the atrezzo items to remove
        ArrayList<FunctionalAtrezzo> atrezzoToRemove = new ArrayList<FunctionalAtrezzo>( );
        for( FunctionalAtrezzo currentAtrezzo : atrezzo ) {
            boolean keepAtrezzo = false;
            
            // For every present item, check if it must be kept
            for( ElementReference atrezzoReference : scene.getAtrezzoReferences( ) ) {
                if( atrezzoReference.getIdTarget( ).equals( currentAtrezzo.getAtrezzo( ).getId( ) ) &&
                		new FunctionalConditions(atrezzoReference.getConditions( )).allConditionsOk( ) ) {
                    keepAtrezzo = true;
                }
            }
            
            // If it must not be kept, add it to the remove list
            if( !keepAtrezzo )
                atrezzoToRemove.add( currentAtrezzo );
        }
        
        // Remove the elements
        for( FunctionalAtrezzo atrToRemove : atrezzoToRemove )
            atrezzo.remove( atrToRemove );
        

    }
    
    /**
     * Updates the resources of the scene (if the current resources and the new one are different)
     */
    public void updateResources( ) {
        // Get the new resources
        Resources newResources = createResourcesBlock( );

        // If the resources have changed, load the new one
        if( resources != newResources ) {
            resources = newResources;
            
            if( resources.existAsset( Scene.RESOURCE_TYPE_BACKGROUND ) )
                background = MultimediaManager.getInstance( ).loadImageFromZip( resources.getAssetPath( Scene.RESOURCE_TYPE_BACKGROUND ), MultimediaManager.IMAGE_SCENE );

            // If there was a foreground, delete it
            if( foreground != null )
                foreground.flush( );
            
            // Load the foreground image
            foreground = null;
            if( background!=null && resources.existAsset( Scene.RESOURCE_TYPE_FOREGROUND ) ){
                BufferedImage bufferedBackground = (BufferedImage) background;
                BufferedImage foregroundHardMap = (BufferedImage) MultimediaManager.getInstance( ).loadImageFromZip( resources.getAssetPath( Scene.RESOURCE_TYPE_FOREGROUND ), MultimediaManager.IMAGE_SCENE );
                BufferedImage bufferedForeground = (BufferedImage)GUI.getInstance( ).getGraphicsConfiguration( ).createCompatibleImage( foregroundHardMap.getWidth( null ), foregroundHardMap.getHeight( null ), Transparency.BITMASK );
                
                for(int i = 0; i < foregroundHardMap.getWidth( null ); i++){
                    for(int j = 0; j < foregroundHardMap.getHeight( null ); j++){
                        if( foregroundHardMap.getRGB( i, j ) == 0xFFFFFFFF )
                            bufferedForeground.setRGB( i, j, 0x00000000 );
                        else
                            bufferedForeground.setRGB( i, j, bufferedBackground.getRGB( i, j ) );
                    }                
                }
                
                foreground = bufferedForeground;
            }
            
            playBackgroundMusic();
        }
    }
    
    /**
     * Returns the contained scene
     * @return Contained scene
     */
    public Scene getScene( ) {
        return scene;
    }

    /**
     * Returns the npc with the given id
     * @param npcId the id of the npc
     * @return the npc with the given id
     */
    public FunctionalNPC getNPC( String npcId ) {
        FunctionalNPC npc = null;

        if( npcId != null ) {
            for( FunctionalNPC currentNPC : npcs )
                if( currentNPC.getElement( ).getId( ).equals( npcId ) )
                    npc = currentNPC;
        }

        return npc;
    }
    
    /**
     * Returns the list of npcs in this scene
     * @return the list of npcs in this scene
     */
    public ArrayList<FunctionalNPC> getNPCs( ) {
        return npcs;
    }
    
    /**
     * Returns the list of items in this scene
     * @return the list of items in this scene
     */
    public ArrayList<FunctionalItem> getItems( ) {
        return items;
    }
    
    /**
     * Returns the list of items in this scene
     * @return the list of items in this scene
     */
    public ArrayList<FunctionalActiveArea> getActiveAreas( ) {
        return areas;
    }


    /*
     * (non-Javadoc)
     * @see es.eucm.eadventure.engine.engine.control.functionaldata.Renderable#update(long)
     */
    public void update( long elapsedTime ) {
        playBackgroundMusic();
              
        // Update the items
        //TODO esto se pude quitar,, item.update() no hace nada
        for( FunctionalItem item : items )
            item.update( elapsedTime );
        
        // Update the active areas
        for( FunctionalActiveArea activeArea : areas )
            activeArea.update( elapsedTime );
        
        // Update the characters
        for( FunctionalNPC npc : npcs )
            npc.update( elapsedTime );
        
        // Update the player
        player.update( elapsedTime );
        
     // Update the atrezzo items
       /* for( FunctionalAtrezzo atr: atrezzo )
            atr.update( elapsedTime );*/
        
        
        // Update the offset
        if( updateOffset( ) && Game.getInstance( ).getLastMouseEvent( )!=null  )
            Game.getInstance( ).mouseMoved( Game.getInstance( ).getLastMouseEvent( ) );
    }
    
    /**
     * Returns the offset of the scroll.
     * @return Offset of the scroll
     */
    public int getOffsetX( ) {
        return offsetX;
    }
    
    /**
     * Updates the offset value of the screen.
     * @return True if the offset has changed, false otherwise
     */
    private boolean updateOffset( ) {
        // TODO Francis: Comentar
        boolean updated = false;
        
        // Scroll
        int iw = background.getWidth( null );
        if( player.getX( ) - offsetX > ( GUI.WINDOW_WIDTH-MAX_OFFSET_X ) ) {
            updated = true;
            offsetX += player.getX( ) - offsetX - ( GUI.WINDOW_WIDTH-MAX_OFFSET_X );
            if( offsetX + GUI.WINDOW_WIDTH > iw )
                offsetX = iw - GUI.WINDOW_WIDTH;
        }
        
        else if( player.getX( ) - offsetX < MAX_OFFSET_X ) {
            updated = true;
            offsetX -= MAX_OFFSET_X - player.getX( ) + offsetX;
            if( offsetX < 0 )
                offsetX = 0;
        }
        
        return updated;
    }

    /*
     * (non-Javadoc)
     * @see es.eucm.eadventure.engine.engine.control.functionaldata.Renderable#draw(java.awt.Graphics2D)
     */
    public void draw( ) {
        
        GUI.getInstance().addBackgroundToDraw(background, offsetX);
               
        for( FunctionalItem item : items ) 
            item.draw( );
        for( FunctionalNPC npc : npcs ) 
            npc.draw( );
        for ( FunctionalAtrezzo at : atrezzo )
        	at.draw();
        player.draw( );

        if(foreground != null)
            GUI.getInstance().addForegroundToDraw(foreground, offsetX);
        
    }

    /**
     * Returns the element in the given position.
     * If there is no element, null is returned
     * @param x the horizontal position
     * @param y the vertical position
     * @return the element in the given position
     */
    public FunctionalElement getElementInside( int x, int y ) {
        FunctionalElement element = null;

        Iterator<FunctionalItem> ito = items.iterator( );
        while( ito.hasNext( ) && element == null ) {
            FunctionalItem currentItem = ito.next( );
            if( currentItem.isPointInside( x + Game.getInstance().getFunctionalScene( ).getOffsetX(), y ) )
                element = currentItem;
        }
        
        Iterator<FunctionalActiveArea> ita = areas.iterator( );
        while( ita.hasNext( ) && element == null ) {
            FunctionalActiveArea currentActiveArea = ita.next( );
            if( currentActiveArea.isPointInside( x + Game.getInstance().getFunctionalScene( ).getOffsetX(), y ) )
                element = currentActiveArea;
        }

        Iterator<FunctionalNPC> itp = npcs.iterator( );
        while( itp.hasNext( ) && element == null ) {
            FunctionalNPC currentNPC = itp.next( );
            if( currentNPC.isPointInside( x + Game.getInstance().getFunctionalScene( ).getOffsetX(), y ) )
                element = currentNPC;
        }

        return element;
    }
    
    public FunctionalTrajectory getTrajectory() {
    	return trajectory;
    }

    /**
     * Returns the exit in the given position.
     * If there is no exit, null is returned.
     * @param x the horizontal position
     * @param y the vertical position
     * @return the exit in the given position
     */
    public Exit getExitInside( int x, int y ) {
        Exit exit = null;
        boolean found = false;
        Iterator<Exit> ito = scene.getExits( ).iterator( );
        while( ito.hasNext( ) && !found ) {
            exit = ito.next( );
            found = exit.isPointInside( x + offsetX, y );
        }
        if( !found )
            exit = null;
        return exit;
    }

    /**
     * Notify that the user has clicked the scene
     * @param x the horizontal position of the click
     * @param y the vertical position of the click
     * @param actionSelected the current action selected (use, give, grab, look, ...)
     */
    public void mouseClicked( int x, int y ) {
        // FIXME Francis: Aclarar el uso del offset, ya que se a�ade en sitios que no deberia y viceversa
        FunctionalElement element = getElementInside( x + offsetX, y );
        if( Game.getInstance( ).getActionManager( ).getActionSelected( ) == ActionManager.ACTION_GOTO || element == null ) {
            // Check barriers (only 3rd person mode)
            int destX = x+offsetX;
            int destY = y;
            FunctionalGoTo functionalGoTo = new FunctionalGoTo(null, destX, destY);
            int finalX = functionalGoTo.getPosX();
            int finalY = functionalGoTo.getPosY();
            Exit exit = Game.getInstance( ).getFunctionalScene( ).getExitInside( finalX, finalY );
            if( exit == null )
                player.addAction(functionalGoTo);
            else {
            	player.addAction(new FunctionalExit(exit));
            	player.addAction(functionalGoTo);
            }
            Game.getInstance( ).getActionManager( ).setActionSelected( ActionManager.ACTION_GOTO );
        } else {
            Game.getInstance().getFunctionalPlayer().performActionInElement( element );
        }
    }

    //private static final float SEC_GAP = 5;
    
    public int[] checkPlayerAgainstBarriers (int destX, int destY){
        int[] finalPos = new int [2];
        finalPos[0] = destX;
        finalPos[1] = destY;
        for (FunctionalBarrier barrier: barriers){
            int[] newDest = barrier.checkPlayerAgainstBarrier( player, finalPos[0], finalPos[1] );
            finalPos[0] = newDest[0];
            finalPos[1] = newDest[1];
        }
        return finalPos;
    }
    
    /**
     * Returns the identifier of the backgrounds music.
     * @return Identifier number of the background music
     */
    public long getBackgroundMusicId( ) {
        return backgroundMusicId;
    }
    
    /**
     * Stops the background music of the scene
     */
    public void stopBackgroundMusic(){
        MultimediaManager.getInstance( ).stopPlaying( backgroundMusicId );
    }
    
    /**
     * Load and play the background music if is not active.
     * If its the first time it loads, it obtains the ID of the background
     * music to be able to identify itself from other sounds.
     */
    public void playBackgroundMusic(){
        if( Game.getInstance( ).getOptions( ).isMusicActive( ) ){
            if( backgroundMusicId != -1 ){
                if( !MultimediaManager.getInstance( ).isPlaying( backgroundMusicId ) ){
                    backgroundMusicId = MultimediaManager.getInstance( ).loadMusic( resources.getAssetPath( Scene.RESOURCE_TYPE_MUSIC ), true );
                    MultimediaManager.getInstance( ).startPlaying( backgroundMusicId );
                }
            }else{
                if( resources.existAsset( Scene.RESOURCE_TYPE_MUSIC ) ) {
                    backgroundMusicId = MultimediaManager.getInstance( ).loadMusic( resources.getAssetPath( Scene.RESOURCE_TYPE_MUSIC ), true );
                    MultimediaManager.getInstance( ).startPlaying( backgroundMusicId );
                } else {
                    MultimediaManager.getInstance( ).stopPlayingMusic();
                }
            }
        }    }
    
    /**
     * Creates the current resource block to be used
     */
    public Resources createResourcesBlock( ) {
        
        // Get the active resources block
        Resources newResources = null;
        for( int i = 0; i < scene.getResources( ).size( ) && newResources == null; i++ )
            if( new FunctionalConditions(scene.getResources( ).get( i ).getConditions( )).allConditionsOk( ) )
                newResources = scene.getResources( ).get( i );

        // If no resource block is available, create a default one 
        if (newResources == null){
            newResources = new Resources();
            newResources.addAsset( new Asset( Scene.RESOURCE_TYPE_BACKGROUND, ResourceHandler.DEFAULT_BACKGROUND ) );
            newResources.addAsset( new Asset( Scene.RESOURCE_TYPE_FOREGROUND, ResourceHandler.DEFAULT_FOREGROUND ) );
            newResources.addAsset( new Asset( Scene.RESOURCE_TYPE_HARDMAP, ResourceHandler.DEFAULT_HARDMAP ) );
        }
        return newResources;
    }
}
