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
package es.eucm.eadventure.editor.gui.otherpanels.bookpanels;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

import es.eucm.eadventure.common.data.chapter.book.BookPage;
import es.eucm.eadventure.common.gui.BookEditorPane;
import es.eucm.eadventure.editor.control.controllers.AssetsController;
import es.eucm.eadventure.editor.control.controllers.book.BookDataControl;
import es.eucm.eadventure.editor.gui.elementpanels.book.PagesTable;

/**
 * Class for the preview of HTML books in Content tab.
 * 
 * @author �ngel S.
 * 
 */
public class BookPagePreviewPanel extends BookPreviewPanel {

    /**
     * Required
     */
    private static final long serialVersionUID = 1L;

    private boolean isValid;

    private BookEditorPane editorPane;

    /**
     * Image if the page is the type Image
     */
    private Image imagePage;

    /**
     * Current state for arrows
     */
    protected Image currentArrowLeft, currentArrowRight;

    /**
     * Current book page
     */
    private BookPage currentBookPage;

    /**
     * Index for the page
     */
    private int pageIndex;

    /**
     * List of pages
     */
    private List<BookPage> bookPageList;

    /**
     * Mouse listener
     */
    private BookPageMouseListener mouseListener;

    private boolean drawArrows = true;

    private PagesTable pagesTable;

    public BookPagePreviewPanel( BookDataControl dControl, boolean previewPanel ) {

        super( dControl );
        this.setOpaque( false );
        isValid = true;
        bookPageList = dControl.getBookPagesList( ).getBookPages( );
        super.loadImages( dControl );

        mouseListener = new BookPageMouseListener( );
        this.addMouseListener( mouseListener );
        this.addMouseMotionListener( mouseListener );

        currentArrowLeft = arrowLeftNormal;
        currentArrowRight = arrowRightNormal;
    }
    
    /**
     * Constructor which includes the pages table. When a page is changed, table
     * changes its index.
     * @param dControl DataController
     * @param previewPanel If it's a preview panel.
     * @param pages Pages table.
     */
    public BookPagePreviewPanel( BookDataControl dControl, boolean previewPanel, PagesTable pages ){
        this( dControl, previewPanel );
        this.pagesTable = pages;
    }

    /**
     * Constructor for the class
     * 
     * @param dControl
     *            Book data control
     * @param initPage
     *            initial page to display in the book
     */
    public BookPagePreviewPanel( BookDataControl dControl, boolean preview, int initPage ) {

        this( dControl, preview );

        setCurrentBookPage( initPage );
    }
    
    public BookEditorPane getBookEditorPane( ){
        return editorPane;
    }

    /**
     * Set the current page of the using its index
     * 
     * @param numPage
     *            Number of page
     * @return true if it was possible to set the page, false otherwise
     */
    public boolean setCurrentBookPage( int numPage ) {

        try {
            currentBookPage = bookPageList.get( numPage );
            pageIndex = numPage;
        }
        catch( Exception e ) {
            // We catch all the exceptions that can happen in the command. Only thing counting
            // is if currentBookPage is null or not
            return false;
        }

        if( currentBookPage != null ) {
            return setCurrentBookPage( currentBookPage );
        }
        else
            return false;
    }

    public boolean setCurrentBookPage( BookPage bookPage ){
        return setCurrentBookPage( bookPage, false );
    }
    
    /**
     * Set the current page of the using the page itself
     * 
     * @param bookPage
     *            The book page.
     * @return true if it was possible to set the page, false otherwise
     */
    public boolean setCurrentBookPage( BookPage bookPage, boolean export ) {
        
        currentBookPage = bookPage;
        if( currentBookPage != null ) {

            if( currentBookPage.getType( ) == BookPage.TYPE_URL ) {
                isValid = createURLPage( currentBookPage );
                imagePage = null;
            }
            else if( currentBookPage.getType( ) == BookPage.TYPE_RESOURCE ) {
                isValid = createResourcePage( currentBookPage, export );
                imagePage = null;
            }

            else if( currentBookPage.getType( ) == BookPage.TYPE_IMAGE ) {
                isValid = createImagePage( currentBookPage );
            }

            if( editorPane != null ) {
                //addEditorPane( );
                repaint( );
            }
            
            if ( isValid && pagesTable != null ){
                pagesTable.changeSelection( pageIndex, 0, false, false );
            }
            return isValid;
        }
        else
            return false;

    }

    public void setDrawArrows( boolean drawArrows ) {

        this.drawArrows = drawArrows;
    }

    private boolean createImagePage( BookPage bookPage ) {

        if( bookPage.getUri( ) != null && bookPage.getUri( ).length( ) > 0 )
            imagePage = AssetsController.getImage( bookPage.getUri( ) );
        isValid = ( imagePage != null );
        return isValid;
    }

    private boolean createResourcePage( BookPage bookPage, boolean export ) {

        editorPane = new BookEditorPaneEditor( currentBookPage );
        ( (BookEditorPaneEditor) editorPane ).setExport( export );

        URL url = AssetsController.getResourceAsURLFromZip( bookPage.getUri( ) );
        String ext = url.getFile( ).substring( url.getFile( ).lastIndexOf( '.' ) + 1, url.getFile( ).length( ) ).toLowerCase( );
        if( ext.equals( "html" ) || ext.equals( "htm" ) || ext.equals( "rtf" ) ) {

            //Read the text
            StringBuffer textBuffer = new StringBuffer( );
            InputStream is = null;
            try {
                is = url.openStream( );
                int c;
                while( ( c = is.read( ) ) != -1 ) {
                    textBuffer.append( (char) c );
                }
            }
            catch( IOException e ) {
                isValid = false;
            }
            finally {
                if( is != null ) {
                    try {
                        is.close( );
                    }
                    catch( IOException e ) {
                        isValid = false;
                    }
                }
            }

            //Set the proper content type
            if( ext.equals( "html" ) || ext.equals( "htm" ) ) {
                editorPane.setContentType( "text/html" );
                //ProcessHTML processor = new ProcessHTML( textBuffer.toString( ) );
                //String htmlProcessed = processor.start( );
                editorPane.setText( textBuffer.toString( ) );

                //String fileName = url.getPath( ).substring( 0, url.getPath( ).lastIndexOf( "/" ) );
                //File f = new File( url.getFile( ) );

                try {
                    editorPane.setDocumentBase( new URL( url.getProtocol( ), url.getHost( ), url.getPort( ), url.getFile( ) ) );
                }
                catch( MalformedURLException e ) {
                    e.printStackTrace( );
                }
            }
            else {
                editorPane.setContentType( "text/rtf" );
                editorPane.setText( textBuffer.toString( ) );
            }
            isValid = true;

        }
        return isValid;
    }

    private boolean createURLPage( BookPage bookPage ) {

        URL url = null;
        editorPane = new BookEditorPane(  );

        try {
            url = new URL( bookPage.getUri( ) );
            url.openStream( ).close( );
        }
        catch( Exception e ) {
            isValid = false;
        }

        try {
            if( isValid ) {
                editorPane.setPage( url );
                editorPane.setEditable( false );
                if( !( editorPane.getEditorKit( ) instanceof HTMLEditorKit ) && !( editorPane.getEditorKit( ) instanceof RTFEditorKit ) ) {
                    isValid = false;
                }
                else
                    isValid = true;
            }
        }
        catch( IOException e ) {
        }
        return isValid;
    }

    // TODO Borrar si no se necesita en el futuro
    /*private void addEditorPane( ) {

         editorPane.setOpaque( false );
         //editorPane.setCaret( null );
         editorPane.setEditable( false );
         //editorPane.setHighlighter( null );
         editorPane.addMouseListener( mouseListener );
         editorPane.addMouseMotionListener( mouseListener );

         this.setOpaque( false );

         //this.setLayout( new BoxLayout(this, BoxLayout.LINE_AXIS) );
         this.setLayout( null );
         if( !currentBookPage.getScrollable( ) ) {
             editorPane.setBounds( currentBookPage.getMargin( ), currentBookPage.getMarginTop( ), GUI.WINDOW_WIDTH - currentBookPage.getMargin( ) - currentBookPage.getMarginEnd( ), GUI.WINDOW_HEIGHT - currentBookPage.getMarginTop( ) - currentBookPage.getMarginBottom( ) );
             this.add( editorPane );
         }
         else {
             JPanel viewPort = new JPanel( ) {

                 private static final long serialVersionUID = 1L;

                 @Override
                 public void paint( Graphics g ) {

                     if( image != null )
                         g.drawImage( image, 0, 0, image.getWidth( null ), image.getHeight( null ), null );
                     super.paint( g );
                 }
             };
             viewPort.setLayout( new BorderLayout( ) );
             viewPort.setOpaque( false );

             editorPane.setBounds( currentBookPage.getMargin( ), currentBookPage.getMarginTop( ), GUI.WINDOW_WIDTH - currentBookPage.getMargin( ) - currentBookPage.getMarginEnd( ), GUI.WINDOW_HEIGHT - currentBookPage.getMarginTop( ) - currentBookPage.getMarginBottom( ) );

             viewPort.add( editorPane, BorderLayout.CENTER );
             JScrollPane scroll = new JScrollPane( viewPort, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
             scroll.getViewport( ).setOpaque( false );
             scroll.getViewport( ).setBorder( null );
             scroll.setOpaque( false );
             this.add( scroll );
         }
     }*/

    /**
     * @return the bookPage
     */
    public BookPage getCurrentBookPage( ) {

        return currentBookPage;
    }

    /**
     * @return the isValid
     */
    @Override
    public boolean isValid( ) {

        return isValid;
    }

    /**
     * @param isValid
     *            the isValid to set
     */
    public void setValid( boolean isValid ) {

        this.isValid = isValid;
    }

    @Override
    public void paint( Graphics g ) {
        if ( bookPageList.size( ) == 0 )
            currentBookPage = null;
        
        if( currentBookPage != null && isImageLoaded( ) ) {
            // Paint the background
            g.drawImage( image, getAbsoluteX( 0 ), getAbsoluteY( 0 ), width, height, null );
            if( imagePage != null ) {
                g.drawImage( imagePage, getAbsoluteX( currentBookPage.getMargin( ) ), getAbsoluteY( currentBookPage.getMarginTop( ) ), getAbsoluteWidth( imagePage.getWidth( null ) ), getAbsoluteHeight( imagePage.getHeight( null ) ), null );
            }
            // Paint editorPane
            else if( editorPane != null ){
                int xPane = getAbsoluteX( currentBookPage.getMargin( ) );
                int yPane = getAbsoluteY( currentBookPage.getMarginTop( ) );
                int widthPane = width - getAbsoluteWidth( currentBookPage.getMarginEnd( ) );
                int heightPane = height - getAbsoluteHeight( currentBookPage.getMarginBottom( ) );
                editorPane.paint( g, xPane, yPane, widthPane, heightPane );
            }

            if( drawArrows ) {

                if( !isInFirstPage( ) )
                    if( currentArrowLeft != null ) {
                        g.drawImage( currentArrowLeft, getAbsoluteX( previousPagePoint.x ), getAbsoluteY( previousPagePoint.y ), getAbsoluteWidth( arrowLeftNormal.getWidth( null ) ), getAbsoluteHeight( arrowLeftNormal.getHeight( null ) ), null );
                    }

                if( !isInLastPage( ) )
                    if( currentArrowRight != null ) {
                        g.drawImage( currentArrowRight, getAbsoluteX( nextPagePoint.x ), getAbsoluteY( nextPagePoint.y ), getAbsoluteWidth( arrowRightNormal.getWidth( null ) ), getAbsoluteHeight( arrowRightNormal.getHeight( null ) ), null );
                    }
            }
        }

    }

    public void nextPage( ) {

        if( !isInLastPage( ) ) {
            pageIndex++;
            this.setCurrentBookPage( pageIndex );
        }
    }

    public void previousPage( ) {

        if( !isInFirstPage( ) ) {
            pageIndex--;
            this.setCurrentBookPage( pageIndex );
        }
    }

    public boolean isInFirstPage( ) {

        return ( pageIndex == 0 );
    }

    public boolean isInLastPage( ) {

        return ( pageIndex == bookPageList.size( ) - 1 );
    }

    @Override
    protected boolean isInNextPage( int x, int y ) {

        return super.isInNextPage( x, y );
    }

    @Override
    protected boolean isInPreviousPage( int x, int y ) {

        return super.isInPreviousPage( x, y );
    }

    private class BookPageMouseListener extends MouseAdapter implements MouseMotionListener {

        @Override
        public void mouseClicked( MouseEvent evt ) {

            int x = evt.getX( );
            int y = evt.getY( );

            // In case event happened in editor pane, we have to add
            // page's margins
            if( evt.getSource( ) == editorPane ) {
                x += currentBookPage.getMarginStart( );
                y += currentBookPage.getMarginTop( );
            }

            if( isInNextPage( x, y ) ) {
                nextPage( );
            }
            else if( isInPreviousPage( x, y ) ) {
                previousPage( );
            }
            repaint( );
        }

        @Override
        public void mouseMoved( MouseEvent evt ) {

            int x = evt.getX( );
            int y = evt.getY( );

            if( isInPreviousPage( x, y ) ) {
                currentArrowLeft = arrowLeftOver;
            }
            else
                currentArrowLeft = arrowLeftNormal;

            if( isInNextPage( x, y ) ) {
                currentArrowRight = arrowRightOver;
            }
            else
                currentArrowRight = arrowRightNormal;

            repaint( );
        }

    }

}
