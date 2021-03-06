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

import java.awt.Adjustable;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.controllers.ScenePreviewEditionController;
import es.eucm.eadventure.engine.core.gui.GUI;

/**
 * A JPanel that allows double-buffering, zooming and other advanced features.
 * 
 * @author Eugenio Marchiori
 * 
 */
public class DrawPanel extends JPanel {

    /**
     * Default serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * The back-buffer
     */
    private BufferedImage backBuffer;

    /**
     * Default margin value
     */
    private static final int MARGIN = 0;

    private static final int MARGIN_X = 300;

    private static final int MARGIN_Y = 300;

    /**
     * The margin left along the x-axis
     */
    private int marginX;

    /**
     * The margin left along the y-axis
     */
    private int marginY;

    /**
     * The ratio between the size of the panel and the size of the background
     * image
     */
    private double sizeRatio;

    /**
     * The background image
     */
    private Image background;

    /**
     * The width of the back-buffer
     */
    private int width;

    /**
     * The height of the back-buffer
     */
    private int height;

    /**
     * The zoom value (1.0 = no zoom)
     */
    private double zoom;

    /**
     * The horizontal scroll bar
     */
    private JScrollBar scrollX;

    /**
     * The vertical scroll bar
     */
    private JScrollBar scrollY;

    /**
     * The panel where the back-buffer is drawn
     */
    private JPanel insidePanel;
    
    private JPanel componentPanel;

    public DrawPanel( boolean zoomable ) {
        zoom = 1.0;
        this.setLayout( new BorderLayout( ) );
        insidePanel = createInsidePanel( );

        componentPanel = new JPanel();
        componentPanel.setOpaque( false );

        componentPanel.setLayout( new ComponentPanelLayout() );
        componentPanel.add(insidePanel, "main");

//        componentPanel.setLayout( new BorderLayout() );
//        componentPanel.add(insidePanel, BorderLayout.CENTER);
 //       componentPanel.add(new JButton("hola"), BorderLayout.NORTH);
        
        this.add( componentPanel, BorderLayout.CENTER );
        addZoomElements( zoomable );
    }

    /**
     * Creates the zoom elements and adds them to the panel if necessary
     * 
     * @param zoomable
     *            boolean indicating if the zoom elements must be added
     */
    private void addZoomElements( boolean zoomable ) {

        JPanel sliderPanel = new JPanel( );

        Icon zoomOutIcon = new ImageIcon( "img/icons/zoomout.png" );
        JButton zoomout = new JButton( zoomOutIcon );
        zoomout.setPreferredSize( new Dimension( 20, 20 ) );
        zoomout.setContentAreaFilled( false );
        zoomout.setToolTipText( TC.get( "DrawPanel.ZoomOut" ) );
        zoomout.setFocusable( false );
        zoomout.setMargin( new Insets( 0, 0, 0, 0 ) );
        zoomout.setBorder( BorderFactory.createEmptyBorder( ) );
        sliderPanel.add( zoomout );

        final JSlider slider = new JSlider( 10, 30 );
        slider.setValue( 10 );
        slider.addChangeListener( new ChangeListener( ) {

            public void stateChanged( ChangeEvent arg0 ) {

                double zoom = slider.getValue( ) / 10.0;
                DrawPanel.this.setZoom( zoom );
                DrawPanel.this.getParent( ).repaint( );
            }
        } );
        slider.setFocusable( false );
        slider.setToolTipText( TC.get( "DrawPanel.ZoomSlider" ) );
        sliderPanel.add( slider );

        Icon zoomInIcon = new ImageIcon( "img/icons/zoomin.png" );
        JButton zoomin = new JButton( zoomInIcon );
        zoomin.setPreferredSize( new Dimension( 20, 20 ) );
        zoomin.setContentAreaFilled( false );
        zoomin.setToolTipText( TC.get( "DrawPanel.ZoomIn" ) );
        zoomin.setFocusable( false );
        zoomin.setMargin( new Insets( 0, 0, 0, 0 ) );
        zoomin.setBorder( BorderFactory.createEmptyBorder( ) );
        sliderPanel.add( zoomin );

        zoomin.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent arg0 ) {

                slider.setValue( slider.getValue( ) + 3 );
            }
        } );
        zoomout.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent arg0 ) {

                slider.setValue( slider.getValue( ) - 3 );
            }
        } );

        scrollX = new JScrollBar( Adjustable.HORIZONTAL, 0, 10, 0, 100 );
        scrollY = new JScrollBar( Adjustable.VERTICAL, 0, 10, 0, 100 );
        scrollX.setValue( 45 );
        scrollY.setValue( 45 );
        scrollX.addAdjustmentListener( new AdjustmentListener( ) {
            public void adjustmentValueChanged( AdjustmentEvent arg0 ) {
                DrawPanel.this.getParent( ).repaint( );
            }
        } );
        scrollY.addAdjustmentListener( new AdjustmentListener( ) {
            public void adjustmentValueChanged( AdjustmentEvent arg0 ) {
                DrawPanel.this.getParent( ).repaint( );
            }
        } );
        if( zoomable )
            this.add( sliderPanel, BorderLayout.NORTH );
        this.add( scrollX, BorderLayout.SOUTH );
        this.add( scrollY, BorderLayout.EAST );

    }

    /**
     * 
     * Create the insidePanel where the back-buffer is drawn
     * 
     * @return the insidePanel
     */
    private JPanel createInsidePanel( ) {

        insidePanel = new JPanel( ) {

            private static final long serialVersionUID = 1L;
            
            @Override
            public void repaint( ) {
                super.repaint( );
                if( getSize( ).width > 0 && getSize( ).height > 0 ) {
                    calculateSize( );
                    if( backBuffer == null || width != getSize( ).width || height != getSize( ).height ) {
                        backBuffer = new BufferedImage( getSize( ).width, getSize( ).height, BufferedImage.TYPE_4BYTE_ABGR );
                        width = getSize( ).width;
                        height = getSize( ).height;
                    }
                }
            }

            @Override
            public void paint( Graphics g ) {

                super.paint( g );
                if( backBuffer != null ) {
                    int dx = (int) ( ( scrollX.getValue( ) / 90.0 ) * ( 1 - 1 / zoom ) * width );
                    int dy = (int) ( ( scrollY.getValue( ) / 90.0 ) * ( 1 - 1 / zoom ) * height );
                    int dw = (int) ( width / zoom );
                    int dh = (int) ( height / zoom );
                    g.drawImage( backBuffer, 0, 0, width, height, dx, dy, dx + dw, dy + dh, null );
                }
            }
            
            
        };
        
        return insidePanel;
    }

    private class ComponentPanelLayout implements LayoutManager2 {

        List<LayoutComponent> components = new ArrayList<LayoutComponent>();
        
        public void addLayoutComponent( Component comp, Object constraints ) {
            components.add( new LayoutComponent(comp, (String) constraints) );
        }

        public float getLayoutAlignmentX( Container target ) {
            return 0;
        }

        public float getLayoutAlignmentY( Container target ) {
            return 0;
        }

        public void invalidateLayout( Container target ) {
        }

        public Dimension maximumLayoutSize( Container target ) {
            return null;
        }

        public void addLayoutComponent( String name, Component comp ) {
        }

        public void layoutContainer( Container parent ) {
            
            for (LayoutComponent component : components) {
                if (component.main)
                    component.comp.setBounds( 0, 0, parent.getWidth( ), parent.getHeight( ) );
            }
            for (LayoutComponent component : components) {
                if (!component.main) {
                    component.comp.setBounds( DrawPanel.this.getRelativeX(component.x) - 30, DrawPanel.this.getRelativeY(component.y) - 20, 60 , 40);
                }
            }

        }

        public Dimension minimumLayoutSize( Container parent ) {
            return new Dimension(0,0);
        }

        public Dimension preferredLayoutSize( Container parent ) {
            return new Dimension( parent.getSize( ).width, parent.getSize( ).height );
        }

        public void removeLayoutComponent( Component comp ) {
            LayoutComponent temp = null;
            for (LayoutComponent component : components) {
                if (component.comp == comp)
                    temp = component;
            }
            if (temp != null)
                components.remove( temp );
        }
        
        private class LayoutComponent {
            
            public Component comp;
            
            boolean main;
            
            int x;
            
            int y;

            public LayoutComponent( Component comp, String constraints ) {
                this.comp = comp;
                if (constraints.equals( "main" ))
                    main = true;
                else {
                    main = false;
                    x = Integer.parseInt( constraints.split( ";" )[0] );
                    y = Integer.parseInt( constraints.split( ";" )[1] );
                }
            }
        }

    }
    
    /**
     * Set the value for the zoom
     * 
     * @param zoom
     *            the value
     */
    public void setZoom( double zoom ) {

        this.zoom = zoom;
    }

    /**
     * Set the background image for the panel
     * 
     * @param background
     *            the background image
     */
    public void setBackgroundImg( Image background ) {

        this.background = background;
        this.calculateSize( );
    }

    @Override
    public void repaint( ) {
        super.repaint( );
        if( insidePanel != null )
            insidePanel.repaint( );
        if (componentPanel != null) {
            if (component != null)
                componentPanel.updateUI( );
            componentPanel.repaint( );
        }
    }

    @Override
    public void paint( Graphics g ) {
        super.paint( g );
    }

    @Override
    public Graphics getGraphics( ) {

        if( backBuffer == null ) {
            int width = insidePanel.getSize( ).width;
            if( width < 1 )
                width = 1;
            int height = insidePanel.getSize( ).height;
            if( height < 1 )
                height = 1;
            backBuffer = new BufferedImage( width, height, BufferedImage.TYPE_4BYTE_ABGR );
            this.height = height;
            this.width = width;
        }
        return backBuffer.getGraphics( );
    }

    /**
     * Paints the background in the back-buffer
     */
    public void paintBackground( ) {

        if( backBuffer != null ) {
            Graphics2D g = (Graphics2D) backBuffer.getGraphics( );
            g.setColor( insidePanel.getBackground( ) );
            g.fillRect( 0, 0, backBuffer.getWidth( ), backBuffer.getHeight( ) );
        }
        if( background != null ) {
            paintRelativeImage( background, background.getWidth( null ) / 2, background.getHeight( null ), 1, 1.0f );
        }
        else {
            ImageIcon icon = new ImageIcon( "img/icons/noImageFrame.png" );
            Image image;
            if( icon != null && icon.getImage( ) != null )
                image = icon.getImage( );
            else
                image = new BufferedImage( 100, 120, BufferedImage.TYPE_3BYTE_BGR );
            paintRelativeImage( image, image.getWidth( null ) / 2, image.getHeight( null ), 1, 1.0f );
        }
    }

    /**
     * Paints an rescaled image in the back-buffer
     * 
     * @param image
     *            Image to be painted
     * @param x
     *            Absolute X position of the center of the image
     * @param y
     *            Absolute Y position of the bottom of the image
     * @param scale
     *            The scale of the image
     */
    protected void paintRelativeImage( Image image, int x, int y, double scale, float alpha ) {

        if( checkBackBuffer( ) && image != null ) {
            int width = (int) ( image.getWidth( null ) * sizeRatio );
            int height = (int) ( image.getHeight( null ) * sizeRatio );

            int posX = marginX + (int) ( x * sizeRatio - width * scale / 2 );
            int posY = marginY + (int) ( y * sizeRatio - height * scale );

            Graphics2D g = (Graphics2D) backBuffer.getGraphics( );

            if( alpha != 1.0f ) {
                AlphaComposite alphaComposite = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha );
                g.setComposite( alphaComposite );
            }
            g.drawImage( image, posX, posY, (int) ( width * scale ), (int) ( height * scale ), null );
        }
    }

    /**
     * Paints a rescaled image in the back-buffer
     * 
     * @param image
     *            The image to be painted
     * @param x
     *            Absolute X position of the top left corner of the image
     * @param y
     *            Absolute Y position of the top left corner of the image
     * @param scale
     *            The scale of the image
     */
    public void paintRelativeImageTop( Image image, int x, int y, double scale ) {

        if( checkBackBuffer( ) && image != null ) {
            int width = (int) ( image.getWidth( null ) * sizeRatio );
            int height = (int) ( image.getHeight( null ) * sizeRatio );

            int posX = marginX + (int) ( x * sizeRatio );
            int posY = marginY + (int) ( y * sizeRatio );

            backBuffer.getGraphics( ).drawImage( image, posX, posY, (int) ( width * scale ), (int) ( height * scale ), null );
        }
    }

    /**
     * Paints a rescaled line form and to the given points
     * 
     * @param g
     *            The graphics where the line is drawn
     * @param x1
     *            The absolute x of the start point
     * @param y1
     *            The absolute y of the start point
     * @param x2
     *            The absolute x of the end point
     * @param y2
     *            The absolute y of the end point
     */
    protected void drawRelativeLine( int x1, int y1, int x2, int y2 ) {

        if( checkBackBuffer( ) ) {
            int posX1 = marginX + (int) ( x1 * sizeRatio );
            int posY1 = marginY + (int) ( y1 * sizeRatio );
            int posX2 = marginX + (int) ( x2 * sizeRatio );
            int posY2 = marginY + (int) ( y2 * sizeRatio );
            backBuffer.getGraphics( ).drawLine( posX1, posY1, posX2, posY2 );
        }
    }

    protected void drawRelativeLineWithBorder( int x1, int y1, int x2, int y2 ) {

        if( checkBackBuffer( ) ) {
            int posX1 = marginX + (int) ( x1 * sizeRatio );
            int posY1 = marginY + (int) ( y1 * sizeRatio );
            int posX2 = marginX + (int) ( x2 * sizeRatio );
            int posY2 = marginY + (int) ( y2 * sizeRatio );
            Graphics2D g = (Graphics2D) backBuffer.getGraphics( );
            g.setStroke( new BasicStroke(3.0f) );
            g.setColor( Color.BLACK );
            g.drawLine( posX1, posY1, posX2, posY2 );
            g.setStroke( new BasicStroke(0.0f) );
            g.setColor( Color.WHITE );
            g.drawLine( posX1, posY1, posX2, posY2 );

        }
    }

    /**
     * Calculate the size of the images, depending on the size of the panel and
     * the size of the background image
     */
    private synchronized void calculateSize( ) {

        int backgroundWidth2 = 800;
        int backgroundHeight2 = 600;
        if( background != null ) {
            backgroundWidth2 = background.getWidth( null );
            backgroundHeight2 = background.getHeight( null );
        }

        if( background != null && insidePanel.getWidth( ) > 0 && insidePanel.getHeight( ) > 0 ) {
            double panelRatio = (double) insidePanel.getWidth( ) / (double) insidePanel.getHeight( );
            double imageRatio = (double) backgroundWidth2 / (double) backgroundHeight2;
            int width, height;
            marginX = MARGIN;
            marginY = MARGIN;

            if( panelRatio <= imageRatio ) {
                int panelWidth = insidePanel.getWidth( ) - MARGIN * 2;
                width = panelWidth;
                height = (int) ( panelWidth / imageRatio );
            }
            else {
                int panelHeight = insidePanel.getHeight( ) - MARGIN * 2;
                width = (int) ( panelHeight * imageRatio );
                height = panelHeight;
            }

            marginX = ( insidePanel.getWidth( ) - width ) / 2 - (int) ( ( scrollX.getValue( ) / 90.0 - 0.5 ) * MARGIN_X );
            marginY = ( insidePanel.getHeight( ) - height ) / 2 - (int) ( ( scrollY.getValue( ) / 90.0 - 0.5 ) * MARGIN_Y );

            sizeRatio = (double) width / (double) backgroundWidth2;
        }
    }

    /**
     * Returns the real X value in relation to the background of the X relative
     * to the panel
     * 
     * @param mouseX
     * @return
     */
    public int getRealX( int mouseX ) {

        //int dx = marginX - (scrollX != null ? (int) ((scrollX.getValue() / (90.0) * zoom) * (1 - 1/zoom) * backgroundWidth) : 0);
        int dx = (int) ( ( marginX - scrollX.getValue( ) / 90.0 * ( 1 - 1 / zoom ) * width ) * zoom );
        return (int) ( ( mouseX - dx ) / sizeRatio / zoom );
    }

    /**
     * Returns the X relative to the panel for a real X relative to the
     * background
     * 
     * @param realX
     * @return
     */
    public int getRelativeX( int realX ) {

        //int dx = marginX - (scrollX != null ? (int) ((scrollX.getValue() / (90.0) / zoom) * (1 - 1/zoom) * backgroundWidth) : 0);
        //return (int) ((realX) * sizeRatio * zoom) + dx;
        return marginX + (int) ( realX * sizeRatio );
    }

    /**
     * Returns the real Y value in relation to the background of the Y relative
     * to the panel
     * 
     * @param mouseY
     * @return
     */
    public int getRealY( int mouseY ) {

        int dy = (int) ( ( marginY - ( scrollY.getValue( ) / 90.0 ) * ( 1 - 1 / zoom ) * height ) * zoom );
        return (int) ( ( mouseY - dy ) / sizeRatio / zoom );
    }

    /**
     * Returns the Y relative to the panel for a real Y relative to the
     * background
     * 
     * @param realY
     * @return
     */
    public int getRelativeY( int realY ) {

        //		int dy = marginY - (scrollY != null ? (int) ((scrollY.getValue() / (90.0) / zoom) * (1 - 1/zoom) * backgroundHeight) : 0);
        //		return (int) ((realY) * sizeRatio * zoom) + dy;

        return marginY + (int) ( realY * sizeRatio );

    }

    public int getRelativeWidth( int realWidth ) {

        return (int) ( realWidth * sizeRatio );
    }

    public int getRealWidth( int relativeWidth ) {

        return (int) ( relativeWidth / sizeRatio / zoom );
    }

    public int getRelativeHeight( int realHeight ) {

        return (int) ( realHeight * sizeRatio );
    }

    public int getRealHeight( int relativeHeight ) {

        return (int) ( relativeHeight / sizeRatio / zoom );
    }

    public void drawRelativeLine( int x1, int y1, int x2, int y2, Color color ) {

        if( checkBackBuffer( ) ) {
            int posX1 = marginX + (int) ( x1 * sizeRatio );
            int posY1 = marginY + (int) ( y1 * sizeRatio );
            int posX2 = marginX + (int) ( x2 * sizeRatio );
            int posY2 = marginY + (int) ( y2 * sizeRatio );
            Graphics g = backBuffer.getGraphics( );
            g.setColor( color );
            g.drawLine( posX1, posY1, posX2, posY2 );
        }
    }

    public void drawRelativeLine( int x1, int y1, int x2, int y2, Color color, Stroke stroke ) {

        if( checkBackBuffer( ) ) {
            int posX1 = marginX + (int) ( x1 * sizeRatio );
            int posY1 = marginY + (int) ( y1 * sizeRatio );
            int posX2 = marginX + (int) ( x2 * sizeRatio );
            int posY2 = marginY + (int) ( y2 * sizeRatio );
            Graphics g = backBuffer.getGraphics( );
            g.setColor( color );
            ( (Graphics2D) g ).setStroke( stroke );
            g.drawLine( posX1, posY1, posX2, posY2 );
        }
    }

    public void fillRelativeCircle( int x2, int y2, Color color, int i ) {

        if( checkBackBuffer( ) ) {
            int posX2 = marginX + (int) ( x2 * sizeRatio );
            int posY2 = marginY + (int) ( y2 * sizeRatio );
            Graphics g = backBuffer.getGraphics( );
            g.setColor( color );
            g.fillOval( posX2 - i / 2, posY2 - i / 2, i, i );
        }
    }

    public void fillRelativePoly( int x[], int y[], Color color, float alpha ) {

        if( checkBackBuffer( ) ) {
            int x2[] = new int[ x.length ];
            int y2[] = new int[ y.length ];
            for( int i = 0; i < x2.length; i++ ) {
                x2[i] = marginX + (int) ( x[i] * sizeRatio );
                y2[i] = marginY + (int) ( y[i] * sizeRatio );
            }

            Graphics2D g = (Graphics2D) backBuffer.getGraphics( );
            g.setColor( color );
            if( alpha != 1.0f ) {
                AlphaComposite alphaComposite = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha );
                g.setComposite( alphaComposite );
            }
            g.fillPolygon( x2, y2, x2.length );
        }

    }

    public void drawRelativeString( String id, int posX, int posY ) {

        if( checkBackBuffer( ) ) {
            int posX1 = marginX + (int) ( posX * sizeRatio );
            int posY1 = marginY + (int) ( posY * sizeRatio );
            Graphics2D g = (Graphics2D) backBuffer.getGraphics( );
            g.setColor( Color.BLACK );
            g.drawString( id, posX1 - 1, posY1 - 1 );
            g.drawString( id, posX1 - 1, posY1 + 1 );
            g.drawString( id, posX1 + 1, posY1 - 1 );
            g.drawString( id, posX1 + 1, posY1 + 1 );
            g.setColor( Color.WHITE );
            g.drawString( id, posX1, posY1 );
        }
    }

    public void drawRelativeArrowTip( int x1, int y1, int x2, int y2, Color color ) {

        if( checkBackBuffer( ) ) {
            Graphics g = backBuffer.getGraphics( );
            g.setColor( color );
            int[] xPoints = new int[ 3 ];
            int[] yPoints = new int[ 3 ];
            int posX2 = marginX + (int) ( x2 * sizeRatio );
            int posY2 = marginY + (int) ( y2 * sizeRatio );

            double w = x2 - x1;
            double h = y2 - y1;
            double temp = Math.sqrt( w * w + h * h );
            if( temp != 0 ) {
                w = w / temp;
                h = h / temp;

                xPoints[0] = posX2;
                yPoints[0] = posY2;
                xPoints[1] = (int) ( posX2 - 10 * w + 5 * h );
                yPoints[1] = (int) ( posY2 - 5 * w - 10 * h );
                xPoints[2] = (int) ( posX2 - 10 * w - 5 * h );
                yPoints[2] = (int) ( posY2 + 5 * w - 10 * h );

                g.fillPolygon( xPoints, yPoints, 3 );
            }
        }
    }

    private boolean checkBackBuffer( ) {

        if( backBuffer == null && insidePanel.getSize( ).width > 0 && insidePanel.getSize( ).height > 0 ) {
            backBuffer = new BufferedImage( insidePanel.getSize( ).width, insidePanel.getSize( ).height, BufferedImage.TYPE_4BYTE_ABGR );
            width = insidePanel.getSize( ).width;
            height = insidePanel.getSize( ).height;
        }
        return backBuffer != null;
    }

    @Override
    public void removeMouseListener( MouseListener ml ) {

        insidePanel.removeMouseListener( ml );
    }

    @Override
    public void addMouseListener( MouseListener ml ) {

        insidePanel.addMouseListener( ml );
    }

    @Override
    public void removeMouseMotionListener( MouseMotionListener ml ) {

        insidePanel.removeMouseMotionListener( ml );
    }

    @Override
    public void addMouseMotionListener( MouseMotionListener ml ) {

        insidePanel.addMouseMotionListener( ml );
    }

    public int getBackgroundWidth( ) {

        int backgroundWidth = GUI.WINDOW_WIDTH;
        if( background != null )
            backgroundWidth = background.getWidth( null );
        return ( backgroundWidth > GUI.WINDOW_WIDTH ? backgroundWidth : GUI.WINDOW_WIDTH );
    }

    private Component component;
    
    public void addComponent(Component component, int x, int y) {
        if (this.component != null)
            this.componentPanel.remove( this.component );
        this.componentPanel.add( component, "" + x + ";" + y );
        this.component = component;
        componentPanel.setComponentZOrder( component, 0 );
        componentPanel.setComponentZOrder( insidePanel, 1 );
        componentPanel.updateUI( );
        componentPanel.repaint( );
    }
    
    private ScenePreviewEditionController controller;
    
    public void removeComponent(Component component) {
        this.componentPanel.remove( component );
        this.component = null;
        componentPanel.setComponentZOrder( insidePanel, 0 );
        componentPanel.updateUI( );
        componentPanel.repaint( );
    }

}
