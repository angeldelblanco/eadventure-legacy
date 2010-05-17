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
package es.eucm.eadventure.editor.gui.editdialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JOptionPane;

public class GenericOptionPaneDialog extends ToolManagableDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JOptionPane optionPane;

    public GenericOptionPaneDialog( Window window, String title, Object message, int messageType, int optionType, String[] options, Icon icon, Object initialValue ) {

        super( window, title, false );
        this.optionPane = new JOptionPane( message, messageType, optionType, icon, options, initialValue );
        this.add( optionPane, BorderLayout.CENTER );
        optionPane.addPropertyChangeListener( new PropertyChangeListener( ) {

            public void propertyChange( PropertyChangeEvent e ) {

                String prop = e.getPropertyName( );

                if( GenericOptionPaneDialog.this.isVisible( ) && ( e.getSource( ) == optionPane ) && ( prop.equals( JOptionPane.VALUE_PROPERTY ) || prop.equals( JOptionPane.INPUT_VALUE_PROPERTY ) ) ) {

                    GenericOptionPaneDialog.this.dispose( );
                }
            }
        } );

        setResizable( false );

        this.pack( );
        Dimension screenSize = Toolkit.getDefaultToolkit( ).getScreenSize( );
        setLocation( ( screenSize.width - getWidth( ) ) / 2, ( screenSize.height - getHeight( ) ) / 2 );
    }

    public GenericOptionPaneDialog( Window window, String title, Object message, int messageType, int optionType ) {

        this( window, title, message, messageType, optionType, null, null, null );
    }

    public GenericOptionPaneDialog( Window window, String title, Object message, int messageType, int optionType, String[] options ) {

        this( window, title, message, messageType, optionType, options, null, null );
        
    }

    public GenericOptionPaneDialog( Window window, String title, Object message, int messageType, Object[] selectionValues, Object initialValue ) {

        this( window, title, message, messageType, JOptionPane.OK_CANCEL_OPTION, null, null, initialValue );

    }

    public void selectInitialValue( ) {

        optionPane.selectInitialValue( );
    }

    public void setInitialSelectionValue( Object initialValue ) {

        optionPane.setInitialSelectionValue( initialValue );
    }

    public void setWantsInput( boolean input ) {

        optionPane.setWantsInput( input );
    }

    public static Object showConfirmDialog( Window window, String title, Object message, int messageType, int optionType ) {

        GenericOptionPaneDialog optionPane = new GenericOptionPaneDialog( window, title, message, messageType, optionType );
        optionPane.setVisible( true );
        return optionPane.getIntegerOption( );
    }

    public void setSelectionValues( Object[] selectionValues ) {

        optionPane.setSelectionValues( selectionValues );
    }

    public static void showMessageDialog( Window window, String title, Object message, int messageType ) {

        GenericOptionPaneDialog optionPane = new GenericOptionPaneDialog( window, title, message, messageType, JOptionPane.DEFAULT_OPTION );
        optionPane.setVisible( true );
    }

    public static Object showInputDialog( Window window, String title, Object message, int messageType, Object[] selectionValues, Object initialValue ) {

        GenericOptionPaneDialog optionPane = new GenericOptionPaneDialog( window, title, message, messageType, null, null );

        optionPane.setWantsInput( true );
        optionPane.setSelectionValues( selectionValues );
        optionPane.setInitialSelectionValue( initialValue );
        optionPane.selectInitialValue( );
        optionPane.setComponentOrientation( window.getComponentOrientation( ) );
        if( selectionValues != null )
            optionPane.setMinimumSize( new Dimension( 200, 150 ) );
        Dimension screenSize = Toolkit.getDefaultToolkit( ).getScreenSize( );
        optionPane.setLocation( ( screenSize.width - optionPane.getWidth( ) ) / 2, ( screenSize.height - optionPane.getHeight( ) ) / 2 );
        optionPane.setVisible( true );

        Object value = optionPane.getInputValue( );
        if( value == JOptionPane.UNINITIALIZED_VALUE )
            return null;
        else
            return value;
    }
    
    public static Object showOptionDialog( Window window, String title, Object message, int messageType, Object[] selectionValues ) {

        GenericOptionPaneDialog optionPane = new GenericOptionPaneDialog( window, title, message, messageType, null, null );

        optionPane.setWantsInput( false );
        optionPane.setSelectionValues( selectionValues );
        //optionPane.setInitialSelectionValue( initialValue );
        //optionPane.selectInitialValue( );
        optionPane.setComponentOrientation( window.getComponentOrientation( ) );
        if( selectionValues != null )
            optionPane.setMinimumSize( new Dimension( 200, 150 ) );
        Dimension screenSize = Toolkit.getDefaultToolkit( ).getScreenSize( );
        optionPane.setLocation( ( screenSize.width - optionPane.getWidth( ) ) / 2, ( screenSize.height - optionPane.getHeight( ) ) / 2 );
        optionPane.setVisible( true );

        Object value = optionPane.getIntegerOption( );
        if( value == JOptionPane.UNINITIALIZED_VALUE )
            return null;
        else
            return value;
    }

    public Object getInputValue( ) {

        return optionPane.getInputValue( );
    }

    public Object getIntegerOption( ) {

        if( optionPane.getValue( ) == null )
            return JOptionPane.CLOSED_OPTION;
        if( optionPane.getOptions( ) == null ) {
            if( optionPane.getValue( ) instanceof Integer )
                return ( (Integer) optionPane.getValue( ) ).intValue( );
            return JOptionPane.CLOSED_OPTION;
        }
        for( int counter = 0, maxCounter = optionPane.getOptions( ).length; counter < maxCounter; counter++ ) {
            if( optionPane.getOptions( )[counter].equals( optionPane.getValue( ) ) )
                return counter;
        }
        return JOptionPane.CLOSED_OPTION;
    }

}
