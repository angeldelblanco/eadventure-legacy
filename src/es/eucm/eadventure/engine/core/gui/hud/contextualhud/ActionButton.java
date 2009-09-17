/**
 * <e-Adventure> is an <e-UCM> research project. <e-UCM>, Department of Software
 * Engineering and Artificial Intelligence. Faculty of Informatics, Complutense
 * University of Madrid (Spain).
 * 
 * @author Del Blanco, A., Marchiori, E., Torrente, F.J. (alphabetical order) *
 * @author L�pez Ma�as, E., P�rez Padilla, F., Sollet, E., Torijano, B. (former
 *         developers by alphabetical order)
 * @author Moreno-Ger, P. & Fern�ndez-Manj�n, B. (directors)
 * @year 2009 Web-site: http://e-adventure.e-ucm.es
 */

/*
 * Copyright (C) 2004-2009 <e-UCM> research group
 * 
 * This file is part of <e-Adventure> project, an educational game & game-like
 * simulation authoring tool, available at http://e-adventure.e-ucm.es.
 * 
 * <e-Adventure> is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * <e-Adventure> is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * <e-Adventure>; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */
package es.eucm.eadventure.engine.core.gui.hud.contextualhud;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;

import es.eucm.eadventure.common.data.adventure.DescriptorData;
import es.eucm.eadventure.common.data.chapter.CustomAction;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.engine.core.control.Game;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalConditions;
import es.eucm.eadventure.engine.core.gui.GUI;
import es.eucm.eadventure.engine.multimedia.MultimediaManager;

public class ActionButton {

    /**
     * Width of an action button
     */
    public static final int ACTIONBUTTON_WIDTH = 40;

    /**
     * Height of an action button
     */
    public static final int ACTIONBUTTON_HEIGHT = 40;

    /**
     * Constant that represent the hand button
     */
    public static final int HAND_BUTTON = 0;

    /**
     * Constant that represents the eye button
     */
    public static final int EYE_BUTTON = 1;

    /**
     * Constant that represents the mouth button
     */
    public static final int MOUTH_BUTTON = 2;
    
    /**
     * Constant that represents the drag button
     */
    public static final int DRAG_BUTTON = 3;
    
    /**
     * Constant that represents the custom button
     */
    public static final int CUSTOM_BUTTON = 4;
    

    /**
     * Image of the button in it's normal state
     */
    private Image buttonNormal;

    /**
     * Image of the button when it's pressed
     */
    private Image buttonPressed;

    /**
     * Image of the button when it has the mouse over
     */
    private Image buttonOver;

    /**
     * Position of the button in the x-axis
     */
    private int posX;

    /**
     * Position of the button in the y-axis
     */
    private int posY;

    /**
     * Boolean that indicates the mouse is over the button
     */
    private boolean over;

    /**
     * Boolean that indicates that the button is being pressed with the mouse
     */
    private boolean pressed;

    /**
     * Custom action of the button
     */
    private CustomAction customAction;

    /**
     * Name of the action represeted by the button
     */
    private String actionName;

    /**
     * The type of the button
     */
    private int type;

    /**
     * Construct a button form it's type
     * 
     * @param type
     *            the type of the button
     */
    public ActionButton( int type ) {

        switch( type ) {
            case HAND_BUTTON:
                loadButtonImages( DescriptorData.USE_GRAB_BUTTON, "btnHand" );
                actionName = TextConstants.getText( "ActionButton.GrabGiveUse" );
                break;
            case EYE_BUTTON:
                loadButtonImages( DescriptorData.EXAMINE_BUTTON, "btnEye" );
                actionName = TextConstants.getText( "ActionButton.Examine" );
                break;
            case MOUTH_BUTTON:
                loadButtonImages( DescriptorData.TALK_BUTTON, "btnMouth" );
                actionName = TextConstants.getText( "ActionButton.Talk" );
                break;
            case DRAG_BUTTON:
                loadButtonImages( DescriptorData.USE_GRAB_BUTTON, "btnHand" );
                actionName = TextConstants.getText( "ActionButton.Drag" );
                break;
        }
        this.type = type;
    }

    private void loadButtonImages( String type, String name ) {

        DescriptorData descriptor = Game.getInstance( ).getGameDescriptor( );
        String customNormalPath = null;
        String customHighlightedPath = null;
        String customPressedPath = null;

        customNormalPath = descriptor.getButtonPath( type, DescriptorData.NORMAL_BUTTON );
        customHighlightedPath = descriptor.getButtonPath( type, DescriptorData.HIGHLIGHTED_BUTTON );
        customPressedPath = descriptor.getButtonPath( type, DescriptorData.PRESSED_BUTTON );

        buttonNormal = loadImage( customNormalPath, "gui/hud/contextual/" + name + ".png" );
        buttonOver = loadImage( customHighlightedPath, "gui/hud/contextual/" + name + "Highlighted.png" );
        buttonPressed = loadImage( customPressedPath, "gui/hud/contextual/" + name + "Pressed.png" );
    }

    private Image loadImage( String customPath, String defaultPath ) {

        Image temp = null;
        if( customPath == null )
            temp = MultimediaManager.getInstance( ).loadImage( defaultPath, MultimediaManager.IMAGE_MENU );
        else {
            try {
                temp = MultimediaManager.getInstance( ).loadImageFromZip( customPath, MultimediaManager.IMAGE_MENU );
            }
            catch( Exception e ) {
                temp = MultimediaManager.getInstance( ).loadImage( "gui/hud/contextual/btnError.png", MultimediaManager.IMAGE_MENU );
            }
        }
        return temp;
    }

    /**
     * Construct a button from a custom action
     * 
     * @param action
     *            the custom action
     */
    public ActionButton( CustomAction action ) {

        actionName = action.getName( );
        customAction = action;

        Resources resources = null;
        for( int i = 0; i < action.getResources( ).size( ) && resources == null; i++ )
            if( new FunctionalConditions( action.getResources( ).get( i ).getConditions( ) ).allConditionsOk( ) )
                resources = action.getResources( ).get( i );

        buttonNormal = loadImage( resources.getAssetPath( "buttonNormal" ), "gui/hud/contextual/btnError.png" );
        buttonOver = loadImage( resources.getAssetPath( "buttonOver" ), "gui/hud/contextual/btnError.png" );
        buttonPressed = loadImage( resources.getAssetPath( "buttonPressed" ), "gui/hud/contextual/btnError.png" );

        buttonNormal = scaleButton( buttonNormal );
        buttonOver = scaleButton( buttonOver );
        buttonPressed = scaleButton( buttonPressed );

        this.type = CUSTOM_BUTTON;
    }

    /**
     * Scale an image of a button
     * 
     * @param button
     *            the image to scale
     * @return the scaled image
     */
    private Image scaleButton( Image button ) {

        if( button.getWidth( null ) > ActionButtons.MAX_BUTTON_WIDTH )
            button = button.getScaledInstance( ActionButtons.MAX_BUTTON_WIDTH, button.getHeight( null ), Image.SCALE_SMOOTH );
        if( button.getWidth( null ) < ActionButtons.MIN_BUTTON_WIDTH )
            button = button.getScaledInstance( ActionButtons.MIN_BUTTON_WIDTH, button.getHeight( null ), Image.SCALE_SMOOTH );
        if( button.getHeight( null ) > ActionButtons.MAX_BUTTON_HEIGHT )
            button = button.getScaledInstance( button.getWidth( null ), ActionButtons.MAX_BUTTON_HEIGHT, Image.SCALE_SMOOTH );
        if( button.getHeight( null ) < ActionButtons.MIN_BUTTON_HEIGHT )
            button = button.getScaledInstance( button.getWidth( null ), ActionButtons.MIN_BUTTON_HEIGHT, Image.SCALE_SMOOTH );
        return button;
    }

    /**
     * @return the buttonNormal
     */
    public Image getButtonNormal( ) {

        return buttonNormal;
    }

    /**
     * @return the buttonPressed
     */
    public Image getButtonPressed( ) {

        return buttonPressed;
    }

    /**
     * @return the buttonOver
     */
    public Image getButtonOver( ) {

        return buttonOver;
    }

    /**
     * @param posX
     *            the posX to set
     */
    public void setPosX( int posX ) {

        this.posX = posX;
    }

    /**
     * @param posY
     *            the posY to set
     */
    public void setPosY( int posY ) {

        this.posY = posY;
    }

    public void draw( Graphics2D g, float percent, int posX, int posY ) {

        int x = ( this.posX - ACTIONBUTTON_WIDTH / 2 );
        int y = this.posY - ACTIONBUTTON_HEIGHT / 2;
        if( pressed )
            g.drawImage( buttonPressed, x, y, null );
        else if( over ) {
            g.drawImage( buttonOver, x, y, null );
        }
        else {
            x = ( posX - ACTIONBUTTON_WIDTH / 2 );
            y = posY - ACTIONBUTTON_HEIGHT / 2;
            Composite original = g.getComposite( );
            Composite alphaComposite = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, percent );
            g.setComposite( alphaComposite );
            g.drawImage( buttonNormal, x, y, null );
            g.setComposite( original );
        }
    }

    public void drawName( Graphics2D g ) {

        int y = this.posY - ACTIONBUTTON_HEIGHT / 2;
        String[] text = new String[] { actionName };
        GUI.drawStringOnto( g, text, posX, y, Color.BLACK, Color.WHITE );
    }

    public String getName( ) {

        return actionName;
    }

    public int getType( ) {

        return type;
    }

    /**
     * Returns true if the coordinates are inside the button
     * 
     * @param x
     *            the x-axis value
     * @param y
     *            the y-axis value
     * @return true if inside the button
     */
    public boolean isInside( int x, int y ) {

        if( x > ( posX - ACTIONBUTTON_WIDTH / 2 ) && x < ( posX + ACTIONBUTTON_WIDTH / 2 ) && y > ( posY - ACTIONBUTTON_HEIGHT / 2 ) && y < ( posY + ACTIONBUTTON_HEIGHT / 2 ) )
            return true;
        return false;
    }

    public void setPressed( boolean pressed ) {

        this.pressed = pressed;
    }

    public void setOver( boolean over ) {

        this.over = over;
    }

    public CustomAction getCustomAction( ) {

        return customAction;
    }

    public void setName( String string ) {

        actionName = string;
    }

    public int getPosX( ) {

        return posX;
    }

    public int getPosY( ) {

        return posY;
    }

    public boolean isOver( ) {

        return over;
    }
}
