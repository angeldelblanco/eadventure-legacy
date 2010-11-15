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
package es.eucm.eadventure.engine.core.gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

import es.eucm.eadventure.common.data.adventure.DescriptorData;
import es.eucm.eadventure.engine.core.control.Game;
import es.eucm.eadventure.engine.core.gui.hud.contextualhud.ContextualHUD;
import es.eucm.eadventure.engine.core.gui.hud.traditionalhud.TraditionalHUD;
import es.eucm.eadventure.engine.multimedia.MultimediaManager;

public class GUIApplet extends GUI {

    private static JApplet applet;

    @Override
    public Frame getJFrame( ) {

        // TODO Auto-generated method stub
        return null;
    }

    public static void create( ) {

        instance = new GUIApplet( );
    }

    @Override
    public void initGUI( int guiType, boolean customized ) {

        //JLabel label = new JLabel(
        //        "You are successfully running a Swing applet!");
        //label.setHorizontalAlignment(JLabel.CENTER);
        //label.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black));

        gameFrame = new Canvas( );
        background = null;
        elementsToDraw = new ArrayList<ElementImage>( );
        textToDraw = new ArrayList<Text>( );

        gameFrame.setIgnoreRepaint( true );
        gameFrame.setFont( new Font( "Dialog", Font.PLAIN, 18 ) );
        Hashtable attributes = new Hashtable();
        attributes.put(TextAttribute.WIDTH, TextAttribute.WIDTH_SEMI_EXTENDED);
        gameFrame.setFont( gameFrame.getFont( ).deriveFont( attributes ) );
        gameFrame.setBackground( Color.black );
        gameFrame.setForeground( Color.white );
        gameFrame.setSize( new Dimension( WINDOW_WIDTH, WINDOW_HEIGHT ) );

        applet.getContentPane( ).add( gameFrame, BorderLayout.CENTER );

        gameFrame.createBufferStrategy( 2 );

        if( guiType == DescriptorData.GUI_TRADITIONAL )
            hud = new TraditionalHUD( );
        else if( guiType == DescriptorData.GUI_CONTEXTUAL )
            hud = new ContextualHUD( );

        gameFrame.setEnabled( true );
        gameFrame.setVisible( true );
        gameFrame.setFocusable( true );

        gameFrame.createBufferStrategy( 2 );
        gameFrame.validate( );

        gameFrame.addFocusListener( this );
        gameFrame.requestFocusInWindow( );

        hud.init( );

        // Load the customized default cursor
        if( Game.getInstance( ).getGameDescriptor( ).getCursorPath( DEFAULT_CURSOR ) != null ) {
            //System.out.println("PATH CURSOR = "+Game.getInstance( ).getGameDescriptor( ).getCursorPath( DEFAULT_CURSOR ) );
            defaultCursor = Toolkit.getDefaultToolkit( ).createCustomCursor( MultimediaManager.getInstance( ).loadImageFromZip( Game.getInstance( ).getGameDescriptor( ).getCursorPath( DEFAULT_CURSOR ), MultimediaManager.IMAGE_MENU ), new Point( 0, 0 ), "defaultCursor" );
            // Load the default default cursor
        }
        else
            defaultCursor = Toolkit.getDefaultToolkit( ).createCustomCursor( MultimediaManager.getInstance( ).loadImage( "gui/cursors/default.png", MultimediaManager.IMAGE_MENU ), new Point( 0, 0 ), "defaultCursor" );
        gameFrame.setCursor( defaultCursor );

    }

    public static void delete( ) {

        instance = null;
    };

    @Override
    public void restoreFrame( ) {

        if( component != null ) {
            applet.remove( component );
        }
        component = null;
        applet.setIgnoreRepaint( true );
        applet.repaint( );
        gameFrame.setVisible( true );
        applet.validate( );
    }

    @Override
    public JFrame showComponent( Component component ) {

        gameFrame.setVisible( false );
        if( this.component != null )
            applet.remove( this.component );
        this.component = component;
        component.setBackground( Color.BLACK );
        component.setForeground( Color.BLACK );
        applet.add( component, BorderLayout.CENTER );
        applet.validate( );
        component.repaint( );
        //System.out.println("IS DISPLAYABLE: " + component.isDisplayable() + "\n");

        return null;
    }

    public static void setApplet( JApplet adventureApplet ) {

        applet = adventureApplet;
    }

    @Override
    public JFrame showComponent( Component component, int w, int h ) {
        gameFrame.setVisible( false );
        if( this.component != null )
            applet.remove( this.component );
        this.component = new JPanel();
        
//        this.component = component;
        component.setBackground( Color.BLACK );
        component.setForeground( Color.BLACK );
        this.component.setBackground( Color.BLACK );
        this.component.setForeground( Color.BLACK );
        ((JPanel) this.component).setLayout( null );
        ((JPanel) this.component).add( component );
        
        int fixedWidth = w;
        int fixedHeight = h;
        w = GUI.WINDOW_WIDTH;
        h = GUI.WINDOW_HEIGHT;
        if (fixedWidth / fixedHeight >= w / h) {
            w = GUI.WINDOW_WIDTH;
            h = (int) (((float) fixedHeight / (float) fixedWidth) * GUI.WINDOW_WIDTH);
        } else {
            h = GUI.WINDOW_HEIGHT;
            w = (int) (((float) fixedWidth / (float) fixedHeight) * GUI.WINDOW_HEIGHT);
        }
        
        int posX = ( GUI.WINDOW_WIDTH - w ) / 2;
        int posY = ( GUI.WINDOW_HEIGHT - h ) / 2;

        component.setBounds(posX,posY, w, h);
        applet.add( component );
        applet.validate( );
        component.repaint( );

        //TODO To implement
        return showComponent (component);
    }

}
