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
package es.eucm.eadventure.editor.gui.otherpanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.controllers.IrregularAreaEditionController;
import es.eucm.eadventure.editor.control.controllers.NormalScenePreviewEditionController;
import es.eucm.eadventure.editor.control.controllers.TrajectoryScenePreviewEditionController;
import es.eucm.eadventure.editor.control.controllers.scene.ActiveAreaDataControl;
import es.eucm.eadventure.editor.control.controllers.scene.ExitDataControl;
import es.eucm.eadventure.editor.control.controllers.scene.PointDataControl;
import es.eucm.eadventure.editor.control.controllers.scene.RectangleArea;
import es.eucm.eadventure.editor.gui.otherpanels.imageelements.ImageElement;

/**
 * A Panel for the edition of irregular active areas and exits
 * 
 * @author Eugenio Marchiori
 */
public class IrregularAreaEditionPanel extends JPanel {

    /**
     * Default generated serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * The preview and edition panel
     */
    private ScenePreviewEditionPanel spep;

    /**
     * The mouse controller for the panel
     */
    protected IrregularAreaEditionController iaec;

    protected JPanel buttonPanel;

    protected JButton turnIrregular;

    private boolean hasInfluenceArea;

    private Color color;

    /**
     * Default constructor, with the path to the background and the
     * trajectoryDataControl
     * 
     * @param scenePath
     *            path to the background image
     * @param trajectoryDataControl
     *            the trajectoryDataControl
     */
    public IrregularAreaEditionPanel( String scenePath, RectangleArea rectangleArea, boolean hasInfluenceArea, Color color ) {

        setLayout( new BorderLayout( ) );
        this.hasInfluenceArea = hasInfluenceArea;
        spep = new ScenePreviewEditionPanel( false, scenePath );
        this.color = color;

        buttonPanel = createButtonPanel( );

        add( spep, BorderLayout.CENTER );

        this.setRectangular( rectangleArea );
    }

    /**
     * Create the button panel
     * 
     * @return the new button panel
     */
    private JPanel createButtonPanel( ) {

        JPanel buttonPanel = new JPanel( );
        ButtonGroup group = new ButtonGroup( );
        buttonPanel.add( createToolButton( "BarriersList.EditNodes", IrregularAreaEditionController.POINT_EDIT, "img/icons/nodeEdit.png", group ) );
        buttonPanel.add( createToolButton( "BarriersList.DeleteTool", IrregularAreaEditionController.DELETE_TOOL, "img/icons/deleteTool.png", group ) );
        JButton turnRectangle = new JButton( TC.get( "SPEP.ConvertToRectangularArea" ) );
        turnRectangle.addActionListener( new ActionListener( ) {
            public void actionPerformed( ActionEvent arg0 ) {
                iaec.getEditionRectangle( ).setRectangular( true );
                setRectangular( iaec.getEditionRectangle( ) );
                IrregularAreaEditionPanel.this.repaint( );
            }
        } );
        JPanel tempPanel = new JPanel( );
        tempPanel.setLayout( new BorderLayout( ) );
        tempPanel.add( turnRectangle, BorderLayout.NORTH );
        tempPanel.add( buttonPanel, BorderLayout.CENTER );

        return tempPanel;
    }

    /**
     * Create a tool button
     * 
     * @param text
     *            The text of the button
     * @param tool
     *            The tool asosiated with the button
     * @param group
     * @return The new button
     */
    private AbstractButton createToolButton( String text, final int tool, String iconPath, ButtonGroup group ) {

        AbstractButton button;
        ImageIcon icon = new ImageIcon( iconPath );
        button = new JToggleButton( icon );
        group.add( button );
        button.setToolTipText( TC.get( text ) );
        button.setFocusable( false );
        if( tool == TrajectoryScenePreviewEditionController.NODE_EDIT ) {
            button.setSelected( true );
        }

        button.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent arg0 ) {

                iaec.setSelectedTool( tool );
            }
        } );
        return button;

    }

    /**
     * Returns the preview panel
     * 
     * @return the preview panel
     */
    public ScenePreviewEditionPanel getScenePreviewEditionPanel( ) {

        return spep;
    }

    public void setRectangular( RectangleArea rectangleArea ) {

        this.remove( buttonPanel );
        if( turnIrregular != null )
            this.remove( turnIrregular );
        if( rectangleArea == null || rectangleArea.isRectangular( ) ) {
            spep.changeController( new NormalScenePreviewEditionController( spep ) );
            spep.setShowTextEdition( true );
            spep.setMovableCategory( ScenePreviewEditionPanel.CATEGORY_ACTIVEAREA, rectangleArea instanceof ActiveAreaDataControl );
            spep.setMovableCategory( ScenePreviewEditionPanel.CATEGORY_EXIT, rectangleArea instanceof ExitDataControl );
            spep.setMovableCategory( ScenePreviewEditionPanel.CATEGORY_POINT, false );
            spep.removeElements( ScenePreviewEditionPanel.CATEGORY_POINT );
            spep.setIrregularRectangle( null, null );
            if( rectangleArea != null && rectangleArea instanceof ActiveAreaDataControl ) {
                spep.setSelectedElement( (ActiveAreaDataControl) rectangleArea );
                if( ( (ActiveAreaDataControl) rectangleArea ).getInfluenceArea( ) != null )
                    spep.addInfluenceArea( ( (ActiveAreaDataControl) rectangleArea ).getInfluenceArea( ) );
            }
            if( rectangleArea != null && rectangleArea instanceof ExitDataControl ) {
                spep.setSelectedElement( (ExitDataControl) rectangleArea );
                if( ( (ExitDataControl) rectangleArea ).getInfluenceArea( ) != null )
                    spep.addInfluenceArea( ( (ExitDataControl) rectangleArea ).getInfluenceArea( ) );
            }
            if( spep.getSelectedElement( ) != null && spep.getSelectedElement( ).getDataControl( ) != null && spep.getSelectedElement( ).getDataControl( ) instanceof RectangleArea ) {
                turnIrregular = new JButton( TC.get( "SPEP.ConvertToIrregularArea" ) );
                turnIrregular.addActionListener( new ActionListener( ) {
                    public void actionPerformed( ActionEvent e ) {
                        ( (RectangleArea) spep.getSelectedElement( ).getDataControl( ) ).setRectangular( false );
                        setRectangular( (RectangleArea) spep.getSelectedElement( ).getDataControl( ) );
                        IrregularAreaEditionPanel.this.repaint( );
                    }
                } );
                this.add( turnIrregular, BorderLayout.NORTH );
            }
        }
        else {
            this.add( buttonPanel, BorderLayout.NORTH );
            iaec = new IrregularAreaEditionController( spep, rectangleArea, color, hasInfluenceArea );
            spep.changeController( iaec );
            spep.setShowTextEdition( false );
            spep.removeElements( ScenePreviewEditionPanel.CATEGORY_POINT );
            for( Point point : rectangleArea.getPoints( ) )
                spep.addPoint( new PointDataControl( point ) );
            spep.setMovableCategory( ScenePreviewEditionPanel.CATEGORY_POINT, true );
            spep.setMovableCategory( ScenePreviewEditionPanel.CATEGORY_ACTIVEAREA, false );
            spep.setMovableCategory( ScenePreviewEditionPanel.CATEGORY_EXIT, false );
            spep.setSelectedElement( (ImageElement) null );
            if( rectangleArea != null && rectangleArea instanceof ActiveAreaDataControl ) {
                if( ( (ActiveAreaDataControl) rectangleArea ).getInfluenceArea( ) != null )
                    spep.addInfluenceArea( ( (ActiveAreaDataControl) rectangleArea ).getInfluenceArea( ) );
            }
            if( rectangleArea != null && rectangleArea instanceof ExitDataControl ) {
                if( ( (ExitDataControl) rectangleArea ).getInfluenceArea( ) != null )
                    spep.addInfluenceArea( ( (ExitDataControl) rectangleArea ).getInfluenceArea( ) );
            }
        }
        this.updateUI( );
    }
}
