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
package es.eucm.eadventure.editor.control.controllers.scene;

import java.util.HashMap;
import java.util.List;

import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.ConditionsController;
import es.eucm.eadventure.editor.control.controllers.DataControl;
import es.eucm.eadventure.editor.control.controllers.Searchable;
import es.eucm.eadventure.editor.control.controllers.ConditionsController.ConditionContextProperty;
import es.eucm.eadventure.editor.control.controllers.ConditionsController.ConditionOwner;
import es.eucm.eadventure.editor.control.controllers.atrezzo.AtrezzoDataControl;
import es.eucm.eadventure.editor.control.controllers.atrezzo.AtrezzoListDataControl;
import es.eucm.eadventure.editor.control.controllers.character.NPCDataControl;
import es.eucm.eadventure.editor.control.controllers.character.NPCsListDataControl;
import es.eucm.eadventure.editor.control.controllers.item.ItemDataControl;
import es.eucm.eadventure.editor.control.controllers.item.ItemsListDataControl;
import es.eucm.eadventure.editor.control.tools.general.ChangeElementReferenceTool;
import es.eucm.eadventure.editor.control.tools.general.commontext.ChangeDocumentationTool;
import es.eucm.eadventure.editor.control.tools.general.commontext.ChangeTargetIdTool;
import es.eucm.eadventure.editor.data.support.VarFlagSummary;

public class ElementReferenceDataControl extends DataControl {

    /**
     * Scene controller that contains this element reference (used to extract
     * the id of the scene).
     */
    private SceneDataControl sceneDataControl;

    /**
     * Contained element reference.
     */
    private ElementReference elementReference;

    private InfluenceAreaDataControl influenceAreaDataControl;

    /**
     * Conditions controller.
     */
    private ConditionsController conditionsController;

    /**
     * The type of the element reference (item, npc or atrezzo)
     */
    private int type;

    private boolean visible;

    /**
     * Contructor.
     * 
     * @param sceneDataControl
     *            Parent scene controller
     * @param elementReference
     *            Element reference of the data control structure
     */
    public ElementReferenceDataControl( SceneDataControl sceneDataControl, ElementReference elementReference, int type, int referenceNumber ) {

        this.sceneDataControl = sceneDataControl;
        this.elementReference = elementReference;
        this.type = type;
        this.visible = true;
        if( type == Controller.ITEM_REFERENCE || type == Controller.NPC_REFERENCE )
            this.influenceAreaDataControl = new InfluenceAreaDataControl( sceneDataControl, elementReference.getInfluenceArea( ), this );

        // Create subcontrollers
        HashMap<String, ConditionContextProperty> context1 = new HashMap<String, ConditionContextProperty>( );
        ConditionOwner parent = new ConditionOwner( Controller.SCENE, sceneDataControl.getId( ) );
        ConditionOwner owner = new ConditionOwner( type, elementReference.getTargetId( ), parent );
        context1.put( ConditionsController.CONDITION_OWNER, owner );

        conditionsController = new ConditionsController( elementReference.getConditions( ), context1 );
    }

    /**
     * Returns the conditions of the element reference.
     * 
     * @return Conditions of the element reference
     */
    public ConditionsController getConditions( ) {

        return conditionsController;
    }

    /**
     * Returns the id of the scene that contains this element reference.
     * 
     * @return Parent scene id
     */
    public String getParentSceneId( ) {

        return sceneDataControl.getId( );
    }

    /**
     * Returns the data controllers of the item references of the scene that
     * contains this element reference.
     * 
     * @return List of item references (including the one being edited)
     */
    public List<ElementReferenceDataControl> getParentSceneItemReferences( ) {

        return sceneDataControl.getReferencesList( ).getItemReferences( );
    }

    /**
     * Returns the data controllers of the character references of the scene
     * that contains this element reference.
     * 
     * @return List of character references (including the one being edited)
     */
    public List<ElementReferenceDataControl> getParentSceneNPCReferences( ) {

        return sceneDataControl.getReferencesList( ).getNPCReferences( );
    }

    /**
     * Returns the data controllers of the atrezzo items references of the scene
     * that contains this element reference.
     * 
     * @return List of atrezzo references (including the one being edited)
     */
    public List<ElementReferenceDataControl> getParentSceneAtrezzoReferences( ) {

        return sceneDataControl.getReferencesList( ).getAtrezzoReferences( );
    }

    public List<ExitDataControl> getParentSceneExitList( ) {

        return sceneDataControl.getExitsList( ).getExits( );
    }

    public List<ActiveAreaDataControl> getParentSceneActiveAreaList( ) {

        return sceneDataControl.getActiveAreasList( ).getActiveAreas( );
    }

    public List<BarrierDataControl> getParentSceneBarrierList( ) {

        return sceneDataControl.getBarriersList( ).getBarriers( );
    }

    /**
     * Returns the id of the referenced element.
     * 
     * @return Id of the referenced element
     */
    public String getElementId( ) {

        return elementReference.getTargetId( );
    }

    /**
     * Returns the x coordinate of the referenced element
     * 
     * @return X coordinate of the referenced element
     */
    public int getElementX( ) {

        return elementReference.getX( );
    }

    /**
     * Returns the y coordinate of the referenced element
     * 
     * @return Y coordinate of the referenced element
     */
    public int getElementY( ) {

        return elementReference.getY( );
    }

    /**
     * Returns the documentation of the element reference.
     * 
     * @return Element reference's documentation
     */
    public String getDocumentation( ) {

        return elementReference.getDocumentation( );
    }

    /**
     * Sets a new next scene id.
     * 
     * @param elementId
     *            New next scene id
     */
    public void setElementId( String elementId ) {

        // If the value is different
        controller.addTool( new ChangeTargetIdTool( elementReference, elementId ) );
        //if( !elementId.equals( elementReference.getTargetId( ) ) ) {
        // Set the new element id, update the tree and modify the data
        //	elementReference.setTargetId( elementId );
        //	controller.updateTree( );
        //	controller.dataModified( );
        //}
    }

    /**
     * Sets the new position for the element reference.
     * 
     * @param x
     *            X coordinate for the element reference
     * @param y
     *            Y coordinate for the element reference
     */
    public void setElementPosition( int x, int y ) {

        controller.addTool( new ChangeElementReferenceTool( elementReference, x, y ) );
    }

    /**
     * Sets the new documentation of the element reference.
     * 
     * @param documentation
     *            Documentation of the element reference
     */
    public void setDocumentation( String documentation ) {

        controller.addTool( new ChangeDocumentationTool( elementReference, documentation ) );
    }

    /**
     * Get the scale for the element reference
     * 
     * @return the scale for the element reference
     */
    public float getElementScale( ) {

        return elementReference.getScale( );
    }

    /**
     * Set the scale for the element reference
     * 
     * @param scale
     *            the scale for the element reference
     */
    public void setElementScale( float scale ) {

        controller.addTool( new ChangeElementReferenceTool( elementReference, scale ) );
    }

    @Override
    public Object getContent( ) {

        return elementReference;
    }

    @Override
    public int[] getAddableElements( ) {

        return new int[] {};
    }

    @Override
    public boolean canAddElement( int type ) {

        return false;
    }

    @Override
    public boolean canBeDeleted( ) {

        return true;
    }

    @Override
    public boolean canBeMoved( ) {

        return true;
    }

    @Override
    public boolean canBeRenamed( ) {

        return false;
    }

    @Override
    public boolean addElement( int type, String id ) {

        return false;
    }

    @Override
    public boolean deleteElement( DataControl dataControl, boolean askConfirmation ) {

        return false;
    }

    @Override
    public boolean moveElementUp( DataControl dataControl ) {

        return false;
    }

    @Override
    public boolean moveElementDown( DataControl dataControl ) {

        return false;
    }

    @Override
    public String renameElement( String name ) {

        return null;
    }

    @Override
    public void updateVarFlagSummary( VarFlagSummary varFlagSummary ) {

        // Update the flag summary with the conditions
        ConditionsController.updateVarFlagSummary( varFlagSummary, elementReference.getConditions( ) );
    }

    @Override
    public boolean isValid( String currentPath, List<String> incidences ) {

        return true;
    }

    @Override
    public int countAssetReferences( String assetPath ) {

        return 0;
    }

    @Override
    public void deleteAssetReferences( String assetPath ) {

        // Do nothing
    }

    @Override
    public int countIdentifierReferences( String id ) {

        int count = 0;
        count += elementReference.getTargetId( ).equals( id ) ? 1 : 0;
        count += conditionsController.countIdentifierReferences( id );
        return count;
    }

    @Override
    public void replaceIdentifierReferences( String oldId, String newId ) {

        if( elementReference.getTargetId( ).equals( oldId ) )
            elementReference.setTargetId( newId );
        conditionsController.replaceIdentifierReferences( oldId, newId );
    }

    @Override
    public void deleteIdentifierReferences( String id ) {

        conditionsController.deleteIdentifierReferences( id );
    }

    @Override
    public void getAssetReferences( List<String> assetPaths, List<Integer> assetTypes ) {

        // Do nothing
    }

    @Override
    public boolean canBeDuplicated( ) {

        return false;
    }

    /**
     * Return the element reference
     * 
     * @return the element reference
     */
    public ElementReference getElementReference( ) {

        return elementReference;
    }

    /**
     * 
     * @return The type of the current element reference
     */
    public int getType( ) {

        return type;
    }

    public SceneDataControl getSceneDataControl( ) {

        return sceneDataControl;
    }

    public InfluenceAreaDataControl getInfluenceArea( ) {

        return influenceAreaDataControl;
    }

    public DataControl getReferencedElementDataControl( ) {

        switch( type ) {
            case Controller.ATREZZO_REFERENCE:
                AtrezzoListDataControl aldc = Controller.getInstance( ).getSelectedChapterDataControl( ).getAtrezzoList( );
                for( AtrezzoDataControl adc : aldc.getAtrezzoList( ) ) {
                    if( adc.getId( ).equals( this.getElementId( ) ) ) {
                        return adc;
                    }
                }
                break;
            case Controller.NPC_REFERENCE:
                NPCsListDataControl nldc = Controller.getInstance( ).getSelectedChapterDataControl( ).getNPCsList( );
                for( NPCDataControl ndc : nldc.getNPCs( ) ) {
                    if( ndc.getId( ).equals( this.getElementId( ) ) ) {
                        return ndc;
                    }
                }
                break;
            case Controller.ITEM_REFERENCE:
                ItemsListDataControl ildc = Controller.getInstance( ).getSelectedChapterDataControl( ).getItemsList( );
                for( ItemDataControl idc : ildc.getItems( ) ) {
                    if( idc.getId( ).equals( this.getElementId( ) ) ) {
                        return idc;
                    }
                }
                break;
            default:
        }
        return null;

    }

    @Override
    public void recursiveSearch( ) {

        check( this.conditionsController, TC.get( "Search.Conditions" ) );
        //check( this.getDocumentation( ), TC.get( "Search.Documentation" ) );
        check( this.getElementId( ), TC.get( "Search.ElementID" ) );
    }

    public boolean isVisible( ) {

        return visible;
    }

    public void setVisible( boolean visible ) {

        this.visible = visible;
    }

    @Override
    public List<Searchable> getPathToDataControl( Searchable dataControl ) {

        return null;
    }

}
