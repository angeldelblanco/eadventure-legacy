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
import es.eucm.eadventure.common.auxiliar.ReleaseFolders;
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
public class EAdventure {
    
	/**
     * Launchs a new e-Adventure game
     * @param args Arguments
     */
    public static void main( String[] args ) {

        // Load the configuration
        ConfigData.loadFromXML( ReleaseFolders.configFileEngineRelativePath() );
        String languageFile = ConfigData.getLanguangeFile( );

        // Init the strings of the application
        TextConstants.loadStrings( ReleaseFolders.LANGUAGE_DIR_ENGINE+"/"+languageFile );
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
       
        GameLauncher gameLauncher = new GameLauncher( );
        
        if (args.length == 0) 
            gameLauncher.init( new File( "" ), "" );
        else {
        	if ( args[0] != "") {
        		
        		File file = new File(args[0]);
        	 
        		if (file.exists( )){
        			gameLauncher.init( new File(args[0]),args[1] );
          
        		} else {
                
        			JOptionPane.showMessageDialog(null,
                        TextConstants.getText("ErrorMessage.Title"),
                        TextConstants.getText("ErrorMessage.Content"),
                        JOptionPane.ERROR_MESSAGE);
                
                gameLauncher.init(new File(""), "");
            }
        	}
        	else 
        		gameLauncher.init( new File( "" ), args[1] );
        	}
        new Thread( gameLauncher ).start( );

    }
}
