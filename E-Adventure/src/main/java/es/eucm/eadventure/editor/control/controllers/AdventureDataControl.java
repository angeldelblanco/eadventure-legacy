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
package es.eucm.eadventure.editor.control.controllers;

import java.util.List;

import es.eucm.eadventure.common.auxiliar.ReportDialog;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.adventure.CustomArrow;
import es.eucm.eadventure.common.data.adventure.CustomButton;
import es.eucm.eadventure.common.data.adventure.CustomCursor;
import es.eucm.eadventure.common.data.adventure.DescriptorData;
import es.eucm.eadventure.common.data.adventure.DescriptorData.DefaultClickAction;
import es.eucm.eadventure.common.data.adventure.DescriptorData.DragBehaviour;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.metadata.ims.IMSDataControl;
import es.eucm.eadventure.editor.control.controllers.metadata.lom.LOMDataControl;
import es.eucm.eadventure.editor.control.controllers.metadata.lomes.LOMESDataControl;
import es.eucm.eadventure.editor.control.tools.Tool;
import es.eucm.eadventure.editor.control.tools.general.assets.DeleteArrowTool;
import es.eucm.eadventure.editor.control.tools.general.assets.DeleteButtonTool;
import es.eucm.eadventure.editor.control.tools.general.assets.DeleteCursorTool;
import es.eucm.eadventure.editor.control.tools.general.assets.SelectArrowTool;
import es.eucm.eadventure.editor.control.tools.general.assets.SelectButtonTool;
import es.eucm.eadventure.editor.control.tools.general.assets.SelectCursorPathTool;
import es.eucm.eadventure.editor.control.tools.general.commontext.ChangeDescriptionTool;
import es.eucm.eadventure.editor.control.tools.general.commontext.ChangeTitleTool;
import es.eucm.eadventure.editor.control.tools.generic.ChangeBooleanValueTool;
import es.eucm.eadventure.editor.control.tools.generic.ChangeIntegerValueTool;
import es.eucm.eadventure.editor.gui.editdialogs.GUIStylesDialog;

/**
 * This class holds all the information of the adventure, including the chapters
 * and the configuration of the HUD.
 * 
 * @author Bruno Torijano Bueno
 */
public class AdventureDataControl {

    /**
     * The whole data of the adventure
     */
    private AdventureData adventureData;

    /**
     * Controller for LOM data (only required when exporting games to LOM)
     */
    private LOMDataControl lomController;

    /**
     * Controller for IMS data (only required when exporting games to SCORM)
     */
    private IMSDataControl imsController;

    /**
     * Controller for LOM-ES data (require to export games as ODE)
     */
    private LOMESDataControl lomesController;

    /**
     * Assessment file data controller
     */
    //private AssessmentProfilesDataControl assessmentProfilesDataControl;
    /**
     * Adaptation file data controller
     */
    //private AdaptationProfilesDataControl adaptationProfilesDataControl;
    /**
     * Constructs the data control with the adventureData
     */
    public AdventureDataControl( AdventureData data ) {

        this( );
        adventureData = data;
        checkContextualButtons();
    }

    /**
     * Empty constructor. Sets all values to null.
     */
    public AdventureDataControl( ) {

        adventureData = new AdventureData( );
        lomController = new LOMDataControl( );
        imsController = new IMSDataControl( );
        lomesController = new LOMESDataControl( );
    }

    /**
     * Constructor which creates an adventure data with default title and
     * description, traditional GUI and one empty chapter (with a scene).
     * 
     * @param adventureTitle
     *            Default title for the adventure
     * @param chapterTitle
     *            Default title for the chapter
     * @param sceneId
     *            Default identifier for the scene
     */
    public AdventureDataControl( String adventureTitle, String chapterTitle, String sceneId, int playerMode ) {

        adventureData = new AdventureData( );
        adventureData.setTitle( adventureTitle );
        adventureData.setDescription( "" );
        adventureData.setGUIType( DescriptorData.GUI_CONTEXTUAL );
        adventureData.setPlayerMode( playerMode );
        adventureData.addChapter( new Chapter( chapterTitle, sceneId ) );
        lomController = new LOMDataControl( );
        imsController = new IMSDataControl( );
        lomesController = new LOMESDataControl( );
    }

    public AdventureDataControl( String adventureTitle, String chapterTitle, String sceneId ) {

        this( adventureTitle, chapterTitle, sceneId, DescriptorData.MODE_PLAYER_3RDPERSON );
    }

    /**
     * Constructor gith given parameters.
     * 
     * @param title
     *            Title of the adventure
     * @param description
     *            Description of the adventure
     * @param guiType
     *            Type of the GUI
     * @param chapters
     *            Chapters of the adventure
     */
    public AdventureDataControl( String title, String description, List<Chapter> chapters ) {

        adventureData = new AdventureData( );
        adventureData.setTitle( title );
        adventureData.setDescription( description );
        adventureData.setGUIType( DescriptorData.GUI_TRADITIONAL );
        adventureData.setChapters( chapters );
        adventureData.setGraphicConfig( DescriptorData.GRAPHICS_WINDOWED );
        adventureData.setPlayerMode( DescriptorData.MODE_PLAYER_3RDPERSON );

        lomController = new LOMDataControl( );
        imsController = new IMSDataControl( );
    }

    public boolean isCursorTypeAllowed( String type ) {

        return isCursorTypeAllowed( DescriptorData.getCursorTypeIndex( type ) );
    }

    public boolean isCursorTypeAllowed( int type ) {

        return DescriptorData.typeAllowed[adventureData.getGUIType( )][type];
    }

    /**
     * Returns the title of the adventure
     * 
     * @return Adventure's title
     */
    public String getTitle( ) {

        return adventureData.getTitle( );
    }

    /**
     * Returns the description of the adventure.
     * 
     * @return Adventure's description
     */
    public String getDescription( ) {

        return adventureData.getDescription( );
    }

    /**
     * Returns the GUI type of the adventure.
     * 
     * @return Adventure's GUI type
     */
    public Integer getGUIType( ) {

        return adventureData.getGUIType( );
    }

    /**
     * Returns the list of chapters of the adventure.
     * 
     * @return Adventure's chapters list
     */
    public List<Chapter> getChapters( ) {

        return adventureData.getChapters( );
    }
    
    /**
     * Returns settings for keyboard navigation
     * @return true if keyboard navigation (using arrows) is enabled, false if it is disabled (default behaviour)
     */
    public boolean isKeyboardNavigationEnabled(){
        return adventureData.isKeyboardNavigationEnabled( );
    }
    
    /**
     * Enables/Disables keyboard navigation - by default is set to false
     * @param enabled
     */
    public boolean setKeyboardNavigation ( boolean enabled ){
        return Controller.getInstance( ).addTool( new ChangeBooleanValueTool(adventureData, enabled, "isKeyboardNavigationEnabled", "setKeyboardNavigation") );
    }

    /**
     * Sets the title of the adventure.
     * 
     * @param title
     *            New title for the adventure
     */
    public void setTitle( String title ) {

        Tool tool = new ChangeTitleTool( adventureData, title );
        Controller.getInstance( ).addTool( tool );
    }

    /**
     * Sets the description of the adventure.
     * 
     * @param description
     *            New description for the adventure
     */
    public void setDescription( String description ) {

        Tool tool = new ChangeDescriptionTool( adventureData, description );
        Controller.getInstance( ).addTool( tool );
    }

    /**
     * Shows the GUI style selection dialog.
     */
    public void showGUIStylesDialog( ) {

        // Show the dialog
        new GUIStylesDialog( adventureData.getGUIType( ) );
    }

    /**
     * @return the playerMode
     */
    public int getPlayerMode( ) {

        return adventureData.getPlayerMode( );
    }

    /**
     * @param playerMode
     *            the playerMode to set
     */
    public void setPlayerMode( int playerMode ) {

        Tool tool = new ChangeIntegerValueTool( adventureData, playerMode, "getPlayerMode", "setPlayerMode" );
        Controller.getInstance( ).addTool( tool );
    }

    public List<CustomCursor> getCursors( ) {

        return adventureData.getCursors( );
    }

    public List<CustomButton> getButtons( ) {

        return adventureData.getButtons( );
    }

    public List<CustomArrow> getArrows( ) {

        return adventureData.getArrows( );
    }

    public String getCursorPath( String type ) {

        for( CustomCursor cursor : adventureData.getCursors( ) ) {
            if( cursor.getType( ).equals( type ) ) {
                return cursor.getPath( );
            }
        }
        return null;
    }

    public String getCursorPath( int type ) {

        return getCursorPath( DescriptorData.getCursorTypeString( type ) );
    }

    public void deleteCursor( int type ) {

        String typeS = DescriptorData.getCursorTypeString( type );
        int position = -1;
        for( int i = 0; i < adventureData.getCursors( ).size( ); i++ ) {
            if( adventureData.getCursors( ).get( i ).getType( ).equals( typeS ) ) {
                position = i;
                break;
            }
        }
        if( position >= 0 ) {
            Controller.getInstance( ).addTool( new DeleteCursorTool( adventureData, position ) );
        }
    }

    public void editCursorPath( int t ) {

        try {
            Controller.getInstance( ).addTool( new SelectCursorPathTool( adventureData, t ) );
        }
        catch( CloneNotSupportedException e ) {
            ReportDialog.GenerateErrorReport( e, false, "Could not clone cursor-adventureData" );
        }
    }

    public String getArrowPath( String type ) {

        for( CustomArrow arrow : adventureData.getArrows( ) ) {
            if( arrow.getType( ).equals( type ) ) {
                return arrow.getPath( );
            }
        }
        return null;
    }
    
    public void deleteArrow( String type ) {

        int position = -1;
        for( int i = 0; i < adventureData.getArrows( ).size( ); i++ ) {
            if( adventureData.getArrows( ).get( i ).getType( ).equals( type ) ) {
                position = i;
                break;
            }
        }
        if( position >= 0 ) {
            Controller.getInstance( ).addTool( new DeleteArrowTool( adventureData, position ) );
        }
    }

    /**
     * @return the lomController
     */
    public LOMDataControl getLomController( ) {

        return lomController;
    }

    /**
     * @param lomController
     *            the lomController to set
     */
    public void setLomController( LOMDataControl lomController ) {

        this.lomController = lomController;
    }

    /**
     * @return the imsController
     */
    public IMSDataControl getImsController( ) {

        return imsController;
    }

    /**
     * @return the lomesController
     */
    public LOMESDataControl getLOMESController( ) {

        return lomesController;
    }

    /**
     * @param imsController
     *            the imsController to set
     */
    public void setImsController( IMSDataControl imsController ) {

        this.imsController = imsController;
    }

    /**
     * @return the assessmentRulesListDataControl
     */
    /*public AssessmentProfilesDataControl getAssessmentRulesListDataControl( ) {
    	return assessmentProfilesDataControl;
    }*/

    /**
     * @return the adaptationRulesListDataControl
     */
    /*public AdaptationProfilesDataControl getAdaptationRulesListDataControl( ) {
    	return adaptationProfilesDataControl;
    }*/

    public boolean isCommentaries( ) {

        return adventureData.isCommentaries( );
    }

    public void setCommentaries( boolean commentaries ) {

        Tool tool = new ChangeBooleanValueTool( adventureData, commentaries, "isCommentaries", "setCommentaries" );
        Controller.getInstance( ).addTool( tool );
    }
    
    public boolean isKeepShowing( ) {

        return adventureData.isKeepShowing( );
    }
    
    public DescriptorData.DefaultClickAction getDefaultClickAction() {
        return adventureData.getDefaultClickAction( );
    }
    
    public DescriptorData.Perspective getPerspective() {
        return adventureData.getPerspective( );
    }

    public void setKeepShowing( boolean keepShowing ) {

        Tool tool = new ChangeBooleanValueTool( adventureData, keepShowing, "isKeepShowing", "setKeepShowing" );
        Controller.getInstance( ).addTool( tool );
    }

    public int getGraphicConfig( ) {

        return adventureData.getGraphicConfig( );
    }

    public void setGraphicConfig( int graphicConfig ) {

        Tool tool = new ChangeIntegerValueTool( adventureData, graphicConfig, "getGraphicConfig", "setGraphicConfig" );
        Controller.getInstance( ).addTool( tool );
    }

    /**
     * @return the adventureData
     */
    public AdventureData getAdventureData( ) {

        return adventureData;
    }

    public String getButtonPath( String action, String type ) {

        for( CustomButton cb : adventureData.getButtons( ) ) {
            if( cb.getType().equals( type ) &&  cb.getAction( ).equals( action ))
                return cb.getPath( );
        }
        return null;
    }
    
    public void deleteButton( String action, String type ) {

        Controller.getInstance( ).addTool( new DeleteButtonTool( adventureData, action, type ) );
    }

    public void editButtonPath( String action, String type ) {

        try {
            Controller.getInstance( ).addTool( new SelectButtonTool( adventureData, action, type ) );
        }
        catch( CloneNotSupportedException e ) {
            ReportDialog.GenerateErrorReport( e, false, "Could not clone resources: buttons" );
        }
    }

    public void editArrowPath( String type ) {

        try {
            Controller.getInstance( ).addTool( new SelectArrowTool( adventureData, type ) );
        }
        catch( CloneNotSupportedException e ) {
            ReportDialog.GenerateErrorReport( e, false, "Could not clone resources: arrows" );
        }
    }

    public int getInventoryPosition( ) {

        return adventureData.getInventoryPosition( );
    }

    public void setInventoryPosition( int inventoryPosition ) {

        Controller.getInstance( ).addTool( new ChangeIntegerValueTool( adventureData, inventoryPosition, "getInventoryPosition", "setInventoryPosition" ) );
    }

    public int countAssetReferences( String assetPath ) {

        return adventureData.countAssetReferences( assetPath );
    }

    public void getAssetReferences( List<String> assetPaths, List<Integer> assetTypes ) {

        adventureData.getAssetReferences( assetPaths, assetTypes );
    }

    /**
     * Deletes a given asset from the script, removing all occurrences.
     * 
     * @param assetPath
     *            Path of the asset (relative to the ZIP), without suffix in
     *            case of an animation or set of slides
     */
    public void deleteAssetReferences( String assetPath ) {

        adventureData.deleteAssetReferences( assetPath );
    }

    public void setGUIStyleDialog( int optionSelected ) {

        Tool tool = new ChangeIntegerValueTool( adventureData, optionSelected, "getGUIType", "setGUIType" );
        Controller.getInstance( ).addTool( tool );
        
    }

    public void setDefaultClickAction( DefaultClickAction defaultClickAction ) {

        adventureData.setDeafultClickAction( defaultClickAction );
        
    }
    
    public void setPerspective( DescriptorData.Perspective perspective ) {
        adventureData.setPerspective(  perspective );
    }
    
    public void setDragBehaviour( DescriptorData.DragBehaviour dragBehaviour ) {
        adventureData.setDragBehaviour( dragBehaviour );
    }

    public DragBehaviour getDragBehaviour( ) {
        return adventureData.getDragBehaviour( );
    }

    /**
     * Checks if the user-grab button was defined, which is deprecated. In this case, buttons "use", "use-with", "grab", and "give-to" are set
     * using the same configuration.
     * 
     * This method should be invoked when the AdventureDataControl is set with a given adventureData
     * 
     * This method was added in version 1.4
     */
    private void checkContextualButtons(){
        checkContextualButtons(DescriptorData.USE_BUTTON);
        checkContextualButtons(DescriptorData.GRAB_BUTTON);
        checkContextualButtons(DescriptorData.GIVETO_BUTTON);
        checkContextualButtons(DescriptorData.USEWITH_BUTTON);
        String useGrabPath=adventureData.getButtonPathFromEditor( DescriptorData.USE_GRAB_BUTTON, DescriptorData.NORMAL_BUTTON );
        if (useGrabPath!=null && !useGrabPath.equals( "" )){
            for (CustomButton button:adventureData.getButtons( )){
                if (button.getAction( ).equals( DescriptorData.USE_GRAB_BUTTON ) &&
                        button.getType( ).equals( DescriptorData.NORMAL_BUTTON )){
                    adventureData.getButtons( ).remove( button );
                    break;
                }
            }
        }
        
        useGrabPath=adventureData.getButtonPathFromEditor( DescriptorData.USE_GRAB_BUTTON, DescriptorData.HIGHLIGHTED_BUTTON );
        if (useGrabPath!=null && !useGrabPath.equals( "" )){
            for (CustomButton button:adventureData.getButtons( )){
                if (button.getAction( ).equals( DescriptorData.USE_GRAB_BUTTON ) &&
                        button.getType( ).equals( DescriptorData.HIGHLIGHTED_BUTTON )){
                    adventureData.getButtons( ).remove( button );
                    break;
                }
            }
        }

    }
    
    private void checkContextualButtons(String action){
        checkContextualButtons(action, DescriptorData.NORMAL_BUTTON);
        checkContextualButtons(action, DescriptorData.HIGHLIGHTED_BUTTON);
    }
    
    private void checkContextualButtons(String action, String type){
        String useGrabPath=adventureData.getButtonPathFromEditor( DescriptorData.USE_GRAB_BUTTON, type );
        if (useGrabPath!=null && !useGrabPath.equals( "" )){
            if (adventureData.getButtonPathFromEditor( action, type )==null || adventureData.getButtonPathFromEditor( action, type ).equals( "" )){
                adventureData.addButton( action, type, useGrabPath );
            }
        }
    }
    
}
