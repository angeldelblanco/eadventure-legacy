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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.config.SceneLinksConfigData;
import es.eucm.eadventure.editor.control.controllers.SceneLinksController;
import es.eucm.eadventure.editor.control.controllers.scene.SceneDataControl;
import es.eucm.eadventure.editor.control.controllers.scene.ScenesListDataControl;
import es.eucm.eadventure.editor.gui.elementpanels.general.tables.EditDataControlCellRendererEditor;
import es.eucm.eadventure.editor.gui.elementpanels.general.tables.InfoHeaderRenderer;
import es.eucm.eadventure.editor.gui.otherpanels.scenelistelements.ActiveAreaElement;
import es.eucm.eadventure.editor.gui.otherpanels.scenelistelements.ExitElement;
import es.eucm.eadventure.editor.gui.otherpanels.scenelistelements.ItemReferenceElement;
import es.eucm.eadventure.editor.gui.otherpanels.scenelistelements.SceneElement;

/**
 * A panel with all the scenes in a chapter with the relations of their exits
 * between one another.
 * 
 * @author Eugenio Marchiori
 * 
 */
public class SceneLinksPanel extends JPanel {

    /**
     * Default generated serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * The panel where everything is drawn
     */
    private DrawPanel drawPanel;

    /**
     * The data control of the list of scenes
     */
    private ScenesListDataControl sldc;

    /**
     * List of sceneElements
     */
    private List<SceneElement> sceneElements;

    /**
     * The scale with which to draw the scenes in the panel
     */
    private float drawingScale = 0.8f;

    /**
     * The list of checkboxes
     */
    private JTable checkBoxes;

    /**
     * The table model for the list of checkboxes
     */
    private DefaultTableModel dtm;

    public static int DEFAULT_SCALE = 5;

    public SceneLinksPanel( ScenesListDataControl sceneListDataControl ) {

        this.sldc = sceneListDataControl;
        setLayout( new GridBagLayout( ) );
        drawPanel = new DrawPanel( true );
        BufferedImage background = new BufferedImage( 800, 600, BufferedImage.TYPE_4BYTE_ABGR );
        Graphics2D g = (Graphics2D) background.getGraphics( );

        g.setColor( ( new JPanel( ) ).getBackground( ) );
        g.fillRect( 0, 0, 800, 600 );
        g.setColor( Color.BLACK );
        g.drawRect( 0, 0, 799, 599 );

        AlphaComposite alphaComposite = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.3f );
        g.setComposite( alphaComposite );
        g.fillRect( 0, 0, 800, 600 );

        JScrollPane sp = createCheckBoxTable( );

        boolean isConfig = true;
        for( SceneDataControl scene : sldc.getScenes( ) ) {
            if( !SceneLinksConfigData.isSceneConfig( scene.getId( ) ) ) {
                isConfig = false;
            }
        }

        sceneElements = new ArrayList<SceneElement>( );
        int i = 0;
        for( SceneDataControl scene : sldc.getScenes( ) ) {
            SceneElement element = new SceneElement( scene, i );
            i++;
            if( isConfig ) {
                element.setVisible( SceneLinksConfigData.getSceneVisible( element.getId( ) ) );
                element.changePosition( SceneLinksConfigData.getSceneX( element.getId( ) ), SceneLinksConfigData.getSceneY( element.getId( ) ) );
            }
            else {
                SceneLinksConfigData.setSceneVisible( element.getId( ), element.isVisible( ) );
                SceneLinksConfigData.setSceneX( element.getId( ), element.getPosX( ) );
                SceneLinksConfigData.setSceneY( element.getId( ), element.getPosY( ) );
            }

            sceneElements.add( element );
            dtm.addRow( new Object[] { new Boolean( element.isVisible( ) ), element.getId( ), element.getDataControl( ) } );
        }

        SceneLinksController controller = new SceneLinksController( this );
        drawPanel.addMouseListener( controller );
        drawPanel.addMouseMotionListener( controller );

        GridBagConstraints c = new GridBagConstraints( );
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 2.0;
        c.weighty = 2.0;
        drawPanel.setBackgroundImg( background );
        add( drawPanel, c );

        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.HORIZONTAL;
        sp.setMinimumSize( new Dimension( 0, 150 ) );
        sp.setPreferredSize( new Dimension( 100, 150 ) );
        add( sp, c );

    }

    /**
     * Creates the checkbox table and all the necessary elements. Returns a
     * scrollpane with the table.
     * 
     * @return A scroll pane with the checkbox table
     */
    private JScrollPane createCheckBoxTable( ) {

        String colNames[] = { TC.get( "SceneLinksPanel.Show" ), TC.get( "SceneLinksPanel.SceneID" ), TC.get( "GeneralText.Edit" ) };
        Object[][] data = null;

        dtm = new DefaultTableModel( data, colNames ) {

            private static final long serialVersionUID = 1946349590475022293L;

            @Override
            public boolean isCellEditable( int row, int column ) {

                return row == checkBoxes.getSelectedRow( ) && column != 1;
            }

        };
        checkBoxes = new JTable( dtm );
        TableColumn tc = checkBoxes.getColumnModel( ).getColumn( 0 );
        tc.setHeaderRenderer( new InfoHeaderRenderer( "scenes/Scenes_Show.html" ) );
        tc.setCellEditor( checkBoxes.getDefaultEditor( Boolean.class ) );
        tc.setCellRenderer( checkBoxes.getDefaultRenderer( Boolean.class ) );
        tc.setMaxWidth( 90 );
        tc.setMinWidth( 90 );
        checkBoxes.getColumnModel( ).getColumn( 1 ).setHeaderRenderer( new InfoHeaderRenderer( ) );
        tc = checkBoxes.getColumnModel( ).getColumn( 2 );
        tc.setHeaderRenderer( new InfoHeaderRenderer( "scenes/Scenes_Edit.html" ) );
        tc.setCellEditor( new EditDataControlCellRendererEditor( ) );
        tc.setCellRenderer( new EditDataControlCellRendererEditor( ) );
        tc.setMaxWidth( 100 );
        dtm.addTableModelListener( new TableModelListener( ) {

            public void tableChanged( TableModelEvent tme ) {

                if( tme.getType( ) == TableModelEvent.UPDATE ) {
                    int row = tme.getFirstRow( );
                    int col = tme.getColumn( );
                    if( col == 0 ) {
                        sceneElements.get( row ).setVisible( ( (Boolean) dtm.getValueAt( row, col ) ).booleanValue( ) );
                        SceneLinksPanel.this.repaint( );
                    }
                }
            }
        } );

        return new JScrollPane( checkBoxes, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
    }

    @Override
    public void repaint( ) {

        super.repaint( );
    }

    @Override
    public void paint( Graphics g ) {

        paintGraph( );
        super.paint( g );
    }

    private void paintGraph( ) {

        drawPanel.paintBackground( );

        int number = sceneElements.size( );
        for( SceneElement scene : sceneElements ) {
            if( !scene.isVisible( ) )
                number--;
        }
        drawingScale = (float) ( 0.3f * ( 1 - Math.log( number ) / Math.log( 100 ) ) );
        List<Line> lines = new ArrayList<Line>( );

        for( SceneElement scene : sceneElements ) {
            if( scene.isVisible( ) ) {
                drawPanel.paintRelativeImageTop( scene.getImage( ), scene.getPosX( ), scene.getPosY( ), drawingScale * DEFAULT_SCALE );
                if( scene.isShowName( ) )
                    drawPanel.drawRelativeString( scene.getId( ), scene.getPosX( ), scene.getPosY( ) );
                for( ExitElement exit : scene.getExitElements( ) ) {
                    int x1 = (int) ( scene.getPosX( ) + exit.getPosX( ) * drawingScale );
                    int y1 = (int) ( scene.getPosY( ) + exit.getPosY( ) * drawingScale );
                    BasicStroke basicStroke = new BasicStroke( );
                    String id = exit.getNextSceneId( );
                    SceneElement temp = this.getSceneElementForId( id );
                    if( temp != null && temp.isVisible( ) ) {
                        addLine( x1, y1, temp, scene.getColor( ), basicStroke, lines );
                    }
                }
                for( ActiveAreaElement aae : scene.getActiveAreaElements( ) ) {
                    int x1 = (int) ( scene.getPosX( ) + aae.getPosX( ) * drawingScale );
                    int y1 = (int) ( scene.getPosY( ) + aae.getPosY( ) * drawingScale );
                    BasicStroke basicStroke = new BasicStroke( 1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0 );
                    for( String id : aae.getSceneIds( ) ) {
                        SceneElement temp = this.getSceneElementForId( id );
                        if( temp != null && temp.isVisible( ) ) {
                            addLine( x1, y1, temp, scene.getColor( ), basicStroke, lines );
                        }
                    }
                }
                for( ItemReferenceElement ire : scene.getItemReferenceElements( ) ) {
                    int x1 = (int) ( scene.getPosX( ) + ire.getPosX( ) * drawingScale );
                    int y1 = (int) ( scene.getPosY( ) + ire.getPosY( ) * drawingScale );
                    BasicStroke basicStroke = new BasicStroke( 1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0 );
                    for( String id : ire.getSceneIds( ) ) {
                        SceneElement temp = this.getSceneElementForId( id );
                        if( temp != null && temp.isVisible( ) ) {
                            addLine( x1, y1, temp, scene.getColor( ), basicStroke, lines );
                        }
                    }
                }
            }
        }

        for( Line line : lines ) {
            drawPanel.drawRelativeLine( line.x1, line.y1, line.x2, line.y2, line.color, line.stroke );
            drawPanel.drawRelativeArrowTip( line.x1, line.y1, line.x2, line.y2, line.color );
        }
    }

    /**
     * Adds a line form a given point to the given sceneElement
     * 
     * @param x1
     *            The x value of the initial point
     * @param y1
     *            The y value of the initial point
     * @param temp
     *            The destination sceneElement
     * @param color
     *            The color of the line
     * @param lines
     *            The list of lines where to add the new one
     */
    private void addLine( int x1, int y1, SceneElement temp, Color color, Stroke stroke, List<Line> lines ) {

        double w = temp.getWidth( ) * drawingScale / 2;
        double h = temp.getHeight( ) * drawingScale / 2;
        int x2 = (int) ( temp.getPosX( ) + w );
        int y2 = (int) ( temp.getPosY( ) + h );

        int x3 = x2;
        int y3 = y2;

        if( h == 0 || y2 == y1 || ( ( y2 - y1 ) != 0 && Math.abs( w / h ) <= Math.abs( ( x2 - x1 ) / ( y2 - y1 ) ) ) ) {
            if( x1 <= x2 ) {
                x3 = (int) ( x2 - w );
                y3 = (int) ( y2 - ( w / ( x2 - x1 ) ) * ( y2 - y1 ) );
            }
            else if( x1 > x2 ) {
                x3 = (int) ( x2 + w );
                y3 = (int) ( y2 + ( w / ( x2 - x1 ) ) * ( y2 - y1 ) );
            }
        }
        else {
            if( y1 <= y2 ) {
                y3 = (int) ( y2 - h );
                x3 = (int) ( x2 - ( h / ( y2 - y1 ) ) * ( x2 - x1 ) );
            }
            else if( y1 > y2 ) {
                y3 = (int) ( y2 + h );
                x3 = (int) ( x2 + ( h / ( y2 - y1 ) ) * ( x2 - x1 ) );
            }
        }

        lines.add( new Line( x1, y1, x3, y3, color, stroke ) );
    }

    /**
     * Class with all the elements of a line
     */
    private class Line {

        public int x1;

        public int x2;

        public int y1;

        public int y2;

        public Color color;

        public Stroke stroke;

        public Line( int x1, int y1, int x2, int y2, Color color, Stroke stroke ) {

            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            this.color = color;
            this.stroke = stroke;
        }
    }

    public SceneElement getSceneElementForId( String id ) {

        for( SceneElement scene : sceneElements ) {
            if( scene.getId( ).equals( id ) )
                return scene;
        }
        return null;
    }

    /**
     * Returns the sceneElements at a given mouse position. Null if there is
     * none.
     * 
     * @param mouseX
     *            The x value of the mouse position relative to the panel
     * @param mouseY
     *            The y value of the mouse position relative to the panel
     * @return The sceneElement in the given position
     */
    public SceneElement getSceneElement( int mouseX, int mouseY ) {

        int x = drawPanel.getRealX( mouseX );
        int y = drawPanel.getRealY( mouseY );

        for( int i = sceneElements.size( ) - 1; i >= 0; i-- ) {
            SceneElement scene = sceneElements.get( i );
            if( scene.isVisible( ) && scene.getPosX( ) < x && scene.getPosX( ) + scene.getWidth( ) * drawingScale * DEFAULT_SCALE > x && scene.getPosY( ) < y && scene.getPosY( ) + scene.getHeight( ) * drawingScale * DEFAULT_SCALE > y ) {
                checkBoxes.changeSelection( i, 0, false, false );
                return scene;
            }
        }

        return null;
    }

    public int getRealWidth( int i ) {

        return drawPanel.getRealWidth( i );
    }

    public int getRealHeight( int i ) {

        return drawPanel.getRealHeight( i );
    }
}
