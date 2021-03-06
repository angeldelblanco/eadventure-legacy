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
package es.eucm.eadventure.editor.gui.editdialogs;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.SwingUtilities;

import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.gui.MainWindow;
import es.eucm.eadventure.editor.gui.Updateable;
import es.eucm.eadventure.editor.gui.auxiliar.JPositionedDialog;

public abstract class ToolManagableDialog extends JPositionedDialog implements Updateable, WindowListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private boolean worksInLocal;

    private static KeyEventDispatcher createKeyDispatcher( ) {

        return new KeyEventDispatcher( ) {

            public boolean dispatchKeyEvent( KeyEvent e ) {

                boolean dispatched = false;
                if( e.getID( ) == KeyEvent.KEY_RELEASED && e.isControlDown( ) && e.getKeyCode( ) == KeyEvent.VK_Z ) {
                    Controller.getInstance( ).undoTool( );
                    e.consume( );
                    dispatched = true;
                }
                else if( e.getID( ) == KeyEvent.KEY_RELEASED && e.isControlDown( ) && e.getKeyCode( ) == KeyEvent.VK_Y ) {
                    Controller.getInstance( ).redoTool( );
                    e.consume( );
                    dispatched = true;
                }

                return dispatched;
            }

        };
    }

    private KeyEventDispatcher undoRedoDispatcher = null;

    private void addUndoRedoDispatcher( ) {

        //KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(undoRedoDispatcher);
        undoRedoDispatcher = createKeyDispatcher( );
        KeyboardFocusManager.getCurrentKeyboardFocusManager( ).addKeyEventDispatcher( undoRedoDispatcher );
    }

    public ToolManagableDialog( Window window, String title ) {

        this( window, title, true );
    }

    public ToolManagableDialog( Window window, String title, boolean worksInLocal ) {

        super( window, title, Dialog.ModalityType.TOOLKIT_MODAL );
        this.worksInLocal = worksInLocal;
        this.addWindowListener( this );
        super.setVisible( false );
    }

    public boolean updateFields( ) {

        //Do nothing
        return false;
    }

    @Override
    public void setTitle( String title ) {

        super.setTitle( title );
    }

    @Override
    public void setVisible( boolean visible ) {

        if( visible != this.isVisible( ) ) {
            if( visible )
                addUndoRedoDispatcher( );
            else
                KeyboardFocusManager.getCurrentKeyboardFocusManager( ).removeKeyEventDispatcher( undoRedoDispatcher );

            if( visible && Controller.getInstance( ).peekWindow( ) != this )
                Controller.getInstance( ).pushWindow( this );
            else if( !visible && Controller.getInstance( ).peekWindow( ) == this ) {
                Controller.getInstance( ).popWindow( );
            }

            if( worksInLocal ) {
                if( visible ) {
                    Controller.getInstance( ).pushLocalToolManager( );
                }
                else {
                    Controller.getInstance( ).popLocalToolManager( );
                }
            }
            if( visible ) {
                SwingUtilities.invokeLater( new Runnable( ) {

                    public void run( ) {

                        repaint( );
                    }
                } );
            }
            super.setVisible( visible );
        }

    }

    public void windowClosing( WindowEvent e ) {

        KeyboardFocusManager.getCurrentKeyboardFocusManager( ).removeKeyEventDispatcher( undoRedoDispatcher );
        if( Controller.getInstance( ).peekWindow( ) == this )
            Controller.getInstance( ).popWindow( );
        setVisible( false );
        dispose( );
        //Controller.getInstance().updatePanel();
    }

    public void windowOpened( WindowEvent e ) {

    }

    public void windowActivated( WindowEvent e ) {

    }

    public void windowClosed( WindowEvent e ) {

        KeyboardFocusManager.getCurrentKeyboardFocusManager( ).removeKeyEventDispatcher( undoRedoDispatcher );
        if( Controller.getInstance( ).peekWindow( ) == this )
            Controller.getInstance( ).popWindow( );
        setVisible( false );
    }

    public void windowDeactivated( WindowEvent e ) {

    }

    public void windowDeiconified( WindowEvent e ) {

    }

    public void windowIconified( WindowEvent e ) {

    }
    
    private static ToolManagableDialog lookForDialog(Component comp){
        
        Component pointer = null;
        while (pointer==null){
            if (comp.getParent( )!=null && comp.getParent() instanceof ToolManagableDialog){
                pointer = comp.getParent( );
            }else if (comp.getParent( )==null){
                break;
            } else {
                comp=comp.getParent( );
            }
        }
        if (pointer!=null){
            return (ToolManagableDialog)pointer;
        } else{
            return null;
        }
            
    }
    
    
    private static MainWindow lookForMainWindow(Component comp){
        
        Component pointer = null;
        while (pointer==null){
            if (comp.getParent( )!=null && comp.getParent() instanceof MainWindow){
                pointer = comp.getParent( );
            }else if (comp.getParent( )==null){
                break;
            } else {
                comp=comp.getParent( );
            }
        }
        if (pointer!=null){
            return (MainWindow)pointer;
        } else{
            return null;
        }
            
    }
    
    public static void cleanSelectionMainWindow(Component component){
        
        SwingUtilities.invokeLater( new ExtendDialog(ToolManagableDialog.lookForMainWindow( component )));
     } 
    
    public static void cleanSelection(Component component){
    
       SwingUtilities.invokeLater( new ExtendDialog(ToolManagableDialog.lookForDialog( component )));
    }
    
    private static class ExtendDialog implements Runnable{
        
        private Component comp;
        
        public ExtendDialog(Component comp){
            this.comp = comp;            
        }

        public void run( ) {

            
            if (comp!=null){
                int w= comp.getWidth( );
                int h=comp.getHeight( );
                comp.setSize( w+1,h+1 );
                SwingUtilities.invokeLater( new ReduceDialog(comp));
            
        }
        
    }
    }
        
        private static class ReduceDialog implements Runnable{
            
            private Component comp;
            
            public ReduceDialog(Component comp){
                this.comp = comp;            
            }

            public void run( ) {
                if (comp!=null){
                    int w= comp.getWidth( );
                    int h=comp.getHeight( );
                    comp.setSize( w-1,h-1);
                }
                
            }
            
        }
        
    
    
    
    
    
}
