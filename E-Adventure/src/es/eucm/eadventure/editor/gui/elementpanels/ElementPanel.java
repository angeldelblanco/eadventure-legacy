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
package es.eucm.eadventure.editor.gui.elementpanels;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import es.eucm.eadventure.editor.control.controllers.Searchable;
import es.eucm.eadventure.editor.gui.DataControlsPanel;
import es.eucm.eadventure.editor.gui.Updateable;
import es.eucm.eadventure.editor.gui.structurepanel.StructureControl;

/**
 * Class that extends JTabbedPane with new functionality, used to place
 * independent JComponents in each tab that are updates every time they are
 * visited as well as when it is requested by the system.<br>
 * This class also implements the DataControlsPanel interface, allowing it to
 * change the selected tab depending on a DataControl.
 * 
 * @author Eugenio Marchiori
 */
public class ElementPanel extends JTabbedPane implements Updateable, DataControlsPanel {

    private static final long serialVersionUID = 1546563540388226634L;

    /**
     * The list of PanelTabs
     */
    private List<PanelTab> tabs;

    /**
     * The JComponent of the tab being shown
     */
    private JComponent component = null;

    /**
     * The index of the selected tab
     */
    private int selected = -1;

    /**
     * Constructor
     */
    public ElementPanel( ) {

        super( );
        tabs = new ArrayList<PanelTab>( );
        this.addChangeListener( new ElementPanelTabChangeListener( ) );
        this.setFocusable( false );
    }

    /**
     * Add a new PanelTab to the panel
     * 
     * @param tab
     *            The new PanelTab element
     */
    public void addTab( PanelTab tab ) {

        tabs.add( tab );
        JPanel panel = new JPanel( );
        panel.setLayout( new BorderLayout( ) );
        if( tab.getToolTipText( ) != null )
            this.addTab( tab.getTitle( ), tab.getIcon( ), panel, tab.getToolTipText( ) );
        else
            this.addTab( tab.getTitle( ), tab.getIcon( ), panel );
    }

    public boolean updateFields( ) {

        boolean update = false;
        update = tabs.get( this.getSelectedIndex( ) ).updateFields( );
        if( !update ) {
            ( (JPanel) getSelectedComponent( ) ).removeAll( );
            PanelTab tab = tabs.get( getSelectedIndex( ) );
            ( (JPanel) getSelectedComponent( ) ).add( tab.getComponent( ), BorderLayout.CENTER );
            ( (JPanel) getSelectedComponent( ) ).updateUI( );
        }
        System.out.println( "Element Panel UPDATED" );
        return true;
    }

    public void setSelectedItem( List<Searchable> path ) {

        if( path.size( ) > 0 ) {
            for( int i = 0; i < tabs.size( ); i++ ) {
                if( tabs.get( i ).getDataControl( ) == path.get( path.size( ) - 1 ) ) {
                    path.remove( path.size( ) - 1 );
                    this.setSelectedIndex( i );
                    break;
                }
            }
        }
        if( path.size( ) > 0 ) {
            if( component != null ) {
                if( component instanceof DataControlsPanel ) {
                    ( (DataControlsPanel) component ).setSelectedItem( path );
                    path = null;
                }
            }
        }

    }

    /**
     * Private class representing the change listener for the changes in the
     * selected tab of the pane.
     */
    private class ElementPanelTabChangeListener implements ChangeListener {

        public void stateChanged( final ChangeEvent arg0 ) {

            if( selected != getSelectedIndex( ) ) {
                setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
                if( selected >= 0 )
                    ElementPanel.this.setTabComponentAt( selected, new JLabel( tabs.get( selected ).getTitle( ), tabs.get( selected ).getIcon( ), SwingConstants.LEFT ) );
                selected = getSelectedIndex( );
                ( (JPanel) getSelectedComponent( ) ).removeAll( );
                PanelTab tab = tabs.get( getSelectedIndex( ) );
                ElementPanel.this.setTabComponentAt( selected, tab.getTab( ) );
                StructureControl.getInstance( ).visitDataControl( tab.getDataControl( ) );
                component = tab.getComponent( );
                ( (JPanel) getSelectedComponent( ) ).add( component, BorderLayout.CENTER );
                ( (JPanel) getSelectedComponent( ) ).updateUI( );
                SwingUtilities.invokeLater( new Runnable( ) {

                    public void run( ) {

                        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                    }
                } );

            }
        }
    }
}