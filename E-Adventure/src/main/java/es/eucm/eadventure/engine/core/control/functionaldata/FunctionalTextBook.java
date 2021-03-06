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
package es.eucm.eadventure.engine.core.control.functionaldata;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.util.ArrayList;

import es.eucm.eadventure.common.data.chapter.book.Book;
import es.eucm.eadventure.common.data.chapter.book.BookParagraph;
import es.eucm.eadventure.engine.core.gui.GUI;

public class FunctionalTextBook extends FunctionalBook {

    /**
     * X position of the first column of text
     */
    public static final int TEXT_X_1 = 110;

    /**
     * X position for the second column of text
     */
    public static final int TEXT_X_2 = 445;

    /**
     * Y position for both columns of text
     */
    public static final int TEXT_Y = 75;

    /**
     * Width of each column of text
     */
    public static final int TEXT_WIDTH = 250;

    /**
     * Width of each column of the bullet text
     */
    public static final int TEXT_WIDTH_BULLET = 225;

    /**
     * Height of each column of text
     */
    public static final int PAGE_TEXT_HEIGHT = 400;

    /**
     * Height of each line of text
     */
    public static final int LINE_HEIGHT = 25;

    /**
     * Height of each line of a title
     */
    public static final int TITLE_HEIGHT = 50;

    /**
     * Number of lines per column
     */
    public static final int TEXT_LINES = PAGE_TEXT_HEIGHT / LINE_HEIGHT;

    /**
     * Image of the book.
     */
    protected Image imgBook;
    
    /**
     * Background image of the book
     */
    protected Image background;

    /**
     * Total height of the book.
     */
    private int totalHeight;

    /**
     * List of functional paragraphs.
     */
    private ArrayList<FunctionalBookParagraph> functionalParagraphs;

    /**
     * Creates a new FunctionalBook
     * 
     * @param book
     *            the book's data
     */
    public FunctionalTextBook( Book b ) {
        super( b );

        // Create the list of paragraphs and add it
        functionalParagraphs = new ArrayList<FunctionalBookParagraph>( );
        for( BookParagraph paragraph : book.getParagraphs( ) ) {
            switch( paragraph.getType( ) ) {
                case BookParagraph.TITLE:
                    functionalParagraphs.add( new FunctionalBookTitle( paragraph ) );
                    break;
                case BookParagraph.BULLET:
                    functionalParagraphs.add( new FunctionalBookBullet( paragraph ) );
                    break;
                case BookParagraph.IMAGE:
                    functionalParagraphs.add( new FunctionalBookImage( paragraph ) );
                    break;
                case BookParagraph.TEXT:
                    functionalParagraphs.add( new FunctionalBookText( paragraph ) );
                    break;
            }
        }

        // Calculate the height of the book and the number of pages
        totalHeight = 0;
        currentPage = 0;
        for( FunctionalBookParagraph functionalParagraph : functionalParagraphs )
            totalHeight += functionalParagraph.getHeight( );
        numPages = (int) Math.ceil( (double) totalHeight / (double) PAGE_TEXT_HEIGHT );

        // Create the image of the book and extract the graphics
        int y = 0;
        imgBook = GUI.getInstance( ).getGraphicsConfiguration( ).createCompatibleImage( TEXT_WIDTH, totalHeight + PAGE_TEXT_HEIGHT, Transparency.TRANSLUCENT );
        Graphics2D g = (Graphics2D) imgBook.getGraphics( );
        g.setFont( new Font( "Dialog", Font.PLAIN, 18 ) );
        g.setColor( Color.DARK_GRAY );

        // Paint each paragraph in the image
        for( FunctionalBookParagraph functionalParagraph : functionalParagraphs ) {

            // If the paragraph can't be splitted and doesn't fit in the current page, jump to the next one
            if( !functionalParagraph.canBeSplitted( ) && ( y % PAGE_TEXT_HEIGHT ) + functionalParagraph.getHeight( ) > PAGE_TEXT_HEIGHT )
                y += ( PAGE_TEXT_HEIGHT - ( y % PAGE_TEXT_HEIGHT ) );

            // Paint the entire paragraph
            functionalParagraph.draw( g, 0, y );
            y += functionalParagraph.getHeight( );
        }
        g.dispose( );
    }

    /**
     * Returns whether the book is in its last page
     * 
     * @return true if the book is in its last page, false otherwise
     */
    @Override
    public boolean isInLastPage( ) {

        return ( currentPage == numPages - 1 || currentPage == numPages - 2 );
    }
    
    @Override
    public boolean isInFirstPage( ) {
        
        return ( currentPage == 0 || currentPage == 1 );
    }

    /**
     * Displays the next page
     */
    @Override
    public void nextPage( ) {

        // Jump two pages (each book has to columns)
        if( currentPage < numPages - 2 ) {
            currentPage += 2;
        }
    }

    /**
     * Displays the previous page
     */
    @Override
    public void previousPage( ) {

        // Jump two pages (each book has to columns)
        if( currentPage > 1 ) {
            currentPage -= 2;
        }
    }

    /**
     * Draws the text of the book
     * 
     * @param g
     *            the graphics where the book must be painted
     */
    public void draw( Graphics2D g ) {
        
        super.draw( g );
        g.setColor( Color.DARK_GRAY );

        // Draw the first page
        g.drawImage( imgBook, TEXT_X_1, TEXT_Y, TEXT_X_1 + TEXT_WIDTH, TEXT_Y + PAGE_TEXT_HEIGHT, 0, currentPage * PAGE_TEXT_HEIGHT, TEXT_WIDTH, ( currentPage + 1 ) * PAGE_TEXT_HEIGHT, null );

        // If there is second page, draw it
        if( currentPage < numPages - 1 ) {
            g.drawImage( imgBook, TEXT_X_2, TEXT_Y, TEXT_X_2 + TEXT_WIDTH, TEXT_Y + PAGE_TEXT_HEIGHT, 0, ( currentPage + 1 ) * PAGE_TEXT_HEIGHT, TEXT_WIDTH, ( currentPage + 2 ) * PAGE_TEXT_HEIGHT, null );
        }
    }
}
