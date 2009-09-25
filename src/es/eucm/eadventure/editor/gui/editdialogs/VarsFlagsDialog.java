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

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.VarFlagsController;
import es.eucm.eadventure.editor.gui.elementpanels.condition.ConditionsPanel;

/**
 * This class is the editing dialog of the flags. Here the user can add new
 * flags to use them in the script. Also, the flags can be deleted.
 * 
 * @author Bruno Torijano Bueno
 */
public class VarsFlagsDialog extends JDialog {

    /**
     * Required.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Path of .html file with help for flags
     */
    private static final String flagsHelpPath = "flagsVars/flags.html";

    /**
     * Path of .html file with help for vars
     */
    private static final String varsHelpPath = "flagsVars/vars.html";

    /**
     * Controller for the flags.
     */
    private VarFlagsController varFlagsController;

    /**
     * Table holding the flags.
     */
    private JTable flagsTable;

    /**
     * Table holding the vars.
     */
    private JTable varsTable;

    /**
     * Constructor.
     * 
     * @param flagController
     *            Controller for the flags
     */
    public VarsFlagsDialog( VarFlagsController flagController ) {

        // Call to the JDialog constructor
        super( Controller.getInstance( ).peekWindow( ), TC.get( "Ids.Title" ), Dialog.ModalityType.TOOLKIT_MODAL );

        // Push the dialog into the stack, and add the window listener to pop in when closing
        Controller.getInstance( ).pushWindow( this );
        addWindowListener( new WindowAdapter( ) {

            @Override
            public void windowClosing( WindowEvent e ) {

                Controller.getInstance( ).popWindow( );
            }
        } );

        // Set the flags controller
        this.varFlagsController = flagController;

        /////////////////////////////////////////
        // FLAGS PANEL (TAB)
        /////////////////////////////////////////
        // Create a container panel, and set the properties
        JPanel flagsMainPanel = new JPanel( );
        flagsMainPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TC.get( "Flags.Title" ) ) );
        flagsMainPanel.setLayout( new GridBagLayout( ) );
        GridBagConstraints c = new GridBagConstraints( );
        c.insets = new Insets( 5, 10, 5, 10 );

        // Create the table and add it
        flagsTable = new JTable( new FlagsTableModel( ) );
        flagsTable.getColumnModel( ).getColumn( 1 ).setMaxWidth( 60 );
        flagsTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        flagsMainPanel.add( new JScrollPane( flagsTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER ), c );

        // Add an "Add flag" button
        JButton addFlag = new JButton( TC.get( "Flags.AddFlag" ) );
        addFlag.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent e ) {

                if( varFlagsController.addVarFlag( true ) )
                    flagsTable.updateUI( );
            }
        } );
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        c.gridy = 1;
        flagsMainPanel.add( addFlag, c );

        // Add an "Delete flag" button
        JButton deleteFlag = new JButton( TC.get( "Flags.DeleteFlag" ) );
        deleteFlag.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent e ) {

                if( flagsTable.getSelectedRow( ) >= 0 )
                    if( varFlagsController.deleteFlag( flagsTable.getSelectedRow( ) ) )
                        flagsTable.updateUI( );
            }
        } );
        c.gridy = 2;
        flagsMainPanel.add( deleteFlag, c );

        /////////////////////////////////////////
        // VARS PANEL (TAB)
        /////////////////////////////////////////
        // Create a container panel, and set the properties
        JPanel varsMainPanel = new JPanel( );
        varsMainPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TC.get( "Vars.Title" ) ) );
        varsMainPanel.setLayout( new GridBagLayout( ) );
        c = new GridBagConstraints( );
        c.insets = new Insets( 5, 10, 5, 10 );

        // Create the table and add it
        varsTable = new JTable( new VarsTableModel( ) );
        varsTable.getColumnModel( ).getColumn( 1 ).setMaxWidth( 60 );
        varsTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        varsMainPanel.add( new JScrollPane( varsTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER ), c );

        // Add an "Add var" button
        JButton addVar = new JButton( TC.get( "Vars.AddVar" ) );
        addVar.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent e ) {

                if( varFlagsController.addVarFlag( false ) )
                    varsTable.updateUI( );
            }
        } );
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        c.gridy = 1;
        varsMainPanel.add( addVar, c );

        // Add an "Delete var" button
        JButton deleteVar = new JButton( TC.get( "Vars.DeleteVar" ) );
        deleteVar.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent e ) {

                if( varsTable.getSelectedRow( ) >= 0 )
                    if( varFlagsController.deleteVar( varsTable.getSelectedRow( ) ) )
                        varsTable.updateUI( );
            }
        } );
        c.gridy = 2;
        varsMainPanel.add( deleteVar, c );

        /////////////////////////////////////////
        // CREATE TABBED PANE
        /////////////////////////////////////////
        JTabbedPane mainPanel = new JTabbedPane( );
        mainPanel.addTab( TC.get( "Flags.Flag" ), null, flagsMainPanel, TC.get( "Flags.FlagTip" ) );
        mainPanel.addTab( TC.get( "Vars.Var" ), null, varsMainPanel, TC.get( "Vars.VarTip" ) );

        JPanel flagsTabComponent = new JPanel( );
        flagsTabComponent.setLayout( new GridBagLayout( ) );
        GridBagConstraints c1 = new GridBagConstraints( );
        c1.fill = GridBagConstraints.BOTH;
        c1.weightx = 1;
        c1.weighty = 1;
        JLabel flagsTitle = new JLabel( TC.get( "Flags.Flag" ) );
        flagsTitle.setHorizontalTextPosition( SwingConstants.CENTER );
        flagsTitle.setAlignmentX( 0.5f );
        flagsTitle.setHorizontalAlignment( SwingConstants.CENTER );
        ImageIcon flagsIcon = new ImageIcon( "img/icons/flag16.png" );
        flagsTabComponent.add( new JLabel( flagsIcon ), c1 );
        c1.gridx++;
        flagsTabComponent.add( flagsTitle, c1 );
        flagsTabComponent.setBackground( ConditionsPanel.FLAG_COLOR );
        mainPanel.setTabComponentAt( 0, flagsTabComponent );

        // button for html help for flags
        JButton flagsInfoButton = new JButton( new ImageIcon( "img/icons/information.png" ) );
        flagsInfoButton.setContentAreaFilled( false );
        flagsInfoButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        flagsInfoButton.setFocusable( false );
        flagsInfoButton.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent arg0 ) {

                new HelpDialog( flagsHelpPath );
            }
        } );
        c1.gridx++;
        flagsTabComponent.add( flagsInfoButton, c1 );

        JPanel varsTabComponent = new JPanel( );
        varsTabComponent.setLayout( new GridBagLayout( ) );
        GridBagConstraints c2 = new GridBagConstraints( );
        c2.fill = GridBagConstraints.BOTH;
        c2.weightx = 1;
        c2.weighty = 1;
        JLabel varsTitle = new JLabel( TC.get( "Vars.Var" ) );
        varsTitle.setHorizontalTextPosition( SwingConstants.CENTER );
        varsTitle.setAlignmentX( 0.5f );
        varsTitle.setHorizontalAlignment( SwingConstants.CENTER );
        ImageIcon varsIcon = new ImageIcon( "img/icons/var16.png" );
        varsTabComponent.add( new JLabel( varsIcon ), c2 );
        c2.gridx++;
        varsTabComponent.add( varsTitle, c2 );
        varsTabComponent.setBackground( ConditionsPanel.VAR_COLOR );
        mainPanel.setTabComponentAt( 1, varsTabComponent );

        // button for html help for vars
        JButton varsInfoButton = new JButton( new ImageIcon( "img/icons/information.png" ) );
        varsInfoButton.setContentAreaFilled( false );
        varsInfoButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        varsInfoButton.setFocusable( false );
        varsInfoButton.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent arg0 ) {

                new HelpDialog( varsHelpPath );
            }
        } );
        c2.gridx++;
        varsTabComponent.add( varsInfoButton, c2 );

        // Add the panel
        setLayout( new GridBagLayout( ) );
        c = new GridBagConstraints( );
        c.insets = new Insets( 5, 5, 5, 5 );
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        add( mainPanel, c );

        // Set the size, position and properties of the dialog
        setResizable( false );
        setSize( 500, 400 );
        Dimension screenSize = Toolkit.getDefaultToolkit( ).getScreenSize( );
        setLocation( ( screenSize.width - getWidth( ) ) / 2, ( screenSize.height - getHeight( ) ) / 2 );
        setVisible( true );
    }

    /**
     * Table model to display the flags.
     */
    private class FlagsTableModel extends AbstractTableModel {

        /**
         * Required.
         */
        private static final long serialVersionUID = 1L;

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount( ) {

            // Two columns, always
            return 2;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount( ) {

            return varFlagsController.getFlagCount( );
        }

        @Override
        public String getColumnName( int columnIndex ) {

            String columnName = "";

            // The first column is the name
            if( columnIndex == 0 )
                columnName = TC.get( "Flags.FlagName" );

            // The second one the references number
            else if( columnIndex == 1 )
                columnName = TC.get( "Flags.FlagReferences" );

            return columnName;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt( int rowIndex, int columnIndex ) {

            Object value = null;

            // The first column has the name
            if( columnIndex == 0 )
                value = varFlagsController.getFlag( rowIndex );

            // The second one the references number
            else if( columnIndex == 1 )
                value = varFlagsController.getFlagReferences( rowIndex );

            return value;
        }
    }

    /**
     * Table model to display the vars.
     */
    private class VarsTableModel extends AbstractTableModel {

        /**
         * Required.
         */
        private static final long serialVersionUID = 1L;

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount( ) {

            // Two columns, always
            return 2;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount( ) {

            return varFlagsController.getVarCount( );
        }

        @Override
        public String getColumnName( int columnIndex ) {

            String columnName = "";

            // The first column is the name
            if( columnIndex == 0 )
                columnName = TC.get( "Vars.VarName" );

            // The second one the references number
            else if( columnIndex == 1 )
                columnName = TC.get( "Vars.VarReferences" );

            return columnName;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt( int rowIndex, int columnIndex ) {

            Object value = null;

            // The first column has the name
            if( columnIndex == 0 )
                value = varFlagsController.getVar( rowIndex );

            // The second one the references number
            else if( columnIndex == 1 )
                value = varFlagsController.getVarReferences( rowIndex );

            return value;
        }
    }
}
