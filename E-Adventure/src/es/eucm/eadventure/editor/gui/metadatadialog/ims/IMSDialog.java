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
package es.eucm.eadventure.editor.gui.metadatadialog.ims;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.metadata.ims.IMSDataControl;
import es.eucm.eadventure.editor.gui.editdialogs.HelpDialog;

public class IMSDialog extends JDialog {

    private static final String helpPath = "metadata/IMSLOM.html";

    private IMSDataControl dataControl;

    private JTabbedPane tabs;

    private JButton ok;

    private int currentTab;

    public IMSDialog( IMSDataControl dataControl ) {

        super( Controller.getInstance( ).peekWindow( ), TC.get( "LOM.Title" ), Dialog.ModalityType.TOOLKIT_MODAL );
        this.dataControl = dataControl;

        tabs = new JTabbedPane( );

        JButton infoButton = new JButton( new ImageIcon( "img/icons/information.png" ) );
        infoButton.setContentAreaFilled( false );
        infoButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        infoButton.setFocusable( false );
        infoButton.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent arg0 ) {

                new HelpDialog( helpPath );
            }
        } );

        tabs.insertTab( TC.get( "LOM.General.Tab" ), null, new IMSGeneralPanel( dataControl.getGeneral( ) ), TC.get( "LOM.General.Tip" ), 0 );
        tabs.insertTab( TC.get( "LOM.LifeCycle.Tab" ) + " & " + TC.get( "LOM.Technical.Tab" ) + " & " + TC.get( "IMS.Meta.Tab" ), null, new IMSLifeCycleTechnicalAndMetaPanel( dataControl.getLifeCycle( ), dataControl.getTechnical( ), dataControl.getMetametadata( ) ), TC.get( "LOM.LifeCycle.Tip" ) + " & " + TC.get( "LOM.Technical.Tip" ) + " & " + TC.get( "IMS.Meta.Tip" ), 1 );
        tabs.insertTab( TC.get( "LOM.Educational.Tab" ), null, new IMSEducationalPanel( dataControl.getEducational( ) ), TC.get( "LOM.Educational.Tip" ), 2 );
        tabs.insertTab( TC.get( "IMS.Rights.Tab" ) + " & " + TC.get( "IMS.Classification.Tab" ), null, new IMSRightsAndClassificationPanel( dataControl.getRights( ), dataControl.getClassification( ) ), TC.get( "IMS.Rights.Tip" ) + " & " + TC.get( "IMS.Classification.Tip" ), 3 );
        tabs.add( new JPanel( ), 4 );
        tabs.setTabComponentAt( 4, infoButton );
        tabs.addChangeListener( new ChangeListener( ) {

            public void stateChanged( ChangeEvent e ) {

                if( tabs.getSelectedIndex( ) == 4 ) {
                    tabs.setSelectedIndex( currentTab );
                }
                else
                    currentTab = tabs.getSelectedIndex( );
            }

        } );
        tabs.setSelectedIndex( 1 );
        currentTab = 1;
        // create button to close the dialog
        ok = new JButton( "OK" );
        ok.setToolTipText( "Close the window" );
        ok.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent e ) {

                close( );
            }
        } );

        //dialogCont.add(ok,BorderLayout.SOUTH);

        JPanel cont = new JPanel( );
        cont.setLayout( new GridBagLayout( ) );
        GridBagConstraints c = new GridBagConstraints( );
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridy = 0;
        cont.add( tabs, c );
        c.fill = GridBagConstraints.NONE;
        c.gridy = 1;
        c.ipady = 0;
        cont.add( ok, c );
        // Set size and position and show the dialog
        this.getContentPane( ).add( cont );
        setMinimumSize( new Dimension( 490, 450 ) );
        Dimension screenSize = Toolkit.getDefaultToolkit( ).getScreenSize( );
        setLocation( ( screenSize.width - getWidth( ) ) / 2, ( screenSize.height - getHeight( ) ) / 2 );
        setVisible( true );

    }

    public void close( ) {

        this.dispose( );
    }

}
