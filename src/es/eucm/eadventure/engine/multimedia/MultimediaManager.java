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
package es.eucm.eadventure.engine.multimedia;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import es.eucm.eadventure.common.loader.Loader;
import es.eucm.eadventure.engine.core.control.animations.Animation;
import es.eucm.eadventure.engine.core.control.animations.FrameAnimation;
import es.eucm.eadventure.engine.core.control.animations.ImageAnimation;
import es.eucm.eadventure.engine.core.control.animations.ImageSet;
import es.eucm.eadventure.engine.core.gui.GUI;
import es.eucm.eadventure.engine.resourcehandler.EngineImageLoader;
import es.eucm.eadventure.engine.resourcehandler.ResourceHandler;

/**
 * This class manages various aspects related to multimedia in eAdventure.
 */
public class MultimediaManager {

    /**
     * Sound midi type
     */
    public static final int MIDI = 0;

    /**
     * Sound mp3 type
     */
    public static final int MP3 = 1;

    /**
     * Cached scene image category
     */
    public static final int IMAGE_SCENE = 0;

    /**
     * Cached menu image category
     */
    public static final int IMAGE_MENU = 1;

    /**
     * Cached player image category
     */
    public static final int IMAGE_PLAYER = 2;

    /**
     * Images cache
     */
    private HashMap<String, Image>[] imageCache;

    /**
     * Mirrored images cache
     */
    private HashMap<String, Image>[] mirrorImageCache;

    /**
     * Sounds cache
     */
    private HashMap<Long, Sound> soundCache;

    /**
     * Background music's id
     */
    private long musicSoundId;

    /**
     * Instance for the singleton
     */
    private static MultimediaManager instance = new MultimediaManager( );

    private HashMap<String, Animation> animationCache;

    /**
     * Returns the MultimediaManager instance. Notice MultimediaManager is a
     * singleton class.
     * 
     * @return the MultimediaManager singleton instance
     */
    public static MultimediaManager getInstance( ) {

        return instance;
    }

    /**
     * Empty constructor
     */
    @SuppressWarnings ( "unchecked")
    private MultimediaManager( ) {

        imageCache = new HashMap[ 3 ];
        for( int i = 0; i < 3; i++ )
            imageCache[i] = new HashMap<String, Image>( );
        mirrorImageCache = new HashMap[ 3 ];
        for( int i = 0; i < 3; i++ )
            mirrorImageCache[i] = new HashMap<String, Image>( );

        soundCache = new HashMap<Long, Sound>( );

        animationCache = new HashMap<String, Animation>( );

        musicSoundId = -1;
    }

    /**
     * Returns an Image for imagePath. If the image file does not exists, a
     * FileNotFoundException is thrown.
     * 
     * @param imagePath
     * @param category
     *            image category for caching
     * @return an Image for imagePath.
     */
    public Image loadImage( String imagePath, int category ) {

        Image image = imageCache[category].get( imagePath );
        // If the image is in cache, don't load it
        if( image == null ) {
            // Load the image and store it in cache
            image = getScaledImage( ResourceHandler.getInstance( ).getResourceAsImage( imagePath ), 1, 1 );
            if( image != null ){
                imageCache[category].put( imagePath, image );
            } else {
                image = buildResourceNotFoundImage( imagePath );
                imageCache[category].put( imagePath, image );
            }
        }
        return image;
    }

    /**
     * Returns an Image for imagePath. If the image file does not exists, a
     * FileNotFoundException is thrown.
     * 
     * @param imagePath
     *            Image path
     * @param category
     *            Category for the image
     * @return an Image for imagePath.
     */
    public Image loadImageFromZip( String imagePath, int category ) {
        Image image = imageCache[category].get( imagePath );
        // If the image is in cache, don't load it
        if( image == null ) {
            // Load the image and store it in cache
            image = getScaledImage( ResourceHandler.getInstance( ).getResourceAsImageFromZip( imagePath ), 1, 1 );
            if( image != null ) {
                imageCache[category].put( imagePath, image );
            } else {
                image = buildResourceNotFoundImage( imagePath );
                imageCache[category].put( imagePath, image );
            }
        }
        return image;
    }

    /**
     * Returns an Image for imagePath after mirroring it. If the image file does
     * not exists, a FileNotFoundException is thrown.
     * 
     * @param imagePath
     *            Image path
     * @param category
     *            Category for the image
     * @return an Image for imagePath.
     */
    public Image loadMirroredImageFromZip( String imagePath, int category ) {

        Image image = mirrorImageCache[category].get( imagePath );
        // If the image is in cache, don't load it
        if( image == null ) {
            // Load the image and store it in cache
            image = getScaledImage( loadImageFromZip( imagePath, category ), -1, 1 );
            if( image != null ) {
                mirrorImageCache[category].put( imagePath, image );
            }
        }
        return image;
    }

    /**
     * Clear the image cache of the given category
     * 
     * @param category
     *            Image category to clear
     */
    public void flushImagePool( int category ) {

        imageCache[category].clear( );
        mirrorImageCache[category].clear( );
    }

    /**
     * Returns a scaled image of the given image. The new width is x times the
     * old width. The new height is y times the old height.
     * 
     * @param image
     *            the image to be scaled
     * @param x
     *            width scale factor
     * @param y
     *            height scale factor
     * @return a scaled image of the given image.
     */
    private Image getScaledImage( Image image, float x, float y ) {

        Image newImage = null;

        if( image != null ) {

            if( x != 1.0f || y != 1.0f ) {
                // set up the transform
                AffineTransform transform = new AffineTransform( );
                transform.scale( x, y );
                transform.translate( ( x - 1 ) * image.getWidth( null ) / 2, ( y - 1 ) * image.getHeight( null ) / 2 );

                // create a transparent (not translucent) image
                newImage = GUI.getInstance( ).getGraphicsConfiguration( ).createCompatibleImage( image.getWidth( null ), image.getHeight( null ), Transparency.TRANSLUCENT );

                //newImage = new BufferedImage (titleWidth+4, titleHeight+4, BufferedImage.TYPE_INT_ARGB);
                //Graphics2D gr = titleImage.createGraphics( );
                //gr.setRenderingHints( GUI.getOptimumRenderingHints());

                
                // draw the transformed image
                Graphics2D g = (Graphics2D) newImage.getGraphics( );

                g.drawImage( image, transform, null );
                g.dispose( );
            }
            else
                return image;
        }
        return newImage;
    }

    /**
     * Returns a scaled image that fits in the game screen.
     * 
     * @param image
     *            the image to be scaled.
     * @return a scaled image that fits in the game screen.
     */
    private Image getFullscreenImage( Image image ) {

        // set up the transform
        AffineTransform transform = new AffineTransform( );
        transform.scale( GUI.WINDOW_WIDTH / (double) image.getWidth( null ), GUI.WINDOW_HEIGHT / (double) image.getHeight( null ) );

        // create a transparent (not translucent) image
        Image newImage = GUI.getInstance( ).getGraphicsConfiguration( ).createCompatibleImage( GUI.WINDOW_WIDTH, GUI.WINDOW_HEIGHT, Transparency.TRANSLUCENT );

        // draw the transformed image
        Graphics2D g = (Graphics2D) newImage.getGraphics( );
        g.drawImage( image, transform, null );
        g.dispose( );

        return newImage;
    }

    /**
     * Returns an Id of the sound of type soundType for the file in soundPath,
     * and sets whether it has to be played or not in a loop.
     * 
     * @param soundType
     *            the type of the sound to be created
     * @param soundPath
     *            the path to the sound to be created
     * @param loop
     *            whether or not the sound must be played in a loop
     * @return an Id that represents the sound with the given configuration.
     */
    public synchronized long loadSound( int soundType, String soundPath, boolean loop ) {

        Sound sound = null;
        long soundId = -1;
        switch( soundType ) {
            case MIDI:
                sound = new SoundMidi( soundPath, loop );
                break;
            case MP3:
                sound = new SoundMp3( soundPath, loop );
                break;
        }
        if( sound != null ) {
            soundCache.put( new Long( sound.getId( ) ), sound );
            soundId = sound.getId( );
        }
        return soundId;
    }

    /**
     * Returns an Id of the sound for the file in soundPath, and sets whether it
     * has to be played or not in a loop. The sound's type is guessed from its
     * soundPath's extension.
     * 
     * @param soundPath
     *            the path to the sound to be created
     * @param loop
     *            whether or not the sound must be played in a loop
     * @return an Id that represents the sound with the given configuration.
     */
    public synchronized long loadSound( String soundPath, boolean loop ) {

        String type = soundPath.substring( soundPath.lastIndexOf( "." ) + 1 ).toLowerCase( );
        int soundType = -1;
        if( type.equals( "mp3" ) )
            soundType = MP3;
        else if( type.equals( "midi" ) )
            soundType = MIDI;
        else if( type.equals( "mid" ) )
            soundType = MIDI;
        return loadSound( soundType, soundPath, loop );
    }

    /**
     * Returns an Id of the music of type musicType for the file in musicPath,
     * and sets whether it has to be played or not in a loop.
     * 
     * @param soundType
     *            the type of the music to be created
     * @param soundPath
     *            the path to the music to be created
     * @param loop
     *            whether or not the music must be played in a loop
     * @return an Id that represents the music with the given configuration.
     */
    public synchronized long loadMusic( int musicType, String musicPath, boolean loop ) {

        musicSoundId = loadSound( musicType, musicPath, loop );
        return musicSoundId;
    }

    /**
     * Returns an Id of the music for the file in musicPath, and sets whether it
     * has to be played or not in a loop. The sound's type is guessed from its
     * musicPath's extension.
     * 
     * @param soundPath
     *            the path to the music to be created
     * @param loop
     *            whether or not the music must be played in a loop
     * @return an Id that represents the music with the given configuration.
     */
    public synchronized long loadMusic( String musicPath, boolean loop ) {

        musicSoundId = loadSound( musicPath, loop );
        return musicSoundId;
    }

    /**
     * Plays the sound from the cache that have soundId as id
     * 
     * @param soundId
     *            Id of the sound to be played
     */
    public synchronized void startPlaying( long soundId ) {

        if( soundCache.containsKey( soundId ) ) {
            Sound sound = soundCache.get( soundId );
            if( sound != null )
                sound.startPlaying( );
        }
    }

    /**
     * Stops the sound from the cache that have soundId as id
     * 
     * @param soundId
     *            Id of the sound to be stopped
     */
    public synchronized void stopPlaying( long soundId ) {

        if( soundCache.containsKey( soundId ) ) {
            Sound sound = soundCache.get( soundId );
            if( sound != null )
                sound.stopPlaying( );
        }
    }

    public synchronized void stopPlayingMusic( ) {

        Collection<Sound> sounds = soundCache.values( );
        for( Sound sound : sounds ) {
            if( sound.getId( ) == musicSoundId ) {
                sound.stopPlaying( );
            }
        }
    }

    public synchronized void stopPlayingInmediately( long soundId ) {

        if( soundCache.containsKey( soundId ) ) {
            Sound sound = soundCache.get( soundId );
            if( sound != null ) {
                sound.stopPlaying( );
                sound.finalize( );
            }
        }

    }

    /**
     * Given the soundId, it returns whether the sound is playing or not
     * 
     * @param soundId
     *            long The soundId
     * @return true if the sound is playing
     */
    public synchronized boolean isPlaying( long soundId ) {

        boolean playing = false;
        if (soundId!=-1){
        if( soundCache.containsKey( soundId ) ) {
            Sound sound = soundCache.get( soundId );
            if( sound != null )
                playing = sound.isPlaying( );
        }
        }
        return playing;
    }

    /**
     * Update the status of all sounds cache and if they have finished, they are
     * removed from the cache
     * 
     * @throws InterruptedException
     */
    public synchronized void update( ) throws InterruptedException {

        Collection<Sound> sounds = soundCache.values( );
        ArrayList<Sound> soundsToRemove = new ArrayList<Sound>( );
        for( Sound sound : sounds ) {
            if( sound.getId( ) != musicSoundId && !sound.isAlive( ) ) {
                soundsToRemove.add( sound );
            }
        }
        for( int i = 0; i < soundsToRemove.size( ); i++ ) {
            Sound sound = soundsToRemove.get( i );
            sound.join( );
            sounds.remove( sound );
        }
    }

    /**
     * Stops and deletes all sounds currently in the cache, except the music
     */
    public synchronized void stopAllSounds( ) {

        Collection<Sound> sounds = soundCache.values( );
        ArrayList<Sound> soundsToRemove = new ArrayList<Sound>( );
        for( Sound sound : sounds ) {
            if( sound.getId( ) != musicSoundId ) {
                soundsToRemove.add( sound );
            }
        }
        for( int i = 0; i < soundsToRemove.size( ); i++ ) {
            Sound sound = soundsToRemove.get( i );
            try {
                sound.join( );
            }
            catch( InterruptedException e ) {
            }
            sounds.remove( sound );
        }
    }

    /**
     * Stops and deletes all sounds currently in the cache even the music
     */
    public synchronized void deleteSounds( ) {

        Collection<Sound> sounds = soundCache.values( );
        for( Sound sound : sounds ) {
            if( sound.getId( ) != musicSoundId ) {
                sound.stopPlaying( );
                try {
                    sound.join( );
                }
                catch( InterruptedException e ) {
                }
            }
        }
        sounds.clear( );
    }

    /**
     * Returns a animation from a path.
     * <p>
     * The animation can be generated from an eaa describing the animation or
     * with frames animationPath_xy.jpg, with xy from 01 to the last existing
     * file with that format (the extension can also be .png).
     * <p>
     * For example, loadAnimation( "path" ) will return an animation with frames
     * path_01.jpg, path_02.jpg, path_03.jpg, if path_04.jpg doesn't exists.
     * 
     * @param animationPath
     *            base path to the animation frames
     * @param mirror
     *            whether or not the frames must be mirrored
     * @param category
     *            Category of the animation
     * @return an Animation with frames animationPath_xy.jpg
     */
    public Animation loadAnimation( String animationPath, boolean mirror, int category ) {
        Animation temp = animationCache.get( animationPath + ( mirror ? "t" : "f" ) );
        if( temp != null )
            return temp;

        if( animationPath != null && animationPath.endsWith( ".eaa" ) ) {
            FrameAnimation animation = new FrameAnimation( Loader.loadAnimation( ResourceHandler.getInstance( ), animationPath, new EngineImageLoader() ) );
            animation.setMirror( mirror );
            temp = animation;
        }
        else {
            int i = 1;
            List<Image> frames = new ArrayList<Image>( );
            Image currentFrame = null;
            boolean end = false;
            while( !end ) {
                if( mirror ){
                    if (ResourceHandler.getInstance( ).getResourceAsImageFromZip( animationPath + "_" + leadingZeros( i ) + ".png")!=null)
                        currentFrame = loadMirroredImageFromZip( animationPath + "_" + leadingZeros( i ) + ".png", category );
                    else
                        end=true;
                }else{
                    if (ResourceHandler.getInstance( ).getResourceAsImageFromZip(  animationPath + "_" + leadingZeros( i ) + ".png")!=null)
                        currentFrame = loadImageFromZip( animationPath + "_" + leadingZeros( i ) + ".png", category );
                    else
                        end=true;
                }

                if( currentFrame != null ) {
                    frames.add( currentFrame );
                    i++;
                }
                else
                    end = true;
            }
            ImageAnimation animation = new ImageAnimation( );
            animation.setImages( frames.toArray( new Image[] {} ) );
            temp = animation;
        }
        animationCache.put( animationPath + ( mirror ? "t" : "f" ), temp );
        return temp;
    }

    /**
     * Returns a animation with frames animationPath_xy.jpg, with xy from 01 to
     * the last existing file with that format (also the extension can be .png).
     * <p>
     * For example, loadAnimation( "path" ) will return an animation with frames
     * path_01.jpg, path_02.jpg, path_03.jpg, if path_04.jpg doesn't exists.
     * 
     * @param slidesPath
     *            base path to the animation frames
     * @param category
     *            Category of the animation
     * @return an Animation with frames animationPath_xy.jpg
     */
    public Animation loadSlides( String slidesPath, int category ) {

        ImageSet imageSet = null;
        if( slidesPath.endsWith( ".eaa" ) ) {
            FrameAnimation animation = new FrameAnimation( Loader.loadAnimation( ResourceHandler.getInstance( ), slidesPath, new EngineImageLoader()) );
            animation.setFullscreen( true );
            return animation;
        }
        else {
            int i = 1;
            List<Image> slides = new ArrayList<Image>( );
            Image currentSlide = null;
            boolean end = false;

            while( !end ) {
                if (ResourceHandler.getInstance( ).getResourceAsImageFromZip( slidesPath + "_" + leadingZeros( i ) + ".jpg")!=null)
                    currentSlide = loadImageFromZip( slidesPath + "_" + leadingZeros( i ) + ".jpg", category );
                else
                    end=true;

                if( currentSlide != null ) {
                    slides.add( getFullscreenImage( currentSlide ) );
                    i++;
                }
                else
                    end = true;
            }

            imageSet = new ImageSet( );
            imageSet.setImages( slides.toArray( new Image[] {} ) );
        }

        return imageSet;
    }

    /**
     * @param n
     *            number to convert to a String
     * @return a 2 character string with value n
     */
    private String leadingZeros( int n ) {

        String s;
        if( n < 10 )
            s = "0";
        else
            s = "";
        s = s + n;
        return s;
    }

    public void flushAnimationPool( ) {

        animationCache.clear( );
    }

    /**
     * Added in version v1.4. It's used to generate a "Not available" image when a resource is not found.
     * How the image is produced depends on the relative path of the resource (that is, the category of the resource).
     * For example, if it belongs to backgrounds (assets/background/*.*), the image is given 800x600 dimensions
     * and a solid black "Resource not found: +uri" is print onto it.
     * 
     * NOTE: This method does not actually check if the resource exists; it just builds the image.
     * 
     * @param uri   The uri of the resource that was not found
     * @return  BufferedImage with the "Not available" image
     */
    public BufferedImage buildResourceNotFoundImage(String uri){
        int w=100;int h=100;
        if (uri.startsWith( "gui/cursors" )){
            w=32;h=32;
        }
        else if (uri.startsWith( "gui/arrows" )){
            w=80;h=48;
        }
        else if (uri.startsWith( "gui/buttons" )){
            w=80;h=48;
        }
        else if (uri.startsWith( "assets/image" )){
            w=300;h=100;
        }
        else if (uri.startsWith( "assets/background" )){
            w=GUI.WINDOW_WIDTH;h=GUI.WINDOW_HEIGHT;
        }
        BufferedImage bImg = new BufferedImage(w, h, BufferedImage.OPAQUE);
        Graphics2D g = bImg.createGraphics( );
        if (w>10 && h>10){
            g.setColor( Color.red );
            g.fillRect( 0, 0, w, h );
            g.setColor( Color.white );
            g.fillRect( 5, 5, w-10, h-10 );
            g.setColor( Color.red );
            Polygon p = new Polygon();
            p.addPoint( 5, 0 );
            p.addPoint( w, h-5 );
            p.addPoint( w-5, h );
            p.addPoint( 0, 5 );
            g.fillPolygon( p );
            
            Polygon p2 = new Polygon();
            p2.addPoint( w-5, 0 );
            p2.addPoint( w, 5 );
            p2.addPoint( 5, h );
            p2.addPoint( 0, h-5 );
            g.fillPolygon( p2 );
        } else {
            g.setColor( Color.white );
            g.fillRect( 0, 0, w, h );
        }
            
        g.setColor( Color.black );
        g.drawString( "Resource not found:", w>50?50:0, h/2-20 );
        g.drawString( uri, w>50?50:0, GUI.WINDOW_HEIGHT/2+20 );
        g.dispose( );
        return bImg;
    }
    
}
