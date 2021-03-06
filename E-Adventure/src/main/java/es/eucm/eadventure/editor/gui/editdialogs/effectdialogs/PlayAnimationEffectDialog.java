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
package es.eucm.eadventure.editor.gui.editdialogs.effectdialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import es.eucm.eadventure.common.auxiliar.File;
import es.eucm.eadventure.common.auxiliar.SpecialAssetPaths;
import es.eucm.eadventure.common.data.animation.Animation;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.AssetsController;
import es.eucm.eadventure.editor.control.controllers.EditorImageLoader;
import es.eucm.eadventure.editor.control.controllers.EffectsController;
import es.eucm.eadventure.editor.control.controllers.general.ResourcesDataControl;
import es.eucm.eadventure.editor.control.writer.AnimationWriter;
import es.eucm.eadventure.editor.gui.displaydialogs.AnimationDialog;
import es.eucm.eadventure.editor.gui.editdialogs.animationeditdialog.AnimationEditDialog;
import es.eucm.eadventure.editor.gui.otherpanels.positionimagepanels.PointImagePanel;
import es.eucm.eadventure.editor.gui.otherpanels.positionpanel.PositionPanel;

public class PlayAnimationEffectDialog extends EffectDialog {

    /**
     * Required.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Controller of the effects.
     */
    private EffectsController effectsController;

    /**
     * Text field containing the path.
     */
    private JTextField pathTextField;

    /**
     * Button to display the actual asset.
     */
    private JButton viewButton;

    /**
     * Combo box with the scenes.
     */
    private JComboBox scenesComboBox;

    /**
     * Panel to select and show the position.
     */
    private PositionPanel pointPositionPanel;

    /**
     * Constructor.
     * 
     * @param effectsController
     *            Controller of the effects
     * @param currentProperties
     *            Set of initial values
     */
    public PlayAnimationEffectDialog( EffectsController effectsController, HashMap<Integer, Object> currentProperties ) {

        // Call the super method
        super( TC.get( "PlayAnimationEffect.Title" ), true );
        this.effectsController = effectsController;

        // Create the set of values for the scenes
        List<String> scenesList = new ArrayList<String>( );
        scenesList.add( TC.get( "SceneLocation.NoSceneSelected" ) );
        String[] scenesArray = controller.getIdentifierSummary( ).getSceneIds( );
        for( String scene : scenesArray )
            scenesList.add( scene );
        scenesArray = scenesList.toArray( new String[] {} );

        // Load the image for the delete content button
        Icon deleteContentIcon = new ImageIcon( "img/icons/deleteContent.png" );

        // Create the asset panel and set the border
        JPanel assetPanel = new JPanel( );
        assetPanel.setLayout( new GridBagLayout( ) );
        assetPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TC.get( "PlayAnimationEffect.Title" ) ) );
        GridBagConstraints c = new GridBagConstraints( );
        c.insets = new Insets( 4, 4, 4, 4 );
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.weighty = 0;

        // Create the delete content button
        JButton deleteContentButton = new JButton( deleteContentIcon );
        deleteContentButton.addActionListener( new DeleteContentButtonActionListener( ) );
        deleteContentButton.setPreferredSize( new Dimension( 20, 20 ) );
        deleteContentButton.setToolTipText( TC.get( "Resources.DeleteAsset" ) );
        assetPanel.add( deleteContentButton, c );

        // Create the text field and insert it
        pathTextField = new JTextField( );
        pathTextField.setEditable( false );
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        assetPanel.add( pathTextField, c );

        // Create the "Select" button and insert it
        JButton selectButton = new JButton( TC.get( "Resources.Select" ) );
        selectButton.addActionListener( new ExamineButtonActionListener( ) );
        c.gridx = 2;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        assetPanel.add( selectButton, c );

        JButton editButton = new JButton( TC.get( "Resources.Create" ) + "/" + TC.get( "Resources.Edit" ) );
        editButton.addActionListener( new EditButtonListener( ) );
        c.gridx = 3;
        assetPanel.add( editButton );

        // Create the "View" button and insert it
        viewButton = new JButton( TC.get( "Resources.ViewAsset" ) );
        viewButton.setEnabled( false );
        viewButton.addActionListener( new ViewButtonActionListener( ) );
        c.gridx = 4;
        assetPanel.add( viewButton, c );

        // Create the main panel
        JPanel mainPanel = new JPanel( );
        mainPanel.setLayout( new GridBagLayout( ) );
        c = new GridBagConstraints( );

        // Set the border of the panel with the description
        mainPanel.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder( 5, 5, 0, 5 ), BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TC.get( "PlayAnimationEffect.Description" ) ) ) );

        // Add the asset panel to the main panel
        c.insets = new Insets( 2, 4, 4, 4 );
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        mainPanel.add( assetPanel, c );

        // Create and add the list of scenes
        c.gridy = 1;
        scenesComboBox = new JComboBox( scenesArray );
        scenesComboBox.addActionListener( new ScenesComboBoxActionListener( ) );
        mainPanel.add( scenesComboBox, c );

        // Create and add the panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 1;
        // Set the defualt values (if present)
        if( currentProperties != null ) {
            int x = 0;
            int y = 0;

            if( currentProperties.containsKey( EffectsController.EFFECT_PROPERTY_PATH ) ) {
                pathTextField.setText( (String) currentProperties.get( EffectsController.EFFECT_PROPERTY_PATH ) );
                viewButton.setEnabled( pathTextField.getText( ) != null );
            }

            if( currentProperties.containsKey( EffectsController.EFFECT_PROPERTY_X ) )
                x = Integer.parseInt( (String) currentProperties.get( EffectsController.EFFECT_PROPERTY_X ) );

            if( currentProperties.containsKey( EffectsController.EFFECT_PROPERTY_Y ) )
                y = Integer.parseInt( (String) currentProperties.get( EffectsController.EFFECT_PROPERTY_Y ) );

            if( x > 5000 )
                x = 5000;
            if( x < -2000 )
                x = -2000;
            if( y > 5000 )
                y = 5000;
            if( y < -2000 )
                y = -2000;
            pointPositionPanel = new PositionPanel( new PointImagePanel( ), x, y );
        }
        else {
            pointPositionPanel = new PositionPanel( new PointImagePanel( ), 400, 500 );
        }
        //pointPositionPanel = new PositionPanel( new PointImagePanel( ) );
        mainPanel.add( pointPositionPanel, c );

        // Add the panel to the center
        add( mainPanel, BorderLayout.CENTER );

        // Set the dialog
        //setResizable( false );
        setSize( 640, 480 );
        Dimension screenSize = Toolkit.getDefaultToolkit( ).getScreenSize( );
        setLocation( ( screenSize.width - getWidth( ) ) / 2, ( screenSize.height - getHeight( ) ) / 2 );
        setVisible( true );
    }

    @Override
    protected void pressedOKButton( ) {

        // Create a set of properties, and put the selected value
        properties = new HashMap<Integer, Object>( );
        properties.put( EffectsController.EFFECT_PROPERTY_PATH, pathTextField.getText( ) );
        properties.put( EffectsController.EFFECT_PROPERTY_X, String.valueOf( pointPositionPanel.getPositionX( ) ) );
        properties.put( EffectsController.EFFECT_PROPERTY_Y, String.valueOf( pointPositionPanel.getPositionY( ) ) );
    }

    /**
     * Listener for the delete content button.
     */
    private class DeleteContentButtonActionListener implements ActionListener {

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed( ActionEvent e ) {

            // Delete the current path and disable the view button
            pathTextField.setText( null );
            viewButton.setEnabled( false );
        }
    }

    /**
     * Listener for the examine button.
     */
    private class ExamineButtonActionListener implements ActionListener {

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed( ActionEvent e ) {

            // Ask the user for an animation
            String newPath = effectsController.selectAsset( EffectsController.ASSET_ANIMATION );

            // If a new value was selected, set it and enable the view button
            if( newPath != null ) {
                pathTextField.setText( newPath );
                viewButton.setEnabled( true );
            }
        }
    }

    /**
     * Listener for the view button.
     */
    private class ViewButtonActionListener implements ActionListener {

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed( ActionEvent e ) {

            new AnimationDialog( pathTextField.getText( ) );
        }
    }

    /**
     * Listener for the scenes combo box.
     */
    private class ScenesComboBoxActionListener implements ActionListener {

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed( ActionEvent arg0 ) {

            int selectedScene = scenesComboBox.getSelectedIndex( );

            // If the first option were selected, remove the image
            if( selectedScene == 0 )
                pointPositionPanel.removeImage( );

            // If other option were selected, load the image
            else
                pointPositionPanel.loadImage( controller.getSceneImagePath( scenesComboBox.getSelectedItem( ).toString( ) ) );
        }
    }

    /**
     * This class is the listener for the "Edit" buttons on the panels.
     */
    private class EditButtonListener implements ActionListener {

        /**
         * Constructor.
         * 
         * @param currentProperties
         * 
         * @param assetIndex
         *            Index of the asset
         */
        public EditButtonListener( ) {

        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed( ActionEvent e ) {

            String path = pathTextField.getText( );

            if( path != null && path.toLowerCase( ).endsWith( ".eaa" ) && !path.equals( SpecialAssetPaths.ASSET_EMPTY_ANIMATION ) ) {
                new AnimationEditDialog( path, null );
            }
            else {
                java.io.File file = null;
                String filename = null;
                String animationName = "anim" + ( new Random( ) ).nextInt( 1000 );
                if( path != null && !path.equals( "" ) ) {
                    String[] temp = path.split( "/" );
                    animationName = temp[temp.length - 1];
                    try {
                        file = File.createTempFile( animationName, ".eaa" );
                    }
                    catch( IOException e1 ) {
                        e1.printStackTrace();
                    }
                    filename = file.getAbsolutePath( );
                    //filename = AssetsController.TempFileGenerator.generateTempFileOverwriteExisting( animationName, "eaa" );
                }
                else {
                    animationName = JOptionPane.showInputDialog( Controller.getInstance( ).peekWindow( ), TC.get( "Animation.AskFilename" ), TC.get( "Animation.AskFilenameTitle" ), JOptionPane.QUESTION_MESSAGE );
                    if( animationName != null && animationName.length( ) > 0 ) {
                        //filename = AssetsController.TempFileGenerator.generateTempFileOverwriteExisting( animationName, "eaa" );
                        try {
                            file = File.createTempFile( animationName, ".eaa" );
                        }
                        catch( IOException e1 ) {
                            e1.printStackTrace();
                        }
                        filename = file.getAbsolutePath( );
                    }
                }
                if( file!=null&&filename != null ) {
                    //File file = new File( filename );
                    //file.create( );
                    AnimationWriter.writeAnimation( filename, new Animation( animationName, new EditorImageLoader()  ) );

                    Animation animation = new Animation( animationName, new EditorImageLoader()  );
                    if( path != null ) {
                        ResourcesDataControl.framesFromImages( animation, path );
                        AnimationWriter.writeAnimation( filename, animation );
                    }

                    String selectedAsset = ( new File( filename ) ).getName( );

                    pathTextField.setText( AssetsController.CATEGORY_ANIMATION_FOLDER + "/" + selectedAsset );
                    new AnimationEditDialog( AssetsController.CATEGORY_ANIMATION_FOLDER + "/" + selectedAsset, animation );
                }

            }
            viewButton.setEnabled( true );

        }
    }

}
