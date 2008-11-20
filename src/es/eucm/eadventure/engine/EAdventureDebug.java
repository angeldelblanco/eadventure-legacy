package es.eucm.eadventure.engine;

import javax.media.Codec;
import javax.media.Format;
import javax.media.PlugInManager;
import javax.media.format.VideoFormat;

import de.schlichtherle.io.ArchiveDetector;
import de.schlichtherle.io.DefaultArchiveDetector;
import de.schlichtherle.io.File;
import es.eucm.eadventure.engine.core.control.Game;
import es.eucm.eadventure.engine.core.control.config.ConfigData;
import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.engine.gamelauncher.GameLauncher;
import es.eucm.eadventure.engine.resourcehandler.ResourceHandler;

import javax.swing.*;

/**
 * This is the main class, when run standalone. Creates a new game and runs it.
 * 
 */
/**
 * @updated by Javier Torrente. New functionalities added
 * - Support for .ead files. Therefore <e-Adventure> files are no longer .zip but .ead
 *  @updated by Enrique L�pez. Functionalities added (10/2008)
 * - Multilanguage support. Two new classes added
 */
public class EAdventureDebug {
	
	private static final String CONFIG_FILE = "config_engine.xml";
	
	private static final String LANGUAGE_DIR = "lanengine";

    /**
     * Launchs a new e-Adventure game
     * @param args Arguments
     */
    public static void main( String[] args ) {
        if (args.length<2)
            return;
        
        String filePath = args[0];
        String languageFile = args[1];
        
        // Load the configuration
        ConfigData.loadFromData( languageFile, "" );

        // Init the strings of the application
        TextConstants.loadStrings( languageFile );
        File.setDefaultArchiveDetector(new DefaultArchiveDetector(
                ArchiveDetector.NULL, // delegate
                new String[] {
                    //"ead", "de.schlichtherle.io.archive.zip.JarDriver",
                    "ead", "de.schlichtherle.io.archive.zip.Zip32Driver",
                }));
        try {
            Codec video = (Codec) Class.forName(
                    "net.sourceforge.jffmpeg.VideoDecoder").newInstance();
            PlugInManager.addPlugIn("net.sourceforge.jffmpeg.VideoDecoder",
                    video.getSupportedInputFormats(),
                    new Format[] { new VideoFormat(VideoFormat.MPEG) },
                    PlugInManager.CODEC);
        } catch (Exception e) {
        }

        // Set the Look&Feel
        try {
            javax.swing.UIManager.setLookAndFeel( javax.swing.UIManager.getSystemLookAndFeelClassName( ) );
        } catch( Exception e ) {
            e.printStackTrace( );
        }
       
        ResourceHandler.setRestrictedMode( false, false );
        ResourceHandler.getInstance( ).setZipFile( filePath );
        Game.create( );
        Game.getInstance( ).setAdventureName( filePath.substring( 
                Math.max( filePath.lastIndexOf( "\\" ), filePath.lastIndexOf( "/" )),
                filePath.length( ))) ;
        Game.getInstance( ).run( );
        Game.delete( );
        ResourceHandler.getInstance( ).closeZipFile( );
        ResourceHandler.delete( );
    }
}
