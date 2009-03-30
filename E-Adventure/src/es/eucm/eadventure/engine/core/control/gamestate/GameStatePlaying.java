package es.eucm.eadventure.engine.core.control.gamestate;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.engine.core.control.Game;
import es.eucm.eadventure.common.data.adaptation.AdaptedState;
import es.eucm.eadventure.common.data.chapter.NextScene;
import es.eucm.eadventure.engine.core.gui.GUI;

/**
 * A game main loop during the normal game 
 */
public class GameStatePlaying extends GameState {
    
    /**
     * List of mouse events.
     */
    private ArrayList<MouseEvent> vMouse;
    
    /**
     * Constructor.
     */
    public GameStatePlaying(){
        super( );
        vMouse = new ArrayList<MouseEvent>();
    }

    /*
     * (non-Javadoc)
     * @see es.eucm.eadventure.engine.engine.control.gamestate.GameState#EvilLoop(long, int)
     */
    public synchronized void mainLoop( long elapsedTime, int fps ) {

        // Process the mouse events
        while( vMouse.size( ) > 0 ) {
            if( vMouse.get( 0 ).getID( ) == MouseEvent.MOUSE_CLICKED )
                mouseClickedEvent( vMouse.get( 0 ) );
            else if( vMouse.get( 0 ).getID( ) == MouseEvent.MOUSE_MOVED )
                mouseMovedEvent( vMouse.get( 0 ) );
            else if ( vMouse.get( 0 ).getID() == MouseEvent.MOUSE_PRESSED)
            	mousePressedEvent( vMouse.get(0) );
            else if ( vMouse.get(0).getID() == MouseEvent.MOUSE_RELEASED)
            	mouseReleasedEvent( vMouse.get(0) );
            else if ( vMouse.get(0).getID() == MouseEvent.MOUSE_DRAGGED)
            	mouseDraggedEvent( vMouse.get(0) );
            vMouse.remove( 0 );
        }

        // Update the time elapsed in the functional scene and in the GUI
        game.getFunctionalScene( ).update( elapsedTime );
        GUI.getInstance( ).update( elapsedTime );
        
        // Get the graphics and paint the whole screen in black
        Graphics2D g = GUI.getInstance( ).getGraphics( );
        // TODO check, though it should give no problems with non-transparent backgrounds
        //g.clearRect( 0, 0, GUI.WINDOW_WIDTH, GUI.WINDOW_HEIGHT );

        // Draw the functional scene, and then the GUI
        game.getFunctionalScene( ).draw( );
        GUI.getInstance( ).drawScene( g , elapsedTime);
        GUI.getInstance( ).drawHUD( g );

        // Draw the FPS
        //g.setColor( Color.WHITE );
        //g.drawString( Integer.toString( fps ), 780, 14 );
       
        // If there is an adapted state to be executed
        if( game.getAdaptedStateToExecute( ) != null ) {
            
            // If it has an initial scene, set it
            if( game.getAdaptedStateToExecute( ).getTargetId( ) != null ) {
                game.setNextScene( new NextScene( game.getAdaptedStateToExecute( ).getTargetId( ) ) );
                game.setState( Game.STATE_NEXT_SCENE );
                game.flushEffectsQueue( );
            }
            
            // Set the flag values
            for( String flag : game.getAdaptedStateToExecute( ).getActivatedFlags( ) )
               if (game.getFlags().existFlag(flag))
        	game.getFlags( ).activateFlag( flag );
            for( String flag : game.getAdaptedStateToExecute( ).getDeactivatedFlags( ) )
              if (game.getFlags().existFlag(flag))
        	game.getFlags( ).deactivateFlag( flag );
            
            // Set the vars
            List<String> adaptedVars = new ArrayList<String>();
            List<String> adaptedValues = new ArrayList<String>();
            game.getAdaptedStateToExecute().getVarsValues(adaptedVars, adaptedValues );
            for ( int i=0; i<adaptedVars.size(); i++ ){
        	String varName = adaptedVars.get(i);
        	String varValue = adaptedValues.get(i);
        	// check if it is a "set value" operation
        	if (AdaptedState.isSetValueOp( varValue)){
        	    String val = AdaptedState.getSetValueData(varValue);
        	    if (val!=null)
        	    game.getVars().setVarValue(varName, Integer.parseInt(val));
        	}
        	// it is "increment" or "decrement" operation, for both of them is necessary to 
        	// get the current value of referenced variable
        	else{
        	    if (game.getVars().existVar(varName)){
        	    int currentValue = game.getVars().getValue(varName);
        	    if (AdaptedState.isIncrementOp(varValue)){
        		game.getVars().setVarValue(varName, currentValue + 1);
        	    }else if (AdaptedState.isDecrementOp(varValue)){
        		game.getVars().setVarValue(varName, currentValue - 1);
        	    }
        	    }
        	}
            }
            	
        
            
        }

        // Update the data pending from the flags
        game.updateDataPendingFromState( true );
        
        // Ends the draw process
        GUI.getInstance( ).endDraw( );
        g.dispose( );
    }


	@Override
    public synchronized void mouseClicked( MouseEvent e ) {
        vMouse.add( e );
    }

    @Override
    public synchronized void mouseMoved( MouseEvent e ) {
        vMouse.add( e );
    }
    
    @Override
    public synchronized void mousePressed( MouseEvent e) {
    	vMouse.add( e );
    }

    @Override
    public synchronized void mouseReleased( MouseEvent e) {
    	vMouse.add( e );
    }
    
    @Override
    public synchronized void mouseDragged( MouseEvent e) {
    	vMouse.add( e );
    }

    /**
     * Triggers the given mouse event.
     * @param e Mouse event
     */
    public void mouseClickedEvent( MouseEvent e ) {
        if( !GUI.getInstance( ).mouseClickedInHud( e ) )
            game.getActionManager( ).mouseClicked( e );
    }
    

    /**
     * Triggers the given mouse event.
     * @param e Mouse event
     */
    public void mouseMovedEvent( MouseEvent e ) {
        game.getActionManager( ).setExitCustomized( null, null );
        game.getActionManager( ).setElementOver( null );
        if( !GUI.getInstance( ).mouseMovedinHud( e ) )
            game.getActionManager( ).mouseMoved( e );
    }

    private void mouseReleasedEvent(MouseEvent e) {
        GUI.getInstance( ).mouseReleasedinHud( e );
	}

	private void mousePressedEvent(MouseEvent e) {
		GUI.getInstance().mousePressedinHud( e );
	}
	
	private void mouseDraggedEvent(MouseEvent e) {
		GUI.getInstance().mouseDraggedinHud( e );
	}


    @Override
    public void keyPressed( KeyEvent e ) {
        switch( e.getKeyCode( ) ) {
            case KeyEvent.VK_ESCAPE:
                game.setState( Game.STATE_OPTIONS );
                break;
        }
    }
}