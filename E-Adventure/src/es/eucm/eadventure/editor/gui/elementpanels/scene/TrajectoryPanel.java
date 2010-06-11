/*******************************************************************************
 * <e-Adventure> (formerly <e-Game>) is a research project of the <e-UCM>
 *         research group.
 *  
 *   Copyright 2005-2010 <e-UCM> research group.
 * 
 *   You can access a list of all the contributors to <e-Adventure> at:
 *         http://e-adventure.e-ucm.es/contributors
 * 
 *   <e-UCM> is a research group of the Department of Software Engineering
 *         and Artificial Intelligence at the Complutense University of Madrid
 *         (School of Computer Science).
 * 
 *         C Profesor Jose Garcia Santesmases sn,
 *         28040 Madrid (Madrid), Spain.
 * 
 *         For more info please visit:  <http://e-adventure.e-ucm.es> or
 *         <http://www.e-ucm.es>
 * 
 * ****************************************************************************
 * 
 * This file is part of <e-Adventure>, version 1.2.
 * 
 *     <e-Adventure> is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     <e-Adventure> is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with <e-Adventure>.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.eucm.eadventure.editor.gui.elementpanels.scene;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.AssetsController;
import es.eucm.eadventure.editor.control.controllers.scene.ActiveAreaDataControl;
import es.eucm.eadventure.editor.control.controllers.scene.BarrierDataControl;
import es.eucm.eadventure.editor.control.controllers.scene.ElementReferenceDataControl;
import es.eucm.eadventure.editor.control.controllers.scene.ExitDataControl;
import es.eucm.eadventure.editor.control.controllers.scene.SceneDataControl;
import es.eucm.eadventure.editor.control.controllers.scene.TrajectoryDataControl;
import es.eucm.eadventure.editor.control.tools.scene.ChangeHasTrajectoryTool;
import es.eucm.eadventure.editor.gui.Updateable;
import es.eucm.eadventure.editor.gui.otherpanels.ScenePreviewEditionPanel;
import es.eucm.eadventure.editor.gui.otherpanels.TrajectoryEditionPanel;
import es.eucm.eadventure.editor.gui.otherpanels.imageelements.ImageElement;
import es.eucm.eadventure.editor.gui.otherpanels.imageelements.ImageElementPlayer;

public class TrajectoryPanel extends JPanel implements Updateable {

    private static final long serialVersionUID = 1L;

    private ScenePreviewEditionPanel spep;

    private TrajectoryEditionPanel tep = null;

    private TrajectoryDataControl dataControl;

    private SceneDataControl sceneDataControl;

    private JRadioButton useTrajectoryRadioButton;

    private JRadioButton initialPositionRadioButton;

    private JPanel initialPositionPanel = null;

    private String scenePath;

    /**
     * Constructor.
     * 
     * @param barriersListDataControl
     *            ActiveAreas list controller
     */
    public TrajectoryPanel( TrajectoryDataControl trajectoryDataControl, SceneDataControl sceneDataControl ) {
        this.dataControl = trajectoryDataControl;
        this.sceneDataControl = sceneDataControl;
        scenePath = Controller.getInstance( ).getSceneImagePath( sceneDataControl.getId( ) );

        setLayout( new BorderLayout( ) );

        JPanel buttonContainer = new JPanel( );
        buttonContainer.setLayout( new GridLayout( 0, 2 ) );

        // create trajectory button
        useTrajectoryRadioButton = new JRadioButton( TC.get( "Scene.UseTrajectory" ), sceneDataControl.getTrajectory( ).hasTrajectory( ) );
        useTrajectoryRadioButton.addItemListener( new TrajectoryCheckBoxListener( ) );
        buttonContainer.add( useTrajectoryRadioButton );
        buttonContainer.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TC.get( "Scene.UseTrajectoryPanel" ) ) );

        // Create initial Position button
        initialPositionRadioButton = new JRadioButton( TC.get( "Scene.UseInitialPosition" ), !sceneDataControl.getTrajectory( ).hasTrajectory( ) );
        initialPositionRadioButton.addItemListener( new InitialPositionCheckBoxListener( ) );
        buttonContainer.add( initialPositionRadioButton );

        ButtonGroup buttonGroup = new ButtonGroup( );
        buttonGroup.add( useTrajectoryRadioButton );
        buttonGroup.add( initialPositionRadioButton );

        add( buttonContainer, BorderLayout.NORTH );

        changePanel( );
    }

    private void changePanel( ) {
        if( useTrajectoryRadioButton.isSelected( ) ) {
            if (tep != null)
                remove( tep );
            tep = new TrajectoryEditionPanel( scenePath, dataControl );
            spep = tep.getScenePreviewEditionPanel( );
            fillSpep( );
            add( tep, BorderLayout.CENTER );
        }
        else {
            initialPositionPanel = new JPanel( );
            initialPositionPanel.setLayout( new BorderLayout( ) );
            spep = new ScenePreviewEditionPanel( false, scenePath );
            fillSpep( );
            initialPositionPanel.add( spep, BorderLayout.CENTER );
            spep.setShowTextEdition( true );

            if( initialPositionRadioButton.isSelected( ) ) {
                Image image = AssetsController.getImage( Controller.getInstance( ).getPlayerImagePath( ) );
                spep.addPlayer( sceneDataControl, image );
                spep.setSelectedElement( new ImageElementPlayer( image, sceneDataControl ) );
                spep.setFixedSelectedElement( true );
            }
            spep.repaint( );
            add( initialPositionPanel, BorderLayout.CENTER );
        }
    }

    private void fillSpep( ) {

        if( scenePath != null && spep != null ) {
            for( ElementReferenceDataControl elementReference : sceneDataControl.getReferencesList( ).getItemReferences( ) ) {
                spep.addElement( ScenePreviewEditionPanel.CATEGORY_OBJECT, elementReference );
            }
            spep.setMovableCategory( ScenePreviewEditionPanel.CATEGORY_OBJECT, false );
            for( ElementReferenceDataControl elementReference : sceneDataControl.getReferencesList( ).getNPCReferences( ) ) {
                spep.addElement( ScenePreviewEditionPanel.CATEGORY_CHARACTER, elementReference );
            }
            spep.setMovableCategory( ScenePreviewEditionPanel.CATEGORY_CHARACTER, false );
            for( ElementReferenceDataControl elementReference : sceneDataControl.getReferencesList( ).getAtrezzoReferences( ) ) {
                spep.addElement( ScenePreviewEditionPanel.CATEGORY_ATREZZO, elementReference );
            }
            spep.setMovableCategory( ScenePreviewEditionPanel.CATEGORY_ATREZZO, false );
            for( ExitDataControl exit : sceneDataControl.getExitsList( ).getExits( ) ) {
                spep.addExit( exit );
            }
            spep.setMovableCategory( ScenePreviewEditionPanel.CATEGORY_EXIT, false );
            for( ActiveAreaDataControl activeArea : sceneDataControl.getActiveAreasList( ).getActiveAreas( ) ) {
                spep.addActiveArea( activeArea );
            }
            spep.setMovableCategory( ScenePreviewEditionPanel.CATEGORY_ACTIVEAREA, false );
            for( BarrierDataControl barrier : sceneDataControl.getBarriersList( ).getBarriers( ) ) {
                spep.addBarrier( barrier );
            }
        }
    }


    /**
     * Listener for the "Use initial position in this scene" check box.
     */
    private class InitialPositionCheckBoxListener implements ItemListener {
        public void itemStateChanged( ItemEvent e ) {
            sceneDataControl.toggleDefaultInitialPosition( );
            spep.setFixedSelectedElement( false );
            spep.setSelectedElement( (ImageElement) null );
            spep.removeElements( ScenePreviewEditionPanel.CATEGORY_PLAYER );

            Image image = AssetsController.getImage( Controller.getInstance( ).getPlayerImagePath( ) );
            spep.addPlayer( sceneDataControl, image );
            spep.setSelectedElement( new ImageElementPlayer( image, sceneDataControl ) );
            spep.setFixedSelectedElement( true );
            spep.repaint( );
        }
    }

    private class TrajectoryCheckBoxListener implements ItemListener {
        public void itemStateChanged( ItemEvent e ) {
            Controller.getInstance( ).addTool( new ChangeHasTrajectoryTool( ( (JRadioButton) e.getSource( ) ).isSelected( ), sceneDataControl ) );
            updateContents( );
        }
    }

    private void updateContents( ) {
        dataControl = sceneDataControl.getTrajectory( );

        if( initialPositionPanel != null ) {
            remove( initialPositionPanel );
            initialPositionPanel = null;
        }

        if( tep != null ) {
            remove( tep );
            tep = null;
        }

        changePanel( );
        updateUI( );
    }

    public boolean updateFields( ) {
        changePanel( );
        if( spep != null ) {
            spep.updateUI( );
            spep.repaint( );
        }
        return true;
    }
}
