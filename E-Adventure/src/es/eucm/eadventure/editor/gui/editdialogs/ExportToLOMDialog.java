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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.Controller;

public class ExportToLOMDialog extends JDialog {

    private boolean validated;

    private String lomName;

    private String authorName;

    private String organizationName;

    private JTextField lomNameTextField;

    private JTextField authorNameTextField;

    private JTextField organizationTextField;

    private JCheckBox windowedCheckBox;

    private JComboBox typeComboBox;

    private boolean windowed = false;

    public ExportToLOMDialog( String defaultLomName ) {

        // Set the values
        super( Controller.getInstance( ).peekWindow( ), TC.get( "Operation.ExportToLOM.Title" ), Dialog.ModalityType.APPLICATION_MODAL );

        this.lomName = defaultLomName;

        this.getContentPane( ).setLayout( new GridBagLayout( ) );
        GridBagConstraints c = new GridBagConstraints( );
        c.insets = new Insets( 5, 5, 5, 5 );
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;

        JPanel typePanel = new JPanel( );
        String[] options = { "IMS CP", "WebCT 4 CP", "SCORM", "SCORM2004", "AGREGA" };
        typeComboBox = new JComboBox( options );
        typePanel.add( typeComboBox );
        typePanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TC.get( "Operation.ExportToLOM.LOMType" ) ) );

        //LOM NAME PANEL
        JPanel lomNamePanel = new JPanel( );
        lomNamePanel.setLayout( new GridBagLayout( ) );
        lomNamePanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TC.get( "Operation.ExportToLOM.LOMName" ) ) );
        lomNameTextField = new JTextField( defaultLomName );
        lomNameTextField.getDocument( ).addDocumentListener( new TextFieldListener( lomNameTextField ) );
        JTextPane lomNameDescription = new JTextPane( );
        lomNameDescription.setEditable( false );
        lomNameDescription.setBackground( getContentPane( ).getBackground( ) );
        lomNameDescription.setText( TC.get( "Operation.ExportToLOM.LOMName.Description" ) );
        GridBagConstraints c2 = new GridBagConstraints( );
        c2.insets = new Insets( 5, 5, 5, 5 );
        c.gridy = 0;
        c2.gridy = 0;
        c2.fill = GridBagConstraints.BOTH;
        c2.weightx = 1;
        lomNamePanel.add( lomNameDescription, c2 );
        c2.gridy = 1;
        lomNamePanel.add( lomNameTextField, c2 );

        //Credentials panel
        JPanel credentialsPanel = new JPanel( );
        credentialsPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TC.get( "Operation.ExportToLOM.Credentials" ) ) );
        credentialsPanel.setLayout( new GridBagLayout( ) );
        c2 = new GridBagConstraints( );
        c2.insets = new Insets( 5, 5, 5, 5 );
        c2.fill = GridBagConstraints.BOTH;
        c2.weightx = 1;
        c2.gridy = 0;
        JTextPane credentialsDescription = new JTextPane( );
        credentialsDescription.setEditable( false );
        credentialsDescription.setBackground( getContentPane( ).getBackground( ) );
        credentialsDescription.setText( TC.get( "Operation.ExportToLOM.Credentials.Description" ) );
        credentialsPanel.add( credentialsDescription, c2 );

        //Author name
        JPanel authorNamePanel = new JPanel( );
        authorNamePanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TC.get( "Operation.ExportToLOM.AuthorName" ) ) );
        authorNamePanel.setLayout( new GridBagLayout( ) );
        GridBagConstraints c3 = new GridBagConstraints( );
        c3.insets = new Insets( 2, 2, 2, 2 );
        c3.weightx = 1;
        c3.fill = GridBagConstraints.BOTH;
        authorNameTextField = new JTextField( "" );
        authorNameTextField.getDocument( ).addDocumentListener( new TextFieldListener( authorNameTextField ) );
        authorNamePanel.add( authorNameTextField, c3 );
        c2.gridy = 1;
        credentialsPanel.add( authorNamePanel, c2 );

        //Organization name
        JPanel organizationNamePanel = new JPanel( );
        organizationNamePanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TC.get( "Operation.ExportToLOM.OrganizationName" ) ) );
        organizationNamePanel.setLayout( new GridBagLayout( ) );
        c3 = new GridBagConstraints( );
        c3.insets = new Insets( 2, 2, 2, 2 );
        c3.weightx = 1;
        c3.fill = GridBagConstraints.BOTH;
        organizationTextField = new JTextField( "" );
        organizationTextField.getDocument( ).addDocumentListener( new TextFieldListener( organizationTextField ) );
        organizationNamePanel.add( organizationTextField, c3 );
        c2.gridy = 2;
        credentialsPanel.add( organizationNamePanel, c2 );

        //Applet properties panel
        JPanel lomAppletPanel = new JPanel( );
        lomAppletPanel.setLayout( new GridBagLayout( ) );
        lomAppletPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TC.get( "Operation.ExportToLOM.LOMAppletProperties" ) ) );
        //lomNameTextField = new JTextField(defaultLomName);
        windowedCheckBox = new JCheckBox( TC.get( "Operation.ExportToLOM.LOMAppletRunInsideBrowser" ) );
        windowedCheckBox.setSelected( true );
        windowedCheckBox.addChangeListener( new ChangeListener( ) {

            public void stateChanged( ChangeEvent arg0 ) {

                changeWindowedMode( );
            }
        } );
        c2 = new GridBagConstraints( );
        c2.insets = new Insets( 5, 5, 5, 5 );
        c.gridy = 0;
        c2.gridy = 0;
        c2.fill = GridBagConstraints.BOTH;
        c2.weightx = 1;
        lomAppletPanel.add( windowedCheckBox, c2 );

        //Button panel
        JPanel buttonPanel = new JPanel( );
        JButton okButton = new JButton( TC.get( "Operation.ExportToLOM.OK" ) );
        okButton.addActionListener( new ActionListener( ) {

            public void actionPerformed( ActionEvent e ) {

                validated = true;
                setVisible( false );
                dispose( );
            }
        } );
        buttonPanel.add( okButton );

        //Add all panels
        this.getContentPane( ).add( typePanel, c );
        c.gridy++;
        this.getContentPane( ).add( lomNamePanel, c );
        c.gridy++;
        this.getContentPane( ).add( credentialsPanel, c );
        c.gridy++;
        this.getContentPane( ).add( lomAppletPanel, c );
        c.gridy++;
        c.anchor = GridBagConstraints.CENTER;
        this.getContentPane( ).add( buttonPanel, c );

        // Add window listener
        addWindowListener( new WindowAdapter( ) {

            @Override
            public void windowClosing( WindowEvent e ) {

                validated = false;
                setVisible( false );
                dispose( );
            }
        } );

        this.setSize( new Dimension( 400, 600 ) );
        Dimension screenSize = Toolkit.getDefaultToolkit( ).getScreenSize( );
        setLocation( ( screenSize.width - getWidth( ) ) / 2, ( screenSize.height - getHeight( ) ) / 2 );
        setResizable( false );
        setVisible( true );
    }

    protected void changeWindowedMode( ) {

        windowed = !windowedCheckBox.isSelected( );
    }

    private class TextFieldListener implements DocumentListener {

        private JTextField textField;

        public TextFieldListener( JTextField textField ) {

            this.textField = textField;
        }

        public void changedUpdate( DocumentEvent e ) {

            //Do nothing
        }

        public void insertUpdate( DocumentEvent e ) {

            if( textField == lomNameTextField ) {
                lomName = textField.getText( );
            }
            else if( textField == authorNameTextField ) {
                authorName = textField.getText( );
            }
            else if( textField == organizationTextField ) {
                organizationName = textField.getText( );
            }
        }

        public void removeUpdate( DocumentEvent e ) {

            insertUpdate( e );
        }

    }

    /**
     * @return the lomName
     */
    public String getLomName( ) {

        return lomName;
    }

    /**
     * @return the authorName
     */
    public String getAuthorName( ) {

        return authorName;
    }

    /**
     * @return the organizationName
     */
    public String getOrganizationName( ) {

        return organizationName;
    }

    public int getType( ) {

        return typeComboBox.getSelectedIndex( );
    }

    /**
     * @return the validated
     */
    public boolean isValidated( ) {

        return validated;
    }

    /**
     * @param validated
     *            the validated to set
     */
    public void setValidated( boolean validated ) {

        this.validated = validated;
    }

    public boolean getWindowed( ) {

        return windowed;
    }
}
