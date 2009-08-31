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
package es.eucm.eadventure.editor.gui.elementpanels.general;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.control.config.ResizeableListConfigData;
import es.eucm.eadventure.editor.control.controllers.DataControl;
import es.eucm.eadventure.editor.gui.elementpanels.general.tables.ResizeableCellRenderer;
import es.eucm.eadventure.editor.gui.elementpanels.general.tables.ResizableTableModel;
import es.eucm.eadventure.editor.gui.structurepanel.StructureControl;

public class ResizeableListPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private ResizeableCellRenderer renderer;

    private ResizableTableModel model;

    private JTable informationTable;

    private String name = "";

    private List<DataControl> dataControlList;

    public ResizeableListPanel( List<DataControl> dataControlList, ResizeableCellRenderer renderer, String name ) {

        this.dataControlList = dataControlList;
        this.renderer = renderer;
        this.name = name;
        createPanel( );
    }

    private void createPanel( ) {

        // Set the layout and the border
        setLayout( new BorderLayout( ) );
        JPanel buttonsPanel = new JPanel( );
        JButton sizeZero = new JButton( new ImageIcon( "img/icons/size0.png" ) );
        sizeZero.setToolTipText( TextConstants.getText( "ResizeableListPanel.Size0ToolTip" ) );
        sizeZero.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent arg0 ) {

                changeSize( 0 );
            }
        } );
        buttonsPanel.add( sizeZero );

        JButton sizeOne = new JButton( new ImageIcon( "img/icons/size1.png" ) );
        sizeOne.setToolTipText( TextConstants.getText( "ResizeableListPanel.Size1ToolTip" ) );
        sizeOne.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent arg0 ) {

                changeSize( 1 );
            }
        } );
        buttonsPanel.add( sizeOne );

        JButton sizeTwo = new JButton( new ImageIcon( "img/icons/size2.png" ) );
        sizeTwo.setToolTipText( TextConstants.getText( "ResizeableListPanel.Size2ToolTip" ) );
        sizeTwo.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent arg0 ) {

                changeSize( 2 );
            }
        } );
        buttonsPanel.add( sizeTwo );

        add( buttonsPanel, BorderLayout.NORTH );

        int size = ResizeableListConfigData.getSize( name );
        model = new ResizableTableModel( dataControlList, size );
        renderer.setSize( size );
        informationTable = new JTable( model );
        informationTable.addMouseListener( new InformationTableMouseListener( ) );

        for( int i = 0; i < model.getColumnCount( ); i++ ) {
            informationTable.getColumnModel( ).getColumn( i ).setCellRenderer( renderer );
            informationTable.getColumnModel( ).getColumn( i ).setCellEditor( renderer );
        }
        if( size == 0 )
            informationTable.setRowHeight( 20 );
        if( size == 1 )
            informationTable.setRowHeight( 100 );
        if( size == 2 )
            informationTable.setRowHeight( 200 );
        informationTable.setTableHeader( null );

        add( new JScrollPane( informationTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER ), BorderLayout.CENTER );
    }

    private class InformationTableMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked( MouseEvent e ) {

            if( e.getClickCount( ) == 2 ) {
                JTable table = (JTable) e.getSource( );
                int row = table.rowAtPoint( e.getPoint( ) );
                int column = table.columnAtPoint( e.getPoint( ) );
                int size = 1;
                if( renderer.getSize( ) == 1 )
                    size = 4;
                else if( renderer.getSize( ) == 2 )
                    size = 2;
                int index = row * size + column;
                if( index < dataControlList.size( ) ) {
                    DataControl dataControl = dataControlList.get( index );
                    StructureControl.getInstance( ).changeDataControl( dataControl );
                }
            }
        }
    }

    private void changeSize( int size ) {

        renderer.setSize( size );
        model.setSize( size );
        model.fireTableStructureChanged( );
        for( int i = 0; i < model.getColumnCount( ); i++ ) {
            informationTable.getColumnModel( ).getColumn( i ).setCellRenderer( renderer );
            informationTable.getColumnModel( ).getColumn( i ).setCellEditor( renderer );
        }
        if( size == 0 )
            informationTable.setRowHeight( 20 );
        if( size == 1 )
            informationTable.setRowHeight( 100 );
        if( size == 2 )
            informationTable.setRowHeight( 200 );
        ResizeableListConfigData.setSize( name, size );
    }
}
