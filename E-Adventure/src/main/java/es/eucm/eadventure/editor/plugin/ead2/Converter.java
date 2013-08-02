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

import ead.common.model.elements.operations.SystemFields;
import ead.converter.AdventureConverter;
import ead.engine.core.gdx.desktop.DesktopGame;
import ead.exporter.JarExporter;
import ead.utils.Log4jConfig;
import ead.utils.Log4jConfig.Slf4jLevel;
import es.eucm.eadventure.common.auxiliar.File;
import es.eucm.eadventure.editor.control.Controller;

import javax.swing.*;

public class Converter {

    private Controller controller;

    private AdventureConverter adventureConverter;

    private DesktopGame game;

    private JFrame frame;

    private JarExporter jarExporter;

    public Converter( ) {
        Log4jConfig.configForConsole(Slf4jLevel.Debug, null);
        this.controller = Controller.getInstance( );
        jarExporter = new JarExporter();
        adventureConverter = new AdventureConverter( );
        SystemFields.EXIT_WHEN_CLOSE.getVarDef( ).setInitialValue(false);
    }

    private String getNewProjectFolder( ){
        return controller.getProjectFolder() + "/ead2";
    }

    public void launch( ) {
        String folder = getNewProjectFolder();
        File f = new File( folder, "data.xml" );
        if( f.exists( ) ) {
            f.delete( );
        }
        adventureConverter.convert( controller.getProjectFolder(), folder );

        game.setModel( folder );
        // Frame needs to be visible
        frame.setVisible( true );
        game.getGame( ).restart( true );
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
        adventureConverter.convert(controller.getProjectFolder(), getNewProjectFolder());
        jarExporter.export(controller.getProjectFolder(), "ead2", destiny);
        return true;
    }

    public boolean exportWar( String destiny ){
        String folder = getNewProjectFolder();
        String destinyFolder = adventureConverter.convert(folder, null);
        return true;
    }

}
