/*******************************************************************************
 * <e-Adventure> (formerly <e-Game>) is a research project of the <e-UCM>
 * research group.
 * 
 * Copyright 2005-2012 <e-UCM> research group.
 * 
 * <e-UCM> is a research group of the Department of Software Engineering and
 * Artificial Intelligence at the Complutense University of Madrid (School of
 * Computer Science).
 * 
 * C Profesor Jose Garcia Santesmases sn, 28040 Madrid (Madrid), Spain.
 * 
 * For more info please visit: <http://e-adventure.e-ucm.es> or
 * <http://www.e-ucm.es>
 * 
 * ****************************************************************************
 * This file is part of <e-Adventure>, version 1.4.
 * 
 * You can access a list of all the contributors to <e-Adventure> at:
 * http://e-adventure.e-ucm.es/contributors
 * 
 * ****************************************************************************
 * <e-Adventure> is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * <e-Adventure> is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with <e-Adventure>. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package es.eucm.eadventure.editor.plugin.ead2;

import es.eucm.ead.engine.desktop.DesktopGame;
import es.eucm.ead.exporter.AndroidExporter;
import es.eucm.ead.exporter.JarExporter;
import es.eucm.ead.importer.AdventureConverter;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.tools.java.utils.FileUtils;
import es.eucm.ead.tools.java.utils.Log4jConfig;
import es.eucm.ead.tools.java.utils.Log4jConfig.Slf4jLevel;
import es.eucm.eadventure.editor.control.Controller;
import org.apache.maven.cli.MavenCli;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Converter {

    private Controller controller;

    private AdventureConverter adventureConverter;

    private DesktopGame game;

    private JFrame frame;

    private JarExporter jarExporter;

    private AndroidExporter androidExporter;

    public Converter( Controller controller ) {
        Log4jConfig.configForConsole(Slf4jLevel.Debug, null);
        this.controller = controller;
        MavenCli maven = new MavenCli();
        jarExporter = new JarExporter(maven);
        androidExporter = new AndroidExporter(maven);
        adventureConverter = new AdventureConverter( );
        adventureConverter.setEnableSimplifications(false);
        SystemFields.EXIT_WHEN_CLOSE.getVarDef( ).setInitialValue(false);
    }

    private String getNewProjectFolder( ){
        return controller.getProjectFolder() + "/ead2";
    }

    public void launch( ) {
        String folder = convert();

        game.setModel( folder );
        // Frame needs to be visible
        frame.setVisible( true );
        game.getGame( ).restart( true );
    }

    public String convert(){
        String folder = getNewProjectFolder();
        File f = new File( folder);
        if( f.exists( ) ) {
            try {
                FileUtils.deleteRecursive(f);
            } catch (IOException e) {

            }
        }
        adventureConverter.convert( controller.getProjectFolder(), folder );
        return folder;
    }

    public void run(){
        if( game == null ) {
            initGame( );
        }
        game.setDebug(false);
        launch();
    }

    public void debug( ) {
        if( game == null ) {
            initGame( );
        }
        game.setDebug(true);
        launch( );
    }

    public void initGame( ) {

        game = new DesktopGame( false );
        String folder = getNewProjectFolder();
        game.setModel( folder );
        frame = game.start( );
    }

    public boolean exportJar( String destiny ){
        jarExporter.export(controller.getProjectFolder(), convert(), destiny);
        return true;
    }

    public boolean exportWar( String destiny ){
        String folder = getNewProjectFolder();
        String destinyFolder = adventureConverter.convert(folder, null);
        return true;
    }

    public DesktopGame getGame(){
        return game;
    }

    public void setSimplifications(boolean simplifications) {
        adventureConverter.setEnableSimplifications(simplifications);
    }

    public boolean exportApk(String destiny, Properties properties) {
        androidExporter.export(controller.getProjectFolder(), convert(), destiny, properties);
        return true;
    }
}
