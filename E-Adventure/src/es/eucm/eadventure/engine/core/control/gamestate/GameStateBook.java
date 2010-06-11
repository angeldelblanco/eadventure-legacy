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
package es.eucm.eadventure.engine.core.control.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import es.eucm.eadventure.common.data.chapter.book.Book;
import es.eucm.eadventure.common.data.chapter.effects.Effects;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalBook;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalStyledBook;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalTextBook;
import es.eucm.eadventure.engine.core.control.functionaldata.functionaleffects.FunctionalEffects;
import es.eucm.eadventure.engine.core.gui.GUI;

/**
 * A game main loop when a "bookscene" is being displayed
 */
public class GameStateBook extends GameState {

    /**
     * Functional book to be displayed
     */
    private FunctionalBook book;

    /**
     * Creates a new GameStateBook
     */
    public GameStateBook( ) {

        super( );
        if( game.getBook( ).getType( ) == Book.TYPE_PARAGRAPHS ) {
            //System.out.println( "[LOG] GameStateBook - Constructor - Paragraphs Book" );
            book = new FunctionalTextBook( game.getBook( ) );
        }
        else if( game.getBook( ).getType( ) == Book.TYPE_PAGES ) {
            //System.out.println( "[LOG] GameStateBook - Constructor - Pages Book" );
            book = new FunctionalStyledBook( game.getBook( ) );
        }
        
    }

    /*
     * (non-Javadoc)
     * @see es.eucm.eadventure.engine.engine.control.gamestate.GameState#EvilLoop(long, int)
     */
    @Override
    public void mainLoop( long elapsedTime, int fps ) {
        if( book.getBook( ).getType( ) == Book.TYPE_PARAGRAPHS ) {
            Graphics2D g = GUI.getInstance( ).getGraphics( );
            g.clearRect( 0, 0, GUI.WINDOW_WIDTH, GUI.WINDOW_HEIGHT );

            ( (FunctionalTextBook) book ).draw( g );

            g.setColor( Color.WHITE );
            //g.drawString(Integer.toString( fps ), 780, 14);

            GUI.getInstance( ).endDraw( );

            g.dispose( );
        }
    }

       
    @Override
    public void mouseClicked( MouseEvent e ) {

        //System.out.println( "MOUSE CLICKED" );
        // Left click changes the page
        if( e.getButton( ) == MouseEvent.BUTTON1 ) {
            if( book.isInPreviousPage( e.getX( ), e.getY( ) ) )
                book.previousPage( );

            else if( book.isInNextPage( e.getX( ), e.getY( ) ) ) {

                if( book.isInLastPage( ) ) {
                    GUI.getInstance( ).restoreFrame( );
                    // this method also change the state to run effects
                    FunctionalEffects.storeAllEffects( new Effects( ) );
                    //game.setState( Game.STATE_RUN_EFFECTS );
                }
                else
                    book.nextPage( );
            }
        }

        // Right click ends the book
        else if( e.getButton( ) == MouseEvent.BUTTON3 ) {
            GUI.getInstance( ).restoreFrame( );
            FunctionalEffects.storeAllEffects( new Effects( ) );
            //game.setState( Game.STATE_RUN_EFFECTS );
        }
    }
    
    @Override
    public void mouseMoved( MouseEvent e ){
        boolean mouseOverPreviousPage = book.isInPreviousPage( e.getX( ), e.getY( ) );
        book.mouseOverPreviousPage( mouseOverPreviousPage );
        
        boolean mouseOverNextPage = book.isInNextPage( e.getX( ), e.getY( ) );
        book.mouseOverNextPage( mouseOverNextPage );
    }
    
}
