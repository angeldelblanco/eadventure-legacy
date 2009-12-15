/**
 * <e-Adventure> is an <e-UCM> research project. <e-UCM>, Department of Software
 * Engineering and Artificial Intelligence. Faculty of Informatics, Complutense
 * University of Madrid (Spain).
 * 
 * @author Del Blanco, A., Marchiori, E., Torrente, F.J. (alphabetical order) *
 * @author L�pez Ma�as, E., P�rez Padilla, F., Sollet, E., Torijano, B. (former
 *         developers by alphabetical order)
 * @author Moreno-Ger, P. & Fernández-Manjón, B. (directors)
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
package es.eucm.eadventure.engine.core.control.functionaldata;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import es.eucm.eadventure.common.data.chapter.book.BookParagraph;
import es.eucm.eadventure.engine.core.gui.GUI;

/**
 * This is a block of text that can be put in a book scene
 */
public class FunctionalBookTitle extends FunctionalBookParagraph {

    /**
     * The text book
     */
    private BookParagraph bookTitle;

    /**
     * The text of the book
     */
    private ArrayList<String> textLines;
    
    /**
     * Extra height necessary for drawing the book
     */
    private int extraHeight = 0;

    /**
     * Creates a new FunctionalBookText
     * 
     * @param text
     *            the text to be rendered
     */
    public FunctionalBookTitle( BookParagraph title ) {

        this.bookTitle = title;
        this.init( );
    }

    private void init( ) {

        textLines = new ArrayList<String>( );

        //Get the text of the book
        String text = bookTitle.getContent( );
        String word = "";
        String line = "";

        Font font = GUI.getInstance( ).getFrame( ).getFont( ).deriveFont( Font.PLAIN, 30 );

        //while there is still text to be process
        while( !text.equals( "" ) ) {
            //get the first char
            char c = text.charAt( 0 );
            //and the rest of the text (without that char)
            text = text.substring( 1 );
            //If the first char is a new line
            if( c == '\n' ) {
                Rectangle2D r = font.getStringBounds( line + " " + word, new FontRenderContext( null, false, true ) );
                if( r.getWidth( ) < FunctionalTextBook.TEXT_WIDTH ) {
                    //finish the line with the current word
                    line = line + word;
                    //add the line to the text of the bullet book
                    textLines.add( line );
                    //empy line and word
                    word = "";
                    line = "";
                }
                else {
                    textLines.add( line );
                    textLines.add( word.substring( 1 ) );
                    line = "";
                    word = "";
                }
            }
            //if its a white space
            else if( Character.isWhitespace( c ) ) {
                //get the width of the line and the word
                Rectangle2D r = font.getStringBounds( line + " " + word, new FontRenderContext( null, false, true ) );
                //if its width size don't go out of the line text width
                if( r.getWidth( ) < FunctionalTextBook.TEXT_WIDTH ) {
                    //add the word to the line
                    line = line + word;
                    word = " ";
                }
                //if it goes out
                else {
                    //and the line to the text of the bullet book
                    textLines.add( line );
                    //the line is now the word
                    line = word.substring( 1 ) + " ";
                    word = "";
                }
            }
            //else we add it to the current word
            else {
                word = word + c;
            }
        }
        //All the text has been process except the last line and last word
        Rectangle2D r = font.getStringBounds( line + " " + word, new FontRenderContext( null, false, true ) );
        if( r.getWidth( ) < FunctionalTextBook.TEXT_WIDTH ) {
            line = line + word;
        }
        else {
            textLines.add( line );
            line = word.substring( 1 );
        }
        textLines.add( line );
    }

    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.core.control.functionaldata.FunctionalBookParagraph#canBeSplitted()
     */
    @Override
    public boolean canBeSplitted( ) {

        return true;
    }

    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.core.control.functionaldata.FunctionalBookParagraph#draw(java.awt.Graphics2D, int, int)
     */
    @Override
    public void draw( Graphics2D g, int xIni, int yIni ) {

        //X and Y coordinates
        int x = xIni;
        int y = yIni;
        //for each line of the text book
        for( int i = 0; i < textLines.size( ); i++ ) {
            //draw the line string
            String line = textLines.get( i );
            
            // If the line doesn't fit, we change the line and add extra height
            // to paragraph height
            if ( FunctionalTextBook.PAGE_TEXT_HEIGHT - ( y % FunctionalTextBook.PAGE_TEXT_HEIGHT ) < FunctionalTextBook.TITLE_HEIGHT ){
                extraHeight += ( FunctionalTextBook.PAGE_TEXT_HEIGHT - ( y % FunctionalTextBook.PAGE_TEXT_HEIGHT ) );
                y += ( FunctionalTextBook.PAGE_TEXT_HEIGHT - ( y % FunctionalTextBook.PAGE_TEXT_HEIGHT ) );
            }

            // TODO ¿parche?
            Font font = g.getFont( );
            g.setFont( font.deriveFont( Font.PLAIN, 30 ) );
            g.drawString( line, x, y + FunctionalTextBook.TITLE_HEIGHT - 15 );
            g.setFont( font );

            //add the line height to the Y coordinate for the next line
            y = y + FunctionalTextBook.TITLE_HEIGHT;

            /*if( i == 0 + FunctionalBook.TEXT_LINES ) {
                x = FunctionalBook.TEXT_X_2;
                y = FunctionalBook.TEXT_Y;
            }*/
        }
    }

    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.core.control.functionaldata.FunctionalBookParagraph#getHeight()
     */
    @Override
    public int getHeight( ) {

        return textLines.size( ) * FunctionalTextBook.TITLE_HEIGHT + extraHeight;
    }

}
